# Guía paso a paso — Entregas 1 y 2 de la memoria

> Esta guía te dice **qué poner en cada apartado, cómo hacerlo y con qué ideas tuyas**. No es el texto final: el texto lo escribes tú con tus palabras (las ideas salen de tu cuaderno).
> Modelo de referencia: la memoria de AutoTerra (DAM) que te pasaron. Copia su **estructura**, no su contenido.

---

## Antes de empezar (una sola vez, 30 minutos)

### Paso 1 — Crear el documento
1. Abre **Word** (o Google Docs, pero Word va mejor para índices automáticos).
2. Guárdalo como `Memoria_TFG_TuNombre.docx` en `documentacion/memoria/` y también en tu nube (Drive/OneDrive). **Ten siempre dos copias.**
3. Formato general (el típico de un TFG si no te dan plantilla):
   - Letra: Arial 11 o Calibri 11 (texto) · interlineado 1,15 o 1,5 · texto justificado.
   - Márgenes: 2,5 cm.
   - Números de página abajo a la derecha (la portada sin número).

### Paso 2 — Usar ESTILOS (esto hace el índice solo)
En Word, pestaña **Inicio → Estilos**:
- Títulos de apartado grandes (RESUMEN, INTRODUCCIÓN…) → estilo **Título 1**.
- Subapartados (Objetivo general, Análisis económico…) → **Título 2**.
- Sub-subapartados → **Título 3**.
- Texto normal → **Normal**.

> ⚠️ No pongas los títulos "a mano" con negrita y tamaño grande: si no usas los estilos, el índice automático no los encuentra.

### Paso 3 — Cómo citar (lo vas a necesitar en la entrega 2)
La memoria de AutoTerra cita así: *texto… (Banco de España, 2023)*. Es el formato **APA**. Cada vez que pongas un dato que no es tuyo:
- En el texto: `(Autor u organización, año)`.
- Al final del documento, en **Bibliografía**: `Organización. (año). Título. URL`.
- Truco: en Word, pestaña **Referencias → Insertar cita** con estilo APA te lo hace solo.

---

# ENTREGA 1 — Portada, Índice, Resumen, Introducción, Objetivos

Tiempo estimado: **2–3 tardes**. Orden recomendado: Objetivos → Introducción → Resumen → Portada → Índice (el resumen se escribe al final porque resume lo demás, y el índice se genera al final).

## 1.1 Portada
**Qué es:** la primera página. No lleva número.
**Qué poner (de arriba abajo):**
- [ ] Logo del centro (si lo tienes) y nombre del centro.
- [ ] "Ciclo Formativo de Grado Superior en Desarrollo de Aplicaciones Multiplataforma".
- [ ] "Proyecto Intermodular / Trabajo de Fin de Grado" (usa el nombre que use tu centro).
- [ ] **Título del proyecto**: nombre de tu app + una frase. Ejemplo de forma: *«Sprinta — Sistema de gestión integral para pequeñas imprentas»*.
- [ ] Logo de tu app (aunque sea sencillo).
- [ ] Autora: tu nombre completo.
- [ ] Tutor/a: nombre.
- [ ] Curso académico: 2026/2027.
- [ ] Fecha de entrega.

> 🔴 El nombre de tu app es **Sprinta** (es tu idea y tu proyecto). Úsalo igual en la memoria, en las apps, en el README y en la base de datos.

## 1.2 Índice
**Qué es:** la lista de apartados con su página.
**Cómo hacerlo (cuando ya tengas los títulos con estilos):**
1. Pon el cursor en la página 2 (después de la portada).
2. **Referencias → Tabla de contenido → Tabla automática 1**.
3. Cada vez que cambies algo: clic derecho sobre el índice → **Actualizar campo → Actualizar toda la tabla**.
- [ ] (Opcional, como AutoTerra) **Índice de figuras**: a cada imagen le pones un pie con **Referencias → Insertar título** y luego **Referencias → Insertar tabla de ilustraciones**. Lo necesitarás en la entrega 2 (gráficos, logos de competidores, DAFO).

