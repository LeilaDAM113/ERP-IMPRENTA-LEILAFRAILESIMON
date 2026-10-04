// VALIDACION DE FORMULARIOS EN EL NAVEGADOR.
//
// Antes de llamar a la API se comprueban aqui los errores obvios (campo
// obligatorio vacio, email mal escrito, numero no numerico...). Esto NO
// sustituye la validacion del backend (que sigue siendo quien de verdad
// protege los datos): solo evita peticiones que ya sabemos que van a fallar
// y le da al usuario el error al momento, junto al campo.
//
// Lo usan tanto los formularios "a mano" (login, alta de trabajador, en
// app.js) como el motor generico de 20 entidades (crud.js), por eso vive en
// su propio fichero y se carga antes que los dos.

const EXPRESION_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

// ===== Validadores sueltos, para los formularios escritos a mano =====

function esEmailValido(valor) {
    return EXPRESION_EMAIL.test(valor.trim());
}

// Marca un campo como invalido: le pone el borde rojo y muestra el mensaje
// en el <p id="error-<idInput>"> que debe existir justo debajo en el HTML.
function marcarCampoInvalido(idInput, mensaje) {
    const input = document.getElementById(idInput);
    const error = document.getElementById(`error-${idInput}`);
    if (input) input.classList.add("campo-invalido");
    if (error) {
        error.textContent = mensaje;
        error.hidden = false;
    }
}

function limpiarCampoInvalido(idInput) {
    const input = document.getElementById(idInput);
    const error = document.getElementById(`error-${idInput}`);
    if (input) input.classList.remove("campo-invalido");
    if (error) error.hidden = true;
}

// Quita cualquier marca de error de todos los campos de un formulario
// (llamar antes de revalidar en cada envio, y al abrir/cerrar el formulario).
function limpiarErroresFormulario(formulario) {
    formulario.querySelectorAll(".campo-invalido").forEach((input) => input.classList.remove("campo-invalido"));
    formulario.querySelectorAll(".campo-error-mensaje").forEach((error) => (error.hidden = true));
}

// Pone el foco en el primer campo marcado como invalido, para que el usuario
// vea de inmediato que hay que corregir.
function enfocarPrimerCampoInvalido(formulario) {
    const primero = formulario.querySelector(".campo-invalido");
    if (primero) primero.focus();
}

// ===== Validacion del motor generico (crud.js) =====
//
// Devuelve el mensaje de error del campo, o null si es valido. `valorCrudo`
// es el valor tal cual esta en el input (string, o boolean para checkboxes).
function validarCampoGenerico(campo, valorCrudo) {
    if (campo.tipo === CAMPO.BOOLEANO) return null; // un checkbox nunca esta "vacio"

    const esVacio = valorCrudo === null || valorCrudo === undefined || valorCrudo === "";

    if (campo.requerido && esVacio) {
        return `${campo.etiqueta} es obligatorio.`;
    }
    if (esVacio) return null; // campo opcional y vacio: no hay nada que comprobar

    if (campo.tipo === CAMPO.NUMERO || campo.tipo === CAMPO.DECIMAL) {
        if (Number.isNaN(Number(valorCrudo))) {
            return `${campo.etiqueta} debe ser un numero.`;
        }
        if (campo.tipo === CAMPO.NUMERO && !Number.isInteger(Number(valorCrudo))) {
            return `${campo.etiqueta} debe ser un numero entero.`;
        }
    }

    if (campo.tipo === CAMPO.TEXTO && campo.nombre.toLowerCase().includes("email") && !esEmailValido(valorCrudo)) {
        return `${campo.etiqueta} no tiene formato de email valido.`;
    }

    return null;
}
