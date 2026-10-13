/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.lexer.ErrorLexico;
import com.practica.promptzal.Backend.parser.ErrorSintactico;
import com.practica.promptzal.Backend.reporte.estiloReporte.ReporteErroresEstilo;
import com.practica.promptzal.Backend.semantico.ErrorSemantico;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author wilian
 */
public class GenerarReporteErroresHTML {

    private String rutaCarpeta;
    private String rutaReporte;
    private ReporteErroresEstilo estilo;

    public GenerarReporteErroresHTML() {
        rutaCarpeta = "reportes";
        rutaReporte = rutaCarpeta + "/reporte_errores.html";
        estilo = new ReporteErroresEstilo();
    }

    public boolean generar(
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos) {

        return generar(
                erroresLexicos,
                erroresSintacticos,
                erroresSemanticos,
                rutaReporte
        );
    }

    public boolean generar(
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos,
            String rutaSalida) {

        if (erroresLexicos == null
                || erroresSintacticos == null
                || erroresSemanticos == null) {

            return false;
        }

        crearCarpetaSalida(rutaSalida);

        String contenido = construirHTML(
                erroresLexicos,
                erroresSintacticos,
                erroresSemanticos
        );

        try (BufferedWriter escritor
                = new BufferedWriter(
                        new FileWriter(rutaSalida)
                )) {

            escritor.write(contenido);

            return true;

        } catch (IOException e) {

            System.out.println(
                    "Error al generar el reporte de errores: "
                    + e.getMessage()
            );

            return false;
        }
    }

    // Metodo temporal para mantener compatibilidad
    // con la interfaz anterior del Proyecto 1
    public void generar(
            ErrorLexico[] errores,
            int cantidadErrores,
            String rutaSalida) {

        ArrayList<ErrorLexico> erroresLexicos
                = new ArrayList<>();

        if (errores != null) {

            for (int i = 0;
                    i < cantidadErrores
                    && i < errores.length;
                    i++) {

                if (errores[i] != null) {
                    erroresLexicos.add(
                            errores[i]
                    );
                }
            }
        }

        ArrayList<ErrorSintactico> erroresSintacticos
                = new ArrayList<>();

        ArrayList<ErrorSemantico> erroresSemanticos
                = new ArrayList<>();

        generar(
                erroresLexicos,
                erroresSintacticos,
                erroresSemanticos,
                rutaSalida
        );
    }

    private void crearCarpetaSalida(String rutaSalida) {
        File archivo = new File(rutaSalida);

        File carpeta = archivo.getParentFile();

        if (carpeta != null
                && !carpeta.exists()) {

            carpeta.mkdirs();
        }
    }

    private String construirHTML(
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos) {

        StringBuilder html = new StringBuilder();

        int totalLexicos = erroresLexicos.size();
        int totalSintacticos = erroresSintacticos.size();
        int totalSemanticos = erroresSemanticos.size();

        int totalErrores = totalLexicos
                + totalSintacticos
                + totalSemanticos;

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");

        agregarHead(html);

        html.append("<body>\n");
        html.append("<main class=\"contenedor\">\n");

        agregarEncabezado(html);

        html.append("<section class=\"contenido\">\n");

        agregarResumen(
                html,
                totalErrores,
                totalLexicos,
                totalSintacticos,
                totalSemanticos
        );

        if (totalErrores == 0) {

            agregarEstadoCorrecto(html);

        } else {

            agregarHerramientas(
                    html,
                    totalErrores
            );

            agregarTabla(
                    html,
                    erroresLexicos,
                    erroresSintacticos,
                    erroresSemanticos
            );

            agregarMensajeSinResultados(html);
        }

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
                "<title>Reporte de Errores - PromptZal</title>\n"
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
                "<h1>Reporte de Errores</h1>\n"
        );

        html.append(
                "<p>Resultados del analisis lexico, "
                + "sintactico y semantico</p>\n"
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
            int totalErrores,
            int totalLexicos,
            int totalSintacticos,
            int totalSemanticos) {

        html.append("<div class=\"resumen\">\n");

        html.append("<div class=\"tarjeta\">\n");
        html.append("<span>Total de errores</span>\n");
        html.append("<strong>")
                .append(totalErrores)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-lexicos\">\n"
        );

