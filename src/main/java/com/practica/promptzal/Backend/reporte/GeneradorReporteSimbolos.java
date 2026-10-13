/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.reporte.estiloReporte.ReporteSimbolosEstilo;
import com.practica.promptzal.Backend.semantico.Simbolo;
import com.practica.promptzal.Backend.semantico.TablaSimbolos;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class GeneradorReporteSimbolos {
    
      private String rutaCarpeta;
    private String rutaReporte;
    private ReporteSimbolosEstilo estilo;

    public GeneradorReporteSimbolos() {
        rutaCarpeta = "reportes";
        rutaReporte = rutaCarpeta + "/tabla_simbolos.html";
        estilo = new ReporteSimbolosEstilo();
    }

    public boolean generar(TablaSimbolos tablaSimbolos) {
        if (tablaSimbolos == null) {
            return false;
        }

        crearCarpeta();

        String contenido = construirHTML(tablaSimbolos);

        try (BufferedWriter escritor
                = new BufferedWriter(new FileWriter(rutaReporte))) {

            escritor.write(contenido);
            return true;

        } catch (IOException e) {

            System.out.println(
                    "Error al generar el reporte de simbolos: "
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

    private String construirHTML(TablaSimbolos tablaSimbolos) {
        StringBuilder html = new StringBuilder();

        int totalSimbolos = tablaSimbolos.getSimbolos().size();
        int totalAgentes = contarTipo(
                tablaSimbolos,
                "AGENTE"
        );

        int totalVariables = contarTipo(
                tablaSimbolos,
                "VARIABLE_DECLARADA"
        );

        int totalResultados = contarTipo(
                tablaSimbolos,
                "RESULTADO_COMANDO"
        );

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");

        agregarHead(html);

        html.append("<body>\n");

        html.append("<main class=\"contenedor\">\n");

        agregarEncabezado(html);

        html.append("<section class=\"contenido\">\n");

        agregarResumen(
                html,
                totalSimbolos,
                totalAgentes,
                totalVariables,
                totalResultados
        );

        agregarHerramientas(
                html,
                totalSimbolos
        );

        agregarTabla(
                html,
                tablaSimbolos
        );

        agregarMensajeSinResultados(html);

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

        html.append("<meta charset=\"UTF-8\">\n");

        html.append(
                "<meta name=\"viewport\" "
                + "content=\"width=device-width, initial-scale=1.0\">\n"
        );

        html.append(
                "<title>Tabla de Simbolos - PromptZal</title>\n"
        );

        html.append("<style>\n");
        html.append(estilo.getCss());
        html.append("\n</style>\n");

        html.append("</head>\n");
    }

    private void agregarEncabezado(StringBuilder html) {
        html.append("<header>\n");

        html.append("<div class=\"titulo-reporte\">\n");
        html.append(
                "<h1>Tabla de Simbolos</h1>\n"
        );

        html.append(
                "<p>Simbolos registrados durante "
                + "el analisis semantico</p>\n"
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
            int totalSimbolos,
            int totalAgentes,
            int totalVariables,
            int totalResultados) {

        html.append("<div class=\"resumen\">\n");

        html.append("<div class=\"tarjeta\">\n");
        html.append("<span>Total de simbolos</span>\n");
        html.append("<strong>")
                .append(totalSimbolos)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-agentes\">\n"
        );

        html.append("<span>Agentes</span>\n");
        html.append("<strong>")
                .append(totalAgentes)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-variables\">\n"
        );

        html.append("<span>Variables declaradas</span>\n");
        html.append("<strong>")
                .append(totalVariables)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-resultados\">\n"
        );

        html.append("<span>Resultados de comandos</span>\n");
        html.append("<strong>")
                .append(totalResultados)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarHerramientas(
            StringBuilder html,
            int totalSimbolos) {

        html.append("<div class=\"herramientas\">\n");

        html.append("<div class=\"grupo-busqueda\">\n");

        html.append(
                "<input "
                + "type=\"text\" "
                + "id=\"buscadorSimbolos\" "
                + "class=\"buscador\" "
                + "placeholder=\"Buscar por nombre, tipo, "
                + "ambito o linea...\">"
        );

        html.append("\n");

        html.append(
                "<select "
                + "id=\"filtroTipo\" "
                + "class=\"filtro\">\n"
        );

        html.append(
                "<option value=\"TODOS\">"
                + "Todos los tipos"
                + "</option>\n"
        );

        html.append(
                "<option value=\"AGENTE\">"
                + "Agentes"
                + "</option>\n"
        );

        html.append(
                "<option value=\"VARIABLE_DECLARADA\">"
                + "Variables declaradas"
                + "</option>\n"
        );

        html.append(
                "<option value=\"RESULTADO_COMANDO\">"
                + "Resultados de comandos"
                + "</option>\n"
        );

        html.append("</select>\n");

        html.append("</div>\n");

        html.append(
                "<div "
                + "id=\"contadorVisibles\" "
                + "class=\"contador\">"
        );

        html.append(totalSimbolos);

        if (totalSimbolos == 1) {
            html.append(" simbolo visible");
        } else {
            html.append(" simbolos visibles");
        }

        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarTabla(
            StringBuilder html,
            TablaSimbolos tablaSimbolos) {

        html.append(
                "<div class=\"contenedor-tabla\">\n"
        );

        html.append("<table>\n");

        html.append("<thead>\n");
        html.append("<tr>\n");

        html.append("<th>No.</th>\n");
        html.append("<th>Nombre</th>\n");
        html.append("<th>Tipo</th>\n");
        html.append("<th>Ambito</th>\n");
        html.append("<th>Linea</th>\n");

        html.append("</tr>\n");
        html.append("</thead>\n");

        html.append("<tbody id=\"cuerpoSimbolos\">\n");

        int numero = 1;

        for (Simbolo simbolo
                : tablaSimbolos.getSimbolos()) {

            agregarFila(
                    html,
                    simbolo,
                    numero
            );

            numero++;
        }

        html.append("</tbody>\n");

        html.append("</table>\n");

        html.append("</div>\n");
    }

    private void agregarFila(
            StringBuilder html,
            Simbolo simbolo,
            int numero) {

        String tipo = simbolo.getTipo();
        String claseTipo = obtenerClaseTipo(tipo);

        html.append(
                "<tr class=\"fila-simbolo\" "
        );

        html.append("data-tipo=\"")
                .append(escaparHTML(tipo))
                .append("\" ");

        html.append("data-linea=\"")
                .append(simbolo.getLinea())
                .append("\">\n");

        html.append("<td class=\"numero\">")
                .append(numero)
                .append("</td>\n");

        html.append("<td class=\"nombre-simbolo\">")
                .append(
                        escaparHTML(
                                simbolo.getNombre()
                        )
                )
                .append("</td>\n");

        html.append("<td>\n");

        html.append("<span class=\"badge ")
                .append(claseTipo)
                .append("\">");

        html.append(
                escaparHTML(
                        obtenerNombreTipo(tipo)
                )
        );

        html.append("</span>\n");

        html.append("</td>\n");

        html.append("<td class=\"ambito\">")
                .append(
                        escaparHTML(
                                simbolo.getAmbito()
                        )
                )
                .append("</td>\n");

        html.append("<td class=\"linea\">")
                .append(simbolo.getLinea())
                .append("</td>\n");

        html.append("</tr>\n");
    }

    private void agregarMensajeSinResultados(
            StringBuilder html) {

        html.append(
                "<div "
                + "id=\"sinResultados\" "
                + "class=\"sin-resultados\">\n"
        );

        html.append(
                "<strong>No se encontraron simbolos</strong>\n"
        );

        html.append(
                "<span>"
                + "Intenta utilizar otro termino "
                + "de busqueda o filtro."
                + "</span>\n"
        );

        html.append("</div>\n");
    }

    private void agregarPie(StringBuilder html) {
        html.append("<footer>\n");

        html.append(
                "<span>PromptZal Compiler</span>\n"
        );

        html.append(
                "<span>Reporte de tabla de simbolos</span>\n"
        );

        html.append("</footer>\n");
    }

    private int contarTipo(
            TablaSimbolos tablaSimbolos,
            String tipo) {

        int cantidad = 0;

        for (Simbolo simbolo
                : tablaSimbolos.getSimbolos()) {

            if (simbolo.getTipo().equals(tipo)) {
                cantidad++;
            }
        }

        return cantidad;
    }

    private String obtenerClaseTipo(String tipo) {
        if (tipo.equals("AGENTE")) {
            return "tipo-agente";
        }

        if (tipo.equals("VARIABLE_DECLARADA")) {
            return "tipo-variable";
        }

        if (tipo.equals("RESULTADO_COMANDO")) {
            return "tipo-resultado";
        }

        return "";
    }

    private String obtenerNombreTipo(String tipo) {
        if (tipo.equals("VARIABLE_DECLARADA")) {
            return "VARIABLE";
        }

        if (tipo.equals("RESULTADO_COMANDO")) {
            return "RESULTADO";
        }

        return tipo;
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
