/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal;

/**
 *
 * @author wilian
 */
 
import com.practica.promptzal.archivos.GestorArchivo;
import com.practica.promptzal.lexer.AnalizadorLexico;
import com.practica.promptzal.lexer.ErrorLexico;
import com.practica.promptzal.lexer.Token;

import com.practica.promptzal.reporte.GeneradorReporteTokensHTML;
import com.practica.promptzal.reporte.GenerarReporteErroresHTML;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class AplicacionPromptZal {
    
  public static final String ANSI_RESET = "\u001B[0m";
public static final String ANSI_RED = "\u001B[31m";
public static final String ANSI_GREEN = "\u001B[32m";
public static final String ANSI_BLUE = "\u001B[34m";

    private final Scanner scanner;
    private GestorArchivo gestorArchivo;

    public AplicacionPromptZal() {
        scanner = new Scanner(System.in);
        gestorArchivo = new GestorArchivo();
    }


    /*
     * Inicia el programa y mantiene activo
     * el menú hasta que el usuario decida salir.
     */
    public void iniciar() {

        boolean ejecutando = true;

        mostrarEncabezado();

        while (ejecutando) {

            mostrarMenu();

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                case "1":
                    analizarArchivo();
                    break;

                case "2":
                    ejecutando = false;
                    System.out.println();
                    System.out.println("Programa finalizado.");
                    break;

                default:
                    System.out.println();
                    System.out.println(
                            "Opcion no valida. Intente nuevamente."
                    );
                    break;
            }
        }

        scanner.close();
    }


    /*
     * Muestra las opciones principales.
     */
    private void mostrarMenu() {

        System.out.println();
        System.out.println(
                "============= MENU PRINCIPAL ============="
        );

        System.out.println(
                "1. Analizar archivo .pz"
        );

        System.out.println(
                "2. Salir"
        );

        System.out.println(
                "=========================================="
        );

           System.out.println("            .---------------------------------------------------------.");
            System.out.println("|╔═╗┌─┐┬  ┌─┐┌─┐┌─┐┬┌─┐┌┐┌┌─┐  ┬ ┬┌┐┌┌─┐  ┌─┐┌─┐┌─┐┬┌─┐┌┐┌|");
            System.out.println("|╚═╗├┤ │  ├┤ │  │  ││ ││││├┤   │ ││││├─┤  │ │├─┘│  ││ ││││|");
            System.out.println("|╚═╝└─┘┴─┘└─┘└─┘└─┘┴└─┘┘└┘└─┘  └─┘┘└┘┴ ┴  └─┘┴  └─┘┴└─┘┘└┘|");
            System.out.println("'---------------------------------------------------------'");
            System.out.println("\n");
            System.out.print("Seleccione una opcion : "); 
        
    }


    /*
     * Solicita un archivo al usuario.
     *
     * Por ahora esta clase sigue realizando
     * la lectura del archivo.
     *
     * En el siguiente paso esta responsabilidad
     * se movera a GestorArchivo.
     */
    private void analizarArchivo() {

        System.out.println();

        System.out.println(
                "===== ANALIZAR ARCHIVO ====="
        );

             
        System.out.println(  "░▀█▀░█▀█░█▀▀░█▀▄░█▀▀░█▀▀░█▀▀░░░█░░░█▀█░░░█▀▄░█░█░▀█▀░█▀█░░░█▀▄░█▀▀░█░░░░░█▀█░█▀▄░█▀▀░█░█░▀█▀░█░█░█▀█░░░░░░█▀█░▀▀█");
        System.out.println(  "░░█░░█░█░█░█░█▀▄░█▀▀░▀▀█░█▀▀░░░█░░░█▀█░░░█▀▄░█░█░░█░░█▀█░░░█░█░█▀▀░█░░░░░█▀█░█▀▄░█░░░█▀█░░█░░▀▄▀░█░█░░░░░░█▀▀░▄▀░");
        System.out.println(  "░▀▀▀░▀░▀░▀▀▀░▀░▀░▀▀▀░▀▀▀░▀▀▀░░░▀▀▀░▀░▀░░░▀░▀░▀▀▀░░▀░░▀░▀░░░▀▀░░▀▀▀░▀▀▀░░░▀░▀░▀░▀░▀▀▀░▀░▀░▀▀▀░░▀░░▀▀▀░░░▀░░▀░░░▀▀▀"); 


        System.out.println("\nIngrese la ruta del archivo .pz");
        System.out.println("o escriba 0 para volver al menu:" );
        String ruta = scanner.nextLine().trim();

        // Regresar al menú
        if (ruta.equals("0")) {
            return;
        }


        // Validar extensión
        if (!gestorArchivo.esArchivoPz(ruta)) {
             System.out.println();
             System.out.println("El archivo debe tener extension .pz.");
             
            return;
        }
        
        if (!gestorArchivo.existeArchivo(ruta)) {
                    System.out.println();
                    System.out.println("No se encontro el archivo.");
                     System.out.println(  "Verifique la ruta e intente nuevamente.");

                      return;
            }

        try {

            String entrada = gestorArchivo.leerArchivo(ruta);

            AnalizadorLexico analizador  = new AnalizadorLexico(entrada);

            analizador.analizar();

            mostrarTokens(analizador);

            mostrarErrores(analizador);

            generarReportes(analizador, ruta   );

            System.out.println();
            System.out.println("Analisis finalizado." );


        } catch (IOException error) {
            System.out.println();
            System.out.println("No se pudo leer el archivo."  );
            System.out.println(    "Verifique que la ruta sea correcta.");
        }
    }

    /*
     * Muestra la tabla de tokens
     * requerida por la práctica.
     */
    private void mostrarTokens(
            AnalizadorLexico analizador) {

        System.out.println();
        System.out.println("===== TOKENS =====");

        System.out.printf(
                "%-5s %-30s %-22s %-8s %-8s%n",
                "No.",
                "Lexema",
                "Tipo",
                "Fila",
                "Columna"
        );


        Token[] tokens= analizador.getTokens();

        for (int i = 0; i < analizador.getCantidadTokens();i++) {

            Token token = tokens[i];

            System.out.printf(
                    "%-5d %-30s %-22s %-8d %-8d%n",
                    token.getNumero(),
                    token.getLexema(),
                    token.getTipo(),
                    token.getFila(),
                    token.getColumna()
            );
        }
    }


    /*
     * Muestra todos los errores encontrados.
     *
     * Si no existen errores, lo indica
     * explícitamente.
     */
    private void mostrarErrores(
            AnalizadorLexico analizador) {
        
        System.out.println();
        System.out.println( "===== ERRORES LEXICOS =====" );

        if (analizador.getCantidadErrores() == 0) {

            System.out.println("No se encontraron errores lexicos."   );

            return;
        }


        System.out.printf(
                "%-30s %-30s %-8s %-8s%n",
                "Lexema",
                "Descripcion",
                "Fila",
                "Columna"
        );

        ErrorLexico[] errores= analizador.getErrores();

        for (int i = 0;i < analizador.getCantidadErrores();  i++) {

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


    /*
     * Genera ambos reportes HTML.
     *
     * Se ejecuta haya o no errores.
     */
    private void generarReportes(  AnalizadorLexico analizador, String ruta) throws IOException {

         Path carpetaReportes= gestorArchivo.creerCarpetaReportes(ruta);

           Path rutaReporteTokens= gestorArchivo.obtenerRutaReporteTokens(ruta,carpetaReportes );

            Path rutaReporteErrores= gestorArchivo.obtenerRutaReporteErrores(   ruta,  carpetaReportes);

           GeneradorReporteTokensHTML reporteTokens= new GeneradorReporteTokensHTML();

            reporteTokens.generar(  analizador.getTokens(),analizador.getCantidadTokens(),  rutaReporteTokens.toString());


    GenerarReporteErroresHTML reporteErrores= new GenerarReporteErroresHTML();

          reporteErrores.generar(analizador.getErrores(),
            analizador.getCantidadErrores(),
            rutaReporteErrores.toString()
    );


        System.out.println();
        System.out.println(
                "===== REPORTES GENERADOS ====="
        );

         System.out.println(ANSI_BLUE + "░█▀▄░█▀▀░█▀█░█▀█░█▀▄░▀█▀░█▀▀░█▀▀░░░█▀▀░█▀▀░█▀█░█▀▀░█▀▄░█▀█░█▀▄░█▀█░█▀▀");
          System.out.println(ANSI_BLUE +"░█▀▄░█▀▀░█▀▀░█░█░█▀▄░░█░░█▀▀░▀▀█░░░█░█░█▀▀░█░█░█▀▀░█▀▄░█▀█░█░█░█░█░▀▀█");
          System.out.println(ANSI_BLUE +"░▀░▀░▀▀▀░▀░░░▀▀▀░▀░▀░░▀░░▀▀▀░▀▀▀░░░▀▀▀░▀▀▀░▀░▀░▀▀▀░▀░▀░▀░▀░▀▀░░▀▀▀░▀▀▀"+ ANSI_RESET);
            

        System.out.println( "Tokens: " + rutaReporteTokens );

        System.out.println( "Errores: "+ rutaReporteErrores);
    }


    /*
     * Encabezado de la aplicación.
     *
     * Puedes sustituir estas líneas después
     * por el ASCII que ya tienes.
     */
    private void mostrarEncabezado() {

          System.out.println();

        System.out.println(ANSI_RED + "============================================================" + ANSI_RESET);

        System.out.println(ANSI_RED+"▐▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▌");
        System.out.println(ANSI_RED+"▐██████╗ ██████╗  ██████╗ ███╗   ███╗██████╗ ████████╗███████╗ █████╗ ██╗     ▌");
        System.out.println(ANSI_RED+"▐██╔══██╗██╔══██╗██╔═══██╗████╗ ████║██╔══██╗╚══██╔══╝╚══███╔╝██╔══██╗██║     ▌");
        System.out.println(ANSI_RED+"▐██████╔╝██████╔╝██║   ██║██╔████╔██║██████╔╝   ██║     ███╔╝ ███████║██║     ▌");
        System.out.println(ANSI_RED+"▐██╔═══╝ ██╔══██╗██║   ██║██║╚██╔╝██║██╔═══╝    ██║    ███╔╝  ██╔══██║██║     ▌");
        System.out.println(ANSI_RED+"▐██║     ██║  ██║╚██████╔╝██║ ╚═╝ ██║██║        ██║   ███████╗██║  ██║███████╗▌");
        System.out.println(ANSI_RED+"▐╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝╚═╝        ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝▌");
        System.out.println(ANSI_RED+"▐▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▌"+ ANSI_RESET);
       

        System.out.println( ANSI_RED+ "                 ANALIZADOR LEXICO"+ ANSI_RESET );

        System.out.println(ANSI_RED + "============================================================"+ ANSI_RESET );

        System.out.println();
}
    
}
