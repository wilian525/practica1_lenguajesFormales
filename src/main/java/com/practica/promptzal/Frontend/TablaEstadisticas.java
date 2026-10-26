/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.practica.promptzal.Frontend;

import com.practica.promptzal.Backend.lexer.ErrorLexico;
import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import com.practica.promptzal.Backend.parser.ErrorSintactico;
import com.practica.promptzal.Backend.semantico.ErrorSemantico;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wilian
 */
public class TablaEstadisticas extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;

    public TablaEstadisticas() {
        initComponents();
        configurarVentana();
        configurarTabla();
    }

    private void configurarVentana() {
        setTitle("Estadisticas");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Estadistica",
                    "Valor"
                }
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        jTable1.setModel(modeloTabla);
        jTable1.setFillsViewportHeight(true);
        jTable1.setAutoCreateRowSorter(true);

        jTable1.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(350);

        jTable1.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(180);
    }

    public void cargarEstadisticas(
            ArrayList<Token> tokens,
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos,
            String codigoFuente) {

        limpiarTabla();

        if (tokens == null
                || erroresLexicos == null
                || erroresSintacticos == null
                || erroresSemanticos == null
                || codigoFuente == null) {

            return;
        }

        int totalTokens = tokens.size();

        int tiposUtilizados = contarTiposUtilizados(tokens);

        int totalLineas= contarLineas(codigoFuente);

        int totalLexicos = erroresLexicos.size();

        int totalSintacticos= erroresSintacticos.size();

        int totalSemanticos = erroresSemanticos.size();

        int totalErrores = totalLexicos+ totalSintacticos+ totalSemanticos;

        agregarFila("Total de tokens",totalTokens);

        agregarFila("Tipos de token utilizados",tiposUtilizados);

        agregarFila("Lineas analizadas", totalLineas);

        agregarFila("Errores lexicos", totalLexicos);

        agregarFila("Errores sintacticos", totalSintacticos );

        agregarFila( "Errores semanticos",totalSemanticos  );

        agregarFila( "Total de errores", totalErrores );

        if (totalErrores == 0) {
            agregarFila( "Estado de la compilacion","Sin errores");
        } else {
            agregarFila( "Estado de la compilacion","Con errores");
        }

        agregarFila("Frecuencia por tipo de token", "");

        cargarFrecuencias( tokens,totalTokens );
    }

    private void cargarFrecuencias( ArrayList<Token> tokens, int totalTokens) {

        TipoToken[] tipos = TipoToken.values();

        for (int i = 0; i < tipos.length; i++) {

            TipoToken tipo = tipos[i];

            int cantidad = contarTipo( tokens, tipo);

            double porcentaje = 0.0;

            if (totalTokens > 0) {
                porcentaje= cantidad * 100.0 / totalTokens;
            }

            String valor= cantidad  + " ("+ String.format(  Locale.US, "%.2f", porcentaje ) + " %)";

            agregarFila( tipo.name(),valor);
        }
    }

    private int contarTipo( ArrayList<Token> tokens,TipoToken tipoBuscado) {

        int cantidad = 0;

        for (Token token : tokens) {

            if (token.getTipo() == tipoBuscado) {

                cantidad++;
            }
        }

        return cantidad;
    }

    private int contarTiposUtilizados(ArrayList<Token> tokens) {

        int cantidad = 0;

        TipoToken[] tipos = TipoToken.values();

        for (int i = 0;i < tipos.length; i++) {

            if (contarTipo( tokens,tipos[i]  ) > 0) {

                cantidad++;
            }
        }

        return cantidad;
    }

    private int contarLineas( String codigoFuente) {

        if (codigoFuente.isEmpty()) {
            return 0;
        }

        int cantidad = 1;

        for (int i = 0; i < codigoFuente.length(); i++) {

            if (codigoFuente.charAt(i) == '\n') {
                cantidad++;
            }
        }

        return cantidad;
    }

    private void agregarFila(String estadistica,  Object valor) {

        modeloTabla.addRow(
                new Object[]{
                    estadistica,
                    valor
                }
        );
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    public javax.swing.JTable getTabla() {
        return jTable1;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 709, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
