package com.tfgLeilaFraileSimon.sprinta;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// la aplicacion de escritorio. Empieza en la pantalla de login
public class SprintaApp extends Application {
    // la ventana principal (la guardo para poder cambiar de pantalla desde los controladores)
    private static Stage ventana;

    @Override
    public void start(Stage stage) throws IOException {
        ventana = stage;
        ventana.setTitle("Sprinta");
        cambiarPantalla("login.fxml", 420, 520);
        ventana.show();
    }

    // carga un archivo .fxml y lo pone en la ventana, con los estilos de Sprinta
    public static void cambiarPantalla(String fxml, double ancho, double alto) throws IOException {
        Parent pantalla = FXMLLoader.load(SprintaApp.class.getResource(fxml));
        Scene escena = new Scene(pantalla, ancho, alto);
        escena.getStylesheets().add(SprintaApp.class.getResource("estilos.css").toExternalForm());
        ventana.setScene(escena);
        ventana.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
