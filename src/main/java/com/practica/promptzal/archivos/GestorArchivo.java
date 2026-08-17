/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.archivos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author wilian
 */
public class GestorArchivo {
    
    public boolean esArchivoPz(String ruta){
        if (ruta == null) {
            return false;
        }
        return ruta.endsWith(".pz");
    }
    
    public boolean existeArchivo(String ruta){
        Path archivo = Path.of(ruta);
        return Files.exists(archivo) && Files.isRegularFile(archivo);
    }
    
    public String leerArchivo(String ruta) throws IOException{
        Path archivo = Path.of(ruta);
        return Files.readString(archivo);
        }
    
    public Path creerCarpetaReportes(String rutaArchivo) throws IOException{
        Path archivo = Path.of(rutaArchivo).toAbsolutePath().normalize();
        Path carpetaArchivo = archivo.getParent();
        Path carpetaReportes = carpetaArchivo.resolve("reportes");
        Files.createDirectories(carpetaReportes);
        return carpetaReportes;
    }
    
    public String obtenerNombreArchivo(String rutaArchivo){
        Path archivo = Path.of(rutaArchivo);
        String nombreCompleto = archivo.getFileName().toString();
        
        if (nombreCompleto.endsWith(".pz")) {
            return nombreCompleto.substring(0,nombreCompleto.length() - 3);
        }
        return nombreCompleto;
    }
    
    public Path obtenerRutaReporteTokens(String rutaArchivo,Path carpetaReportes){
        String nombre = obtenerNombreArchivo(rutaArchivo);
        return carpetaReportes.resolve(nombre + "_tokens.html");
    }
    
    public Path obtenerRutaReporteErrores(String rutaArchivo, Path carpetaReporte){
        String nombre = obtenerNombreArchivo(rutaArchivo);
        return carpetaReporte.resolve(nombre + "_errores.html");
        
    }
}
