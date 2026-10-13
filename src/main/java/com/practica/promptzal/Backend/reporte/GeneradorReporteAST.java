/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.ast.Agente;
import com.practica.promptzal.Backend.ast.Programa;
import com.practica.promptzal.Backend.reporte.estiloReporte.ReporteASTEstilo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author wilian
 */
public class GeneradorReporteAST {
    
    private String rutaCarpeta;
    private String rutaReporte;
    private ReporteASTEstilo estilo;
    private GeneradorAST generadorAST;

    public GeneradorReporteAST() {
        rutaCarpeta = "reportes";
        rutaReporte = rutaCarpeta + "/reporte_ast.html";

        estilo = new ReporteASTEstilo();
        generadorAST = new GeneradorAST();
    }

    public boolean generar(Programa programa) {
        if (programa == null) {
            return false;
        }

        crearCarpeta();

        boolean imagenGenerada
                = generadorAST.generar(programa);

        if (!imagenGenerada) {
            System.out.println(
                    "No se pudo generar la imagen del AST."
            );

            return false;
        }

        String contenido = construirHTML(programa);

        try (BufferedWriter escritor
                = new BufferedWriter(
                        new FileWriter(rutaReporte)
                )) {

            escritor.write(contenido);

            return true;

        } catch (IOException e) {

            System.out.println(
                    "Error al generar el reporte del AST: "
                    + e.getMessage()
            );

            return false;
        }
    }

    private void crearCarpeta() {
        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    private String construirHTML(Programa programa) {
        StringBuilder html = new StringBuilder();

        int totalAgentes
                = programa.getAgentes().size();

        int totalVariables
                = contarVariables(programa);

        int totalComandos
                = contarComandos(programa);

        int totalEjecuciones
                = programa.getEjecuciones().size();

        String nombreImagen = obtenerNombreImagen();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");

        agregarHead(html);

        html.append("<body>\n");

        html.append("<main class=\"contenedor\">\n");

        agregarEncabezado(html);

        html.append("<section class=\"contenido\">\n");

        agregarResumen(
                html,
                totalAgentes,
                totalVariables,
                totalComandos,
                totalEjecuciones
        );

        agregarBarraHerramientas(html);

        agregarVisor(
                html,
                nombreImagen
        );

        agregarLeyenda(html);

        agregarInformacion(
                html,
                programa
        );

        html.append("</section>\n");

        agregarPie(html);

        html.append("</main>\n");

        html.append("<script>\n");
        html.append(estilo.getJavaScript());
        html.append("\n</script>\n");

        html.append("</body>\n");
        html.append("</html>\n");

        return html.toString();
    }

    private void agregarHead(StringBuilder html) {
        html.append("<head>\n");

        html.append(
                "<meta charset=\"UTF-8\">\n"
        );

        html.append(
                "<meta name=\"viewport\" "
                + "content=\"width=device-width, initial-scale=1.0\">\n"
        );

        html.append(
                "<title>AST - PromptZal</title>\n"
        );

        html.append("<style>\n");
        html.append(estilo.getCss());
        html.append("\n</style>\n");

        html.append("</head>\n");
    }

    private void agregarEncabezado(
            StringBuilder html) {

        html.append("<header>\n");

        html.append(
                "<div class=\"titulo-reporte\">\n"
        );

        html.append(
                "<h1>Arbol de Sintaxis Abstracta</h1>\n"
        );

        html.append(
                "<p>Representacion dinamica del AST "
                + "construido durante el analisis</p>\n"
        );

        html.append("</div>\n");

        html.append(
                "<div class=\"marca\">"
                + "PromptZal Compiler"
                + "</div>\n"
        );

        html.append("</header>\n");
    }

    private void agregarResumen(
            StringBuilder html,
            int agentes,
            int variables,
            int comandos,
            int ejecuciones) {

        html.append("<div class=\"resumen\">\n");

        html.append(
                "<div class=\"tarjeta tarjeta-agentes\">\n"
        );

        html.append("<span>Agentes</span>\n");

        html.append("<strong>")
                .append(agentes)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-variables\">\n"
        );

        html.append("<span>Variables declaradas</span>\n");

        html.append("<strong>")
                .append(variables)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-comandos\">\n"
        );

        html.append("<span>Comandos</span>\n");

