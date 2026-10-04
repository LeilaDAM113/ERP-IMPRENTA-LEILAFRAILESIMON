package com.tfgLeilaFraileSimon.ERPImprentaCliente.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ErrorApi;

/** Equivalente a "vista-trabajadores" de frontend/index.html + la seccion de
 *  trabajadores en app.js: pantalla propia porque la contrasena y el rol
 *  necesitan logica especial que no encaja en el motor generico. */
public class TrabajadoresPanel extends JPanel {

    private final ApiClient api;
    private final boolean esAdmin;

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"Nombre", "Email", "Rol", "Activo"}, 0) {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel labelError = new JLabel(" ");
    private final JButton botonNuevo = new JButton("+ Nuevo trabajador");
    private final JButton botonEliminar = new JButton("Eliminar");

    private JsonNode trabajadoresActuales;

    public TrabajadoresPanel(ApiClient api, JsonNode usuario) {
        this.api = api;
        this.esAdmin = "ADMIN".equals(usuario.path("rol").asText());

        setLayout(new BorderLayout(0, 8));

        JPanel cabecera = new JPanel(new BorderLayout());
        JLabel titulo = new JLabel("Trabajadores");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        cabecera.add(titulo, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonEliminar.setEnabled(false);
        botonEliminar.addActionListener(e -> eliminarSeleccionado());
        botonNuevo.addActionListener(e -> new FormularioTrabajadorDialog(
                (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this), api, this::cargar).setVisible(true));
        if (esAdmin) {
            botones.add(botonEliminar);
            botones.add(botonNuevo);
        }
        cabecera.add(botones, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        tabla.getSelectionModel().addListSelectionListener(e -> botonEliminar.setEnabled(esAdmin && tabla.getSelectedRow() >= 0));
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        labelError.setForeground(new Color(0xB3261E));
        add(labelError, BorderLayout.SOUTH);
    }

    public void cargar() {
        labelError.setText(" ");
        new SwingWorker<JsonNode, Void>() {
            @Override
            protected JsonNode doInBackground() throws ErrorApi {
                return api.llamar("/trabajador");
            }

            @Override
            protected void done() {
                try {
                    trabajadoresActuales = get();
                    pintarTabla();
                } catch (Exception e) {
                    mostrarError(e);
                }
            }
        }.execute();
    }

    private void pintarTabla() {
        modeloTabla.setRowCount(0);
        if (trabajadoresActuales == null) {
            return;
        }
        for (JsonNode trabajador : trabajadoresActuales) {
            modeloTabla.addRow(new Object[]{
                    trabajador.path("nombreCompleto").asText(""),
                    trabajador.path("email").asText(""),
                    trabajador.path("rol").asText(""),
                    trabajador.path("activo").asBoolean(false) ? "Sí" : "No",
            });
        }
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este trabajador?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        int id = trabajadoresActuales.get(fila).path("id").asInt();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws ErrorApi {
                api.llamar("/trabajador/" + id, "DELETE", null);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    cargar();
                } catch (Exception e) {
                    mostrarError(e);
                }
            }
        }.execute();
    }

    private void mostrarError(Exception e) {
        Throwable causa = e.getCause() != null ? e.getCause() : e;
        labelError.setText(causa.getMessage());
    }
}
