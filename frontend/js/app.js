// Logica de la SPA. Sin framework: varias "vistas" (login / panel /
// trabajadores / generica) que se muestran u ocultan con el atributo hidden,
// y funciones normales que tocan el DOM a mano. Para un proyecto de este
// tamano es suficiente; si crece mucho mas, ese es el momento de plantearse
// un framework.

let usuarioActual = null; // se rellena tras el login con lo que devuelve /api/me
let seccionActual = "dashboard";

// Modulos del ERP agrupados por area del negocio, para pintar la barra
// lateral. La clave "dashboard" y "trabajador" tienen vista propia (mas
// abajo); el resto se resuelve con el motor generico de js/crud.js a partir
// de su entrada correspondiente en ENTIDADES.
const GRUPOS_NAV = [
    {
        titulo: "General",
        items: [{ clave: "dashboard", etiqueta: "Panel" }],
    },
    {
        titulo: "Personal",
        items: [
            { clave: "trabajador", etiqueta: "Trabajadores" },
            { clave: "puesto", etiqueta: "Puestos" },
        ],
    },
    {
        titulo: "Clientes",
        items: [
            { clave: "cliente", etiqueta: "Clientes" },
            { clave: "empresa", etiqueta: "Empresas" },
            { clave: "particular", etiqueta: "Particulares" },
            { clave: "contactoEmpresa", etiqueta: "Contactos de empresa" },
        ],
    },
    {
        titulo: "Compras y taller",
        items: [
            { clave: "proveedor", etiqueta: "Proveedores" },
            { clave: "pedido", etiqueta: "Pedidos" },
            { clave: "inventario", etiqueta: "Inventario" },
            { clave: "maquinaria", etiqueta: "Maquinaria" },
            { clave: "catalogo", etiqueta: "Catálogo" },
        ],
    },
    {
        titulo: "Producción",
        items: [
            { clave: "ordenTrabajo", etiqueta: "Órdenes de trabajo" },
            { clave: "ordenTrabajador", etiqueta: "Horas trabajadas" },
            { clave: "ordenEmpleado", etiqueta: "Empleados en orden" },
        ],
    },
    {
        titulo: "Ventas",
        items: [
            { clave: "presupuesto", etiqueta: "Presupuestos" },
            { clave: "lineaPresupuesto", etiqueta: "Líneas de presupuesto" },
            { clave: "albaran", etiqueta: "Albaranes" },
            { clave: "lineaAlbaran", etiqueta: "Líneas de albarán" },
            { clave: "factura", etiqueta: "Facturas" },
            { clave: "lineaFactura", etiqueta: "Líneas de factura" },
            { clave: "pago", etiqueta: "Pagos" },
        ],
    },
];

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("form-login").addEventListener("submit", alEnviarLogin);
    document.getElementById("btn-logout").addEventListener("click", cerrarSesion);
    document.getElementById("btn-mostrar-alta").addEventListener("click", () => cambiarVisibilidadAlta(true));
    document.getElementById("btn-cancelar-alta").addEventListener("click", () => cambiarVisibilidadAlta(false));
    document.getElementById("form-alta").addEventListener("submit", alEnviarAlta);
    document.getElementById("cuerpo-tabla-trabajadores").addEventListener("click", alPulsarEnTabla);

    ["login-email", "login-password", "alta-nombre", "alta-email", "alta-password"].forEach((idInput) => {
        document.getElementById(idInput).addEventListener("input", () => limpiarCampoInvalido(idInput));
    });

    iniciar();
});

// Si ya habia credenciales guardadas de antes (sessionStorage sobrevive a
// recargar la pagina, aunque no a cerrar la pestana), intentamos entrar
// directamente sin volver a pedir el email/contrasena.
async function iniciar() {
    if (!hayCredenciales()) {
        mostrarLogin();
        return;
    }
    try {
        usuarioActual = await llamarApi("/me");
        mostrarApp();
    } catch {
        mostrarLogin();
    }
}

async function alEnviarLogin(evento) {
    evento.preventDefault();
    const formulario = document.getElementById("form-login");
    const email = document.getElementById("login-email").value.trim();
    const password = document.getElementById("login-password").value;
    const mensajeError = document.getElementById("login-error");
    mensajeError.hidden = true;
    limpiarErroresFormulario(formulario);

    let hayErrores = false;
    if (!email) {
        marcarCampoInvalido("login-email", "El email es obligatorio.");
        hayErrores = true;
    } else if (!esEmailValido(email)) {
        marcarCampoInvalido("login-email", "El email no tiene un formato valido.");
        hayErrores = true;
    }
    if (!password) {
        marcarCampoInvalido("login-password", "La contraseña es obligatoria.");
        hayErrores = true;
    }
    if (hayErrores) {
        enfocarPrimerCampoInvalido(formulario);
        return;
    }

    guardarCredenciales(email, password);
    try {
        usuarioActual = await llamarApi("/me");
        document.getElementById("login-password").value = "";
        mostrarApp();
    } catch (error) {
        borrarCredenciales();
        // Mensaje generico a proposito: no decimos si el email no existe, si
        // la contrasena es incorrecta o si la cuenta esta bloqueada por
        // fuerza bruta (ver LoginAttemptService), igual que hace el backend.
        mensajeError.textContent = "Email o contraseña incorrectos.";
        mensajeError.hidden = false;
    }
}

