/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.practica.promptzal.Frontend;

import com.practica.promptzal.Backend.lexer.ErrorLexico;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wilian
 */
public class TablaErroes extends javax.swing.JInternalFrame {

      private DefaultTableModel modeloTabla;

    public TablaErroes() {
        initComponents();
        configurarTabla();
    }

    private void configurarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Lexema",
                    "Tipo",
                    "Descripción",
                    "Fila",
                    "Columna"
                }
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        jTable1.setModel(modeloTabla);
    }

    public void cargarErrores(ErrorLexico[] errores, int cantidad) {

        modeloTabla.setRowCount(0);
        if (errores == null || cantidad <= 0) {
            return;
        }

        int limite = Math.min(cantidad, errores.length);
        for (int i = 0; i < limite; i++) {
            ErrorLexico error = errores[i];

            if (error != null) {

                modeloTabla.addRow(new Object[]{
                    error.getLexema(),
                    error.getTipo(),
                    error.getDescripcion(),
                    error.getFila(),
                    error.getColumna()
                });
            }
        }
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
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Lexema", "Tipo", "Descripcion", "Fila", "Columna"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
