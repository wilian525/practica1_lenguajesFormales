/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.practica.promptzal.Frontend;

import com.practica.promptzal.Backend.afd.GeneradorAFD;
import com.practica.promptzal.Backend.archivos.GestorArchivo;
import com.practica.promptzal.Backend.ast.Programa;
import com.practica.promptzal.Backend.generador.GeneradorPromptzal;
import com.practica.promptzal.Backend.lexer.ErrorLexico;
import com.practica.promptzal.Backend.lexer.Lexer;
import com.practica.promptzal.Backend.lexer.TipoToken;
import com.practica.promptzal.Backend.lexer.Token;
import com.practica.promptzal.Backend.parser.ErrorSintactico;
import com.practica.promptzal.Backend.parser.Parser;
import com.practica.promptzal.Backend.reporte.GeneradorReporteAST;
import com.practica.promptzal.Backend.reporte.GeneradorReporteEstadisticasHTML;
import com.practica.promptzal.Backend.reporte.GeneradorReporteSimbolos;
import com.practica.promptzal.Backend.reporte.GeneradorReporteTokensHTML;
import com.practica.promptzal.Backend.reporte.GenerarReporteErroresHTML;
import com.practica.promptzal.Backend.semantico.AnalizadorSemantico;
import com.practica.promptzal.Backend.semantico.ErrorSemantico;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ventanaPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ventanaPrincipal.class.getName());

    private final GestorVistaIDE gestorVista;
    private final EstiloIDE estiloIDE;
    private final GestorArchivo gestorArchivo;

    private JButton btnExplorador;
    private JButton btnPrompt;
    private JButton btnResultados;

    private Path rutaActual;
    private boolean cambiosSinGuardar;
    private boolean actualizandoEditor;
    private boolean compilacionDisponible;

    private String codigoCompilado = "";
    private Programa programaActual;
    private AnalizadorSemantico semanticoActual;

    private ArrayList<Token> tokensActuales = new ArrayList<>();
    private ArrayList<ErrorLexico> erroresLexicosActuales = new ArrayList<>();
    private ArrayList<ErrorSintactico> erroresSintacticosActuales = new ArrayList<>();
    private ArrayList<ErrorSemantico> erroresSemanticosActuales = new ArrayList<>();

    public ventanaPrincipal() {
        estiloIDE = new EstiloIDE();
        estiloIDE.configurarTemaGlobal();
        gestorArchivo = new GestorArchivo();

        initComponents();

        gestorVista = new GestorVistaIDE();

        configurarVentana();
        configurarInterfaz();
        configurarEventos();

        estiloIDE.aplicar(gestorVista);
        aplicarEstiloPrincipal();

        nuevoEstadoInicial();
    }

    private void configurarVentana() {
        setTitle("PromptZal IDE - Sin titulo.pz");
        setMinimumSize(new Dimension(1050, 650));
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (confirmarCambiosPendientes()) dispose();
            }
        });
    }

    private void configurarInterfaz() {
        btnExplorador = new JButton("EXPLORADOR");
        btnPrompt = new JButton("PROMPT");
        btnResultados = new JButton("RESULTADOS");

        jLabel2.setText("PROMPTZAL IDE");

        jComboBoxReportes.setModel(new DefaultComboBoxModel<>(new String[]{
            "Reporte AST",
            "Reporte de Simbolos",
            "Reporte de Errores",
            "Reporte de Estadisticas",
            "Reporte de Tokens",
            "AFD General"
        }));

        jButtonExportar.setText("EXPORTAR REPORTE");

        jPanel2.removeAll();
        jPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 10));

        jPanel2.add(btnExplorador);
        jPanel2.add(btnPrompt);
        jPanel2.add(btnResultados);
        jPanel2.add(jButtonNuevo);
        jPanel2.add(jButtonAbrir);
        jPanel2.add(jButtonGuardar);
        jPanel2.add(jButtonCompilar);
        jPanel2.add(jComboBoxReportes);
        jPanel2.add(jButtonExportar);

        jPanel2.setPreferredSize(new Dimension(0, 58));

        JPanel panelTitulos = new JPanel(new BorderLayout());
        panelTitulos.add(jLabel1, BorderLayout.WEST);
        panelTitulos.add(jLabel2, BorderLayout.CENTER);

        jPanel3.removeAll();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(panelTitulos, BorderLayout.NORTH);
        jPanel3.add(jPanel2, BorderLayout.CENTER);

        jPanel1.removeAll();
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(jPanel3, BorderLayout.NORTH);
        jPanel1.add(gestorVista.getPanelPrincipal(), BorderLayout.CENTER);

        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void aplicarEstiloPrincipal() {
        Color fondo = estiloIDE.getFondo();
        Color panel = estiloIDE.getPanel();
        Color azul = estiloIDE.getAzul();

        jPanel1.setBackground(fondo);
        jPanel2.setBackground(panel);
        jPanel3.setBackground(panel);

        if (jLabel1.getParent() instanceof JPanel panelTitulos) {
            panelTitulos.setBackground(panel);
        }

        jLabel1.setForeground(azul);
        jLabel2.setForeground(Color.WHITE);

        jLabel1.setFont(new Font("SansSerif", Font.BOLD, 13));
        jLabel2.setFont(new Font("SansSerif", Font.BOLD, 20));

        jComboBoxReportes.setBackground(panel);
        jComboBoxReportes.setForeground(Color.WHITE);
        jComboBoxReportes.setPreferredSize(new Dimension(220, 32));

        estiloIDE.aplicarBotonAzul(btnExplorador);
        estiloIDE.aplicarBotonAzul(btnPrompt);
        estiloIDE.aplicarBotonAzul(btnResultados);
        estiloIDE.aplicarBotonAzul(jButtonNuevo);
        estiloIDE.aplicarBotonAzul(jButtonAbrir);
        estiloIDE.aplicarBotonAzul(jButtonGuardar);
        estiloIDE.aplicarBotonAzul(jButtonExportar);

        jButtonCompilar.setBackground(estiloIDE.getVerde());
        jButtonCompilar.setForeground(Color.WHITE);
        jButtonCompilar.setFocusPainted(false);

        SwingUtilities.updateComponentTreeUI(this);
    }

    private void configurarEventos() {
        btnExplorador.addActionListener(e -> gestorVista.alternarExplorador());
        btnPrompt.addActionListener(e -> gestorVista.alternarPrompt());
        btnResultados.addActionListener(e -> gestorVista.alternarResultados());

        jButtonNuevo.addActionListener(e -> nuevoArchivo());
        jButtonAbrir.addActionListener(e -> abrirArchivo());
        jButtonCompilar.addActionListener(e -> compilar());
        jButtonExportar.addActionListener(e -> exportarReporte());

        gestorVista.getBtnGuardarPrompt().addActionListener(e -> guardarPrompt());

        gestorVista.getTxtEditor().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                documentoModificado();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentoModificado();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentoModificado();
            }
        });
    }

    private void nuevoEstadoInicial() {
        rutaActual = null;
        cambiosSinGuardar = false;
        actualizandoEditor = false;

        limpiarCompilacion();

        gestorVista.actualizarArchivo("Sin titulo.pz");
        gestorVista.actualizarEstado("Listo");

        actualizarTitulo();
    }

    private void nuevoArchivo() {
        if (!confirmarCambiosPendientes()) return;

        actualizandoEditor = true;
        gestorVista.getTxtEditor().setText("");
        actualizandoEditor = false;

        rutaActual = null;
        cambiosSinGuardar = false;

        limpiarCompilacion();

        gestorVista.actualizarArchivo("Sin titulo.pz");
        gestorVista.actualizarEstado("Nuevo archivo");

        actualizarTitulo();
    }

    private void abrirArchivo() {
        JFileChooser selector = new JFileChooser();

        selector.setDialogTitle("Abrir archivo PromptZal");
        selector.setFileFilter( new FileNameExtensionFilter( "Archivos PromptZal (*.pz)", "pz")
        );

        if (selector.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        Path ruta = selector.getSelectedFile().toPath();

        if (!gestorArchivo.esArchivoPz(ruta)) {
            mostrarAdvertencia("Selecciona un archivo con extension .pz");
            return;
        }

        if (!confirmarCambiosPendientes()) return;

        try {
            String contenido = gestorArchivo.leerArchivo(ruta);

            actualizandoEditor = true;

            gestorVista.getTxtEditor().setText(contenido);
            gestorVista.getTxtEditor().setCaretPosition(0);

            actualizandoEditor = false;

            rutaActual = ruta.toAbsolutePath().normalize();
            cambiosSinGuardar = false;

            limpiarCompilacion();

            gestorVista.actualizarArchivo( rutaActual.getFileName().toString() );

            gestorVista.actualizarEstado("Archivo abierto");

            actualizarTitulo();

        } catch (IOException ex) {
            actualizandoEditor = false;
            mostrarError( "No se pudo abrir el archivo: " + ex.getMessage() );
        }
    }

    private boolean guardarArchivo() {
        Path destino = rutaActual;

        if (destino == null) {
            JFileChooser selector = new JFileChooser();

            selector.setDialogTitle("Guardar archivo PromptZal");
            selector.setFileFilter(
                    new FileNameExtensionFilter( "Archivos PromptZal (*.pz)","pz" )
            );

            selector.setSelectedFile( new File("archivo.pz") );

            if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            destino = selector.getSelectedFile().toPath();
        }

        try {
            rutaActual = gestorArchivo.guardarArchivo(destino,gestorVista.getTxtEditor().getText()).toAbsolutePath().normalize();

            cambiosSinGuardar = false;

            gestorVista.actualizarArchivo(rutaActual.getFileName().toString());

            gestorVista.actualizarEstado("Archivo guardado");

            actualizarTitulo();

            return true;

        } catch (IOException | IllegalArgumentException ex) {
            mostrarError(
                    "No se pudo guardar el archivo: "+ ex.getMessage());

            return false;
        }
    }

    private boolean confirmarCambiosPendientes() {
        if (!cambiosSinGuardar) return true;

        int opcion = JOptionPane.showConfirmDialog( this, "Hay cambios sin guardar. ¿Deseas guardarlos?","PromptZal",
                JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE );

        if (opcion == JOptionPane.CANCEL_OPTION|| opcion == JOptionPane.CLOSED_OPTION) {
            return false;
        }

        if (opcion == JOptionPane.YES_OPTION) {
            return guardarArchivo();
        }

        return true;
    }

    private void documentoModificado() {
        if (actualizandoEditor) return;

        cambiosSinGuardar = true;

        if (compilacionDisponible) {
            limpiarCompilacion();
        }

        gestorVista.actualizarEstado("Cambios sin compilar");
        actualizarTitulo();
    }

    private void actualizarTitulo() {
        String nombre;

        if (rutaActual == null) {
            nombre = "Sin titulo.pz";
        } else {
            nombre = rutaActual.getFileName().toString();
        }

        setTitle("PromptZal IDE - "+ nombre + (cambiosSinGuardar ? " *" : ""));
    }

    private void compilar() {
        String codigo = gestorVista.getTxtEditor().getText();

        if (codigo.isBlank()) {
            mostrarAdvertencia("El editor esta vacio.");
            return;
        }

        limpiarCompilacion();

        gestorVista.actualizarEstado("Compilando...");
        gestorVista.getTablaSalida().agregarMensaje(
                "[PromptZal] Compilacion iniciada..."
        );

        try {
            Lexer lexer = new Lexer(
                    new StringReader(codigo)
            );

            Token token;

            do {
                token = lexer.yylex();

                if (token != null) {
                    tokensActuales.add(token);
                }

            } while (token != null
                    && token.getTipo() != TipoToken.EOF);

            erroresLexicosActuales.addAll(
                    lexer.getErrores()
            );

            gestorVista.getTablaSalida().agregarMensaje(
                    "[Lexer] "
                    + tokensActuales.size()
                    + " tokens reconocidos."
            );

            gestorVista.getTablaSalida().agregarMensaje(
                    "[Lexer] "
                    + erroresLexicosActuales.size()
                    + " errores."
            );

            Parser parser = new Parser(tokensActuales);

            programaActual = parser.parsear();

            erroresSintacticosActuales.addAll(
                    parser.getErrores()
            );

            gestorVista.getTablaSalida().agregarMensaje(
                    "[Parser] Analisis sintactico finalizado."
            );

            gestorVista.getTablaSalida().agregarMensaje(
                    "[Parser] "
                    + erroresSintacticosActuales.size()
                    + " errores."
            );

            semanticoActual = new AnalizadorSemantico();

            if (programaActual != null) {
                semanticoActual.analizar(programaActual);
            }

            erroresSemanticosActuales.addAll(
                    semanticoActual.getErrores()
            );

            gestorVista.getTablaSalida().agregarMensaje(
                    "[Semantico] Analisis finalizado."
            );

            gestorVista.getTablaSalida().agregarMensaje(
                    "[Semantico] "
                    + erroresSemanticosActuales.size()
                    + " errores."
            );

            codigoCompilado = codigo;
            compilacionDisponible = true;

            cargarResultadosEnVista();

            int totalErrores
                    = erroresLexicosActuales.size()
                    + erroresSintacticosActuales.size()
                    + erroresSemanticosActuales.size();

            if (totalErrores == 0
                    && programaActual != null) {

                GeneradorPromptzal generador
                        = new GeneradorPromptzal();

                String prompt
                        = generador.generar(
                                programaActual
                        );

                gestorVista
                        .getTxtPromptGenerado()
                        .setText(prompt);

                gestorVista
                        .getTxtPromptGenerado()
                        .setCaretPosition(0);

                gestorVista
                        .getTablaSalida()
                        .agregarMensaje(
                                "[Prompt] Prompt generado correctamente."
                        );

                gestorVista
                        .getTablaSalida()
                        .agregarMensaje(
                                "[PromptZal] Compilacion finalizada sin errores."
                        );

                gestorVista.actualizarEstado(
                        "Compilacion correcta"
                );

                gestorVista.mostrarPrompt(true);

            } else {
                gestorVista
                        .getTxtPromptGenerado()
                        .setText("");

                gestorVista
                        .getTablaSalida()
                        .agregarMensaje(
                                "[Prompt] No se genero el prompt porque existen errores."
                        );

                gestorVista
                        .getTablaSalida()
                        .agregarMensaje(
                                "[PromptZal] Compilacion finalizada con "
                                + totalErrores
                                + " errores."
                        );

                gestorVista.actualizarEstado(
                        totalErrores + " errores"
                );

                gestorVista.mostrarErrores();
            }

        } catch (IOException ex) {
            compilacionDisponible = false;

            gestorVista.actualizarEstado(
                    "Error de compilacion"
            );

            gestorVista
                    .getTablaSalida()
                    .agregarMensaje(
                            "[Lexer] Error: "
                            + ex.getMessage()
                    );

            gestorVista.mostrarSalida();

            mostrarError(
                    "No se pudo ejecutar el analizador lexico: "
                    + ex.getMessage()
            );
        }
    }

    private void cargarResultadosEnVista() {
        gestorVista
                .getTablaTokens()
                .cargarTokens(
                        tokensActuales
                );

        gestorVista
                .getTablaErrores()
                .cargarErrores(
                        erroresLexicosActuales,
                        erroresSintacticosActuales,
                        erroresSemanticosActuales
                );

        gestorVista
                .getTablaEstadisticas()
                .cargarEstadisticas(
                        tokensActuales,
                        erroresLexicosActuales,
                        erroresSintacticosActuales,
                        erroresSemanticosActuales,
                        codigoCompilado
                );

        gestorVista
                .getTablaSimbolos()
                .cargarSimbolos(
                        semanticoActual
                                .getTablaSimbolos()
                                .getSimbolos()
                );
    }

    private void limpiarCompilacion() {
        compilacionDisponible = false;
        codigoCompilado = "";
        programaActual = null;
        semanticoActual = null;

        tokensActuales.clear();
        erroresLexicosActuales.clear();
        erroresSintacticosActuales.clear();
        erroresSemanticosActuales.clear();

        if (gestorVista != null) {
            gestorVista.getTxtPromptGenerado().setText("");
            gestorVista.getTablaTokens().limpiar();
            gestorVista.getTablaErrores().limpiarTabla();
            gestorVista.getTablaEstadisticas().limpiarTabla();
            gestorVista.getTablaSimbolos().limpiarTabla();
            gestorVista.getTablaSalida().limpiarSalida();
        }
    }

    private void guardarPrompt() {
        String prompt
                = gestorVista
                        .getTxtPromptGenerado()
                        .getText();

        if (prompt.isBlank()) {
            mostrarAdvertencia(
                    "No hay un prompt generado para guardar."
            );
            return;
        }

        JFileChooser selector = new JFileChooser();

        FileNameExtensionFilter filtroMd
                = new FileNameExtensionFilter(
                        "Markdown (*.md)",
                        "md"
                );

        FileNameExtensionFilter filtroTxt
                = new FileNameExtensionFilter(
                        "Texto (*.txt)",
                        "txt"
                );

        selector.setDialogTitle(
                "Guardar prompt generado"
        );

        selector.addChoosableFileFilter(filtroTxt);
        selector.setFileFilter(filtroMd);

        selector.setSelectedFile(
                new File("prompt_generado.md")
        );

        if (selector.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        String extension;

        if (selector.getFileFilter() == filtroTxt) {
            extension = "txt";
        } else {
            extension = "md";
        }

        Path destino = asegurarExtension(
                selector.getSelectedFile().toPath(),
                extension
        );

        try {
            Files.writeString(
                    destino,
                    prompt,
                    StandardCharsets.UTF_8
            );

            gestorVista.actualizarEstado(
                    "Prompt guardado"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Prompt guardado correctamente.",
                    "PromptZal",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            mostrarError(
                    "No se pudo guardar el prompt: "
                    + ex.getMessage()
            );
        }
    }

    private void exportarReporte() {
        String opcion
                = (String) jComboBoxReportes
                        .getSelectedItem();

        if (opcion == null) return;

        if (opcion.equals("AFD General")) {
            exportarAFD();
            return;
        }

        if (!compilacionDisponible) {
            mostrarAdvertencia(
                    "Primero debes compilar el codigo para generar este reporte."
            );
            return;
        }

        boolean generado;
        String ruta;
        String nombre;
        boolean incluyeImagenAST = false;

        switch (opcion) {
            case "Reporte AST" -> {
                GeneradorReporteAST reporte
                        = new GeneradorReporteAST();

                generado = reporte.generar(
                        programaActual
                );

                ruta = reporte.getRutaReporte();
                nombre = "reporte_ast.html";
                incluyeImagenAST = true;
            }

            case "Reporte de Simbolos" -> {
                GeneradorReporteSimbolos reporte
                        = new GeneradorReporteSimbolos();

                generado
                        = semanticoActual != null
                        && reporte.generar(
                                semanticoActual
                                        .getTablaSimbolos()
                        );

                ruta = reporte.getRutaReporte();
                nombre = "tabla_simbolos.html";
            }

            case "Reporte de Errores" -> {
                GenerarReporteErroresHTML reporte
                        = new GenerarReporteErroresHTML();

                generado = reporte.generar(
                        erroresLexicosActuales,
                        erroresSintacticosActuales,
                        erroresSemanticosActuales
                );

                ruta = reporte.getRutaReporte();
                nombre = "reporte_errores.html";
            }

            case "Reporte de Estadisticas" -> {
                GeneradorReporteEstadisticasHTML reporte
                        = new GeneradorReporteEstadisticasHTML();

                generado = reporte.generar(
                        tokensActuales,
                        erroresLexicosActuales,
                        erroresSintacticosActuales,
                        erroresSemanticosActuales,
                        codigoCompilado
                );

                ruta = reporte.getRutaReporte();
                nombre = "reporte_estadisticas.html";
            }

            case "Reporte de Tokens" -> {
                GeneradorReporteTokensHTML reporte
                        = new GeneradorReporteTokensHTML();

                generado = reporte.generar(
                        tokensActuales
                );

                ruta = reporte.getRutaReporte();
                nombre = "reporte_tokens.html";
            }

            default -> {
                return;
            }
        }

        if (!generado) {
            mostrarError(
                    "No se pudo generar el reporte seleccionado."
            );
            return;
        }

        guardarCopiaReporte(
                ruta,
                nombre,
                incluyeImagenAST
        );
    }

    private void guardarCopiaReporte(
            String rutaOrigen,
            String nombreSugerido,
            boolean incluyeImagenAST) {

        JFileChooser selector = new JFileChooser();

        selector.setDialogTitle(
                "Guardar reporte"
        );

        selector.setFileFilter(
                new FileNameExtensionFilter(
                        "Archivo HTML (*.html)",
                        "html"
                )
        );

        selector.setSelectedFile(
                new File(nombreSugerido)
        );

        if (selector.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path origen
                = Path.of(rutaOrigen)
                        .toAbsolutePath()
                        .normalize();

        Path destino
                = asegurarExtension(
                        selector.getSelectedFile().toPath(),
                        "html"
                )
                        .toAbsolutePath()
                        .normalize();

        try {
            copiarSiEsDistinto(
                    origen,
                    destino
            );

            if (incluyeImagenAST) {
                Path imagenOrigen
                        = origen.getParent()
                                .resolve(
                                        "ast_promptzal.png"
                                );

                Path imagenDestino
                        = destino.getParent()
                                .resolve(
                                        "ast_promptzal.png"
                                );

                if (Files.exists(imagenOrigen)) {
                    copiarSiEsDistinto(
                            imagenOrigen,
                            imagenDestino
                    );
                }
            }

            gestorVista.actualizarEstado(
                    "Reporte exportado"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Reporte guardado correctamente.",
                    "PromptZal",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            mostrarError(
                    "No se pudo guardar el reporte: "
                    + ex.getMessage()
            );
        }
    }

    private void exportarAFD() {
        GeneradorAFD generador
                = new GeneradorAFD();

        if (!generador.generar()) {
            mostrarError(
                    "No se pudo generar el AFD. "
                    + "Verifica que Graphviz este instalado."
            );
            return;
        }

        JFileChooser selector
                = new JFileChooser();

        selector.setDialogTitle(
                "Guardar AFD"
        );

        selector.setFileFilter(
                new FileNameExtensionFilter(
                        "Imagen PNG (*.png)",
                        "png"
                )
        );

        selector.setSelectedFile(
                new File("afd_promptzal.png")
        );

        if (selector.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        Path destinoImagen
                = asegurarExtension(
                        selector.getSelectedFile().toPath(),
                        "png"
                )
                        .toAbsolutePath()
                        .normalize();

        String nombre
                = destinoImagen
                        .getFileName()
                        .toString();

        String base
                = nombre.substring(
                        0,
                        nombre.length() - 4
                );

        Path destinoDot
                = destinoImagen.resolveSibling(
                        base + ".dot"
                );

        try {
            copiarSiEsDistinto(
                    Path.of(
                            generador.getRutaImagen()
                    )
                            .toAbsolutePath()
                            .normalize(),
                    destinoImagen
            );

            copiarSiEsDistinto(
                    Path.of(
                            generador.getRutaDot()
                    )
                            .toAbsolutePath()
                            .normalize(),
                    destinoDot
            );

            gestorVista.actualizarEstado(
                    "AFD exportado"
            );

            JOptionPane.showMessageDialog(
                    this,
                    "AFD guardado correctamente.",
                    "PromptZal",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException ex) {
            mostrarError(
                    "No se pudo guardar el AFD: "
                    + ex.getMessage()
            );
        }
    }

    private Path asegurarExtension(
            Path ruta,
            String extension) {

        String nombre
                = ruta.getFileName()
                        .toString();

        if (nombre.toLowerCase()
                .endsWith(
                        "." + extension.toLowerCase()
                )) {

            return ruta;
        }

        return ruta.resolveSibling(
                nombre + "." + extension
        );
    }

    private void copiarSiEsDistinto(
            Path origen,
            Path destino) throws IOException {

        if (destino.getParent() != null) {
            Files.createDirectories(
                    destino.getParent()
            );
        }

        if (!origen.equals(destino)) {
            Files.copy(
                    origen,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
    }

    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "PromptZal",
                JOptionPane.WARNING_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this,
                mensaje,
                "PromptZal",
                JOptionPane.ERROR_MESSAGE
        );
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jButtonNuevo = new javax.swing.JButton();
        jButtonAbrir = new javax.swing.JButton();
        jButtonGuardar = new javax.swing.JButton();
        jButtonCompilar = new javax.swing.JButton();
        jComboBoxReportes = new javax.swing.JComboBox<>();
        jButtonExportar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Bitstream Vera Sans Mono", 3, 18)); // NOI18N
        jLabel1.setText("ARCHIVO");

        jLabel2.setFont(new java.awt.Font("Bitstream Vera Serif", 3, 24)); // NOI18N
        jLabel2.setText("PROMPZAL IDE");

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));

        jButtonNuevo.setBackground(new java.awt.Color(0, 51, 51));
        jButtonNuevo.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNuevo.setText("NUEVO");

        jButtonAbrir.setBackground(new java.awt.Color(0, 51, 51));
        jButtonAbrir.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAbrir.setText("ABRIR");

        jButtonGuardar.setBackground(new java.awt.Color(0, 51, 51));
        jButtonGuardar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGuardar.setText("GUARDAR");
        jButtonGuardar.addActionListener(this::jButtonGuardarActionPerformed);

        jButtonCompilar.setBackground(new java.awt.Color(51, 51, 0));
        jButtonCompilar.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCompilar.setText("COMPILAR");
        jButtonCompilar.addActionListener(this::jButtonCompilarActionPerformed);

        jComboBoxReportes.setBackground(new java.awt.Color(0, 51, 51));
        jComboBoxReportes.setForeground(new java.awt.Color(255, 255, 255));
        jComboBoxReportes.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Exportar ReporteAST", "Exportar ReporteSimbolos", "Exportar ReportesErrores", "Exportar ReportesEstadistica", "Exportar ReportesTokens" }));

        jButtonExportar.setText("EXPORTAR");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButtonNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(78, 78, 78)
                .addComponent(jButtonAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(jButtonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112)
                .addComponent(jButtonCompilar, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72)
                .addComponent(jComboBoxReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButtonExportar)
                .addContainerGap(136, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBoxReportes, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonExportar))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButtonNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonAbrir)
                        .addComponent(jButtonGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonCompilar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(556, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jLabel1)
                .addGap(430, 430, 430)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 906, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGuardarActionPerformed
        guardarArchivo();
    }//GEN-LAST:event_jButtonGuardarActionPerformed

    private void jButtonCompilarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCompilarActionPerformed
  
    }//GEN-LAST:event_jButtonCompilarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new ventanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAbrir;
    private javax.swing.JButton jButtonCompilar;
    private javax.swing.JButton jButtonExportar;
    private javax.swing.JButton jButtonGuardar;
    private javax.swing.JButton jButtonNuevo;
    private javax.swing.JComboBox<String> jComboBoxReportes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    // End of variables declaration//GEN-END:variables
}
