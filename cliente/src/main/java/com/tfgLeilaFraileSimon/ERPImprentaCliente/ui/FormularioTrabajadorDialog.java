package com.tfgLeilaFraileSimon.ERPImprentaCliente.ui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ErrorApi;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.util.Validacion;

/** Equivalente al formulario "tarjeta-alta" de frontend/index.html + alEnviarAlta() en app.js. */
public class FormularioTrabajadorDialog extends JDialog {

    private final ApiClient api;
    private final Runnable alGuardar;

    private final JTextField campoNombre = new JTextField(20);
    private final JTextField campoEmail = new JTextField(20);
    private final JPasswordField campoPassword = new JPasswordField(20);
    private final JComboBox<String> campoRol = new JComboBox<>(
            new String[]{"ADMIN", "COMERCIAL", "ENCARGADO_TALLER", "OPERARIO", "TRANSPORTISTA"});
    private final JLabel labelError = new JLabel(" ");
    private final JButton botonCrear = new JButton("Crear");

    public FormularioTrabajadorDialog(Frame owner, ApiClient api, Runnable alGuardar) {
        super(owner, "Nuevo trabajador", true);
        this.api = api;
        this.alGuardar = alGuardar;
        campoRol.setSelectedItem("OPERARIO");
        construirUi();
        pack();
        setLocationRelativeTo(owner);
    }

    private void construirUi() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        panel.add(new JLabel("Nombre completo"), c);
        c.gridy++;
        panel.add(campoNombre, c);

        c.gridy++;
        panel.add(new JLabel("Email"), c);
        c.gridy++;
        panel.add(campoEmail, c);

        c.gridy++;
        panel.add(new JLabel("Contraseña"), c);
        c.gridy++;
        panel.add(campoPassword, c);

        c.gridy++;
        panel.add(new JLabel("Rol"), c);
        c.gridy++;
        panel.add(campoRol, c);

        c.gridy++;
        labelError.setForeground(new Color(0xB3261E));
        panel.add(labelError, c);

        c.gridy++;
        JPanel botones = new JPanel();
        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        botonCrear.addActionListener(e -> guardar());
        botones.add(botonCrear);
        botones.add(botonCancelar);
        panel.add(botones, c);

        setContentPane(panel);
    }

    private void guardar() {
        String nombre = campoNombre.getText().trim();
        String email = campoEmail.getText().trim();
        String password = new String(campoPassword.getPassword());
        String rol = (String) campoRol.getSelectedItem();
        labelError.setText(" ");

        if (nombre.isEmpty()) {
            labelError.setText("El nombre es obligatorio.");
            return;
        }
        if (email.isEmpty()) {
            labelError.setText("El email es obligatorio.");
            return;
        }
        if (!Validacion.esEmailValido(email)) {
            labelError.setText("El email no tiene un formato válido.");
            return;
        }
        if (password.isEmpty()) {
            labelError.setText("La contraseña es obligatoria.");
            return;
        }
        if (password.length() < 8) {
            labelError.setText("La contraseña debe tener al menos 8 caracteres.");
            return;
        }

        var mapper = api.getMapper();
        var payload = mapper.createObjectNode();
        payload.put("nombreCompleto", nombre);
        payload.put("email", email);
        payload.put("password", password);
        payload.put("rol", rol);
        payload.put("activo", true);

        botonCrear.setEnabled(false);
        new SwingWorker<JsonNode, Void>() {
            @Override
            protected JsonNode doInBackground() throws ErrorApi {
                return api.llamar("/trabajador", "POST", payload);
            }

            @Override
            protected void done() {
                botonCrear.setEnabled(true);
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
}
