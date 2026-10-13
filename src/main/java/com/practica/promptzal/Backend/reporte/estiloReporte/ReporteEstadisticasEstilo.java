/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte.estiloReporte;

/**
 *
 * @author wilian
 */
public class ReporteEstadisticasEstilo {

    public String getCss() {
        return """
               :root {
                   --fondo-principal: #0d1117;
                   --fondo-secundario: #161b22;
                   --panel: #161b22;
                   --panel-suave: #1c2333;
                   --borde: #30363d;
                   --borde-suave: #263041;

                   --texto-principal: #e6edf3;
                   --texto-secundario: #8b949e;
                   --texto-suave: #6e7681;

                   --acento: #58a6ff;
                   --tokens: #58a6ff;
                   --tipos: #bc8cff;
                   --lineas: #3fb950;
                   --errores: #ff6b6b;

                   --barra: #58a6ff;
                   --sombra: rgba(0, 0, 0, 0.35);
               }

               * {
                   box-sizing: border-box;
               }

               html {
                   scroll-behavior: smooth;
               }

               body {
                   margin: 0;
                   min-height: 100vh;
                   padding: 32px;
                   font-family: "Segoe UI", Arial, Helvetica, sans-serif;
                   background:
                       radial-gradient(
                           circle at top right,
                           rgba(88, 166, 255, 0.08),
                           transparent 28%
                       ),
                       radial-gradient(
                           circle at bottom left,
                           rgba(188, 140, 255, 0.05),
                           transparent 30%
                       ),
                       var(--fondo-principal);
                   color: var(--texto-principal);
               }

               .contenedor {
                   width: 100%;
                   max-width: 1350px;
                   margin: 0 auto;
                   background: var(--panel);
                   border: 1px solid var(--borde);
                   border-radius: 14px;
                   overflow: hidden;
                   box-shadow: 0 18px 50px var(--sombra);
                   animation: aparecerReporte 0.45s ease;
               }

               @keyframes aparecerReporte {
                   from {
                       opacity: 0;
                       transform: translateY(12px);
                   }

                   to {
                       opacity: 1;
                       transform: translateY(0);
                   }
               }

               header {
                   display: flex;
                   justify-content: space-between;
                   align-items: center;
                   gap: 20px;
                   padding: 26px 30px;
                   background:
                       linear-gradient(
                           135deg,
                           #111827,
                           #0d1117
                       );
                   border-bottom: 1px solid var(--borde);
               }

               .titulo-reporte {
                   display: flex;
                   flex-direction: column;
                   gap: 6px;
               }

               .titulo-reporte h1 {
                   margin: 0;
                   font-size: 28px;
                   font-weight: 700;
                   letter-spacing: 0.4px;
               }

               .titulo-reporte p {
                   margin: 0;
                   color: var(--texto-secundario);
                   font-size: 14px;
               }

               .marca {
                   display: inline-flex;
                   align-items: center;
                   padding: 8px 12px;
                   border: 1px solid var(--borde);
                   border-radius: 8px;
                   background: rgba(88, 166, 255, 0.08);
                   color: var(--acento);
                   font-size: 13px;
                   font-weight: 600;
               }

               .contenido {
                   padding: 28px 30px 32px;
               }

               .resumen {
                   display: grid;
                   grid-template-columns:
                       repeat(4, minmax(150px, 1fr));
                   gap: 16px;
                   margin-bottom: 26px;
               }

               .tarjeta {
                   position: relative;
                   overflow: hidden;
                   padding: 18px;
                   background: var(--panel-suave);
                   border: 1px solid var(--borde);
                   border-radius: 11px;
                   transition:
                       transform 0.22s ease,
                       border-color 0.22s ease,
                       box-shadow 0.22s ease;
               }

               .tarjeta:hover {
                   transform: translateY(-3px);
                   border-color: #46566e;
                   box-shadow:
                       0 8px 20px rgba(0, 0, 0, 0.25);
               }

               .tarjeta::before {
                   content: "";
                   position: absolute;
                   top: 0;
                   left: 0;
                   width: 100%;
                   height: 3px;
                   background: var(--acento);
               }

               .tarjeta-tokens::before {
                   background: var(--tokens);
               }

               .tarjeta-tipos::before {
                   background: var(--tipos);
               }

               .tarjeta-lineas::before {
                   background: var(--lineas);
               }

               .tarjeta-errores::before {
                   background: var(--errores);
               }

               .tarjeta span {
                   display: block;
                   color: var(--texto-secundario);
                   font-size: 13px;
                   margin-bottom: 7px;
               }

               .tarjeta strong {
                   display: block;
                   color: var(--texto-principal);
                   font-size: 25px;
                   font-weight: 700;
               }

               .seccion {
                   margin-top: 22px;
                   padding: 20px;
                   background: #111827;
                   border: 1px solid var(--borde);
                   border-radius: 11px;
               }

               .seccion:first-of-type {
                   margin-top: 0;
               }

               .titulo-seccion {
                   display: flex;
                   justify-content: space-between;
                   align-items: center;
                   flex-wrap: wrap;
                   gap: 10px;
                   margin-bottom: 18px;
               }

               .titulo-seccion h2 {
                   margin: 0;
                   color: var(--texto-principal);
                   font-size: 18px;
               }

               .titulo-seccion span {
                   color: var(--texto-secundario);
                   font-size: 12px;
               }

               .lista-frecuencias {
                   display: flex;
                   flex-direction: column;
                   gap: 12px;
               }

               .frecuencia {
                   padding: 12px 14px;
                   background: var(--fondo-secundario);
                   border: 1px solid var(--borde-suave);
                   border-radius: 9px;
                   animation: aparecerFila 0.35s ease both;
                   transition:
                       transform 0.2s ease,
                       border-color 0.2s ease,
                       background-color 0.2s ease;
               }

               .frecuencia:hover {
                   transform: translateX(4px);
                   border-color: #46566e;
                   background: #1b2431;
               }

               @keyframes aparecerFila {
                   from {
                       opacity: 0;
                       transform: translateY(6px);
                   }

                   to {
                       opacity: 1;
                       transform: translateY(0);
                   }
               }

               .frecuencia-encabezado {
                   display: flex;
                   justify-content: space-between;
                   align-items: center;
                   gap: 15px;
                   margin-bottom: 9px;
               }

               .tipo-token {
                   font-family:
                       "JetBrains Mono",
                       "Consolas",
                       monospace;
                   color: var(--texto-principal);
                   font-size: 13px;
                   font-weight: 700;
               }

               .cantidad {
                   display: inline-flex;
                   align-items: center;
                   justify-content: center;
                   min-width: 42px;
                   padding: 4px 8px;
                   border-radius: 999px;
                   background: rgba(88, 166, 255, 0.10);
                   border: 1px solid rgba(88, 166, 255, 0.24);
                   color: var(--acento);
                   font-family:
                       "JetBrains Mono",
                       "Consolas",
                       monospace;
                   font-size: 12px;
                   font-weight: 700;
               }

               .barra-contenedor {
                   position: relative;
                   width: 100%;
                   height: 8px;
                   overflow: hidden;
                   background: #0d1117;
                   border-radius: 999px;
               }

               .barra {
                   width: 0;
                   height: 100%;
                   border-radius: 999px;
                   background:
                       linear-gradient(
                           90deg,
                           #1f6feb,
                           #58a6ff
                       );
                   box-shadow:
                       0 0 8px rgba(88, 166, 255, 0.30);
                   transition: width 0.8s ease;
               }

               .porcentaje {
                   margin-top: 6px;
                   color: var(--texto-secundario);
                   font-size: 11px;
                   text-align: right;
               }

               .tabla-contenedor {
                   width: 100%;
                   overflow-x: auto;
                   border: 1px solid var(--borde);
                   border-radius: 10px;
               }

               table {
                   width: 100%;
                   min-width: 650px;
                   border-collapse: collapse;
                   background: var(--fondo-secundario);
               }

               thead {
                   background: #0d1117;
               }

               thead th {
                   padding: 14px 16px;
                   text-align: left;
                   color: #c9d1d9;
                   font-size: 12px;
                   text-transform: uppercase;
                   letter-spacing: 0.7px;
                   border-bottom: 1px solid var(--borde);
               }

               tbody td {
                   padding: 14px 16px;
                   border-bottom: 1px solid var(--borde-suave);
                   color: #c9d1d9;
                   font-size: 14px;
               }

               tbody tr:last-child td {
                   border-bottom: none;
               }

               tbody tr {
                   transition:
                       background-color 0.2s ease,
                       transform 0.2s ease;
               }

               tbody tr:hover {
                   background: #202b3c;
                   transform: translateX(3px);
               }

               .dato {
                   font-family:
                       "JetBrains Mono",
                       "Consolas",
                       monospace;
                   color: var(--acento);
                   font-weight: 600;
               }

               .estado-correcto {
                   color: var(--lineas);
                   font-weight: 700;
               }

               .estado-error {
                   color: var(--errores);
                   font-weight: 700;
               }

               footer {
                   display: flex;
                   justify-content: space-between;
                   flex-wrap: wrap;
                   gap: 10px;
                   padding: 17px 30px;
                   border-top: 1px solid var(--borde);
                   background: #0d1117;
                   color: var(--texto-secundario);
                   font-size: 12px;
               }

               @media (max-width: 900px) {
                   body {
                       padding: 15px;
                   }

                   header {
                       flex-direction: column;
                       align-items: flex-start;
                   }

                   .contenido {
                       padding: 20px;
                   }

                   .resumen {
                       grid-template-columns:
                           repeat(2, 1fr);
                   }
               }

               @media (max-width: 520px) {
                   .resumen {
                       grid-template-columns: 1fr;
                   }

                   .titulo-reporte h1 {
                       font-size: 22px;
                   }
               }
               """;
    }

    public String getJavaScript() {
        return """
               document.addEventListener(
                   "DOMContentLoaded",
                   function () {

                       const barras =
                           document.querySelectorAll(".barra");

                       const frecuencias =
                           document.querySelectorAll(".frecuencia");

                       for (let i = 0;
                               i < frecuencias.length;
                               i++) {

                           frecuencias[i].style.animationDelay =
                               (i * 40) + "ms";
                       }

                       setTimeout(
                           function () {

                               for (let i = 0;
                                       i < barras.length;
                                       i++) {

                                   const porcentaje =
                                       barras[i].getAttribute(
                                           "data-porcentaje"
                                       );

                                   barras[i].style.width =
                                       porcentaje + "%";
                               }
                           },
                           150
                       );
                   }
               );
               """;
    }
}
