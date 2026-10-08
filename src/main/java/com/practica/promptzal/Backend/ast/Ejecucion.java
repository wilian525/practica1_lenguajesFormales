/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.ast;

/**
 *
 * @author wilian
 */
public class Ejecucion {
    
       private String nombreAgente;
    private int linea;

    public Ejecucion(String nombreAgente, int linea) {
        this.nombreAgente = nombreAgente;
        this.linea = linea;
    }

    public String getNombreAgente() {
        return nombreAgente;
    }

    public int getLinea() {
        return linea;
    }
}
