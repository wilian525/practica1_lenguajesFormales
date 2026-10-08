/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.parser;

/**
 *
 * @author wilian
 */
public class ErrorSintactico {
    
    private String tokenEncontrado;
    private String tokenEsperado;
    private int fila;
    private int columna;

    public ErrorSintactico(String tokenEncontrado, String tokenEsperado,
            int fila, int columna) {

        this.tokenEncontrado = tokenEncontrado;
        this.tokenEsperado = tokenEsperado;
        this.fila = fila;
        this.columna = columna;
    }

    public String getTokenEncontrado() {
        return tokenEncontrado;
    }

    public String getTokenEsperado() {
        return tokenEsperado;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getDescripcion() {
        return "Se encontro '" + tokenEncontrado
                + "' y se esperaba '" + tokenEsperado + "'";
    }
}
