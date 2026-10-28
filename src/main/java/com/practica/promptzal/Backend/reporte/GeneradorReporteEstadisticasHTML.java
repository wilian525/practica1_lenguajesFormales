/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.lexer.ErrorLexico;
import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import com.practica.promptzal.Backend.parser.ErrorSintactico;
import com.practica.promptzal.Backend.reporte.estiloReporte.ReporteEstadisticasEstilo;
import com.practica.promptzal.Backend.semantico.ErrorSemantico;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 *
 * @author wilian
 */
public class GeneradorReporteEstadisticasHTML {

    private String rutaCarpeta;
    private String rutaReporte;
    private ReporteEstadisticasEstilo estilo;

    public GeneradorReporteEstadisticasHTML() {
        rutaCarpeta = "reportes";
        rutaReporte = rutaCarpeta + "/reporte_estadisticas.html";
        estilo = new ReporteEstadisticasEstilo();
    }

    public boolean generar(
            ArrayList<Token> tokens,
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos,
            String codigoFuente) {

        if (tokens == null
                || erroresLexicos == null
                || erroresSintacticos == null
                || erroresSemanticos == null
                || codigoFuente == null) {

            return false;
        }

        crearCarpeta();

        String contenido = construirHTML(
                tokens,
                erroresLexicos,
                erroresSintacticos,
                erroresSemanticos,
                codigoFuente
        );

        try (BufferedWriter escritor= new BufferedWriter( new FileWriter(rutaReporte) )) {
            escritor.write(contenido);
            return true;

        } catch (IOException e) {

            System.out.println("Error al generar el reporte de estadisticas: "+ e.getMessage());
            return false;
        }
    }

    private void crearCarpeta() {
        File carpeta = new File(rutaCarpeta);

        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    private String construirHTML(
            ArrayList<Token> tokens,
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos,
            String codigoFuente) {

        StringBuilder html = new StringBuilder();

        int totalTokens = tokens.size();

        int tiposUtilizados = contarTiposUtilizados(tokens);

        int totalLineas = contarLineas(codigoFuente);

        int totalLexicos = erroresLexicos.size();

        int totalSintacticos= erroresSintacticos.size();

        int totalSemanticos= erroresSemanticos.size();

        int totalErrores = totalLexicos + totalSintacticos+ totalSemanticos;

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");

        agregarHead(html);

        html.append("<body>\n");
        html.append("<main class=\"contenedor\">\n");

        agregarEncabezado(html);

        html.append("<section class=\"contenido\">\n");

        agregarResumen(html,totalTokens,tiposUtilizados,totalLineas, totalErrores);

        agregarFrecuencias(html, tokens,totalTokens);

        agregarResumenCompilacion( html, totalTokens,tiposUtilizados, totalLineas, totalLexicos,  totalSintacticos, totalSemanticos, totalErrores );

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

    private void agregarHead(
            StringBuilder html) {

        html.append("<head>\n");

        html.append(
                "<meta charset=\"UTF-8\">\n"
        );

        html.append(
                "<meta name=\"viewport\" "
                + "content=\"width=device-width, initial-scale=1.0\">\n"
        );

        html.append(
                "<title>Reporte de Estadisticas - PromptZal</title>\n"
        );

        html.append("<style>\n");
        html.append(estilo.getCss());
        html.append("\n</style>\n");

        html.append("</head>\n");
    }

    private void agregarEncabezado(
            StringBuilder html) {

        html.append("<header>\n");

        html.append("<div class=\"titulo-reporte\">\n");

        html.append("<h1>Reporte de Estadisticas</h1>\n");

        html.append( "<p>Resumen general del proceso "+ "de compilacion de PromptZal</p>\n" );

        html.append("</div>\n");

        html.append("<div class=\"marca\">"+ "PromptZal Compiler"+ "</div>\n");

        html.append("</header>\n");
    }

    private void agregarResumen(
            StringBuilder html,
            int totalTokens,
            int tiposUtilizados,
            int totalLineas,
            int totalErrores) {

        html.append("<div class=\"resumen\">\n");

        html.append(
                "<div class=\"tarjeta tarjeta-tokens\">\n"
        );

        html.append("<span>Total de tokens</span>\n");

        html.append("<strong>")
                .append(totalTokens)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-tipos\">\n"
        );

        html.append("<span>Tipos utilizados</span>\n");

        html.append("<strong>")
                .append(tiposUtilizados)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-lineas\">\n"
        );

        html.append("<span>Total de lineas</span>\n");

        html.append("<strong>")
                .append(totalLineas)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-errores\">\n"
        );

        html.append("<span>Total de errores</span>\n");

        html.append("<strong>")
                .append(totalErrores)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarFrecuencias( StringBuilder html,ArrayList<Token> tokens,  int totalTokens) {

        html.append("<div class=\"seccion\">\n");

        html.append("<div class=\"titulo-seccion\">\n" );

        html.append("<h2>Frecuencia por tipo de token</h2>\n");

        html.append("<span>Distribucion de los terminales "+ "reconocidos por JFlex</span>\n" );

        html.append("</div>\n");

        html.append( "<div class=\"lista-frecuencias\">\n" );

        TipoToken[] tipos = TipoToken.values();

        for (int i = 0; i < tipos.length; i++) {

            TipoToken tipo = tipos[i];

            int cantidad = contarTipo(tokens, tipo);

            double porcentaje = 0.0;

            if (totalTokens > 0) {
                porcentaje= cantidad * 100.0 / totalTokens;
            }

            agregarFrecuencia( html,tipo, cantidad, porcentaje );
        }

        html.append("</div>\n");
        html.append("</div>\n");
    }

