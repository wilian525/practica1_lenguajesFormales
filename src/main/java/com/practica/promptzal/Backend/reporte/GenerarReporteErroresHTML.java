/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.lexer.ErrorLexico;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author wilian
 */
public class GenerarReporteErroresHTML {
    
    public void generar(ErrorLexico[] errores, int cantidadErrores, String rutaSalida){
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        html.append("<html lang=\"es\">");
        
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Reporte de Errores - PromptZal</title>");

        html.append("<style>");
        html.append(obtenerEstilos());
        html.append("</style>");

        html.append("</head>");

        html.append("<body>");

        html.append("<div class=\"contenedor\">");

        html.append("<header>");
        html.append("<h1>PromptZal</h1>");
        html.append("<p>Reporte de Errores Lexicos</p>");
        html.append("</header>");
        
        if (cantidadErrores == 0) {
              html.append("<div class=\"sin-errores\">");

            html.append("<h2>Analisis completado</h2>");

            html.append("<p>");
            html.append("No se encontraron errores lexicos.");
            html.append("</p>");

            html.append("</div>");

        } else {

            html.append("<div class=\"resumen\">");

            html.append("<strong>Total de errores:</strong> ");

            html.append(cantidadErrores);

            html.append("</div>");


            html.append("<table>");

            html.append("<thead>");

            html.append("<tr>");
            html.append("<th>No.</th>");
            html.append("<th>Lexema</th>");
            html.append("<th>Descripcion</th>");
            html.append("<th>Fila</th>");
            html.append("<th>Columna</th>");
            html.append("</tr>");

            html.append("</thead>");

            html.append("<tbody>");
            
            for (int i = 0; i < cantidadErrores; i++) {
                ErrorLexico error = errores[i];
                  html.append("<tr>");

                html.append("<td>");
                html.append(i + 1);
                html.append("</td>");

                html.append("<td class=\"lexema\">");
                html.append(escaparHTML(error.getLexema()));
                html.append("</td>");

                html.append("<td>");
                html.append(escaparHTML(error.getDescripcion()));
                html.append("</td>");

                html.append("<td>");
                html.append(error.getFila());
                html.append("</td>");

                html.append("<td>");
                html.append(error.getColumna());
                html.append("</td>");

                html.append("</tr>");
            }


            html.append("</tbody>");

            html.append("</table>");
        }


        html.append("<footer>");
        html.append("Laboratorio de Lenguajes Formales y de Programacion");
        html.append("</footer>");

        html.append("</div>");

        html.append("</body>");

        html.append("</html>");
        
        escribirArchivo(rutaSalida,html.toString());
            }
    
    private String obtenerEstilos(){
            return """
                      body{
                                margin 0;
                                padding: 30px;
                                font-family: Arial, Helvetica, sans-serif;
                                background-color: #f4f6f8;
                                color:  #252525;
                   }
                   
                   .contenedor {
                                       max-width: 1100px;
                                       margin: 0 auto;
                                       background-color: white;
                                       border: 1px solid #d9dee3;
                                       border-radius: 8px;
                                       overflow: hidden;
                                   }
                   
                                   header {
                                       background-color: #5e2b2b;
                                       color: white;
                                       padding: 25px 30px;
                                   }
                   
                                   header h1 {
                                       margin: 0;
                                   }
                   
                                   header p {
                                       margin: 6px 0 0;
                                       color: #f2dddd;
                                   }
                   
                                   .resumen {
                                       margin: 25px 30px 15px;
                                       padding: 14px;
                                       background-color: #f9eeee;
                                       border-left: 4px solid #a33a3a;
                                   }
                   
                                   table {
                                       width: calc(100% - 60px);
                                       margin: 20px 30px 30px;
                                       border-collapse: collapse;
                                   }
                   
                                   thead {
                                       background-color: #7a3434;
                                       color: white;
                                   }
                   
                                   th,
                                   td {
                                       padding: 11px 13px;
                                       border: 1px solid #d9dee3;
                                       text-align: left;
                                   }
                   
                                   tbody tr:nth-child(even) {
                                       background-color: #fbf7f7;
                                   }
                   
                                   .lexema {
                                       font-family: "Courier New", monospace;
                                       font-weight: bold;
                                   }
                   
                                   .sin-errores {
                                       margin: 30px;
                                       padding: 25px;
                                       background-color: #edf7ef;
                                       border-left: 5px solid #397846;
                                   }
                   
                                   .sin-errores h2 {
                                       color: #2e6639;
                                       margin-top: 0;
                                   }
                   
                                   footer {
                                       padding: 18px;
                                       background-color: #eef1f4;
                                       text-align: center;
                                       color: #626b73;
                                   }
                   """;
    
    }
    
    private String escaparHTML(String texto){
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
    
    private void escribirArchivo(String rutaSalida,String contenido){
        try {
            Files.writeString(Path.of(rutaSalida), contenido);
            System.out.println("Reporte de errores generado " + rutaSalida);
        } catch (IOException e) {
            System.out.println("Error al generar reporte de errores");
        }
    }

        }
    
    