## 1.3 Resumen (+ Palabras clave)
**Qué es:** UN párrafo (8–12 líneas) que explica el proyecto entero a alguien que no sabe nada. Se escribe **el último**.
**Debe responder, en este orden:**
1. ¿Qué es? → un sistema de gestión (ERP) para pequeñas imprentas.
2. ¿De qué partes se compone? → una API REST con base de datos, una aplicación de escritorio y una aplicación móvil.
3. ¿Qué permite hacer? → gestionar clientes, calcular presupuestos, órdenes de trabajo, taller, stock, albaranes, facturas y cobros.
4. ¿Qué tiene de especial? → (1–2 cosas de tu apartado de Innovación).
5. ¿Para quién es y qué beneficio da? → para la pequeña imprenta: saber cuánto cuesta de verdad cada trabajo y no perder información.

**Palabras clave** (5–8, separadas por comas). Ejemplo: *ERP, imprenta, artes gráficas, presupuestos, órdenes de trabajo, gestión de stock, aplicación multiplataforma, API REST.*

- [ ] Resumen escrito (máximo media página).
- [ ] Palabras clave.

## 1.4 Introducción
**Qué es:** media página o una página contando **por qué** haces este proyecto. Puede ser personal (AutoTerra habla de su pasión por los animales).
**Guion (un párrafo por punto):**
1. **Tu motivación**: ¿por qué una imprenta? (¿conoces alguna, has trabajado o visto una por dentro, alguien de tu entorno?). Cuéntalo con tus palabras: es lo que hace que se note que el proyecto es tuyo.
2. **El problema**: muchas imprentas pequeñas siguen trabajando con papel, Excel y WhatsApp. Presupuestan "a ojo", no saben cuánto les ha costado de verdad un trabajo, se quedan sin material sin darse cuenta y no tienen controlado quién les debe dinero.
3. **Tu solución en una frase**: un sistema que acompaña al trabajo desde que entra el cliente hasta que se cobra la factura, con el escritorio para la oficina y el móvil para el taller y el reparto.
4. **Qué vas a usar** (una línea, sin detalle): Java con Spring Boot y MySQL para el servidor, JavaFX para escritorio y Android para el móvil.
5. **Cómo está organizado el documento** (opcional): "En los siguientes apartados se presentan los objetivos, el análisis del contexto…".

- [ ] Introducción escrita.

## 1.5 Objetivos
**Qué es:** qué quieres conseguir. Se divide en **un objetivo general** (una frase) y **objetivos específicos** (lista numerada; cada uno empieza por un verbo en infinitivo).
> 💡 Los objetivos específicos son MUY importantes: en la entrega 2, cada requisito funcional dirá "Objetivo relacionado: Objetivo X". Numéralos bien.

**Objetivo general** (adáptalo con tus palabras):
> Desarrollar un sistema de gestión integral para pequeñas imprentas, compuesto por una API REST, una aplicación de escritorio y una aplicación móvil, que cubra todo el ciclo de un trabajo: desde la captación del cliente hasta el cobro de la factura.

