# Plan completo del TFG — ERP de imprenta (DAM2)

> Hoja de ruta paso a paso, del estado actual a la defensa. Marca cada casilla `[x]` al terminarla.
> Escrito el 7/10/2026 tras auditar el repositorio (`backend/`, `cliente/`, `frontend/`) y las 196 capturas de Sprinta de `sprinta.zip`. Actualizado el 10/10/2026 con tus respuestas.

---

## ⭐ EMPIEZA AQUÍ — qué hago y en qué orden

Tienes **dos caminos en paralelo** y los dos cuentan para la nota:
- 📝 **Memoria** (lo que entregas por escrito) → guía en [`memoria/GUIA_ENTREGAS_1_Y_2.md`](memoria/GUIA_ENTREGAS_1_Y_2.md).
- 💻 **Código** (backend + escritorio + móvil) → las fases de este documento.

**Regla para no agobiarte:** cuando se acerque una entrega de la memoria, esa semana la memoria va primero. El resto del tiempo, programa por la tarde y escribe la memoria un rato cada día.

### Decisiones ya tomadas (no hace falta volver a pensarlas)
| Tema | Decisión | Por qué |
|---|---|---|
| Escritorio | **JavaFX** | Lo has dado en clase, es Java (igual que el servidor) y funciona en Windows y macOS. C#/WPF también lo diste, pero así todo el proyecto usa un solo lenguaje. |
| Móvil | **Android Studio con Java** (pantallas en XML) | Llevas dos cursos de Java y te piden usar lo que has aprendido. ⚠️ **Solo si en clase de móvil (PMDM) programasteis en Kotlin, usa Kotlin.** Si no lo recuerdas, mira tus prácticas de esa asignatura: el lenguaje que usasteis allí es el que tienes que usar. |
| Base de datos | **MySQL** | Es la que has usado en el curso. |
| Servidor | **Tu portátil** (ver la explicación en la [Fase 7](#fase-7--despliegue-e-instaladores)) | Lo que te dijo tu profesor. |
| Login | **JWT** | Fase 1.2. |
| IA | Lo que ya está hecho con IA se queda. A partir de ahora la usas solo para **dudas y para lo que no sepas hacer**, y lo declaras en la memoria (ver [Anexo C](#anexo-c--cómo-hacer-que-se-note-que-lo-has-hecho-tú)). | Lo que te dijo tu profesor. |

### Cómo está el código hoy (10/10/2026)
- ✅ Vuelve a compilar (has quitado la `/` de `RolTrabajador`).
- ✅ Has añadido Swagger (`springdoc-openapi` 3.1.1) y DTOs para Trabajador (`dto/`).
- 🔴 **OJO:** ahora mismo `SecurityConfig` tiene `.anyRequest().permitAll()` y los `@PreAuthorize` de `TrabajadorController` están comentados → **cualquiera puede usar toda la API sin iniciar sesión**. Vale para probar, pero apúntalo: se arregla en la Fase 1.2 y **no lo subas así a `main`**.
- ⏳ Todo lo demás de las fases sigue pendiente.

### El orden, paso a paso
1. 📝 **Entrega 1** de la memoria (portada, índice, resumen, introducción, objetivos). → Guía, parte 1.
2. 💻 **Fase 1**: login con JWT y volver a proteger la API.
3. 📝 **Entrega 2** de la memoria (contexto, estado del arte, innovación, DAFO, requisitos). → Guía, parte 2.
   - Los **requisitos** que escribas aquí son la lista de lo que vas a programar: el código de las fases siguientes tiene que cumplirlos.
4. 💻 **Fase 2**: dejar el backend bien hecho (errores, DTOs, roles y modelo de datos completo).
5. 🎨 **Diseño**: bocetos de las pantallas de escritorio y móvil (tu cuaderno decía "diseño escritorio y móvil, 3 días"). Te servirán para la siguiente entrega de la memoria, que será probablemente la de *Diseño*.
6. 💻 **Fase 3**: probar la API con Swagger y borrar la web.
7. 💻 **Fase 4**: prueba pequeña con JavaFX (login + tabla de clientes) → enséñasela a tu profesora.
8. 💻 **Fase 5**: escritorio completo, módulo a módulo (en el orden de tu cuaderno: trabajadores con rol → almacén y stock mínimo → maquinaria → clientes → presupuestos → órdenes de trabajo → albaranes → facturas → estadísticas).
9. 💻 **Fase 6**: móvil (taller y reparto).
10. 💻 **Fase 7**: dejar el servidor funcionando para la defensa.
11. 📝 **Fase 8**: completar la memoria, pruebas y defensa.

> 🗓️ Cuando sepas las fechas de cada entrega, escríbelas aquí: Entrega 1: ____ · Entrega 2: ____ · Siguientes: ____ · Defensa: ____

---

## Índice

0. [Resumen: cómo está el proyecto hoy](#0-resumen-cómo-está-el-proyecto-hoy)
1. [Fase 0 — Decisiones antes de programar](#fase-0--decisiones-antes-de-programar)
2. [Fase 1 — Arreglar lo que está roto (urgente)](#fase-1--arreglar-lo-que-está-roto-urgente)
3. [Fase 2 — Asentar la arquitectura del backend](#fase-2--asentar-la-arquitectura-del-backend)
4. [Fase 3 — Probar sin la web y eliminarla](#fase-3--probar-sin-la-web-y-eliminarla)
5. [Fase 4 — Prueba pequeña de escritorio](#fase-4--prueba-pequeña-de-escritorio)
6. [Fase 5 — Aplicación de escritorio completa](#fase-5--aplicación-de-escritorio-completa)
7. [Fase 6 — Aplicación móvil](#fase-6--aplicación-móvil)
8. [Fase 7 — Despliegue e instaladores](#fase-7--despliegue-e-instaladores)
9. [Fase 8 — Memoria, pruebas finales y defensa](#fase-8--memoria-pruebas-finales-y-defensa)
10. [Anexo A — Auditoría del front: todas las pantallas de Sprinta](#anexo-a--auditoría-del-front-todas-las-pantallas-de-sprinta)
11. [Anexo B — Auditoría del back: problemas encontrados](#anexo-b--auditoría-del-back-problemas-encontrados)
12. [Anexo C — Cómo hacer que se note que lo has hecho tú](#anexo-c--cómo-hacer-que-se-note-que-lo-has-hecho-tú)
13. [Anexo D — Checklist final de entrega](#anexo-d--checklist-final-de-entrega)

---

## 0. Resumen: cómo está el proyecto hoy

| Pieza | Estado real | Qué hacer |
|---|---|---|
| **Backend** (Spring Boot 4.1, Java 21, MySQL) | **No compila.** 33 modelos (21 entidades + 12 enums) con su CRUD genérico. Seguridad a medias: se empezó a pasar de Basic a JWT y se quedó sin terminar. | Arreglar (Fase 1) y asentar (Fase 2). Es la base de todo y **se queda**. |
| **`cliente/`** (Java Swing) | Funcionaba con Basic Auth + `/api/me`. Ahora el login falla porque `/api/me` ya no existe. | Sustituir por la app de escritorio definitiva (Fase 4). Se puede reutilizar `ApiClient`. |
| **`frontend/`** (HTML/JS de prueba) | **Ya está desconectada**: `application.properties` no tiene `static-locations` (el backend no la sirve) y llama a `/api/me`, que no existe. | No invertir más tiempo. Sustituir por Swagger y borrarla (Fase 3). |
| **`database/`, `documentacion/`** | Vacías (solo `.gitkeep`). | Script SQL, diagramas y memoria (Fases 2 y 8). |
| **README** | Solo pone "Sprinta". | Reescribirlo: qué es Sprinta, capturas y cómo arrancarlo (Anexo C). |

**Idea clave de la arquitectura** (lo que dice tu profesora de la academia):

```
 ┌───────────────────┐      HTTP + JSON       ┌──────────────────────┐      JPA      ┌─────────┐
 │ App de ESCRITORIO │ ─────────────────────▶ │                      │ ────────────▶ │         │
 │ (Windows / macOS) │ ◀───────────────────── │   API REST           │ ◀──────────── │  MySQL  │
 └───────────────────┘   token JWT en cabecera│   Spring Boot        │               │         │
 ┌───────────────────┐                        │   (tu backend)       │               └─────────┘
 │ App MÓVIL         │ ─────────────────────▶ │                      │
 │ (Android)         │ ◀───────────────────── │                      │
 └───────────────────┘                        └──────────────────────┘
```

Las dos apps **nunca** tocan la base de datos: solo hablan con la API. Toda la lógica de negocio (cálculo de presupuestos, permisos por rol, stock…) vive en el backend, una única vez. Por eso el backend es lo primero que hay que dejar bien.

---

## Fase 0 — Decisiones antes de programar

### 0.1 Preguntas al tutor — RESPONDIDAS
- [x] Tecnologías: **solo lo dado en el curso** → JavaFX para escritorio y Android Studio para móvil (decisiones en [Empieza aquí](#-empieza-aquí--qué-hago-y-en-qué-orden)).
- [x] Java obligatorio: sí, es lo que has dado durante los dos cursos.
- [x] Base de datos: MySQL.
- [x] Despliegue: servidor en tu portátil (Fase 7).
- [x] Plantilla/rúbrica: no hay → seguimos la estructura de la memoria de ejemplo (AutoTerra).
- [x] IA: puedes mantener lo hecho; a partir de ahora, sin abusar.
- [ ] **Fechas**: apúntalas en [Empieza aquí](#-empieza-aquí--qué-hago-y-en-qué-orden) en cuanto las sepas.
- [ ] **Pregunta pendiente:** "¿En PMDM programábamos Android en Java o en Kotlin?" (mira tus prácticas).

### 0.2 Decisión tomada
- [x] **Escritorio: JavaFX** (Java 21, FXML + CSS, Scene Builder).
- [x] **Móvil: Android Studio en Java** con pantallas XML (o Kotlin si fue lo que se dio en PMDM).
- [ ] Escríbela en la memoria (Estado del arte → tecnologías) con el porqué.

> **No sigas Swing.** No se puede conseguir el aspecto de Sprinta con un esfuerzo razonable y JavaFX es su sustituto oficial.

### 0.3 Definir el alcance (qué entra y qué no)

Sprinta es enorme (tiene incluso un motor de costes con imposición de planchas). Tienes que **acotar** para llegar a tiempo. Propuesta:

| Prioridad | Módulos | Escritorio | Móvil |
|---|---|---|---|
| **MVP (imprescindible)** | Login + roles, Dashboard, Clientes (empresa/particular + contactos), Productos/Catálogo (simplificado), Presupuestos (líneas, estados, IVA, PDF), Órdenes de trabajo (estados, asignación), Registro de tiempos, Inventario de materiales (stock), Empleados | ✅ | Parcial (ver Fase 6) |
| **Importante** | Modo Taller (iniciar/finalizar tarea, registrar material), Maquinaria con coste/hora, Acabados, Facturas, Compras/Pedidos a proveedor, cálculo de presupuesto **simplificado** | ✅ | Modo Taller ✅ |
| **Extra (si sobra tiempo)** | Escandallo por tramos de cantidad, visor de imposición de planchas, ruta de producción en grafo, externalizar fase, cierre financiero/rentabilidad, envío por email, retales, imágenes de producto, comisiones | Opcional | ❌ |

- [ ] Alcance acordado con el tutor/profesora.

### 0.4 Identidad visual de Sprinta

- [x] **Nombre**: Sprinta (idea y proyecto tuyos).
- [ ] Diseñar un **logo** sencillo (Figma, Penpot o Canva).
- [ ] Fijar la **paleta** de Sprinta (estilo oscuro de las capturas; ejemplo de partida, cámbialo a tu gusto):
  - Fondo `#0E1024`, tarjetas `#171A36`, bordes `#2A2F5E`
  - Primario `#5B5BF0` (botones), secundario `#22D3EE` (acentos)
  - Éxito `#22C55E`, aviso `#F59E0B`, error `#EF4444`
  - Tipografía: Inter u Outfit
- [ ] Hacer **wireframes/mockups** de 5–6 pantallas clave antes de programarlas (login, dashboard, lista de presupuestos, formulario de cliente, modo taller, y lo mismo en móvil). Van a la memoria.

### 0.5 Organización del trabajo

- [ ] Crear en GitHub un **tablero** (Projects) con una tarjeta por cada paso de este plan.
- [ ] Trabajar en **ramas**: `main` (estable) ← `develop` ← `feature/jwt-login`, `feature/dto-cliente`…
- [ ] Commits pequeños con mensajes en tus palabras: `Arregla compilación de RolTrabajador`, no `Primer commit` (ahora hay dos commits seguidos con ese nombre).
- [ ] Llevar un **diario de desarrollo** (`documentacion/diario.md`): fecha, qué hiciste, qué problema tuviste y cómo lo resolviste. Es oro para la memoria y para la defensa.
- [ ] Rellenar fechas reales en un **diagrama de Gantt** cuando sepas los plazos (Fase 8).

---

## Fase 1 — Arreglar lo que está roto (urgente)

Objetivo: que el backend vuelva a compilar y que se pueda iniciar sesión. **1–2 días.**

### 1.1 Que vuelva a compilar ✅ (hecho)
- [x] En `backend/ERP-Imprenta/src/main/java/.../model/RolTrabajador.java` hay una línea que solo contiene `/` (línea 3, entre el `package` y el `public enum`). Bórrala.
- [ ] Comprueba: desde `backend/ERP-Imprenta` ejecuta `./mvnw compile` → debe acabar en `BUILD SUCCESS`. (Lo he comprobado en una copia: quitando esa barra compila.)
- [ ] Arranca con `./mvnw spring-boot:run` y verifica en MySQL que se crean las tablas.
- [ ] Commit: `Arregla error de compilación en RolTrabajador`.

### 1.2 Decidir el sistema de login: **JWT** (recomendado)

Ahora mismo hay una mezcla: `httpBasic` activo, la dependencia `spring-boot-starter-security-oauth2-resource-server` añadida pero sin usar, un `AuthController` que solo devuelve lo que le mandas, y un `UsuariosController` sin terminar.

**Por qué JWT para escritorio + móvil:** con Basic las apps tienen que guardar la contraseña y enviarla en cada petición; con JWT se envía la contraseña **una sola vez**, el servidor devuelve un *token* firmado que caduca, y la app solo guarda ese token. Además es un punto fuerte para explicarlo en la memoria.

Pasos (escríbelos tú siguiendo la documentación oficial de Spring Security, sección *OAuth2 Resource Server → JWT*):

- [ ] **Secreto**: añade `app.jwt.secreto=...` (mínimo 32 caracteres aleatorios) en `local.properties` (que ya está en `.gitignore`) y `app.jwt.minutos-validez=480` en `application.properties`.
- [ ] **Configuración** (`config/JwtConfig.java`): dos beans, un `JwtEncoder` (con `NimbusJwtEncoder` y la clave secreta, HS256) y un `JwtDecoder` (`NimbusJwtDecoder.withSecretKey(...)`).
- [ ] **DTOs** (`dto/LoginRequest` con `email` y `password`; `dto/LoginResponse` con `token`, `expiraEn` y los datos básicos del usuario). Usa `record` de Java.
- [ ] **`AuthController`** → `POST /auth/login`:
  1. recibe `LoginRequest` con `@Valid`,
  2. autentica con el `AuthenticationManager` (así sigue funcionando tu bloqueo por fuerza bruta de `LoginAttemptService`),
  3. genera el token con: `subject` = email, claim `rol` = el rol, `issuedAt`, `expiresAt`,
  4. devuelve `LoginResponse`. Si falla → 401 con `{"message": "Email o contraseña incorrectos"}`.
- [ ] **`SecurityConfig`**:
  - `/auth/login` → `permitAll()`.
  - `/api/**` → `authenticated()`.
  - Todo lo demás → `denyAll()` (cuando quites la web; mientras, puedes dejarlo).
  - Sustituir `.httpBasic(...)` por `.oauth2ResourceServer(o -> o.jwt(...))` con un `JwtAuthenticationConverter` que lea el claim `rol` **sin** prefijo `ROLE_` (para que tu `hasAuthority('ADMIN')` siga funcionando).
  - Arreglar la indentación y el comentario que habla de `spring.web.resources.static-locations` (esa propiedad ya no existe).
- [ ] **`GET /api/me`**: vuelve a crearlo (ahora no existe y por eso fallan los dos clientes). Devuelve un `UsuarioActualDto` (id, nombre, email, rol). Puedes obtener el email con `@AuthenticationPrincipal Jwt jwt` → `jwt.getSubject()`.
- [ ] **Borra `UsuariosController.java`**: no tiene ninguna ruta mapeada y el `Jwt` que importa (`OAuth2ResourceServerProperties.Jwt`) es una clase de *configuración*, no el token. La correcta es `org.springframework.security.oauth2.jwt.Jwt`.
- [ ] Prueba a mano (Swagger o Postman, ver Fase 3): login correcto → token; login incorrecto → 401; `/api/cliente` sin token → 401; con token → 200; 6 fallos seguidos → bloqueado.
- [ ] Commit: `Login con JWT y endpoint /api/me`.

> **Plan B si vas muy justa de tiempo:** deja Basic Auth, quita la dependencia `oauth2-resource-server`, borra `UsuariosController` y el `/auth/login` falso, y restaura `GET /api/me`. Funciona, pero en la memoria tendrás que justificar por qué las apps guardan la contraseña.

---

## Fase 2 — Asentar la arquitectura del backend

Objetivo: que no queden "cosas sueltas". Hazlo **antes** de las apps: cada cambio en la API después obliga a cambiar dos clientes.

### 2.1 Estructura de paquetes final
```
com.tfgLeilaFraileSimon.ERP_Imprenta
├── config/        SecurityConfig, JwtConfig, OpenApiConfig, AdminInicialRunner, DatosDemoRunner
├── security/      LoginAttemptService, LoginAttemptListener
├── controller/    Solo recibe la petición, valida (@Valid) y llama al servicio
├── service/       Lógica de negocio (cálculos, cambios de estado, stock)
├── repository/    Interfaces JpaRepository + consultas de búsqueda
├── model/         Entidades JPA y enums
├── dto/           Records de entrada (XxxRequest) y de salida (XxxResponse)
├── mapper/        Conversión entidad ⇄ DTO (a mano, para que se vea que lo entiendes)
└── exception/     Excepciones propias + GlobalExceptionHandler
```
- [ ] Crear los paquetes que faltan: `dto`, `mapper`, `exception`.

### 2.2 Gestión de errores global
Hoy, si pides un id que no existe, `obtener()` lanza `NoSuchElementException` → **500** (debería ser 404) y no hay respuesta de error uniforme.
- [ ] Crear `exception/RecursoNoEncontradoException` y usarla en todos los `obtener()`/`actualizar()`/`eliminar()`.
- [ ] Crear `exception/GlobalExceptionHandler` con `@RestControllerAdvice` que devuelva siempre el mismo JSON: `{ "estado": 404, "mensaje": "...", "errores": {campo: mensaje}, "fecha": "..." }`:
  - `RecursoNoEncontradoException` → 404
  - `MethodArgumentNotValidException` → 400 con el error de cada campo
  - `DataIntegrityViolationException` (email duplicado, borrar algo referenciado) → 409
  - `AccessDeniedException` → 403
  - `ResponseStatusException` → su código
  - Cualquier otra → 500 sin detalles internos
- [ ] Las dos apps mostrarán `mensaje` (y `errores` bajo cada campo del formulario).

### 2.3 DTOs y validación
Ahora los controladores reciben y devuelven **entidades** directamente. Problemas: se pueden colar campos que no deberían tocarse, puede haber bucles infinitos al serializar relaciones, y no hay validación (solo `Trabajador` usa `@Valid`).
- [ ] Para cada entidad del MVP, crear `XxxRequest` (lo que manda la app) y `XxxResponse` (lo que devuelve la API). En las relaciones, el request lleva el **id** (`idCliente`) y el response lleva un resumen (`clienteNombre`).
- [ ] Anotaciones en los requests: `@NotBlank`, `@NotNull`, `@Email`, `@Size`, `@Positive`, `@PositiveOrZero`, `@Pattern` (DNI/NIE y CIF), `@Digits` en importes.
- [ ] Poner `@Valid` en **todos** los `@RequestBody`.
- [ ] Orden sugerido: Trabajador → Cliente/Empresa/Particular/Contacto → Material → Maquinaria → Producto → Presupuesto+Líneas → OrdenTrabajo → RegistroTiempo → Factura.

### 2.4 Arreglar los `actualizar()` genéricos
Hoy hacen `objeto.setId(id); save(objeto)`: si el id no existe **crean** uno nuevo, y cualquier campo que no venga en el JSON se queda a `null`.
- [ ] Cargar el existente (404 si no está), copiar los campos del request y guardar (como ya haces bien en `TrabajadorService.actualizar`).

### 2.5 Roles y permisos
Solo `Trabajador` está protegido. Cualquiera con sesión puede borrar clientes o facturas.
- [ ] Decidir y escribir esta **matriz de permisos** (es una tabla para la memoria):

| Módulo | ADMIN | COMERCIAL | ENCARGADO_TALLER | OPERARIO | TRANSPORTISTA |
|---|---|---|---|---|---|
| Empleados | CRUD | — | Ver | — | — |
| Clientes | CRUD | CRUD | Ver | — | Ver |
| Productos / Acabados | CRUD | Ver | Ver | — | — |
| Presupuestos | CRUD | CRUD (los suyos) | Ver | — | — |
| Órdenes de trabajo | CRUD | Ver | CRUD | Ver las suyas | Ver |
| Registro tiempos / Modo Taller | Todo | — | Todo | Los suyos | — |
| Maquinaria | CRUD | — | CRUD | Ver | — |
| Inventario / Compras | CRUD | Ver | CRUD | Registrar consumo | — |
| Facturas | CRUD | Ver | — | — | — |
| Albaranes | CRUD | Ver | Ver | — | Ver / marcar entregado |

- [ ] Aplicarla con `@PreAuthorize("hasAnyAuthority('ADMIN','COMERCIAL')")` en cada método de controlador.
- [ ] Decidir qué hacer con el rol `USER` (no existe en Sprinta). Propuesta: eliminarlo y obligar a elegir rol al crear el trabajador.
- [ ] Decidir `Puesto` vs `rol`: hoy hay los dos. `rol` = permisos en la app; `Puesto` = cargo laboral. Si no aporta nada, elimina `Puesto` para simplificar.

### 2.6 Limpiar y completar el modelo de datos
**Primero actualiza el diagrama E-R y el relacional (draw.io)**, después el código. Revisa cada punto:

Cosas sueltas a resolver:
- [ ] **`OrdenEmpleado` y `OrdenTrabajador` son la misma relación** (OT ↔ Trabajador). Quédate con una (`OrdenTrabajador`, que tiene `horasReales`) y borra la otra con su repository/service/controller.
- [ ] `Proveedor.producto` apunta a `Catalogo` (producto que se vende). Un proveedor suministra **materiales** → relaciónalo con `Inventario` (o quita el campo).
- [ ] `Pago` mezcla cobros a clientes (factura) y pagos a proveedores (pedido/inventario). Sepáralo o deja claro con un campo `tipo` qué es cada cosa.
- [ ] `Factura` no tiene cliente (solo a través de la OT) ni base imponible/IVA. Añade `cliente`, `baseImponible`, `porcentajeIva`, `cuotaIva`, `total` y sus líneas.
- [ ] `EstadoPresupuesto` no tiene **`ENVIADO`** (Sprinta usa Borrador → Enviado → Aceptado/Rechazado).
- [ ] `Cliente` + `Empresa`/`Particular` con `@OneToOne`: decide si un cliente es **o** empresa **o** particular (validación en el servicio) y crea un endpoint que dé de alta el cliente completo de una vez (en Sprinta el formulario tiene el selector "Soy Empresa / Soy Particular").
- [ ] Renombrar `Catalogo` → `Producto` e `Inventario` → `Material` si quieres que el código hable el mismo idioma que las pantallas (opcional pero recomendable).

Campos que faltan para las pantallas de Sprinta (solo los del alcance elegido):
- [ ] `Cliente`: `perfilTecnico` (General, Offset, Digital, Gran formato, Rotulación, Mixto), `formaPago` (Transferencia, Recibo SEPA, Contado/Tarjeta, 30/60/90 días), `direccion`.
- [ ] `Trabajador`: `costeHora`. (DNI, Nº Seguridad Social e IBAN son **datos sensibles** → guárdalos solo si hacen falta y menciona el RGPD en la memoria.)
- [ ] `Presupuesto`: `numero`, `comercial` (Trabajador), `notas`, `margenIndustrial`, `porcentajeIva`, `baseImponible`, `total`, `fechaEnvio`.
- [ ] `Material` (Inventario): `tipo` (Papel hoja, Papel bobina, Plancha CTP, Tinta, Lona, Vinilo, Rígido…), `unidadMedida`, `margen`, `gramaje`, `grosor`, `ancho`, `alto`, `unidadesPorPack`.
- [ ] `Maquinaria`: `costeHora` y, si haces el cálculo, `velocidad` (pliegos/h), `tiempoArranque`, `mermaArranque`, `pliegoMaxAncho/Alto`. Amplía `TipoMaquinaria` (Guillotina, Laminadora, Plegadora, Troqueladora…).
- [ ] `OrdenTrabajo`: `fechaEntrega`, `encargado`, estado `CERRADA` (cierre financiero) si lo haces.

Entidades nuevas (según alcance):
- [ ] `RegistroTiempo` (OT, trabajador, máquina, inicio, fin, horas, observaciones) → Registro de tiempos y cronómetro del Modo Taller.
- [ ] `ConsumoMaterial` (OT, material, cantidad, trabajador, fecha) → "Registrar material" del Modo Taller; resta stock.
- [ ] `Acabado` (nombre, material asociado, máquina, coste/hora).
- [ ] `FormatoProducto` (nombre, ancho, alto, sangrado) y relación Producto ↔ Acabado.
- [ ] Extra: `ParteProducto` (tripa/portada), `PasoRuta` (ruta de producción), `Retal`.

Después:
- [ ] `DROP DATABASE sprinta;` y arrancar de nuevo (`ddl-auto=update` **no borra** columnas ni tablas viejas). Cambia también el nombre de la BD por el de tu proyecto.
- [ ] Exportar el esquema a `database/schema.sql` (`mysqldump --no-data`) y guardar los diagramas en `documentacion/diagramas/`.

### 2.7 Endpoints de negocio (no todo es CRUD)
- [ ] `GET /api/dashboard` → contadores (trabajos activos, presupuestos por estado) y comisiones.
- [ ] Presupuestos — las 6 acciones de cada fila de la tabla:
  1. **Visualizar** → `GET /api/presupuestos/{id}` (con sus líneas, base, IVA y total).
  2. **Descargar PDF** → `GET /{id}/pdf`.
  3. **Calcular la línea** (desglose de costes) → `POST /calcular`: mano de obra (€/h × horas) + máquina (€/h × horas) + material + margen, con **presupuesto mínimo**. Devuelve el precio sin guardar nada.
  4. **Enviar** → `POST /{id}/enviar` (BORRADOR → ENVIADO; más adelante, opcional, por email).
  5. **Rechazar** → `POST /{id}/rechazar` (→ RECHAZADO).
  6. **Aceptar** → `POST /{id}/aceptar` (→ ACEPTADO y **crea la orden de trabajo automáticamente**).
  - En las apps, **ventana de confirmación** antes de enviar, aceptar o rechazar ("¿Seguro que quieres…?"), y los botones solo aparecen cuando tienen sentido (un presupuesto ACEPTADO ya no muestra Enviar/Rechazar/Aceptar).
- [ ] Órdenes de trabajo: `POST /{id}/iniciar`, `POST /{id}/completar`, `GET /{id}/hoja-ruta/pdf`, `GET /mis-tareas` (las del operario logueado).
- [ ] Tiempos: `POST /api/tiempos/iniciar` y `POST /api/tiempos/{id}/detener` (cronómetro).
- [ ] Materiales: `POST /api/materiales/{id}/consumo` (resta stock, falla si no hay), `GET /api/materiales/bajo-minimo`.
- [ ] Toda la lógica va en los **servicios** con `@Transactional`, no en los controladores.

### 2.8 Búsquedas, filtros y paginación
Sprinta tiene buscadores en todas las tablas ("Buscar cliente, CP, teléfono…") y filtros por estado.
- [ ] Listados con `?q=texto&estado=BORRADOR&page=0&size=20` usando `Pageable` y métodos de repositorio (`findByNombreCompletoContainingIgnoreCase…`) o `@Query`.

### 2.9 Motor de cálculo del presupuesto (versión simplificada)
Es lo que hace especial a tu ERP frente a un CRUD cualquiera. Hazlo en un servicio aparte (`CalculoPresupuestoService`) **con tests**:
1. Cabida = cuántas piezas caben en un pliego (formato producto + sangrado vs. pliego de la máquina).
2. Pliegos netos = ⌈cantidad / cabida⌉; pliegos totales = netos + merma de arranque + % merma de tirada.
3. Coste papel = pliegos totales × coste por pliego del material.
4. Planchas = nº de colores × caras (offset); coste planchas.
5. Tiempo máquina = tiempo de arranque + pliegos totales / velocidad; coste = tiempo × coste/hora.
6. Acabados = tiempo × coste/hora del acabado (+ material).
7. Subtotal → + margen industrial (%) → base imponible → + IVA 21 % → total. Precio unitario = total / cantidad.

- [ ] Haz **un ejemplo a mano** (en una hoja de cálculo) y conviértelo en test unitario: si el test da lo mismo que tu hoja, el cálculo está bien. Este ejemplo va a la memoria.

### 2.10 PDF y correo
- [ ] PDF de presupuesto, OT (hoja de ruta) y factura **generado en el servidor** (OpenPDF o JasperReports) → las dos apps solo descargan y abren el archivo.
- [ ] (Extra) Envío por email con `spring-boot-starter-mail`; para probar usa Mailtrap. Credenciales siempre en `local.properties`.

### 2.11 Configuración por entornos
- [ ] `application-dev.properties`: `ddl-auto=update`, `show-sql=true`, Swagger activo, datos demo.
- [ ] `application-prod.properties`: `ddl-auto=validate`, `show-sql=false`, `app.security.require-https=true`, Swagger desactivado, todos los secretos por variables de entorno.
- [ ] (Opcional, muy recomendable) **Flyway** para versionar el esquema con scripts SQL en vez de depender de `ddl-auto`.

### 2.12 Datos de demostración
Para la defensa necesitas datos que se vean bien (Sprinta tiene clientes, máquinas Heidelberg/Komori, papeles con código `[PAP-EST-001]`…).
- [ ] `config/DatosDemoRunner` activo solo con perfil `dev`: 6 trabajadores (uno por rol), 5 clientes, 20 materiales, 8 máquinas, 4 productos, 10 presupuestos en distintos estados, 3 OTs.

### 2.13 Tests del backend
Ahora solo existe `ErpImprentaApplicationTests` (y necesita MySQL arrancado).
- [ ] Perfil `test` con H2 en memoria (o Testcontainers con MySQL).
- [ ] Tests unitarios de servicios (JUnit 5 + Mockito): cálculo de presupuesto, aceptar presupuesto crea OT, consumo de material sin stock falla.
- [ ] Tests de controlador (`MockMvc`): login OK/KO, 401 sin token, 403 si un OPERARIO intenta crear un trabajador, 404, 400 de validación.
- [ ] `./mvnw test` en verde antes de cada commit a `main`.

---

## Fase 3 — Probar sin la web y eliminarla

### 3.1 Sustituir la web por herramientas de prueba de API
La web de prueba ya no funciona (el backend no la sirve y llama a `/api/me`, que se borró). No merece la pena repararla:
- [ ] Añadir **springdoc-openapi** (`springdoc-openapi-starter-webmvc-ui`, rama **3.x**, la compatible con Spring Boot 4; confirma la versión en springdoc.org). Tendrás **Swagger UI** en `http://localhost:8080/swagger-ui.html`, con botón *Authorize* para pegar el token.
- [ ] En `SecurityConfig`, permitir `/swagger-ui/**` y `/v3/api-docs/**` **solo en dev**.
- [ ] Guardar una colección de **Postman o Bruno** (o archivos `.http` de VS Code/IntelliJ) en `documentacion/api/` con login, CRUD y endpoints de negocio. Sirve también como anexo de la memoria.
- [ ] Comprobar con Swagger: login, roles (entra con cada rol), CRUD de cada módulo, errores 400/401/403/404/409.

### 3.2 Eliminar la web de prueba
Cuando el punto 3.1 funcione:
- [ ] `git rm -r frontend` (sigue en el historial de git por si la necesitas consultar).
- [ ] `SecurityConfig`: cambiar `.anyRequest().permitAll()` por `.anyRequest().denyAll()` (esa regla solo existía para servir el HTML) y borrar los comentarios que hablan del frontend web.
- [ ] Buscar restos: `grep -rn "frontend\|index.html\|static" backend/` y limpiar.
- [ ] Recuerda para la memoria: **CORS solo afecta a navegadores**; las apps de escritorio y móvil no lo necesitan.
- [ ] Actualizar README.
- [ ] Commit: `Elimina la web de pruebas; la API se prueba con Swagger`.

---

## Fase 4 — Prueba pequeña de escritorio

Lo que propone tu profesora: antes de hacer toda la app, una prueba mínima conectada a tu API. (Escrito para JavaFX; si eliges otra tecnología, los pasos son los mismos.)

### 4.1 Crear el proyecto
- [ ] Carpeta `escritorio/` en la raíz (módulo Maven independiente): Java 21, OpenJFX 21 o 25 (LTS), plugin `javafx-maven-plugin`, Jackson.
- [ ] Instalar **Scene Builder** para diseñar las vistas FXML.
- [ ] Copiar y adaptar `cliente/.../api/ApiClient.java` (usa `java.net.http.HttpClient` + Jackson; ya funciona) para que mande `Authorization: Bearer <token>` en vez de Basic.

### 4.2 La prueba mínima (3 pantallas)
- [ ] **Login**: email + contraseña → `POST /auth/login` → guarda el token en memoria → `GET /api/me`.
- [ ] **Ventana principal**: barra lateral (estilo Sprinta) con el nombre y rol del usuario y "Cerrar sesión".
- [ ] **Clientes**: `TableView` cargada desde `GET /api/cliente`.
- [ ] Hoja de estilos `estilos.css` con tu paleta (fondo oscuro, tarjetas con borde redondeado, botón primario).
- [ ] Las llamadas a la API van en un `Task` (hilo aparte) para que la ventana no se congele.
- [ ] Enseñárselo a tu profesora → si se valida, seguir con la Fase 5.

### 4.3 Retirar el cliente Swing
- [ ] Cuando la prueba de JavaFX funcione: `git rm -r cliente` (o renómbralo a `escritorio` si reaprovechas el proyecto Maven). Así no quedan dos clientes de escritorio a medias.
- [ ] Commit: `Sustituye el cliente Swing por JavaFX`.

---

## Fase 5 — Aplicación de escritorio completa

### 5.1 Arquitectura de la app (para que no se desordene)
```
escritorio/src/main/java/.../
├── api/          ApiClient + un servicio por módulo (ClienteApi, PresupuestoApi…)
├── modelo/       Copia de los DTOs de la API (records)
├── controlador/  Un controlador por vista FXML
├── vista/        (resources) FXML + CSS + iconos
├── sesion/       Usuario logueado + token (Singleton)
└── util/         Validación, formato de moneda/fechas, alertas, toasts
```
- [ ] Patrón **MVC** (FXML = vista, controlador JavaFX, servicios de API = modelo). Explícalo en la memoria.

### 5.2 Componentes reutilizables (hazlos una vez, úsalos en todas las pantallas)
- [ ] Barra lateral con secciones y "ADMINISTRACIÓN" (se ocultan opciones según el **rol**).
- [ ] Tarjeta KPI (icono + título + número).
- [ ] Tabla con buscador, filtro por estado y botones de acción por fila (ver, editar, borrar con confirmación).
- [ ] Etiqueta de estado de colores (BORRADOR gris, ENVIADO azul, ACEPTADO verde, RECHAZADO rojo, EN PROCESO cian…).
- [ ] Ventana modal de formulario con validación por campo (reutiliza `cliente/.../util/Validacion.java`).
- [ ] Notificación tipo *toast* ("Tiempo iniciado", "Guardado correctamente").

### 5.3 Pantallas, en este orden
Detalle de cada una en el [Anexo A](#anexo-a--auditoría-del-front-todas-las-pantallas-de-sprinta).
- [ ] Login (+ mensaje de bienvenida la primera vez)
- [ ] Dashboard (KPIs + gráfico de anillo `PieChart` con presupuestos por estado)
- [ ] Clientes (tabla + formulario Empresa/Particular + contactos)
- [ ] Empleados (solo ADMIN)
- [ ] Materiales / Inventario (stock, mínimo, alerta de bajo stock)
- [ ] Maquinaria (tarjetas por tipo, estado, coste/hora)
- [ ] Productos / Catálogo (tarjetas con precio "desde", ficha de producto)
- [ ] Presupuestos (tabla con acciones + detalle + aceptar/rechazar + PDF)
- [ ] Órdenes de trabajo (tabla + estados + imprimir hoja de ruta)
- [ ] Registro de tiempos
- [ ] Modo Taller (vista grande para el operario)
- [ ] Facturas, Compras, Acabados (según alcance)

### 5.4 Calidad
- [ ] Ningún error de la API debe "romper" la app: siempre mensaje legible.
- [ ] Si el token caduca (401) → volver al login.
- [ ] Probar con cada rol que solo ve lo que debe.

---

## Fase 6 — Aplicación móvil

### 6.1 Qué hace la app móvil (no tiene que hacer todo)
En un taller, el móvil se usa en planta y fuera de la oficina. Propuesta:
- [ ] Login + Dashboard reducido.
- [ ] **Modo Taller** (el corazón del móvil): "Mis tareas pendientes" → Iniciar (elige máquina) → cronómetro "Trabajo en curso" → Finalizar tarea; botón **MAT** → "Registrar material" (tarjetas con stock).
- [ ] Presupuestos: lista con filtro por estado, detalle, **aceptar/rechazar**, ver PDF.
- [ ] Clientes (consulta y llamar/enviar email con un toque).
- [ ] Consulta de stock de materiales.
- [ ] (Transportista) Albaranes del día y marcar como entregado.

### 6.2 Tecnología: Android Studio + Java
- [ ] Android Studio → *New Project* → **Empty Views Activity** (la plantilla de Java con pantallas XML) → lenguaje **Java**, `minSdk` 26. Guárdalo en `movil/` en la raíz del repositorio.
- [ ] Pantallas en **XML** con Material Components en tema oscuro y tu paleta (`res/values/colors.xml` y `themes.xml`).
- [ ] Para llamar a la API, usa **la librería que usasteis en clase** (Retrofit o Volley). Si no usasteis ninguna: **Retrofit + Gson**, con un *interceptor* de OkHttp que añada `Authorization: Bearer <token>` a todas las peticiones.
- [ ] Una `Activity` (o `Fragment`) por pantalla + clases `XxxApi`/`XxxRepository` para las llamadas. Si en clase visteis `ViewModel` + `LiveData`, úsalos (es MVVM y queda muy bien en la memoria).
- [ ] Guardar el token en `SharedPreferences` (o `EncryptedSharedPreferences` para más seguridad).
- [ ] Barra de navegación inferior (Trabajos · Almacén · Perfil), con las opciones según el rol.
- [ ] (Si eliges Kotlin porque fue lo de PMDM: mismos pasos, con Kotlin.)

### 6.3 Conectar el móvil con tu backend (aquí se atasca casi todo el mundo)
- [ ] **Emulador**: la URL es `http://10.0.2.2:8080/` (no `localhost`, que sería el propio emulador).
- [ ] **Móvil real**: la IP de tu ordenador en la misma Wi-Fi (`http://192.168.x.x:8080/`) y el firewall del ordenador abierto en el puerto 8080.
- [ ] Android bloquea `http://` por defecto: en desarrollo añade un `network_security_config.xml` que lo permita **solo** para esas IPs; en producción, HTTPS (Fase 7).
- [ ] Pon la URL base en `BuildConfig`/`local.properties`, nunca a fuego en el código.

---

## Fase 7 — Despliegue e instaladores

### 7.1 El servidor en tu portátil — lo que hay que saber
**"localhost" significa "este mismo ordenador".** Si escribes `http://localhost:8080` en el ordenador de clase, ese ordenador se buscará **a sí mismo**, no a tu portátil de casa. Por eso no basta con "poner la misma URL" en otro equipo. Tienes tres formas de hacerlo (pregúntale a tu profesor cuál quería decir):

| Opción | Cómo | Dificultad | Recomendación |
|---|---|---|---|
| **1. Llevar el portátil a la defensa** | El portátil ejecuta MySQL + backend + app de escritorio. El móvil se conecta al portátil por Wi-Fi. | ⭐ Fácil | ✅ **La más segura.** Es la que recomiendo. |
| 2. Portátil en casa + abrir el router | Abres el puerto en el router de casa (*port forwarding*) y usas un dominio gratuito (DuckDNS) porque la IP de casa cambia. Hace falta HTTPS. | ⭐⭐⭐ Difícil | Solo si te obligan. La red del instituto puede bloquearlo, y si tu casa se queda sin luz o internet, no hay demo. |
| 3. Portátil en casa + túnel | Un programa (Cloudflare Tunnel, ngrok o Tailscale) da una dirección pública sin tocar el router. | ⭐⭐ Media | Buen **plan B** si no puedes llevar el portátil. |

**Para la opción 1 (paso a paso):**
- [ ] El día de antes, comprueba que todo arranca en el portátil **sin internet**.
- [ ] Usa el **punto de acceso (hotspot) de tu móvil** o de otro móvil para conectar portátil y móvil a la misma red (la Wi-Fi del instituto suele impedir que los dispositivos se vean entre sí).
- [ ] Mira la IP del portátil: Windows `ipconfig` / macOS *Ajustes → Wi-Fi → Detalles*. Será algo como `192.168.x.x`.
- [ ] En la app móvil, la URL del servidor será `http://192.168.x.x:8080/`. Haz que se pueda **cambiar desde una pantalla de ajustes** de la app, para no tener que recompilar si cambia la IP.
- [ ] Permite el puerto 8080 en el cortafuegos del portátil.
- [ ] Lleva el **vídeo de respaldo** por si algo falla.

### 7.2 Empaquetado
- [ ] **Backend**: `Dockerfile` + `docker-compose.yml` (servicio MySQL + servicio API, secretos en `.env` ignorado por git). Con eso arrancas todo con un comando en cualquier ordenador.
- [ ] (Si el centro pide despliegue real) Servidor/VPS o tu propio PC con dominio dinámico (DuckDNS, como Sprinta) + proxy inverso con HTTPS (Caddy o Nginx + Let's Encrypt). `app.security.require-https=true`.
- [ ] **Escritorio**: instalador con `jpackage` (`.msi`/`.exe` en Windows, `.dmg` en macOS) que incluye su propio Java.
- [ ] **Móvil**: APK/AAB de *release* firmado.
- [ ] Probar la instalación desde cero en un ordenador que no sea el tuyo.
- [ ] Manual de instalación paso a paso (va a la memoria).

---

## Fase 8 — Memoria, pruebas finales y defensa

### 8.1 Índice de memoria (adáptalo a la plantilla del centro)
- [ ] Introducción, motivación y contexto (sector de artes gráficas).
- [ ] Objetivos (generales y específicos).
- [ ] Estudio de mercado / soluciones existentes (ERPs de imprenta del mercado; Sprinta es tu app, no un competidor).
- [ ] Planificación: metodología (por sprints/fases de este plan), **Gantt**, recursos y presupuesto del proyecto.
- [ ] Análisis: requisitos funcionales y no funcionales, actores y **roles**, casos de uso, matriz de permisos (Fase 2.5).
- [ ] Diseño: arquitectura (diagrama del apartado 0), **E-R y relacional**, diagrama de clases, diseño de la API (tabla de endpoints o Swagger), **mockups** de escritorio y móvil, guía de estilo (paleta, tipografía, componentes).
- [ ] Implementación: tecnologías y por qué, estructura de cada proyecto, decisiones importantes (JWT, DTOs, gestión de errores, motor de cálculo con el ejemplo a mano).
- [ ] Seguridad: BCrypt, JWT, bloqueo por fuerza bruta, roles, HTTPS, secretos fuera del repositorio, **RGPD** (datos de empleados).
- [ ] Pruebas: unitarias, de integración, de API (Postman/Swagger), pruebas manuales por rol (tabla caso → resultado esperado → resultado obtenido), pruebas de usabilidad.
- [ ] Despliegue y manual de instalación.
- [ ] Manual de usuario (con capturas de **tu** app).
- [ ] Conclusiones, dificultades, líneas futuras (lo que dejaste fuera del alcance).
- [ ] Bibliografía/webgrafía y declaración de uso de herramientas de IA si el centro lo pide.
- [ ] Anexos: colección de la API, scripts SQL, diario de desarrollo.

### 8.2 Preparar la defensa
- [ ] Guion de demo (5–10 min) que recorra un flujo completo: el comercial crea cliente → presupuesto → lo envía → el cliente acepta → se crea la OT → el operario la hace desde el **móvil** (inicia, registra material, finaliza) → el admin ve el stock bajado y factura.
- [ ] Base de datos demo lista y un **vídeo de respaldo** por si algo falla en directo.
- [ ] Preparar respuestas: ¿por qué JWT? ¿por qué DTOs? ¿cómo se calcula un presupuesto? ¿qué pasa si dos operarios consumen el mismo material a la vez? (`@Transactional`).

---

## Anexo A — Auditoría del front: todas las pantallas de Sprinta

Inventario sacado de las capturas. Columna **Back hoy**: ✅ existe · 🟡 existe a medias · ❌ falta.

### Estructura general
- Menú lateral: Dashboard, Catálogo, Clientes, Presupuestos, Trabajos, Registro Tiempos · bloque **ADMINISTRACIÓN**: Empleados, Productos, Acabados, Maquinaria, Inventario, Compras, Facturas · abajo: tarjeta de usuario (nombre, email, rol), Cerrar sesión, botón **Modo Taller**.
- Estilo: fondo azul oscuro con "estrellas", tarjetas translúcidas con borde iluminado, botones índigo, etiquetas de estado de colores, ventanas modales grandes.
- En móvil Sprinta es la misma web adaptada: menú hamburguesa, tablas y modales a pantalla completa.

| Pantalla | Qué tiene en Sprinta | Back hoy | Prioridad |
|---|---|---|---|
| **Login** | Logo, email, contraseña, "Iniciar sesión"; modal de bienvenida "¡Hola, …!" la primera vez | 🟡 (roto, Fase 1) | MVP |
| **Dashboard** | "Bienvenido, nombre"; KPIs: Trabajos activos, Enviados, Aceptados, Borrador, Rechazados; anillo "Estado de tus presupuestos"; "Comisiones devengadas" últimos 30 días (5 %) | ❌ | MVP (comisiones: extra) |
| **Catálogo** | Tarjetas con foto, categoría, nombre, "Desde X € / N uds" | 🟡 (`Catalogo` solo tiene descripción y precio) | MVP |
| **Ficha de producto** | Foto, precio total y unitario, cantidad (y páginas), "Añadir al presupuesto", Editar, Importar acabados, Descatalogar, descripción detallada; **Opciones**: formato (Americano/Europeo/DIN A4/A5/Personalizado), tipo de papel por parte (tripa/portada), modo de impresión (4/4, 4/0, 2/2, 1/1…), selección de tintas, encuadernación (grapa a caballete, rústica fresada/cosida…), acabado por parte; "Máquina activa por paso de ruta" | ❌ | Importante (formatos/acabados); extra (partes, tintas, ruta) |
| **Desglose técnico y de costes** | Parte, material, cabidas, formas, pliegos (tiraje + merma), coste papel, planchas, tintas, procesos y tiempos por máquina, subtotal, margen comercial, precio final | ❌ | Importante (versión simplificada, Fase 2.9) |
| **Clientes** | Buscador; tabla: cliente, perfil técnico, identificador (CIF/DNI), contacto principal, forma de pago, acciones (crear presupuesto, editar, borrar) | 🟡 (faltan perfil técnico y forma de pago) | MVP |
| **Nuevo cliente** | Selector Soy Empresa / Soy Particular; razón social, nombre comercial, CIF (o nombre y DNI/NIE); perfil técnico; email, teléfono, dirección; forma de pago; contactos de la empresa (nombre, departamento, email, teléfono) | 🟡 | MVP |
| **Presupuestos** | Buscador, filtro por estado (Borrador, Enviado, Aceptado, Rechazado), "Mostrar rechazados"; tabla ID, cliente, fecha, total, estado; acciones: ver, descargar PDF, escandallo, enviar por email, rechazar, **aceptar y generar trabajo** (en tu captura anotada del móvil: 1 visualizar, 2 PDF, 3 línea/escandallo) | 🟡 (sin líneas operativas, sin ENVIADO, sin acciones) | MVP |
| **Detalle de presupuesto** | Datos del cliente, conceptos (descripción, cantidad, precio, subtotal), notas, base imponible, IVA 21 %, total; configuración económica (margen industrial %, IVA %, comisión comercial); escandallo por tramos de cantidad; visor de imposición de planchas (poses/pliego, orientación, aprovechamiento, croquis); imprimir | ❌ | MVP (datos, totales, PDF); extra (escandallo por tramos, imposición) |
| **Órdenes de trabajo** | Buscador; tabla OT, descripción, prioridad, fecha entrega, encargado, operarios, estado (En proceso, Completado, Cerrado financieramente); acciones: hoja de ruta, imprimir OT, completar, iniciar/detener cronómetro, externalizar fase, rentabilidad | 🟡 (CRUD básico) | MVP (estados, asignación, imprimir); extra (externalizar, rentabilidad) |
| **Hoja de ruta (taller)** | Referencia, observaciones; componentes de impresión (papel, gramaje, formato, tirada, páginas/pliego, tintas); especificaciones de máquinas (orden, máquina, operación, tiempo de preparación, cantidad); subcontrataciones; impresión en "hoja amarilla" | ❌ | Importante |
| **Externalizar fase** | Proveedor, tarea, coste estimado → crea pedido de compra en BORRADOR y pausa la OT | ❌ | Extra |
| **Rentabilidad / cierre** | Ingreso, costes de materiales, máquina, operarios y externos, margen neto %, "Sellar cierre financiero" | ❌ | Extra |
| **Registro de tiempos** | Lista del día; nuevo registro: OT, horas, observaciones | 🟡 (`OrdenTrabajador.horasReales`) | MVP |
| **Empleados** | Buscador; tabla nombre, email, rol, coste/hora; formulario: nombre, apellidos, email, contraseña temporal, rol, coste/hora, DNI, teléfono, dirección, nº SS, IBAN | ✅ CRUD (faltan coste/hora y otros campos) | MVP |
| **Productos (admin)** | Tabla nombre, categoría, precio base, nº formatos/acabados, "Ocultar descatalogados"; editor con pestañas **Datos comerciales** (nombre, familia, estado Borrador/Pendiente de taller/Activo/Descatalogado, cantidad mínima, encuadernaciones permitidas, formatos con sangrado, acabados, materiales, partes del producto, tintas) y **Ruta de producción** (grafo de pasos con máquinas candidatas y condiciones) | ❌ | Importante (datos, formatos, acabados); extra (partes, grafo) |
| **Acabados** | Catálogo de acabados globales: nombre, material asociado, máquina, fase, coste operación/hora | ❌ | Importante |
| **Maquinaria** | Tarjetas filtrables por tipo, coste operativo €/h, estado (Operativa, En mantenimiento, Fuera de servicio); formulario con datos generales y, para offset: pliego máx./mín., grosor, margen de pinza, tiempos fijos, velocidad, mermas | 🟡 (sin coste/hora ni parámetros) | MVP (datos + coste/hora); extra (parámetros offset) |
| **Inventario de materiales** | Pestañas Materiales / Retales; buscador y filtro por tipo; tabla material, tipo, stock (con mínimo), coste unitario, margen, estado; formulario con tipo, unidad, stock, mínimo, coste, margen, medidas, unidades por pack | 🟡 | MVP (sin retales) |
| **Compras / Facturas** | En el menú (sin capturas) | 🟡 (`Pedido`, `Factura`, `Pago`) | Importante |
| **Modo Taller** | "Mis tareas pendientes" (tarjetas con Iniciar y MAT), "Selecciona máquina", "Trabajo en curso" con "Finalizar tarea", "Registrar material" (tarjetas con stock), avisos "Tiempo iniciado/registrado" | ❌ | Importante (y núcleo del móvil) |

---

## Anexo B — Auditoría del back: problemas encontrados

| # | Gravedad | Problema | Dónde | Se resuelve en |
|---|---|---|---|---|
| 1 | 🔴 Bloqueante | No compila: línea con solo `/` | `model/RolTrabajador.java:3` | 1.1 |
| 2 | 🔴 Bloqueante | `GET /api/me` eliminado; los dos clientes lo usan para el login | `AuthController` | 1.2 |
| 3 | 🔴 | `POST /auth/login` es público y solo devuelve lo que recibe | `AuthController` | 1.2 |
| 4 | 🟠 | `UsuariosController` sin ruta y con un import equivocado (`OAuth2ResourceServerProperties.Jwt`) | `controller/UsuariosController.java` | 1.2 |
| 5 | 🟠 | Dependencia `oauth2-resource-server` añadida pero sin configurar; conviven Basic y un JWT sin terminar | `pom.xml`, `SecurityConfig` | 1.2 |
| 6 | 🟠 | Comentarios que mencionan `static-locations` y la web, que ya no se sirve | `SecurityConfig` | 1.2 / 3.2 |
| 7 | 🟠 | `obtener()` devuelve 500 en vez de 404; no hay gestor de errores global | todos los services | 2.2 |
| 8 | 🟠 | Sin `@Valid` ni DTOs salvo en Trabajador; las entidades se exponen tal cual | todos los controllers | 2.3 |
| 9 | 🟠 | `actualizar()` crea registros si el id no existe y pone a `null` lo que no llega | services genéricos | 2.4 |
| 10 | 🟠 | Solo Trabajador está protegido por rol; el resto lo puede modificar cualquiera con sesión | controllers | 2.5 |
| 11 | 🟡 | `OrdenEmpleado` y `OrdenTrabajador` duplican la misma relación | `model/` | 2.6 |
| 12 | 🟡 | `Proveedor.producto` apunta al catálogo de venta en vez de a materiales | `model/Proveedor.java` | 2.6 |
| 13 | 🟡 | `Pago` mezcla cobros y pagos; `Factura` sin cliente ni IVA | `model/` | 2.6 |
| 14 | 🟡 | `EstadoPresupuesto` sin `ENVIADO`; rol `USER` que no existe en el negocio; `Puesto` y `rol` solapados | `model/` | 2.5 / 2.6 |
| 15 | 🟡 | Faltan entidades para tiempos, consumos, acabados y formatos | `model/` | 2.6 |
| 16 | 🟡 | Solo CRUD: no hay endpoints de negocio (aceptar presupuesto, iniciar OT…) ni búsquedas | controllers | 2.7 / 2.8 |
| 17 | 🟡 | `ddl-auto=update` y `show-sql=true` sin perfiles; el esquema no se guarda en el repositorio | `application.properties`, `database/` | 2.6 / 2.11 |
| 18 | 🟡 | Sin tests reales (el único necesita MySQL arrancado) | `src/test` | 2.13 |
| 19 | ⚪ | El proyecto Maven y los paquetes se llaman `ERP-Imprenta`/`ERP_Imprenta` y el README está vacío; decide si los renombras a Sprinta para que todo sea coherente | pom.xml, README | Anexo C |
| ✅ | Bien hecho | BCrypt, bloqueo por fuerza bruta, admin inicial automático, contraseña `WRITE_ONLY`, `isEnabled()` según `activo`, contraseña de la BD fuera de git (`local.properties` ignorado), `include-stacktrace=never`, opción de forzar HTTPS | — | Mantener y explicarlo en la memoria |

---

## Anexo C — Cómo hacer que se note que lo has hecho tú

1. **Sprinta es tu idea**: cuenta en la introducción de la memoria de dónde salió y cómo la fuiste construyendo (las fotos de tu cuaderno sirven como anexo).
2. **Diseña antes de programar**: tus mockups, tu E-R y tu matriz de permisos son trabajo claramente tuyo.
3. **Escribe tú el código** siguiendo este plan y la documentación oficial. Si usas ayuda (IA, tutoriales), no pegues nada que no sepas explicar línea a línea: en la defensa te pueden preguntar por cualquier clase.
   - **Repasa el código que ya hizo la IA** (seguridad, CRUD, DTOs de Trabajador) hasta poder explicarlo con tus palabras. Si algo no lo entiendes, pregunta *por qué* funciona, no solo *cómo* arreglarlo.
   - **Declaración de IA en la memoria** (un párrafo en *Implementación* o en un anexo), por ejemplo: qué herramienta usaste, para qué partes (estructura inicial del CRUD y de la seguridad, resolución de dudas, revisión de errores) y que todo el código ha sido revisado y entendido por ti. Ser sincera aquí te protege.
   - **Si alguien te ha ayudado a programar alguna parte**, dilo también en la memoria (como con la IA). Lo que cuenta en la defensa es que sepas explicar cada parte del código.
4. **Comentarios con tus palabras**, cortos, explicando el *porqué*. Revisa los que ya hay: algunos describen cosas que ya no existen (la web, `/api/me`).
5. **Historial de git honesto**: commits frecuentes y pequeños, con mensajes que cuenten la evolución. Un historial de meses con avances graduales demuestra autoría mejor que nada.
6. **Diario de desarrollo** con problemas reales y cómo los resolviste (p. ej. "el móvil no conectaba con localhost → descubrí 10.0.2.2").
7. **Tu caso de negocio**: el ejemplo de cálculo de presupuesto hecho a mano, datos demo con nombres inventados por ti, el flujo de la demo.
8. **README propio**: qué es, capturas, cómo arrancar backend/escritorio/móvil, tecnologías y autora.
9. **Entiende cada decisión**: este plan te dice *qué* hacer y *por qué*; la forma concreta de hacerlo la decides y la defiendes tú.

---

## Anexo D — Checklist final de entrega

- [ ] `./mvnw test` en verde; el backend arranca desde cero con Docker.
- [ ] Ningún secreto en el repositorio (`git log -p | grep -i password` no muestra contraseñas reales).
- [ ] Carpeta `frontend/` y `cliente/` (Swing) eliminadas; no quedan referencias.
- [ ] App de escritorio instalable y probada con cada rol.
- [ ] APK instalada en un móvil real conectando con el backend.
- [ ] Swagger/colección de la API exportada.
- [ ] `database/schema.sql` y datos demo.
- [ ] Diagramas E-R, relacional, clases, arquitectura y casos de uso actualizados.
- [ ] Memoria completa según la plantilla del centro, revisada la ortografía.
- [ ] Manuales de usuario e instalación.
- [ ] Presentación + guion de demo + vídeo de respaldo.
- [ ] README final con capturas.

---

### Orden de trabajo resumido

```
Fase 0 (decidir) → Fase 1 (compila + login JWT) → Fase 2 (arquitectura back) → Fase 3 (Swagger, borrar web)
      → Fase 4 (prueba JavaFX) → Fase 5 (escritorio) → Fase 6 (móvil) → Fase 7 (despliegue) → Fase 8 (memoria y defensa)
```
La memoria **no se deja para el final**: cada fase deja su apartado escrito (diagramas en Fase 2, mockups en Fase 0, pruebas en cada fase).
