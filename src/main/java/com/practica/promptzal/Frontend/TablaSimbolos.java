/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.practica.promptzal.Frontend;

import com.practica.promptzal.Backend.semantico.Simbolo;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wilian
 */
public class TablaSimbolos extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;

    public TablaSimbolos() {
        initComponents();
        configurarVentana();
        configurarTabla();
    }

    private void configurarVentana() {
        setTitle("Tabla de Simbolos");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Nombre",
                    "Tipo",
                    "Ambito",
                    "Linea"
                }
        ) {
            @Override
            public boolean isCellEditable(
                    int fila,
                    int columna) {

                return false;
            }
        };

        jTable1.setModel(modeloTabla);
        jTable1.setFillsViewportHeight(true);
        jTable1.setAutoCreateRowSorter(true);
    }

    public void cargarSimbolos(
            ArrayList<Simbolo> simbolos) {

        limpiarTabla();

        if (simbolos == null) {
            return;
        }

        for (Simbolo simbolo : simbolos) {

            modeloTabla.addRow(
                    new Object[]{
                        simbolo.getNombre(),
                        simbolo.getTipo(),
                        simbolo.getAmbito(),
                        simbolo.getLinea()
                    }
            );
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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombre", "Tipo", "Ambito", "Linea"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
