/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.generador;

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

/**
 *
 * @author wilian
 */
public class GeneradorPromptzal {

    public String generar(Programa programa) {
        if (programa == null) {
            return "";
        }

        StringBuilder prompt = new StringBuilder();

        prompt.append("# Prompt generado con PromptZal\n\n");

        agregarDirectivas(programa, prompt);
        agregarAgentesEjecutados(programa, prompt);
        agregarExportaciones(programa, prompt);

        return prompt.toString();
    }

    private void agregarDirectivas(
            Programa programa,
            StringBuilder prompt) {

        String modelo = buscarDirectiva(programa, "@modelo");
        String rol = buscarDirectiva(programa, "@rol");
        String formato = buscarDirectiva(programa, "@formato");

        if (modelo != null) {
            prompt.append("Modelo destino: ")
                    .append(modelo)
                    .append("\n");
        }

        if (rol != null) {
            prompt.append("Rol general: ")
                    .append(rol)
                    .append("\n");
        }

        if (formato != null) {
            prompt.append("Formato de respuesta: ")
                    .append(formato)
                    .append("\n");
        }

        if (modelo != null || rol != null || formato != null) {
            prompt.append("\n");
        }
    }

    private String buscarDirectiva(
            Programa programa,
            String nombre) {

        for (Directiva directiva : programa.getDirectivas()) {
            if (directiva.getNombre().equals(nombre)) {
                return directiva.getValor();
            }
        }

        return null;
    }

    private void agregarAgentesEjecutados(
            Programa programa,
            StringBuilder prompt) {

        for (Ejecucion ejecucion : programa.getEjecuciones()) {

            Agente agente = buscarAgente(
                    programa,
                    ejecucion.getNombreAgente()
            );

            if (agente == null) {
                continue;
            }

            prompt.append("## Agente: ")
                    .append(agente.getNombre())
                    .append("\n\n");

            prompt.append("Contexto: ")
                    .append(agente.getContexto())
                    .append("\n\n");

            agregarVariables(agente, prompt);
            agregarComandos(agente, prompt);
        }
    }

    private Agente buscarAgente(
            Programa programa,
            String nombre) {

        for (Agente agente : programa.getAgentes()) {
            if (agente.getNombre().equals(nombre)) {
                return agente;
            }
        }

        return null;
    }

    private void agregarVariables(
            Agente agente,
            StringBuilder prompt) {

        if (agente.getVariables().isEmpty()) {
            return;
        }

        prompt.append("Variables disponibles:\n");

        for (Variable variable : agente.getVariables()) {
            prompt.append("- ")
                    .append(variable.getNombre())
                    .append(": ")
                    .append(expresionComoValor(variable.getExpresion()))
                    .append("\n");
        }

        prompt.append("\n");
    }

    private void agregarComandos(
            Agente agente,
            StringBuilder prompt) {

        prompt.append("Instrucciones:\n");

        int numero = 1;

        for (Comando comando : agente.getComandos()) {
            prompt.append(numero)
                    .append(". ")
                    .append(generarInstruccion(comando))
                    .append("\n");

            numero++;
        }

        prompt.append("\n");
    }

    private String generarInstruccion(Comando comando) {
        String tipo = comando.getTipo();

        switch (tipo) {
            case "PREGUNTAR":
                return generarPreguntar(comando);

            case "GENERAR":
                return generarGenerar(comando);

            case "RESUMIR":
                return generarResumir(comando);

            case "ANALIZAR":
                return generarAnalizar(comando);

            case "TRADUCIR":
                return generarTraducir(comando);

            case "CLASIFICAR":
                return generarClasificar(comando);

            case "EXTRAER":
                return generarExtraer(comando);

            default:
                return generarGenerico(comando);
        }
    }

