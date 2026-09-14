/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.archivos;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author wilian
 */
public class GestorArchivo {
    
    public boolean esArchivoPz(Path ruta){
        if (ruta == null  || ruta.getFileName() == null) {
            return false;
        }
        String nombre = ruta.getFileName().toString().toLowerCase();
        return ruta.endsWith(".pz");
    }
    
    public boolean existeArchivo(Path ruta){
        return ruta != null && Files.exists(ruta) && Files.isRegularFile(ruta);
    }
    
    public String leerArchivo(Path ruta) throws IOException{
          if (!existeArchivo(ruta)) {
             throw new IOException("El archivo no existe.");
        }
         return Files.readString(ruta,StandardCharsets.UTF_8);
        }
    
    public Path guardarArchivo(Path ruta,String contenido) throws IOException{
         if (ruta == null) {
             throw new IllegalArgumentException( "La ruta no puede ser nula." );
        }
         if (contenido == null) {
               contenido = "";
        }
         Path rutaFinal = asegurarExtensionPz(ruta);
         Path directorioPadre = rutaFinal.toAbsolutePath().normalize().getParent();
         if (directorioPadre != null) {
              Files.createDirectories(directorioPadre);
        }
         Files.writeString(rutaFinal, contenido, StandardCharsets.UTF_8);
         return rutaFinal;
    }
    
    public Path asegurarExtensionPz(Path ruta){
        if (ruta ==  null) {
               throw new IllegalArgumentException(
                    "La ruta no puede ser nula."
            );
        }
        String nombre = ruta.getFileName().toString();
        if (nombre.toLowerCase().endsWith(".pz")) {
             return ruta;
        }
        return ruta.resolveSibling(nombre + ".pz");
    }
    
    public Path asegurarExtencionHTML(Path ruta){
            if (ruta == null) {
                throw new IllegalArgumentException("La ruta no puede ser nula.");
        }
            String nombre = ruta.getFileName().toString();
            if (nombre.toLowerCase().endsWith(".html")) {
              return ruta;
        }
            return ruta.resolveSibling(nombre + ".html");
    }
  
    public Path creerCarpetaReportes(String rutaArchivo) throws IOException{
        Path archivo = Path.of(rutaArchivo).toAbsolutePath().normalize();
        Path carpetaArchivo = archivo.getParent();
        Path carpetaReportes = carpetaArchivo.resolve("reportes");
        Files.createDirectories(carpetaReportes);
        return carpetaReportes;
    }
    
    public String obtenerNombreArchivo(Path ruta){
         if (ruta == null || ruta.getFileName() == null) {
              return "";
        }
        String nombre = ruta.getFileName().toString();
        
        if (nombre.toLowerCase().endsWith(".pz")) {
            return nombre.substring(0,nombre.length() - 3);
        }
        return nombre;
    }
}
