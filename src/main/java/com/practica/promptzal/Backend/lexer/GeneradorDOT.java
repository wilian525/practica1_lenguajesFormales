/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.LinkedHashSet;
import java.util.Set;
/**
 *
 * @author wilian
 */
public class GeneradorDOT {

    private HashMap<String, String> tokenDeEstado;
    private HashMap<String, Boolean> estadosDeError;

    private AutomataFinitoDeterministico automata;

    public GeneradorDOT() {

        this.tokenDeEstado = new HashMap<>();
        this.estadosDeError = new HashMap<>();
        this.automata = null;
    }

    public AutomataFinitoDeterministico construirAutomata() {

        AutomataFinitoDeterministico afd
                = new AutomataFinitoDeterministico();

        tokenDeEstado.clear();
        estadosDeError.clear();

        // estados 
        EstadoAFD q0 = new EstadoAFD("q0");

        // Palabras: reservadas, comandos, conectores, funciones, identificadores
        EstadoAFD q1 = new EstadoAFD("q1");

        // Numeros
        EstadoAFD q2 = new EstadoAFD("q2");
        EstadoAFD q3 = new EstadoAFD("q3");
        EstadoAFD q4 = new EstadoAFD("q4");

        // Directivas
        EstadoAFD q5 = new EstadoAFD("q5");
        EstadoAFD q6 = new EstadoAFD("q6");

        // Cadenas
        EstadoAFD q7 = new EstadoAFD("q7");
        EstadoAFD q8 = new EstadoAFD("q8");

        // Comentarios
        EstadoAFD q9 = new EstadoAFD("q9");
        EstadoAFD q10 = new EstadoAFD("q10");
        EstadoAFD q11 = new EstadoAFD("q11");
        EstadoAFD q12 = new EstadoAFD("q12");
        EstadoAFD q13 = new EstadoAFD("q13");
        EstadoAFD q14 = new EstadoAFD("q14");

        // Flecha
        EstadoAFD q15 = new EstadoAFD("q15");
        EstadoAFD q16 = new EstadoAFD("q16");

        // Operadores y delimitadores
        EstadoAFD q17 = new EstadoAFD("q17");
        EstadoAFD q18 = new EstadoAFD("q18");

        // Errores lexicos
        EstadoAFD q19 = new EstadoAFD("q19");
        EstadoAFD q20 = new EstadoAFD("q20");

        afd.establecerEstadoInicial(q0);

        /*
         * ----------- Estados de aceptacion -----------
         */
        afd.agregarEstadoAceptacion(q1);
        afd.agregarEstadoAceptacion(q2);
        afd.agregarEstadoAceptacion(q4);
        afd.agregarEstadoAceptacion(q6);
        afd.agregarEstadoAceptacion(q8);
        afd.agregarEstadoAceptacion(q11);
        afd.agregarEstadoAceptacion(q14);
        afd.agregarEstadoAceptacion(q16);
        afd.agregarEstadoAceptacion(q17);
        afd.agregarEstadoAceptacion(q18);
        afd.agregarEstadoAceptacion(q19);
        afd.agregarEstadoAceptacion(q20);

        /*
         * ----------- Etiquetas de los estados -----------
         */
        tokenDeEstado.put("q1", "PALABRA / IDENT");
        tokenDeEstado.put("q2", "ENTERO");
        tokenDeEstado.put("q4", "DECIMAL");
        tokenDeEstado.put("q6", "DIRECTIVA");
        tokenDeEstado.put("q8", "CADENA");
        tokenDeEstado.put("q11", "COMENT. LINEA");
        tokenDeEstado.put("q14", "COMENT. BLOQUE");
        tokenDeEstado.put("q16", "CONECTOR ->");
        tokenDeEstado.put("q17", "OPERADOR");
        tokenDeEstado.put("q18", "DELIMITADOR");
        tokenDeEstado.put("q19", "ERROR");
        tokenDeEstado.put("q20", "ERROR CADENA");

        estadosDeError.put("q19", true);
        estadosDeError.put("q20", true);

        /*
         * ----------- Transiciones -----------
         *
         * Palabras reservadas, comandos, conectores e identificadores
         */
        afd.agregarTransicion(q0, "letra | _", q1);
        afd.agregarTransicion(q1, "letra | digito | _", q1);

        /*
         * Numeros enteros y decimales
         */
        afd.agregarTransicion(q0, "digito", q2);
        afd.agregarTransicion(q2, "digito", q2);
        afd.agregarTransicion(q2, ".", q3);
        afd.agregarTransicion(q3, "digito", q4);
        afd.agregarTransicion(q4, "digito", q4);

        /*
         * Directivas @modelo @rol @formato
         */
        afd.agregarTransicion(q0, "@", q5);
        afd.agregarTransicion(q5, "letra", q6);
        afd.agregarTransicion(q6, "letra | digito | _", q6);
        afd.agregarTransicion(q5, "otro", q19);

        /*
         * Literales de cadena
         */
        afd.agregarTransicion(q0, "\"", q7);
        afd.agregarTransicion(q7, "no \" ni salto", q7);
        afd.agregarTransicion(q7, "\"", q8);
        afd.agregarTransicion(q7, "salto de linea | EOF", q20);

        /*
         * Comentarios de linea y de bloque
         */
        afd.agregarTransicion(q0, "/", q9);
        afd.agregarTransicion(q9, "/", q10);
        afd.agregarTransicion(q10, "no salto", q10);
        afd.agregarTransicion(q10, "salto de linea | EOF", q11);

        afd.agregarTransicion(q9, "*", q12);
        afd.agregarTransicion(q12, "no *", q12);
        afd.agregarTransicion(q12, "*", q13);
        afd.agregarTransicion(q13, "*", q13);
        afd.agregarTransicion(q13, "otro", q12);
        afd.agregarTransicion(q13, "/", q14);
        afd.agregarTransicion(q9, "otro", q19);

        /*
         * Conector de asignacion ->
         */
        afd.agregarTransicion(q0, "-", q15);
        afd.agregarTransicion(q15, ">", q16);
        afd.agregarTransicion(q15, "otro", q19);

        /*
         * Operadores y delimitadores
         */
        afd.agregarTransicion(q0, "= | +", q17);
        afd.agregarTransicion(q0, "{ | } | ( | ) | ,", q18);

        /*
         * Recuperacion de errores: cualquier otro simbolo
         */
        afd.agregarTransicion(q0, "otro simbolo", q19);

        this.automata = afd;

        return afd;
    }
    
