package com.tfgLeilaFraileSimon.ERPImprentaCliente.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ErrorApi;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.CampoConfig;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.EntidadConfig;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.Entidades;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.util.Etiquetas;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.util.Validacion;

/** Equivalente al formulario "generica-tarjeta-formulario" + construirCampoFormulario()
 *  / alEnviarFormularioGenerico() de frontend/js/crud.js: construye el
 *  formulario de crear/editar a partir de la EntidadConfig de la seccion
 *  elegida, en vez de tener un dialogo Swing distinto por cada entidad. */
public class FormularioGenericoDialog extends JDialog {

    // Representa una opcion de un combo de relacion (id + etiqueta legible).
    // id == -1 es el "Sin asignar" de crud.js (payload null para ese campo).
    private record OpcionRelacion(int id, String etiqueta) {
        @Override
        public String toString() {
            return etiqueta;
        }
    }

    private record ResultadoCarga(JsonNode registro, Map<String, List<OpcionRelacion>> opciones) {
    }

    private static final OpcionRelacion SIN_ASIGNAR = new OpcionRelacion(-1, "Sin asignar");

    private final ApiClient api;
    private final EntidadConfig config;
    private final Integer idEdicion; // null = crear
    private final Runnable alGuardar;

    private final JPanel panelCampos = new JPanel();
    private final JLabel labelError = new JLabel(" ");
    private final JButton botonGuardar = new JButton();
    private final Map<String, JComponent> entradas = new LinkedHashMap<>();
    private final Map<String, JLabel> erroresCampo = new LinkedHashMap<>();

    public FormularioGenericoDialog(Frame owner, ApiClient api, EntidadConfig config, Integer idEdicion, Runnable alGuardar) {
        super(owner, idEdicion != null
                ? "Editar registro de " + config.titulo.toLowerCase()
                : "Nuevo registro en " + config.titulo.toLowerCase(), true);
        this.api = api;
        this.config = config;
        this.idEdicion = idEdicion;
        this.alGuardar = alGuardar;
        botonGuardar.setText(idEdicion != null ? "Guardar cambios" : "Crear");
        botonGuardar.setEnabled(false);

        construirUi();
        setSize(480, 620);
        setLocationRelativeTo(owner);
        cargarDatos();
    }

