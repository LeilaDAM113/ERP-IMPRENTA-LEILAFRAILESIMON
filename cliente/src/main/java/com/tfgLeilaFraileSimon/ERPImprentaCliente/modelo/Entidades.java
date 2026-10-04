package com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.AREA;
import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.BOOLEANO;
import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.DECIMAL;
import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.FECHA;
import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.NUMERO;
import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.RELACION;
import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.SELECT;
import static com.tfgLeilaFraileSimon.ERPImprentaCliente.modelo.TipoCampo.TEXTO;

/**
 * Puerto directo de ENTIDADES y GRUPOS_NAV (frontend/js/crud.js y app.js): el
 * backend tiene 21 entidades con el mismo patron de API REST, asi que en vez
 * de escribir 20 pantallas Swing casi identicas a mano, esta clase describe
 * cada una (campos, tipos, endpoint) y GenericoPanel/FormularioGenericoDialog
 * construyen la tabla y el formulario a partir de esa configuracion.
 *
 * Trabajador se queda FUERA de este motor y tiene su propia pantalla
 * (TrabajadoresPanel), igual que en el frontend web: la contrasena y el rol
 * necesitan logica especial que no encaja en un CRUD generico.
 */
public final class Entidades {

    public static final Map<String, EntidadConfig> ENTIDADES = new LinkedHashMap<>();
    public static final List<GrupoNav> GRUPOS_NAV;

    private Entidades() {
    }

    private static void registrar(String clave, String titulo, String subtitulo, String endpoint, CampoConfig... campos) {
        ENTIDADES.put(clave, new EntidadConfig(clave, titulo, subtitulo, endpoint, List.of(campos)));
    }

