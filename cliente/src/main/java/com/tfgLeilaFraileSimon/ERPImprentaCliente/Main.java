package com.tfgLeilaFraileSimon.ERPImprentaCliente;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.ui.LoginFrame;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignorado) {
            // Si el look and feel del sistema no esta disponible, seguimos con el por defecto de Swing.
        }

        SwingUtilities.invokeLater(() -> new LoginFrame(new ApiClient()).setVisible(true));
    }
}
