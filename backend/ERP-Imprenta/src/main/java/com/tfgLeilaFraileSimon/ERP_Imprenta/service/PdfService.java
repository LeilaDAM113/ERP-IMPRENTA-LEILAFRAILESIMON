package com.tfgLeilaFraileSimon.ERP_Imprenta.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Cliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.LineaPresupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;

// hace los PDF del presupuesto y de la factura (con la libreria OpenPDF).
// el PDF lo hace el servidor y las apps solo lo descargan y lo abren
@Service
public class PdfService {
    // el morado de Sprinta
    private static final Color MORADO = new Color(108, 63, 197);
    private static final Font TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, MORADO);
    private static final Font SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    private static final Font NEGRITA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
    private static final Font NORMAL = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private static final Font PEQUENA = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);
    private static final Font CABECERA_TABLA = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

    // dinero y fechas en formato de Espana: 1.234,56 € y 08/11/2026
    private static final Locale ESPANA = Locale.of("es", "ES");
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ---- PRESUPUESTO ----
    public byte[] presupuesto(Presupuesto presupuesto, List<LineaPresupuesto> lineas) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        cabecera(documento, "PRESUPUESTO", presupuesto.getNumero());
        datos(documento,
                "Cliente", nombreCliente(presupuesto.getCliente()),
                "Fecha", fecha(presupuesto.getFechaEmision()),
                "Valido hasta", fecha(presupuesto.getFechaValidez()),
                "Comercial", presupuesto.getComercial() == null ? "-" : presupuesto.getComercial().getNombreCompleto(),
                "Estado", String.valueOf(presupuesto.getEstado()));
        descripcion(documento, presupuesto.getDescripcion());

        PdfPTable tabla = tabla("Producto", "Cantidad", "Precio/unidad", "Total");
        for (LineaPresupuesto linea : lineas) {
            celda(tabla, linea.getProducto() == null ? "-" : linea.getProducto().getNombre(), Element.ALIGN_LEFT);
            celda(tabla, String.valueOf(linea.getCantidad()), Element.ALIGN_RIGHT);
            celda(tabla, precioUnidad(linea.getPrecioUnitario()), Element.ALIGN_RIGHT);
            celda(tabla, euros(linea.getTotal()), Element.ALIGN_RIGHT);
        }
        documento.add(tabla);

        BigDecimal subtotal = cero(presupuesto.getSubtotal());
        BigDecimal importe = cero(presupuesto.getImporte());
        totales(documento,
                "Subtotal", euros(subtotal),
                "IVA (" + porcentaje(presupuesto.getPorcentajeIva()) + " %)", euros(importe.subtract(subtotal)),
                "TOTAL", euros(importe));
        pie(documento, "Presupuesto valido hasta la fecha indicada. Precios con IVA incluido en el total.");
        documento.close();
        return salida.toByteArray();
    }

    // ---- FACTURA ----
    public byte[] factura(Factura factura, List<LineaFactura> lineas) {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        cabecera(documento, "FACTURA", factura.getNumero());
        datos(documento,
                "Cliente", nombreCliente(factura.getCliente()),
                "Fecha", fecha(factura.getFechaEmision()),
                "Vencimiento", fecha(factura.getFechaVencimiento()),
                "Forma de pago", factura.getMetodoPago() == null ? "-" : String.valueOf(factura.getMetodoPago()),
                "Estado", String.valueOf(factura.getEstado()));

        PdfPTable tabla = tabla("Concepto", "Cantidad", "Precio/unidad", "Importe");
        if (lineas.isEmpty()) {
            // sin lineas: una sola linea con el trabajo y la base
            String concepto = factura.getOrdenTrabajo() == null ? "Trabajos de impresion"
                    : "Trabajo " + factura.getOrdenTrabajo().getNumero() + " - " + factura.getOrdenTrabajo().getTitulo();
            celda(tabla, concepto, Element.ALIGN_LEFT);
            celda(tabla, "1", Element.ALIGN_RIGHT);
            celda(tabla, euros(factura.getBaseImponible()), Element.ALIGN_RIGHT);
            celda(tabla, euros(factura.getBaseImponible()), Element.ALIGN_RIGHT);
        }
        for (LineaFactura linea : lineas) {
            String concepto = linea.getDescripcion() != null ? linea.getDescripcion()
                    : linea.getProducto() == null ? "-" : linea.getProducto().getNombre();
            celda(tabla, concepto, Element.ALIGN_LEFT);
            celda(tabla, String.valueOf(linea.getCantidad()), Element.ALIGN_RIGHT);
            celda(tabla, precioUnidad(linea.getPrecioUnitario()), Element.ALIGN_RIGHT);
            celda(tabla, euros(linea.getSubtotal()), Element.ALIGN_RIGHT);
        }
        documento.add(tabla);

        totales(documento,
                "Base imponible", euros(factura.getBaseImponible()),
                "IVA (" + porcentaje(factura.getPorcentajeIva()) + " %)", euros(factura.getCuotaIva()),
                "TOTAL", euros(factura.getTotal()));
        pie(documento, "Gracias por confiar en nosotros.");
        documento.close();
        return salida.toByteArray();
    }

    // ---- trozos que se repiten en los dos PDF ----

    // nombre de la empresa arriba y el titulo con el numero
    private void cabecera(Document documento, String titulo, String numero) {
        documento.add(new Paragraph("Sprinta", TITULO));
        documento.add(new Paragraph("Gestion para imprentas", PEQUENA));
        Paragraph linea = new Paragraph(titulo + "  " + (numero == null ? "" : numero), SUBTITULO);
        linea.setSpacingBefore(20);
        linea.setSpacingAfter(10);
        documento.add(linea);
    }

    // tabla sin bordes con "etiqueta: valor" (cliente, fecha...). Van de dos en dos
    private void datos(Document documento, String... pares) {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(60);
        tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla.setSpacingAfter(10);
        for (int i = 0; i < pares.length; i += 2) {
            PdfPCell etiqueta = new PdfPCell(new Phrase(pares[i], NEGRITA));
            PdfPCell valor = new PdfPCell(new Phrase(pares[i + 1], NORMAL));
            etiqueta.setBorder(PdfPCell.NO_BORDER);
            valor.setBorder(PdfPCell.NO_BORDER);
            tabla.addCell(etiqueta);
            tabla.addCell(valor);
        }
        documento.add(tabla);
    }

    private void descripcion(Document documento, String texto) {
        if (texto != null && !texto.isBlank()) {
            Paragraph parrafo = new Paragraph(texto, NORMAL);
            parrafo.setSpacingAfter(10);
            documento.add(parrafo);
        }
    }

    // tabla con la cabecera en morado
    private PdfPTable tabla(String... columnas) {
        PdfPTable tabla = new PdfPTable(new float[] { 4, 1.3f, 1.7f, 1.7f });
        tabla.setWidthPercentage(100);
        tabla.setSpacingAfter(10);
        for (String columna : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, CABECERA_TABLA));
            celda.setBackgroundColor(MORADO);
            celda.setPadding(6);
            tabla.addCell(celda);
        }
        return tabla;
    }

    private void celda(PdfPTable tabla, String texto, int alineacion) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, NORMAL));
        celda.setHorizontalAlignment(alineacion);
        celda.setPadding(5);
        tabla.addCell(celda);
    }

    // subtotal, IVA y total a la derecha (el ultimo en negrita)
    private void totales(Document documento, String... pares) {
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(40);
        tabla.setHorizontalAlignment(Element.ALIGN_RIGHT);
        for (int i = 0; i < pares.length; i += 2) {
            boolean ultimo = i == pares.length - 2;
            PdfPCell etiqueta = new PdfPCell(new Phrase(pares[i], ultimo ? NEGRITA : NORMAL));
            PdfPCell valor = new PdfPCell(new Phrase(pares[i + 1], ultimo ? NEGRITA : NORMAL));
            etiqueta.setBorder(PdfPCell.NO_BORDER);
            valor.setBorder(PdfPCell.NO_BORDER);
            valor.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tabla.addCell(etiqueta);
            tabla.addCell(valor);
        }
        documento.add(tabla);
    }

    private void pie(Document documento, String texto) {
        Paragraph parrafo = new Paragraph(texto, PEQUENA);
        parrafo.setSpacingBefore(30);
        documento.add(parrafo);
    }

    // ---- formatos ----

    private String nombreCliente(Cliente cliente) {
        return cliente == null ? "-" : cliente.getNombreCompleto();
    }

    private String fecha(LocalDate fecha) {
        return fecha == null ? "-" : fecha.format(FECHA);
    }

    // 1234.5 -> "1.234,50 €"
    private String euros(BigDecimal cantidad) {
        return cantidad == null ? "-" : NumberFormat.getCurrencyInstance(ESPANA).format(cantidad);
    }

    // el precio por unidad puede tener hasta 4 decimales (0,0216 €)
    private String precioUnidad(BigDecimal cantidad) {
        if (cantidad == null) {
            return "-";
        }
        NumberFormat formato = NumberFormat.getNumberInstance(ESPANA);
        formato.setMinimumFractionDigits(2);
        formato.setMaximumFractionDigits(4);
        return formato.format(cantidad) + " €";
    }

    // 21.00 -> "21"
    private String porcentaje(BigDecimal cantidad) {
        return cantidad == null ? "-" : cantidad.stripTrailingZeros().toPlainString();
    }

    private BigDecimal cero(BigDecimal cantidad) {
        return cantidad == null ? BigDecimal.ZERO : cantidad;
    }
}
