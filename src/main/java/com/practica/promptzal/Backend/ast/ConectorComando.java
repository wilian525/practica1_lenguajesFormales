/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.ast;

/**
 *
 * @author wilian
 */
public class ConectorComando {
    
     private String tipo;
    private Expresiones expresion;

    public ConectorComando(String tipo, Expresiones expresion) {
        this.tipo = tipo;
        this.expresion = expresion;
    }

    public String getTipo() {
        return tipo;
    }

    public Expresiones getExpresion() {
        return expresion;
    }
}