function cerrarSesion() {
    borrarCredenciales();
    usuarioActual = null;
    mostrarLogin();
}

function mostrarLogin() {
    document.getElementById("vista-app").hidden = true;
    document.getElementById("vista-login").hidden = false;
    document.getElementById("form-login").reset();
}

function mostrarApp() {
    document.getElementById("vista-login").hidden = true;
    document.getElementById("vista-app").hidden = false;

    document.getElementById("usuario-nombre").textContent = usuarioActual.nombreCompleto;
    document.getElementById("usuario-rol").textContent = usuarioActual.rol;

    const esAdmin = usuarioActual.rol === "ADMIN";
    document.getElementById("btn-mostrar-alta").hidden = !esAdmin;

    renderizarNavegacion();
    seleccionarSeccion("dashboard");
}

// Si cualquier llamada a la API devuelve 401 (credenciales invalidas o
// bloqueo por fuerza bruta activado mientras se estaba usando la app),
// volvemos a la pantalla de login en vez de dejar la app en un estado raro.
function manejarErrorApi(error, elementoMensaje) {
    if (error.status === 401) {
        mostrarLogin();
        return;
    }
    elementoMensaje.textContent = error.message;
    elementoMensaje.hidden = false;
}

// ===== Navegacion entre modulos =====

function renderizarNavegacion() {
    const contenedor = document.getElementById("navegacion");
    contenedor.innerHTML = "";

    for (const grupo of GRUPOS_NAV) {
        const divGrupo = document.createElement("div");
        divGrupo.className = "grupo-nav";

        const titulo = document.createElement("p");
        titulo.className = "titulo-grupo";
        titulo.textContent = grupo.titulo;
        divGrupo.appendChild(titulo);

        for (const item of grupo.items) {
            const boton = document.createElement("button");
            boton.type = "button";
            boton.className = "enlace-nav";
            boton.textContent = item.etiqueta;
            boton.dataset.seccion = item.clave;
            boton.addEventListener("click", () => seleccionarSeccion(item.clave));
            divGrupo.appendChild(boton);
        }

        contenedor.appendChild(divGrupo);
    }
}

function seleccionarSeccion(clave) {
    seccionActual = clave;

    document.querySelectorAll(".enlace-nav").forEach((boton) => {
        boton.classList.toggle("activo", boton.dataset.seccion === clave);
    });

    const item = GRUPOS_NAV.flatMap((g) => g.items).find((i) => i.clave === clave);
    document.getElementById("titulo-seccion").textContent = item ? item.etiqueta : "Panel";

    document.getElementById("vista-dashboard").hidden = clave !== "dashboard";
    document.getElementById("vista-trabajadores").hidden = clave !== "trabajador";
    document.getElementById("vista-generica").hidden = clave === "dashboard" || clave === "trabajador";

    if (clave === "dashboard") {
        cargarDashboard();
    } else if (clave === "trabajador") {
        cargarTrabajadores();
    } else {
        mostrarVistaGenerica(clave);
    }
}

// ===== Panel / Dashboard =====

async function cargarDashboard() {
    document.getElementById("dashboard-nombre").textContent = usuarioActual.nombreCompleto;

    const contenedorEstadisticas = document.getElementById("dashboard-estadisticas");
    contenedorEstadisticas.innerHTML = '<p class="texto-cargando">Cargando datos...</p>';

    const tarjetasEstadisticas = [
        { etiqueta: "Clientes", endpoint: "/cliente" },
        { etiqueta: "Trabajadores", endpoint: "/trabajador" },
        { etiqueta: "Presupuestos", endpoint: "/presupuesto" },
        { etiqueta: "Órdenes de trabajo", endpoint: "/orden-trabajo" },
    ];

    try {
        const resultados = await Promise.all(
            tarjetasEstadisticas.map((t) => llamarApi(t.endpoint).catch(() => []))
        );
        contenedorEstadisticas.innerHTML = tarjetasEstadisticas
            .map(
                (t, indice) => `
                    <div class="tarjeta-estadistica">
                        <div class="etiqueta-estadistica">${t.etiqueta}</div>
                        <div class="numero-estadistica">${resultados[indice].length}</div>
                    </div>
                `
            )
            .join("");
    } catch {
        contenedorEstadisticas.innerHTML = "";
    }

    const contenedorAccesos = document.getElementById("dashboard-accesos");
    contenedorAccesos.innerHTML = GRUPOS_NAV.flatMap((g) => g.items)
        .filter((item) => item.clave !== "dashboard")
        .map((item) => `<button type="button" class="acceso-rapido" data-seccion="${item.clave}">${item.etiqueta}</button>`)
        .join("");
    contenedorAccesos.querySelectorAll(".acceso-rapido").forEach((boton) => {
        boton.addEventListener("click", () => seleccionarSeccion(boton.dataset.seccion));
    });
}

