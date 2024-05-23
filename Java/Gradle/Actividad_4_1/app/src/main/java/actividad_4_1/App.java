package actividad_4_1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class App {
    public static String url = "jdbc:mysql://localhost:3306/pfinal";
    public static Dimension tamanyoPantalla = Toolkit. getDefaultToolkit(). getScreenSize();
    /**
     * Metodo principal con el que simplemente se aplica el tema y se inicia la ventana de vinos
     * @param args
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        inicializarVentanaVinos();
    }

    /**
     * Metodo que inicializa la ventana de vinos
     */
    public static void inicializarVentanaVinos() {
        JFrame ventana = new JFrame("Vinos");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBounds((int)tamanyoPantalla.getWidth()/2 - 300, (int)tamanyoPantalla.getHeight()/2 - 200, 600, 400);
        ventana.setLayout(new BorderLayout());

        // Selector
        JPanel selectorPanel = new JPanel(new GridBagLayout());
        String[] opciones = {"Todos", "Tintos", "Blancos"};
        JComboBox<String> selector = new JComboBox<>(opciones);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        selectorPanel.add(selector, gbc);
        ventana.add(selectorPanel, BorderLayout.NORTH);

        // Tabla
        JPanel tablaPanel = new JPanel(new BorderLayout());
        DefaultTableModel modeloTabla = new DefaultTableModel();
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Anyos");
        modeloTabla.addColumn("Tipo de uva");
        modeloTabla.addColumn("Bodega");
        modeloTabla.addColumn("Calificación");
        JScrollPane scrollPane = new JScrollPane(tabla);
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        ventana.add(tablaPanel, BorderLayout.CENTER);

        // Botones
        JPanel botonesPanel = new JPanel();
        JButton botonAnyadirVino = new JButton("Anyadir vino");
        JButton botonTop10 = new JButton("Ver top 10");
        JButton botonBodegas = new JButton("Bodegas");
        botonesPanel.add(botonAnyadirVino);
        botonesPanel.add(botonTop10);
        botonesPanel.add(botonBodegas);
        ventana.add(botonesPanel, BorderLayout.SOUTH);

        obtenerVinos(modeloTabla, ventana, null);

        // Eventos
        selector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<?> combo = (JComboBox<?>) e.getSource();
                String tipoUva = (String) combo.getSelectedItem();
                obtenerVinos(modeloTabla, ventana, tipoUva);
            }
        });

        botonAnyadirVino.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inicializarVentanaAnyadirVino(ventana, modeloTabla, selector.getSelectedItem().toString());
            }
        });

        botonTop10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
                inicializarVentanaTop10();
            }
        });

        botonBodegas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
                inicializarVentanaBodegas();
            }
        });
    }

    /**
     * Metodo que limpia la tabla, obtiene los vinos de la base de datos y los anyade a la tabla
     * @param modeloTabla
     * @param ventana
     * @param tipoUva
     */
    public static void obtenerVinos(DefaultTableModel modeloTabla, JFrame ventana, String tipoUva) {
        modeloTabla.setRowCount(0);
        try (Connection conexion = DriverManager.getConnection(url, "root", "")) {
            String queryVinos = "SELECT V.id, V.nombre, V.anyos, U.tipo AS tipo, B.nombre AS nombre_bodega, V.calificacion FROM vino V JOIN uva U ON V.id_uva = U.id JOIN bodega B ON V.id_bodega = B.id";;
            if (tipoUva == "Tintos") {
                queryVinos = "SELECT V.id, V.nombre, V.anyos, U.tipo AS tipo, B.nombre AS nombre_bodega, V.calificacion FROM vino V JOIN uva U ON V.id_uva = U.id JOIN bodega B ON V.id_bodega = B.id WHERE U.tipo = 'Tinto'";
            } else if (tipoUva == "Blancos") {
                queryVinos = "SELECT V.id, V.nombre, V.anyos, U.tipo AS tipo, B.nombre AS nombre_bodega, V.calificacion FROM vino V JOIN uva U ON V.id_uva = U.id JOIN bodega B ON V.id_bodega = B.id WHERE U.tipo = 'Blanco'";
            }
            PreparedStatement statementTodos = conexion.prepareStatement(queryVinos);
            ResultSet rsVinos = statementTodos.executeQuery();
            while (rsVinos.next()) {
                modeloTabla.addRow(new Object[]{
                    rsVinos.getString("nombre"),
                    rsVinos.getString("anyos"),
                    rsVinos.getString("tipo"),
                    rsVinos.getString("nombre_bodega"),
                    rsVinos.getString("calificacion"),
                });
            }

            ventana.setVisible(true);
        } catch (Exception e) {
            System.out.println("Terrible... \n" + e);
        }
    }

    /**
     * Metodo que inicializa la ventana de anyadir vino
     */
    public static void inicializarVentanaAnyadirVino(JFrame ventanaPrincipal, DefaultTableModel modeloTabla, String tipoUva) {
        JDialog ventana = new JDialog(ventanaPrincipal, "Anyadir vino", true);
        ventana.setBounds((int)tamanyoPantalla.getWidth()/2 - 200, (int)tamanyoPantalla.getHeight()/2 - 150, 400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Etiquetas y campos
        JLabel etiquetaCampoNombre = new JLabel("Nombre:");
        JTextField campoTextoNombre = new JTextField();
        JLabel etiquetaCampoUva = new JLabel("Tipo de uva:");
        JComboBox<String> selectorUva = new JComboBox<>(new String[] {});
        JLabel etiquetaCampoAnyos = new JLabel("Anyos:");
        JTextField campoTextoAnyos = new JTextField();
        JLabel etiquetaCampoBodega = new JLabel("Bodega:");
        JComboBox<String> selectorBodega = new JComboBox<>(new String[] {});
        JLabel etiquetaCampoCalificacion = new JLabel("Calificación:");
        JTextField campoTextoCalificacion = new JTextField();

        Dimension tamanyoCampo = new Dimension(180, 20);
        campoTextoNombre.setPreferredSize(tamanyoCampo);
        selectorUva.setPreferredSize(tamanyoCampo);
        campoTextoAnyos.setPreferredSize(tamanyoCampo);
        selectorBodega.setPreferredSize(tamanyoCampo);
        campoTextoCalificacion.setPreferredSize(tamanyoCampo);

        // Primer fila
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(etiquetaCampoNombre, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(campoTextoNombre, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(etiquetaCampoUva, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(selectorUva, gbc);

        // Segunda fila
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(etiquetaCampoAnyos, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(campoTextoAnyos, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(etiquetaCampoBodega, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(selectorBodega, gbc);

        // Tercera fila
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(etiquetaCampoCalificacion, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(campoTextoCalificacion, gbc);

        // Botones
        JButton botonAceptar = new JButton("Aceptar");
        JButton botonCancelar = new JButton("Cancelar");
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(botonAceptar);
        panelBotones.add(botonCancelar);
        
        ventana.add(panel, BorderLayout.CENTER);
        ventana.add(panelBotones, BorderLayout.SOUTH);

        // Obtener tipos de uva y bodegas
        try (Connection conexion = DriverManager.getConnection(url, "root", "")) {
            String queryUvas = "SELECT CONCAT(id, ' - ' , nombre, ' - ', tipo) AS uva FROM uva";
            PreparedStatement statementUvas = conexion.prepareStatement(queryUvas);
            ResultSet rsUvas = statementUvas.executeQuery();
            while (rsUvas.next()) {
                selectorUva.addItem(rsUvas.getString("uva"));
            }

            String queryBodegas = "SELECT CONCAT(id, ' - ' , nombre) AS bodega FROM bodega";
            PreparedStatement statementBodegas = conexion.prepareStatement(queryBodegas);
            ResultSet rsBodegas = statementBodegas.executeQuery();
            while (rsBodegas.next()) {
                selectorBodega.addItem(rsBodegas.getString("bodega"));
            }
        } catch (Exception e) {
            System.out.println("Terrible... \n" + e);
        }

        // Guardar vino
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (Connection conexion = DriverManager.getConnection(url, "root", "")) {
                    String[] uva = selectorUva.getSelectedItem().toString().split(" - ");
                    String[] bodega = selectorBodega.getSelectedItem().toString().split(" - ");
                    String query = "INSERT INTO vino (nombre, anyos, id_uva, id_bodega, calificacion) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement statement = conexion.prepareStatement(query);
                    statement.setString(1, campoTextoNombre.getText());
                    statement.setInt(2, Integer.parseInt(campoTextoAnyos.getText()));
                    statement.setInt(3, Integer.parseInt(uva[0]));
                    statement.setInt(4, Integer.parseInt(bodega[0]));
                    statement.setInt(5, Integer.parseInt(campoTextoCalificacion.getText()));
                    statement.executeUpdate();
                    ventana.setVisible(false);
                    obtenerVinos(modeloTabla, ventanaPrincipal, tipoUva);
                } catch (Exception ex) {
                    System.out.println("Terrible... \n" + ex);
                }
            }
        });
        
        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
            }
        });

        ventana.setVisible(true);
    }

    /**
     * Metodo que inicializa la ventana de bodegas
     */
    public static void inicializarVentanaBodegas() {
        JFrame ventana = new JFrame("Bodegas");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBounds((int)tamanyoPantalla.getWidth()/2 - 300, (int)tamanyoPantalla.getHeight()/2 - 200, 600, 400);
        ventana.setLayout(new BorderLayout());

        // Tabla
        JPanel tablaPanel = new JPanel(new BorderLayout());
        DefaultTableModel modeloTabla = new DefaultTableModel();
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("País");
        modeloTabla.addColumn("Enólogo");
        JScrollPane scrollPane = new JScrollPane(tabla);
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        ventana.add(tablaPanel, BorderLayout.CENTER);

        // Botones
        JPanel botonPanel = new JPanel();
        JButton botonVinos = new JButton("Volver a vinos");
        botonPanel.add(botonVinos);
        ventana.add(botonPanel, BorderLayout.SOUTH);

        try (Connection conexion = DriverManager.getConnection(url, "root", "")) {
            String queryBodegas = "SELECT b.nombre, b.pais, e.nombre AS nombre_e, e.apellido1, e.apellido2 " +
                                  "FROM bodega b " +
                                  "JOIN enologo e ON b.id_enologo = e.id";
            PreparedStatement statementBodegas = conexion.prepareStatement(queryBodegas);
            ResultSet rsBodegas = statementBodegas.executeQuery();
            while (rsBodegas.next()) { 
                modeloTabla.addRow(new Object[]{
                    rsBodegas.getString("nombre"),
                    rsBodegas.getString("pais"),
                    rsBodegas.getString("nombre_e") + " " + rsBodegas.getString("apellido1") + " " + rsBodegas.getString("apellido2"),
                });
            }

            ventana.setVisible(true);
        } catch (Exception e) {
            System.out.println("Terrible... \n" + e);
        }

        botonVinos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
                inicializarVentanaVinos();
            }
        });

        ventana.setVisible(true);
    }

    /**
     * Metodo que inicializa la ventana de top 10
     */
    public static void inicializarVentanaTop10() {
        JFrame ventana = new JFrame("Top 10 Vinos");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setBounds((int)tamanyoPantalla.getWidth()/2 - 300, (int)tamanyoPantalla.getHeight()/2 - 200, 600, 400);
        ventana.setLayout(new BorderLayout());

        // Tabla
        JPanel tablaPanel = new JPanel(new BorderLayout());
        DefaultTableModel modeloTabla = new DefaultTableModel();
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Anyos");
        modeloTabla.addColumn("Tipo de uva");
        modeloTabla.addColumn("Bodega");
        modeloTabla.addColumn("Calificación");
        JScrollPane scrollPane = new JScrollPane(tabla);
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        ventana.add(tablaPanel, BorderLayout.CENTER);

        // Botones
        JPanel botonPanel = new JPanel();
        JButton botonVinos = new JButton("Volver a vinos");
        botonPanel.add(botonVinos);
        ventana.add(botonPanel, BorderLayout.SOUTH);

        try (Connection conexion = DriverManager.getConnection(url, "root", "")) {
            String queryTop10 = "SELECT v.nombre, v.anyos, u.tipo AS tipo, b.nombre AS nombre_b, v.calificacion " +
                                "FROM vino v " +
                                "JOIN bodega b ON v.id_bodega = b.id " +
                                "JOIN uva u ON v.id_uva = u.id " +
                                "ORDER BY v.calificacion DESC " +
                                "LIMIT 10";
            PreparedStatement statementTop10 = conexion.prepareStatement(queryTop10);
            ResultSet rsTop10 = statementTop10.executeQuery();
            while (rsTop10.next()) {
                modeloTabla.addRow(new Object[]{
                    rsTop10.getString("nombre"),
                    rsTop10.getString("anyos"),
                    rsTop10.getString("tipo"),
                    rsTop10.getString("nombre_b"),
                    rsTop10.getString("calificacion"),
                });
            }

            ventana.setVisible(true);
        } catch (Exception e) {
            System.out.println("Terrible... \n" + e);
        }

        botonVinos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ventana.setVisible(false);
                inicializarVentanaVinos();
            }
        });
    }
}
