/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.lexer;

/**
 *
 * @author wilian
 */
public class TipoToken {
    
    public static final TipoToken DIRECTIVA = new TipoToken("Directiva");
    public static final TipoToken RESERVADA = new TipoToken("Palabra reservada");
    public static final TipoToken COMANDO_IA = new TipoToken("Comando de IA");
    public static final TipoToken CONECTOR = new TipoToken("Conector");
    public static final TipoToken IDENTIFICADOR = new TipoToken("Identificador");
    public static final TipoToken CADENA = new TipoToken("Literal de cadena");
    public static final TipoToken ENTERO = new TipoToken("Literal entero");
    public static final TipoToken DECIMAL = new TipoToken("Literal decimal");
    public static final TipoToken OPERADOR = new TipoToken("Operador");
    public static final TipoToken DELIMITADOR = new TipoToken("Delimitador");
    public static final TipoToken FUNCION = new TipoToken("Funcion");
    
    private final String descripcion;
    
     private TipoToken(String descripcion){
         this.descripcion = descripcion;
     }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
     
     
}