    private String generarPreguntar(Comando comando) {
        String pregunta = expresionATexto(
                comando.getExpresionPrincipal()
        );

        String sobre = obtenerConector(
                comando,
                "SOBRE"
        );

        StringBuilder texto = new StringBuilder();

        if (sobre != null) {
            texto.append("Basandote en ")
                    .append(sobre)
                    .append(", ");
        }

        texto.append("responde: ")
                .append(pregunta);

        if (!pregunta.endsWith("?")
                && !pregunta.endsWith("!")
                && !pregunta.endsWith(".")) {

            texto.append(".");
        }

        texto.append(" Guarda la respuesta como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String generarGenerar(Comando comando) {
        String tipoContenido = expresionATexto(
                comando.getExpresionPrincipal()
        );

        String desde = obtenerConector(
                comando,
                "DESDE"
        );

        StringBuilder texto = new StringBuilder();

        texto.append("Genera ")
                .append(tipoContenido);

        if (desde != null) {
            texto.append(" a partir de: ")
                    .append(desde);
        }

        texto.append(". Guarda el resultado como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String generarResumir(Comando comando) {
        String contenido = expresionATexto(
                comando.getExpresionPrincipal()
        );

        String en = obtenerConector(
                comando,
                "EN"
        );

        StringBuilder texto = new StringBuilder();

        texto.append("Resume ")
                .append(contenido);

        if (en != null) {
            texto.append(" en un maximo de ")
                    .append(en);
        }

        texto.append(". Guarda el resultado como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String generarAnalizar(Comando comando) {
        String contenido = expresionATexto(
                comando.getExpresionPrincipal()
        );

        String sobre = obtenerConector(
                comando,
                "SOBRE"
        );

        StringBuilder texto = new StringBuilder();

        texto.append("Analiza ")
                .append(contenido);

        if (sobre != null) {
            texto.append(" considerando: ")
                    .append(sobre);
        }

        texto.append(". Guarda el resultado como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String generarTraducir(Comando comando) {
        String contenido = expresionATexto(
                comando.getExpresionPrincipal()
        );

        String idioma = obtenerConector(
                comando,
                "EN"
        );

        StringBuilder texto = new StringBuilder();

        texto.append("Traduce ")
                .append(contenido);

        if (idioma != null) {
            texto.append(" al idioma ")
                    .append(idioma);
        }

        texto.append(". Guarda el resultado como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String generarClasificar(Comando comando) {
        String contenido = expresionATexto(
                comando.getExpresionPrincipal()
        );

        String categorias = obtenerConector(
                comando,
                "EN"
        );

        StringBuilder texto = new StringBuilder();

        texto.append("Clasifica ")
                .append(contenido);

        if (categorias != null) {
            texto.append(" en las categorias: ")
                    .append(categorias);
        }

        texto.append(". Guarda el resultado como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String generarExtraer(Comando comando) {
        String informacion = expresionATexto(
                comando.getExpresionPrincipal()
        );

        String desde = obtenerConector(
                comando,
                "DESDE"
        );

        StringBuilder texto = new StringBuilder();

        texto.append("Extrae ")
                .append(informacion);

        if (desde != null) {
            texto.append(" a partir de ")
                    .append(desde);
        }

        texto.append(". Guarda el resultado como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String generarGenerico(Comando comando) {
        StringBuilder texto = new StringBuilder();

        texto.append(comando.getTipo())
                .append(" ")
                .append(
                        expresionATexto(
                                comando.getExpresionPrincipal()
                        )
                );

        for (ConectorComando conector
                : comando.getConectores()) {

            if (!conector.getTipo().equals("COMO")) {
                texto.append(" ")
                        .append(conector.getTipo())
                        .append(" ")
                        .append(
                                expresionATexto(
                                        conector.getExpresion()
                                )
                        );
            }
        }

        texto.append(". Guarda el resultado como ")
                .append(comando.getVariableDestino())
                .append(".");

        agregarFormato(comando, texto);

        return texto.toString();
    }

    private String obtenerConector(
            Comando comando,
            String tipo) {

        for (ConectorComando conector
                : comando.getConectores()) {

            if (conector.getTipo().equals(tipo)) {
                return expresionATexto(
                        conector.getExpresion()
                );
            }
        }

        return null;
    }

    private void agregarFormato(
            Comando comando,
            StringBuilder texto) {

        String formato = obtenerConector(
                comando,
                "COMO"
        );

        if (formato != null) {
            texto.append(" Entrega el resultado en formato ")
                    .append(formato)
                    .append(".");
        }
    }

    private String expresionComoValor(Expresiones expresion) {
        if (expresion == null) {
            return "";
        }

        StringBuilder texto = new StringBuilder();

        for (int i = 0;
                i < expresion.getTerminos().size();
                i++) {

            Termino termino = expresion.getTerminos().get(i);

            if (i > 0) {
                texto.append(" + ");
            }

            if (termino.getTipo().equals("CARGAR")) {
                texto.append("contenido del archivo \"")
                        .append(termino.getValor())
                        .append("\" (adjuntar el archivo)");
            } else {
                texto.append(terminoATexto(termino));
            }
        }

        return texto.toString();
    }

    private String expresionATexto(Expresiones expresion) {
        if (expresion == null) {
            return "";
        }

        StringBuilder texto = new StringBuilder();

        for (int i = 0;
                i < expresion.getTerminos().size();
                i++) {

            if (i > 0) {
                texto.append(" ");
            }

            texto.append(
                    terminoATexto(
                            expresion.getTerminos().get(i)
                    )
            );
        }

        return texto.toString();
    }

    private String terminoATexto(Termino termino) {
        if (termino.getTipo().equals("NUMERO")) {

            if (termino.getUnidad() != null) {
                return termino.getValor()
                        + " "
                        + termino.getUnidad();
            }

            return termino.getValor();
        }

        if (termino.getTipo().equals("CARGAR")) {
            return "archivo \"" + termino.getValor() + "\"";
        }

        return termino.getValor();
    }

    private void agregarExportaciones(
            Programa programa,
            StringBuilder prompt) {

        prompt.append("## Resultado solicitado\n\n");

        for (Exportacion exportacion
                : programa.getExportaciones()) {

            prompt.append("- ")
                    .append(exportacion.getNombreVariable())
                    .append("\n");
        }
    }
}
