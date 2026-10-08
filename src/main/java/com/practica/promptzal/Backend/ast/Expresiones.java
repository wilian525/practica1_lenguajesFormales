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
public class Expresiones {
    
     private ArrayList<Termino> terminos;

    public Expresiones() {
        terminos = new ArrayList<>();
    }

    public void agregarTermino(Termino termino) {
        terminos.add(termino);
    }

    public ArrayList<Termino> getTerminos() {
        return terminos;
    }
}