// ===== Trabajadores (pantalla propia: login, roles y contrasena tienen
// logica especial que no encaja en el motor generico de crud.js) =====

async function cargarTrabajadores() {
    const cargando = document.getElementById("trabajadores-cargando");
    const tabla = document.getElementById("tabla-trabajadores");
    const mensajeError = document.getElementById("trabajadores-error");
    mensajeError.hidden = true;
    tabla.hidden = true;
    cargando.hidden = false;

    try {
        const trabajadores = await llamarApi("/trabajador");
        pintarTrabajadores(trabajadores);
        cargando.hidden = true;
        tabla.hidden = false;
    } catch (error) {
        cargando.hidden = true;
        manejarErrorApi(error, mensajeError);
    }
}

function pintarTrabajadores(trabajadores) {
    const esAdmin = usuarioActual.rol === "ADMIN";
    const cuerpo = document.getElementById("cuerpo-tabla-trabajadores");
    cuerpo.innerHTML = "";

    for (const trabajador of trabajadores) {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${escaparHtml(trabajador.nombreCompleto ?? "")}</td>
            <td>${escaparHtml(trabajador.email ?? "")}</td>
            <td>${escaparHtml(trabajador.rol ?? "")}</td>
            <td>${trabajador.activo ? "Sí" : "No"}</td>
            <td class="columna-acciones">
                ${esAdmin ? `<button class="boton-borrar" data-id="${trabajador.id}">Eliminar</button>` : ""}
            </td>
        `;
        cuerpo.appendChild(fila);
    }
}

// Delegacion de eventos: un unico listener en la tabla en vez de uno por fila
function alPulsarEnTabla(evento) {
    const boton = evento.target.closest(".boton-borrar");
    if (!boton) return;
    eliminarTrabajador(boton.dataset.id);
}

async function eliminarTrabajador(id) {
    if (!confirm("¿Seguro que quieres eliminar este trabajador?")) return;
    try {
        await llamarApi(`/trabajador/${id}`, { method: "DELETE" });
        cargarTrabajadores();
    } catch (error) {
        manejarErrorApi(error, document.getElementById("trabajadores-error"));
    }
}

function cambiarVisibilidadAlta(visible) {
    document.getElementById("tarjeta-alta").hidden = !visible;
    document.getElementById("alta-error").hidden = true;
    if (!visible) {
        const formulario = document.getElementById("form-alta");
        formulario.reset();
        limpiarErroresFormulario(formulario);
    }
}

async function alEnviarAlta(evento) {
    evento.preventDefault();
    const formulario = document.getElementById("form-alta");
    limpiarErroresFormulario(formulario);

    const nuevoTrabajador = {
        nombreCompleto: document.getElementById("alta-nombre").value.trim(),
        email: document.getElementById("alta-email").value.trim(),
        password: document.getElementById("alta-password").value,
        rol: document.getElementById("alta-rol").value,
        activo: true,
    };

    let hayErrores = false;
    if (!nuevoTrabajador.nombreCompleto) {
        marcarCampoInvalido("alta-nombre", "El nombre es obligatorio.");
        hayErrores = true;
    }
    if (!nuevoTrabajador.email) {
        marcarCampoInvalido("alta-email", "El email es obligatorio.");
        hayErrores = true;
    } else if (!esEmailValido(nuevoTrabajador.email)) {
        marcarCampoInvalido("alta-email", "El email no tiene un formato valido.");
        hayErrores = true;
    }
    if (!nuevoTrabajador.password) {
        marcarCampoInvalido("alta-password", "La contraseña es obligatoria.");
        hayErrores = true;
    } else if (nuevoTrabajador.password.length < 8) {
        marcarCampoInvalido("alta-password", "La contraseña debe tener al menos 8 caracteres.");
        hayErrores = true;
    }
    if (hayErrores) {
        enfocarPrimerCampoInvalido(formulario);
        return;
    }

    try {
        await llamarApi("/trabajador", {
            method: "POST",
            body: JSON.stringify(nuevoTrabajador),
        });
        cambiarVisibilidadAlta(false);
        cargarTrabajadores();
    } catch (error) {
        manejarErrorApi(error, document.getElementById("alta-error"));
    }
}

// Evita que un nombre/email con < > & etc. rompa el HTML de la tabla
function escaparHtml(texto) {
    const div = document.createElement("div");
    div.textContent = texto;
    return div.innerHTML;
}
