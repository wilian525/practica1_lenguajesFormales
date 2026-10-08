/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.lexer;

/**
 *
 * @author wilian
 */
public enum TipoToken {

    DIRECTIVA("Directiva"),
    AGENTE("Agente"),
    CONTEXTO("Contexto"),
    VARIABLE("Variable"),
    EJECUTAR("Ejecutar"),
    EXPORTAR("Exportar"),
    COMANDO_IA("Comando de IA"),
    CARGAR("Funcion del sistema"),
    CONECTOR("Conector"),
    FLECHA("Flecha"),
    IGUAL("Asignacion"),
    MAS("Concatenacion"),
    LLAVE_A("Llave de apertura"),
    LLAVE_C("Llave de cierre"),
    PAR_A("Parentesis de apertura"),
    PAR_C("Parentesis de cierre"),
    COMA("Coma"),
    ID("Identificador"),
    CADENA("Literal de cadena"),
    NUMERO("Literal numerico"),
    EOF("Fin de archivo");

    private final String descripcion;

    private TipoToken(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
