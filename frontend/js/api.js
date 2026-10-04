// Cliente HTTP muy pequeño para hablar con la API (/api/...).
// El backend usa autenticacion Basic (email + contrasena en cada peticion,
// ver SecurityConfig.java), no cookies ni tokens JWT. Por eso aqui no hay
// "sesion" del lado del servidor: guardamos el email y la contrasena
// codificados en Base64 en sessionStorage (se borran solos al cerrar la
// pestaña) y se los mandamos en la cabecera Authorization en cada llamada.
//
// OJO: Base64 NO es cifrado, es solo una codificacion reversible. Esto es
// aceptable en local bajo HTTPS (que es como deberia servirse esto siempre
// que no sea localhost), pero si mas adelante se quiere algo mas robusto,
// el paso natural es cambiar el backend a login con JWT.

const CLAVE_CREDENCIALES = "erp-imprenta-credenciales";

function guardarCredenciales(email, password) {
    sessionStorage.setItem(CLAVE_CREDENCIALES, btoa(`${email}:${password}`));
}

function borrarCredenciales() {
    sessionStorage.removeItem(CLAVE_CREDENCIALES);
}

function hayCredenciales() {
    return sessionStorage.getItem(CLAVE_CREDENCIALES) !== null;
}

// Error propio para poder distinguir "la API respondio con un fallo" de un
// error de red, y para que quien llame pueda mirar error.status.
class ErrorApi extends Error {
    constructor(status, mensaje) {
        super(mensaje);
        this.status = status;
    }
}

// Llama a /api/<ruta>. `opciones` admite lo mismo que fetch (method, body...).
// El body, si viene, se manda ya convertido a JSON.
async function llamarApi(ruta, opciones = {}) {
    const credenciales = sessionStorage.getItem(CLAVE_CREDENCIALES);
    const cabeceras = { ...opciones.headers };

    if (credenciales) {
        cabeceras["Authorization"] = `Basic ${credenciales}`;
    }
    if (opciones.body) {
        cabeceras["Content-Type"] = "application/json";
    }

    const respuesta = await fetch(`/api${ruta}`, { ...opciones, headers: cabeceras });

    if (respuesta.status === 401) {
        borrarCredenciales();
    }

    if (!respuesta.ok) {
        throw new ErrorApi(respuesta.status, await extraerMensajeError(respuesta));
    }

    const texto = await respuesta.text();
    return texto ? JSON.parse(texto) : null;
}

// Intenta sacar un mensaje legible del error que devuelve la API.
// - Validaciones (@Valid) devuelven un array "errors" con un mensaje cada una.
// - El resto de errores devuelven un campo "message".
async function extraerMensajeError(respuesta) {
    try {
        const cuerpo = await respuesta.json();
        if (Array.isArray(cuerpo.errors) && cuerpo.errors.length > 0) {
            return cuerpo.errors.map((error) => error.defaultMessage).join(". ");
        }
        return cuerpo.message || `Error ${respuesta.status}`;
    } catch {
        return `Error ${respuesta.status}`;
    }
}
