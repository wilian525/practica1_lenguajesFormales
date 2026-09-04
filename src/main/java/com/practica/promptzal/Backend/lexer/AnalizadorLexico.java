/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

import java.util.ArrayList;
import java.util.HashMap;

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

   private ArrayList<Token> tokens;
   private ArrayList<ErrorLexico> errores;
   private HashMap<String, TipoToken> palabrasEspeciales;
   
   private enum EstadoPalabra{
       Q0,
       Q1
   }
   private enum EstadoNumero{
       Q0,
       Q1_ENTERO,
       Q2_PUNTO_DECIMAL,
       Q3_DECIMAL
   }

    public AnalizadorLexico(String entrada) {

    this.entrada = entrada;

    this.posicion = 0;
    this.fila = 1;
    this.columna = 1;
    this.numeroToken = 1;

    this.tokens = new ArrayList<>();
    this.errores = new ArrayList<>();

    this.palabrasEspeciales = new HashMap<>();

    inicializarPalabrasEspeciales();
}
    
    private void inicializarPalabrasEspeciales(){
        palabrasEspeciales.put("AGENTE", TipoToken.RESERVADA);
        palabrasEspeciales.put("contexto", TipoToken.RESERVADA);
        palabrasEspeciales.put("variable", TipoToken.RESERVADA);
        palabrasEspeciales.put("EJECUTAR", TipoToken.RESERVADA);
       palabrasEspeciales.put("EXPORTAR", TipoToken.RESERVADA);

    // Comandos de IA
    palabrasEspeciales.put("PREGUNTAR", TipoToken.COMANDO_IA);
    palabrasEspeciales.put("GENERAR", TipoToken.COMANDO_IA);
    palabrasEspeciales.put("RESUMIR", TipoToken.COMANDO_IA);
    palabrasEspeciales.put("ANALIZAR", TipoToken.COMANDO_IA);
    palabrasEspeciales.put("TRADUCIR", TipoToken.COMANDO_IA);
    palabrasEspeciales.put("CLASIFICAR", TipoToken.COMANDO_IA);
    palabrasEspeciales.put("EXTRAER", TipoToken.COMANDO_IA);

    // Conectores
    palabrasEspeciales.put("SOBRE", TipoToken.CONECTOR);
    palabrasEspeciales.put("DESDE", TipoToken.CONECTOR);
    palabrasEspeciales.put("EN", TipoToken.CONECTOR);
    palabrasEspeciales.put("COMO", TipoToken.CONECTOR);

    // Funcion del sistema
    palabrasEspeciales.put("CARGAR", TipoToken.FUNCION);

    // Directivas
    palabrasEspeciales.put("@modelo", TipoToken.DIRECTIVA);
    palabrasEspeciales.put("@rol", TipoToken.DIRECTIVA);
    palabrasEspeciales.put("@formato", TipoToken.DIRECTIVA);
    }
   public void analizar() {

    while (posicion < entrada.length()) {

        char actual = entrada.charAt(posicion);

        // Espacios y saltos de línea
        if (actual == ' '
                || actual == '\t'
                || actual == '\r'
                || actual == '\n') {
// agregar mas tomar en cuenta
            avanzar();

        // Cadenas
        } else if (actual == '"') {
            analizarCadena();

        // Comentario de línea
        } else if (actual == '/' && siguienteEs('/')) {
            ignorarComentarioLinea();

        // Comentario de bloque
        } else if (actual == '/'&& siguienteEs('*')) {
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
        
        EstadoPalabra estado = EstadoPalabra.Q0;
        boolean terminado = false;
        
         while(!terminado && posicion  < entrada.length()){
             char actual = entrada.charAt(posicion);
             
             switch(estado){
                 case Q0:
                     if (esLetra(actual) || actual == '_') {
                         lexema.append(actual);
                         avanzar();
                         
                         estado = EstadoPalabra.Q1;
                     } else {
                         terminado = true;
                     } 
                     break;
                     
                 case Q1:
                     if (esLetra(actual) || esDigito(actual) || actual == '_') {
                         lexema.append(actual);
                         avanzar();
                     } else {
                         terminado = true;
                     }
                     break;
                }
             
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
      TipoToken tipo = palabrasEspeciales.get(palabra);
       if (tipo != null) {
           return tipo;
       }
       return TipoToken.IDENTIFICADOR;
   }
   
   //reconoce numeros enteros y decimales
   private void analizarNumero(){
       int filaInicio = fila;
       int columnaInico = columna;
       
       StringBuilder lexema = new StringBuilder();
       EstadoNumero estado = EstadoNumero.Q0;
       boolean terminado = false;
       boolean tieneDecimal = false;
       
       while(! terminado && posicion < entrada.length()){
           char actual = entrada.charAt(posicion);
           
           switch(estado){
               case Q0:
                   if (esDigito(actual)) {
                        lexema.append(actual);
                        avanzar();
                        
                        estado = EstadoNumero.Q1_ENTERO;
                   } else {
                       terminado = true;
                   }
                   break;
           }
           
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
    TipoToken tipo = palabrasEspeciales.get(directiva);
    
    if (tipo == TipoToken.DIRECTIVA) {
            agregarToken(directiva,TipoToken.DIRECTIVA,filaInicio,columnaInicio);
    } else {
        agregarError(directiva,"Directiva no reconocida",filaInicio,columnaInicio);
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
                
                case';':
                    agregarToken(";",TipoToken.DELIMITADOR,filaInicio,columnaInicio);
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
        
        Token nuevoToken = new Token(numeroToken,lexema,tipo,fila,columna);
        tokens.add(nuevoToken);
        numeroToken++;
      
    }
    
    // guarda los errores el programa no termina
    private void agregarError(String lexema,TipoErrorLexico tipo,int fila, int columna){
            ErrorLexico nuevoError = new ErrorLexico(lexema,tipo,fila,columna);
            errores.add(nuevoError);
    }
    
    private void ignorarComentarioLinea(){
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
    
    private void ignorarComentarioBloque(){
            int filaInicio = fila;
            int columnaInicio = columna;
        
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
        
       // agregar error
    }

    public Token[] getTokens() {
        return tokens.toArray(new Token[0]);
    }

    public ErrorLexico[] getErrores() {
        return errores.toArray(new ErrorLexico[0]);
    }

    public int getCantidadTokens() {
        return tokens.size();
    }

    public int getCantidadErrores() {
        return tokens.size();
    }
    
    
        }
    
    

