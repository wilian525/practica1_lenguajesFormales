/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

/**
 *
 * @author wilian
 */
public class EstadoAFD {
    
     private String nombre;

    private boolean aceptacion;

    public EstadoAFD(String nombre) {

        this.nombre = nombre;

        this.aceptacion = false;
    }

    public EstadoAFD(String nombre, boolean aceptacion) {

        this.nombre = nombre;

        this.aceptacion = aceptacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isAceptacion() {
        return aceptacion;
    }

    public void setAceptacion(boolean aceptacion) {
        this.aceptacion = aceptacion;
    }

    @Override
    public String toString() {

        return nombre;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        EstadoAFD otro =
                (EstadoAFD) obj;

        if (nombre == null) {
            return otro.nombre == null;
        }

        return nombre.equals(otro.nombre);
    }

    @Override
    public int hashCode() {

        return nombre != null
                ? nombre.hashCode()
                : 0;
    }
}
