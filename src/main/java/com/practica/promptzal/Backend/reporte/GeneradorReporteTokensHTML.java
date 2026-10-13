/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import com.practica.promptzal.Backend.reporte.estiloReporte.ReporteTokensEstilo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author wilian
 */
public class GeneradorReporteTokensHTML {

   private String rutaCarpeta;
    private String rutaReporte;
    private ReporteTokensEstilo estilo;

    public GeneradorReporteTokensHTML() {
        rutaCarpeta = "reportes";
        rutaReporte = rutaCarpeta + "/reporte_tokens.html";
        estilo = new ReporteTokensEstilo();
    }

    public boolean generar(ArrayList<Token> tokens) {
        if (tokens == null) {
            return false;
        }

        crearCarpeta();

        String contenido = construirHTML(tokens);

        try (BufferedWriter escritor
                = new BufferedWriter(
                        new FileWriter(rutaReporte)
                )) {

            escritor.write(contenido);

            return true;

        } catch (IOException e) {

            System.out.println(
                    "Error al generar el reporte de tokens: "
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

    private String construirHTML(
            ArrayList<Token> tokens) {

        StringBuilder html = new StringBuilder();

        int totalTokens = tokens.size();

        int tiposDiferentes
                = contarTiposDiferentes(tokens);

        int totalLineas
                = obtenerTotalLineas(tokens);

        int cantidadEOF
                = contarTipo(
                        tokens,
                        TipoToken.EOF
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
                totalTokens,
                tiposDiferentes,
                totalLineas,
                cantidadEOF
        );

        agregarHerramientas(
                html,
                totalTokens
        );

        agregarTabla(
                html,
                tokens
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
                "<title>Reporte de Tokens - PromptZal</title>\n"
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
                "<h1>Reporte de Tokens</h1>\n"
        );

        html.append(
                "<p>Tokens reconocidos por el "
                + "analizador lexico generado con JFlex</p>\n"
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
            int totalTokens,
            int tiposDiferentes,
            int totalLineas,
            int cantidadEOF) {

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

        html.append("<span>Tipos diferentes</span>\n");

        html.append("<strong>")
                .append(tiposDiferentes)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-lineas\">\n"
        );

        html.append("<span>Lineas detectadas</span>\n");

        html.append("<strong>")
                .append(totalLineas)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append(
                "<div class=\"tarjeta tarjeta-eof\">\n"
        );

        html.append("<span>Token EOF</span>\n");

        html.append("<strong>")
                .append(cantidadEOF)
                .append("</strong>\n");

        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarHerramientas(
            StringBuilder html,
            int totalTokens) {

        html.append(
                "<div class=\"herramientas\">\n"
        );

        html.append(
                "<div class=\"grupo-busqueda\">\n"
        );

        html.append(
                "<input "
                + "type=\"text\" "
                + "id=\"buscadorTokens\" "
                + "class=\"buscador\" "
                + "placeholder=\"Buscar por numero, lexema, "
                + "tipo, fila o columna...\">\n"
        );

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

        TipoToken[] tipos = TipoToken.values();

        for (int i = 0; i < tipos.length; i++) {

            TipoToken tipo = tipos[i];

            html.append("<option value=\"")
                    .append(tipo.name())
                    .append("\">");

            html.append(tipo.name())
                    .append(" - ")
                    .append(
                            escaparHTML(
                                    tipo.getDescripcion()
                            )
                    );

            html.append("</option>\n");
        }

        html.append("</select>\n");

        html.append("</div>\n");

        html.append(
                "<div "
                + "id=\"contadorVisibles\" "
                + "class=\"contador\">"
        );

        html.append(totalTokens);

        if (totalTokens == 1) {
            html.append(" token visible");
        } else {
            html.append(" tokens visibles");
        }

        html.append("</div>\n");

        html.append("</div>\n");
    }

    private void agregarTabla(
            StringBuilder html,
            ArrayList<Token> tokens) {

        html.append(
                "<div class=\"contenedor-tabla\">\n"
        );

        html.append("<table>\n");

        html.append("<thead>\n");
        html.append("<tr>\n");

        html.append("<th>No.</th>\n");
        html.append("<th>Lexema</th>\n");
        html.append("<th>Tipo</th>\n");
        html.append("<th>Fila</th>\n");
        html.append("<th>Columna</th>\n");

        html.append("</tr>\n");
        html.append("</thead>\n");

        html.append(
                "<tbody id=\"cuerpoTokens\">\n"
        );

        for (Token token : tokens) {
            agregarFila(
                    html,
                    token
            );
        }

        html.append("</tbody>\n");
        html.append("</table>\n");

        html.append("</div>\n");
    }

    private void agregarFila(
            StringBuilder html,
            Token token) {

        String tipo = token.getTipo().name();

        String claseTipo
                = obtenerClaseTipo(
                        token.getTipo()
                );

        html.append(
                "<tr class=\"fila-token\" "
        );

        html.append("data-tipo=\"")
                .append(tipo)
                .append("\" ");

        html.append("data-numero=\"")
                .append(token.getNumero())
                .append("\" ");

        html.append("data-fila=\"")
                .append(token.getFila())
                .append("\" ");

        html.append("data-columna=\"")
                .append(token.getColumna())
                .append("\">\n");

        html.append(
                "<td class=\"numero\">"
        );

        html.append(token.getNumero());

        html.append("</td>\n");

        html.append(
                "<td class=\"lexema\">"
        );

        html.append(
                escaparHTML(
                        token.getLexema()
                )
        );

        html.append("</td>\n");

        html.append("<td>\n");

        html.append(
                "<span class=\"badge "
        );

        html.append(claseTipo);

        html.append("\" title=\"");

        html.append(
                escaparHTML(
                        token.getTipo()
                                .getDescripcion()
                )
        );

        html.append("\">");

        html.append(tipo);

        html.append("</span>\n");

        html.append("</td>\n");

        html.append(
                "<td class=\"posicion\">"
        );

        html.append(token.getFila());

        html.append("</td>\n");

        html.append(
                "<td class=\"posicion\">"
        );

        html.append(token.getColumna());

        html.append("</td>\n");

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
                "<strong>No se encontraron tokens</strong>\n"
        );

        html.append(
                "<span>"
                + "Prueba con otro termino "
                + "de busqueda o filtro."
                + "</span>\n"
        );

        html.append("</div>\n");
    }

    private void agregarPie(
            StringBuilder html) {

        html.append("<footer>\n");

        html.append(
                "<span>PromptZal Compiler</span>\n"
        );

        html.append(
                "<span>Reporte de tokens</span>\n"
        );

        html.append("</footer>\n");
    }

    private int contarTiposDiferentes(
            ArrayList<Token> tokens) {

        ArrayList<TipoToken> tiposEncontrados
                = new ArrayList<>();

        for (Token token : tokens) {

            TipoToken tipo = token.getTipo();

            if (!tiposEncontrados.contains(tipo)) {
                tiposEncontrados.add(tipo);
            }
        }

        return tiposEncontrados.size();
    }

    private int contarTipo(
            ArrayList<Token> tokens,
            TipoToken tipoBuscado) {

        int cantidad = 0;

        for (Token token : tokens) {

            if (token.getTipo() == tipoBuscado) {
                cantidad++;
            }
        }

        return cantidad;
    }

    private int obtenerTotalLineas(
            ArrayList<Token> tokens) {

        int mayorFila = 0;

        for (Token token : tokens) {

            if (token.getFila() > mayorFila) {
                mayorFila = token.getFila();
            }
        }

        return mayorFila;
    }

    private String obtenerClaseTipo(
            TipoToken tipo) {

        if (tipo == TipoToken.DIRECTIVA) {
            return "tipo-directiva";
        }

        if (tipo == TipoToken.AGENTE
                || tipo == TipoToken.CONTEXTO
                || tipo == TipoToken.VARIABLE
                || tipo == TipoToken.EJECUTAR
                || tipo == TipoToken.EXPORTAR) {

            return "tipo-estructura";
        }

        if (tipo == TipoToken.COMANDO_IA) {
            return "tipo-comando";
        }

        if (tipo == TipoToken.CONECTOR) {
            return "tipo-conector";
        }

        if (tipo == TipoToken.CADENA
                || tipo == TipoToken.NUMERO) {

            return "tipo-literal";
        }

        if (tipo == TipoToken.ID) {
            return "tipo-identificador";
        }

        if (tipo == TipoToken.IGUAL
                || tipo == TipoToken.MAS
                || tipo == TipoToken.FLECHA) {

            return "tipo-operador";
        }

        if (tipo == TipoToken.LLAVE_A
                || tipo == TipoToken.LLAVE_C
                || tipo == TipoToken.PAR_A
                || tipo == TipoToken.PAR_C
                || tipo == TipoToken.COMA) {

            return "tipo-delimitador";
        }

        if (tipo == TipoToken.CARGAR) {
            return "tipo-sistema";
        }

        if (tipo == TipoToken.EOF) {
            return "tipo-eof";
        }

        return "tipo-identificador";
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
