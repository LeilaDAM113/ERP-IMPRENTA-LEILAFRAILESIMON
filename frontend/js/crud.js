// MOTOR GENERICO DE CRUD.
//
// El backend tiene 21 entidades con exactamente el mismo patron de API
// (listar/obtener/crear/actualizar/eliminar, ver los controladores Java).
// En vez de escribir 20 pantallas de HTML+JS casi identicas a mano (una por
// cada entidad, cambiando solo los nombres de los campos), este fichero lee
// una lista de configuracion (ENTIDADES, mas abajo) y genera la tabla y el
// formulario de cada una sobre la marcha.
//
// Trabajador se queda FUERA de este motor y tiene su propia pantalla en
// app.js: la contrasena y el rol necesitan logica especial (cifrado,
// @PreAuthorize solo ADMIN...) que no encaja en un CRUD generico.

const CAMPO = {
    TEXTO: "texto",
    AREA: "area",
    NUMERO: "numero",
    DECIMAL: "decimal",
    FECHA: "fecha",
    BOOLEANO: "booleano",
    SELECT: "select",
    RELACION: "relacion",
};

// Intenta encontrar un nombre identificativo razonable para mostrar un
// registro relacionado (en una tabla o en las opciones de un <select>): numero
// de documento, nombre... Si no hay nada mejor, se usa el id.
function etiquetaDeRegistro(item) {
    if (!item) return "";
    return item.numero || item.nombreCompleto || item.nombreComercial || item.nombre
        || item.descripcion || `#${item.id}`;
}