        html.append("<strong>")
                .append(comandos)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-ejecuciones\">\n"
        );

        html.append("<span>Ejecuciones</span>\n");

        html.append("<strong>")
                .append(ejecuciones)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarBarraHerramientas(
            StringBuilder html) {

        html.append(
                "<div class=\"barra-herramientas\">\n"
        );

        html.append(
                "<div class=\"grupo-controles\">\n"
        );

        html.append(
                "<button "
                + "id=\"zoomMenos\" "
                + "class=\"boton boton-zoom\" "
                + "type=\"button\" "
                + "title=\"Alejar\">"
                + "&minus;"
                + "</button>\n"
        );

        html.append(
                "<div "
                + "id=\"zoomIndicador\" "
                + "class=\"indicador-zoom\">"
                + "100%"
                + "</div>\n"
        );

        html.append(
                "<button "
                + "id=\"zoomMas\" "
                + "class=\"boton boton-zoom\" "
                + "type=\"button\" "
                + "title=\"Acercar\">"
                + "+"
                + "</button>\n"
        );

        html.append(
                "<button "
                + "id=\"zoomFit\" "
                + "class=\"boton\" "
                + "type=\"button\">"
                + "Ajustar"
                + "</button>\n"
        );

        html.append(
                "<button "
                + "id=\"zoomReset\" "
                + "class=\"boton\" "
                + "type=\"button\">"
                + "Restablecer"
                + "</button>\n"
        );

        html.append("</div>\n");

        html.append(
                "<div class=\"ayuda\">"
                + "Arrastra el arbol con el mouse "
                + "para desplazarte"
                + "</div>\n"
        );

        html.append("</div>\n");
    }

    private void agregarVisor(
            StringBuilder html,
            String nombreImagen) {

        html.append(
                "<div "
                + "id=\"astViewport\" "
                + "class=\"visor\">\n"
        );

        html.append(
                "<img "
                + "id=\"astImagen\" "
                + "class=\"ast-imagen\" "
                + "src=\""
        );

        html.append(
                escaparHTML(nombreImagen)
        );

        html.append(
                "\" "
                + "alt=\"Arbol de Sintaxis Abstracta "
                + "de PromptZal\" "
                + "draggable=\"false\">"
        );

        html.append("\n");

        html.append("</div>\n");
    }

    private void agregarLeyenda(
            StringBuilder html) {

        html.append("<div class=\"leyenda\">\n");

        agregarElementoLeyenda(
                html,
                "punto-programa",
                "Programa y estructura general"
        );

        agregarElementoLeyenda(
                html,
                "punto-agente",
                "Agentes"
        );

        agregarElementoLeyenda(
                html,
                "punto-variable",
                "Variables y expresiones"
        );

        agregarElementoLeyenda(
                html,
                "punto-comando",
                "Comandos y conectores"
        );

        html.append("</div>\n");
    }

    private void agregarElementoLeyenda(
            StringBuilder html,
            String clase,
            String texto) {

        html.append(
                "<div class=\"leyenda-item\">\n"
        );

        html.append(
                "<span class=\"punto "
                + clase
                + "\"></span>\n"
        );

        html.append("<span>")
                .append(
                        escaparHTML(texto)
                )
                .append("</span>\n");

        html.append("</div>\n");
    }

    private void agregarInformacion(
            StringBuilder html,
            Programa programa) {

        html.append("<div class=\"info\">\n");

        html.append(
                "<strong>AST dinamico:</strong> "
        );

        html.append(
                "este arbol fue construido recorriendo "
                + "el objeto Programa generado por el parser. "
        );

        html.append(
                "Su estructura cambia de acuerdo con las "
                + "directivas, agentes, variables, comandos, "
                + "ejecuciones y exportaciones del codigo "
                + "PromptZal analizado."
        );

        html.append("<br><br>\n");

        html.append(
                "<strong>Elementos actuales:</strong> "
        );

        html.append(
                programa.getDirectivas().size()
        );

        html.append(" directiva");

        if (programa.getDirectivas().size() != 1) {
            html.append("s");
        }

        html.append(", ");

        html.append(
                programa.getAgentes().size()
        );

        html.append(" agente");

        if (programa.getAgentes().size() != 1) {
            html.append("s");
        }

        html.append(", ");

        html.append(
                programa.getEjecuciones().size()
        );

        html.append(" ejecucion");

        if (programa.getEjecuciones().size() != 1) {
            html.append("es");
        }

        html.append(" y ");

        html.append(
                programa.getExportaciones().size()
        );

        html.append(" exportacion");

        if (programa.getExportaciones().size() != 1) {
            html.append("es");
        }

        html.append(".");

        html.append("</div>\n");
    }

    private void agregarPie(
            StringBuilder html) {

        html.append("<footer>\n");

        html.append(
                "<span>PromptZal Compiler</span>\n"
        );

        html.append(
                "<span>Reporte del AST</span>\n"
        );

        html.append("</footer>\n");
    }

    private int contarVariables(
            Programa programa) {

        int cantidad = 0;

        for (Agente agente : programa.getAgentes()) {

            cantidad += agente
                    .getVariables()
                    .size();
        }

        return cantidad;
    }

    private int contarComandos(
            Programa programa) {

        int cantidad = 0;

        for (Agente agente : programa.getAgentes()) {

            cantidad += agente
                    .getComandos()
                    .size();
        }

        return cantidad;
    }

    private String obtenerNombreImagen() {
        File imagen = new File(
                generadorAST.getRutaImagen()
        );

        return imagen.getName();
    }

    private String escaparHTML(String texto) {
        if (texto == null) {
            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    public String getRutaReporte() {
        return rutaReporte;
    }
}