**Objetivos específicos** (sacados de tu cuaderno — revisa, quita o añade):
1. Gestionar los trabajadores de la imprenta y sus roles (administrador, comercial, encargado de taller, operario, transportista).
2. Garantizar un acceso seguro a la aplicación según el rol de cada usuario.
3. Gestionar los clientes, tanto empresas como particulares, y sus personas de contacto (CRM).
4. Gestionar el catálogo de productos que ofrece la imprenta.
5. Gestionar el inventario de materiales y los proveedores.
6. Gestionar la maquinaria y el coste por hora de cada máquina.
7. Calcular automáticamente el precio de un presupuesto a partir de la mano de obra, el uso de maquinaria, el material y el margen de beneficio, respetando un importe mínimo.
8. Gestionar el ciclo de vida de los presupuestos: visualizar, descargar en PDF, enviar al cliente, aceptar y rechazar.
9. Generar automáticamente la orden de trabajo al aceptar un presupuesto y asignar un encargado y los operarios.
10. Registrar desde el móvil el tiempo dedicado y el material consumido en cada orden de trabajo.
11. Avisar automáticamente cuando una orden de trabajo se finaliza.
12. Generar albaranes de entrega y recoger la firma del cliente en el móvil.
13. Emitir facturas y controlar los cobros pendientes y los clientes morosos.
14. Detectar cuándo un material baja de su stock mínimo y preparar automáticamente el pedido al proveedor.
15. Mostrar estadísticas del negocio: rentabilidad real de cada trabajo, comisiones de los comerciales e importes pendientes de cobro.
16. Permitir subir y descargar archivos asociados al cliente o al trabajo (logotipos, diseños).

- [ ] Objetivo general.
- [ ] Objetivos específicos numerados (ni demasiados ni muy pocos: entre 10 y 16 está bien).

## ✅ Checklist antes de entregar la 1
- [ ] Portada completa con el nombre de tu app.
- [ ] Índice automático actualizado.
- [ ] Resumen + palabras clave.
- [ ] Introducción.
- [ ] Objetivos (general + específicos numerados).
- [ ] Revisado ortografía (Word: **Revisar → Ortografía y gramática**) y leído en voz alta una vez.
- [ ] Exportado a PDF (**Archivo → Guardar como → PDF**) y comprobado que el índice tiene bien los números de página.

---

# ENTREGA 2 — Análisis del contexto, Estado del arte, Innovación, DAFO, Requisitos

Tiempo estimado: **1–2 semanas** (es la entrega más larga de las dos). Orden recomendado: Requisitos (porque ya los tienes en la cabeza) → Innovación → Estado del arte → Análisis del contexto → DAFO.

En AutoTerra el bloque se llama **"Análisis y contexto del arte"** y tiene esta forma; usa la misma:
```
ANÁLISIS Y CONTEXTO DEL ARTE      (Título 1)
  Análisis del contexto           (Título 2)
    Análisis económico            (Título 3)
    Análisis tecnológico
    Análisis sociocultural
    Análisis legislativo
    Análisis de la competencia
    Matriz comparativa de la competencia
    Análisis DAFO
  Estado del arte                 (Título 2)
  Innovación                      (Título 2)
ANÁLISIS DE REQUISITOS            (Título 1)
  Requisitos funcionales          (Título 2)
  Requisitos no funcionales       (Título 2)
```

## 2.1 Análisis del contexto
**Qué es:** el "mundo" en el que va a vivir tu app. Cada subapartado: 1–2 páginas, con **datos citados** y algún gráfico o tabla.
**Regla de oro:** cada dato, con su fuente y su año. Si dos fuentes dan cifras distintas, **usa una y di de dónde es** (las cifras de número de empresas cambian mucho según la fuente porque cuentan cosas distintas).

### a) Análisis económico — "¿cómo está el sector de las imprentas?"
Pasos:
1. Busca el último **informe económico de Neobis** (la Asociación de la Comunicación Gráfica) — sale cada año y se presenta en noticias de IFEMA/Interempresas.
2. Apunta: número de empresas, facturación, empleo, si el sector crece o baja, tamaño típico (la mayoría son pymes pequeñas).
3. Puedes completar con **eInforma** (sector CNAE 1812 "Otras actividades de impresión y artes gráficas") o **DBK**.
4. Termina con una conclusión tuya: *"Es un sector formado mayoritariamente por pequeñas empresas, con márgenes ajustados, donde calcular bien los costes es clave…"* → por eso tu app tiene sentido.

