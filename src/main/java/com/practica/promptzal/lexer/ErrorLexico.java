/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.lexer;

/**
 *
 * @author wilian
 */
public class ErrorLexico {
    
    private String lexema;
    private String descripcion;
    private int fila;
    private int columna;
    
    public ErrorLexico(String lexema, String descripcion,int fila,int columna){
        this.lexema = lexema;
        this.descripcion = descripcion;
        this.fila = fila;
        this.columna = columna;
        
    }

    public String getLexema() {
        return lexema;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return "ErrorLexico{" + "lexema=" + lexema + ", descripcion=" + descripcion + ", fila=" + fila + ", columna=" + columna + '}';
    }
                 
    
            
    
    
}
