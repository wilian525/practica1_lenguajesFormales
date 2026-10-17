/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package com.practica.promptzal.Frontend;

/**
 *
 * @author wilian
 */
public class TablaSalida extends javax.swing.JInternalFrame {

    public TablaSalida() {
        initComponents();
        configurarVentana();
        configurarSalida();
    }

    private void configurarVentana() {
        setTitle("Salida");
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
    }

    private void configurarSalida() {
        txtSalida.setEditable(false);
        txtSalida.setLineWrap(true);
        txtSalida.setWrapStyleWord(true);
    }

    public void agregarMensaje(String mensaje) {
        if (mensaje == null) {
            return;
        }

        if (!txtSalida.getText().isEmpty()) {
            txtSalida.append("\n");
        }

        txtSalida.append(mensaje);

        txtSalida.setCaretPosition(
                txtSalida.getDocument().getLength()
        );
    }

    public void establecerTexto(String texto) {
        if (texto == null) {
            txtSalida.setText("");
            return;
        }

        txtSalida.setText(texto);
        txtSalida.setCaretPosition(0);
    }

    public void limpiarSalida() {
        txtSalida.setText("");
    }

    public javax.swing.JTextArea getTxtSalida() {
        return txtSalida;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollSalida = new javax.swing.JScrollPane();
        txtSalida = new javax.swing.JTextArea();

        txtSalida.setColumns(20);
        txtSalida.setRows(5);
        scrollSalida.setViewportView(txtSalida);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(scrollSalida, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollSalida, javax.swing.GroupLayout.DEFAULT_SIZE, 353, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane scrollSalida;
    private javax.swing.JTextArea txtSalida;
    // End of variables declaration//GEN-END:variables
}
