package com.tfgLeilaFraileSimon.ERPImprentaCliente.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ErrorApi;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.CampoConfig;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.EntidadConfig;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.util.Etiquetas;

/** Equivalente a "vista-generica" de frontend/index.html + el motor generico
 *  de frontend/js/crud.js: una unica pantalla (tabla + toolbar) que se
 *  reconstruye segun la EntidadConfig de la seccion elegida en la barra
 *  lateral, en vez de tener una clase Swing por cada una de las 20 entidades. */
public class GenericoPanel extends JPanel {

    private final ApiClient api;

    private final JLabel titulo = new JLabel();
    private final JLabel subtitulo = new JLabel();
    private final JLabel labelError = new JLabel(" ");
    private final DefaultTableModel modeloTabla = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int fila, int columna) {
            return false;
        }
    };
    private final JTable tabla = new JTable(modeloTabla);
    private final JButton botonNuevo = new JButton("+ Nuevo");
    private final JButton botonEditar = new JButton("Editar");
    private final JButton botonEliminar = new JButton("Eliminar");

    private EntidadConfig configActual;
    private JsonNode registrosActuales;

    public GenericoPanel(ApiClient api) {
        this.api = api;
        setLayout(new BorderLayout(0, 8));

        JPanel cabecera = new JPanel(new BorderLayout());
        JPanel textos = new JPanel();
        textos.setLayout(new javax.swing.BoxLayout(textos, javax.swing.BoxLayout.Y_AXIS));
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
        subtitulo.setForeground(new Color(0x6B7280));
        textos.add(titulo);
        textos.add(subtitulo);
        cabecera.add(textos, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botonEditar.setEnabled(false);
        botonEliminar.setEnabled(false);
        botonNuevo.addActionListener(e -> abrirFormulario(null));
        botonEditar.addActionListener(e -> editarSeleccionado());
        botonEliminar.addActionListener(e -> eliminarSeleccionado());
        botones.add(botonEditar);
        botones.add(botonEliminar);
        botones.add(botonNuevo);
        cabecera.add(botones, BorderLayout.EAST);
        add(cabecera, BorderLayout.NORTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            boolean haySeleccion = tabla.getSelectedRow() >= 0;
            botonEditar.setEnabled(haySeleccion);
            botonEliminar.setEnabled(haySeleccion);
        });
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editarSeleccionado();
                }
            }
        });
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        labelError.setForeground(new Color(0xB3261E));
        add(labelError, BorderLayout.SOUTH);
    }

    public void mostrar(EntidadConfig config) {
        this.configActual = config;
        titulo.setText(config.titulo);
        subtitulo.setText(config.subtitulo == null ? "" : config.subtitulo);
        cargarListado();
    }

    private void cargarListado() {
        labelError.setText(" ");
        EntidadConfig config = configActual;

        new SwingWorker<JsonNode, Void>() {
            @Override
            protected JsonNode doInBackground() throws ErrorApi {
                return api.llamar(config.endpoint);
            }

            @Override
            protected void done() {
                if (config != configActual) {
                    return; // el usuario ya ha cambiado de seccion mientras se cargaba
                }
                try {
                    registrosActuales = get();
                    pintarTabla(config);
                } catch (Exception e) {
                    mostrarError(e);
                }
            }
        }.execute();
    }

    private void pintarTabla(EntidadConfig config) {
        String[] columnas = config.campos.stream().map(c -> c.etiqueta).toArray(String[]::new);
        modeloTabla.setDataVector(new Object[0][columnas.length], columnas);

        if (registrosActuales == null) {
            return;
        }
        for (JsonNode registro : registrosActuales) {
            Object[] fila = new Object[config.campos.size()];
            for (int i = 0; i < config.campos.size(); i++) {
                fila[i] = valorParaTabla(config.campos.get(i), registro);
            }
            modeloTabla.addRow(fila);
        }
    }

    private String valorParaTabla(CampoConfig campo, JsonNode registro) {
        JsonNode valor = registro.get(campo.nombre);
        if (campo.tipo == TipoCampo.RELACION) {
            return valor == null || valor.isNull() ? "—" : Etiquetas.de(valor);
        }
        if (campo.tipo == TipoCampo.BOOLEANO) {
            return valor != null && valor.asBoolean(false) ? "Sí" : "No";
        }
        if (valor == null || valor.isNull() || valor.asText().isEmpty()) {
            return "—";
        }
        return valor.asText();
    }

    private void abrirFormulario(Integer idEdicion) {
        new FormularioGenericoDialog((java.awt.Frame) SwingUtilities.getWindowAncestor(this), api, configActual,
                idEdicion, this::cargarListado).setVisible(true);
    }

    private void editarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        abrirFormulario(registrosActuales.get(fila).path("id").asInt());
    }

    private void eliminarSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar este registro?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }
        int id = registrosActuales.get(fila).path("id").asInt();
        EntidadConfig config = configActual;

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws ErrorApi {
                api.llamar(config.endpoint + "/" + id, "DELETE", null);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    cargarListado();
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
