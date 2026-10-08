/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.ast;

/**
 *
 * @author wilian
 */
public class Exportacion {

    private String nombreVariable;
    private int linea;

    public Exportacion(String nombreVariable, int linea) {
        this.nombreVariable = nombreVariable;
        this.linea = linea;
    }

    public String getNombreVariable() {
        return nombreVariable;
    }

    public int getLinea() {
        return linea;
    }
}
