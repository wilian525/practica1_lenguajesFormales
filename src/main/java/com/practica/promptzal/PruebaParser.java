package com.practica.promptzal;

import com.practica.promptzal.Backend.ast.Programa;
import com.practica.promptzal.Backend.generador.GeneradorPromptzal;
import com.practica.promptzal.Backend.lexer.Lexer;
import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import com.practica.promptzal.Backend.parser.ErrorSintactico;
import com.practica.promptzal.Backend.parser.Parser;
import com.practica.promptzal.Backend.reporte.GeneradorAST;
import com.practica.promptzal.Backend.semantico.AnalizadorSemantico;
import com.practica.promptzal.Backend.semantico.ErrorSemantico;
import com.practica.promptzal.Backend.semantico.Simbolo;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import com.practica.promptzal.Backend.reporte.GeneradorReporteSimbolos;
import com.practica.promptzal.Backend.reporte.GenerarReporteErroresHTML;
import com.practica.promptzal.Backend.reporte.GeneradorReporteAST;
import com.practica.promptzal.Backend.reporte.GeneradorReporteTokensHTML;
import com.practica.promptzal.Backend.reporte.GeneradorReporteEstadisticasHTML;

public class PruebaParser {

    public static void main(String[] args) {
        String codigo = """
        @modelo "claude-sonnet-4-6"
        @rol "analista de datos"
        @formato "markdown"

        AGENTE analista {
            contexto = "Eres un analista de datos experto"
            variable ventas = CARGAR("ventas.csv")
            PREGUNTAR "Cuales son las tendencias?" SOBRE ventas -> tendencias
            RESUMIR tendencias EN 100 palabras -> resumen
        }

        EJECUTAR analista
        EXPORTAR resumen
        """;

        probar(codigo);
    }

