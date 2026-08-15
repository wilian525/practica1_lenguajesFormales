/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.practica.promptzal;

import com.practica.promptzal.lexer.AnalizadorLexico;
import com.practica.promptzal.lexer.ErrorLexico;
import com.practica.promptzal.lexer.Token;

/**
 *
 * @author wilian
 */


public class PromptZal {

    public static void main(String[] args) {

        String entrada = """
                AGENTE analista {
                    variable ventas = CARGAR(datos)
                    RESUMIR ventas EN 100 -> resumen
                }

                EJECUTAR analista
                EXPORTAR resumen
                """;


        AnalizadorLexico analizador
                = new AnalizadorLexico(entrada);


        analizador.analizar();


        mostrarTokens(analizador);

        mostrarErrores(analizador);
    }


    private static void mostrarTokens(
            AnalizadorLexico analizador) {


        System.out.println(
                "===== TOKENS ====="
        );


        System.out.println(
                "No.\tLexema\tTipo\tFila\tColumna"
        );


        Token[] tokens = analizador.getTokens();


        for (int i = 0;
                i < analizador.getCantidadTokens();
                i++) {


            System.out.println(tokens[i]);
        }
    }


    private static void mostrarErrores(
            AnalizadorLexico analizador) {


        System.out.println();

        System.out.println(
                "===== ERRORES ====="
        );


        ErrorLexico[] errores
                = analizador.getErrores();


        if (analizador.getCantidadErrores() == 0) {

            System.out.println(
                    "No se encontraron errores lexicos."
            );

            return;
        }


        for (int i = 0;
                i < analizador.getCantidadErrores();
                i++) {


            System.out.println(errores[i]);
        }
    }
}
    

