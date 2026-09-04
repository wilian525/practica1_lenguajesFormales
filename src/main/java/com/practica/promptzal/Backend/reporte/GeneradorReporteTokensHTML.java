/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte;

import com.practica.promptzal.Backend.lexer.Token;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author wilian
 */
public class GeneradorReporteTokensHTML {
    
    public void generar( Token[] tokens,int cantidaTokens,String rutaSalida){
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>");
        html.append("<html lang = =\"es\">");
        
        html.append("<head>");
        html.append("<meta charset=\"UTF-8\">");
        html.append("<title>Reporte de Tokens - PromptZal</title>");

        html.append("<style>");
        html.append(obtenerEstilos());
        html.append("</style>");

        html.append("</head>");

        html.append("<body>");

        html.append("<div class=\"contenedor\">");

        html.append("<header>");
        html.append("<h1>PromptZal</h1>");
        html.append("<p>Reporte de Tokens</p>");
        html.append("</header>");

        html.append("<div class=\"resumen\">");
        html.append("<strong>Total de tokens:</strong> ");
        html.append(cantidaTokens);
        html.append("</div>");

        html.append("<table>");

        html.append("<thead>");
        html.append("<tr>");
        html.append("<th>No.</th>");
        html.append("<th>Lexema</th>");
        html.append("<th>Tipo</th>");
        html.append("<th>Fila</th>");
        html.append("<th>Columna</th>");
        html.append("</tr>");
        html.append("</thead>");

        html.append("<tbody>");
        
        for (int i = 0; i < cantidaTokens; i++) {
            Token token = tokens[i];
           

            html.append("<tr>");

            html.append("<td>");
            html.append(token.getNumero());
            html.append("</td>");

            html.append("<td class=\"lexema\">");
            html.append(escaparHTML(token.getLexema()));
            html.append("</td>");

            html.append("<td>");
            html.append(escaparHTML(token.getTipo().toString()));
            html.append("</td>");

            html.append("<td>");
            html.append(token.getFila());
            html.append("</td>");

            html.append("<td>");
            html.append(token.getColumna());
            html.append("</td>");

            html.append("</tr>");
        
        }
        html.append("</tbody>");
        html.append("</table>");
        
        html.append("<footer>");
        html.append("Laboratorio de Lenguajes Formales de Programacion");
        html.append("</footer>");
        
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        escribirArchivo(rutaSalida,html.toString());
    }
    
    private String obtenerEstilos(){
        return """
                    body{
                               margin: 0;
                               padding: 30px;
                               front-family: Arial,Helvetica, sans-serif;
                               background-color: #f4f6f8;
                               color: #252525;
                    }
                    .contenedor {
                              max-width: 1100px;
                              margin: 0 auto;
                              background-color: white;
                              boder: 1px solid #d9dee3;
                              boder-radius: 8px;
                             overflow: hidden;
                  }
               
                    header{
                              background-color: #23364d;
                              color: white;
                              padding: 25px 30px;
               }
                    header h1{
                                margin: 0;
               }
                    header p{
                                margin: 6px 0 0;
                                color: #dce4ed;
               }
                    .resumen{
                                margin: 25px 30px 15px;
                                padding: 14px;
                                background-color: #edf2f7;
                                border-left: 4px solid #3d5f82;
               }
                    table {
                               width: calc(100% - 60px);
                               margin: 20px 30px 30px;
                               border-collapse: collapse;
               }
                    thead{
                                background-color: #344f6b;
                                color: white;
               }
                    th,
                    td {
                                padding: 11px 13px;
                                border: 1px solid #d9dee3;
                                text-align: left;
               }
               
                    tbody tr:nth-child(even){
                                background-color #f8fafc;
               }
                    .lexema{
                                font-family: "Courier New", monospace;
                                font-weight: bold;
               }
                    footer{
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
            Files.writeString(Path.of(rutaSalida),contenido);
             System.out.println("Reporte de tokens generado" + rutaSalida);
        } catch (IOException e) {
            System.out.println("Error al generar reporte de tokens");
        }
    }
}