const ENTIDADES = {
    cliente: {
        titulo: "Clientes",
        subtitulo: "Particulares y empresas dados de alta como clientes.",
        endpoint: "/cliente",
        campos: [
            { nombre: "nombreCompleto", etiqueta: "Nombre", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "tipoCliente", etiqueta: "Tipo de cliente", tipo: CAMPO.SELECT, opciones: ["PARTICULAR", "EMPRESA"] },
            { nombre: "telefono", etiqueta: "Teléfono", tipo: CAMPO.TEXTO },
            { nombre: "email", etiqueta: "Email", tipo: CAMPO.TEXTO },
            { nombre: "fechaAlta", etiqueta: "Fecha de alta", tipo: CAMPO.FECHA },
        ],
    },
    empresa: {
        titulo: "Empresas",
        subtitulo: "Perfil de empresa asociado a un cliente.",
        endpoint: "/empresa",
        campos: [
            { nombre: "cliente", etiqueta: "Cliente", tipo: CAMPO.RELACION, entidad: "cliente", requerido: true },
            { nombre: "cif", etiqueta: "CIF", tipo: CAMPO.TEXTO },
            { nombre: "nombreComercial", etiqueta: "Nombre comercial", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "razonSocial", etiqueta: "Razón social", tipo: CAMPO.TEXTO },
            { nombre: "direccion", etiqueta: "Dirección", tipo: CAMPO.TEXTO },
            { nombre: "ciudad", etiqueta: "Ciudad", tipo: CAMPO.TEXTO },
            { nombre: "provincia", etiqueta: "Provincia", tipo: CAMPO.TEXTO },
            { nombre: "codigoPostal", etiqueta: "Código postal", tipo: CAMPO.TEXTO },
            { nombre: "telefono", etiqueta: "Teléfono", tipo: CAMPO.TEXTO },
            { nombre: "email", etiqueta: "Email", tipo: CAMPO.TEXTO },
            { nombre: "web", etiqueta: "Web", tipo: CAMPO.TEXTO },
        ],
    },
    particular: {
        titulo: "Particulares",
        subtitulo: "Perfil de particular asociado a un cliente.",
        endpoint: "/particular",
        campos: [
            { nombre: "cliente", etiqueta: "Cliente", tipo: CAMPO.RELACION, entidad: "cliente", requerido: true },
            { nombre: "dni", etiqueta: "DNI", tipo: CAMPO.TEXTO },
            { nombre: "nombre", etiqueta: "Nombre", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "apellido", etiqueta: "Apellidos", tipo: CAMPO.TEXTO },
            { nombre: "direccion", etiqueta: "Dirección", tipo: CAMPO.TEXTO },
            { nombre: "ciudad", etiqueta: "Ciudad", tipo: CAMPO.TEXTO },
            { nombre: "provincia", etiqueta: "Provincia", tipo: CAMPO.TEXTO },
            { nombre: "codigoPostal", etiqueta: "Código postal", tipo: CAMPO.TEXTO },
            { nombre: "email", etiqueta: "Email", tipo: CAMPO.TEXTO },
        ],
    },
    contactoEmpresa: {
        titulo: "Contactos de empresa",
        subtitulo: "Personas de contacto dentro de una empresa cliente.",
        endpoint: "/contacto-empresa",
        campos: [
            { nombre: "empresa", etiqueta: "Empresa", tipo: CAMPO.RELACION, entidad: "empresa", requerido: true },
            { nombre: "nombreCompleto", etiqueta: "Nombre", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "departamento", etiqueta: "Departamento", tipo: CAMPO.TEXTO },
            { nombre: "telefono", etiqueta: "Teléfono", tipo: CAMPO.TEXTO },
            { nombre: "email", etiqueta: "Email", tipo: CAMPO.TEXTO },
        ],
    },
    puesto: {
        titulo: "Puestos",
        subtitulo: "Puestos de trabajo que se pueden asignar a un trabajador.",
        endpoint: "/puesto",
        campos: [
            { nombre: "nombre", etiqueta: "Nombre", tipo: CAMPO.TEXTO, requerido: true },
        ],
    },
    proveedor: {
        titulo: "Proveedores",
        subtitulo: "Proveedores de materiales y servicios.",
        endpoint: "/proveedor",
        campos: [
            { nombre: "nombreCompleto", etiqueta: "Nombre", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "nif", etiqueta: "NIF", tipo: CAMPO.TEXTO },
            { nombre: "nombreContacto", etiqueta: "Persona de contacto", tipo: CAMPO.TEXTO },
            { nombre: "telefono", etiqueta: "Teléfono", tipo: CAMPO.TEXTO },
            { nombre: "email", etiqueta: "Email", tipo: CAMPO.TEXTO },
            { nombre: "direccion", etiqueta: "Dirección", tipo: CAMPO.TEXTO },
            { nombre: "ciudad", etiqueta: "Ciudad", tipo: CAMPO.TEXTO },
            { nombre: "provincia", etiqueta: "Provincia", tipo: CAMPO.TEXTO },
            { nombre: "codigoPostal", etiqueta: "Código postal", tipo: CAMPO.TEXTO },
            { nombre: "pais", etiqueta: "País", tipo: CAMPO.TEXTO },
            { nombre: "web", etiqueta: "Web", tipo: CAMPO.TEXTO },
            { nombre: "producto", etiqueta: "Producto que suministra", tipo: CAMPO.RELACION, entidad: "catalogo" },
            { nombre: "fechaAlta", etiqueta: "Fecha de alta", tipo: CAMPO.FECHA },
            { nombre: "activo", etiqueta: "Activo", tipo: CAMPO.BOOLEANO },
            { nombre: "observaciones", etiqueta: "Observaciones", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    maquinaria: {
        titulo: "Maquinaria",
        subtitulo: "Máquinas del taller.",
        endpoint: "/maquinaria",
        campos: [
            { nombre: "nombre", etiqueta: "Nombre", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "marca", etiqueta: "Marca", tipo: CAMPO.TEXTO },
            { nombre: "modelo", etiqueta: "Modelo", tipo: CAMPO.TEXTO },
            { nombre: "numeroSerie", etiqueta: "Número de serie", tipo: CAMPO.TEXTO },
            { nombre: "tipo", etiqueta: "Tipo", tipo: CAMPO.SELECT, opciones: ["OFFSET", "DIGITAL", "GRAN_FORMATO", "ENCUADERNACION", "ACABADOS", "CORTE"] },
            { nombre: "estado", etiqueta: "Estado", tipo: CAMPO.SELECT, opciones: ["OPERATIVA", "EN_MANTENIMIENTO", "AVERIADA", "FUERA_DE_SERVICIO"] },
            { nombre: "fechaCompra", etiqueta: "Fecha de compra", tipo: CAMPO.FECHA },
            { nombre: "precioCompra", etiqueta: "Precio de compra (€)", tipo: CAMPO.DECIMAL },
            { nombre: "rentabilidad", etiqueta: "Rentabilidad", tipo: CAMPO.DECIMAL },
            { nombre: "ultimaRevision", etiqueta: "Última revisión", tipo: CAMPO.FECHA },
            { nombre: "activo", etiqueta: "Activa", tipo: CAMPO.BOOLEANO },
            { nombre: "observaciones", etiqueta: "Observaciones", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    inventario: {
        titulo: "Inventario",
        subtitulo: "Materiales y existencias en almacén.",
        endpoint: "/inventario",
        campos: [
            { nombre: "nombre", etiqueta: "Nombre", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "categoria", etiqueta: "Categoría", tipo: CAMPO.TEXTO },
            { nombre: "proveedor", etiqueta: "Proveedor", tipo: CAMPO.RELACION, entidad: "proveedor" },
            { nombre: "cantidad", etiqueta: "Cantidad", tipo: CAMPO.NUMERO },
            { nombre: "stockActual", etiqueta: "Stock actual", tipo: CAMPO.NUMERO },
            { nombre: "stockMinimo", etiqueta: "Stock mínimo", tipo: CAMPO.NUMERO },
            { nombre: "precioProducto", etiqueta: "Precio (€)", tipo: CAMPO.DECIMAL },
            { nombre: "fechaActualizacion", etiqueta: "Última actualización", tipo: CAMPO.FECHA },
            { nombre: "descripcion", etiqueta: "Descripción", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    catalogo: {
        titulo: "Catálogo",
        subtitulo: "Productos y servicios que se pueden vender.",
        endpoint: "/catalogo",
        campos: [
            { nombre: "descripcion", etiqueta: "Descripción", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "precioVenta", etiqueta: "Precio de venta (€)", tipo: CAMPO.DECIMAL },
        ],
    },
    pedido: {
        titulo: "Pedidos",
        subtitulo: "Pedidos de material a proveedores.",
        endpoint: "/pedido",
        campos: [
            { nombre: "proveedor", etiqueta: "Proveedor", tipo: CAMPO.RELACION, entidad: "proveedor" },
            { nombre: "inventario", etiqueta: "Material", tipo: CAMPO.RELACION, entidad: "inventario" },
            { nombre: "cantidad", etiqueta: "Cantidad", tipo: CAMPO.NUMERO },
            { nombre: "importe", etiqueta: "Importe (€)", tipo: CAMPO.DECIMAL },
            { nombre: "estado", etiqueta: "Estado", tipo: CAMPO.SELECT, opciones: ["PENDIENTE", "ENVIADO", "RECIBIDO", "CANCELADO"] },
            { nombre: "fechaPedido", etiqueta: "Fecha de pedido", tipo: CAMPO.FECHA },
            { nombre: "descripcion", etiqueta: "Descripción", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    ordenTrabajo: {
        titulo: "Órdenes de trabajo",
        subtitulo: "Trabajos en curso o planificados en el taller.",
        endpoint: "/orden-trabajo",
        campos: [
            { nombre: "numero", etiqueta: "Número", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "titulo", etiqueta: "Título", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "presupuesto", etiqueta: "Presupuesto de origen", tipo: CAMPO.RELACION, entidad: "presupuesto" },
            { nombre: "trabajador", etiqueta: "Trabajador responsable", tipo: CAMPO.RELACION, entidad: "trabajador" },
            { nombre: "prioridad", etiqueta: "Prioridad", tipo: CAMPO.SELECT, opciones: ["BAJA", "MEDIA", "ALTA"] },
            { nombre: "estado", etiqueta: "Estado", tipo: CAMPO.SELECT, opciones: ["PENDIENTE", "EN_PROCESO", "PAUSADA", "COMPLETADA", "CANCELADA"] },
            { nombre: "fechaInicio", etiqueta: "Fecha de inicio", tipo: CAMPO.FECHA },
            { nombre: "fechaFin", etiqueta: "Fecha de fin", tipo: CAMPO.FECHA },
            { nombre: "descripcion", etiqueta: "Descripción", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    ordenTrabajador: {
        titulo: "Horas trabajadas",
        subtitulo: "Horas reales que ha dedicado cada trabajador a una orden.",
        endpoint: "/orden-trabajador",
        campos: [
            { nombre: "ordenTrabajo", etiqueta: "Orden de trabajo", tipo: CAMPO.RELACION, entidad: "ordenTrabajo", requerido: true },
            { nombre: "trabajador", etiqueta: "Trabajador", tipo: CAMPO.RELACION, entidad: "trabajador", requerido: true },
            { nombre: "horasReales", etiqueta: "Horas reales", tipo: CAMPO.DECIMAL },
            { nombre: "observaciones", etiqueta: "Observaciones", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    ordenEmpleado: {
        titulo: "Empleados en orden",
        subtitulo: "Qué trabajadores están asignados a cada orden de trabajo.",
        endpoint: "/orden-empleado",
        campos: [
            { nombre: "ordenTrabajo", etiqueta: "Orden de trabajo", tipo: CAMPO.RELACION, entidad: "ordenTrabajo", requerido: true },
            { nombre: "trabajador", etiqueta: "Trabajador", tipo: CAMPO.RELACION, entidad: "trabajador", requerido: true },
        ],
    },
    presupuesto: {
        titulo: "Presupuestos",
        subtitulo: "Presupuestos enviados a clientes.",
        endpoint: "/presupuesto",
        campos: [
            { nombre: "cliente", etiqueta: "Cliente", tipo: CAMPO.RELACION, entidad: "cliente", requerido: true },
            { nombre: "estado", etiqueta: "Estado", tipo: CAMPO.SELECT, opciones: ["BORRADOR", "ACEPTADO", "RECHAZADO"] },
            { nombre: "fechaEmision", etiqueta: "Fecha de emisión", tipo: CAMPO.FECHA },
            { nombre: "fechaValidez", etiqueta: "Válido hasta", tipo: CAMPO.FECHA },
            { nombre: "subtotal", etiqueta: "Subtotal (€)", tipo: CAMPO.DECIMAL },
            { nombre: "importe", etiqueta: "Importe total (€)", tipo: CAMPO.DECIMAL },
            { nombre: "descripcion", etiqueta: "Descripción", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    lineaPresupuesto: {
        titulo: "Líneas de presupuesto",
        subtitulo: "Productos incluidos en cada presupuesto.",
        endpoint: "/linea-presupuesto",
        campos: [
            { nombre: "presupuesto", etiqueta: "Presupuesto", tipo: CAMPO.RELACION, entidad: "presupuesto", requerido: true },
            { nombre: "producto", etiqueta: "Producto", tipo: CAMPO.RELACION, entidad: "catalogo", requerido: true },
            { nombre: "cantidad", etiqueta: "Cantidad", tipo: CAMPO.NUMERO },
            { nombre: "precioUnitario", etiqueta: "Precio unitario (€)", tipo: CAMPO.DECIMAL },
            { nombre: "subtotal", etiqueta: "Subtotal (€)", tipo: CAMPO.DECIMAL },
            { nombre: "total", etiqueta: "Total (€)", tipo: CAMPO.DECIMAL },
        ],
    },
    factura: {
        titulo: "Facturas",
        subtitulo: "Facturas emitidas a clientes.",
        endpoint: "/factura",
        campos: [
            { nombre: "numero", etiqueta: "Número", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "ordenTrabajo", etiqueta: "Orden de trabajo", tipo: CAMPO.RELACION, entidad: "ordenTrabajo" },
            { nombre: "estado", etiqueta: "Estado", tipo: CAMPO.SELECT, opciones: ["PENDIENTE", "PAGADA", "VENCIDA", "CANCELADA"] },
            { nombre: "metodoPago", etiqueta: "Método de pago", tipo: CAMPO.SELECT, opciones: ["EFECTIVO", "TARJETA", "TRANSFERENCIA", "DOMICILIACION"] },
            { nombre: "total", etiqueta: "Total (€)", tipo: CAMPO.DECIMAL },
            { nombre: "fechaEmision", etiqueta: "Fecha de emisión", tipo: CAMPO.FECHA },
            { nombre: "fechaPago", etiqueta: "Fecha de pago", tipo: CAMPO.FECHA },
        ],
    },
    lineaFactura: {
        titulo: "Líneas de factura",
        subtitulo: "Productos incluidos en cada factura.",
        endpoint: "/linea-factura",
        campos: [
            { nombre: "factura", etiqueta: "Factura", tipo: CAMPO.RELACION, entidad: "factura", requerido: true },
            { nombre: "producto", etiqueta: "Producto", tipo: CAMPO.RELACION, entidad: "catalogo" },
            { nombre: "lineaPresupuesto", etiqueta: "Línea de presupuesto de origen", tipo: CAMPO.RELACION, entidad: "lineaPresupuesto" },
            { nombre: "lineaAlbaran", etiqueta: "Línea de albarán de origen", tipo: CAMPO.RELACION, entidad: "lineaAlbaran" },
            { nombre: "cantidad", etiqueta: "Cantidad", tipo: CAMPO.NUMERO },
        ],
    },
    albaran: {
        titulo: "Albaranes",
        subtitulo: "Entregas realizadas a clientes.",
        endpoint: "/albaran",
        campos: [
            { nombre: "numero", etiqueta: "Número", tipo: CAMPO.TEXTO, requerido: true },
            { nombre: "cliente", etiqueta: "Cliente", tipo: CAMPO.RELACION, entidad: "cliente" },
            { nombre: "ordenTrabajo", etiqueta: "Orden de trabajo", tipo: CAMPO.RELACION, entidad: "ordenTrabajo" },
            { nombre: "estado", etiqueta: "Estado", tipo: CAMPO.SELECT, opciones: ["PENDIENTE", "ENTREGADO", "CANCELADO"] },
            { nombre: "fechaEmision", etiqueta: "Fecha de emisión", tipo: CAMPO.FECHA },
            { nombre: "fechaEntrega", etiqueta: "Fecha de entrega", tipo: CAMPO.FECHA },
            { nombre: "observaciones", etiqueta: "Observaciones", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
    lineaAlbaran: {
        titulo: "Líneas de albarán",
        subtitulo: "Productos entregados en cada albarán.",
        endpoint: "/linea-albaran",
        campos: [
            { nombre: "albaran", etiqueta: "Albarán", tipo: CAMPO.RELACION, entidad: "albaran", requerido: true },
            { nombre: "producto", etiqueta: "Producto", tipo: CAMPO.RELACION, entidad: "catalogo" },
            { nombre: "cantidad", etiqueta: "Cantidad", tipo: CAMPO.NUMERO },
        ],
    },
    pago: {
        titulo: "Pagos",
        subtitulo: "Cobros y pagos asociados a facturas, órdenes, proveedores o inventario.",
        endpoint: "/pago",
        campos: [
            { nombre: "factura", etiqueta: "Factura", tipo: CAMPO.RELACION, entidad: "factura" },
            { nombre: "ordenTrabajo", etiqueta: "Orden de trabajo", tipo: CAMPO.RELACION, entidad: "ordenTrabajo" },
            { nombre: "proveedor", etiqueta: "Proveedor", tipo: CAMPO.RELACION, entidad: "proveedor" },
            { nombre: "inventario", etiqueta: "Material", tipo: CAMPO.RELACION, entidad: "inventario" },
            { nombre: "importe", etiqueta: "Importe (€)", tipo: CAMPO.DECIMAL },
            { nombre: "estado", etiqueta: "Estado", tipo: CAMPO.SELECT, opciones: ["PENDIENTE", "COMPLETADO", "CANCELADO"] },
            { nombre: "metodoPago", etiqueta: "Método de pago", tipo: CAMPO.SELECT, opciones: ["EFECTIVO", "TARJETA", "TRANSFERENCIA", "DOMICILIACION"] },
            { nombre: "fechaEmision", etiqueta: "Fecha de emisión", tipo: CAMPO.FECHA },
            { nombre: "fechaPago", etiqueta: "Fecha de pago", tipo: CAMPO.FECHA },
            { nombre: "descripcion", etiqueta: "Descripción", tipo: CAMPO.AREA, ancho: "completo" },
        ],
    },
};

// --- Estado de la vista generica actualmente abierta ---
let seccionGenericaActual = null;
let idEnEdicionGenerica = null;
// Cache muy simple de listas usadas para poblar los <select> de relaciones,
// para no volver a pedir /api/cliente cada vez que se abre un formulario
// dentro de la misma sesion. Se borra la entrada de una entidad cuando se
// crea/edita un registro suyo, para que el select se actualice con el ultimo dato.
const cacheOpcionesRelacion = {};

async function mostrarVistaGenerica(clave) {
    seccionGenericaActual = clave;
    const config = ENTIDADES[clave];

    document.getElementById("generica-titulo").textContent = config.titulo;
    document.getElementById("generica-subtitulo").textContent = config.subtitulo || "";
    ocultarFormularioGenerico();

    await cargarListadoGenerico();
}

async function cargarListadoGenerico() {
    const config = ENTIDADES[seccionGenericaActual];
    const cargando = document.getElementById("generica-cargando");
    const vacio = document.getElementById("generica-vacio");
    const tabla = document.getElementById("generica-tabla");
    const error = document.getElementById("generica-error");

    error.hidden = true;
    vacio.hidden = true;
    tabla.hidden = true;
    cargando.hidden = false;

    try {
        const registros = await llamarApi(config.endpoint);
        cargando.hidden = true;
        if (registros.length === 0) {
            vacio.hidden = false;
            return;
        }
        renderizarTablaGenerica(config, registros);
        tabla.hidden = false;
    } catch (error2) {
        cargando.hidden = true;
        manejarErrorApi(error2, error);
    }
}

function renderizarTablaGenerica(config, registros) {
    const thead = document.getElementById("generica-thead");
    const tbody = document.getElementById("generica-tbody");

    thead.innerHTML = `<tr>${config.campos.map((c) => `<th>${c.etiqueta}</th>`).join("")}<th></th></tr>`;

    tbody.innerHTML = "";
    for (const registro of registros) {
        const fila = document.createElement("tr");
        const celdas = config.campos
            .map((campo) => `<td>${escaparHtml(String(valorParaTabla(campo, registro)))}</td>`)
            .join("");
        fila.innerHTML = `
            ${celdas}
            <td class="columna-acciones">
                <button class="boton-editar" data-id="${registro.id}">Editar</button>
                <button class="boton-borrar" data-id="${registro.id}">Eliminar</button>
            </td>
        `;
        tbody.appendChild(fila);
    }
}

function valorParaTabla(campo, registro) {
    const valor = registro[campo.nombre];
    if (campo.tipo === CAMPO.RELACION) return valor ? etiquetaDeRegistro(valor) : "—";
    if (campo.tipo === CAMPO.BOOLEANO) return valor ? "Sí" : "No";
    if (valor === null || valor === undefined || valor === "") return "—";
    return valor;
}

// --- Formulario (crear / editar) ---

async function mostrarFormularioGenerico(idExistente) {
    idEnEdicionGenerica = idExistente || null;
    const config = ENTIDADES[seccionGenericaActual];
    const tarjeta = document.getElementById("generica-tarjeta-formulario");
    const tituloForm = document.getElementById("generica-form-titulo");
    const contenedorCampos = document.getElementById("generica-form-campos");
    const botonGuardar = document.getElementById("generica-form-guardar");
    const errorForm = document.getElementById("generica-form-error");

    errorForm.hidden = true;
    tituloForm.textContent = idEnEdicionGenerica
        ? `Editar registro de ${config.titulo.toLowerCase()}`
        : `Nuevo registro en ${config.titulo.toLowerCase()}`;
    botonGuardar.textContent = idEnEdicionGenerica ? "Guardar cambios" : "Crear";

    contenedorCampos.innerHTML = '<p class="texto-cargando">Cargando formulario...</p>';
    tarjeta.hidden = false;
    tarjeta.scrollIntoView({ behavior: "smooth", block: "nearest" });

    const registroExistente = idEnEdicionGenerica
        ? await llamarApi(`${config.endpoint}/${idEnEdicionGenerica}`)
        : null;

    contenedorCampos.innerHTML = "";
    for (const campo of config.campos) {
        contenedorCampos.appendChild(await construirCampoFormulario(campo, registroExistente));
    }
}

function ocultarFormularioGenerico() {
    document.getElementById("generica-tarjeta-formulario").hidden = true;
    idEnEdicionGenerica = null;
}

async function construirCampoFormulario(campo, registroExistente) {
    const contenedor = document.createElement("div");
    const valorActual = registroExistente ? registroExistente[campo.nombre] : null;
    const idInput = `campo-${campo.nombre}`;

    // Los checkboxes se maquetan distinto (casilla + etiqueta en la misma linea)
    if (campo.tipo === CAMPO.BOOLEANO) {
        contenedor.className = "campo-booleano" + (campo.ancho === "completo" ? " campo-ancho-completo" : "");
        contenedor.innerHTML = `
            <input type="checkbox" id="${idInput}" data-campo="${campo.nombre}" data-tipo="${campo.tipo}" ${valorActual ? "checked" : ""}>
            <label for="${idInput}">${campo.etiqueta}</label>
        `;
        return contenedor;
    }

    if (campo.ancho === "completo") contenedor.className = "campo-ancho-completo";

    const etiqueta = document.createElement("label");
    etiqueta.setAttribute("for", idInput);
    etiqueta.textContent = campo.etiqueta;
    contenedor.appendChild(etiqueta);

    let input;
    if (campo.tipo === CAMPO.AREA) {
        input = document.createElement("textarea");
        input.value = valorActual ?? "";
    } else if (campo.tipo === CAMPO.SELECT) {
        input = document.createElement("select");
        input.innerHTML =
            `<option value="">Sin especificar</option>` +
            campo.opciones.map((o) => `<option value="${o}" ${o === valorActual ? "selected" : ""}>${o}</option>`).join("");
    } else if (campo.tipo === CAMPO.RELACION) {
        input = document.createElement("select");
        const opciones = await obtenerOpcionesRelacion(campo.entidad);
        const idSeleccionado = valorActual ? valorActual.id : "";
        input.innerHTML =
            `<option value="">Sin asignar</option>` +
            opciones
                .map((o) => `<option value="${o.id}" ${String(o.id) === String(idSeleccionado) ? "selected" : ""}>${escaparHtml(etiquetaDeRegistro(o))}</option>`)
                .join("");
    } else {
        input = document.createElement("input");
        input.type = campo.tipo === CAMPO.FECHA ? "date" : campo.tipo === CAMPO.NUMERO || campo.tipo === CAMPO.DECIMAL ? "number" : "text";
        if (campo.tipo === CAMPO.DECIMAL) input.step = "0.01";
        input.value = valorActual ?? "";
    }

    input.id = idInput;
    input.dataset.campo = campo.nombre;
    input.dataset.tipo = campo.tipo;
    if (campo.requerido) input.required = true;
    input.addEventListener("input", () => limpiarCampoInvalido(idInput));
    contenedor.appendChild(input);

    const errorCampo = document.createElement("p");
    errorCampo.id = `error-${idInput}`;
    errorCampo.className = "campo-error-mensaje";
    errorCampo.hidden = true;
    contenedor.appendChild(errorCampo);

    return contenedor;
}

async function obtenerOpcionesRelacion(claveEntidad) {
    if (!cacheOpcionesRelacion[claveEntidad]) {
        cacheOpcionesRelacion[claveEntidad] = await llamarApi(ENTIDADES[claveEntidad].endpoint);
    }
    return cacheOpcionesRelacion[claveEntidad];
}

async function alEnviarFormularioGenerico(evento) {
    evento.preventDefault();
    const config = ENTIDADES[seccionGenericaActual];
    const formulario = document.getElementById("generica-form");
    const errorForm = document.getElementById("generica-form-error");
    errorForm.hidden = true;
    limpiarErroresFormulario(formulario);

    // Primera pasada: valida todos los campos ANTES de llamar a la API. Si
    // algo no es valido no se manda nada, se marcan los campos con error y
    // se corta aqui.
    let hayErrores = false;
    for (const campo of config.campos) {
        const input = document.getElementById(`campo-${campo.nombre}`);
        const valorCrudo = campo.tipo === CAMPO.BOOLEANO ? input.checked : input.value;
        const mensaje = validarCampoGenerico(campo, valorCrudo);
        if (mensaje) {
            marcarCampoInvalido(input.id, mensaje);
            hayErrores = true;
        }
    }
    if (hayErrores) {
        errorForm.textContent = "Revisa los campos marcados en rojo.";
        errorForm.hidden = false;
        enfocarPrimerCampoInvalido(formulario);
        return;
    }

    const payload = {};
    for (const campo of config.campos) {
        const input = document.getElementById(`campo-${campo.nombre}`);
        if (campo.tipo === CAMPO.BOOLEANO) {
            payload[campo.nombre] = input.checked;
        } else if (campo.tipo === CAMPO.RELACION) {
            payload[campo.nombre] = input.value ? { id: Number(input.value) } : null;
        } else if (campo.tipo === CAMPO.NUMERO || campo.tipo === CAMPO.DECIMAL) {
            payload[campo.nombre] = input.value === "" ? null : Number(input.value);
        } else {
            payload[campo.nombre] = input.value === "" ? null : input.value;
        }
    }

    try {
        if (idEnEdicionGenerica) {
            await llamarApi(`${config.endpoint}/${idEnEdicionGenerica}`, { method: "PUT", body: JSON.stringify(payload) });
        } else {
            await llamarApi(config.endpoint, { method: "POST", body: JSON.stringify(payload) });
        }
        delete cacheOpcionesRelacion[seccionGenericaActual]; // otro formulario puede usar esta entidad como relacion
        ocultarFormularioGenerico();
        cargarListadoGenerico();
    } catch (error) {
        manejarErrorApi(error, errorForm);
    }
}

function alPulsarEnTablaGenerica(evento) {
    const botonEditar = evento.target.closest(".boton-editar");
    if (botonEditar) {
        mostrarFormularioGenerico(botonEditar.dataset.id);
        return;
    }
    const botonBorrar = evento.target.closest(".boton-borrar");
    if (botonBorrar) {
        eliminarRegistroGenerico(botonBorrar.dataset.id);
    }
}

async function eliminarRegistroGenerico(id) {
    if (!confirm("¿Seguro que quieres eliminar este registro?")) return;
    const config = ENTIDADES[seccionGenericaActual];
    try {
        await llamarApi(`${config.endpoint}/${id}`, { method: "DELETE" });
        delete cacheOpcionesRelacion[seccionGenericaActual];
        cargarListadoGenerico();
    } catch (error) {
        manejarErrorApi(error, document.getElementById("generica-error"));
    }
}

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("generica-btn-nuevo").addEventListener("click", () => mostrarFormularioGenerico(null));
    document.getElementById("generica-form-cancelar").addEventListener("click", ocultarFormularioGenerico);
    document.getElementById("generica-form").addEventListener("submit", alEnviarFormularioGenerico);
    document.getElementById("generica-tbody").addEventListener("click", alPulsarEnTablaGenerica);
});
