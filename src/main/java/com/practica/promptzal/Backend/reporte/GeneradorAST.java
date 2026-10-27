/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.ast.Agente;
import com.practica.promptzal.Backend.ast.Comando;
import com.practica.promptzal.Backend.ast.ConectorComando;
import com.practica.promptzal.Backend.ast.Directiva;
import com.practica.promptzal.Backend.ast.Ejecucion;
import com.practica.promptzal.Backend.ast.Expresiones;
import com.practica.promptzal.Backend.ast.Exportacion;
import com.practica.promptzal.Backend.ast.Programa;
import com.practica.promptzal.Backend.ast.Termino;
import com.practica.promptzal.Backend.ast.Variable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author wilian
 */
public class GeneradorAST {

    private String rutaCarpeta;
    private String rutaDot;
    private String rutaImagen;
    private int contador;

    public GeneradorAST() {
        rutaCarpeta = "reportes";
        rutaDot = rutaCarpeta + "/ast_promptzal.dot";
        rutaImagen = rutaCarpeta + "/ast_promptzal.png";
        contador = 0;
    }

    public boolean generar(Programa programa) {
        if (programa == null) {
            return false;
        }

        crearCarpeta();

        boolean generadoDot = generarArchivoDot(programa);

        if (!generadoDot) {
            return false;
        }

        return generarImagen();
    }

