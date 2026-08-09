/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.lexer;

/**
 *
 * @author wilian
 */
public class Token {
    private int numero;
    private String lexema;
    private TipoToken tipo;
    private int fila;
    private int columna;
    
    public Token(int numero,String lexema, TipoToken tipo, int fila, int columna ){
        this.numero = numero;
        this.lexema = lexema;
        this.tipo = tipo;
        this.fila = fila;
        this.columna =  columna;
       
    }

    public int getNumero() {
        return numero;
    }

    public String getLexema() {
        return lexema;
    }

    public TipoToken getTipo() {
        return tipo;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return "Token{" + "numero=" + numero + ", lexema=" + lexema + ", tipo=" + tipo + ", fila=" + fila + ", columna=" + columna + '}';
    }
    
    
    
}