### b) Análisis tecnológico — "¿cómo de digitalizadas están las pymes?"
1. **INE – Encuesta sobre el uso de TIC y del comercio electrónico en las empresas**: % de empresas que usan ERP, CRM, nube… (fíjate en las de menos de 10 empleados: suelen estar muy por debajo).
2. Programa **Kit Digital** (ayudas públicas para digitalizar pymes): demuestra que hay interés en que las pequeñas empresas se digitalicen.
3. Conclusión: las grandes imprentas tienen software caro y las pequeñas todavía usan papel y Excel → hueco para tu app.

### c) Análisis sociocultural — "¿cómo es la gente que la va a usar?"
Esto lo puedes escribir casi sin fuentes, con tu conocimiento y si puedes, hablando con alguien de una imprenta:
- Muchas imprentas son **negocios familiares** con personal veterano poco acostumbrado a la informática → la app tiene que ser sencilla.
- El trabajo del taller se hace **de pie, junto a las máquinas** → el móvil encaja mejor que un ordenador.
- El repartidor está **fuera** → necesita el móvil para el albarán.
- **Relevo generacional**: los jóvenes que entran esperan herramientas digitales.
- 💡 Si puedes, **entrevista a una imprenta real** (5 preguntas: cómo presupuestan, cómo saben el stock, cómo saben qué les deben…). Ponerlo en la memoria da muchísimo valor y es 100 % tuyo.

### d) Análisis legislativo — "¿qué leyes afectan a mi app?"
- **RGPD** (Reglamento UE 2016/679) y **LOPDGDD** (Ley Orgánica 3/2018): guardas datos de clientes y trabajadores (DNI, teléfono, IBAN…) → hay que protegerlos (contraseñas cifradas, acceso por roles).
- **Reglamento de facturación** (Real Decreto 1619/2012): qué datos obligatorios lleva una factura (número correlativo, fecha, NIF, base, IVA…).
- **Verifactu** (Real Decreto 1007/2023): el software de facturación tendrá que cumplir unos requisitos antifraude. Su obligatoriedad se ha aplazado al **1 de enero de 2027 para sociedades y al 1 de julio de 2027 para autónomos** (Real Decreto-ley 15/2025). → Menciónalo como **línea futura**: tu app no lo implementa pero lo tienes en cuenta. *(Comprueba la fecha en la web de la Agencia Tributaria antes de entregar, por si ha cambiado.)*
- **Ley 18/2022 "Crea y Crece"**: factura electrónica obligatoria entre empresas (pendiente de desarrollo) → otra línea futura.

### e) Análisis de la competencia
Elige **4–6 programas** y para cada uno: logo (pie de figura), qué hace, para quién, puntos fuertes y débiles, y la fuente. Busca cada uno en su web oficial o en Capterra:

| Programa | Tipo | Qué mirar |
|---|---|---|
| **Gestion Global Print** | ERP específico para artes gráficas | Tiene CRM, facturación, cobros y órdenes de trabajo. ¿Tiene app móvil para el taller? |
| **MultiPress** (Dataline) | ERP/MIS para imprentas (Bélgica, distribuido en España) | Muy completo, orientado a imprentas medianas/grandes. Precio no público. |
| **Optimus** | MIS para artes gráficas (internacional) | Muy potente, complejo y caro para un taller pequeño. |
| **Palmart** | Gestión para artes gráficas | Integra *web-to-print* (tienda online con precio al instante). |
| **Holded** o **Odoo** | ERP genérico | Barato y fácil, pero **no entiende de imprenta** (ni máquinas, ni mano de obra por trabajo, ni hojas de ruta). |

> ⚠️ Sprinta es **tu** aplicación: en la matriz aparece como "Mi aplicación", no como competidor.

### f) Matriz comparativa (como la "Matriz morfológica" de AutoTerra)
Una tabla con ✔ / ✘ (o 1–5). Las columnas son **justo lo que te diferencia** (ver Innovación):

