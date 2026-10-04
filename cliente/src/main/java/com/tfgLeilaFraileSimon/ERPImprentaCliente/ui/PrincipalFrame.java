package com.tfgLeilaFraileSimon.ERPImprentaCliente.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.EntidadConfig;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.Entidades;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.GrupoNav;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.ItemNav;

/** Equivalente a la vista "app" (barra lateral + cabecera + contenido) de
 *  frontend/index.html, con la navegacion de app.js (seleccionarSeccion). */
public class PrincipalFrame extends JFrame {

    private final ApiClient api;
    private final JsonNode usuario;

    private final JLabel tituloSeccion = new JLabel("Panel");
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelCentral = new JPanel(cardLayout);
    private final Map<String, JButton> botonesNav = new LinkedHashMap<>();

    private final DashboardPanel dashboardPanel;
    private final TrabajadoresPanel trabajadoresPanel;
    private final GenericoPanel genericoPanel;

    public PrincipalFrame(ApiClient api, JsonNode usuario) {
        super("ERP Imprenta");
        this.api = api;
        this.usuario = usuario;

        this.dashboardPanel = new DashboardPanel(api, usuario, this::seleccionarSeccion);
        this.trabajadoresPanel = new TrabajadoresPanel(api, usuario);
        this.genericoPanel = new GenericoPanel(api);

        construirUi();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        seleccionarSeccion("dashboard");
    }

    private void construirUi() {
        setLayout(new BorderLayout());
        add(construirBarraLateral(), BorderLayout.WEST);
        add(construirCabecera(), BorderLayout.NORTH);

        panelCentral.add(dashboardPanel, "dashboard");
        panelCentral.add(trabajadoresPanel, "trabajador");
        panelCentral.add(genericoPanel, "generico");
        panelCentral.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel construirBarraLateral() {
        JPanel barra = new JPanel();
        barra.setLayout(new BoxLayout(barra, BoxLayout.Y_AXIS));
        barra.setBackground(new Color(0x1F2937));
        barra.setPreferredSize(new Dimension(220, 0));
        barra.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        JLabel marca = new JLabel("ERP Imprenta");
        marca.setForeground(Color.WHITE);
        marca.setFont(marca.getFont().deriveFont(Font.BOLD, 16f));
        marca.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        marca.setBorder(BorderFactory.createEmptyBorder(0, 4, 16, 0));
        barra.add(marca);

        for (GrupoNav grupo : Entidades.GRUPOS_NAV) {
            JLabel tituloGrupo = new JLabel(grupo.titulo().toUpperCase());
            tituloGrupo.setForeground(new Color(0x9CA3AF));
            tituloGrupo.setFont(tituloGrupo.getFont().deriveFont(Font.BOLD, 11f));
            tituloGrupo.setBorder(BorderFactory.createEmptyBorder(12, 4, 4, 0));
            tituloGrupo.setAlignmentX(JLabel.LEFT_ALIGNMENT);
            barra.add(tituloGrupo);

            for (ItemNav item : grupo.items()) {
                JButton boton = new JButton(item.etiqueta());
                boton.setAlignmentX(JLabel.LEFT_ALIGNMENT);
                boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
                boton.setHorizontalAlignment(SwingConstants.LEFT);
                boton.setFocusPainted(false);
                boton.setBorderPainted(false);
                boton.setContentAreaFilled(false);
                boton.setForeground(Color.WHITE);
                boton.addActionListener(e -> seleccionarSeccion(item.clave()));
                botonesNav.put(item.clave(), boton);
                barra.add(boton);
            }
        }

        barra.add(Box.createVerticalGlue());
        return barra;
    }

    private JPanel construirCabecera() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE5E7EB)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));

        tituloSeccion.setFont(tituloSeccion.getFont().deriveFont(Font.BOLD, 18f));
        cabecera.add(tituloSeccion, BorderLayout.WEST);

        JPanel usuarioPanel = new JPanel();
        usuarioPanel.setLayout(new BoxLayout(usuarioPanel, BoxLayout.X_AXIS));

        JLabel nombre = new JLabel(usuario.path("nombreCompleto").asText());
        nombre.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));

        JLabel chipRol = new JLabel(usuario.path("rol").asText());
        chipRol.setOpaque(true);
        chipRol.setBackground(new Color(0xE0E7FF));
        chipRol.setForeground(new Color(0x3730A3));
        chipRol.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        chipRol.setFont(chipRol.getFont().deriveFont(Font.BOLD, 11f));

        JButton botonLogout = new JButton("Cerrar sesión");
        botonLogout.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        botonLogout.addActionListener(e -> cerrarSesion());

        usuarioPanel.add(nombre);
        usuarioPanel.add(chipRol);
        usuarioPanel.add(Box.createHorizontalStrut(12));
        usuarioPanel.add(botonLogout);
        cabecera.add(usuarioPanel, BorderLayout.EAST);

        return cabecera;
    }

    public void seleccionarSeccion(String clave) {
        botonesNav.forEach((c, boton) -> boton.setFont(boton.getFont().deriveFont(c.equals(clave) ? Font.BOLD : Font.PLAIN)));

        ItemNav item = Entidades.buscarItemNav(clave);
        tituloSeccion.setText(item != null ? item.etiqueta() : "Panel");

        if ("dashboard".equals(clave)) {
            cardLayout.show(panelCentral, "dashboard");
            dashboardPanel.cargar();
        } else if ("trabajador".equals(clave)) {
            cardLayout.show(panelCentral, "trabajador");
            trabajadoresPanel.cargar();
        } else {
            EntidadConfig config = Entidades.ENTIDADES.get(clave);
            genericoPanel.mostrar(config);
            cardLayout.show(panelCentral, "generico");
        }
    }

    private void cerrarSesion() {
        api.borrarCredenciales();
        new LoginFrame(api).setVisible(true);
        dispose();
    }
}
