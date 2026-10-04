package com.tfgLeilaFraileSimon.ERPImprentaCliente.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

import com.fasterxml.jackson.databind.JsonNode;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.api.ApiClient;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.Entidades;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.GrupoNav;
import com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.ItemNav;

/** Equivalente a "vista-dashboard" de frontend/index.html + cargarDashboard() en app.js. */
public class DashboardPanel extends JPanel {

    private record TarjetaEstadistica(String etiqueta, String endpoint) {
    }

    private static final List<TarjetaEstadistica> TARJETAS = List.of(
            new TarjetaEstadistica("Clientes", "/cliente"),
            new TarjetaEstadistica("Trabajadores", "/trabajador"),
            new TarjetaEstadistica("Presupuestos", "/presupuesto"),
            new TarjetaEstadistica("Órdenes de trabajo", "/orden-trabajo"));

    private final ApiClient api;
    private final JPanel gridEstadisticas = new JPanel(new GridLayout(1, TARJETAS.size(), 12, 12));

    public DashboardPanel(ApiClient api, JsonNode usuario, Consumer<String> navegar) {
        this.api = api;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel bienvenida = new JLabel("Bienvenida, " + usuario.path("nombreCompleto").asText());
        bienvenida.setFont(bienvenida.getFont().deriveFont(Font.BOLD, 20f));
        bienvenida.setAlignmentX(LEFT_ALIGNMENT);
        add(bienvenida);

        JLabel resumen = new JLabel("Resumen rápido de la actividad de la imprenta.");
        resumen.setAlignmentX(LEFT_ALIGNMENT);
        resumen.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(resumen);

        gridEstadisticas.setAlignmentX(LEFT_ALIGNMENT);
        gridEstadisticas.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 90));
        add(gridEstadisticas);

        JLabel tituloAccesos = new JLabel("Accesos rápidos");
        tituloAccesos.setFont(tituloAccesos.getFont().deriveFont(Font.BOLD, 15f));
        tituloAccesos.setBorder(BorderFactory.createEmptyBorder(24, 0, 8, 0));
        tituloAccesos.setAlignmentX(LEFT_ALIGNMENT);
        add(tituloAccesos);

        JPanel gridAccesos = new JPanel(new GridLayout(0, 4, 8, 8));
        gridAccesos.setAlignmentX(LEFT_ALIGNMENT);
        for (GrupoNav grupo : Entidades.GRUPOS_NAV) {
            for (ItemNav item : grupo.items()) {
                if ("dashboard".equals(item.clave())) {
                    continue;
                }
                JButton boton = new JButton(item.etiqueta());
                boton.addActionListener(e -> navegar.accept(item.clave()));
                gridAccesos.add(boton);
            }
        }
        add(gridAccesos);
    }

    public void cargar() {
        gridEstadisticas.removeAll();
        for (TarjetaEstadistica tarjeta : TARJETAS) {
            gridEstadisticas.add(construirTarjeta(tarjeta.etiqueta(), "…"));
        }
        gridEstadisticas.revalidate();
        gridEstadisticas.repaint();

        for (int indice = 0; indice < TARJETAS.size(); indice++) {
            final int i = indice;
            final TarjetaEstadistica tarjeta = TARJETAS.get(indice);
            new SwingWorker<Integer, Void>() {
                @Override
                protected Integer doInBackground() {
                    try {
                        JsonNode registros = api.llamar(tarjeta.endpoint());
                        return registros != null && registros.isArray() ? registros.size() : 0;
                    } catch (Exception e) {
                        return 0;
                    }
                }

                @Override
                protected void done() {
                    try {
                        gridEstadisticas.remove(i);
                        gridEstadisticas.add(construirTarjeta(tarjeta.etiqueta(), String.valueOf(get())), i);
                        gridEstadisticas.revalidate();
                        gridEstadisticas.repaint();
                    } catch (Exception ignorado) {
                        // Panel ya no visible o worker cancelado: no hay nada que actualizar.
                    }
                }
            }.execute();
        }
    }

    private JPanel construirTarjeta(String etiqueta, String numero) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xE5E7EB)),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        JLabel labelEtiqueta = new JLabel(etiqueta);
        labelEtiqueta.setForeground(new Color(0x6B7280));

        JLabel labelNumero = new JLabel(numero, SwingConstants.LEFT);
        labelNumero.setFont(labelNumero.getFont().deriveFont(Font.BOLD, 26f));

        tarjeta.add(labelEtiqueta);
        tarjeta.add(labelNumero);
        return tarjeta;
    }
}
