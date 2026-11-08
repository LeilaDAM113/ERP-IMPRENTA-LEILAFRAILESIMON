package com.tfgLeilaFraileSimon.ERP_Imprenta.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.AlbaranRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.ConsumoMaterialRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.FacturaRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.LineaPresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.OrdenTrabajoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PagoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.PresupuestoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.RegistroTiempoRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.dto.TrabajadorCrearRequest;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Aviso;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Catalogo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Cliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Configuracion;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoAlbaran;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoFactura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoMaquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoOrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.EstadoPago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Factura;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.FormaPago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Inventario;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Maquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.MetodoPago;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.OrdenTrabajo;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Presupuesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.PrioridadOrden;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Proveedor;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Puesto;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.TipoAviso;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.TipoCliente;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.TipoMaquinaria;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.TipoMaterial;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.Trabajador;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.AvisoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.CatalogoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ClienteRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ConfiguracionRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.InventarioRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.MaquinariaRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.ProveedorRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.repository.PuestoRepository;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.AlbaranService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.ConsumoMaterialService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.FacturaService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.LineaPresupuestoService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.OrdenTrabajoService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.PagoService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.PresupuestoService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.RegistroTiempoService;
import com.tfgLeilaFraileSimon.ERP_Imprenta.service.TrabajadorService;

