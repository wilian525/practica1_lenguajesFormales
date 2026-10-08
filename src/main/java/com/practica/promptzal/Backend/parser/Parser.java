/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.parser;

import com.practica.promptzal.Backend.ast.Agente;
import com.practica.promptzal.Backend.ast.Comando;
import com.practica.promptzal.Backend.ast.ConectorComando;
import com.practica.promptzal.Backend.ast.Directiva;
import com.practica.promptzal.Backend.ast.Ejecucion;
import com.practica.promptzal.Backend.ast.Exportacion;
import com.practica.promptzal.Backend.ast.Expresiones;
import com.practica.promptzal.Backend.ast.Programa;
import com.practica.promptzal.Backend.ast.Termino;
import com.practica.promptzal.Backend.ast.Variable;
import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import java.util.ArrayList;

/**
 *
 * @author wilian
 */
public class Parser {

    private ArrayList<Token> tokens;
    private ArrayList<ErrorSintactico> errores;
    private int posicion;

    public Parser(ArrayList<Token> tokens) {
        if (tokens == null || tokens.isEmpty()) {
            throw new IllegalArgumentException("La lista de tokens esta vacia");
        }

        this.tokens = tokens;
        this.errores = new ArrayList<>();
        this.posicion = 0;
    }

    public Programa parsear() {
        posicion = 0;
        errores.clear();
        return programa();
    }

    public ArrayList<ErrorSintactico> getErrores() {
        return errores;
    }

    // <programa> -> <directivas> <agentes> <ejecuciones> <exportar> EOF
    private Programa programa() {
        Programa programa = new Programa();

        directivas(programa);
        agentes(programa);
        ejecuciones(programa);
        exportar(programa);
        consumir(TipoToken.EOF, "EOF");

        return programa;
    }

    // <directivas> -> DIRECTIVA CADENA <directivas> | epsilon
    private void directivas(Programa programa) {
        if (!verificar(TipoToken.DIRECTIVA)) {
            return;
        }

        Token directiva = avanzar();

        Token valor = consumir(
                TipoToken.CADENA,
                "CADENA",
                TipoToken.DIRECTIVA,
                TipoToken.AGENTE
        );

        if (valor != null) {
            programa.agregarDirectiva(
                    new Directiva(
                            directiva.getLexema(),
                            quitarComillas(valor.getLexema())
                    )
            );
        }

        directivas(programa);
    }

    // <agentes> -> <agente> <mas_agentes>
    private void agentes(Programa programa) {
        if (!verificar(TipoToken.AGENTE)) {
            registrarError("AGENTE");

            sincronizar(
                    TipoToken.AGENTE,
                    TipoToken.EJECUTAR,
                    TipoToken.EXPORTAR
            );
        }

        if (verificar(TipoToken.AGENTE)) {
            Agente agente = agente();

            if (agente != null) {
                programa.agregarAgente(agente);
            }

            masAgentes(programa);
        }
    }

    // <mas_agentes> -> <agente> <mas_agentes> | epsilon
    private void masAgentes(Programa programa) {
        if (!verificar(TipoToken.AGENTE)) {
            return;
        }

        Agente agente = agente();

        if (agente != null) {
            programa.agregarAgente(agente);
        }

        masAgentes(programa);
    }

    // <agente> -> AGENTE ID LLAVE_A <contexto> <variables> <comandos> LLAVE_C
    private Agente agente() {
        Token inicio = consumir(
                TipoToken.AGENTE,
                "AGENTE"
        );

        Token nombre = consumir(
                TipoToken.ID,
                "ID",
                TipoToken.LLAVE_A,
                TipoToken.CONTEXTO
        );

        consumir(
                TipoToken.LLAVE_A,
                "LLAVE_A",
                TipoToken.CONTEXTO,
                TipoToken.VARIABLE,
                TipoToken.COMANDO_IA
        );

        String textoContexto = contexto();

        int linea = inicio != null
                ? inicio.getFila()
                : actual().getFila();

        String nombreAgente = nombre != null
                ? nombre.getLexema()
                : "";

        Agente agente = new Agente(
                nombreAgente,
                textoContexto,
                linea
        );

        variables(agente);
        comandos(agente);

        consumir(
                TipoToken.LLAVE_C,
                "LLAVE_C",
                TipoToken.AGENTE,
                TipoToken.EJECUTAR,
                TipoToken.EXPORTAR
        );

        return agente;
    }

