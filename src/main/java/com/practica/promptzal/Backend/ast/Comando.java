/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.ast;

import java.util.ArrayList;

/**
 *
 * @author wilian
 */
public class Comando {
    
     private String tipo;
    private Expresiones expresionPrincipal;
    private ArrayList<ConectorComando> conectores;
    private String variableDestino;
    private int linea;

    public Comando(String tipo, Expresiones expresionPrincipal,
            String variableDestino, int linea) {

        this.tipo = tipo;
        this.expresionPrincipal = expresionPrincipal;
        this.variableDestino = variableDestino;
        this.linea = linea;
        this.conectores = new ArrayList<>();
    }

    public void agregarConector(ConectorComando conector) {
        conectores.add(conector);
    }

    public String getTipo() {
        return tipo;
    }

    public Expresiones getExpresionPrincipal() {
        return expresionPrincipal;
    }

    public ArrayList<ConectorComando> getConectores() {
        return conectores;
    }

    public String getVariableDestino() {
        return variableDestino;
    }

    public int getLinea() {
        return linea;
    }
}
