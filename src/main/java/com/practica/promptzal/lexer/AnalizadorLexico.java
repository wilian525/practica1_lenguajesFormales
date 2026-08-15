/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.lexer;

/**
 *
 * @author wilian
 */
public class AnalizadorLexico {
    
    private String entrada;
    private int posicion;
    private int fila;
    private int columna;
    private int numeroToken;

    private Token[] tokens;
    private ErrorLexico[] errores;

    private int cantidadTokens;
    private int cantidadErrores;
   
    public AnalizadorLexico(String entrada){
        this.entrada = entrada;
        this.posicion = 0;
        this.fila = 1;
        this.columna = 1;
        this.numeroToken =1;
        
         tokens = new Token[10];
        errores = new ErrorLexico[10];

        cantidadTokens = 0;
        cantidadErrores = 0;
  
    }
    
   public void analizar() {

    while (posicion < entrada.length()) {

        char actual = entrada.charAt(posicion);

        // Espacios y saltos de línea
        if (actual == ' '
                || actual == '\t'
                || actual == '\r'
                || actual == '\n') {

            avanzar();

        // Cadenas
        } else if (actual == '"') {

            analizarCadena();

        // Comentario de línea
        } else if (actual == '/'
                && siguienteEs('/')) {

            ignorarComentarioLinea();

        // Comentario de bloque
        } else if (actual == '/'
                && siguienteEs('*')) {

            ignorarComentarioBloque();

        // Palabras e identificadores
        } else if (esLetra(actual) || actual == '_') {

            analizadorPalabra();

        // Números
        } else if (esDigito(actual)) {

            analizarNumero();

        // Directivas
        } else if (actual == '@') {

            analizadorDirectivo();

        // Símbolos y errores
        } else {

            analizadorSimbolo();
        }
    }
}
    
       // reconoce palabra reservada,Ia,funciones e identificadores
    public void analizadorPalabra(){
        int filaInicio = fila;
        int columnaInicio = columna;
        
        StringBuilder lexema = new StringBuilder();
        
         while(posicion  < entrada.length()){
             char actual = entrada.charAt(posicion);
             
             if (esLetra(actual) || esDigito(actual) || actual == '_') {
                 lexema.append(actual);
                 avanzar();
             }else{
             break;
         }
    }
    
    String palabra = lexema.toString();
    TipoToken tipo = obtenerTipoPalabra(palabra);
    
    agregarToken(palabra,tipo,filaInicio,columnaInicio);
    
    }
            // palabra reservada
   private TipoToken obtenerTipoPalabra(String palabra){
       if (palabra.equals("AGENTE" ) 
                || palabra.equals("contexto")
                || palabra.equals("variable")
                || palabra.equals("EJECUTAR")
                || palabra.equals("EXPORTAR")) {
  
           return TipoToken.RESERVADA;
       }
        // Comando Ia
       if (palabra.equals("PREGUNTAR")
            || palabra.equals("GENERAR")
            || palabra.equals("RESUMIR")
            || palabra.equals("ANALIZAR")
            || palabra.equals("TRADUCIR")
            || palabra.equals("CLASIFICAR")
            || palabra.equals("EXTRAER")) {

        return TipoToken.COMANDO_IA;
    }
       // connectores
       if (palabra.equals("SOBRE")                  
                || palabra.equals("DESDE")
                || palabra.equals("EN")
                || palabra.equals("COMO") ) {
           
           return  TipoToken.CONECTOR;
       }
       
       if (palabra.equals("CARGAR")) {
            return TipoToken.FUNCION;
       }
       //
       return TipoToken.IDENTIFICADOR;
   }
   
   //reconoce numeros enteros y decimales
   private void analizarNumero(){
       int filaInicio = fila;
       int columnaInico = columna;
       
       StringBuilder lexema = new StringBuilder();
       boolean tieneDecimal = false;
       
       while(posicion < entrada.length()){
           char actual = entrada.charAt(posicion);
           if (esDigito(actual)) {
               lexema.append(actual);
               avanzar();
           } // solo acepta si : todavia no aparecio otro punto , despues del punto  viene un numero
           else if(actual == '.' &&  !tieneDecimal && siguienteEsDigito()){
               tieneDecimal = true;
               lexema.append(actual);
               avanzar();
           }
           else{
               break;
           }
       }
       if (tieneDecimal) {
                agregarToken(lexema.toString(),TipoToken.DECIMAL,filaInicio,columnaInico);
       }
       else{
           agregarToken(lexema.toString(),TipoToken.ENTERO,filaInicio,columnaInico);
       }
   }
        
