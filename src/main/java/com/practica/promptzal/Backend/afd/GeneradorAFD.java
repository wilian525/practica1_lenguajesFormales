/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.afd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GeneradorAFD {

    private String rutaCarpeta;
    private String rutaDot;
    private String rutaImagen;

    public GeneradorAFD() {
        rutaCarpeta = "reportes";
        rutaDot = rutaCarpeta + "/afd_promptzal.dot";
        rutaImagen = rutaCarpeta + "/afd_promptzal.png";
    }

    public boolean generar() {
        crearCarpeta();

        boolean dotGenerado = generarArchivoDot();

        if (!dotGenerado) {
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

    private boolean generarArchivoDot() {
        String contenido = construirDot();

        try (BufferedWriter escritor
                = new BufferedWriter(new FileWriter(rutaDot))) {

            escritor.write(contenido);
            return true;

        } catch (IOException e) {
            System.out.println(
                    "Error al generar el archivo DOT del AFD: "
                    + e.getMessage()
            );

            return false;
        }
    }

    private String construirDot() {
        StringBuilder dot = new StringBuilder();

        dot.append("digraph AFD_PromptZal {\n");
        dot.append("rankdir=LR;\n");
        dot.append("labelloc=\"t\";\n");
        dot.append("label=\"AFD General - PromptZal\";\n");
        dot.append("fontsize=20;\n");
        dot.append("node [shape=circle];\n\n");

        // Estado inicial
        dot.append("inicio [shape=point];\n");
        dot.append("q0 [label=\"q0\"];\n");
        dot.append("inicio -> q0;\n\n");

        // Estados de aceptacion
        dot.append("qID [shape=doublecircle, label=\"ID\\nPalabra reservada\"];\n");
        dot.append("qNUM [shape=doublecircle, label=\"NUMERO\"];\n");
        dot.append("qCAD [shape=doublecircle, label=\"CADENA\"];\n");
        dot.append("qDIR [shape=doublecircle, label=\"DIRECTIVA\"];\n");
        dot.append("qIGUAL [shape=doublecircle, label=\"IGUAL\"];\n");
        dot.append("qMAS [shape=doublecircle, label=\"MAS\"];\n");
        dot.append("qFLECHA [shape=doublecircle, label=\"FLECHA\"];\n");
        dot.append("qLLAVEA [shape=doublecircle, label=\"LLAVE_A\"];\n");
        dot.append("qLLAVEC [shape=doublecircle, label=\"LLAVE_C\"];\n");
        dot.append("qPARA [shape=doublecircle, label=\"PAR_A\"];\n");
        dot.append("qPARC [shape=doublecircle, label=\"PAR_C\"];\n");
        dot.append("qCOMA [shape=doublecircle, label=\"COMA\"];\n");
        dot.append("qCOMLINEA [shape=doublecircle, label=\"Comentario linea\\nIgnorado\"];\n");
        dot.append("qCOMBLOQUE [shape=doublecircle, label=\"Comentario bloque\\nIgnorado\"];\n\n");

        // Identificadores y palabras reservadas
        dot.append("q0 -> qID [label=\"letra, _\"];\n");
        dot.append("qID -> qID [label=\"letra, digito, _\"];\n\n");

        // Numeros
        dot.append("q0 -> qNUM [label=\"digito\"];\n");
        dot.append("qNUM -> qNUM [label=\"digito\"];\n");
        dot.append("qNUM -> qPUNTO [label=\".\"];\n");
        dot.append("qPUNTO -> qDEC [label=\"digito\"];\n");
        dot.append("qDEC [shape=doublecircle, label=\"NUMERO\"];\n");
        dot.append("qDEC -> qDEC [label=\"digito\"];\n\n");

        // Cadenas
        dot.append("q0 -> qCADINI [label=\"comilla\"];\n");
        dot.append("qCADINI -> qCADINI [label=\"caracter excepto comilla y salto\"];\n");
        dot.append("qCADINI -> qCAD [label=\"comilla\"];\n\n");

        // Directivas
        dot.append("q0 -> qARROBA [label=\"@\"];\n");
        dot.append("qARROBA -> qDIR [label=\"modelo, rol, formato\"];\n\n");

        // Operadores
        dot.append("q0 -> qIGUAL [label=\"=\"];\n");
        dot.append("q0 -> qMAS [label=\"+\"];\n");

        dot.append("q0 -> qGUION [label=\"-\"];\n");
        dot.append("qGUION -> qFLECHA [label=\">\"];\n\n");

        // Delimitadores
        dot.append("q0 -> qLLAVEA [label=\"{\"];\n");
        dot.append("q0 -> qLLAVEC [label=\"}\"];\n");
        dot.append("q0 -> qPARA [label=\"(\"];\n");
        dot.append("q0 -> qPARC [label=\")\"];\n");
        dot.append("q0 -> qCOMA [label=\",\"];\n\n");

        // Comentarios
        dot.append("q0 -> qDIAGONAL [label=\"/\"];\n");

        dot.append("qDIAGONAL -> qCOMLINEA [label=\"/\"];\n");
        dot.append("qCOMLINEA -> qCOMLINEA [label=\"caracter excepto salto\"];\n");

        dot.append("qDIAGONAL -> qCOMBLOQUEINI [label=\"*\"];\n");
        dot.append("qCOMBLOQUEINI -> qCOMBLOQUEINI "
                + "[label=\"cualquier caracter excepto cierre\"];\n");
        dot.append("qCOMBLOQUEINI -> qASTERISCO [label=\"*\"];\n");
        dot.append("qASTERISCO -> qCOMBLOQUE [label=\"/\"];\n");
        dot.append("qASTERISCO -> qCOMBLOQUEINI "
                + "[label=\"otro caracter\"];\n\n");

        // Nota de palabras reservadas
        dot.append("subgraph cluster_reservadas {\n");
        dot.append("label=\"Clasificacion posterior del identificador\";\n");
        dot.append("style=dashed;\n");

        dot.append("reservadas [shape=note, label=\"");
        dot.append("AGENTE\\n");
        dot.append("contexto\\n");
        dot.append("variable\\n");
        dot.append("EJECUTAR\\n");
        dot.append("EXPORTAR\\n");
        dot.append("PREGUNTAR\\n");
        dot.append("GENERAR\\n");
        dot.append("RESUMIR\\n");
        dot.append("ANALIZAR\\n");
        dot.append("TRADUCIR\\n");
        dot.append("CLASIFICAR\\n");
        dot.append("EXTRAER\\n");
        dot.append("CARGAR\\n");
        dot.append("SOBRE\\n");
        dot.append("DESDE\\n");
        dot.append("EN\\n");
        dot.append("COMO");
        dot.append("\"];\n");

        dot.append("}\n");

        dot.append("}\n");

        return dot.toString();
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
                    "Graphviz no pudo generar la imagen del AFD."
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
                    "La generacion del AFD fue interrumpida."
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
