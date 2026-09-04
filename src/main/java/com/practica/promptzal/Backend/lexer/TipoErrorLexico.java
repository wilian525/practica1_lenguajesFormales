/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

/**
 *
 * @author wilian
 */
public enum TipoErrorLexico {
     
    CARACTER_NO_RECONOCIDO("Caracter no reconocido"),
    CADENA_SIN_CERRAR("Cadena sin cerrar"),
    COMENTARIO_BLOQUE_SIN_CERRAR("Comentario de bloque sin cerrar"),
    DIRECTIVA_NO_RECONOCIDA("Directiva no reconocida");
    
    private final String descripcion;
    
    private TipoErrorLexico(String descripcion){
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