     // reconoce las tres directivas 
private void analizadorDirectivo(){
    int filaInicio = fila;
    int columnaInicio = columna;
    StringBuilder lexema = new StringBuilder();
    lexema.append('@');
    avanzar();
    
    while(posicion < entrada.length() ){
       char actual = entrada.charAt(posicion);
       
        if (esLetra(actual) || esDigito(actual) || actual == '_') {
            lexema.append(actual);
            avanzar();
        }else{
            break;
        }
    }
    String directiva = lexema.toString();
    if (directiva.equals("@modelo") || directiva.equals("@rol") || directiva.equals("@formato")) {
            agregarToken(directiva,TipoToken.DIRECTIVA,filaInicio,columnaInicio);
    } else{
        agregarError(directiva,"Directiva no reconocida", filaInicio,columnaInicio);
    }
}   
        
// reconoce operadores
    private void analizadorSimbolo(){
        char actual = entrada.charAt(posicion);
       
        int filaInicio = fila;
        int columnaInicio = columna;
        
        switch(actual){
            case '=':
                 agregarToken("=",TipoToken.OPERADOR,filaInicio,columnaInicio);
                 avanzar();
                 break;
              
            case '+':
                agregarToken("+",TipoToken.OPERADOR,filaInicio,columnaInicio);
                avanzar();
                break;
                
             case'{':
                 agregarToken("{",TipoToken.DELIMITADOR,filaInicio,columnaInicio);
                 avanzar();
                 break;
                 
             case '}':
                 agregarToken("}",TipoToken.DELIMITADOR,filaInicio,columnaInicio);
                 avanzar();
                 break;
                 
                 case '(':
                agregarToken("(",TipoToken.DELIMITADOR, filaInicio,columnaInicio );
                avanzar();
                break;

            case ')':
                agregarToken( ")",TipoToken.DELIMITADOR,filaInicio, columnaInicio);
                avanzar();
                break;

            case ',':
                agregarToken( ",",TipoToken.DELIMITADOR, filaInicio, columnaInicio);
                avanzar();
                break;


            case '-':
                if (siguienteEs('>')) {
                    agregarToken("->", TipoToken.CONECTOR,filaInicio,columnaInicio );

                    // Consumimos -
                    avanzar();

                    // Consumimos >
                    avanzar();

                } else {
                    agregarError( String.valueOf(actual),"Caracter no reconocido",filaInicio, columnaInicio);
                    avanzar();
                }
                break;

            default:
                agregarError( String.valueOf(actual),"Caracter no reconocido", filaInicio, columnaInicio);
                avanzar();
                break;
        }
    }
    
    // Comprueba el caracter siguiente no mueve la posicion
    private boolean siguienteEs(char esperado){
        if (posicion +1 >= entrada.length()) {
            return false;
        }
        return entrada.charAt(posicion +1 ) == esperado;
    }
    
    // Comprueba di despues de la posicion actual existe
    private boolean siguienteEsDigito(){
        if (posicion +1 >= entrada.length()) {
             return false;
        }
        char siguiente = entrada.charAt(posicion + 1);
        return esDigito(siguiente);
    }
    
    // Reconocimiento manual de letras
    private boolean esLetra(char caracter){
            return(caracter >= 'a' && caracter <= 'z' || (caracter >= 'A' && caracter <= 'Z'));
    }
    
    // Reconocimiento de numeros
    private boolean esDigito(char caracter){
        return caracter >= '0' && caracter <= '9';
    }
    
    //Avanza un caracter y matiene correctamente fila y columna
    private void avanzar() {

    if (posicion >= entrada.length()) {
        return;
    }

    char actual = entrada.charAt(posicion);

    posicion++;

    if (actual == '\n') {

        fila++;
        columna = 1;

    } else {

        columna++;
    }
}
    
    private void analizarCadena(){
        int filaInicio = fila;
        int columnaInicio = columna;
        
        StringBuilder lexema = new StringBuilder();
        boolean cerrada = false;
        
        //Guarda comillas iniciales
        lexema.append('"');
        avanzar();
        
        while(posicion < entrada.length()){
            char actual = entrada.charAt(posicion);
            
            //Encontramos comillas de cierre
            if (actual == '"') {
                lexema.append('"');
                avanzar();
                
                cerrada = true;
                break;
            }
            
            //si se llega a un salto de linea sin cerrar considera la cadena no cerrada
            if (actual == '\n') {
                break;
            }
            lexema.append(actual);
            avanzar();
        }
        if (cerrada) {
            agregarToken(lexema.toString(),TipoToken.CADENA,filaInicio,columnaInicio);
            
        }else{
            agregarError(lexema.toString(),"Cadena sin cerrar",filaInicio,columnaInicio);
        }
    }
    
    // guardar un nuevo token
    private void agregarToken(String lexema,TipoToken tipo,int fila,int columna){
        
        if (cantidadTokens == tokens.length) {
            aumentarArregloTokens();
        }
        Token nuevoToken = new Token(numeroToken, lexema,tipo,fila,columna);
        tokens[cantidadTokens] = nuevoToken;
        cantidadTokens++;
        numeroToken++;
    }
    
    // guarda los errores el programa no termina
    private void agregarError(String lexema,String descripcion,int fila, int columna){
        if (cantidadErrores == errores.length) {
            aumentarArregloErrores();
        }
        
         ErrorLexico nuevoError = new ErrorLexico( lexema,descripcion,fila,columna);
         
         errores[cantidadErrores] = nuevoError;
         cantidadErrores++;
    }
    
    private void aumentarArregloTokens() {

    Token[] nuevoArreglo= new Token[tokens.length * 2];

    for (int i = 0; i < tokens.length; i++) {

        nuevoArreglo[i] = tokens[i];
    }
    tokens = nuevoArreglo;
    }
    
    
    private void aumentarArregloErrores() {

    ErrorLexico[] nuevoArreglo
            = new ErrorLexico[errores.length * 2];
    
    for (int i = 0; i < errores.length; i++) {
        
        nuevoArreglo[i] = errores[i];
    }
    errores = nuevoArreglo;
}
    public void ignorarComentarioLinea(){
        // consumimos
        avanzar();
        avanzar();
    
        while(posicion < entrada.length()){
            char actual = entrada.charAt(posicion);
            if (actual == '\n') {
                break;
            }
            avanzar();
        }
    }
    
    public void ignorarComentarioBloque(){
        avanzar();
        avanzar();
        
        while(posicion < entrada.length()){
            if (entrada.charAt(posicion) == '*' && siguienteEs('/')) {
                avanzar();
                avanzar();
                return;
            }
            avanzar();
        }
    }

    public Token[] getTokens() {
        return tokens;
    }

    public ErrorLexico[] getErrores() {
        return errores;
    }

    public int getCantidadTokens() {
        return cantidadTokens;
    }

    public int getCantidadErrores() {
        return cantidadErrores;
    }
    
    
        }
    
    