    private void agregarFrecuencia(StringBuilder html,TipoToken tipo,int cantidad,double porcentaje) {

        String porcentajeTexto = String.format(Locale.US,"%.2f",porcentaje);

        html.append(
                "<div class=\"frecuencia\">\n"
        );

        html.append(
                "<div class=\"frecuencia-encabezado\">\n"
        );

        html.append(
                "<span class=\"tipo-token\" title=\""
        );

        html.append(
                escaparHTML(
                        tipo.getDescripcion()
                )
        );

        html.append("\">");

        html.append(
                escaparHTML(
                        tipo.name()
                )
        );

        html.append("</span>\n");

        html.append(
                "<span class=\"cantidad\">"
        );

        html.append(cantidad);

        html.append("</span>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"barra-contenedor\">\n"
        );

        html.append(
                "<div class=\"barra\" "
                + "data-porcentaje=\""
        );

        html.append(
                Double.toString(porcentaje)
        );

        html.append("\"></div>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"porcentaje\">"
        );

        html.append(porcentajeTexto)
                .append(" %");

        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarResumenCompilacion(
            StringBuilder html,
            int totalTokens,
            int tiposUtilizados,
            int totalLineas,
            int totalLexicos,
            int totalSintacticos,
            int totalSemanticos,
            int totalErrores) {

        html.append("<div class=\"seccion\">\n");

        html.append(
                "<div class=\"titulo-seccion\">\n"
        );

        html.append(
                "<h2>Resumen de compilacion</h2>\n"
        );

        html.append(
                "<span>Resultados generales del analisis</span>\n"
        );

        html.append("</div>\n");

        html.append(
                "<div class=\"tabla-contenedor\">\n"
        );

        html.append("<table>\n");

        html.append("<thead>\n");
        html.append("<tr>\n");

        html.append("<th>Metrica</th>\n");
        html.append("<th>Valor</th>\n");

        html.append("</tr>\n");
        html.append("</thead>\n");

        html.append("<tbody>\n");

        agregarFilaResumen(
                html,
                "Tokens reconocidos",
                String.valueOf(totalTokens),
                false
        );

        agregarFilaResumen(
                html,
                "Tipos de token utilizados",
                String.valueOf(tiposUtilizados),
                false
        );

        agregarFilaResumen(
                html,
                "Lineas analizadas",
                String.valueOf(totalLineas),
                false
        );

        agregarFilaResumen(
                html,
                "Errores lexicos",
                String.valueOf(totalLexicos),
                totalLexicos > 0
        );

        agregarFilaResumen(
                html,
                "Errores sintacticos",
                String.valueOf(totalSintacticos),
                totalSintacticos > 0
        );

        agregarFilaResumen(
                html,
                "Errores semanticos",
                String.valueOf(totalSemanticos),
                totalSemanticos > 0
        );

        agregarFilaResumen(
                html,
                "Total de errores",
                String.valueOf(totalErrores),
                totalErrores > 0
        );

        agregarEstadoCompilacion(
                html,
                totalErrores
        );

        html.append("</tbody>\n");

        html.append("</table>\n");

        html.append("</div>\n");
        html.append("</div>\n");
    }

    private void agregarFilaResumen(
            StringBuilder html,
            String nombre,
            String valor,
            boolean error) {

        html.append("<tr>\n");

        html.append("<td>")
                .append(
                        escaparHTML(nombre)
                )
                .append("</td>\n");

        html.append("<td class=\"");

        if (error) {
            html.append("estado-error");
        } else {
            html.append("dato");
        }

        html.append("\">");

        html.append(
                escaparHTML(valor)
        );

        html.append("</td>\n");

        html.append("</tr>\n");
    }

    private void agregarEstadoCompilacion(
            StringBuilder html,
            int totalErrores) {

        html.append("<tr>\n");

        html.append(
                "<td>Estado de la compilacion</td>\n"
        );

        if (totalErrores == 0) {

            html.append(
                    "<td class=\"estado-correcto\">"
                    + "Sin errores"
                    + "</td>\n"
            );

        } else {

            html.append(
                    "<td class=\"estado-error\">"
                    + "Con errores"
                    + "</td>\n"
            );
        }

        html.append("</tr>\n");
    }

    private int contarTipo(
            ArrayList<Token> tokens,
            TipoToken tipoBuscado) {

        int cantidad = 0;

        for (Token token : tokens) {

            if (token.getTipo()
                    == tipoBuscado) {

                cantidad++;
            }
        }

        return cantidad;
    }

    private int contarTiposUtilizados(
            ArrayList<Token> tokens) {

        int cantidad = 0;

        TipoToken[] tipos
                = TipoToken.values();

        for (int i = 0;
                i < tipos.length;
                i++) {

            if (contarTipo(
                    tokens,
                    tipos[i]
            ) > 0) {

                cantidad++;
            }
        }

        return cantidad;
    }

    private int contarLineas(
            String codigoFuente) {

        if (codigoFuente.isEmpty()) {
            return 0;
        }

        int cantidad = 1;

        for (int i = 0;
                i < codigoFuente.length();
                i++) {

            if (codigoFuente.charAt(i) == '\n') {
                cantidad++;
            }
        }

        return cantidad;
    }

    private void agregarPie(
            StringBuilder html) {

        html.append("<footer>\n");

        html.append(
                "<span>PromptZal Compiler</span>\n"
        );

        html.append(
                "<span>Reporte de estadisticas</span>\n"
        );

        html.append("</footer>\n");
    }

    private String escaparHTML(
            String texto) {

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
