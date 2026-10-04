package com.tfgLeilaFraileSimon.ERPImprentaCliente.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ErrorApi;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.util.Validacion;

/** Equivalente a la vista "login" de frontend/index.html + app.js. */
public class LoginFrame extends JFrame {

    private final ApiClient api;
    private final JTextField campoEmail = new JTextField(22);
    private final JPasswordField campoPassword = new JPasswordField(22);
    private final JLabel labelError = new JLabel(" ");
    private final JButton botonEntrar = new JButton("Entrar");

    public LoginFrame(ApiClient api) {
        super("ERP Imprenta");
        this.api = api;
        construirUi();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void construirUi() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(32, 32, 32, 32));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;

        JLabel titulo = new JLabel("ERP Imprenta", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(titulo, c);

        c.gridy++;
        JLabel subtitulo = new JLabel("Inicia sesión con tu cuenta de trabajador", SwingConstants.CENTER);
        panel.add(subtitulo, c);

        c.gridy++;
        c.insets = new Insets(16, 4, 2, 4);
        panel.add(new JLabel("Email"), c);
        c.gridy++;
        c.insets = new Insets(2, 4, 2, 4);
        panel.add(campoEmail, c);

        c.gridy++;
        c.insets = new Insets(12, 4, 2, 4);
        panel.add(new JLabel("Contraseña"), c);
        c.gridy++;
        c.insets = new Insets(2, 4, 2, 4);
        panel.add(campoPassword, c);

        c.gridy++;
        c.insets = new Insets(12, 4, 4, 4);
        labelError.setForeground(new Color(0xB3261E));
        panel.add(labelError, c);

        c.gridy++;
        botonEntrar.addActionListener(e -> intentarLogin());
        panel.add(botonEntrar, c);

        getRootPane().setDefaultButton(botonEntrar);
        panel.setPreferredSize(new Dimension(360, panel.getPreferredSize().height));
        setContentPane(panel);
    }

    private void intentarLogin() {
        String email = campoEmail.getText().trim();
        String password = new String(campoPassword.getPassword());
        labelError.setText(" ");

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

        botonEntrar.setEnabled(false);
        api.guardarCredenciales(email, password);

        new SwingWorker<JsonNode, Void>() {
            @Override
            protected JsonNode doInBackground() throws ErrorApi {
                return api.llamar("/me");
            }

            @Override
            protected void done() {
                botonEntrar.setEnabled(true);
                try {
                    JsonNode usuario = get();
                    campoPassword.setText("");
                    new PrincipalFrame(api, usuario).setVisible(true);
                    dispose();
                } catch (Exception e) {
                    // Mensaje generico a proposito: no decimos si el email no existe, si
                    // la contrasena es incorrecta o si la cuenta esta bloqueada por
                    // fuerza bruta, igual que hace el frontend web.
                    api.borrarCredenciales();
                    labelError.setText("Email o contraseña incorrectos.");
                }
            }
        }.execute();
    }
}
