package com.tfgLeilaFraileSimon.sprinta.pantallas;

import java.io.IOException;

import com.tfgLeilaFraileSimon.sprinta.SprintaApp;
import com.tfgLeilaFraileSimon.sprinta.api.ApiCliente;
import com.tfgLeilaFraileSimon.sprinta.api.ErrorApi;
import com.tfgLeilaFraileSimon.sprinta.api.Sesion;
import com.tfgLeilaFraileSimon.sprinta.modelo.Cliente;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

// pantalla con la tabla de clientes (clientes.fxml)
public class ClientesController {
    @FXML
    private Label saludo;
    @FXML
    private Label mensaje;
    @FXML
    private TableView<Cliente> tabla;
    @FXML
    private TableColumn<Cliente, String> columnaNombre;
    @FXML
    private TableColumn<Cliente, String> columnaTipo;
    @FXML
    private TableColumn<Cliente, String> columnaFormaPago;
    @FXML
    private TableColumn<Cliente, String> columnaEmail;
    @FXML
    private TableColumn<Cliente, String> columnaTelefono;

    // initialize() lo llama JavaFX solo, al abrir la pantalla
    @FXML
    private void initialize() {
        saludo.setText("Hola, " + Sesion.getTrabajador().getNombreCompleto()
                + "  ·  " + Sesion.getTrabajador().getRol());
        // cada columna enseña un atributo del Cliente (busca su getter: getNombreCompleto...)
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));
        columnaFormaPago.setCellValueFactory(new PropertyValueFactory<>("formaPago"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        cargar();
    }

    // pido los clientes a la API y los pongo en la tabla
    @FXML
    private void cargar() {
        try {
            tabla.setItems(FXCollections.observableArrayList(ApiCliente.listar("/api/cliente", Cliente.class)));
            mensaje.setText(tabla.getItems().size() + " clientes");
        } catch (ErrorApi e) {
            // por ejemplo un operario no puede ver clientes (403)
            tabla.getItems().clear();
            mensaje.setText(e.getMessage());
        }
    }

    // olvido el token y vuelvo al login
    @FXML
    private void cerrarSesion() throws IOException {
        Sesion.cerrar();
        SprintaApp.cambiarPantalla("login.fxml", 420, 520);
    }
}
