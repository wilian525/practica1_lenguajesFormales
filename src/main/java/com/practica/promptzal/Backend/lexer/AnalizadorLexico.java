
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
   
   private enum EstadoDirectiva{
       Q0,
       Q1_ARROBA,
       Q2_DIRECTIVA
   }
   
   private enum EstadoCadena{
       Q0,
       Q1_CONTENIDO,
       Q2_CERRAR
   }
   
   private enum EstadoComentarioLinea{
       Q0,
       Q1_BARRA,
       Q2_COMENTARIO
   }
   
   private enum EstadoComentarioBloque{
       Q0,
       Q1_BARRA,
       Q2_CONTENIDO,
       Q3_POSIBLE_CIERRE,
       Q4_CERRADO
   }
   
   private enum EstadoFlecha{
       Q0,
       Q1_GUION,
       Q2_FLECHA
   }
   
   private enum EstadoSimbolo{
       Q0,
       Q1_ACEPTADO;
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
                   
               case Q1_ENTERO:
                    if (esDigito(actual)) {
                           lexema.append(actual);
                           avanzar();
                   } else if(actual == '.' && siguienteEsDigito()){
                       lexema.append(actual);
                       avanzar();
                       estado = EstadoNumero.Q2_PUNTO_DECIMAL;
                   } else {
                       terminado = true;
                   }
                    break;
               
               case Q2_PUNTO_DECIMAL:

                if (esDigito(actual)) {

                    lexema.append(actual);
                    avanzar();
                    estado = EstadoNumero.Q3_DECIMAL;

                } else {
                    terminado = true;
                }

                break;

            case Q3_DECIMAL:

                if (esDigito(actual)) {
                    lexema.append(actual);
                    avanzar();

                } else {
                    terminado = true;
                }

                break;
        }
    }

    if (estado == EstadoNumero.Q3_DECIMAL) {

        agregarToken( lexema.toString(),TipoToken.DECIMAL, filaInicio,columnaInico);

    } else {
        agregarToken(lexema.toString(),TipoToken.ENTERO,filaInicio,columnaInico);
    }
           }
       

        
     // reconoce las tres directivas 
private void analizadorDirectivo(){
    int filaInicio = fila;
    int columnaInicio = columna;
    StringBuilder lexema = new StringBuilder();
    EstadoDirectiva estado = EstadoDirectiva.Q0;
    boolean terminado = false;
    
    while (!terminado && posicion< entrada.length()){
       char actual = entrada.charAt(posicion);
       
       switch(estado){
           case Q0:
               if (actual == '@') {
                   lexema.append(actual);
                   avanzar();
                   estado = EstadoDirectiva.Q1_ARROBA;
               } else {
                   terminado = true;
                }
               break;
               
           case Q1_ARROBA:
               if (esLetra(actual)) {
                    lexema.append(actual);
                    avanzar();
                    estado = EstadoDirectiva.Q2_DIRECTIVA;
               } else {
                   terminado = true;
               }
               break;
               
           case Q2_DIRECTIVA:
               if (esLetra(actual) || esDigito(actual) || actual == '_') {
                     lexema.append(actual);
                     avanzar();
               } else {
                   terminado = true;
               }
               break;
       }
    }
    String directiva = lexema.toString();
    
    if (estado == EstadoDirectiva.Q2_DIRECTIVA && palabrasEspeciales.get(directiva) == TipoToken.DIRECTIVA) {
         agregarToken(directiva,TipoToken.DIRECTIVA,filaInicio,columnaInicio);
    } else {
    agregarError(directiva,TipoErrorLexico.DIRECTIVA_NO_RECONOCIDA,columnaInicio,filaInicio);
} 
}   
        
