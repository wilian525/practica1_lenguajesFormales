/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

/**
 *
 * @author wilian
 */
public enum TipoToken {
    
     DIRECTIVA("Directiva"),
    RESERVADA("Palabra reservada"),
    COMANDO_IA("Comando de IA"),
    CONECTOR("Conector"),
    IDENTIFICADOR("Identificador"),
    CADENA("Literal de cadena"),
    ENTERO("Literal entero"),
    DECIMAL("Literal decimal"),
    OPERADOR("Operador"),
    DELIMITADOR("Delimitador"),
    FUNCION("Funcion");
    
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