    // <contexto> -> CONTEXTO IGUAL CADENA
    private String contexto() {
        consumir(
                TipoToken.CONTEXTO,
                "CONTEXTO",
                TipoToken.IGUAL,
                TipoToken.CADENA,
                TipoToken.VARIABLE,
                TipoToken.COMANDO_IA,
                TipoToken.LLAVE_C
        );

        consumir(
                TipoToken.IGUAL,
                "IGUAL",
                TipoToken.CADENA,
                TipoToken.VARIABLE,
                TipoToken.COMANDO_IA,
                TipoToken.LLAVE_C
        );

        Token cadena = consumir(
                TipoToken.CADENA,
                "CADENA",
                TipoToken.VARIABLE,
                TipoToken.COMANDO_IA,
                TipoToken.LLAVE_C
        );

        if (cadena == null) {
            return "";
        }

        return quitarComillas(cadena.getLexema());
    }

    // <variables> -> VARIABLE ID IGUAL <expresion> <variables> | epsilon
    private void variables(Agente agente) {
        if (!verificar(TipoToken.VARIABLE)) {
            return;
        }

        Token inicio = avanzar();

        Token nombre = consumir(
                TipoToken.ID,
                "ID",
                TipoToken.IGUAL,
                TipoToken.CADENA,
                TipoToken.NUMERO,
                TipoToken.CARGAR
        );

        consumir(
                TipoToken.IGUAL,
                "IGUAL",
                TipoToken.CADENA,
                TipoToken.ID,
                TipoToken.NUMERO,
                TipoToken.CARGAR
        );

        Expresiones expresion = expresion();

        if (nombre != null && expresion != null) {
            agente.agregarVariable(
                    new Variable(
                            nombre.getLexema(),
                            expresion,
                            inicio.getFila()
                    )
            );
        }

        variables(agente);
    }

    // <comandos> -> <comando> <mas_comandos>
    private void comandos(Agente agente) {
        if (!verificar(TipoToken.COMANDO_IA)) {
            registrarError("COMANDO_IA");

            sincronizar(
                    TipoToken.COMANDO_IA,
                    TipoToken.LLAVE_C,
                    TipoToken.EJECUTAR,
                    TipoToken.EXPORTAR
            );
        }

        if (verificar(TipoToken.COMANDO_IA)) {
            Comando comando = comando();

            if (comando != null) {
                agente.agregarComando(comando);
            }

            masComandos(agente);
        }
    }

    // <mas_comandos> -> <comando> <mas_comandos> | epsilon
    private void masComandos(Agente agente) {
        if (!verificar(TipoToken.COMANDO_IA)) {
            return;
        }

        Comando comando = comando();

        if (comando != null) {
            agente.agregarComando(comando);
        }

        masComandos(agente);
    }

    // <comando> -> COMANDO_IA <expresion> <conectores> FLECHA ID
    private Comando comando() {
        Token tipo = consumir(
                TipoToken.COMANDO_IA,
                "COMANDO_IA"
        );

        Expresiones expresionPrincipal = expresion();

        ArrayList<ConectorComando> listaConectores = conectores();

        consumir(
                TipoToken.FLECHA,
                "FLECHA",
                TipoToken.ID,
                TipoToken.COMANDO_IA,
                TipoToken.LLAVE_C
        );

        Token destino = consumir(
                TipoToken.ID,
                "ID",
                TipoToken.COMANDO_IA,
                TipoToken.LLAVE_C,
                TipoToken.EJECUTAR,
                TipoToken.EXPORTAR
        );

        if (tipo == null
                || expresionPrincipal == null
                || destino == null) {
            return null;
        }

        Comando comando = new Comando(
                tipo.getLexema(),
                expresionPrincipal,
                destino.getLexema(),
                tipo.getFila()
        );

        for (ConectorComando conector : listaConectores) {
            comando.agregarConector(conector);
        }

        return comando;
    }

