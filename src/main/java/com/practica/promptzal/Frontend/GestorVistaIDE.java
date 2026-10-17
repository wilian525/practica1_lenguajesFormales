/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class GestorVistaIDE {
    
     private final JPanel panelPrincipal, panelExplorador, panelEditor, panelPrompt, panelResultados, panelEstado, panelAccionesPrompt;
    private final JSplitPane splitVerticalPrincipal, splitExploradorTrabajo, splitEditorPrompt;
    private final JDesktopPane desktopResultados;
    private final JTextArea txtEditor, txtPromptGenerado;
    private final JTree arbolArchivos;
    private final JLabel lblArchivoActual, lblPosicionCursor, lblEstadoCompilacion;
    private final JButton btnCopiarPrompt, btnGuardarPrompt, btnProblemas, btnTokens, btnEstadisticas, btnSimbolos, btnSalida;

    private final TablaTokens tablaTokens;
    private final TablaErroes tablaErrores;
    private final TablaEstadisticas tablaEstadisticas;
    private final TablaSimbolos tablaSimbolos;
    private final TablaSalida tablaSalida;

    private boolean exploradorVisible, promptVisible, resultadosVisible;

    public GestorVistaIDE() {
        panelPrincipal = new JPanel(new BorderLayout());
        panelExplorador = new JPanel(new BorderLayout());
        panelEditor = new JPanel(new BorderLayout());
        panelPrompt = new JPanel(new BorderLayout());
        panelResultados = new JPanel(new BorderLayout());
        panelEstado = new JPanel(new BorderLayout());
        panelAccionesPrompt = new JPanel();

        splitVerticalPrincipal = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitExploradorTrabajo = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitEditorPrompt = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        desktopResultados = new JDesktopPane();
        txtEditor = new JTextArea();
        txtPromptGenerado = new JTextArea();
        arbolArchivos = new JTree();
        lblArchivoActual = new JLabel("Sin archivo.pz");
        lblPosicionCursor = new JLabel(" Ln 1, Col 1");
        lblEstadoCompilacion = new JLabel("Listo ");

        btnCopiarPrompt = new JButton("Copiar");
        btnGuardarPrompt = new JButton("Guardar");
        btnProblemas = new JButton("Problemas");
        btnTokens = new JButton("Tokens");
        btnEstadisticas = new JButton("Estadisticas");
        btnSimbolos = new JButton("Simbolos");
        btnSalida = new JButton("Salida");

        tablaTokens = new TablaTokens();
        tablaErrores = new TablaErroes();
        tablaEstadisticas = new TablaEstadisticas();
        tablaSimbolos = new TablaSimbolos();
        tablaSalida = new TablaSalida();

        crearVista();
        configurarVentanas();
        configurarEventos();
        mostrarEstadoInicial();
    }

    private void crearVista() {
        crearExplorador();
        crearEditor();
        crearPrompt();
        crearResultados();
        crearEstado();

        splitEditorPrompt.setLeftComponent(panelEditor);
        splitEditorPrompt.setRightComponent(null);
        splitEditorPrompt.setResizeWeight(1.0);
        splitEditorPrompt.setDividerSize(0);

        splitExploradorTrabajo.setLeftComponent(null);
        splitExploradorTrabajo.setRightComponent(splitEditorPrompt);
        splitExploradorTrabajo.setResizeWeight(0.0);
        splitExploradorTrabajo.setDividerSize(0);

        splitVerticalPrincipal.setTopComponent(splitExploradorTrabajo);
        splitVerticalPrincipal.setBottomComponent(null);
        splitVerticalPrincipal.setResizeWeight(1.0);
        splitVerticalPrincipal.setDividerSize(0);

        panelPrincipal.add(splitVerticalPrincipal, BorderLayout.CENTER);
        panelPrincipal.add(panelEstado, BorderLayout.SOUTH);
    }

    private void crearExplorador() {
        JLabel titulo = new JLabel(" EXPLORADOR");
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("PROMPTZAL");
        raiz.add(new DefaultMutableTreeNode("Sin archivo"));
        arbolArchivos.setModel(new DefaultTreeModel(raiz));
        panelExplorador.setPreferredSize(new Dimension(220, 400));
        panelExplorador.add(titulo, BorderLayout.NORTH);
        panelExplorador.add(new JScrollPane(arbolArchivos), BorderLayout.CENTER);
    }

    private void crearEditor() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.add(lblArchivoActual, BorderLayout.WEST);
        txtEditor.setTabSize(4);
        panelEditor.add(cabecera, BorderLayout.NORTH);
        panelEditor.add(new JScrollPane(txtEditor), BorderLayout.CENTER);
    }

    private void crearPrompt() {
        JLabel titulo = new JLabel(" PROMPT GENERADO");
        txtPromptGenerado.setEditable(false);
        txtPromptGenerado.setLineWrap(true);
        txtPromptGenerado.setWrapStyleWord(true);
        panelAccionesPrompt.add(btnCopiarPrompt);
        panelAccionesPrompt.add(btnGuardarPrompt);
        panelPrompt.setPreferredSize(new Dimension(350, 400));
        panelPrompt.add(titulo, BorderLayout.NORTH);
        panelPrompt.add(new JScrollPane(txtPromptGenerado), BorderLayout.CENTER);
        panelPrompt.add(panelAccionesPrompt, BorderLayout.SOUTH);
    }

    private void crearResultados() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);
        barra.add(btnProblemas);
        barra.add(btnTokens);
        barra.add(btnEstadisticas);
        barra.add(btnSimbolos);
        barra.add(btnSalida);
        panelResultados.setPreferredSize(new Dimension(800, 280));
        panelResultados.add(barra, BorderLayout.NORTH);
        panelResultados.add(desktopResultados, BorderLayout.CENTER);
    }

    private void crearEstado() {
        panelEstado.add(lblPosicionCursor, BorderLayout.WEST);
        panelEstado.add(lblEstadoCompilacion, BorderLayout.EAST);
    }

    private void configurarVentanas() {
        tablaTokens.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        tablaErrores.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        tablaEstadisticas.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        tablaSimbolos.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        tablaSalida.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);

        desktopResultados.add(tablaTokens);
        desktopResultados.add(tablaErrores);
        desktopResultados.add(tablaEstadisticas);
        desktopResultados.add(tablaSimbolos);
        desktopResultados.add(tablaSalida);
    }

    private void configurarEventos() {
        btnProblemas.addActionListener(e -> mostrarErrores());
        btnTokens.addActionListener(e -> mostrarTokens());
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        btnSimbolos.addActionListener(e -> mostrarSimbolos());
        btnSalida.addActionListener(e -> mostrarSalida());

        txtEditor.addCaretListener(e -> actualizarPosicionCursor());

        btnCopiarPrompt.addActionListener(e -> {
            txtPromptGenerado.selectAll();
            txtPromptGenerado.copy();
            txtPromptGenerado.setCaretPosition(0);
        });
    }

    private void mostrarEstadoInicial() {
        exploradorVisible = false;
        promptVisible = false;
        resultadosVisible = false;
    }

    public void alternarExplorador() { mostrarExplorador(!exploradorVisible); }

    public void mostrarExplorador(boolean mostrar) {
        exploradorVisible = mostrar;
        if (mostrar) {
            splitExploradorTrabajo.setLeftComponent(panelExplorador);
            splitExploradorTrabajo.setDividerSize(6);
            SwingUtilities.invokeLater(() -> splitExploradorTrabajo.setDividerLocation(220));
        } else {
            splitExploradorTrabajo.setLeftComponent(null);
            splitExploradorTrabajo.setDividerSize(0);
        }
        splitExploradorTrabajo.revalidate();
        splitExploradorTrabajo.repaint();
    }

    public void alternarPrompt() { mostrarPrompt(!promptVisible); }

    public void mostrarPrompt(boolean mostrar) {
        promptVisible = mostrar;
        if (mostrar) {
            splitEditorPrompt.setRightComponent(panelPrompt);
            splitEditorPrompt.setDividerSize(6);
            SwingUtilities.invokeLater(() -> splitEditorPrompt.setDividerLocation(0.70));
        } else {
            splitEditorPrompt.setRightComponent(null);
            splitEditorPrompt.setDividerSize(0);
        }
        splitEditorPrompt.revalidate();
        splitEditorPrompt.repaint();
    }

    public void alternarResultados() { mostrarResultados(!resultadosVisible); }

    public void mostrarResultados(boolean mostrar) {
        resultadosVisible = mostrar;
        if (mostrar) {
            splitVerticalPrincipal.setBottomComponent(panelResultados);
            splitVerticalPrincipal.setDividerSize(6);
            SwingUtilities.invokeLater(() -> splitVerticalPrincipal.setDividerLocation(0.68));
        } else {
            splitVerticalPrincipal.setBottomComponent(null);
            splitVerticalPrincipal.setDividerSize(0);
        }
        splitVerticalPrincipal.revalidate();
        splitVerticalPrincipal.repaint();
    }

    public void mostrarTokens() { mostrarFrame(tablaTokens); }
    public void mostrarErrores() { mostrarFrame(tablaErrores); }
    public void mostrarEstadisticas() { mostrarFrame(tablaEstadisticas); }
    public void mostrarSimbolos() { mostrarFrame(tablaSimbolos); }
    public void mostrarSalida() { mostrarFrame(tablaSalida); }

    private void mostrarFrame(JInternalFrame frame) {
        if (!resultadosVisible) mostrarResultados(true);
        for (JInternalFrame ventana : desktopResultados.getAllFrames()) ventana.setVisible(false);
        frame.setVisible(true);
        SwingUtilities.invokeLater(() -> {
            frame.setBounds(0, 0, desktopResultados.getWidth(), desktopResultados.getHeight());
            try { frame.setMaximum(true); frame.setSelected(true); }
            catch (PropertyVetoException ex) { }
            frame.toFront();
        });
    }

    private void actualizarPosicionCursor() {
        try {
            int posicion = txtEditor.getCaretPosition();
            int linea = txtEditor.getLineOfOffset(posicion);
            int columna = posicion - txtEditor.getLineStartOffset(linea);
            lblPosicionCursor.setText(" Ln " + (linea + 1) + ", Col " + (columna + 1));
        } catch (Exception ex) {
            lblPosicionCursor.setText(" Ln 1, Col 1");
        }
    }

    public void actualizarArchivo(String nombre) {
        if (nombre == null || nombre.isBlank()) nombre = "Sin archivo.pz";
        lblArchivoActual.setText(nombre);

        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("PROMPTZAL");
        raiz.add(new DefaultMutableTreeNode(nombre));
        arbolArchivos.setModel(new DefaultTreeModel(raiz));
    }

    public void actualizarEstado(String estado) {
        lblEstadoCompilacion.setText((estado == null ? "Listo" : estado) + " ");
    }

    public JPanel getPanelPrincipal() { return panelPrincipal; }
    public JTextArea getTxtEditor() { return txtEditor; }
    public JTextArea getTxtPromptGenerado() { return txtPromptGenerado; }
    public JButton getBtnGuardarPrompt() { return btnGuardarPrompt; }
    public TablaTokens getTablaTokens() { return tablaTokens; }
    public TablaErroes getTablaErrores() { return tablaErrores; }
    public TablaEstadisticas getTablaEstadisticas() { return tablaEstadisticas; }
    public TablaSimbolos getTablaSimbolos() { return tablaSimbolos; }
    public TablaSalida getTablaSalida() { return tablaSalida; }
    public JDesktopPane getDesktopResultados() { return desktopResultados; }
    public boolean isExploradorVisible() { return exploradorVisible; }
    public boolean isPromptVisible() { return promptVisible; }
    public boolean isResultadosVisible() { return resultadosVisible; }
}
