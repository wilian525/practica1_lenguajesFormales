/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.practica.promptzal.Frontend;

import com.practica.promptzal.Backend.lexer.ErrorLexico;
import com.practica.promptzal.Backend.parser.ErrorSintactico;
import com.practica.promptzal.Backend.semantico.ErrorSemantico;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author wilian
 */
public class TablaErroes extends javax.swing.JInternalFrame {

    private DefaultTableModel modeloTabla;

    public TablaErroes() {
        initComponents();
        configurarVentana();
        configurarTabla();
    }

    private void configurarVentana() {
        setTitle("Problemas");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
    }

    private void configurarTabla() {
        modeloTabla = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Tipo",
                    "Descripcion",
                    "Fila",
                    "Columna",
                    "Encontrado",
                    "Esperado"
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

    public void cargarErrores(
            ArrayList<ErrorLexico> erroresLexicos,
            ArrayList<ErrorSintactico> erroresSintacticos,
            ArrayList<ErrorSemantico> erroresSemanticos) {

        limpiarTabla();

        cargarErroresLexicos(erroresLexicos);
        cargarErroresSintacticos(erroresSintacticos);
        cargarErroresSemanticos(erroresSemanticos);
    }

    private void cargarErroresLexicos(
            ArrayList<ErrorLexico> errores) {

        if (errores == null) {
            return;
        }

        for (ErrorLexico error : errores) {

            modeloTabla.addRow(
                    new Object[]{
                        "LEXICO",
                        error.getDescripcion(),
                        error.getFila(),
                        error.getColumna(),
                        error.getLexema(),
                        "-"
                    }
            );
        }
    }

    private void cargarErroresSintacticos(
            ArrayList<ErrorSintactico> errores) {

        if (errores == null) {
            return;
        }

        for (ErrorSintactico error : errores) {

            modeloTabla.addRow(
                    new Object[]{
                        "SINTACTICO",
                        error.getDescripcion(),
                        error.getFila(),
                        error.getColumna(),
                        error.getTokenEncontrado(),
                        error.getTokenEsperado()
                    }
            );
        }
    }

    private void cargarErroresSemanticos(
            ArrayList<ErrorSemantico> errores) {

        if (errores == null) {
            return;
        }

        for (ErrorSemantico error : errores) {

            modeloTabla.addRow(
                    new Object[]{
                        "SEMANTICO",
                        error.getDescripcion(),
                        error.getLinea(),
                        "-",
                        "-",
                        "-"
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
