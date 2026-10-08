/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.semantico;

/**
 *
 * @author wilian
 */
public class ErrorSemantico {

    private String descripcion;
    private int linea;

    public ErrorSemantico(String descripcion, int linea) {
        this.descripcion = descripcion;
        this.linea = linea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getLinea() {
        return linea;
    }
}