    public Set<String> determinarEstadosUsados(
        Token[] tokens,
        int cantidadTokens,
        ErrorLexico[] errores,
        int cantidadErrores,
        int comentariosLinea,
        int comentariosBloqueCerrados) {

    Set<String> usados = new LinkedHashSet<>();
    usados.add("q0");

    for (int i = 0; i < cantidadTokens; i++) {

        Token token = tokens[i];
        if (token == null) {
            continue;
        }

        String lexema = token.getLexema();
        if (lexema == null) {
            lexema = "";
        }

        switch (token.getTipo()) {

            case DIRECTIVA:
                usados.add("q5");
                usados.add("q6");
                break;

            case CADENA:
                usados.add("q7");
                usados.add("q8");
                break;

            case ENTERO:
                usados.add("q2");
                break;

            case DECIMAL:
                usados.add("q2");
                usados.add("q3");
                usados.add("q4");
                break;

            case OPERADOR:
                usados.add("q17");
                break;

            case DELIMITADOR:
                usados.add("q18");
                break;

            case RESERVADA:
            case COMANDO_IA:
            case IDENTIFICADOR:
            case FUNCION:
                usados.add("q1");
                break;

            case CONECTOR:
                if (lexema.equals("->")) {
                    usados.add("q15");
                    usados.add("q16");
                } else {
                    usados.add("q1");
                }
                break;

            default:
                break;
        }
    }

    if (comentariosLinea > 0) {
        usados.add("q9");
        usados.add("q10");
        usados.add("q11");
    }

    if (comentariosBloqueCerrados > 0) {
        usados.add("q9");
        usados.add("q12");
        usados.add("q13");
        usados.add("q14");
    }

    for (int i = 0; i < cantidadErrores; i++) {

        ErrorLexico error = errores[i];
        if (error == null) {
            continue;
        }

        switch (error.getTipo()) {

            case CADENA_SIN_CERRAR:
                usados.add("q7");
                usados.add("q20");
                break;

            case COMENTARIO_BLOQUE_SIN_CERRAR:
                usados.add("q9");
                usados.add("q12");
                usados.add("q13");
                break;

            case DIRECTIVA_NO_RECONOCIDA:
                usados.add("q5");
                usados.add("q6");
                break;

            case CARACTER_NO_RECONOCIDO:
            default:
                usados.add("q19");
                break;
        }
    }

    return usados;
}

