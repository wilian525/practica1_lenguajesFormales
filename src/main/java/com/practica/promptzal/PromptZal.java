/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.practica.promptzal;

import com.practica.promptzal.lexer.AnalizadorLexico;
import com.practica.promptzal.lexer.ErrorLexico;
import com.practica.promptzal.lexer.Token;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 *
 * @author wilian
 */


public class PromptZal {

    public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_BLUE = "\u001B[34m";


    public static void main(String[] args) {
        
          Scanner scanner = new Scanner(System.in);
        
        System.out.println(ANSI_RED+"▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌");
        System.out.println(ANSI_RED+"▐██████╗ ██████╗  ██████╗ ███╗   ███╗██████╗ ████████╗███████╗ █████╗ ██╗     ▌");
        System.out.println(ANSI_RED+"▐██╔══██╗██╔══██╗██╔═══██╗████╗ ████║██╔══██╗╚══██╔══╝╚══███╔╝██╔══██╗██║     ▌");
        System.out.println(ANSI_RED+"▐██████╔╝██████╔╝██║   ██║██╔████╔██║██████╔╝   ██║     ███╔╝ ███████║██║     ▌");
        System.out.println(ANSI_RED+"▐██╔═══╝ ██╔══██╗██║   ██║██║╚██╔╝██║██╔═══╝    ██║    ███╔╝  ██╔══██║██║     ▌");
        System.out.println(ANSI_RED+"▐██║     ██║  ██║╚██████╔╝██║ ╚═╝ ██║██║        ██║   ███████╗██║  ██║███████╗▌");
        System.out.println(ANSI_RED+"▐╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝╚═╝        ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝▌");
        System.out.println(ANSI_RED+"▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌"+ ANSI_RESET);
       
        System.out.println("\n");
       
        System.out.println(  "░▀█▀░█▀█░█▀▀░█▀▄░█▀▀░█▀▀░█▀▀░░░█░░░█▀█░░░█▀▄░█░█░▀█▀░█▀█░░░█▀▄░█▀▀░█░░░░░█▀█░█▀▄░█▀▀░█░█░▀█▀░█░█░█▀█░░░░░░█▀█░▀▀█");
        System.out.println(  "░░█░░█░█░█░█░█▀▄░█▀▀░▀▀█░█▀▀░░░█░░░█▀█░░░█▀▄░█░█░░█░░█▀█░░░█░█░█▀▀░█░░░░░█▀█░█▀▄░█░░░█▀█░░█░░▀▄▀░█░█░░░░░░█▀▀░▄▀░");
        System.out.println(  "░▀▀▀░▀░▀░▀▀▀░▀░▀░▀▀▀░▀▀▀░▀▀▀░░░▀▀▀░▀░▀░░░▀░▀░▀▀▀░░▀░░▀░▀░░░▀▀░░▀▀▀░▀▀▀░░░▀░▀░▀░▀░▀▀▀░▀░▀░▀▀▀░░▀░░▀▀▀░░░▀░░▀░░░▀▀▀");
        System.out.println("\n ingrese:");



        String ruta = scanner.nextLine();


        if (!ruta.endsWith(".pz")) {

            System.out.println(
                    "Error: el archivo debe tener extension .pz"
            );

            scanner.close();

            return;
        }


        try {

            String entrada = Files.readString(
                    Path.of(ruta)
            );


            AnalizadorLexico analizador
                    = new AnalizadorLexico(entrada);


            analizador.analizar();


            mostrarTokens(analizador);

            mostrarErrores(analizador);


        } catch (IOException error) {

            System.out.println(
                    "No se pudo leer el archivo."
            );

            System.out.println(
                    "Verifique que la ruta sea correcta."
            );
        }


        scanner.close();
    }


    private static void mostrarTokens(
            AnalizadorLexico analizador) {


        System.out.println();

        System.out.println(
                "===== TOKENS ====="
        );
        System.out.println("░▀█▀░█▀█░█░█░█▀▀░█▀█░█▀▀");
        System.out.println("░░█░░█░█░█▀▄░█▀▀░█░█░▀▀█");
        System.out.println("░░▀░░▀▀▀░▀░▀░▀▀▀░▀░▀░▀▀▀");



        System.out.printf(
                "%-5s %-25s %-22s %-8s %-8s%n",
                "No.",
                "Lexema",
                "Tipo",
                "Fila",
                "Columna"
        );


        Token[] tokens
                = analizador.getTokens();


        for (int i = 0;
                i < analizador.getCantidadTokens();
                i++) {


            Token token = tokens[i];


            System.out.printf(
                    "%-5d %-25s %-22s %-8d %-8d%n",
                    token.getNumero(),
                    token.getLexema(),
                    token.getTipo(),
                    token.getFila(),
                    token.getColumna()
            );
        }
    }


    private static void mostrarErrores(
            AnalizadorLexico analizador) {


        System.out.println();

        System.out.println(
                "===== ERRORES LEXICOS ====="
        );
         System.out.println(" ░█▀▀░█▀▄░█▀▄░█▀█░█▀▄░█▀▀░█▀▀░░░█░░░█▀▀░█░█░▀█▀░█▀▀░█▀█");
         System.out.println(" ░█▀▀░█▀▄░█▀▄░█░█░█▀▄░█▀▀░▀▀█░░░█░░░█▀▀░▄▀▄░░█░░█░░░█░█");
         System.out.println(" ░▀▀▀░▀░▀░▀░▀░▀▀▀░▀░▀░▀▀▀░▀▀▀░░░▀▀▀░▀▀▀░▀░▀░▀▀▀░▀▀▀░▀▀▀");


        if (analizador.getCantidadErrores() == 0) {

            System.out.println(
                    "No se encontraron errores lexicos."
            );

            return;
        }


        System.out.printf(
                "%-30s %-30s %-8s %-8s%n",
                "Lexema",
                "Descripcion",
                "Fila",
                "Columna"
        );


        ErrorLexico[] errores
                = analizador.getErrores();


        for (int i = 0;
                i < analizador.getCantidadErrores();
                i++) {


            ErrorLexico error = errores[i];


            System.out.printf(
                    "%-30s %-30s %-8d %-8d%n",
                    error.getLexema(),
                    error.getDescripcion(),
                    error.getFila(),
                    error.getColumna()
            );
        }
    }
}

