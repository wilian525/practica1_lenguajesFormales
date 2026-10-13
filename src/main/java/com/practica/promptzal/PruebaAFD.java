package com.practica.promptzal;

import com.practica.promptzal.Backend.afd.GeneradorAFD;

public class PruebaAFD {

    public static void main(String[] args) {

        GeneradorAFD generador = new GeneradorAFD();

        boolean generado = generador.generar();

        if (generado) {
            System.out.println("AFD generado correctamente.");
            System.out.println(
                    "DOT: " + generador.getRutaDot()
            );
            System.out.println(
                    "Imagen: " + generador.getRutaImagen()
            );
        } else {
            System.out.println(
                    "No se pudo generar completamente el AFD."
            );
        }
    }
}
