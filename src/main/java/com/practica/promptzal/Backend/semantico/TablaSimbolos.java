/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.semantico;

import java.util.ArrayList;

/**
 *
 * @author wilian
 */
public class TablaSimbolos {

    private ArrayList<Simbolo> simbolos;

    public TablaSimbolos() {
        simbolos = new ArrayList<>();
    }

    public void agregarSimbolo(Simbolo simbolo) {
        simbolos.add(simbolo);
    }

    public boolean existeAgente(String nombre) {
        for (Simbolo simbolo : simbolos) {
            if (simbolo.getTipo().equals("AGENTE")
                    && simbolo.getNombre().equals(nombre)) {
                return true;
            }
        }

        return false;
    }

    public Simbolo buscarAgente(String nombre) {
        for (Simbolo simbolo : simbolos) {
            if (simbolo.getTipo().equals("AGENTE")
                    && simbolo.getNombre().equals(nombre)) {
                return simbolo;
            }
        }

        return null;
    }

    public boolean existeEnAmbito(String nombre, String ambito) {
        for (Simbolo simbolo : simbolos) {
            if (simbolo.getNombre().equals(nombre)
                    && simbolo.getAmbito().equals(ambito)) {
                return true;
            }
        }

        return false;
    }

    public Simbolo buscarEnAmbito(String nombre, String ambito) {
        for (Simbolo simbolo : simbolos) {
            if (simbolo.getNombre().equals(nombre)
                    && simbolo.getAmbito().equals(ambito)) {
                return simbolo;
            }
        }

        return null;
    }

    public ArrayList<Simbolo> getSimbolos() {
        return simbolos;
    }

    public void limpiar() {
        simbolos.clear();
    }
}