// datos de demostracion para la defensa: una imprenta de ejemplo con clientes,
// materiales, maquinas, presupuestos, ordenes, facturas...
// solo se crean si en application.properties pongo app.datos-demo=true
// y la base de datos no tiene ningun cliente (asi nunca machaca datos de verdad).
// uso mis propios servicios, asi se ve funcionar la logica: aceptar un presupuesto
// crea su orden, gastar material baja el stock y pide al proveedor, etc.
@Component
public class DatosDemoRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(DatosDemoRunner.class);
    // la contraseña de todos los trabajadores de prueba
    private static final String CLAVE_DEMO = "demo1234";

    @Value("${app.datos-demo:false}")
    private boolean activado;

    private final ClienteRepository clienteRepositorio;
    private final ProveedorRepository proveedorRepositorio;
    private final InventarioRepository inventarioRepositorio;
    private final MaquinariaRepository maquinariaRepositorio;
    private final CatalogoRepository catalogoRepositorio;
    private final PuestoRepository puestoRepositorio;
    private final ConfiguracionRepository configuracionRepositorio;
    private final AvisoRepository avisoRepositorio;
    private final TrabajadorService trabajadores;
    private final PresupuestoService presupuestos;
    private final LineaPresupuestoService lineas;
    private final OrdenTrabajoService ordenes;
    private final RegistroTiempoService tiempos;
    private final ConsumoMaterialService consumos;
    private final AlbaranService albaranes;
    private final FacturaService facturas;
    private final PagoService pagos;

    public DatosDemoRunner(ClienteRepository clienteRepositorio, ProveedorRepository proveedorRepositorio,
            InventarioRepository inventarioRepositorio, MaquinariaRepository maquinariaRepositorio,
            CatalogoRepository catalogoRepositorio, PuestoRepository puestoRepositorio,
            ConfiguracionRepository configuracionRepositorio, AvisoRepository avisoRepositorio,
            TrabajadorService trabajadores, PresupuestoService presupuestos, LineaPresupuestoService lineas,
            OrdenTrabajoService ordenes, RegistroTiempoService tiempos, ConsumoMaterialService consumos,
            AlbaranService albaranes, FacturaService facturas, PagoService pagos) {
        this.clienteRepositorio = clienteRepositorio;
        this.proveedorRepositorio = proveedorRepositorio;
        this.inventarioRepositorio = inventarioRepositorio;
        this.maquinariaRepositorio = maquinariaRepositorio;
        this.catalogoRepositorio = catalogoRepositorio;
        this.puestoRepositorio = puestoRepositorio;
        this.configuracionRepositorio = configuracionRepositorio;
        this.avisoRepositorio = avisoRepositorio;
        this.trabajadores = trabajadores;
        this.presupuestos = presupuestos;
        this.lineas = lineas;
        this.ordenes = ordenes;
        this.tiempos = tiempos;
        this.consumos = consumos;
        this.albaranes = albaranes;
        this.facturas = facturas;
        this.pagos = pagos;
    }

    @Override
    public void run(String... args) {
        if (!activado || clienteRepositorio.count() > 0) {
            return; // no esta activado o ya hay datos: no hago nada
        }
        LocalDate hoy = LocalDate.now();

        // ---- configuracion general ----
        Configuracion configuracion = new Configuracion();
        configuracion.setPorcentajeIva(dinero(21));
        configuracion.setMargenDefecto(dinero(30));
        configuracion.setPorcentajeComision(dinero(5));
        configuracion.setCosteHoraManoObra(dinero(18));
        configuracionRepositorio.save(configuracion);

        // ---- puestos y trabajadores (uno de cada rol) ----
        Puesto oficina = puesto("Oficina");
        Puesto taller = puesto("Taller");
        Puesto reparto = puesto("Reparto");
        Trabajador marta = trabajador("Marta Lopez Garcia", "marta@sprinta.es", oficina, 22, RolTrabajador.COMERCIAL);
        Trabajador javier = trabajador("Javier Ruiz Moreno", "javier@sprinta.es", taller, 25, RolTrabajador.ENCARGADO_TALLER);
        Trabajador lucia = trabajador("Lucia Martin Sanz", "lucia@sprinta.es", taller, 18, RolTrabajador.OPERARIO);
        Trabajador carlos = trabajador("Carlos Gil Romero", "carlos@sprinta.es", taller, 18, RolTrabajador.OPERARIO);
        Trabajador tomas = trabajador("Tomas Sanz Perez", "tomas@sprinta.es", reparto, 16, RolTrabajador.TRANSPORTISTA);

        // ---- clientes ----
        Cliente bar = cliente("Bar La Esquina", TipoCliente.EMPRESA, FormaPago.DIAS_30, "barlaesquina@correo.es");
        Cliente gimnasio = cliente("Gimnasio FitLife", TipoCliente.EMPRESA, FormaPago.DIAS_60, "info@fitlife.es");
        Cliente ayuntamiento = cliente("Ayuntamiento de Villanueva", TipoCliente.EMPRESA, FormaPago.DIAS_90, "compras@villanueva.es");
        Cliente laura = cliente("Laura Perez Diaz", TipoCliente.PARTICULAR, FormaPago.CONTADO, "laura.perez@correo.es");
        Cliente clinica = cliente("Clinica Dental Sonrisa", TipoCliente.EMPRESA, FormaPago.TRANSFERENCIA, "recepcion@clinicasonrisa.es");

        // ---- proveedores y materiales del almacen ----
        Proveedor papeles = proveedor("Papeles Garcia S.L.", "B12345678", "Antonio Garcia");
        Proveedor tintas = proveedor("Tintas del Norte S.A.", "A87654321", "Elena Vidal");
        Inventario estucado = material("PAP-EST-135", "Estucado brillo 135 g", TipoMaterial.PAPEL, "hojas", 5000, 2000, "0.045", papeles);
        material("PAP-OFF-90", "Offset 90 g", TipoMaterial.PAPEL, "hojas", 8000, 3000, "0.020", papeles);
        Inventario cartulina = material("CAR-300", "Cartulina 300 g", TipoMaterial.PAPEL, "hojas", 1500, 500, "0.120", papeles);
        Inventario cian = material("TIN-CIAN", "Tinta cian", TipoMaterial.TINTA, "kg", 12, 5, "18.50", tintas);
        material("TIN-NEGRA", "Tinta negra", TipoMaterial.TINTA, "kg", 15, 5, "16.00", tintas);
        Inventario planchas = material("PLA-CTP", "Plancha CTP", TipoMaterial.PLANCHA, "unidades", 60, 20, "6.50", tintas);

        // ---- maquinas (rentabilidad = lo que cuesta una hora de maquina) ----
        Maquinaria heidelberg = maquina("Heidelberg Speedmaster SM 52", "Heidelberg", TipoMaquinaria.OFFSET, 45);
        maquina("Konica Minolta AccurioPress C4080", "Konica Minolta", TipoMaquinaria.DIGITAL, 30);
        Maquinaria guillotina = maquina("Guillotina Polar 78", "Polar", TipoMaquinaria.CORTE, 20);

        // ---- productos ----
        Catalogo folletos = producto("Folletos A5", "Folleto a color en estucado", 30, 40);
        Catalogo tarjetas = producto("Tarjetas de visita", "Tarjeta 85x55 en cartulina", 50, 25);
        Catalogo carteles = producto("Carteles A3", "Cartel a color", 35, 30);
        Catalogo catalogos = producto("Catalogos A4", "Catalogo grapado de 16 paginas", 30, 120);

        // ---- P-0001: aceptado, hecho, entregado y facturado (pero sin pagar -> moroso) ----
        Presupuesto p1 = presupuesto("P-0001", bar, marta, hoy.minusDays(55), "5.000 folletos A5 para la carta del bar");
        linea(p1, folletos, 5000, 40, 25, 18, "1.5");
        presupuestos.enviar(p1.getId());
        OrdenTrabajo ot1 = presupuestos.aceptar(p1.getId());
        ordenes.actualizar(ot1.getId(), new OrdenTrabajoRequest(ot1.getNumero(), ot1.getTitulo(), p1.getDescripcion(),
                p1.getId(), javier.getId(), hoy.minusDays(52), hoy.minusDays(50), hoy.minusDays(48),
                PrioridadOrden.ALTA, EstadoOrdenTrabajo.COMPLETADA));
        tiempos.guardar(new RegistroTiempoRequest(ot1.getId(), lucia.getId(), heidelberg.getId(),
                hoy.minusDays(51).atTime(8, 0), hoy.minusDays(51).atTime(10, 15), 135, "Tirada sin incidencias"));
        tiempos.guardar(new RegistroTiempoRequest(ot1.getId(), carlos.getId(), guillotina.getId(),
                hoy.minusDays(50).atTime(9, 0), hoy.minusDays(50).atTime(9, 40), 40, "Corte y empaquetado"));
        consumos.guardar(new ConsumoMaterialRequest(ot1.getId(), estucado.getId(), lucia.getId(), dinero(2600),
                hoy.minusDays(51).atTime(10, 20)));
        consumos.guardar(new ConsumoMaterialRequest(ot1.getId(), cian.getId(), lucia.getId(), dinero(2),
                hoy.minusDays(51).atTime(10, 20)));
        consumos.guardar(new ConsumoMaterialRequest(ot1.getId(), planchas.getId(), lucia.getId(), dinero(4),
                hoy.minusDays(51).atTime(7, 50)));
        albaranes.guardar(new AlbaranRequest("ALB-0001", bar.getId(), ot1.getId(), tomas.getId(), hoy.minusDays(48),
                hoy.minusDays(48).atTime(11, 30), null, "Pepe (encargado del bar)", null, EstadoAlbaran.ENTREGADO));
        facturas.guardar(new FacturaRequest("F-0001", bar.getId(), ot1.getId(), hoy.minusDays(48), null, null,
                MetodoPago.TRANSFERENCIA, EstadoFactura.PENDIENTE, presupuestos.obtener(p1.getId()).getSubtotal(),
                null));

        // ---- P-0002: enviado, esperando respuesta del cliente ----
        Presupuesto p2 = presupuesto("P-0002", gimnasio, marta, hoy.minusDays(5), "Tarjetas y carteles de la nueva temporada");
        linea(p2, tarjetas, 1000, 8, 5, 6, "1");
        linea(p2, carteles, 50, 30, 20, 15, "2");
        presupuestos.enviar(p2.getId());

        // ---- P-0003: en borrador (todavia se puede cambiar) ----
        Presupuesto p3 = presupuesto("P-0003", ayuntamiento, marta, hoy, "Catalogo de las fiestas patronales");
        linea(p3, catalogos, 200, 300, 150, 120, "6");

        // ---- P-0004: rechazado ----
        Presupuesto p4 = presupuesto("P-0004", laura, marta, hoy.minusDays(20), "Tarjetas personales");
        lineas.guardar(new LineaPresupuestoRequest(p4.getId(), tarjetas.getId(), 100, new BigDecimal("0.30"),
                null, null, null, null, null));
        presupuestos.enviar(p4.getId());
        presupuestos.rechazar(p4.getId());

        // ---- P-0005: aceptado y en marcha en el taller ----
        Presupuesto p5 = presupuesto("P-0005", clinica, marta, hoy.minusDays(3), "2.000 folletos de la campana de higiene");
        linea(p5, folletos, 2000, 20, 15, 12, "1");
        presupuestos.enviar(p5.getId());
        OrdenTrabajo ot5 = presupuestos.aceptar(p5.getId());
        ordenes.actualizar(ot5.getId(), new OrdenTrabajoRequest(ot5.getNumero(), ot5.getTitulo(), p5.getDescripcion(),
                p5.getId(), javier.getId(), hoy, null, hoy.plusDays(3), PrioridadOrden.MEDIA,
                EstadoOrdenTrabajo.EN_PROCESO));
        // Carlos esta con ella ahora mismo (todavia no ha terminado)
        tiempos.guardar(new RegistroTiempoRequest(ot5.getId(), carlos.getId(), heidelberg.getId(),
                hoy.atTime(9, 0), null, null, null));
        // este consumo deja el estucado por debajo del minimo -> se prepara solo un pedido a Papeles Garcia
        consumos.guardar(new ConsumoMaterialRequest(ot5.getId(), estucado.getId(), carlos.getId(), dinero(600), null));
        consumos.guardar(new ConsumoMaterialRequest(ot5.getId(), cartulina.getId(), carlos.getId(), dinero(20), null));

        // ---- facturas antiguas: una vencida (moroso) y otra pagada ----
        facturas.guardar(new FacturaRequest("F-0002", ayuntamiento.getId(), null, hoy.minusDays(120), null, null,
                MetodoPago.TRANSFERENCIA, EstadoFactura.PENDIENTE, dinero(480), null));
        Factura pagada = facturas.guardar(new FacturaRequest("F-0003", gimnasio.getId(), null, hoy.minusDays(100),
                null, hoy.minusDays(40), MetodoPago.TRANSFERENCIA, EstadoFactura.PAGADA, dinero(250), null));
        pagos.guardar(new PagoRequest(pagada.getId(), "Pago completo de la factura F-0003", hoy.minusDays(100),
                hoy.minusDays(40), MetodoPago.TRANSFERENCIA, pagada.getTotal(), EstadoPago.COMPLETADO));

        // ---- avisos ----
        aviso(javier, TipoAviso.STOCK_MINIMO,
                "El Estucado brillo 135 g ha bajado del minimo: se ha preparado un pedido a Papeles Garcia");
        aviso(marta, TipoAviso.OT_FINALIZADA, "La " + ot1.getNumero() + " (Bar La Esquina) esta terminada y entregada");
        aviso(marta, TipoAviso.FACTURA_VENCIDA, "La factura F-0002 del Ayuntamiento de Villanueva esta vencida");

        log.warn("Se han creado los datos de demostracion. Los trabajadores entran con la contrasena {}", CLAVE_DEMO);
    }

    // ---- ayudantes para no repetir ----

    private BigDecimal dinero(int cantidad) {
        return BigDecimal.valueOf(cantidad);
    }

    private Puesto puesto(String nombre) {
        Puesto puesto = new Puesto();
        puesto.setNombre(nombre);
        return puestoRepositorio.save(puesto);
    }

    private Trabajador trabajador(String nombre, String email, Puesto puesto, int costeHora, RolTrabajador rol) {
        return trabajadores.guardar(new TrabajadorCrearRequest(nombre, null, null, email, CLAVE_DEMO, puesto.getId(),
                null, dinero(costeHora), LocalDate.now().minusYears(2), true, rol));
    }

    private Cliente cliente(String nombre, TipoCliente tipo, FormaPago formaPago, String email) {
        Cliente cliente = new Cliente();
        cliente.setNombreCompleto(nombre);
        cliente.setTipoCliente(tipo);
        cliente.setFormaPago(formaPago);
        cliente.setEmail(email);
        cliente.setFechaAlta(LocalDate.now().minusMonths(6));
        return clienteRepositorio.save(cliente);
    }

    private Proveedor proveedor(String nombre, String nif, String contacto) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombreCompleto(nombre);
        proveedor.setNif(nif);
        proveedor.setNombreContacto(contacto);
        proveedor.setActivo(true);
        proveedor.setFechaAlta(LocalDate.now().minusYears(3));
        return proveedorRepositorio.save(proveedor);
    }

    private Inventario material(String codigo, String nombre, TipoMaterial tipo, String unidad, int stock, int minimo,
            String precio, Proveedor proveedor) {
        Inventario material = new Inventario();
        material.setCodigo(codigo);
        material.setNombre(nombre);
        material.setTipo(tipo);
        material.setUnidadMedida(unidad);
        material.setStockActual(dinero(stock));
        material.setStockMinimo(dinero(minimo));
        material.setPrecioProducto(new BigDecimal(precio));
        material.setProveedor(proveedor);
        material.setFechaActualizacion(LocalDate.now());
        return inventarioRepositorio.save(material);
    }

    private Maquinaria maquina(String nombre, String marca, TipoMaquinaria tipo, int euroHora) {
        Maquinaria maquina = new Maquinaria();
        maquina.setNombre(nombre);
        maquina.setMarca(marca);
        maquina.setTipo(tipo);
        maquina.setEstado(EstadoMaquinaria.OPERATIVA);
        maquina.setActivo(true);
        maquina.setRentabilidad(dinero(euroHora));
        maquina.setFechaCompra(LocalDate.now().minusYears(5));
        return maquinariaRepositorio.save(maquina);
    }

    private Catalogo producto(String nombre, String descripcion, int margen, int importeMinimo) {
        Catalogo producto = new Catalogo();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setMargen(dinero(margen));
        producto.setImporteMinimo(dinero(importeMinimo));
        producto.setActivo(true);
        return catalogoRepositorio.save(producto);
    }

    private Presupuesto presupuesto(String numero, Cliente cliente, Trabajador comercial, LocalDate fecha,
            String descripcion) {
        return presupuestos.guardar(new PresupuestoRequest(numero, cliente.getId(), comercial.getId(), fecha,
                fecha.plusDays(30), descripcion, null));
    }

    // linea con sus costes: material, maquina y mano de obra (el margen sale del producto)
    private void linea(Presupuesto presupuesto, Catalogo producto, int cantidad, int material, int maquina,
            int manoObra, String horas) {
        lineas.guardar(new LineaPresupuestoRequest(presupuesto.getId(), producto.getId(), cantidad, null,
                dinero(material), dinero(maquina), dinero(manoObra), new BigDecimal(horas), null));
    }

    private void aviso(Trabajador destinatario, TipoAviso tipo, String mensaje) {
        Aviso aviso = new Aviso();
        aviso.setDestinatario(destinatario);
        aviso.setTipo(tipo);
        aviso.setMensaje(mensaje);
        aviso.setFecha(LocalDateTime.now());
        aviso.setLeido(false);
        avisoRepositorio.save(aviso);
    }
}
