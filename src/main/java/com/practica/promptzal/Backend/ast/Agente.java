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
public class Agente {
    
    private String nombre;
    private String contexto;
    private int linea;
    private ArrayList<Variable> variables;
    private ArrayList<Comando> comandos;

    public Agente(String nombre, String contexto, int linea) {
        this.nombre = nombre;
        this.contexto = contexto;
        this.linea = linea;
        this.variables = new ArrayList<>();
        this.comandos = new ArrayList<>();
    }

    public void agregarVariable(Variable variable) {
        variables.add(variable);
    }

    public void agregarComando(Comando comando) {
        comandos.add(comando);
    }

    public String getNombre() {
        return nombre;
    }

    public String getContexto() {
        return contexto;
    }

    public int getLinea() {
        return linea;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public ArrayList<Comando> getComandos() {
        return comandos;
    }
}