    // generar dto 
    public String generarCodigoDOT(
            AutomataFinitoDeterministico afd) {

        if (afd == null) {
            return "";
        }

        StringBuilder dot = new StringBuilder();

        dot.append("digraph AFD_PromptZal {\n");
        dot.append("    rankdir=LR;\n");
        dot.append("    bgcolor=\"white\";\n");
        dot.append("    fontname=\"Helvetica\";\n");
        dot.append("    labelloc=\"t\";\n");
        dot.append("    label=\"AFD del analizador lexico de PromptZal\";\n");
        dot.append("    fontsize=20;\n");
        dot.append("    node [fontname=\"Helvetica\", fontsize=10];\n");
        dot.append("    edge [fontname=\"Helvetica\", fontsize=9];\n\n");

        /*
         * Nodo invisible que marca el estado inicial.
         */
        dot.append("    inicio [shape=point, width=0.12];\n");

        /*
         * Declaracion de los estados.
         */
        for (EstadoAFD estado : afd.getEstados()) {

            String nombre = estado.getNombre();

            String etiqueta = nombre;

            if (tokenDeEstado.containsKey(nombre)) {

                etiqueta = nombre
                        + "\\n"
                        + tokenDeEstado.get(nombre);
            }

            dot.append("    ");
            dot.append(nombre);
            dot.append(" [label=\"");
            dot.append(escaparDOT(etiqueta));
            dot.append("\"");

            if (afd.esEstadoAceptacion(estado)) {
                dot.append(", shape=doublecircle");
            } else {
                dot.append(", shape=circle");
            }

            if (estadosDeError.containsKey(nombre)) {

                dot.append(", style=filled, fillcolor=\"#f6c6c6\"");
                dot.append(", color=\"#a33a3a\"");

            } else if (afd.esEstadoAceptacion(estado)) {

                dot.append(", style=filled, fillcolor=\"#cde9d3\"");
                dot.append(", color=\"#2e6639\"");

            } else if (estado == afd.getEstadoInicial()) {

                dot.append(", style=filled, fillcolor=\"#d6e4f5\"");
                dot.append(", color=\"#23364d\"");
            }

            dot.append("];\n");
        }

        dot.append("\n");

        /*
         * Flecha hacia el estado inicial.
         */
        if (afd.getEstadoInicial() != null) {

            dot.append("    inicio -> ");
            dot.append(afd.getEstadoInicial().getNombre());
            dot.append(";\n\n");
        }

        /*
         * Transiciones.
         */
        for (TransicionAFD transicion : afd.getTransiciones()) {

            dot.append("    ");
            dot.append(transicion.getEstadoOrigen().getNombre());
            dot.append(" -> ");
            dot.append(transicion.getEstadoDestino().getNombre());
            dot.append(" [label=\"");
            dot.append(escaparDOT(transicion.getSimbolo()));
            dot.append("\"];\n");
        }

        dot.append("}\n");

        return dot.toString();
    }

    public String generarCodigoDOT(
        AutomataFinitoDeterministico afd,
        Set<String> estadosUsados) {

    if (afd == null) {
        return "";
    }
    if (estadosUsados == null || estadosUsados.isEmpty()) {
        return generarCodigoDOT(afd);
    }

    StringBuilder dot = new StringBuilder();

    dot.append("digraph AFD_PromptZal {\n");
    dot.append("    rankdir=LR;\n");
    dot.append("    bgcolor=\"white\";\n");
    dot.append("    fontname=\"Helvetica\";\n");
    dot.append("    labelloc=\"t\";\n");
    dot.append("    label=\"AFD recorrido para este archivo\";\n");
    dot.append("    fontsize=20;\n");
    dot.append("    node [fontname=\"Helvetica\", fontsize=10];\n");
    dot.append("    edge [fontname=\"Helvetica\", fontsize=9];\n\n");
    dot.append("    inicio [shape=point, width=0.12];\n");

    for (EstadoAFD estado : afd.getEstados()) {

        String nombre = estado.getNombre();
        if (!estadosUsados.contains(nombre)) {
            continue;
        }

        String etiqueta = nombre;
        if (tokenDeEstado.containsKey(nombre)) {
            etiqueta = nombre + "\\n" + tokenDeEstado.get(nombre);
        }

        dot.append("    ").append(nombre)
           .append(" [label=\"").append(escaparDOT(etiqueta)).append("\"");

        dot.append(afd.esEstadoAceptacion(estado) ? ", shape=doublecircle" : ", shape=circle");

        if (estadosDeError.containsKey(nombre)) {
            dot.append(", style=filled, fillcolor=\"#f6c6c6\", color=\"#a33a3a\"");
        } else if (afd.esEstadoAceptacion(estado)) {
            dot.append(", style=filled, fillcolor=\"#cde9d3\", color=\"#2e6639\"");
        } else if (estado == afd.getEstadoInicial()) {
            dot.append(", style=filled, fillcolor=\"#d6e4f5\", color=\"#23364d\"");
        }

        dot.append("];\n");
    }

    dot.append("\n");

    if (afd.getEstadoInicial() != null
            && estadosUsados.contains(afd.getEstadoInicial().getNombre())) {
        dot.append("    inicio -> ").append(afd.getEstadoInicial().getNombre()).append(";\n\n");
    }

    for (TransicionAFD transicion : afd.getTransiciones()) {

        String origen = transicion.getEstadoOrigen().getNombre();
        String destino = transicion.getEstadoDestino().getNombre();

        if (!estadosUsados.contains(origen) || !estadosUsados.contains(destino)) {
            continue;
        }

        dot.append("    ").append(origen).append(" -> ").append(destino)
           .append(" [label=\"").append(escaparDOT(transicion.getSimbolo())).append("\"];\n");
    }

    dot.append("}\n");
    return dot.toString();
}
    /**
     * Escapa los caracteres que DOT interpreta de forma especial.
     */
    private String escaparDOT(String texto) {

        if (texto == null) {
            return "";
        }

        String resultado = texto;

        resultado = resultado.replace("\\", "\\\\");
        resultado = resultado.replace("\"", "\\\"");

        /*
         * El salto de linea de las etiquetas se escribe \\n
         * y no debe volver a escaparse.
         */
        resultado = resultado.replace("\\\\n", "\\n");

        return resultado;
    }