// reconoce operadores
    private void analizadorSimbolo(){
        char actual = entrada.charAt(posicion);
        int filaInicio = fila;
        int columnaInicio = columna;
        
        if (actual == '-') {
             analizarFlecha();
             return ;
        }
        EstadoSimbolo estado = EstadoSimbolo.Q0;
        String lexema = "";
        TipoToken tipo =  null;
        
        switch(estado){
            case Q0:
                switch(actual){
                    case '=':
                        lexema = "=";
                        tipo = TipoToken.OPERADOR;
                        estado = EstadoSimbolo.Q1_ACEPTADO;
                        break;
                        
                     case'+':
                            lexema = "+";
                            tipo =TipoToken.OPERADOR;
                            estado = EstadoSimbolo.Q1_ACEPTADO;
                            break;
                            
                     case'{':
                         lexema = "{";
                         tipo = TipoToken.DELIMITADOR;
                         estado = EstadoSimbolo.Q1_ACEPTADO;
                         break;
                            
                     case '}':
                          lexema = "}";
                          tipo = TipoToken.DELIMITADOR;
                         estado = EstadoSimbolo.Q1_ACEPTADO;
                         break;

                    case '(':
                          lexema = "(";
                           tipo = TipoToken.DELIMITADOR;
                          estado = EstadoSimbolo.Q1_ACEPTADO;
                         break;

                  case ')':
                           lexema = ")";
                              tipo = TipoToken.DELIMITADOR;
                           estado = EstadoSimbolo.Q1_ACEPTADO;
                           break;

                     case ',':
                          lexema = ",";
                           tipo = TipoToken.DELIMITADOR;
                          estado = EstadoSimbolo.Q1_ACEPTADO;
                          break;

                default:
                    break;
            }

            break;

        case Q1_ACEPTADO:
            break;
    }
        if (estado == EstadoSimbolo.Q1_ACEPTADO) {
             agregarToken(lexema,tipo,filaInicio,columnaInicio);
             avanzar();
        } else{
            agregarError(String.valueOf(actual),TipoErrorLexico.CARACTER_NO_RECONOCIDO,filaInicio,columnaInicio);
            avanzar();
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
        EstadoCadena estado = EstadoCadena.Q0;
        boolean terminado = false;
       
         while(!terminado && posicion < entrada.length()){
             char actual = entrada.charAt(posicion);
             
             switch(estado){
                 
                 case Q0:
                     if (actual == '"') {
                          lexema.append(actual);
                          avanzar();
                          estado = EstadoCadena.Q1_CONTENIDO;
                     } else {
                         terminado = true;
                     }
                     break;
                     
                 case Q1_CONTENIDO:
                     if (actual == '"') {
                         lexema.append(actual);
                         avanzar();
                         estado = EstadoCadena.Q2_CERRAR;
                         terminado = true;
                     } else if (actual == '\n'){
                         terminado = true;
                     } else {
                         lexema.append(actual);
                         avanzar();
                     }
                     break;
                     
                 case Q2_CERRAR:
                     terminado = true;
                     break;
             }
         }
         
         if (estado == EstadoCadena.Q2_CERRAR) {
             agregarToken(lexema.toString(),TipoToken.CADENA,filaInicio,columnaInicio);
        } else {
             agregarError(lexema.toString(),TipoErrorLexico.CADENA_SIN_CERRAR,filaInicio,columnaInicio);
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
        EstadoComentarioLinea estado = EstadoComentarioLinea.Q0;
        boolean terminado = false;
        
        while(!terminado && posicion  < entrada.length()){
                char actual = entrada.charAt(posicion);
                 switch(estado){
                     case Q0:
                         if (actual == '/') {
                              avanzar();
                              estado = EstadoComentarioLinea.Q1_BARRA;
                         } else {
                             terminado = true;
                         }
                         break;
                         
                     case Q1_BARRA:
                         if (actual == '/') {
                              avanzar();
                              estado = EstadoComentarioLinea.Q2_COMENTARIO;
                         } else {
                             terminado = true;
                         }
                          break;
                          
                     case Q2_COMENTARIO:
                         if (actual == '\n') {
                              terminado = true;
                         } else {
                             avanzar();
                         }
                         break;
                 }
        }
    }
    
    private void ignorarComentarioBloque(){
            int filaInicio = fila;
            int columnaInicio = columna;
         
            StringBuilder lexema = new StringBuilder();
            EstadoComentarioBloque estado = EstadoComentarioBloque.Q0;
            boolean terminado = false;
            
            while(!terminado && posicion < entrada.length()){
                char actual = entrada.charAt(posicion);
                
                switch(estado){
                    case Q0:
                        if (actual == '/') {
                             lexema.append(actual);
                             avanzar();
                             estado = EstadoComentarioBloque.Q1_BARRA;
                        } else {
                            terminado = true;
                        }
                        break;
                        
                    case Q1_BARRA:
                        if (actual == '*') {
                             lexema.append(actual);
                             avanzar();
                             estado = EstadoComentarioBloque.Q2_CONTENIDO;
                        } else {
                            terminado = true;
                        }
                        break;
                        
                    case Q2_CONTENIDO:
                        if (actual == '*') {
                             lexema.append(actual);
                             avanzar();
                             estado = EstadoComentarioBloque.Q3_POSIBLE_CIERRE;
                        } else {
                             lexema.append(actual);
                             avanzar();
                        }
                         break;
                         
                    case Q3_POSIBLE_CIERRE:
                        if (actual == '/') {
                             lexema.append(actual);
                             avanzar();
                             estado = EstadoComentarioBloque.Q4_CERRADO;
                             terminado = true;
                        } else if(actual == '*'){
                            lexema.append(actual);
                            avanzar();
                            estado = EstadoComentarioBloque.Q3_POSIBLE_CIERRE;
                        } else {
                            lexema.append(actual);
                            avanzar();
                            estado = EstadoComentarioBloque.Q2_CONTENIDO;
                            }
                        break;
                        
                    case Q4_CERRADO:
                        terminado = true;
                        break;
                } 
            }
            if (estado != EstadoComentarioBloque.Q4_CERRADO) {
             agregarError(lexema.toString(),TipoErrorLexico.COMENTARIO_BLOQUE_SIN_CERRAR,filaInicio,columnaInicio);
        }
    }
    
    private void analizarFlecha(){
        int filaInicio = fila;
        int columnaInicio = columna;
        StringBuilder lexema = new StringBuilder();
        
        EstadoFlecha estado = EstadoFlecha.Q0;
        boolean terminado = false;
        
        while(!terminado && posicion < entrada.length()){
            char actual = entrada.charAt(posicion);
            switch(estado){
                
                case Q0:
                    if (actual == '-') {
                         lexema.append(actual);
                         avanzar();
                         estado = EstadoFlecha.Q1_GUION;
                    } else {
                        terminado = true;
                    }
                    break;
                    
                case Q1_GUION:
                    if (actual == '>') {
                         lexema.append(actual);
                         avanzar();
                         estado = EstadoFlecha.Q2_FLECHA;
                    }
                    terminado = true;
                    break;
                    
                case Q2_FLECHA:
                    terminado = true;
                    break;
            }
        }
        if (estado == EstadoFlecha.Q2_FLECHA) {
             agregarToken(lexema.toString(),TipoToken.CONECTOR,filaInicio,columnaInicio);
        } else {
            agregarError(lexema.toString(), TipoErrorLexico.CARACTER_NO_RECONOCIDO,filaInicio,columnaInicio);
        }
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
        return errores.size();
    }
    
    public ArrayList<Token> getListaTokens() {

    return tokens;
}
    
    public ArrayList<ErrorLexico> getListaErrores() {

    return errores;
}
    
        }
    
    

