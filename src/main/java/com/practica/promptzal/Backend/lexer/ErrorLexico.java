/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

/**
 *
 * @author wilian
 */
public class ErrorLexico {
    
    private String lexema;
    private TipoErrorLexico tipo;
    private int fila;
    private int columna;
    
    public ErrorLexico(String lexema,TipoErrorLexico tipo,int fila,int columna){
        this.lexema = lexema;
        this.tipo = tipo;
        this.fila = fila;
        this.columna = columna;
        
    }

    public String getLexema() {
        return lexema;
    }
    
    public TipoErrorLexico getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return tipo.getDescripcion();
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {

    return "ErrorLexico{"
            + "lexema='" + lexema + '\''
            + ", tipo='" + tipo + '\''
            + ", fila=" + fila
            + ", columna=" + columna
            + '}';
}
                 
    
            
    
    
}