    public Path guardarCodigoDOT(
            String codigoDOT,
            Path rutaDot) throws IOException {

        Path directorio
                = rutaDot.toAbsolutePath().normalize().getParent();

        if (directorio != null) {
            Files.createDirectories(directorio);
        }

        Files.writeString(
                rutaDot,
                codigoDOT,
                StandardCharsets.UTF_8
        );

        return rutaDot;
    }

    /**
     * Comprueba si Graphviz esta instalado en el sistema.
     */
    public boolean graphvizDisponible() {

        try {

            ProcessBuilder constructor
                    = new ProcessBuilder("dot", "-V");

            constructor.redirectErrorStream(true);

            Process proceso = constructor.start();

            boolean termino
                    = proceso.waitFor(5, TimeUnit.SECONDS);

            if (!termino) {

                proceso.destroy();
                return false;
            }

            return proceso.exitValue() == 0;

        } catch (IOException | InterruptedException e) {

            return false;
        }
    }

    /**
     * Ejecuta Graphviz para convertir el archivo .dot en imagen.
     */
    public Path generarImagen(
            Path rutaDot,
            Path rutaImagen) throws IOException {

        ProcessBuilder constructor
                = new ProcessBuilder(
                        "dot",
                        "-Tpng",
                        rutaDot.toAbsolutePath().toString(),
                        "-o",
                        rutaImagen.toAbsolutePath().toString()
                );

        constructor.redirectErrorStream(true);

        Process proceso = constructor.start();

        try {

            boolean termino
                    = proceso.waitFor(20, TimeUnit.SECONDS);

            if (!termino) {

                proceso.destroy();

                throw new IOException(
                        "Graphviz tardo demasiado en responder."
                );
            }

            if (proceso.exitValue() != 0) {

                String salida
                        = new String(
                                proceso.getInputStream().readAllBytes(),
                                StandardCharsets.UTF_8
                        );

                throw new IOException(
                        "Graphviz devolvio un error: " + salida
                );
            }

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            throw new IOException(
                    "La generacion del AFD fue interrumpida."
            );
        }

        return rutaImagen;
    }

    // Construye el AFD, escribe el .dot y genera el .png
    public Path generarTodo(Path carpetaSalida,  String nombreBase, Set<String> estadosUsados) throws IOException {

      
    if (carpetaSalida == null) {
        carpetaSalida = Path.of(System.getProperty("user.home"));
    }

    Files.createDirectories(carpetaSalida);

    String base = (nombreBase == null || nombreBase.isBlank())
            ? "archivo"
            : nombreBase.replaceAll("[^A-Za-z0-9_\\-]", "_");

    AutomataFinitoDeterministico afd = construirAutomata();

    String codigoCompleto = generarCodigoDOT(afd);

    Path rutaDotCompleto
            = carpetaSalida.resolve("afd_promptzal_completo.dot");

    Path rutaPngCompleto
            = carpetaSalida.resolve("afd_promptzal_completo.png");

    guardarCodigoDOT(codigoCompleto, rutaDotCompleto);

    String codigoRecorrido = generarCodigoDOT(afd, estadosUsados);

    Path rutaDotRecorrido
            = carpetaSalida.resolve("afd_" + base + "_recorrido.dot");

    Path rutaPngRecorrido
            = carpetaSalida.resolve("afd_" + base + "_recorrido.png");

    guardarCodigoDOT(codigoRecorrido, rutaDotRecorrido);

    if (!graphvizDisponible()) {

        throw new IOException(
                "No se encontro Graphviz (comando 'dot') en el PATH.\n"
                + "El codigo DOT si se guardo en:\n"
                + rutaDotCompleto + "\n"
                + rutaDotRecorrido
        );
    }

    generarImagen(rutaDotCompleto, rutaPngCompleto);
    generarImagen(rutaDotRecorrido, rutaPngRecorrido);

    return rutaPngRecorrido;
    }

    public AutomataFinitoDeterministico getAutomata() {
        return automata;
    }
}
