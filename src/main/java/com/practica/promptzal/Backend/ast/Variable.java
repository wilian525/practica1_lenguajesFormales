/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.ast;

/**
 *
 * @author wilian
 */
public class Variable {
    
    private String nombre;
    private Expresiones expresion;
    private int linea;

    public Variable(String nombre, Expresiones expresion, int linea) {
        this.nombre = nombre;
        this.expresion = expresion;
        this.linea = linea;
    }

    public String getNombre() {
        return nombre;
    }

    public Expresiones getExpresion() {
        return expresion;
    }

    public int getLinea() {
        return linea;
    }
}