    private void crearCarpeta() {
        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    private boolean generarArchivoDot(Programa programa) {
        contador = 0;

        String contenido = construirDot(programa);

        try (BufferedWriter escritor= new BufferedWriter(new FileWriter(rutaDot))) {

            escritor.write(contenido);
            return true;

        } catch (IOException e) {
            System.out.println("Error al generar el DOT del AST: " + e.getMessage() );

            return false;
        }
    }

    private String construirDot(Programa programa) {
        StringBuilder dot = new StringBuilder();

        dot.append("digraph AST_PromptZal {\n");
        dot.append("rankdir=TB;\n");
        dot.append("labelloc=\"t\";\n");
        dot.append("label=\"AST - PromptZal\";\n");
        dot.append("fontsize=20;\n");
        dot.append("node [shape=box, style=\"rounded\"];\n\n");

        String nodoPrograma = crearNodo(dot,"Programa" );

        agregarDirectivas(programa,nodoPrograma, dot);

        agregarAgentes(programa,nodoPrograma, dot );

        agregarEjecuciones(programa, nodoPrograma, dot);

        agregarExportaciones(programa,nodoPrograma,dot);

        dot.append("}\n");

        return dot.toString();
    }

    private void agregarDirectivas(Programa programa, String padre,StringBuilder dot) {

        if (programa.getDirectivas().isEmpty()) {
            return;
        }

        String nodoDirectivas = crearNodo(dot, "Directivas" );

        conectar(dot, padre, nodoDirectivas);

        for (Directiva directiva : programa.getDirectivas()) {

            String nodo = crearNodo(dot,  directiva.getNombre()+ "\\n" + directiva.getValor());

            conectar(dot, nodoDirectivas, nodo );
        }
    }

    private void agregarAgentes(Programa programa,String padre,StringBuilder dot) {

        if (programa.getAgentes().isEmpty()) {
            return;
        }

        String nodoAgentes = crearNodo(  dot,"Agentes" );

        conectar(dot, padre, nodoAgentes);

        for (Agente agente : programa.getAgentes()) {
            agregarAgente(agente,nodoAgentes,dot);
        }
    }

    private void agregarAgente( Agente agente, String padre, StringBuilder dot) {

        String nodoAgente = crearNodo(dot,"Agente\\n" + agente.getNombre());

        conectar(dot, padre, nodoAgente);

        String nodoContexto = crearNodo( dot, "Contexto\\n"+ agente.getContexto());

        conectar( dot,nodoAgente, nodoContexto);

        agregarVariables(agente, nodoAgente, dot );

        agregarComandos(agente,nodoAgente,dot );
    }

    private void agregarVariables(Agente agente,String padre,StringBuilder dot) {

        if (agente.getVariables().isEmpty()) {
            return;
        }

        String nodoVariables = crearNodo( dot,"Variables");

        conectar(dot, padre, nodoVariables);

        for (Variable variable : agente.getVariables()) {

            String nodoVariable = crearNodo( dot,"Variable\\n"+ variable.getNombre() );

            conectar( dot,nodoVariables, nodoVariable);

            agregarExpresion( variable.getExpresion(),nodoVariable, dot);
        }
    }

    private void agregarComandos( Agente agente,String padre,StringBuilder dot) {

        if (agente.getComandos().isEmpty()) {
            return;
        }

        String nodoComandos = crearNodo(dot,"Comandos" );

        conectar(dot, padre, nodoComandos);

        for (Comando comando : agente.getComandos()) {

            String nodoComando = crearNodo(dot,"Comando\\n"+ comando.getTipo());

            conectar( dot, nodoComandos,nodoComando );

            String nodoPrincipal = crearNodo( dot, "Expresion principal");

            conectar(dot,nodoComando, nodoPrincipal );

            agregarExpresion( comando.getExpresionPrincipal(), nodoPrincipal, dot );

            agregarConectores(comando,nodoComando, dot);

            String nodoDestino = crearNodo(dot,"Destino\\n"+ comando.getVariableDestino());

            conectar(dot, nodoComando, nodoDestino );
        }
    }

    private void agregarConectores(
            Comando comando,
            String padre,
            StringBuilder dot) {

        if (comando.getConectores().isEmpty()) {
            return;
        }

        String nodoConectores = crearNodo(dot, "Conectores" );

        conectar(dot, padre, nodoConectores);

        for (ConectorComando conector : comando.getConectores()) {

            String nodoConector = crearNodo( dot, conector.getTipo());

            conectar(dot,nodoConectores, nodoConector);

            agregarExpresion(conector.getExpresion(), nodoConector,dot );
        }
    }

    private void agregarExpresion(
            Expresiones expresion,
            String padre,
            StringBuilder dot) {

        if (expresion == null) {
            return;
        }

        String nodoExpresion = crearNodo(
                dot,
                "Expresion"
        );

        conectar(dot, padre, nodoExpresion);

        for (Termino termino : expresion.getTerminos()) {

            String texto = termino.getTipo()
                    + "\\n"
                    + termino.getValor();

            if (termino.getUnidad() != null) {
                texto += " " + termino.getUnidad();
            }

            String nodoTermino = crearNodo(
                    dot,
                    texto
            );

            conectar(
                    dot,
                    nodoExpresion,
                    nodoTermino
            );
        }
    }

    private void agregarEjecuciones(
            Programa programa,
            String padre,
            StringBuilder dot) {

        if (programa.getEjecuciones().isEmpty()) {
            return;
        }

        String nodoEjecuciones = crearNodo(
                dot,
                "Ejecuciones"
        );

        conectar(
                dot,
                padre,
                nodoEjecuciones
        );

        for (Ejecucion ejecucion
                : programa.getEjecuciones()) {

            String nodo = crearNodo(
                    dot,
                    "EJECUTAR\\n"
                    + ejecucion.getNombreAgente()
            );

            conectar(
                    dot,
                    nodoEjecuciones,
                    nodo
            );
        }
    }

    private void agregarExportaciones(
            Programa programa,
            String padre,
            StringBuilder dot) {

        if (programa.getExportaciones().isEmpty()) {
            return;
        }

        String nodoExportaciones = crearNodo(
                dot,
                "Exportaciones"
        );

        conectar(
                dot,
                padre,
                nodoExportaciones
        );

        for (Exportacion exportacion
                : programa.getExportaciones()) {

            String nodo = crearNodo(
                    dot,
                    "EXPORTAR\\n"
                    + exportacion.getNombreVariable()
            );

            conectar(
                    dot,
                    nodoExportaciones,
                    nodo
            );
        }
    }

    private String crearNodo(
            StringBuilder dot,
            String texto) {

        String id = "nodo" + contador;
        contador++;

        dot.append(id)
                .append(" [label=\"")
                .append(escapar(texto))
                .append("\"];\n");

        return id;
    }

    private void conectar(
            StringBuilder dot,
            String padre,
            String hijo) {

        dot.append(padre)
                .append(" -> ")
                .append(hijo)
                .append(";\n");
    }

    private String escapar(String texto) {
        if (texto == null) {
            return "";
        }

        return texto.replace("\"", "\\\"");
    }

    private boolean generarImagen() {
        try {
            ProcessBuilder proceso = new ProcessBuilder(
                    "dot",
                    "-Tpng",
                    rutaDot,
                    "-o",
                    rutaImagen
            );

            Process ejecucion = proceso.start();

            int resultado = ejecucion.waitFor();

            if (resultado == 0) {
                return true;
            }

            System.out.println(
                    "Graphviz no pudo generar la imagen del AST."
            );

            return false;

        } catch (IOException e) {

            System.out.println(
                    "No se pudo ejecutar Graphviz: "
                    + e.getMessage()
            );

            return false;

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "La generacion del AST fue interrumpida."
            );

            return false;
        }
    }

    public String getRutaDot() {
        return rutaDot;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }
}
