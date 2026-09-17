/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author wilian
 */
public class GeneradorReporteEstadisticasHTML {

    public void generar(
            Token[] tokens,
            int cantidadTokens,
            int cantidadErrores,
            int cantidadLineas,
            String rutaSalida) {

        /*
         * Frecuencia de cada tipo de token.
         */
        EnumMap<TipoToken, Integer> frecuencias
                = new EnumMap<>(TipoToken.class);

        for (TipoToken tipo : TipoToken.values()) {
            frecuencias.put(tipo, 0);
        }

        for (int i = 0; i < cantidadTokens; i++) {

            if (tokens[i] != null) {

                TipoToken tipo = tokens[i].getTipo();

                frecuencias.put(
                        tipo,
                        frecuencias.get(tipo) + 1
                );
            }
        }

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"es\">");

        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Reporte de Estadisticas - PromptZal</title>");
        html.append("<style>");
        html.append(obtenerEstilos());
        html.append("</style>");
        html.append("</head>");

        html.append("<body>");
        html.append("<div class=\"contenedor\">");

        html.append("<header>");
        html.append("<h1>PromptZal</h1>");
        html.append("<p>Reporte de Estadisticas</p>");
        html.append("</header>");

        /*
         * Tarjetas con los totales.
         */
        html.append("<div class=\"tarjetas\">");

        html.append(crearTarjeta("Total de tokens", cantidadTokens));
        html.append(crearTarjeta("Total de errores", cantidadErrores));
        html.append(crearTarjeta("Total de lineas", cantidadLineas));

        html.append("</div>");

        /*
         * Tabla de frecuencias.
         */
        html.append("<table>");

        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>No.</th>");
        html.append("<th>Tipo de token</th>");
        html.append("<th>Frecuencia</th>");
        html.append("<th>Porcentaje</th>");
        html.append("</tr>");
        html.append("</thead>");

        html.append("<tbody>");

        int numero = 1;

        for (Map.Entry<TipoToken, Integer> entrada
                : frecuencias.entrySet()) {

            if (entrada.getValue() == 0) {
                continue;
            }

            double porcentaje = 0;

            if (cantidadTokens > 0) {

                porcentaje
                        = (entrada.getValue() * 100.0) / cantidadTokens;
            }

            html.append("<tr>");

            html.append("<td>").append(numero).append("</td>");

            html.append("<td>");
            html.append(escaparHTML(entrada.getKey().toString()));
            html.append("</td>");

            html.append("<td>").append(entrada.getValue()).append("</td>");

            html.append("<td>");
            html.append(String.format("%.2f", porcentaje));
            html.append(" %</td>");

            html.append("</tr>");

            numero++;
        }

        html.append("</tbody>");
        html.append("</table>");

        html.append("<footer>");
        html.append("Laboratorio de Lenguajes Formales y de Programacion");
        html.append("</footer>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        escribirArchivo(rutaSalida, html.toString());
    }

    private String crearTarjeta(String titulo, int valor) {

        StringBuilder tarjeta = new StringBuilder();

        tarjeta.append("<div class=\"tarjeta\">");
        tarjeta.append("<span class=\"valor\">");
        tarjeta.append(valor);
        tarjeta.append("</span>");
        tarjeta.append("<span class=\"titulo\">");
        tarjeta.append(escaparHTML(titulo));
        tarjeta.append("</span>");
        tarjeta.append("</div>");

        return tarjeta.toString();
    }

    private String obtenerEstilos() {

        return """
               :root {
                                                    --acento: #3b82f6;
                                                    --fondo: #0f172a;
                                                    --panel: #16202f;
                                                    --texto: #e2e8f0;
                                                    --borde: #2b3b52;
                                                }
                                                body{
                                                           margin: 0;
                                                           padding: 30px;
                                                           font-family: Arial, Helvetica, sans-serif;
                                                           background-color: var(--fondo);
                                                           color: var(--texto);
                                                }
                                                .contenedor {
                                                          max-width: 1100px;
                                                          margin: 0 auto;
                                                          background-color: var(--panel);
                                                          border: 1px solid var(--borde);
                                                          border-radius: 8px;
                                                          overflow: hidden;
                                                }
                                                header{
                                                          background-color: #0b1220;
                                                          color: white;
                                                          padding: 25px 30px;
                                                }
                                                header h1{ margin: 0; }
                                                header p{ margin: 6px 0 0; color: #9db3cc; }
                                                .resumen{
                                                          margin: 25px 30px 15px;
                                                          padding: 14px;
                                                          background-color: #1e293b;
                                                          border-left: 4px solid var(--acento);
                                                }
                                                table {
                                                          width: calc(100% - 60px);
                                                          margin: 20px 30px 30px;
                                                          border-collapse: collapse;
                                                }
                                                thead{
                                                          background-color: var(--acento);
                                                          color: white;
                                                }
                                                th, td {
                                                          padding: 11px 13px;
                                                          border: 1px solid var(--borde);
                                                          text-align: left;
                                                }
                                                tbody tr:nth-child(even){ background-color: #1b2635; }
                                                .lexema{
                                                          font-family: "Courier New", monospace;
                                                          font-weight: bold;
                                                          color: #f1f5f9;
                                                }
                                                footer{
                                                          padding: 18px;
                                                          background-color: #0b1220;
                                                          text-align: center;
                                                          color: #7c8ba1;
                }
                                                """;
    }

    private String escaparHTML(String texto) {

        if (texto == null) {
            return "";
        }

        String resultado = texto;

        resultado = resultado.replace("&", "&amp;");
        resultado = resultado.replace("<", "&lt;");
        resultado = resultado.replace(">", "&gt;");
        resultado = resultado.replace("\"", "&quot;");

        return resultado;
    }

    private void escribirArchivo(String rutaSalida, String contenido) {

        try {

            Files.writeString(
                    Path.of(rutaSalida),
                    contenido,
                    StandardCharsets.UTF_8
            );

            System.out.println(
                    "Reporte de estadisticas generado " + rutaSalida
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al generar reporte de estadisticas"
            );
        }
    }

}