    static {
        registrar("cliente", "Clientes", "Particulares y empresas dados de alta como clientes.", "/cliente",
                new CampoConfig("nombreCompleto", "Nombre", TEXTO).requerido(),
                new CampoConfig("tipoCliente", "Tipo de cliente", SELECT).opciones("PARTICULAR", "EMPRESA"),
                new CampoConfig("telefono", "Teléfono", TEXTO),
                new CampoConfig("email", "Email", TEXTO),
                new CampoConfig("fechaAlta", "Fecha de alta", FECHA));

        registrar("empresa", "Empresas", "Perfil de empresa asociado a un cliente.", "/empresa",
                new CampoConfig("cliente", "Cliente", RELACION).relacion("cliente").requerido(),
                new CampoConfig("cif", "CIF", TEXTO),
                new CampoConfig("nombreComercial", "Nombre comercial", TEXTO).requerido(),
                new CampoConfig("razonSocial", "Razón social", TEXTO),
                new CampoConfig("direccion", "Dirección", TEXTO),
                new CampoConfig("ciudad", "Ciudad", TEXTO),
                new CampoConfig("provincia", "Provincia", TEXTO),
                new CampoConfig("codigoPostal", "Código postal", TEXTO),
                new CampoConfig("telefono", "Teléfono", TEXTO),
                new CampoConfig("email", "Email", TEXTO),
                new CampoConfig("web", "Web", TEXTO));

        registrar("particular", "Particulares", "Perfil de particular asociado a un cliente.", "/particular",
                new CampoConfig("cliente", "Cliente", RELACION).relacion("cliente").requerido(),
                new CampoConfig("dni", "DNI", TEXTO),
                new CampoConfig("nombre", "Nombre", TEXTO).requerido(),
                new CampoConfig("apellido", "Apellidos", TEXTO),
                new CampoConfig("direccion", "Dirección", TEXTO),
                new CampoConfig("ciudad", "Ciudad", TEXTO),
                new CampoConfig("provincia", "Provincia", TEXTO),
                new CampoConfig("codigoPostal", "Código postal", TEXTO),
                new CampoConfig("email", "Email", TEXTO));

        registrar("contactoEmpresa", "Contactos de empresa", "Personas de contacto dentro de una empresa cliente.", "/contacto-empresa",
                new CampoConfig("empresa", "Empresa", RELACION).relacion("empresa").requerido(),
                new CampoConfig("nombreCompleto", "Nombre", TEXTO).requerido(),
                new CampoConfig("departamento", "Departamento", TEXTO),
                new CampoConfig("telefono", "Teléfono", TEXTO),
                new CampoConfig("email", "Email", TEXTO));

        registrar("puesto", "Puestos", "Puestos de trabajo que se pueden asignar a un trabajador.", "/puesto",
                new CampoConfig("nombre", "Nombre", TEXTO).requerido());

        registrar("proveedor", "Proveedores", "Proveedores de materiales y servicios.", "/proveedor",
                new CampoConfig("nombreCompleto", "Nombre", TEXTO).requerido(),
                new CampoConfig("nif", "NIF", TEXTO),
                new CampoConfig("nombreContacto", "Persona de contacto", TEXTO),
                new CampoConfig("telefono", "Teléfono", TEXTO),
                new CampoConfig("email", "Email", TEXTO),
                new CampoConfig("direccion", "Dirección", TEXTO),
                new CampoConfig("ciudad", "Ciudad", TEXTO),
                new CampoConfig("provincia", "Provincia", TEXTO),
                new CampoConfig("codigoPostal", "Código postal", TEXTO),
                new CampoConfig("pais", "País", TEXTO),
                new CampoConfig("web", "Web", TEXTO),
                new CampoConfig("producto", "Producto que suministra", RELACION).relacion("catalogo"),
                new CampoConfig("fechaAlta", "Fecha de alta", FECHA),
                new CampoConfig("activo", "Activo", BOOLEANO),
                new CampoConfig("observaciones", "Observaciones", AREA).anchoCompleto());

        registrar("maquinaria", "Maquinaria", "Máquinas del taller.", "/maquinaria",
                new CampoConfig("nombre", "Nombre", TEXTO).requerido(),
                new CampoConfig("marca", "Marca", TEXTO),
                new CampoConfig("modelo", "Modelo", TEXTO),
                new CampoConfig("numeroSerie", "Número de serie", TEXTO),
                new CampoConfig("tipo", "Tipo", SELECT).opciones("OFFSET", "DIGITAL", "GRAN_FORMATO", "ENCUADERNACION", "ACABADOS", "CORTE"),
                new CampoConfig("estado", "Estado", SELECT).opciones("OPERATIVA", "EN_MANTENIMIENTO", "AVERIADA", "FUERA_DE_SERVICIO"),
                new CampoConfig("fechaCompra", "Fecha de compra", FECHA),
                new CampoConfig("precioCompra", "Precio de compra (€)", DECIMAL),
                new CampoConfig("rentabilidad", "Rentabilidad", DECIMAL),
                new CampoConfig("ultimaRevision", "Última revisión", FECHA),
                new CampoConfig("activo", "Activa", BOOLEANO),
                new CampoConfig("observaciones", "Observaciones", AREA).anchoCompleto());

        registrar("inventario", "Inventario", "Materiales y existencias en almacén.", "/inventario",
                new CampoConfig("nombre", "Nombre", TEXTO).requerido(),
                new CampoConfig("categoria", "Categoría", TEXTO),
                new CampoConfig("proveedor", "Proveedor", RELACION).relacion("proveedor"),
                new CampoConfig("cantidad", "Cantidad", NUMERO),
                new CampoConfig("stockActual", "Stock actual", NUMERO),
                new CampoConfig("stockMinimo", "Stock mínimo", NUMERO),
                new CampoConfig("precioProducto", "Precio (€)", DECIMAL),
                new CampoConfig("fechaActualizacion", "Última actualización", FECHA),
                new CampoConfig("descripcion", "Descripción", AREA).anchoCompleto());

        registrar("catalogo", "Catálogo", "Productos y servicios que se pueden vender.", "/catalogo",
                new CampoConfig("descripcion", "Descripción", TEXTO).requerido(),
                new CampoConfig("precioVenta", "Precio de venta (€)", DECIMAL));

        registrar("pedido", "Pedidos", "Pedidos de material a proveedores.", "/pedido",
                new CampoConfig("proveedor", "Proveedor", RELACION).relacion("proveedor"),
                new CampoConfig("inventario", "Material", RELACION).relacion("inventario"),
                new CampoConfig("cantidad", "Cantidad", NUMERO),
                new CampoConfig("importe", "Importe (€)", DECIMAL),
                new CampoConfig("estado", "Estado", SELECT).opciones("PENDIENTE", "ENVIADO", "RECIBIDO", "CANCELADO"),
                new CampoConfig("fechaPedido", "Fecha de pedido", FECHA),
                new CampoConfig("descripcion", "Descripción", AREA).anchoCompleto());

        registrar("ordenTrabajo", "Órdenes de trabajo", "Trabajos en curso o planificados en el taller.", "/orden-trabajo",
                new CampoConfig("numero", "Número", TEXTO).requerido(),
                new CampoConfig("titulo", "Título", TEXTO).requerido(),
                new CampoConfig("presupuesto", "Presupuesto de origen", RELACION).relacion("presupuesto"),
                new CampoConfig("trabajador", "Trabajador responsable", RELACION).relacion("trabajador"),
                new CampoConfig("prioridad", "Prioridad", SELECT).opciones("BAJA", "MEDIA", "ALTA"),
                new CampoConfig("estado", "Estado", SELECT).opciones("PENDIENTE", "EN_PROCESO", "PAUSADA", "COMPLETADA", "CANCELADA"),
                new CampoConfig("fechaInicio", "Fecha de inicio", FECHA),
                new CampoConfig("fechaFin", "Fecha de fin", FECHA),
                new CampoConfig("descripcion", "Descripción", AREA).anchoCompleto());

        registrar("ordenTrabajador", "Horas trabajadas", "Horas reales que ha dedicado cada trabajador a una orden.", "/orden-trabajador",
                new CampoConfig("ordenTrabajo", "Orden de trabajo", RELACION).relacion("ordenTrabajo").requerido(),
                new CampoConfig("trabajador", "Trabajador", RELACION).relacion("trabajador").requerido(),
                new CampoConfig("horasReales", "Horas reales", DECIMAL),
                new CampoConfig("observaciones", "Observaciones", AREA).anchoCompleto());

        registrar("ordenEmpleado", "Empleados en orden", "Qué trabajadores están asignados a cada orden de trabajo.", "/orden-empleado",
                new CampoConfig("ordenTrabajo", "Orden de trabajo", RELACION).relacion("ordenTrabajo").requerido(),
                new CampoConfig("trabajador", "Trabajador", RELACION).relacion("trabajador").requerido());

        registrar("presupuesto", "Presupuestos", "Presupuestos enviados a clientes.", "/presupuesto",
                new CampoConfig("cliente", "Cliente", RELACION).relacion("cliente").requerido(),
                new CampoConfig("estado", "Estado", SELECT).opciones("BORRADOR", "ACEPTADO", "RECHAZADO"),
                new CampoConfig("fechaEmision", "Fecha de emisión", FECHA),
                new CampoConfig("fechaValidez", "Válido hasta", FECHA),
                new CampoConfig("subtotal", "Subtotal (€)", DECIMAL),
                new CampoConfig("importe", "Importe total (€)", DECIMAL),
                new CampoConfig("descripcion", "Descripción", AREA).anchoCompleto());

        registrar("lineaPresupuesto", "Líneas de presupuesto", "Productos incluidos en cada presupuesto.", "/linea-presupuesto",
                new CampoConfig("presupuesto", "Presupuesto", RELACION).relacion("presupuesto").requerido(),
                new CampoConfig("producto", "Producto", RELACION).relacion("catalogo").requerido(),
                new CampoConfig("cantidad", "Cantidad", NUMERO),
                new CampoConfig("precioUnitario", "Precio unitario (€)", DECIMAL),
                new CampoConfig("subtotal", "Subtotal (€)", DECIMAL),
                new CampoConfig("total", "Total (€)", DECIMAL));

        registrar("factura", "Facturas", "Facturas emitidas a clientes.", "/factura",
                new CampoConfig("numero", "Número", TEXTO).requerido(),
                new CampoConfig("ordenTrabajo", "Orden de trabajo", RELACION).relacion("ordenTrabajo"),
                new CampoConfig("estado", "Estado", SELECT).opciones("PENDIENTE", "PAGADA", "VENCIDA", "CANCELADA"),
                new CampoConfig("metodoPago", "Método de pago", SELECT).opciones("EFECTIVO", "TARJETA", "TRANSFERENCIA", "DOMICILIACION"),
                new CampoConfig("total", "Total (€)", DECIMAL),
                new CampoConfig("fechaEmision", "Fecha de emisión", FECHA),
                new CampoConfig("fechaPago", "Fecha de pago", FECHA));

        registrar("lineaFactura", "Líneas de factura", "Productos incluidos en cada factura.", "/linea-factura",
                new CampoConfig("factura", "Factura", RELACION).relacion("factura").requerido(),
                new CampoConfig("producto", "Producto", RELACION).relacion("catalogo"),
                new CampoConfig("lineaPresupuesto", "Línea de presupuesto de origen", RELACION).relacion("lineaPresupuesto"),
                new CampoConfig("lineaAlbaran", "Línea de albarán de origen", RELACION).relacion("lineaAlbaran"),
                new CampoConfig("cantidad", "Cantidad", NUMERO));

        registrar("albaran", "Albaranes", "Entregas realizadas a clientes.", "/albaran",
                new CampoConfig("numero", "Número", TEXTO).requerido(),
                new CampoConfig("cliente", "Cliente", RELACION).relacion("cliente"),
                new CampoConfig("ordenTrabajo", "Orden de trabajo", RELACION).relacion("ordenTrabajo"),
                new CampoConfig("estado", "Estado", SELECT).opciones("PENDIENTE", "ENTREGADO", "CANCELADO"),
                new CampoConfig("fechaEmision", "Fecha de emisión", FECHA),
                new CampoConfig("fechaEntrega", "Fecha de entrega", FECHA),
                new CampoConfig("observaciones", "Observaciones", AREA).anchoCompleto());

        registrar("lineaAlbaran", "Líneas de albarán", "Productos entregados en cada albarán.", "/linea-albaran",
                new CampoConfig("albaran", "Albarán", RELACION).relacion("albaran").requerido(),
                new CampoConfig("producto", "Producto", RELACION).relacion("catalogo"),
                new CampoConfig("cantidad", "Cantidad", NUMERO));

        registrar("pago", "Pagos", "Cobros y pagos asociados a facturas, órdenes, proveedores o inventario.", "/pago",
                new CampoConfig("factura", "Factura", RELACION).relacion("factura"),
                new CampoConfig("ordenTrabajo", "Orden de trabajo", RELACION).relacion("ordenTrabajo"),
                new CampoConfig("proveedor", "Proveedor", RELACION).relacion("proveedor"),
                new CampoConfig("inventario", "Material", RELACION).relacion("inventario"),
                new CampoConfig("importe", "Importe (€)", DECIMAL),
                new CampoConfig("estado", "Estado", SELECT).opciones("PENDIENTE", "COMPLETADO", "CANCELADO"),
                new CampoConfig("metodoPago", "Método de pago", SELECT).opciones("EFECTIVO", "TARJETA", "TRANSFERENCIA", "DOMICILIACION"),
                new CampoConfig("fechaEmision", "Fecha de emisión", FECHA),
                new CampoConfig("fechaPago", "Fecha de pago", FECHA),
                new CampoConfig("descripcion", "Descripción", AREA).anchoCompleto());

        // Igual que GRUPOS_NAV en frontend/js/app.js: agrupa los modulos por
        // area del negocio para la barra lateral. "dashboard" y "trabajador"
        // tienen vista propia; el resto se resuelve con GenericoPanel a partir
        // de su entrada en ENTIDADES.
        GRUPOS_NAV = List.of(
                new GrupoNav("General", List.of(new ItemNav("dashboard", "Panel"))),
                new GrupoNav("Personal", List.of(
                        new ItemNav("trabajador", "Trabajadores"),
                        new ItemNav("puesto", "Puestos"))),
                new GrupoNav("Clientes", List.of(
                        new ItemNav("cliente", "Clientes"),
                        new ItemNav("empresa", "Empresas"),
                        new ItemNav("particular", "Particulares"),
                        new ItemNav("contactoEmpresa", "Contactos de empresa"))),
                new GrupoNav("Compras y taller", List.of(
                        new ItemNav("proveedor", "Proveedores"),
                        new ItemNav("pedido", "Pedidos"),
                        new ItemNav("inventario", "Inventario"),
                        new ItemNav("maquinaria", "Maquinaria"),
                        new ItemNav("catalogo", "Catálogo"))),
                new GrupoNav("Producción", List.of(
                        new ItemNav("ordenTrabajo", "Órdenes de trabajo"),
                        new ItemNav("ordenTrabajador", "Horas trabajadas"),
                        new ItemNav("ordenEmpleado", "Empleados en orden"))),
                new GrupoNav("Ventas", List.of(
                        new ItemNav("presupuesto", "Presupuestos"),
                        new ItemNav("lineaPresupuesto", "Líneas de presupuesto"),
                        new ItemNav("albaran", "Albaranes"),
                        new ItemNav("lineaAlbaran", "Líneas de albarán"),
                        new ItemNav("factura", "Facturas"),
                        new ItemNav("lineaFactura", "Líneas de factura"),
                        new ItemNav("pago", "Pagos"))));
    }

    public static ItemNav buscarItemNav(String clave) {
        return GRUPOS_NAV.stream()
                .flatMap(g -> g.items().stream())
                .filter(i -> i.clave().equals(clave))
                .findFirst()
                .orElse(null);
    }

    // Trabajador no tiene entrada en ENTIDADES (tiene pantalla propia, ver
    // TrabajadoresPanel) pero SI se usa como relacion en ordenTrabajo,
    // ordenTrabajador y ordenEmpleado, asi que los combos de esos formularios
    // necesitan saber que su endpoint es /trabajador.
    public static String endpointDeEntidadRelacion(String claveEntidad) {
        if ("trabajador".equals(claveEntidad)) {
            return "/trabajador";
        }
        EntidadConfig config = ENTIDADES.get(claveEntidad);
        return config != null ? config.endpoint : null;
    }
}
