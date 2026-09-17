/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

/**
 *
 * @author wilian
 */
public class TransicionAFD {

    private EstadoAFD estadoOrigen;
    private String simbolo;
    private EstadoAFD estadoDestino;

    public TransicionAFD(EstadoAFD estadoOrigen, String simbolo, EstadoAFD estadoDestino) {
        this.estadoOrigen = estadoOrigen;
        this.simbolo = simbolo;
        this.estadoDestino = estadoDestino;
    }

    public EstadoAFD getEstadoOrigen() {
        return estadoOrigen;
    }

    public void setEstadoOrigen(EstadoAFD estadoOrigen) {
        this.estadoOrigen = estadoOrigen;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public EstadoAFD getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(EstadoAFD estadoDestino) {
        this.estadoDestino = estadoDestino;
    }

    @Override
    public String toString() {
        return estadoOrigen.getNombre()
                + " -- "
                + simbolo
                + " --> "
                + estadoDestino.getNombre();
    }
}
