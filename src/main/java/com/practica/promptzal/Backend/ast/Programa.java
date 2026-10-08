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
    private ArrayList<Ejecucion> ejecuciones;
    private ArrayList<Exportacion> exportaciones;

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

    public void agregarEjecucion(Ejecucion ejecucion) {
        ejecuciones.add(ejecucion);
    }

    public void agregarExportacion(Exportacion exportacion) {
        exportaciones.add(exportacion);
    }

    public ArrayList<Directiva> getDirectivas() {
        return directivas;
    }

    public ArrayList<Agente> getAgentes() {
        return agentes;
    }

    public ArrayList<Ejecucion> getEjecuciones() {
        return ejecuciones;
    }

    public ArrayList<Exportacion> getExportaciones() {
        return exportaciones;
    }

}
