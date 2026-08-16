/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.practica.promptzal;

import com.practica.promptzal.lexer.AnalizadorLexico;
import com.practica.promptzal.lexer.ErrorLexico;
import com.practica.promptzal.lexer.Token;
import com.practica.promptzal.reporte.GeneradorReporteTokensHTML;
import com.practica.promptzal.reporte.GenerarReporteErroresHTML;
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

          boolean programaActivo = true;

        while (programaActivo) {
            mostrarEncabezado();
            mostrarMenu();

            System.out.println("            .---------------------------------------------------------.");
            System.out.println("|╔═╗┌─┐┬  ┌─┐┌─┐┌─┐┬┌─┐┌┐┌┌─┐  ┬ ┬┌┐┌┌─┐  ┌─┐┌─┐┌─┐┬┌─┐┌┐┌|");
            System.out.println("|╚═╗├┤ │  ├┤ │  │  ││ ││││├┤   │ ││││├─┤  │ │├─┘│  ││ ││││|");
            System.out.println("|╚═╝└─┘┴─┘└─┘└─┘└─┘┴└─┘┘└┘└─┘  └─┘┘└┘┴ ┴  └─┘┴  └─┘┴└─┘┘└┘|");
            System.out.println("'---------------------------------------------------------'");
             System.out.println("\n");
            System.out.print("Seleccione una opcion : "); 

            String opcion = scanner.nextLine().trim();

            switch (opcion) {

                case "1":
                    analizarArchivo(scanner);
                    pausar(scanner);

                    break;


                case "2":
                    System.out.println();
                    System.out.println("Programa finalizado.");
                    programaActivo = false;

                    break;

                default:
                    System.out.println();
                    System.out.println( "Opcion no valida."
                    );

                    System.out.println("Ingrese 1 para analizar o 2 para salir.");
                    pausar(scanner);

                    break;
            }
        }
        scanner.close();
    }


    private static void mostrarEncabezado() {
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


    private static void mostrarMenu() {
        System.out.println( "============== MENU PRINCIPAL ==============" );

        System.out.println(  "1. Analizar archivo .pz");

        System.out.println( "2. Salir" );

        System.out.println( "============================================");

        System.out.println();
    }


    private static void analizarArchivo( Scanner scanner) {

        boolean rutaCorrecta = false;

        while (!rutaCorrecta) {

            System.out.println();
             System.out.println("\n");
             System.out.println("\n");
             System.out.println("\n");
            System.out.println("\n");
            System.out.println(
                    "===== ANALIZAR ARCHIVO ====="
            );
            
        System.out.println(  "░▀█▀░█▀█░█▀▀░█▀▄░█▀▀░█▀▀░█▀▀░░░█░░░█▀█░░░█▀▄░█░█░▀█▀░█▀█░░░█▀▄░█▀▀░█░░░░░█▀█░█▀▄░█▀▀░█░█░▀█▀░█░█░█▀█░░░░░░█▀█░▀▀█");
        System.out.println(  "░░█░░█░█░█░█░█▀▄░█▀▀░▀▀█░█▀▀░░░█░░░█▀█░░░█▀▄░█░█░░█░░█▀█░░░█░█░█▀▀░█░░░░░█▀█░█▀▄░█░░░█▀█░░█░░▀▄▀░█░█░░░░░░█▀▀░▄▀░");
        System.out.println(  "░▀▀▀░▀░▀░▀▀▀░▀░▀░▀▀▀░▀▀▀░▀▀▀░░░▀▀▀░▀░▀░░░▀░▀░▀▀▀░░▀░░▀░▀░░░▀▀░░▀▀▀░▀▀▀░░░▀░▀░▀░▀░▀▀▀░▀░▀░▀▀▀░░▀░░▀▀▀░░░▀░░▀░░░▀▀▀"); 

            System.out.print(
                    "\n Ingrese la ruta del archivo .pz "
                    + "o escriba 0 para volver al menu: "
            );

            String ruta = scanner.nextLine().trim();

             // permite regresar al menu sin cerrar programa
            if (ruta.equals("0")) {
                return;
            }

            //verifica que el ususario escriba algo
            if (ruta.isEmpty()) {

                System.out.println();
                System.out.println("Error: debe ingresar una ruta." );
                
                continue;
            }

            //se valida la ruta o extencion 
            if (!ruta.toLowerCase().endsWith(".pz")) {

                System.out.println();
                System.out.println( "Error: el archivo debe tener extension .pz");
                System.out.println( "Intente nuevamente.");

                continue;
            }

            Path rutaArchivo;
            try {

                rutaArchivo = Path.of(ruta)
                        .toAbsolutePath()
                        .normalize();

            } catch (Exception error) {
                System.out.println();
                System.out.println(  "Error: la ruta ingresada no es valida." );

                continue;
            }

            // se comprueba si existe
            if (!Files.exists(rutaArchivo)) {

                System.out.println();
                System.out.println( "Error: el archivo no existe."  );
                System.out.println( "Ruta buscada:" );
                System.out.println( rutaArchivo);

                continue;
            }

            // se comprueba si es archivo o una carpeta
            if (!Files.isRegularFile(rutaArchivo)) {

                System.out.println();
                System.out.println("Error: la ruta no corresponde a un archivo." );

                continue;
            }

            try {

                // lee todo el archivo 
                String entrada = Files.readString( rutaArchivo);

                
                 // Creamos el analizador.
                AnalizadorLexico analizador  = new AnalizadorLexico(entrada);

                 //Ejecutamos el analisis.
                analizador.analizar();

                 //Resultados en consola.
                 
                mostrarTokens(analizador);
                mostrarErrores(analizador);

                
                //  Generamos los reportes HTML.
                generarReportes( analizador, rutaArchivo);

                System.out.println();
                System.out.println(
                        ANSI_GREEN
                        + "Analisis finalizado correctamente."
                        + ANSI_RESET );

                rutaCorrecta = true;

            } catch (IOException error) {
                System.out.println();
                System.out.println("No se pudo leer el archivo." );

                System.out.println(
                        "Verifique los permisos "
                        + "o la ruta e intente nuevamente." );

            } catch (Exception error) {

                /*
                 * Evitamos que un error inesperado
                 * cierre todo el programa.
                 */
                System.out.println();

                System.out.println(
                        "Ocurrio un problema durante el analisis."
                );

                System.out.println(
                        "El programa continuara funcionando."
                );

                System.out.println(
                        "Detalle: "
                        + error.getMessage()
                );
            }
        }
    }


    private static void generarReportes(
            AnalizadorLexico analizador,
            Path rutaArchivo) {

        try {

            /*
             * La carpeta de reportes se crea
             * junto al archivo .pz.
             */
            Path carpetaArchivo
                    = rutaArchivo.getParent();


            /*
             * Por seguridad, aunque normalmente
             * siempre existira un padre.
             */
            if (carpetaArchivo == null) {

                carpetaArchivo = Path.of(".")
                        .toAbsolutePath()
                        .normalize();
            }


            Path carpetaReportes
                    = carpetaArchivo.resolve(
                            "reportes"
                    );


            /*
             * Si la carpeta no existe,
             * Java la crea.
             */
            Files.createDirectories(
                    carpetaReportes
            );


            Path rutaReporteTokens
                    = carpetaReportes.resolve(
                            "reporte_tokens.html"
                    );


            Path rutaReporteErrores
                    = carpetaReportes.resolve(
                            "reporte_errores.html"
                    );


            /*
             * Reporte de tokens.
             */
            GeneradorReporteTokensHTML reporteTokens
                    = new GeneradorReporteTokensHTML();


            reporteTokens.generar(
                    analizador.getTokens(),
                    analizador.getCantidadTokens(),
                    rutaReporteTokens.toString()
            );


            /*
             * Reporte de errores.
             */
            GenerarReporteErroresHTML reporteErrores
                    = new GenerarReporteErroresHTML();


            reporteErrores.generar(
                    analizador.getErrores(),
                    analizador.getCantidadErrores(),
                    rutaReporteErrores.toString()
            );


            System.out.println();

            System.out.println(
                    "===== REPORTES GENERADOS ====="
            );
            System.out.println("░█▀▄░█▀▀░█▀█░█▀█░█▀▄░▀█▀░█▀▀░█▀▀░░░█▀▀░█▀▀░█▀█░█▀▀░█▀▄░█▀█░█▀▄░█▀█░█▀▀");
            System.out.println("░█▀▄░█▀▀░█▀▀░█░█░█▀▄░░█░░█▀▀░▀▀█░░░█░█░█▀▀░█░█░█▀▀░█▀▄░█▀█░█░█░█░█░▀▀█");
            System.out.println("░▀░▀░▀▀▀░▀░░░▀▀▀░▀░▀░░▀░░▀▀▀░▀▀▀░░░▀▀▀░▀▀▀░▀░▀░▀▀▀░▀░▀░▀░▀░▀▀░░▀▀▀░▀▀▀");
            
            System.out.println(
                    "Reporte de tokens:"
            );

            System.out.println(
                    rutaReporteTokens
            );


            System.out.println();

            System.out.println(
                    "Reporte de errores:"
            );

            System.out.println(
                    rutaReporteErrores
            );


        } catch (IOException error) {

            /*
             * Si los reportes fallan,
             * no cerramos el programa.
             */
            System.out.println();

            System.out.println(
                    "No fue posible crear "
                    + "la carpeta de reportes."
            );

            System.out.println(
                    "El analisis se realizo, "
                    + "pero los reportes no pudieron guardarse."
            );

        } catch (Exception error) {

            System.out.println();

            System.out.println(
                    "Ocurrio un problema "
                    + "al generar los reportes."
            );

            System.out.println(
                    "El programa continuara funcionando."
            );
        }
    }


    private static void mostrarTokens(
            AnalizadorLexico analizador) {

        System.out.println();

        System.out.println(
                "===== TOKENS ====="
        );


        System.out.printf(
                "%-5s %-30s %-22s %-8s %-8s%n",
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
                    "%-5d %-30s %-22s %-8d %-8d%n",
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


    private static void pausar(
            Scanner scanner) {

        System.out.println();

        System.out.print(
                "Presione ENTER para continuar..."
        );

        scanner.nextLine();

        System.out.println();

    
}
}