    // <conectores> -> CONECTOR <expresion> <conectores> | epsilon
    private ArrayList<ConectorComando> conectores() {
        ArrayList<ConectorComando> lista = new ArrayList<>();

        if (!verificar(TipoToken.CONECTOR)) {
            return lista;
        }

        Token conector = avanzar();

        Expresiones expresion = expresion();

        if (expresion != null) {
            lista.add(
                    new ConectorComando(
                            conector.getLexema(),
                            expresion
                    )
            );
        }

        lista.addAll(conectores());

        return lista;
    }

    // <expresion> -> <termino> <mas_terminos>
    private Expresiones expresion() {
        Expresiones expresion = new Expresiones();

        Termino termino = termino();

        if (termino == null) {
            return null;
        }

        expresion.agregarTermino(termino);

        masTerminos(expresion);

        return expresion;
    }

    // <mas_terminos> -> MAS <termino> <mas_terminos> | epsilon
    private void masTerminos(Expresiones expresion) {
        if (!verificar(TipoToken.MAS)) {
            return;
        }

        avanzar();

        Termino termino = termino();

        if (termino != null) {
            expresion.agregarTermino(termino);
        }

        masTerminos(expresion);
    }

    // <termino> -> CADENA | ID | NUMERO <unidad> | <cargar>
    private Termino termino() {
        if (verificar(TipoToken.CADENA)) {
            Token token = avanzar();

            return new Termino(
                    "CADENA",
                    quitarComillas(token.getLexema()),
                    token.getFila()
            );
        }

        if (verificar(TipoToken.ID)) {
            Token token = avanzar();

            return new Termino(
                    "ID",
                    token.getLexema(),
                    token.getFila()
            );
        }

        if (verificar(TipoToken.NUMERO)) {
            Token token = avanzar();

            String unidad = unidad();

            if (unidad == null) {
                return new Termino(
                        "NUMERO",
                        token.getLexema(),
                        token.getFila()
                );
            }

            return new Termino(
                    "NUMERO",
                    token.getLexema(),
                    unidad,
                    token.getFila()
            );
        }

        if (verificar(TipoToken.CARGAR)) {
            return cargar();
        }

        registrarError(
                "CADENA, ID, NUMERO o CARGAR"
        );

        sincronizar(
                TipoToken.MAS,
                TipoToken.CONECTOR,
                TipoToken.FLECHA,
                TipoToken.VARIABLE,
                TipoToken.COMANDO_IA,
                TipoToken.LLAVE_C,
                TipoToken.EJECUTAR,
                TipoToken.EXPORTAR
        );

        return null;
    }

    // <unidad> -> ID | epsilon
    private String unidad() {
        if (!verificar(TipoToken.ID)) {
            return null;
        }

        return avanzar().getLexema();
    }

    // <cargar> -> CARGAR PAR_A CADENA PAR_C
    private Termino cargar() {
        Token cargar = consumir(
                TipoToken.CARGAR,
                "CARGAR"
        );

        consumir(
                TipoToken.PAR_A,
                "PAR_A",
                TipoToken.CADENA,
                TipoToken.PAR_C
        );

        Token archivo = consumir(
                TipoToken.CADENA,
                "CADENA",
                TipoToken.PAR_C,
                TipoToken.MAS,
                TipoToken.CONECTOR,
                TipoToken.FLECHA
        );

        consumir(
                TipoToken.PAR_C,
                "PAR_C",
                TipoToken.MAS,
                TipoToken.CONECTOR,
                TipoToken.FLECHA,
                TipoToken.VARIABLE,
                TipoToken.COMANDO_IA,
                TipoToken.LLAVE_C
        );

        if (cargar == null || archivo == null) {
            return null;
        }

        return new Termino(
                "CARGAR",
                quitarComillas(archivo.getLexema()),
                cargar.getFila()
        );
    }

    // <ejecuciones> -> EJECUTAR ID <mas_ejecuciones>
    private void ejecuciones(Programa programa) {
        if (!verificar(TipoToken.EJECUTAR)) {
            registrarError("EJECUTAR");

            sincronizar(
                    TipoToken.EJECUTAR,
                    TipoToken.EXPORTAR
            );
        }

        if (verificar(TipoToken.EJECUTAR)) {
            Token ejecutar = avanzar();

            Token agente = consumir(
                    TipoToken.ID,
                    "ID",
                    TipoToken.EJECUTAR,
                    TipoToken.EXPORTAR
            );

            if (agente != null) {
                programa.agregarEjecucion(
                        new Ejecucion(
                                agente.getLexema(),
                                ejecutar.getFila()
                        )
                );
            }

            masEjecuciones(programa);
        }
    }

