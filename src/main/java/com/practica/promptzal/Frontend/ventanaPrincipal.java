/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.practica.promptzal.Frontend;

import com.practica.promptzal.Backend.archivos.GestorArchivo;
import com.practica.promptzal.Backend.lexer.ErrorLexico;
import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import com.practica.promptzal.Backend.reporte.GeneradorReporteEstadisticasHTML;
import com.practica.promptzal.Backend.reporte.GeneradorReporteTokensHTML;
import com.practica.promptzal.Backend.reporte.GenerarReporteErroresHTML;
import com.practica.promptzal.Backend.lexer.Lexer;
import java.io.StringReader;
import java.util.ArrayList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author wilian
 */
public class ventanaPrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ventanaPrincipal.class.getName());

    private GestorArchivo gestorArchivo;

    private ArrayList<Token> tokensLexicos;
    private ArrayList<ErrorLexico> erroresLexicos;

    private Path rutaArchivoActual;

    private String contenidoArchivo;

    private TablaTokens tablaTokens;
    private TablaErroes tablaErroes;
    private TablaEstadisticas tablaEstadisticas;

    private JDesktopPane escritorio;
    private JTextArea editor;
    private Path rutaImagenAFD;

    public ventanaPrincipal() {

        initComponents();

        gestorArchivo = new GestorArchivo();

        contenidoArchivo = "";
        rutaArchivoActual = null;
        tokensLexicos = null;
        erroresLexicos = null;

        /*
     * Los JFileChooser están colocados visualmente
     * en el formulario de NetBeans, pero no queremos
     * mostrarlos al iniciar.
         */
        jFileChooser1.setVisible(false);
        jFileChooser3.setVisible(false);

        /*
     * Grupo de radio buttons.
         */
        ButtonGroup grupo = new ButtonGroup();

        grupo.add(jRadioButtonTokens);
        grupo.add(jRadioButtonErroes);
        grupo.add(jRadioButtonEstadisticas);

        /*
     * Creamos las tres ventanas de tablas.
         */
        tablaTokens = new TablaTokens();
        tablaErroes = new TablaErroes();
        tablaEstadisticas = new TablaEstadisticas();

        /*
     * Escritorio para colocar los JInternalFrame.
         */
        escritorio = new JDesktopPane();

        jPanelMostrarTablas.setLayout(new BorderLayout());

        jPanelMostrarTablas.removeAll();

        jPanelMostrarTablas.add(
                escritorio,
                BorderLayout.CENTER
        );

        jPanelMostrarTablas.revalidate();
        jPanelMostrarTablas.repaint();

        /*
     * Al iniciar todavía no hay resultados.
         */
        jButtonExportarHTML.setEnabled(false);

        jLabelTokens.setText("Tokens: 0");
        jLabelErroes.setText("Errores: 0");
        jLabelLineas.setText("Lineas: 0");

        configurarEditor();

        setTitle("PROMPTZAL - ANALIZADOR LEXICO");
        setLocationRelativeTo(null);
    }

    private void configurarEditor() {

        editor = new JTextArea();

        editor.setFont(
                new Font("Monospaced", Font.PLAIN, 14)
        );

        editor.setTabSize(4);

        JScrollPane scrollEditor
                = new JScrollPane(editor);

        /*
     * El JFileChooser que estaba dibujado en el panel
     * se sustituye por el editor.
         */
        jPanel2.removeAll();

        jPanel2.setLayout(new BorderLayout());

        jLabel3.setText("ARCHIVO .PZ");

        jPanel2.add(jLabel3, BorderLayout.NORTH);

        jPanel2.add(scrollEditor, BorderLayout.CENTER);

        jPanel2.setPreferredSize(
                new Dimension(400, 420)
        );

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    private void configurarVentana() {

        setTitle("PROMTZAL - ANALIZADOR");

        setLocationRelativeTo(null);

        /*
         * Estos JFileChooser pertenecen al diseño de NetBeans,
         * pero no queremos mostrarlos directamente al iniciar.
         */
        jFileChooser1.setVisible(false);
        jFileChooser3.setVisible(false);

        /*
         * El panel de resultados utilizará un JDesktopPane
         * para mostrar los JInternalFrame.
         */
        escritorio = new JDesktopPane();

        jPanelMostrarTablas.setLayout(new BorderLayout());
        jPanelMostrarTablas.removeAll();
        jPanelMostrarTablas.add(escritorio, BorderLayout.CENTER);

        /*
         * Agrupamos los radio buttons para que solamente
         * uno pueda estar seleccionado.
         */
        ButtonGroup grupoResultados = new ButtonGroup();

        grupoResultados.add(jRadioButtonTokens);
        grupoResultados.add(jRadioButtonErroes);
        grupoResultados.add(jRadioButtonEstadisticas);

        /*
         * Al iniciar no hay ningún resultado seleccionado.
         */
        jRadioButtonTokens.setSelected(false);
        jRadioButtonErroes.setSelected(false);
        jRadioButtonEstadisticas.setSelected(false);

        /*
         * Los resultados todavía no están disponibles.
         */
        jButtonExportarHTML.setEnabled(false);

        /*
         * Los contadores comienzan en cero.
         */
        jLabelTokens.setText("Tokens: 0");
        jLabelErroes.setText("Errores: 0");
        jLabelLineas.setText("Lineas: 0");
    }

    private void configurarComponentes() {

        /*
         * Los internal frames se crean una sola vez.
         */
        tablaTokens = new TablaTokens();
        tablaErroes = new TablaErroes();
        tablaEstadisticas = new TablaEstadisticas();

        /*
         * Al principio no mostramos ningún internal frame.
         */
        tablaTokens.setVisible(false);
        tablaErroes.setVisible(false);
        tablaEstadisticas.setVisible(false);

        /*
         * Filtros para los JFileChooser.
         */
        jFileChooser1.setFileFilter(
                new FileNameExtensionFilter(
                        "Archivos PromptZal (*.pz)",
                        "pz"
                )
        );

        jFileChooser3.setFileFilter(
                new FileNameExtensionFilter(
                        "Archivos HTML (*.html)",
                        "html"
                )
        );
    }

    private int contarLineas(String contenido) {

        if (contenido == null || contenido.isEmpty()) {
            return 0;
        }

        int lineas = 1;

        for (int i = 0; i < contenido.length(); i++) {

            if (contenido.charAt(i) == '\n') {
                lineas++;
            }
        }

        return lineas;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButtonNuevo = new javax.swing.JButton();
        jButtonAbrirPz = new javax.swing.JButton();
        jButtonGuardarPz = new javax.swing.JButton();
        jButtonAnalizar = new javax.swing.JButton();
        jButtonSalir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jFileChooser1 = new javax.swing.JFileChooser();
        jLabel3 = new javax.swing.JLabel();
        jButtonCargarAFD = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        JpanelMonstrarAutomata = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jRadioButtonTokens = new javax.swing.JRadioButton();
        jRadioButtonErroes = new javax.swing.JRadioButton();
        jRadioButtonEstadisticas = new javax.swing.JRadioButton();
        jLabelTokens = new javax.swing.JLabel();
        jLabelErroes = new javax.swing.JLabel();
        jLabelLineas = new javax.swing.JLabel();
        jButtonExportarHTML = new javax.swing.JButton();
        jFileChooser3 = new javax.swing.JFileChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelMostrarTablas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 153, 51)));

        jLabel1.setText("PROMTZAL -ANALIZADOR");

        jButtonNuevo.setText("Nuevo");
        jButtonNuevo.addActionListener(this::jButtonNuevoActionPerformed);

        jButtonAbrirPz.setText("Abrir.pz");
        jButtonAbrirPz.addActionListener(this::jButtonAbrirPzActionPerformed);

        jButtonGuardarPz.setText("guardar .pz");
        jButtonGuardarPz.addActionListener(this::jButtonGuardarPzActionPerformed);

        jButtonAnalizar.setText("Analizar");
        jButtonAnalizar.addActionListener(this::jButtonAnalizarActionPerformed);

        jButtonSalir.setText("Salir");
        jButtonSalir.addActionListener(this::jButtonSalirActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(629, 629, 629)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jButtonNuevo)
                        .addGap(111, 111, 111)
                        .addComponent(jButtonAbrirPz)
                        .addGap(96, 96, 96)
                        .addComponent(jButtonGuardarPz)
                        .addGap(64, 64, 64)
                        .addComponent(jButtonAnalizar)
                        .addGap(75, 75, 75)
                        .addComponent(jButtonSalir)))
                .addContainerGap(549, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonNuevo)
                    .addComponent(jButtonAbrirPz)
                    .addComponent(jButtonGuardarPz)
                    .addComponent(jButtonAnalizar)
                    .addComponent(jButtonSalir))
                .addGap(21, 21, 21))
        );

        jPanel2.setBackground(new java.awt.Color(204, 255, 204));

        jLabel3.setText("ARCHIVO .PZ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addComponent(jLabel3))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(jFileChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jButtonCargarAFD.setBackground(new java.awt.Color(255, 204, 0));
        jButtonCargarAFD.setForeground(new java.awt.Color(0, 0, 0));
        jButtonCargarAFD.setText("construir AFD");
        jButtonCargarAFD.addActionListener(this::jButtonCargarAFDActionPerformed);

        jLabel2.setFont(new java.awt.Font("Bitstream Vera Serif", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 51));
        jLabel2.setText("AUTOMATA FINITO DETERMINISTICO");

        JpanelMonstrarAutomata.setBackground(new java.awt.Color(0, 51, 51));

        javax.swing.GroupLayout JpanelMonstrarAutomataLayout = new javax.swing.GroupLayout(JpanelMonstrarAutomata);
        JpanelMonstrarAutomata.setLayout(JpanelMonstrarAutomataLayout);
        JpanelMonstrarAutomataLayout.setHorizontalGroup(
            JpanelMonstrarAutomataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 763, Short.MAX_VALUE)
        );
        JpanelMonstrarAutomataLayout.setVerticalGroup(
            JpanelMonstrarAutomataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(0, 51, 51));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(0, 0, 0)));
        jPanel4.setForeground(new java.awt.Color(0, 0, 0));

        jRadioButtonTokens.setText("TOKENS");
        jRadioButtonTokens.addActionListener(this::jRadioButtonTokensActionPerformed);

        jRadioButtonErroes.setText("ERRORES");
        jRadioButtonErroes.addActionListener(this::jRadioButtonErroesActionPerformed);

        jRadioButtonEstadisticas.setText("ESTADISTICA");
        jRadioButtonEstadisticas.addActionListener(this::jRadioButtonEstadisticasActionPerformed);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jRadioButtonTokens)
                .addGap(120, 120, 120)
                .addComponent(jRadioButtonErroes)
                .addGap(115, 115, 115)
                .addComponent(jRadioButtonEstadisticas)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButtonTokens)
                    .addComponent(jRadioButtonErroes)
                    .addComponent(jRadioButtonEstadisticas))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jLabelTokens.setText("tokens");

        jLabelErroes.setText("Erroes");

        jLabelLineas.setText("Lineas");

        jButtonExportarHTML.setBackground(new java.awt.Color(255, 102, 102));
        jButtonExportarHTML.setFont(new java.awt.Font("FantasqueSansM Nerd Font Propo", 3, 14)); // NOI18N
        jButtonExportarHTML.setForeground(new java.awt.Color(51, 51, 51));
        jButtonExportarHTML.setText("ExportarHTML");
        jButtonExportarHTML.addActionListener(this::jButtonExportarHTMLActionPerformed);

        javax.swing.GroupLayout jPanelMostrarTablasLayout = new javax.swing.GroupLayout(jPanelMostrarTablas);
        jPanelMostrarTablas.setLayout(jPanelMostrarTablasLayout);
        jPanelMostrarTablasLayout.setHorizontalGroup(
            jPanelMostrarTablasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 911, Short.MAX_VALUE)
        );
        jPanelMostrarTablasLayout.setVerticalGroup(
            jPanelMostrarTablasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 346, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(jPanelMostrarTablas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(150, 150, 150)
                                        .addComponent(jLabel2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jButtonCargarAFD))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(JpanelMonstrarAutomata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabelLineas)
                                .addGap(377, 377, 377))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(74, 74, 74)
                                        .addComponent(jLabelTokens)
                                        .addGap(87, 87, 87)
                                        .addComponent(jLabelErroes))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 913, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jFileChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(14, 14, 14))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButtonExportarHTML, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(127, 127, 127))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel2)
                        .addGap(20, 20, 20)
                        .addComponent(JpanelMonstrarAutomata, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jButtonCargarAFD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonExportarHTML, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jFileChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelTokens)
                                .addComponent(jLabelErroes))
                            .addComponent(jLabelLineas, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAbrirPzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbrirPzActionPerformed
        JFileChooser selector = new JFileChooser();

        selector.setDialogTitle("Abrir archivo PromptZal");

        selector.setFileFilter(
                new FileNameExtensionFilter(
                        "Archivo PromptZal (*.pz)",
                        "pz"
                )
        );

        int resultado = selector.showOpenDialog(this);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File archivo = selector.getSelectedFile();

        Path ruta = archivo.toPath();

        if (!gestorArchivo.esArchivoPz(ruta)) {

            JOptionPane.showMessageDialog(
                    this,
                    "El archivo seleccionado no tiene extensión .pz",
                    "Archivo no válido",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            contenidoArchivo = gestorArchivo.leerArchivo(ruta);

            rutaArchivoActual = ruta;

            editor.setText(contenidoArchivo);
            editor.setCaretPosition(0);
            /*
         * Cada archivo nuevo necesita un análisis nuevo.
             */
            tokensLexicos = null;
            erroresLexicos = null;

            /*
         * Limpiamos resultados anteriores.
             */
            tablaTokens.limpiarTabla();
            tablaErroes.limpiarTabla();
            tablaEstadisticas.limpiarTabla();

            escritorio.removeAll();
            escritorio.repaint();

            /*
         * Actualizamos información de la interfaz.
             */
            jLabelTokens.setText("Tokens: 0");
            jLabelErroes.setText("Errores: 0");

            int lineas = contarLineas(contenidoArchivo);

            jLabelLineas.setText(
                    "Lineas: " + lineas
            );

            jButtonExportarHTML.setEnabled(false);

            JOptionPane.showMessageDialog(
                    this,
                    "Archivo cargado correctamente:\n"
                    + archivo.getName(),
                    "Archivo abierto",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo leer el archivo:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_jButtonAbrirPzActionPerformed

    private void jRadioButtonTokensActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonTokensActionPerformed
        if (tokensLexicos == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debe analizar un archivo .pz.",
                    "Sin resultados",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        escritorio.removeAll();

        tablaTokens.setVisible(true);
        tablaErroes.setVisible(false);
        tablaEstadisticas.setVisible(false);

        escritorio.add(tablaTokens);

        tablaTokens.setLocation(
                20,
                20
        );

        tablaTokens.setSize(
                escritorio.getWidth() - 40,
                escritorio.getHeight() - 40
        );

        tablaTokens.setVisible(true);

        try {
            tablaTokens.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            // No se detiene el programa.
        }

        escritorio.revalidate();
        escritorio.repaint();
    }//GEN-LAST:event_jRadioButtonTokensActionPerformed

    private void jRadioButtonErroesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonErroesActionPerformed
        if (erroresLexicos == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debe analizar un archivo .pz.",
                    "Sin resultados",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        escritorio.removeAll();

        tablaTokens.setVisible(false);
        tablaErroes.setVisible(true);
        tablaEstadisticas.setVisible(false);

        escritorio.add(tablaErroes);

        tablaErroes.setLocation(
                20,
                20
        );

        tablaErroes.setSize(
                escritorio.getWidth() - 40,
                escritorio.getHeight() - 40
        );

        tablaErroes.setVisible(true);

        try {
            tablaErroes.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            // No se detiene el programa.
        }

        escritorio.revalidate();
        escritorio.repaint();
    }//GEN-LAST:event_jRadioButtonErroesActionPerformed

    private void jRadioButtonEstadisticasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButtonEstadisticasActionPerformed
        if (tokensLexicos == null || erroresLexicos == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debe analizar un archivo .pz.",
                    "Sin resultados",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        escritorio.removeAll();

        tablaTokens.setVisible(false);
        tablaErroes.setVisible(false);
        tablaEstadisticas.setVisible(true);

        tablaEstadisticas.limpiarTabla();

        int totalTokens = tokensLexicos.size();
        int totalErrores = erroresLexicos.size();
        int totalLineas = contarLineas(contenidoArchivo);

        tablaEstadisticas.cargarEstadisticas(
                totalTokens,
                totalErrores,
                totalLineas
        );

        EnumMap<TipoToken, Integer> frecuencias
                = new EnumMap<>(TipoToken.class);

        for (TipoToken tipo : TipoToken.values()) {
            frecuencias.put(tipo, 0);
        }

        for (Token token : tokensLexicos) {
            TipoToken tipo = token.getTipo();

            frecuencias.put(
                    tipo,
                    frecuencias.get(tipo) + 1
            );
        }

        for (Map.Entry<TipoToken, Integer> entrada
                : frecuencias.entrySet()) {

            if (entrada.getValue() > 0) {
                tablaEstadisticas.agregarEstadistica(
                        entrada.getKey().toString(),
                        entrada.getValue()
                );
            }
        }

        escritorio.add(tablaEstadisticas);

        tablaEstadisticas.setLocation(20, 20);

        tablaEstadisticas.setSize(
                escritorio.getWidth() - 40,
                escritorio.getHeight() - 40
        );

        tablaEstadisticas.setVisible(true);

        try {
            tablaEstadisticas.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            // No se detiene el programa.
        }

        escritorio.revalidate();
        escritorio.repaint();
    }//GEN-LAST:event_jRadioButtonEstadisticasActionPerformed

    private void jButtonCargarAFDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCargarAFDActionPerformed
        JOptionPane.showMessageDialog(
                this,
                "El analizador lexico del Proyecto 2 se genera con JFlex.",
                "JFlex",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_jButtonCargarAFDActionPerformed

    private void jButtonExportarHTMLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonExportarHTMLActionPerformed
        if (tokensLexicos == null || erroresLexicos == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Primero debe analizar un archivo .pz.",
                    "Sin resultados",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!jRadioButtonTokens.isSelected()
                && !jRadioButtonErroes.isSelected()
                && !jRadioButtonEstadisticas.isSelected()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione el reporte que desea exportar.",
                    "Reporte",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        JFileChooser selector
                = new JFileChooser();

        selector.setDialogTitle(
                "Guardar reporte HTML"
        );

        selector.setFileFilter(
                new FileNameExtensionFilter(
                        "Archivo HTML (*.html)",
                        "html"
                )
        );

        String nombre;

        if (jRadioButtonTokens.isSelected()) {

            nombre = "reporte_tokens.html";

        } else if (jRadioButtonErroes.isSelected()) {

            nombre = "reporte_errores.html";

        } else {

            nombre = "reporte_estadisticas.html";
        }

        selector.setSelectedFile(
                new File(nombre)
        );

        int resultado
                = selector.showSaveDialog(this);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path ruta
                = gestorArchivo.asegurarExtencionHTML(
                        selector.getSelectedFile().toPath()
                );

        try {

            if (jRadioButtonTokens.isSelected()) {

                GeneradorReporteTokensHTML generador
                        = new GeneradorReporteTokensHTML();

                Token[] arregloTokens
                        = tokensLexicos.toArray(new Token[0]);

                generador.generar(
                        arregloTokens,
                        arregloTokens.length,
                        ruta.toString()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Reporte de tokens generado correctamente:\n"
                        + ruta
                );

            } else if (jRadioButtonErroes.isSelected()) {

                GenerarReporteErroresHTML generador
                        = new GenerarReporteErroresHTML();

                ErrorLexico[] arregloErrores
                        = erroresLexicos.toArray(new ErrorLexico[0]);

                generador.generar(
                        arregloErrores,
                        arregloErrores.length,
                        ruta.toString()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Reporte de errores generado correctamente:\n"
                        + ruta
                );

            } else {

                GeneradorReporteEstadisticasHTML generador
                        = new GeneradorReporteEstadisticasHTML();

                Token[] arregloTokens
                        = tokensLexicos.toArray(new Token[0]);

                generador.generar(
                        arregloTokens,
                        arregloTokens.length,
                        erroresLexicos.size(),
                        contarLineas(contenidoArchivo),
                        ruta.toString()
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Reporte de estadisticas generado correctamente:\n"
                        + ruta
                );

            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo generar el reporte:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_jButtonExportarHTMLActionPerformed

    private void jButtonNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNuevoActionPerformed
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea crear un nuevo archivo?",
                "Nuevo archivo",
                JOptionPane.YES_NO_OPTION
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        contenidoArchivo = "";
        rutaArchivoActual = null;

        tokensLexicos = null;
        erroresLexicos = null;

        editor.setText("");

        tablaTokens.limpiarTabla();
        tablaErroes.limpiarTabla();
        tablaEstadisticas.limpiarTabla();

        escritorio.removeAll();
        escritorio.repaint();

        JpanelMonstrarAutomata.removeAll();
        JpanelMonstrarAutomata.revalidate();
        JpanelMonstrarAutomata.repaint();

        jLabelTokens.setText("Tokens: 0");
        jLabelErroes.setText("Errores: 0");
        jLabelLineas.setText("Lineas: 0");

        jButtonExportarHTML.setEnabled(false);

        jRadioButtonTokens.setSelected(false);
        jRadioButtonErroes.setSelected(false);
        jRadioButtonEstadisticas.setSelected(false);
    }//GEN-LAST:event_jButtonNuevoActionPerformed

    private void jButtonGuardarPzActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarPzActionPerformed
        contenidoArchivo = editor.getText();

        JFileChooser selector = new JFileChooser();

        selector.setDialogTitle("Guardar archivo PromptZal");

        selector.setFileFilter(
                new FileNameExtensionFilter(
                        "Archivo PromptZal (*.pz)",
                        "pz"
                )
        );

        if (rutaArchivoActual != null) {
            selector.setSelectedFile(rutaArchivoActual.toFile());
        }

        int resultado = selector.showSaveDialog(this);

        if (resultado != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path ruta = selector.getSelectedFile().toPath();

        try {
            Path rutaFinal = gestorArchivo.guardarArchivo(
                    ruta,
                    contenidoArchivo
            );

            rutaArchivoActual = rutaFinal;

            JOptionPane.showMessageDialog(
                    this,
                    "Archivo guardado correctamente:\n" + rutaFinal,
                    "Guardar",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo guardar el archivo:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_jButtonGuardarPzActionPerformed

    private void jButtonAnalizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnalizarActionPerformed
        contenidoArchivo = editor.getText();

        if (contenidoArchivo == null
                || contenidoArchivo.trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El editor esta vacio. Escriba o abra un archivo .pz.",
                    "Sin contenido",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {
            Lexer lexer = new Lexer(
                    new StringReader(contenidoArchivo)
            );

            tokensLexicos = new ArrayList<>();

            Token token;

            do {
                token = lexer.yylex();
                tokensLexicos.add(token);

            } while (token.getTipo() != TipoToken.EOF);

            erroresLexicos = lexer.getErrores();

            tablaTokens.cargarTokens(tokensLexicos);

            ErrorLexico[] arregloErrores
                    = erroresLexicos.toArray(new ErrorLexico[0]);

            tablaErroes.cargarErrores(
                    arregloErrores,
                    erroresLexicos.size()
            );

            tablaEstadisticas.cargarEstadisticas(
                    tokensLexicos.size(),
                    erroresLexicos.size(),
                    contarLineas(contenidoArchivo)
            );

            jLabelTokens.setText(
                    "Tokens: " + tokensLexicos.size()
            );

            jLabelErroes.setText(
                    "Errores: " + erroresLexicos.size()
            );

            jLabelLineas.setText(
                    "Lineas: " + contarLineas(contenidoArchivo)
            );

            jButtonExportarHTML.setEnabled(true);

            jRadioButtonTokens.setSelected(true);

            jRadioButtonTokensActionPerformed(null);

            JOptionPane.showMessageDialog(
                    this,
                    "Analisis lexico terminado correctamente.",
                    "Analisis",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ocurrio un error durante el analisis lexico:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_jButtonAnalizarActionPerformed

    private void jButtonSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalirActionPerformed
        int respuesta
                = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro que desea salir?",
                        "Salir",
                        JOptionPane.YES_NO_OPTION
                );

        if (respuesta == JOptionPane.YES_OPTION) {

            dispose();
        }
    }//GEN-LAST:event_jButtonSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel JpanelMonstrarAutomata;
    private javax.swing.JButton jButtonAbrirPz;
    private javax.swing.JButton jButtonAnalizar;
    private javax.swing.JButton jButtonCargarAFD;
    private javax.swing.JButton jButtonExportarHTML;
    private javax.swing.JButton jButtonGuardarPz;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JButton jButtonSalir;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JFileChooser jFileChooser3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelErroes;
    private javax.swing.JLabel jLabelLineas;
    private javax.swing.JLabel jLabelTokens;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelMostrarTablas;
    private javax.swing.JRadioButton jRadioButtonErroes;
    private javax.swing.JRadioButton jRadioButtonEstadisticas;
    private javax.swing.JRadioButton jRadioButtonTokens;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