    private static void probar(String codigo) {
        try {
            Lexer lexer = new Lexer(new StringReader(codigo));
            ArrayList<Token> tokens = new ArrayList<>();

            Token token;

            do {
                token = lexer.yylex();
                tokens.add(token);
            } while (token.getTipo() != TipoToken.EOF);

            System.out.println("TOKENS");
            System.out.println("----------------");

            for (Token actual : tokens) {
                System.out.println(
                        actual.getTipo()
                        + " | "
                        + actual.getLexema()
                        + " | fila: "
                        + actual.getFila()
                        + " columna: "
                        + actual.getColumna()
                );
            }

            GeneradorReporteTokensHTML reporteTokens
                    = new GeneradorReporteTokensHTML();

            boolean reporteTokensGenerado
                    = reporteTokens.generar(tokens);

            System.out.println();
            System.out.println("REPORTE DE TOKENS");
            System.out.println("----------------");

            if (reporteTokensGenerado) {
                System.out.println(
                        "Reporte de tokens generado correctamente."
                );

                System.out.println(
                        "Ruta: "
                        + reporteTokens.getRutaReporte()
                );
            } else {
                System.out.println(
                        "No se pudo generar el reporte de tokens."
                );
            }

            System.out.println();
            System.out.println("ERRORES LEXICOS: " + lexer.getErrores().size());

            Parser parser = new Parser(tokens);
            Programa programa = parser.parsear();

            GeneradorReporteAST reporteAST
                    = new GeneradorReporteAST();

            boolean reporteASTGenerado
                    = reporteAST.generar(programa);

            System.out.println();
            System.out.println("REPORTE AST");
            System.out.println("----------------");

            if (reporteASTGenerado) {
                System.out.println(
                        "Reporte AST generado correctamente."
                );

                System.out.println(
                        "Ruta: "
                        + reporteAST.getRutaReporte()
                );
            } else {
                System.out.println(
                        "No se pudo generar el reporte AST."
                );
            }

            GeneradorAST generadorAST = new GeneradorAST();

            boolean astGenerado = generadorAST.generar(programa);

            System.out.println();
            System.out.println("AST GRAFICO");
            System.out.println("----------------");

            if (astGenerado) {
                System.out.println("AST generado correctamente.");
                System.out.println("DOT: " + generadorAST.getRutaDot());
                System.out.println("Imagen: " + generadorAST.getRutaImagen());
            } else {
                System.out.println("No se pudo generar el AST.");
            }

            AnalizadorSemantico semantico = new AnalizadorSemantico();
            semantico.analizar(programa);

            GenerarReporteErroresHTML reporteErrores
                    = new GenerarReporteErroresHTML();

            boolean reporteErroresGenerado = reporteErrores.generar(
                    lexer.getErrores(),
                    parser.getErrores(),
                    semantico.getErrores()
            );

            GeneradorReporteEstadisticasHTML reporteEstadisticas
                    = new GeneradorReporteEstadisticasHTML();

            boolean reporteEstadisticasGenerado
                    = reporteEstadisticas.generar(
                            tokens,
                            lexer.getErrores(),
                            parser.getErrores(),
                            semantico.getErrores(),
                            codigo
                    );

            System.out.println();
            System.out.println("REPORTE DE ESTADISTICAS");
            System.out.println("----------------");

            if (reporteEstadisticasGenerado) {
                System.out.println(
                        "Reporte de estadisticas generado correctamente."
                );

                System.out.println(
                        "Ruta: "
                        + reporteEstadisticas.getRutaReporte()
                );
            } else {
                System.out.println(
                        "No se pudo generar el reporte de estadisticas."
                );
            }

            System.out.println();
            System.out.println("REPORTE DE ERRORES");
            System.out.println("----------------");

            if (reporteErroresGenerado) {
                System.out.println(
                        "Reporte generado correctamente."
                );

                System.out.println(
                        "Ruta: "
                        + reporteErrores.getRutaReporte()
                );
            } else {
                System.out.println(
                        "No se pudo generar el reporte de errores."
                );
            }

            GeneradorReporteSimbolos reporteSimbolos
                    = new GeneradorReporteSimbolos();

            boolean reporteGenerado = reporteSimbolos.generar(
                    semantico.getTablaSimbolos()
            );

            System.out.println();
            System.out.println("REPORTE DE SIMBOLOS");
            System.out.println("----------------");

            if (reporteGenerado) {
                System.out.println("Reporte generado correctamente.");
                System.out.println(
                        "Ruta: " + reporteSimbolos.getRutaReporte()
                );
            } else {
                System.out.println(
                        "No se pudo generar el reporte de simbolos."
                );
            }

            GeneradorPromptzal generador = new GeneradorPromptzal();
            String promptGenerado = generador.generar(programa);

            System.out.println();
            System.out.println("PROMPTZAL GENERADO");
            System.out.println("----------------");
            System.out.println(promptGenerado);

            System.out.println();
            System.out.println("ERRORES SEMANTICOS");
            System.out.println("----------------");

            if (semantico.getErrores().isEmpty()) {
                System.out.println("No hay errores semanticos.");
            } else {
                for (ErrorSemantico error : semantico.getErrores()) {
                    System.out.println(
                            error.getDescripcion()
                            + " | linea: "
                            + error.getLinea()
                    );
                }
            }

            System.out.println();
            System.out.println("TABLA DE SIMBOLOS");
            System.out.println("----------------");

            for (Simbolo simbolo
                    : semantico.getTablaSimbolos().getSimbolos()) {

                System.out.println(
                        simbolo.getNombre()
                        + " | "
                        + simbolo.getTipo()
                        + " | "
                        + simbolo.getAmbito()
                        + " | linea: "
                        + simbolo.getLinea()
                );
            }

            System.out.println();
            System.out.println("ERRORES SINTACTICOS");
            System.out.println("----------------");

            if (parser.getErrores().isEmpty()) {
                System.out.println("No hay errores sintacticos.");
            } else {
                for (ErrorSintactico error : parser.getErrores()) {
                    System.out.println(
                            error.getDescripcion()
                            + " | fila: "
                            + error.getFila()
                            + " columna: "
                            + error.getColumna()
                    );
                }
            }

            System.out.println();
            System.out.println("AST");
            System.out.println("----------------");
            System.out.println("Directivas: " + programa.getDirectivas().size());
            System.out.println("Agentes: " + programa.getAgentes().size());
            System.out.println("Ejecuciones: " + programa.getEjecuciones().size());
            System.out.println("Exportaciones: " + programa.getExportaciones().size());

        } catch (IOException e) {
            System.out.println("Error al ejecutar el lexer: " + e.getMessage());
        }
    }
}