    // <mas_ejecuciones> -> EJECUTAR ID <mas_ejecuciones> | epsilon
    private void masEjecuciones(Programa programa) {
        if (!verificar(TipoToken.EJECUTAR)) {
            return;
        }

        Token ejecutar = avanzar();

        Token agente = consumir(
                TipoToken.ID,
                "ID",
                TipoToken.EJECUTAR,
                TipoToken.EXPORTAR
        );

        if (agente != null) {
            programa.agregarEjecucion(
                    new Ejecucion(
                            agente.getLexema(),
                            ejecutar.getFila()
                    )
            );
        }

        masEjecuciones(programa);
    }

    // <exportar> -> EXPORTAR ID <mas_ids>
    private void exportar(Programa programa) {
        if (!verificar(TipoToken.EXPORTAR)) {
            registrarError("EXPORTAR");

            sincronizar(
                    TipoToken.EXPORTAR,
                    TipoToken.EOF
            );
        }

        if (!verificar(TipoToken.EXPORTAR)) {
            return;
        }

        Token exportar = avanzar();

        Token variable = consumir(
                TipoToken.ID,
                "ID",
                TipoToken.COMA,
                TipoToken.EOF
        );

        if (variable != null) {
            programa.agregarExportacion(
                    new Exportacion(
                            variable.getLexema(),
                            exportar.getFila()
                    )
            );
        }

        masIds(programa);
    }

    // <mas_ids> -> COMA ID <mas_ids> | epsilon
    private void masIds(Programa programa) {
        if (!verificar(TipoToken.COMA)) {
            return;
        }

        Token coma = avanzar();

        Token variable = consumir(
                TipoToken.ID,
                "ID",
                TipoToken.COMA,
                TipoToken.EOF
        );

        if (variable != null) {
            programa.agregarExportacion(
                    new Exportacion(
                            variable.getLexema(),
                            coma.getFila()
                    )
            );
        }

        masIds(programa);
    }

    private Token consumir(
            TipoToken esperado,
            String descripcion,
            TipoToken... sincronizacion) {

        if (verificar(esperado)) {
            return avanzar();
        }

        registrarError(descripcion);

        sincronizar(sincronizacion);

        if (verificar(esperado)) {
            return avanzar();
        }

        return null;
    }

    private void registrarError(String esperado) {
        Token token = actual();

        errores.add(
                new ErrorSintactico(
                        token.getLexema(),
                        esperado,
                        token.getFila(),
                        token.getColumna()
                )
        );
    }

    private void sincronizar(TipoToken... adicionales) {
        while (!verificar(TipoToken.EOF)
                && !esTokenSincronizacion(
                        actual().getTipo(),
                        adicionales
                )) {

            avanzar();
        }
    }

    private boolean esTokenSincronizacion(
            TipoToken tipo,
            TipoToken... adicionales) {

        if (tipo == TipoToken.AGENTE
                || tipo == TipoToken.LLAVE_C
                || tipo == TipoToken.COMANDO_IA
                || tipo == TipoToken.VARIABLE
                || tipo == TipoToken.EJECUTAR
                || tipo == TipoToken.EXPORTAR) {

            return true;
        }

        for (TipoToken adicional : adicionales) {
            if (tipo == adicional) {
                return true;
            }
        }

        return false;
    }

    private boolean verificar(TipoToken tipo) {
        return actual().getTipo() == tipo;
    }

    private Token avanzar() {
        Token token = actual();

        if (posicion < tokens.size() - 1) {
            posicion++;
        }

        return token;
    }

    private Token actual() {
        return tokens.get(posicion);
    }

    private String quitarComillas(String texto) {
        if (texto != null
                && texto.length() >= 2
                && texto.charAt(0) == '"'
                && texto.charAt(texto.length() - 1) == '"') {

            return texto.substring(
                    1,
                    texto.length() - 1
            );
        }

        return texto;
    }
}
