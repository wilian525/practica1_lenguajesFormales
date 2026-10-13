/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Backend.reporte.estiloReporte;

/**
 *
 * @author wilian
 */
public class ReporteTokensEstilo {

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

                   --directiva: #58a6ff;
                   --estructura: #f0a84b;
                   --comando: #bc8cff;
                   --conector: #3fb950;
                   --literal: #ff7b72;
                   --identificador: #79c0ff;
                   --operador: #d2a8ff;
                   --delimitador: #a5d6ff;
                   --sistema: #56d4dd;
                   --eof: #8b949e;

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
                           rgba(88, 166, 255, 0.09),
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
                   max-width: 1400px;
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
                   background: var(--acento);
               }

               .tarjeta-tipos::before {
                   background: var(--comando);
               }

               .tarjeta-lineas::before {
                   background: var(--conector);
               }

               .tarjeta-eof::before {
                   background: var(--eof);
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

               .herramientas {
                   display: flex;
                   flex-wrap: wrap;
                   gap: 12px;
                   align-items: center;
                   justify-content: space-between;
                   margin-bottom: 18px;
                   padding: 14px;
                   background: #111827;
                   border: 1px solid var(--borde);
                   border-radius: 10px;
               }

               .grupo-busqueda {
                   display: flex;
                   flex: 1;
                   min-width: 280px;
                   gap: 10px;
               }

               .buscador {
                   width: 100%;
                   min-width: 240px;
                   padding: 11px 13px;
                   border: 1px solid var(--borde);
                   border-radius: 8px;
                   outline: none;
                   background: var(--fondo-principal);
                   color: var(--texto-principal);
                   font-size: 14px;
                   transition:
                       border-color 0.2s ease,
                       box-shadow 0.2s ease;
               }

               .buscador::placeholder {
                   color: var(--texto-suave);
               }

               .buscador:focus {
                   border-color: var(--acento);
                   box-shadow:
                       0 0 0 3px rgba(88, 166, 255, 0.13);
               }

               .filtro {
                   min-width: 220px;
                   padding: 10px 12px;
                   border: 1px solid var(--borde);
                   border-radius: 8px;
                   outline: none;
                   background: var(--fondo-principal);
                   color: var(--texto-principal);
                   cursor: pointer;
               }

               .contador {
                   padding: 8px 12px;
                   border-radius: 7px;
                   background: rgba(88, 166, 255, 0.10);
                   border: 1px solid rgba(88, 166, 255, 0.25);
                   color: var(--acento);
                   font-size: 13px;
                   white-space: nowrap;
               }

               .contenedor-tabla {
                   width: 100%;
                   overflow-x: auto;
                   border: 1px solid var(--borde);
                   border-radius: 10px;
               }

               table {
                   width: 100%;
                   min-width: 900px;
                   border-collapse: collapse;
                   background: var(--fondo-secundario);
               }

               thead {
                   background: #111827;
               }

               thead th {
                   position: sticky;
                   top: 0;
                   z-index: 2;
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

               .fila-token {
                   transition:
                       background-color 0.20s ease,
                       transform 0.20s ease,
                       box-shadow 0.20s ease;
                   animation: aparecerFila 0.35s ease both;
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

               .fila-token:hover {
                   position: relative;
                   z-index: 1;
                   background: #202b3c;
                   transform: translateY(-2px) scale(1.002);
                   box-shadow:
                       inset 4px 0 0 var(--acento),
                       0 6px 18px rgba(0, 0, 0, 0.20);
                   cursor: pointer;
               }

               .fila-token.seleccionada {
                   background: rgba(88, 166, 255, 0.10);
                   box-shadow:
                       inset 4px 0 0 var(--acento);
               }

               .fila-token.coincidencia {
                   animation: resaltarCoincidencia 0.45s ease;
                   background: rgba(88, 166, 255, 0.10);
               }

               @keyframes resaltarCoincidencia {
                   0% {
                       background: rgba(88, 166, 255, 0.30);
                   }

                   100% {
                       background: rgba(88, 166, 255, 0.10);
                   }
               }

               .numero {
                   width: 70px;
                   color: var(--texto-secundario);
                   font-variant-numeric: tabular-nums;
               }

               .lexema {
                   font-family:
                       "JetBrains Mono",
                       "Consolas",
                       monospace;
                   font-weight: 600;
                   color: #f0f6fc;
               }

               .posicion {
                   font-family:
                       "JetBrains Mono",
                       "Consolas",
                       monospace;
                   color: var(--acento);
                   font-weight: 600;
               }

               .badge {
                   display: inline-flex;
                   align-items: center;
                   gap: 7px;
                   padding: 5px 9px;
                   border-radius: 999px;
                   font-size: 11px;
                   font-weight: 700;
                   letter-spacing: 0.35px;
                   border: 1px solid transparent;
               }

               .badge::before {
                   content: "";
                   width: 7px;
                   height: 7px;
                   border-radius: 50%;
                   background: currentColor;
                   box-shadow: 0 0 8px currentColor;
               }

               .tipo-directiva {
                   color: var(--directiva);
                   background: rgba(88, 166, 255, 0.10);
                   border-color: rgba(88, 166, 255, 0.25);
               }

               .tipo-estructura {
                   color: var(--estructura);
                   background: rgba(240, 168, 75, 0.10);
                   border-color: rgba(240, 168, 75, 0.25);
               }

               .tipo-comando {
                   color: var(--comando);
                   background: rgba(188, 140, 255, 0.10);
                   border-color: rgba(188, 140, 255, 0.25);
               }

               .tipo-conector {
                   color: var(--conector);
                   background: rgba(63, 185, 80, 0.10);
                   border-color: rgba(63, 185, 80, 0.25);
               }

               .tipo-literal {
                   color: var(--literal);
                   background: rgba(255, 123, 114, 0.10);
                   border-color: rgba(255, 123, 114, 0.25);
               }

               .tipo-identificador {
                   color: var(--identificador);
                   background: rgba(121, 192, 255, 0.10);
                   border-color: rgba(121, 192, 255, 0.25);
               }

               .tipo-operador {
                   color: var(--operador);
                   background: rgba(210, 168, 255, 0.10);
                   border-color: rgba(210, 168, 255, 0.25);
               }

               .tipo-delimitador {
                   color: var(--delimitador);
                   background: rgba(165, 214, 255, 0.10);
                   border-color: rgba(165, 214, 255, 0.25);
               }

               .tipo-sistema {
                   color: var(--sistema);
                   background: rgba(86, 212, 221, 0.10);
                   border-color: rgba(86, 212, 221, 0.25);
               }

               .tipo-eof {
                   color: var(--eof);
                   background: rgba(139, 148, 158, 0.10);
                   border-color: rgba(139, 148, 158, 0.25);
               }

               .sin-resultados {
                   display: none;
                   padding: 34px;
                   text-align: center;
                   color: var(--texto-secundario);
                   background: var(--fondo-secundario);
                   border: 1px dashed var(--borde);
                   border-radius: 10px;
                   margin-top: 14px;
               }

               .sin-resultados strong {
                   display: block;
                   margin-bottom: 5px;
                   color: var(--texto-principal);
                   font-size: 16px;
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

                   .herramientas {
                       flex-direction: column;
                       align-items: stretch;
                   }

                   .grupo-busqueda {
                       flex-direction: column;
                   }

                   .filtro {
                       width: 100%;
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

                       const buscador =
                           document.getElementById("buscadorTokens");

                       const filtro =
                           document.getElementById("filtroTipo");

                       const contador =
                           document.getElementById("contadorVisibles");

                       const sinResultados =
                           document.getElementById("sinResultados");

                       const filas =
                           document.querySelectorAll(".fila-token");

                       if (!buscador || !filtro) {
                           return;
                       }

                       function actualizarTabla() {

                           const texto =
                               buscador.value
                                   .toLowerCase()
                                   .trim();

                           const tipoSeleccionado =
                               filtro.value;

                           let visibles = 0;

                           for (let i = 0; i < filas.length; i++) {

                               const fila = filas[i];

                               const contenido =
                                   fila.textContent
                                       .toLowerCase();

                               const tipo =
                                   fila.getAttribute("data-tipo");

                               const numero =
                                   fila.getAttribute("data-numero");

                               const filaToken =
                                   fila.getAttribute("data-fila");

                               const columnaToken =
                                   fila.getAttribute("data-columna");

                               const coincideTexto =
                                   texto === ""
                                   || contenido.includes(texto)
                                   || numero === texto
                                   || filaToken === texto
                                   || columnaToken === texto;

                               const coincideTipo =
                                   tipoSeleccionado === "TODOS"
                                   || tipo === tipoSeleccionado;

                               if (coincideTexto
                                       && coincideTipo) {

                                   fila.style.display = "";
                                   visibles++;

                                   if (texto !== "") {

                                       fila.classList.add(
                                           "coincidencia"
                                       );

                                   } else {

                                       fila.classList.remove(
                                           "coincidencia"
                                       );
                                   }

                               } else {

                                   fila.style.display = "none";

                                   fila.classList.remove(
                                       "coincidencia"
                                   );
                               }
                           }

                           if (contador) {

                               contador.textContent =
                                   visibles
                                   + " token"
                                   + (visibles === 1 ? "" : "s")
                                   + " visible"
                                   + (visibles === 1 ? "" : "s");
                           }

                           if (sinResultados) {

                               if (visibles === 0) {
                                   sinResultados.style.display = "block";
                               } else {
                                   sinResultados.style.display = "none";
                               }
                           }
                       }

                       for (let i = 0;
                               i < filas.length;
                               i++) {

                           filas[i].style.animationDelay =
                               (i * 35) + "ms";

                           filas[i].addEventListener(
                               "click",
                               function () {

                                   for (let j = 0;
                                           j < filas.length;
                                           j++) {

                                       filas[j].classList.remove(
                                           "seleccionada"
                                       );
                                   }

                                   this.classList.add(
                                       "seleccionada"
                                   );
                               }
                           );
                       }

                       buscador.addEventListener(
                           "input",
                           actualizarTabla
                       );

                       filtro.addEventListener(
                           "change",
                           actualizarTabla
                       );

                       actualizarTabla();
                   }
               );
               """;
    }
}
