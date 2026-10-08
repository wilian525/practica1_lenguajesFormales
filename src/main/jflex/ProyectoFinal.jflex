package com.practica.promptzal.Backend.lexer;

import java.util.ArrayList;

%%

%public
%class Lexer
%unicode
%line
%column
%type Token

%xstate COMENTARIO_BLOQUE

%{
    private int numeroToken = 1;
    private ArrayList<ErrorLexico> errores = new ArrayList<>();
    private StringBuilder comentarioBloque = new StringBuilder();
    private int filaComentario;
    private int columnaComentario;

    private Token crearToken(TipoToken tipo) {
        Token token = new Token(
            numeroToken,
            yytext(),
                                tipo,
                                yyline + 1,
                                yycolumn + 1
        );

        numeroToken++;
        return token;
    }

    private Token crearEOF() {
        Token token = new Token(
            numeroToken,
            "EOF",
            TipoToken.EOF,
            yyline + 1,
            yycolumn + 1
        );

        numeroToken++;
        return token;
    }

    private void agregarError(String lexema, TipoErrorLexico tipo, int fila, int columna) {
        ErrorLexico error = new ErrorLexico(
            lexema,
            tipo,
            fila,
            columna
        );

        errores.add(error);
    }

        public ArrayList<ErrorLexico> getErrores() {
        return errores;
    }
%}

     // Expresiones

     ESPACIO = [ \t\f\r\n]+
    IDENTIFICADOR = [a-zA-Z_][a-zA-Z0-9_]*
    NUMERO = [0-9]+(\.[0-9]+)?
    CADENA = \"[^\r\n\"]*\"
    CADENA_SIN_CERRAR = \"[^\r\n\"]*
    DIRECTIVA = "@modelo"|"@rol"|"@formato"
    DIRECTIVA_DESCONOCIDA = @[a-zA-Z_][a-zA-Z0-9_]*
    COMANDO_IA = "PREGUNTAR"|"GENERAR"|"RESUMIR"|"ANALIZAR"|"TRADUCIR"|"CLASIFICAR"|"EXTRAER"
    CONECTOR = "SOBRE"|"DESDE"|"EN"|"COMO"
    COMENTARIO_LINEA = "//"[^\r\n]*

%%

    // Espacios

    {ESPACIO} {
        /* Se ignoran */
    }

    // Comentarios

    {COMENTARIO_LINEA} {
        /* Se ignora */
    }

    "/*" {
        filaComentario = yyline + 1;
        columnaComentario = yycolumn + 1;
        comentarioBloque.setLength(0);
        comentarioBloque.append(yytext());
        yybegin(COMENTARIO_BLOQUE);
    }

    <COMENTARIO_BLOQUE> "*/" {
        comentarioBloque.append(yytext());
        yybegin(YYINITIAL);
    }

    <COMENTARIO_BLOQUE> [^*\r\n]+ {
        comentarioBloque.append(yytext());
    }

    <COMENTARIO_BLOQUE> "*" {
        comentarioBloque.append(yytext());
    }

    <COMENTARIO_BLOQUE> \r\n|\r|\n {
        comentarioBloque.append(yytext());
    }

    <COMENTARIO_BLOQUE><<EOF>> {
        agregarError(
            comentarioBloque.toString(),
                     TipoErrorLexico.COMENTARIO_BLOQUE_SIN_CERRAR,
                     filaComentario,
                     columnaComentario
        );

        yybegin(YYINITIAL);
        return crearEOF();
    }

    // Directivas

    {DIRECTIVA} {
        return crearToken(TipoToken.DIRECTIVA);
    }

    {DIRECTIVA_DESCONOCIDA} {
        agregarError(
            yytext(),
                     TipoErrorLexico.DIRECTIVA_NO_RECONOCIDA,
                     yyline + 1,
                     yycolumn + 1
        );
    }

    // Palabras de estructura

    "AGENTE" {
        return crearToken(TipoToken.AGENTE);
    }

    "contexto" {
        return crearToken(TipoToken.CONTEXTO);
    }

    "variable" {
        return crearToken(TipoToken.VARIABLE);
    }

    "EJECUTAR" {
        return crearToken(TipoToken.EJECUTAR);
    }

    "EXPORTAR" {
        return crearToken(TipoToken.EXPORTAR);
    }

    // Comandos IA

    {COMANDO_IA} {
        return crearToken(TipoToken.COMANDO_IA);
    }

    // Funcion del sistema

    "CARGAR" {
        return crearToken(TipoToken.CARGAR);
    }

    // Conectores

    {CONECTOR} {
        return crearToken(TipoToken.CONECTOR);
    }

    // Flecha

    "->" {
        return crearToken(TipoToken.FLECHA);
    }

    // Operadores

    "=" {
        return crearToken(TipoToken.IGUAL);
    }

    "+" {
        return crearToken(TipoToken.MAS);
    }

    // Delimitadores

    "{" {
    return crearToken(TipoToken.LLAVE_A);
    }

    "}" {
        return crearToken(TipoToken.LLAVE_C);
    }

    "(" {
        return crearToken(TipoToken.PAR_A);
    }

    ")" {
        return crearToken(TipoToken.PAR_C);
    }

    "," {
        return crearToken(TipoToken.COMA);
    }

    // Cadenas

    {CADENA} {
        return crearToken(TipoToken.CADENA);
    }

    {CADENA_SIN_CERRAR} {
        agregarError(
            yytext(),
                     TipoErrorLexico.CADENA_SIN_CERRAR,
                     yyline + 1,
                     yycolumn + 1
        );
    }

    // Numeros

    {NUMERO} {
        return crearToken(TipoToken.NUMERO);
    }

    // Identificadores

    {IDENTIFICADOR} {
        return crearToken(TipoToken.ID);
    }

    // Fin del archivo

    <<EOF>> {
        return crearEOF();
    }

    // Caracter invalido

    . {
        agregarError(yytext(),
                     TipoErrorLexico.CARACTER_NO_RECONOCIDO,
                     yyline + 1,
                     yycolumn + 1
        );
    }
