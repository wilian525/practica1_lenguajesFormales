/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.ast;

/**
 *
 * @author wilian
 */
public class Termino {
    
    private String tipo;
    private String valor;
    private String unidad;
    private int linea;

    public Termino(String tipo, String valor, int linea) {
        this.tipo = tipo;
        this.valor = valor;
        this.unidad = null;
        this.linea = linea;
    }

    public Termino(String tipo, String valor, String unidad, int linea) {
        this.tipo = tipo;
        this.valor = valor;
        this.unidad = unidad;
        this.linea = linea;
    }

    public String getTipo() {
        return tipo;
    }

    public String getValor() {
        return valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public int getLinea() {
        return linea;
    }
}