    private void construirUi() {
        setLayout(new BorderLayout());

        panelCampos.setLayout(new BoxLayout(panelCampos, BoxLayout.Y_AXIS));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panelCampos.add(new JLabel("Cargando formulario..."));
        add(new JScrollPane(panelCampos), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        labelError.setForeground(new Color(0xB3261E));
        labelError.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));
        panelInferior.add(labelError, BorderLayout.NORTH);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        botonGuardar.addActionListener(e -> guardar());
        botones.add(botonGuardar);
        botones.add(botonCancelar);
        panelInferior.add(botones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    // Carga en segundo plano el registro a editar (si lo hay) y las listas de
    // todas las relaciones que aparecen en el formulario, para rellenar sus
    // combos (equivalente a obtenerOpcionesRelacion() en crud.js).
    private void cargarDatos() {
        new SwingWorker<ResultadoCarga, Void>() {
            @Override
            protected ResultadoCarga doInBackground() throws ErrorApi {
                JsonNode registro = idEdicion != null ? api.llamar(config.endpoint + "/" + idEdicion) : null;

                Map<String, List<OpcionRelacion>> opciones = new HashMap<>();
                for (CampoConfig campo : config.campos) {
                    if (campo.tipo != TipoCampo.RELACION || opciones.containsKey(campo.entidadRelacion)) {
                        continue;
                    }
                    List<OpcionRelacion> lista = new ArrayList<>();
                    String endpoint = Entidades.endpointDeEntidadRelacion(campo.entidadRelacion);
                    if (endpoint != null) {
                        JsonNode registros = api.llamar(endpoint);
                        if (registros != null) {
                            for (JsonNode r : registros) {
                                lista.add(new OpcionRelacion(r.path("id").asInt(), Etiquetas.de(r)));
                            }
                        }
                    }
                    opciones.put(campo.entidadRelacion, lista);
                }
                return new ResultadoCarga(registro, opciones);
            }

            @Override
            protected void done() {
                try {
                    ResultadoCarga resultado = get();
                    construirFormulario(resultado.registro(), resultado.opciones());
                } catch (Exception e) {
                    Throwable causa = e.getCause() != null ? e.getCause() : e;
                    labelError.setText(causa.getMessage());
                }
            }
        }.execute();
    }

    private void construirFormulario(JsonNode registroExistente, Map<String, List<OpcionRelacion>> opciones) {
        panelCampos.removeAll();
        for (CampoConfig campo : config.campos) {
            JsonNode valorActual = registroExistente != null ? registroExistente.get(campo.nombre) : null;
            panelCampos.add(crearFilaCampo(campo, valorActual, opciones));
        }
        panelCampos.revalidate();
        panelCampos.repaint();
        botonGuardar.setEnabled(true);
    }

    private JPanel crearFilaCampo(CampoConfig campo, JsonNode valorActual, Map<String, List<OpcionRelacion>> opciones) {
        JPanel fila = new JPanel();
        fila.setLayout(new BoxLayout(fila, BoxLayout.Y_AXIS));
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setBorder(BorderFactory.createEmptyBorder(4, 0, 10, 0));

        if (campo.tipo == TipoCampo.BOOLEANO) {
            JCheckBox check = new JCheckBox(campo.etiqueta);
            check.setSelected(valorActual != null && valorActual.asBoolean(false));
            check.setAlignmentX(Component.LEFT_ALIGNMENT);
            entradas.put(campo.nombre, check);
            fila.add(check);
            return fila;
        }

        JLabel etiqueta = new JLabel(campo.etiqueta + (campo.requerido ? " *" : ""));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(etiqueta);

        JComponent widget = crearWidget(campo, valorActual, opciones);
        entradas.put(campo.nombre, widget);

        JComponent visual = widget;
        if (widget instanceof JTextArea area) {
            JScrollPane scroll = new JScrollPane(area);
            scroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
            visual = scroll;
        } else {
            widget.setMaximumSize(new Dimension(Integer.MAX_VALUE, widget.getPreferredSize().height));
        }
        visual.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.add(visual);

        JLabel error = new JLabel(" ");
        error.setForeground(new Color(0xB3261E));
        error.setFont(error.getFont().deriveFont(11f));
        error.setAlignmentX(Component.LEFT_ALIGNMENT);
        erroresCampo.put(campo.nombre, error);
        fila.add(error);

        return fila;
    }

    private JComponent crearWidget(CampoConfig campo, JsonNode valorActual, Map<String, List<OpcionRelacion>> opciones) {
        switch (campo.tipo) {
            case AREA: {
                JTextArea area = new JTextArea(3, 20);
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                if (valorActual != null && !valorActual.isNull()) {
                    area.setText(valorActual.asText());
                }
                return area;
            }
            case SELECT: {
                JComboBox<String> combo = new JComboBox<>();
                combo.addItem("");
                for (String opcion : campo.opciones) {
                    combo.addItem(opcion);
                }
                if (valorActual != null && !valorActual.isNull()) {
                    combo.setSelectedItem(valorActual.asText());
                }
                return combo;
            }
            case RELACION: {
                JComboBox<OpcionRelacion> combo = new JComboBox<>();
                combo.addItem(SIN_ASIGNAR);
                for (OpcionRelacion opcion : opciones.getOrDefault(campo.entidadRelacion, List.of())) {
                    combo.addItem(opcion);
                }
                if (valorActual != null && !valorActual.isNull()) {
                    int idSeleccionado = valorActual.path("id").asInt(-1);
                    for (int i = 0; i < combo.getItemCount(); i++) {
                        if (combo.getItemAt(i).id() == idSeleccionado) {
                            combo.setSelectedIndex(i);
                            break;
                        }
                    }
                }
                return combo;
            }
            case FECHA: {
                JTextField campoFecha = new JTextField();
                campoFecha.setToolTipText("Formato: aaaa-mm-dd");
                if (valorActual != null && !valorActual.isNull()) {
                    campoFecha.setText(valorActual.asText());
                }
                return campoFecha;
            }
            default: { // TEXTO, NUMERO, DECIMAL
                JTextField campoTexto = new JTextField();
                if (valorActual != null && !valorActual.isNull()) {
                    campoTexto.setText(valorActual.asText());
                }
                return campoTexto;
            }
        }
    }

    // Valor tal cual esta en el campo, para pasarselo a Validacion.validarCampoGenerico
    // (equivalente a "valorCrudo" en alEnviarFormularioGenerico() de crud.js).
    private String valorCrudoDe(CampoConfig campo) {
        JComponent widget = entradas.get(campo.nombre);
        return switch (campo.tipo) {
            case AREA -> ((JTextArea) widget).getText();
            case SELECT -> {
                String seleccionado = (String) ((JComboBox<?>) widget).getSelectedItem();
                yield seleccionado == null ? "" : seleccionado;
            }
            case RELACION -> {
                OpcionRelacion seleccionado = (OpcionRelacion) ((JComboBox<?>) widget).getSelectedItem();
                yield (seleccionado == null || seleccionado.id() == -1) ? "" : String.valueOf(seleccionado.id());
            }
            case BOOLEANO -> null; // Validacion ignora este tipo, nunca se llama para checkboxes
            default -> ((JTextField) widget).getText();
        };
    }

    private void guardar() {
        labelError.setText(" ");
        boolean hayErrores = false;
        JComponent primerInvalido = null;

        for (CampoConfig campo : config.campos) {
            String mensaje = Validacion.validarCampoGenerico(campo, valorCrudoDe(campo));
            JLabel labelErrorCampo = erroresCampo.get(campo.nombre);
            if (labelErrorCampo != null) {
                labelErrorCampo.setText(mensaje == null ? " " : mensaje);
            }
            if (mensaje != null) {
                hayErrores = true;
                if (primerInvalido == null) {
                    primerInvalido = entradas.get(campo.nombre);
                }
            }
        }

        if (hayErrores) {
            labelError.setText("Revisa los campos marcados en rojo.");
            if (primerInvalido != null) {
                primerInvalido.requestFocusInWindow();
            }
            return;
        }

        ObjectMapper mapper = api.getMapper();
        ObjectNode payload = mapper.createObjectNode();
        for (CampoConfig campo : config.campos) {
            construirValorPayload(payload, mapper, campo);
        }

        botonGuardar.setEnabled(false);
        String ruta = idEdicion != null ? config.endpoint + "/" + idEdicion : config.endpoint;
        String metodo = idEdicion != null ? "PUT" : "POST";

        new SwingWorker<JsonNode, Void>() {
            @Override
            protected JsonNode doInBackground() throws ErrorApi {
                return api.llamar(ruta, metodo, payload);
            }

            @Override
            protected void done() {
                botonGuardar.setEnabled(true);
                try {
                    get();
                    alGuardar.run();
                    dispose();
                } catch (Exception e) {
                    Throwable causa = e.getCause() != null ? e.getCause() : e;
                    labelError.setText(causa.getMessage());
                }
            }
        }.execute();
    }

    private void construirValorPayload(ObjectNode payload, ObjectMapper mapper, CampoConfig campo) {
        JComponent widget = entradas.get(campo.nombre);
        switch (campo.tipo) {
            case BOOLEANO:
                payload.put(campo.nombre, ((JCheckBox) widget).isSelected());
                break;
            case RELACION: {
                OpcionRelacion seleccionado = (OpcionRelacion) ((JComboBox<?>) widget).getSelectedItem();
                if (seleccionado == null || seleccionado.id() == -1) {
                    payload.putNull(campo.nombre);
                } else {
                    payload.set(campo.nombre, mapper.createObjectNode().put("id", seleccionado.id()));
                }
                break;
            }
            case NUMERO: {
                String texto = ((JTextField) widget).getText().trim();
                if (texto.isEmpty()) {
                    payload.putNull(campo.nombre);
                } else {
                    payload.put(campo.nombre, (int) Math.round(Double.parseDouble(texto.replace(',', '.'))));
                }
                break;
            }
            case DECIMAL: {
                String texto = ((JTextField) widget).getText().trim();
                if (texto.isEmpty()) {
                    payload.putNull(campo.nombre);
                } else {
                    payload.put(campo.nombre, new BigDecimal(texto.replace(',', '.')));
                }
                break;
            }
            case FECHA: {
                String texto = ((JTextField) widget).getText().trim();
                if (texto.isEmpty()) {
                    payload.putNull(campo.nombre);
                } else {
                    payload.put(campo.nombre, texto);
                }
                break;
            }
            case SELECT: {
                String seleccionado = (String) ((JComboBox<?>) widget).getSelectedItem();
                if (seleccionado == null || seleccionado.isEmpty()) {
                    payload.putNull(campo.nombre);
                } else {
                    payload.put(campo.nombre, seleccionado);
                }
                break;
            }
            default: { // TEXTO, AREA
                String texto = widget instanceof JTextArea area ? area.getText().trim() : ((JTextField) widget).getText().trim();
                if (texto.isEmpty()) {
                    payload.putNull(campo.nombre);
                } else {
                    payload.put(campo.nombre, texto);
                }
                break;
            }
        }
    }
}
