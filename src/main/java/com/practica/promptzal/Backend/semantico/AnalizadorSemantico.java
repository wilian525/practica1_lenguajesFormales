/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.semantico;

import com.practica.promptzal.Backend.ast.Agente;
import com.practica.promptzal.Backend.ast.Comando;
import com.practica.promptzal.Backend.ast.ConectorComando;
import com.practica.promptzal.Backend.ast.Ejecucion;
import com.practica.promptzal.Backend.ast.Exportacion;
import com.practica.promptzal.Backend.ast.Expresiones;
import com.practica.promptzal.Backend.ast.Programa;
import com.practica.promptzal.Backend.ast.Termino;
import com.practica.promptzal.Backend.ast.Variable;
import java.util.ArrayList;

/**
 *
 * @author wilian
 */
public class AnalizadorSemantico {

    private TablaSimbolos tablaSimbolos;
    private ArrayList<ErrorSemantico> errores;

    public AnalizadorSemantico() {
        tablaSimbolos = new TablaSimbolos();
        errores = new ArrayList<>();
    }

    public void analizar(Programa programa) {
        tablaSimbolos.limpiar();
        errores.clear();

        if (programa == null) {
            return;
        }

        registrarAgentes(programa);
        analizarAgentes(programa);
        validarEjecuciones(programa);
        validarExportaciones(programa);
    }

    private void registrarAgentes(Programa programa) {
        for (Agente agente : programa.getAgentes()) {

            if (tablaSimbolos.existeAgente(agente.getNombre())) {
                errores.add(
                        new ErrorSemantico(
                                "El agente '" + agente.getNombre()
                                + "' ya fue declarado.",
                                agente.getLinea()
                        )
                );

                continue;
            }

            tablaSimbolos.agregarSimbolo(
                    new Simbolo(
                            agente.getNombre(),
                            "AGENTE",
                            "GLOBAL",
                            agente.getLinea()
                    )
            );
        }
    }

    private void analizarAgentes(Programa programa) {
        ArrayList<String> agentesAnalizados = new ArrayList<>();

        for (Agente agente : programa.getAgentes()) {

            if (agentesAnalizados.contains(agente.getNombre())) {
                continue;
            }

            agentesAnalizados.add(agente.getNombre());

            analizarVariables(agente);
            analizarComandos(agente);
        }
    }

    private void analizarVariables(Agente agente) {
        for (Variable variable : agente.getVariables()) {

            validarExpresion(
                    variable.getExpresion(),
                    agente.getNombre(),
                    false
            );

            tablaSimbolos.agregarSimbolo(
                    new Simbolo(
                            variable.getNombre(),
                            "VARIABLE_DECLARADA",
                            agente.getNombre(),
                            variable.getLinea()
                    )
            );
        }
    }

    private void analizarComandos(Agente agente) {
        for (Comando comando : agente.getComandos()) {

            boolean ignorarPrimerId
                    = comando.getTipo().equals("GENERAR");

            validarExpresion(
                    comando.getExpresionPrincipal(),
                    agente.getNombre(),
                    ignorarPrimerId
            );

            for (ConectorComando conector : comando.getConectores()) {
                validarExpresion(
                        conector.getExpresion(),
                        agente.getNombre(),
                        false
                );
            }

            tablaSimbolos.agregarSimbolo(
                    new Simbolo(
                            comando.getVariableDestino(),
                            "RESULTADO_COMANDO",
                            agente.getNombre(),
                            comando.getLinea()
                    )
            );
        }
    }

    private void validarExpresion(
            Expresiones expresion,
            String ambito,
            boolean ignorarPrimerId) {

        if (expresion == null) {
            return;
        }

        boolean primerTermino = true;

        for (Termino termino : expresion.getTerminos()) {

            if (termino.getTipo().equals("ID")) {

                if (ignorarPrimerId && primerTermino) {
                    primerTermino = false;
                    continue;
                }

                if (!tablaSimbolos.existeEnAmbito(
                        termino.getValor(),
                        ambito)) {

                    errores.add(
                            new ErrorSemantico(
                                    "El identificador '"
                                    + termino.getValor()
                                    + "' no ha sido declarado antes "
                                    + "en el agente '" + ambito + "'.",
                                    termino.getLinea()
                            )
                    );
                }
            }

            primerTermino = false;
        }
    }

    private void validarEjecuciones(Programa programa) {
        for (Ejecucion ejecucion : programa.getEjecuciones()) {

            if (!tablaSimbolos.existeAgente(
                    ejecucion.getNombreAgente())) {

                errores.add(
                        new ErrorSemantico(
                                "El agente '"
                                + ejecucion.getNombreAgente()
                                + "' usado en EJECUTAR no existe.",
                                ejecucion.getLinea()
                        )
                );
            }
        }
    }

    private void validarExportaciones(Programa programa) {
        for (Exportacion exportacion : programa.getExportaciones()) {

            if (!existeVariableEnAgenteEjecutado(
                    exportacion.getNombreVariable(),
                    programa)) {

                errores.add(
                        new ErrorSemantico(
                                "La variable '"
                                + exportacion.getNombreVariable()
                                + "' usada en EXPORTAR no existe "
                                + "en ningun agente ejecutado.",
                                exportacion.getLinea()
                        )
                );
            }
        }
    }

    private boolean existeVariableEnAgenteEjecutado(
            String nombreVariable,
            Programa programa) {

        for (Ejecucion ejecucion : programa.getEjecuciones()) {

            Simbolo simbolo = tablaSimbolos.buscarEnAmbito(
                    nombreVariable,
                    ejecucion.getNombreAgente()
            );

            if (simbolo != null
                    && (simbolo.getTipo().equals("VARIABLE_DECLARADA")
                    || simbolo.getTipo().equals("RESULTADO_COMANDO"))) {

                return true;
            }
        }

        return false;
    }

    public TablaSimbolos getTablaSimbolos() {
        return tablaSimbolos;
    }

    public ArrayList<ErrorSemantico> getErrores() {
        return errores;
    }
}