| | Especializado en imprenta | Presupuesto con costes reales (mano de obra + máquina + material) | App móvil para el taller | Firma del albarán en el móvil | Pedido automático al bajar de stock | Rentabilidad real vs. presupuestada | Control de morosos | Pensado para pymes (sencillo/asequible) |
|---|---|---|---|---|---|---|---|---|
| **Sprinta (mi aplicación)** | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ |
| Gestion Global Print | ✔ | ? | ? | ? | ? | ? | ✔ | ? |
| MultiPress | ✔ | ✔ | ? | ? | ? | ? | ✔ | ✘ |
| Holded | ✘ | ✘ | ✘ | ✘ | ? | ✘ | ✔ | ✔ |

> Rellena los "?" mirando la web de cada uno. **Sé honesta**: si un competidor tiene algo, ponle ✔. Una matriz donde tú ganas en todo y los demás no tienen nada no se la cree nadie.

### g) Análisis DAFO
Es un cuadro de 4 casillas. Hazlo como imagen (PowerPoint, Canva o draw.io), ponle pie de figura y **debajo explica cada punto en 1–2 líneas**. Borrador para que lo adaptes:

| **Debilidades** (internas, negativas) | **Amenazas** (externas, negativas) |
|---|---|
| Proyecto de una sola persona y con tiempo limitado. | Competidores consolidados con muchos años en el mercado (MultiPress, Optimus…). |
| Sin una imprenta real probándolo durante el desarrollo. | ERP genéricos baratos y conocidos (Holded, Odoo). |
| Servidor en local: depende de que el equipo esté encendido. | Resistencia al cambio en talleres tradicionales. |
| Aún no cumple Verifactu ni factura electrónica. | Sector con márgenes ajustados y poca inversión en tecnología. |

| **Fortalezas** (internas, positivas) | **Oportunidades** (externas, positivas) |
|---|---|
| Especializado en imprenta: cálculo con mano de obra, máquina y material. | Ayudas públicas para digitalizar pymes (Kit Digital). |
| Cubre el ciclo completo: cliente → presupuesto → taller → entrega → cobro. | Muchas imprentas pequeñas aún no usan ningún software. |
| App móvil para taller y reparto (fichar tiempos, material, firma del albarán). | Nuevas obligaciones de facturación (Verifactu, factura electrónica) que empujarán a cambiar de software. |
| Tecnologías sin coste de licencia (Java, MySQL, Android). | Auge de la impresión personalizada y de tiradas cortas, que exige presupuestar rápido. |

## 2.2 Estado del arte
**Qué es:** cómo se resuelve hoy el problema y con qué tecnologías. AutoTerra lo hace con **preguntas y respuestas** — es una forma fácil de escribirlo. Úsalas como títulos (Título 3):

1. **¿Cómo gestiona hoy su trabajo una imprenta pequeña?** → papel, Excel, hojas de ruta impresas que viajan con el trabajo, WhatsApp con el cliente, presupuestos "a ojo".
2. **¿Qué tipos de software existen?**
   - **MIS** (*Management Information System*) específicos de imprenta: presupuestos técnicos, planificación de taller (los de la competencia).
   - **Web-to-print**: tiendas online donde el cliente configura y paga.
   - **ERP genéricos**: facturación y contabilidad, sin parte de producción.
   - Estándar del sector para comunicar software y máquinas: **JDF** (de la organización CIP4). Solo menciónalo: tu app no lo usa.
3. **¿Cuáles son los problemas habituales?** (pon cada uno como viñeta con una frase):
   - Presupuestar por debajo del coste real → se pierde dinero sin saberlo.
   - No saber cuánto tiempo y material se gastó de verdad en cada trabajo.
   - Quedarse sin papel o tinta en mitad de un trabajo.
   - Información repartida entre papeles, Excel y móviles.
   - Facturas que se olvidan de cobrar / clientes morosos.
   - Albaranes en papel que se pierden o no se firman.
