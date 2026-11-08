package com.tfgLeilaFraileSimon.sprinta.pantallas;

import java.io.IOException;

import com.tfgLeilaFraileSimon.sprinta.SprintaApp;
import com.tfgLeilaFraileSimon.sprinta.api.ApiCliente;
import com.tfgLeilaFraileSimon.sprinta.api.ErrorApi;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

// pantalla de login (login.fxml)
public class LoginController {
    // los @FXML se enlazan solos con los fx:id del archivo .fxml
    @FXML
    private TextField campoEmail;
    @FXML
    private PasswordField campoPassword;
    @FXML
    private Label mensajeError;

    // al pulsar "Entrar" (o Enter en la contraseña)
    @FXML
    private void entrar() throws IOException {
        mensajeError.setText("");
        if (campoEmail.getText().isBlank() || campoPassword.getText().isBlank()) {
            mensajeError.setText("Escribe tu email y tu contraseña");
            return;
        }
        try {
            ApiCliente.login(campoEmail.getText().trim(), campoPassword.getText());
            SprintaApp.cambiarPantalla("clientes.fxml", 1000, 650);
        } catch (ErrorApi e) {
            // email o contraseña mal, cuenta bloqueada, servidor apagado...
            mensajeError.setText(e.getMessage());
        }
    }
}
