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
public class Programa {
    
    private ArrayList<Directiva> directivas;
    private ArrayList<Agente> agentes;
    private ArrayList<String> ejecuciones;
    private ArrayList<String> exportaciones;

    public Programa() {
        directivas = new ArrayList<>();
        agentes = new ArrayList<>();
        ejecuciones = new ArrayList<>();
        exportaciones = new ArrayList<>();
    }

    public void agregarDirectiva(Directiva directiva) {
        directivas.add(directiva);
    }

    public void agregarAgente(Agente agente) {
        agentes.add(agente);
    }

    public void agregarEjecucion(String nombreAgente) {
        ejecuciones.add(nombreAgente);
    }

    public void agregarExportacion(String nombreVariable) {
        exportaciones.add(nombreVariable);
    }

    public ArrayList<Directiva> getDirectivas() {
        return directivas;
    }

    public ArrayList<Agente> getAgentes() {
        return agentes;
    }

    public ArrayList<String> getEjecuciones() {
        return ejecuciones;
    }

    public ArrayList<String> getExportaciones() {
        return exportaciones;
    }
    
}
