/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

/**
 *
 * @author wilian
 */
public class AutomataFinitoDeterministico {
    
    private EstadoAFD estadoInicial;

    private ArrayList<EstadoAFD> estados;

    private ArrayList<EstadoAFD> estadosAceptacion;

    private LinkedList<TransicionAFD> transiciones;

    private HashMap<String, EstadoAFD> indiceEstados;

    public AutomataFinitoDeterministico() {

        this.estadoInicial = null;

        this.estados = new ArrayList<>();

        this.estadosAceptacion = new ArrayList<>();

        this.transiciones = new LinkedList<>();

        this.indiceEstados = new HashMap<>();
    }

    /**
     * Agrega un estado al automata.
     */
    public void agregarEstado(EstadoAFD estado) {

        if (estado == null) {
            return;
        }

        if (!indiceEstados.containsKey(estado.getNombre())) {

            estados.add(estado);

            indiceEstados.put(
                    estado.getNombre(),
                    estado
            );
        }
    }

    /**
     * Define el estado inicial.
     */
    public void establecerEstadoInicial(EstadoAFD estado) {

        if (estado == null) {
            return;
        }

        agregarEstado(estado);

        estadoInicial = estado;
    }

    /**
     * Agrega un estado de aceptacion.
     */
    public void agregarEstadoAceptacion(EstadoAFD estado) {

        if (estado == null) {
            return;
        }

        agregarEstado(estado);

        if (!estadosAceptacion.contains(estado)) {

            estadosAceptacion.add(estado);

            estado.setAceptacion(true);
        }
    }

    /**
     * Agrega una transicion al automata.
     */
    public void agregarTransicion(
            EstadoAFD origen,
            String simbolo,
            EstadoAFD destino) {

        if (origen == null
                || simbolo == null
                || destino == null) {

            return;
        }

        agregarEstado(origen);
        agregarEstado(destino);

        TransicionAFD transicion =
                new TransicionAFD(
                        origen,
                        simbolo,
                        destino
                );

        transiciones.add(transicion);
    }

    /**
     * Busca una transicion desde un estado
     * utilizando un simbolo determinado.
     */
    public Optional<EstadoAFD> obtenerSiguienteEstado(
            EstadoAFD estado,
            String simbolo) {

        if (estado == null || simbolo == null) {
            return Optional.empty();
        }

        for (TransicionAFD transicion : transiciones) {

            if (transicion.getEstadoOrigen() == estado
                    && transicion.getSimbolo().equals(simbolo)) {

                return Optional.of(
                        transicion.getEstadoDestino()
                );
            }
        }

        return Optional.empty();
    }

    /**
     * Busca un estado por su nombre.
     */
    public Optional<EstadoAFD> obtenerEstado(String nombre) {

        if (nombre == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(
                indiceEstados.get(nombre)
        );
    }

    /**
     * Comprueba si un estado es de aceptacion.
     */
    public boolean esEstadoAceptacion(EstadoAFD estado) {

        if (estado == null) {
            return false;
        }

        return estadosAceptacion.contains(estado);
    }

    /**
     * Obtiene el estado inicial.
     */
    public EstadoAFD getEstadoInicial() {
        return estadoInicial;
    }

    /**
     * Obtiene todos los estados.
     */
    public ArrayList<EstadoAFD> getEstados() {
        return estados;
    }

    /**
     * Obtiene los estados de aceptacion.
     */
    public ArrayList<EstadoAFD> getEstadosAceptacion() {
        return estadosAceptacion;
    }

    /**
     * Obtiene todas las transiciones.
     */
    public LinkedList<TransicionAFD> getTransiciones() {
        return transiciones;
    }

    /**
     * Limpia completamente el automata.
     */
    public void limpiar() {

        estadoInicial = null;

        estados.clear();

        estadosAceptacion.clear();

        transiciones.clear();

        indiceEstados.clear();
    }

    /**
     * Permite agregar una transicion usando los nombres
     * de los estados.
     */
    public boolean agregarTransicion(
            String estadoOrigen,
            String simbolo,
            String estadoDestino) {

        Optional<EstadoAFD> origen =
                obtenerEstado(estadoOrigen);

        Optional<EstadoAFD> destino =
                obtenerEstado(estadoDestino);

        if (origen.isEmpty() || destino.isEmpty()) {
            return false;
        }

        agregarTransicion(
                origen.get(),
                simbolo,
                destino.get()
        );

        return true;
    }

    /**
     * Valida una cadena recorriendo el automata
     * desde el estado inicial.
     */
    public boolean validar(String entrada) {

        if (estadoInicial == null
                || entrada == null) {

            return false;
        }

        EstadoAFD actual =
                estadoInicial;

        for (int i = 0;
                i < entrada.length();
                i++) {

            String simbolo =
                    String.valueOf(
                            entrada.charAt(i)
                    );

            Optional<EstadoAFD> siguiente =
                    obtenerSiguienteEstado(
                            actual,
                            simbolo
                    );

            if (siguiente.isEmpty()) {
                return false;
            }

            actual = siguiente.get();
        }

        return esEstadoAceptacion(actual);
    }
    
}
