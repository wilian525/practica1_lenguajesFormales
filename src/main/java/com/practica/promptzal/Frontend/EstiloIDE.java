/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.practica.promptzal.Frontend;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class EstiloIDE {

    private final Color fondo = new Color(13, 17, 23);
    private final Color panel = new Color(22, 27, 34);
    private final Color panelClaro = new Color(28, 35, 44);
    private final Color borde = new Color(48, 54, 61);
    private final Color texto = new Color(230, 237, 243);
    private final Color textoSecundario = new Color(139, 148, 158);
    private final Color azul = new Color(88, 166, 255);
    private final Color azulOscuro = new Color(31, 111, 235);
    private final Color verde = new Color(63, 185, 80);
    private final Color rojo = new Color(248, 81, 73);
    private final Color verdeLinea = new Color(86, 211, 100);
    private final Font fuente = new Font("SansSerif", Font.PLAIN, 13);
    private final Font fuenteEditor = new Font("Monospaced", Font.PLAIN, 14);

    public void configurarTemaGlobal() {
        UIManager.put("Panel.background", fondo);
        UIManager.put("Label.foreground", texto);
        UIManager.put("ToolBar.background", panel);
        UIManager.put("ToolBar.border", BorderFactory.createMatteBorder(0, 0, 1, 0, borde));
        UIManager.put("Button.background", panelClaro);
        UIManager.put("Button.foreground", texto);
        UIManager.put("TextArea.background", fondo);
        UIManager.put("TextArea.foreground", texto);
        UIManager.put("TextArea.caretForeground", azul);
        UIManager.put("TextArea.selectionBackground", azulOscuro);
        UIManager.put("TextArea.selectionForeground", Color.WHITE);
        UIManager.put("ScrollPane.background", fondo);
        UIManager.put("Viewport.background", fondo);
        UIManager.put("Table.background", fondo);
        UIManager.put("Table.foreground", texto);
        UIManager.put("Table.selectionBackground", azulOscuro);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("Table.gridColor", borde);
        UIManager.put("TableHeader.background", panelClaro);
        UIManager.put("TableHeader.foreground", azul);
        UIManager.put("Tree.background", panel);
        UIManager.put("Tree.textForeground", texto);
        UIManager.put("Tree.selectionBackground", azulOscuro);
        UIManager.put("Tree.selectionForeground", Color.WHITE);
        UIManager.put("Desktop.background", fondo);
        UIManager.put("InternalFrame.activeTitleBackground", azulOscuro);
        UIManager.put("InternalFrame.activeTitleForeground", Color.WHITE);
        UIManager.put("InternalFrame.inactiveTitleBackground", panelClaro);
        UIManager.put("InternalFrame.inactiveTitleForeground", textoSecundario);
        UIManager.put("MenuBar.background", panel);
        UIManager.put("Menu.background", panel);
        UIManager.put("Menu.foreground", texto);
        UIManager.put("MenuItem.background", panel);
        UIManager.put("MenuItem.foreground", texto);
        UIManager.put("Separator.foreground", borde);
    }

    public void aplicar(GestorVistaIDE vista) {
        aplicarComponente(vista.getPanelPrincipal());

        estilizarEditor(vista.getTxtEditor());
        estilizarEditor(vista.getTxtPromptGenerado());

        estilizarTabla(vista.getTablaTokens().getTablaTokens());
        estilizarTabla(vista.getTablaErrores().getTabla());
        estilizarTabla(vista.getTablaEstadisticas().getTabla());
        estilizarTabla(vista.getTablaSimbolos().getTabla());

        JTextArea salida = vista.getTablaSalida().getTxtSalida();
        salida.setBackground(fondo);
        salida.setForeground(verde);
        salida.setCaretColor(azul);
        salida.setFont(fuenteEditor);

        vista.getDesktopResultados().setBackground(fondo);

        estilizarInternalFrame(vista.getTablaTokens());
        estilizarInternalFrame(vista.getTablaErrores());
        estilizarInternalFrame(vista.getTablaEstadisticas());
        estilizarInternalFrame(vista.getTablaSimbolos());
        estilizarInternalFrame(vista.getTablaSalida());

        SwingUtilities.updateComponentTreeUI(vista.getPanelPrincipal());
    }

    private void aplicarComponente(Component componente) {
        componente.setFont(fuente);

        if (componente instanceof JPanel panelActual) {
            panelActual.setBackground(fondo);
        }
        if (componente instanceof JToolBar barra) {
            barra.setBackground(panel);
            barra.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borde));
        }

        if (componente instanceof JLabel label) {
            label.setForeground(texto);
            label.setBorder(new EmptyBorder(6, 8, 6, 8));
        }

        if (componente instanceof JButton boton) {
            estilizarBoton(boton);
        }

        if (componente instanceof JScrollPane scroll) {
            scroll.setBorder(BorderFactory.createLineBorder(borde));
            scroll.getViewport().setBackground(fondo);
        }

        if (componente instanceof JTree arbol) {
            arbol.setBackground(panel);
            arbol.setForeground(texto);
            arbol.setFont(fuente);
        }

        if (componente instanceof JSplitPane split) {
            split.setBackground(borde);
            split.setBorder(null);
        }

        if (componente instanceof Container contenedor) {
            for (Component hijo : contenedor.getComponents()) {
                aplicarComponente(hijo);
            }
        }
    }

    private void estilizarEditor(JTextArea area) {
        area.setBackground(fondo);
        area.setForeground(texto);
        area.setCaretColor(azul);
        area.setSelectionColor(azulOscuro);
        area.setSelectedTextColor(Color.WHITE);
        area.setFont(fuenteEditor);
        area.setMargin(new Insets(12, 14, 12, 14));
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(fondo);
        tabla.setForeground(texto);
        tabla.setSelectionBackground(azulOscuro);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(verdeLinea);
        tabla.setRowHeight(27);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setFont(fuente);

        JTableHeader header = tabla.getTableHeader();
        header.setOpaque(true);
        header.setBackground(panelClaro);
        header.setForeground(azul);
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, verdeLinea));

        JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(
                JScrollPane.class, tabla
        );

        if (scroll != null) {
            scroll.setBorder(BorderFactory.createLineBorder(verdeLinea));
        }
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(panelClaro);
        boton.setForeground(texto);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borde),
                new EmptyBorder(6, 12, 6, 12)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(azulOscuro);
                boton.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(panelClaro);
                boton.setForeground(texto);
            }
        });
    }

    private void estilizarInternalFrame(JInternalFrame frame) {
        frame.getContentPane().setBackground(fondo);
        frame.setBorder(BorderFactory.createLineBorder(borde));
    }

    public void aplicarBotonPrincipal(JButton boton) {
        estilizarBoton(boton);
        boton.setBackground(verde);
        boton.setForeground(Color.WHITE);

        for (MouseListener listener : boton.getMouseListeners()) {
            boton.removeMouseListener(listener);
        }

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(new Color(46, 160, 67));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(verde);
            }
        });
    }

    public void aplicarBotonAzul(JButton boton) {
        estilizarBoton(boton);
        boton.setForeground(azul);
    }

    public void aplicarEstadoCorrecto(JLabel label) {
        label.setForeground(verde);
    }

    public void aplicarEstadoError(JLabel label) {
        label.setForeground(rojo);
    }

    public void aplicarEstadoNormal(JLabel label) {
        label.setForeground(textoSecundario);
    }

    public Color getFondo() {
        return fondo;
    }

    public Color getPanel() {
        return panel;
    }

    public Color getAzul() {
        return azul;
    }

    public Color getVerde() {
        return verde;
    }
}