4. **¿Qué tecnologías se usan hoy para construir este tipo de aplicaciones y cuáles uso yo?** (aquí justificas tu elección, con lo que has dado en DAM):
   - Arquitectura **cliente-servidor con API REST**: un servidor central y varios clientes (escritorio y móvil) que hablan por HTTP en formato JSON.
   - Servidor: **Java 21 + Spring Boot** (estándar en empresa) + **MySQL**.
   - Escritorio: **JavaFX** (frente a C#/WPF, que solo funciona en Windows; JavaFX funciona en Windows, macOS y Linux y comparte lenguaje con el servidor).
   - Móvil: **Android nativo con Android Studio**.
   - Seguridad: contraseñas cifradas con **BCrypt** y sesiones con **token JWT**.

## 2.3 Innovación — "¿qué diferencia a mi app de las demás?"
**Qué es:** 1 página explicando qué aporta tu proyecto que no tengan los demás (o que no tengan juntos). Es el apartado que más va a mirar el tribunal.

### Tu idea en una frase (la "propuesta de valor")
> *Una herramienta pensada para la imprenta pequeña que conecta la oficina, el taller y el reparto, y que dice al dueño **cuánto ha ganado de verdad con cada trabajo**.*

### Las 5 diferencias (todas salen de tu cuaderno)
Escribe un párrafo por cada una: **qué hace** + **por qué importa** + **por qué los demás no lo hacen o lo hacen peor**.

1. **Presupuesto con costes reales y rentabilidad real.**
   El precio se calcula sumando lo que cuesta cada parte (de tu cuaderno):
   `mano de obra (€/h del trabajador × horas) + maquinaria (€/h de la máquina × horas) + material` → **coste de producción**, y encima el **margen**, con un **presupuesto mínimo** para que nunca se venda por debajo de lo que compensa.
   Lo innovador: cuando el trabajo se termina, la app **compara lo presupuestado con lo que costó de verdad** (horas y material que los operarios registraron desde el móvil). El dueño ve qué trabajos le dan dinero y cuáles le hacen perderlo.
2. **El taller y el reparto en el móvil.**
   El operario ve sus tareas, pulsa *Iniciar*/*Finalizar* (se registra el tiempo solo), elige la máquina y apunta el material que gasta. El transportista entrega y **el cliente firma el albarán en la pantalla del móvil**: se acabaron los albaranes de papel perdidos.
3. **Reposición automática de stock.**
   Cuando un material baja de su mínimo, la app **prepara sola el pedido al proveedor** (agrupando por proveedor). La persona encargada de compras **solo tiene que revisar y pulsar "Comprar"**, sin buscar producto por producto.
4. **Seguimiento de cobros y morosos.**
   Filtros de facturas **pendientes de pago** y **morosas**, y respuesta inmediata a "¿cuánto me deben?" con estadísticas.
5. **Todo el ciclo en una sola herramienta, con un flujo automático.**
   Cliente (CRM del comercial) → presupuesto → **al aceptarlo se crea sola la orden de trabajo** → taller → **aviso automático cuando termina** → albarán firmado → factura → cobro. Además, la comisión del comercial se calcula sola.

### Ideas opcionales (elige como mucho 1–2, si te sobra tiempo)
- **Presupuesto automático vs. revisión manual**: si el trabajo es un producto estándar del catálogo, el presupuesto se envía directamente; si es especial, queda "pendiente de revisión manual". Siempre con **ventana de confirmación** antes de enviar, aceptar o rechazar (para evitar errores).
- **Archivos del cliente**: subir el logo o el diseño una vez y reutilizarlo en presupuestos futuros ("¿ya tenemos su logo?").
- **Coste de transporte según volumen** (de tu cuaderno: los envíos pequeños salen caros por unidad y los grandes salen baratos).

> 💡 La idea que tiene que repetirse en todo el documento: **Sprinta gestiona el negocio de la imprenta de principio a fin y lleva el móvil al taller y al reparto**.

## 2.4 Análisis de requisitos

### a) Requisitos funcionales (RF) — qué HACE el sistema
Usa la **misma ficha que AutoTerra**, una por requisito (créala como tabla en Word y cópiala):

| Número de requisito | RF01 |
|---|---|
| Nombre de requisito | Inicio de sesión |
| Tipo | ☒ Requisito ☐ Restricción |
| Descripción | El sistema debe permitir a los trabajadores iniciar sesión con su email y contraseña. |
| Prioridad | ☒ Alta/Esencial ☐ Media/Deseado ☐ Baja/Opcional |
| Objetivo relacionado | Objetivo 2 |

Lista de partida (redacta la descripción de cada uno con "El sistema debe permitir…"). **Prioridad**: A = Alta, M = Media, B = Baja. **Obj.** = número de tu objetivo específico.

| RF | Nombre | Prioridad | Obj. |
|---|---|---|---|
| RF01 | Inicio de sesión con email y contraseña | A | 2 |
| RF02 | Cierre de sesión | A | 2 |
| RF03 | Bloqueo temporal tras 5 intentos fallidos | M | 2 |
| RF04 | Control de acceso por rol (cada rol solo ve sus menús) | A | 1, 2 |
| RF05 | Gestión de trabajadores (alta, consulta, modificación, baja) — solo administrador | A | 1 |
| RF06 | Gestión de clientes empresa y particular | A | 3 |
| RF07 | Gestión de contactos de empresa | M | 3 |
| RF08 | Búsqueda y filtrado de clientes | M | 3 |
| RF09 | Gestión del catálogo de productos | A | 4 |
| RF10 | Gestión de materiales del inventario (stock actual y mínimo) | A | 5 |
| RF11 | Gestión de proveedores | M | 5 |
| RF12 | Gestión de maquinaria con coste por hora y estado | A | 6 |
| RF13 | Creación de presupuestos con líneas de producto | A | 7, 8 |
| RF14 | Cálculo automático del precio (mano de obra + máquina + material + margen, con mínimo) | A | 7 |
| RF15 | Visualizar un presupuesto | A | 8 |
| RF16 | Descargar un presupuesto en PDF | A | 8 |
| RF17 | Enviar un presupuesto al cliente (estado ENVIADO) | M | 8 |
| RF18 | Aceptar o rechazar un presupuesto, con confirmación | A | 8 |
| RF19 | Generar la orden de trabajo automáticamente al aceptar | A | 9 |
| RF20 | Asignar encargado y operarios a una orden de trabajo | A | 9 |
| RF21 | Consultar "mis tareas" (operario) en el móvil | A | 10 |
| RF22 | Iniciar y finalizar una tarea registrando el tiempo | A | 10 |
| RF23 | Registrar el material consumido (descuenta stock) | A | 10, 5 |
| RF24 | Aviso al finalizar una orden de trabajo | M | 11 |
| RF25 | Generar albarán de entrega | M | 12 |
| RF26 | Firma del albarán por el cliente en el móvil | M | 12 |
| RF27 | Generar factura a partir de la orden de trabajo (base, IVA, total) | A | 13 |
| RF28 | Registrar cobros y filtrar facturas pendientes y morosas | M | 13 |
| RF29 | Aviso y pedido automático a proveedor cuando el stock baja del mínimo | M | 14 |
| RF30 | Panel de estadísticas (presupuestos por estado, rentabilidad real, pendiente de cobro) | M | 15 |
| RF31 | Cálculo de comisiones de los comerciales | B | 15 |
| RF32 | Subida y descarga de archivos del cliente/trabajo | B | 16 |

> Puedes añadir una **"Restricción"** como tipo, por ejemplo: *RF33 — Un presupuesto aceptado o rechazado no se puede modificar* (Tipo: Restricción).

### b) Requisitos no funcionales (RNF) — CÓMO tiene que ser el sistema
AutoTerra solo puso las tecnologías. **Hazlo mejor** con categorías (una lista o una tabla con RNF01, RNF02…):

- **Tecnológicos**: servidor en Java 21 con Spring Boot; base de datos MySQL; escritorio en JavaFX; móvil Android (Android Studio, Java); comunicación por API REST con JSON.
- **Seguridad**: contraseñas cifradas con BCrypt; autenticación con token JWT que caduca; acceso por roles; bloqueo tras 5 intentos fallidos; credenciales fuera del código; HTTPS si se despliega fuera de la red local.
- **Usabilidad**: interfaz en español, tema oscuro coherente entre escritorio y móvil, mensajes de error claros, confirmación antes de acciones irreversibles, botones grandes en el modo taller del móvil.
- **Rendimiento**: las pantallas habituales responden en menos de 2 segundos con datos de prueba.
- **Compatibilidad**: escritorio en Windows y macOS; móvil Android 8.0 (API 26) o superior.
- **Mantenibilidad**: arquitectura en capas (controlador, servicio, repositorio), código en un repositorio Git, API documentada con Swagger.
- **Legales**: cumplimiento del RGPD/LOPDGDD en los datos personales; facturas con los datos obligatorios del RD 1619/2012 y numeración correlativa.
- **Disponibilidad**: el servidor se ejecuta en un equipo local accesible desde la red; copia de seguridad de la base de datos.

## ✅ Checklist antes de entregar la 2
- [ ] Los 5 subapartados del contexto, con datos citados y al menos 1 gráfico o tabla en el económico.
- [ ] Competencia: 4–6 programas con su descripción, logo y fuente.
- [ ] Matriz comparativa honesta.
- [ ] DAFO (imagen + explicación).
- [ ] Estado del arte en formato pregunta-respuesta.
- [ ] Innovación con tus 5 diferencias.
- [ ] RF en fichas, numerados, cada uno con su objetivo relacionado.
- [ ] RNF por categorías.
- [ ] Bibliografía actualizada con todas las fuentes citadas.
- [ ] Índice y lista de figuras actualizados → PDF.

---

## Qué vendrá después (para que no te pille por sorpresa)
Según la memoria de referencia, las siguientes partes serán probablemente: **Diseño** (diagrama de red, relacional, de clases, mapa de navegación y pantallas), **Planificación** (Gantt), **Implementación** (API, base de datos, escritorio, móvil), **Puesta en marcha**, **Pruebas** (casos de prueba), **Presentación de la empresa** (forma jurídica y plan económico), **Conclusiones**, **Bibliografía** y **Anexos** (manuales de usuario e instalación). El plan técnico de `documentacion/PLAN_TFG.md` va preparando todo eso.

## Fuentes consultadas para preparar esta guía
- Capterra – Gestion Global Print: https://www.capterra.es/software/1015081/gestion-global-print
- Dataline – MultiPress en España (Cyan): https://www.dataline.eu/sites/default/files/inline-files/PR%20MultiPress%20Cyan%20PR_ES.pdf
- Interempresas – Optimus en Graphispag: https://www.interempresas.net/Envase/48428-Optimus-Espana-presentara-en-Graphispag-2011-su-nuevo-ERP.html?R=47627
- Interempresas – Palmart: https://www.interempresas.net/MetalMecanica/49246-Palmart-en-Graphispag-2011.html
- IFEMA – Informe económico de Neobis: https://www.ifema.es/digicom/noticias/neobis-presenta-el-informe-economico-del-sector-de-la-comunicacion-grafica
- eInforma – CNAE 1812: https://www.einforma.com/informes-sectoriales/cnae-1812-empresas-otras-actividades-de-impresion-y-artes-graficas
- Legal Today – Prórroga de Verifactu a 2027: https://www.legaltoday.com/actualidad-juridica/noticias-de-derecho/nueva-prorroga-verifactu-no-sera-obligatorio-hasta-2027-para-sociedades-y-otros-contribuyentes-2025-12-04/