        html.append("<span>Errores lexicos</span>\n");
        html.append("<strong>")
                .append(totalLexicos)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-sintacticos\">\n"
        );

        html.append("<span>Errores sintacticos</span>\n");
        html.append("<strong>")
                .append(totalSintacticos)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-semanticos\">\n"
        );

        html.append("<span>Errores semanticos</span>\n");
        html.append("<strong>")
                .append(totalSemanticos)
                .append("</strong>\n");
        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarHerramientas(
            StringBuilder html,
            int totalErrores) {

        html.append("<div class=\"herramientas\">\n");

        html.append("<div class=\"grupo-busqueda\">\n");

        html.append(
                "<input "
                + "type=\"text\" "
                + "id=\"buscadorErrores\" "
                + "class=\"buscador\" "
                + "placeholder=\"Buscar por descripcion, "
                + "token, fila o columna...\">\n"
        );

        html.append(
                "<select "
                + "id=\"filtroTipo\" "
                + "class=\"filtro\">\n"
        );

        html.append(
                "<option value=\"TODOS\">"
                + "Todos los errores"
                + "</option>\n"
        );

        html.append(
                "<option value=\"LEXICO\">"
                + "Lexicos"
                + "</option>\n"
        );

        html.append(
                "<option value=\"SINTACTICO\">"
                + "Sintacticos"
                + "</option>\n"
        );

        html.append(
                "<option value=\"SEMANTICO\">"
                + "Semanticos"
                + "</option>\n"
        );

        html.append("</select>\n");

        html.append("</div>\n");

        html.append(
                "<div "
                + "id=\"contadorVisibles\" "
                + "class=\"contador\">"
        );

        html.append(totalErrores);

        if (totalErrores == 1) {
            html.append(" error visible");
        } else {
            html.append(" errores visibles");
        }

        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarTabla(
            StringBuilder html,
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos) {

        html.append(
                "<div class=\"contenedor-tabla\">\n"
        );

        html.append("<table>\n");

        html.append("<thead>\n");
        html.append("<tr>\n");

        html.append("<th>No.</th>\n");
        html.append("<th>Tipo</th>\n");
        html.append("<th>Descripcion</th>\n");
        html.append("<th>Fila</th>\n");
        html.append("<th>Columna</th>\n");
        html.append("<th>Encontrado</th>\n");
        html.append("<th>Esperado</th>\n");

        html.append("</tr>\n");
        html.append("</thead>\n");

        html.append("<tbody id=\"cuerpoErrores\">\n");

        int numero = 1;

        for (ErrorLexico error : erroresLexicos) {

            agregarFilaLexica(
                    html,
                    error,
                    numero
            );

            numero++;
        }

        for (ErrorSintactico error
                : erroresSintacticos) {

            agregarFilaSintactica(
                    html,
                    error,
                    numero
            );

            numero++;
        }

        for (ErrorSemantico error
                : erroresSemanticos) {

            agregarFilaSemantica(
                    html,
                    error,
                    numero
            );

            numero++;
        }

        html.append("</tbody>\n");

        html.append("</table>\n");

        html.append("</div>\n");
    }

    private void agregarFilaLexica(
            StringBuilder html,
            ErrorLexico error,
            int numero) {

        abrirFila(
                html,
                "LEXICO",
                error.getFila(),
                String.valueOf(error.getColumna())
        );

        agregarNumero(
                html,
                numero
        );

        agregarTipo(
                html,
                "LEXICO",
                "tipo-lexico"
        );

        agregarDescripcion(
                html,
                error.getDescripcion()
        );

        agregarPosicion(
                html,
                error.getFila(),
                String.valueOf(error.getColumna())
        );

        agregarToken(
                html,
                error.getLexema()
        );

        agregarVacio(html);

        cerrarFila(html);
    }

    private void agregarFilaSintactica(
            StringBuilder html,
            ErrorSintactico error,
            int numero) {

        abrirFila(
                html,
                "SINTACTICO",
                error.getFila(),
                String.valueOf(error.getColumna())
        );

        agregarNumero(
                html,
                numero
        );

        agregarTipo(
                html,
                "SINTACTICO",
                "tipo-sintactico"
        );

        agregarDescripcion(
                html,
                error.getDescripcion()
        );

        agregarPosicion(
                html,
                error.getFila(),
                String.valueOf(error.getColumna())
        );

        agregarToken(
                html,
                error.getTokenEncontrado()
        );

        agregarToken(
                html,
                error.getTokenEsperado()
        );

        cerrarFila(html);
    }

    private void agregarFilaSemantica(
            StringBuilder html,
            ErrorSemantico error,
            int numero) {

        abrirFila(
                html,
                "SEMANTICO",
                error.getLinea(),
                ""
        );

        agregarNumero(
                html,
                numero
        );

        agregarTipo(
                html,
                "SEMANTICO",
                "tipo-semantico"
        );

        agregarDescripcion(
                html,
                error.getDescripcion()
        );

        agregarPosicion(
                html,
                error.getLinea(),
                null
        );

        agregarVacio(html);
        agregarVacio(html);

        cerrarFila(html);
    }

    private void abrirFila(
            StringBuilder html,
            String tipo,
            int fila,
            String columna) {

        html.append(
                "<tr class=\"fila-error\" "
        );

        html.append("data-tipo=\"")
                .append(tipo)
                .append("\" ");

        html.append("data-fila=\"")
                .append(fila)
                .append("\" ");

        html.append("data-columna=\"")
                .append(
                        columna == null
                                ? ""
                                : escaparHTML(columna)
                )
                .append("\">\n");
    }

    private void cerrarFila(StringBuilder html) {
        html.append("</tr>\n");
    }

    private void agregarNumero(
            StringBuilder html,
            int numero) {

        html.append("<td class=\"numero\">")
                .append(numero)
                .append("</td>\n");
    }

    private void agregarTipo(
            StringBuilder html,
            String texto,
            String clase) {

        html.append("<td>\n");

        html.append("<span class=\"badge ")
                .append(clase)
                .append("\">");

        html.append(texto);

        html.append("</span>\n");

        html.append("</td>\n");
    }

    private void agregarDescripcion(
            StringBuilder html,
            String descripcion) {

        html.append("<td class=\"descripcion\">")
                .append(
                        escaparHTML(descripcion)
                )
                .append("</td>\n");
    }

    private void agregarPosicion(
            StringBuilder html,
            int fila,
            String columna) {

        html.append("<td class=\"posicion\">")
                .append(fila)
                .append("</td>\n");

        if (columna == null
                || columna.isEmpty()) {

            agregarVacio(html);

        } else {

            html.append("<td class=\"posicion\">")
                    .append(
                            escaparHTML(columna)
                    )
                    .append("</td>\n");
        }
    }

    private void agregarToken(
            StringBuilder html,
            String token) {

        if (token == null
                || token.isEmpty()) {

            agregarVacio(html);
            return;
        }

        html.append("<td class=\"token\">")
                .append(
                        escaparHTML(token)
                )
                .append("</td>\n");
    }

    private void agregarVacio(StringBuilder html) {
        html.append(
                "<td class=\"vacio\">&mdash;</td>\n"
        );
    }

    private void agregarEstadoCorrecto(
            StringBuilder html) {

        html.append(
                "<div class=\"estado-correcto\">\n"
        );

        html.append(
                "<div class=\"icono\">&#10003;</div>\n"
        );

        html.append(
                "<h2>Compilacion sin errores</h2>\n"
        );

        html.append(
                "<p>No se encontraron errores lexicos, "
                + "sintacticos ni semanticos.</p>\n"
        );

        html.append("</div>\n");
    }

    private void agregarMensajeSinResultados(
            StringBuilder html) {

        html.append(
                "<div "
                + "id=\"sinResultados\" "
                + "class=\"sin-resultados\">\n"
        );

        html.append(
                "<strong>No se encontraron coincidencias</strong>\n"
        );

        html.append(
                "<span>"
                + "Prueba con otro termino "
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
                "<span>Reporte general de errores</span>\n"
        );

        html.append("</footer>\n");
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
