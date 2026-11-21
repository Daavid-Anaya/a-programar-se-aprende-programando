# Especificación de Requisitos de Software (SRS)

**Proyecto:** Border Radius Previewer - Vista previa de `border-radius` (aplicación solo cliente)

**Autor:** Juan David Villegas Anaya

**Organización:** ...

**Fecha:** Noviembre 2026

**Versión:** 1.0

---

## 1. Introducción

### 1.1 Propósito
Este documento define los requisitos funcionales y no funcionales de Border Radius Previewer. El objetivo del sistema es que el usuario ajuste los radios de las esquinas de una caja, vea el resultado en vivo y copie la declaración CSS `border-radius` correspondiente, escrita en su forma abreviada más corta.

### 1.2 Alcance del Sistema
Border Radius Previewer es una aplicación web que se ejecuta por completo en el navegador. Permite ajustar los 4 radios de una caja (modo simple) o los 8 radios que admite `border-radius`, uno horizontal y uno vertical por esquina (modo avanzado), en píxeles o en porcentaje. La aplicación genera la declaración CSS y la copia al portapapeles.

### 1.3 Fuera de alcance y Trabajo Futuro
Para esta iteración, no se contemplan:
* Backend o API. El problema no requiere procesamiento en un servidor.
* Persistencia: guardar, compartir o cargar formas.
* Unidades distintas de `px` y `%` (por ejemplo, `em` o `rem`), valores decimales o una unidad distinta por esquina.
* Cambiar el tamaño de la caja de vista previa.
* Otras propiedades CSS (bordes, sombras, colores) y prefijos de proveedor (`-webkit-`).
* Despliegue a producción. La aplicación se ejecuta solo en el entorno local de desarrollo.

### 1.4 Definiciones y Acrónimos
Los términos técnicos están en el Apéndice 8.1. Los principales: declaración CSS (par propiedad-valor, como `border-radius: 10px;`), forma abreviada o *shorthand* (una propiedad que fija varios valores a la vez), radio horizontal y vertical (los dos semiejes de la elipse que redondea cada esquina) y Clipboard API (interfaz del navegador para escribir en el portapapeles).

---

## 2. Descripción General

### 2.1 Perspectiva del Producto
El sistema es un único módulo cliente:
* **Cliente web:** React, con una arquitectura modular basada en componentes y estilos en CSS.
* **Lógica de generación:** un módulo de JavaScript independiente de React que recibe los radios y devuelve la declaración CSS (RNF-02).

### 2.2 Funciones principales
* Vista previa en vivo de una caja con los radios aplicados.
* Ajuste de cada radio mediante un deslizador y un campo numérico sincronizados.
* Modo avanzado con radios horizontal y vertical por esquina.
* Selección de la unidad (`px` o `%`).
* Saneamiento de la entrada numérica en tiempo real.
* Generación de la declaración `border-radius` en su forma abreviada más corta.
* Copia de la declaración al portapapeles.

### 2.3 Características de los Usuarios
La herramienta está dirigida a estudiantes y desarrolladores de frontend que quieren entender o generar un valor de `border-radius`. Se asume un nivel básico de navegación web y conocimientos básicos de CSS. El usuario espera ver el efecto de cada cambio de inmediato y obtener un CSS que pueda pegar sin editarlo.

### 2.4 Entorno Operativo
*   **Cliente:** Google Chrome en su versión actual, con JavaScript habilitado.

### 2.5 Restricciones de diseño
* El cliente se desarrolla con React (componentes funcionales), CSS y JavaScript.
* No hay servidor, base de datos ni autenticación.
* Los valores son enteros: de 0 a 200 en `px` y de 0 a 100 en `%`.
* La caja de vista previa mide 300 × 200 px.
* El idioma de la interfaz y de los mensajes es español.

### 2.6 Suposiciones y dependencias
* El navegador del usuario tiene JavaScript habilitado.
* La aplicación se sirve en un contexto seguro (`https://` o `http://localhost`), requisito de la Clipboard API.
* Dependencias del cliente: React y su herramienta de compilación (Vite), con Node.js y pnpm para el desarrollo, y Vitest para las pruebas unitarias.

### 2.7 Casos de Uso
| ID | Nombre | Tipo | Descripción |
| :--- | :--- | :--- | :--- |
| CU-01 | Ajustar las esquinas | Principal | El usuario mueve el deslizador o escribe un valor en una esquina (ej. 20 en las 4). La caja y el código se actualizan de inmediato (`border-radius: 20px;`). |
| CU-02 | Copiar el CSS | Principal | El usuario pulsa "Copiar CSS". La declaración mostrada queda en el portapapeles y aparece un mensaje de confirmación. |
| CU-03 | Forma compleja | Alterno | El usuario activa el modo avanzado y ajusta por separado el radio horizontal y el vertical de cada esquina (ej. `10px / 20px`). |
| CU-04 | Entrada inválida | Alterno | El usuario teclea una letra, pega `1x2` o escribe un valor mayor que el máximo. La UI corrige el valor y muestra una advertencia. |
| CU-05 | Falla al copiar | Alterno | El navegador no permite escribir en el portapapeles. La UI muestra un mensaje de error y el código sigue disponible para copiarlo a mano. |

---

## 3. Requisitos Funcionales (RF)

| ID | Nombre | Descripción |
| :--- | :--- | :--- |
| **RF-01** | **Vista previa** | La UI debe mostrar una caja de 300 × 200 px cuyo estilo `border-radius` es, en todo momento, exactamente la declaración generada por RF-08. |
| **RF-02** | **Controles por esquina** | En modo simple, la UI debe mostrar 4 controles, uno por esquina, en este orden: superior izquierda, superior derecha, inferior derecha e inferior izquierda. Cada control tiene un deslizador y un campo numérico sincronizados en ambos sentidos. |
| **RF-03** | **Activar el modo avanzado** | La casilla "Modo avanzado (8 valores)" muestra dos controles por esquina: radio horizontal y radio vertical. Al activarla, cada radio vertical toma el valor horizontal de su esquina, de modo que la forma no cambia. |
| **RF-04** | **Volver al modo simple** | Al desactivar el modo avanzado, se descartan los radios verticales y cada esquina conserva su radio horizontal. |
| **RF-05** | **Unidad** | Un selector con las opciones `px` y `%` define la unidad de todos los radios. Por defecto es `px`. Al cambiar de unidad, los números se conservan y los que superen el nuevo máximo (RF-06) se ajustan a ese máximo. |
| **RF-06** | **Saneamiento de la entrada** | Los campos numéricos solo admiten enteros de 0 al máximo de la unidad (200 en `px`, 100 en `%`). Ante eventos de teclado, pegado o arrastrar-soltar, la UI elimina los caracteres que no son dígitos y ajusta al máximo un valor que lo supere. En ambos casos muestra una advertencia, que se oculta en el siguiente evento de entrada que no corrija nada. |
| **RF-07** | **Campo vacío** | Mientras un campo está vacío, la vista previa y el código usan `0` para ese radio. Al perder el foco, el campo vacío muestra `0`. |
| **RF-08** | **Generación de la declaración** | El sistema debe generar la declaración `border-radius: <valor>;` en su forma abreviada más corta, según las reglas del Apéndice 8.2. |
| **RF-09** | **Cero sin unidad** | Un radio con valor `0` se escribe sin unidad (`0`, no `0px` ni `0%`). |
| **RF-10** | **Barra solo si es necesaria** | En modo avanzado, si cada radio vertical es igual al horizontal de su esquina, la declaración omite la `/` y los radios verticales. |
| **RF-11** | **Código generado** | La UI debe mostrar la declaración generada en un bloque de código que se actualiza en cada cambio. |
| **RF-12** | **Copiar al portapapeles** | El botón "Copiar CSS" escribe en el portapapeles, mediante la Clipboard API, exactamente el texto del bloque de código. Si la escritura tiene éxito, la UI muestra "CSS copiado al portapapeles." durante 3 s. |
| **RF-13** | **Error al copiar** | Si la Clipboard API no está disponible o rechaza la escritura, la UI muestra "No se pudo copiar. Seleccione el código y cópielo manualmente.". El bloque de código permite seleccionar su texto. |

---

## 4. Requisitos No Funcionales (RNF)

| ID | Atributo | Descripción |
| :--- | :--- | :--- |
| **RNF-01** | **Solo cliente** | La aplicación no realiza peticiones de red después de cargarse. Todo el procesamiento ocurre en el navegador. |
| **RNF-02** | **Separación de la lógica** | La generación de la declaración (RF-08 a RF-10) es una función pura en un módulo que no importa React. Los componentes solo la invocan y muestran su resultado. |
| **RNF-03** | **Pruebas** | La función de generación debe verificarse con pruebas unitarias (Vitest), que cubran las cuatro formas abreviadas, el cero sin unidad, la unidad `%`, la omisión de la `/` y la declaración de 8 valores. |
| **RNF-04** | **Accesibilidad (a11y)** | Cada deslizador y cada campo tiene una etiqueta `<label>` vinculada que nombra la esquina y, en modo avanzado, el eje (ej. "Superior izquierda, radio vertical"). El texto tiene un contraste mínimo de 4.5:1 (WCAG 2.1, criterio 1.4.3), toda la interfaz se puede usar con teclado y las advertencias y los mensajes de copia se anuncian a lectores de pantalla (`aria-live`). El bloque de código es la alternativa textual de la vista previa. |
| **RNF-05** | **Compatibilidad** | La UI debe funcionar sin errores en la versión actual de Chrome. |
| **RNF-06** | **Seguridad de contenido** | El cliente trata la entrada y el código generado siempre como texto (`interpolación JSX`, nunca `dangerouslySetInnerHTML`). |

---

## 5. Interfaces externas

### 5.1  Interfaz de usuario
La pantalla principal es minimalista e incluye:
* Un encabezado que identifica el propósito de la herramienta.
* La caja de vista previa de 300 × 200 px (RF-01).
* El selector de unidad `px` / `%` y la casilla "Modo avanzado (8 valores)" (RF-03, RF-05).
* Los controles de radio, agrupados por esquina en el orden de RF-02. Cada control tiene su `<label>`, un deslizador y un campo numérico (RF-02, RNF-04). En modo avanzado, cada esquina muestra dos controles: horizontal y vertical (RF-03).
* Un contenedor de visibilidad dinámica para las advertencias de validación (RF-06).
* El bloque de código con la declaración generada (RF-11).
* El botón "Copiar CSS", junto al bloque de código, y un contenedor para los mensajes de copia (RF-12, RF-13).

### 5.2 Interfaces de software
El cliente usa la Clipboard API del navegador (`navigator.clipboard.writeText`). No hay otras integraciones.

### 5.3 Interfaces de comunicaciones
No aplica. La aplicación no se comunica con ningún servidor después de cargarse (RNF-01).

---

## 6. Criterios de Aceptación y Casos de Prueba

El sistema se acepta cuando los 20 casos siguientes pasan. Cada requisito funcional tiene al menos una prueba. Los casos se ejecutan en el navegador. Salvo que se indique otra cosa, cada caso parte del estado inicial: modo simple, unidad `px` y las 4 esquinas en `0`. Las esquinas se nombran en el orden de RF-02: superior izquierda (SI), superior derecha (SD), inferior derecha (ID) e inferior izquierda (II).

| ID Prueba | Descripción / Requisito | Entrada | Salida Esperada | Requisitos |
| :--- | :--- | :--- | :--- | :--- |
| **CP-01** | Estado inicial | Cargar la página | El código muestra `border-radius: 0;` y la caja no tiene esquinas redondeadas. | RF-01, RF-09, RF-11 |
| **CP-02** | Un valor | Las 4 esquinas en `20` | El código muestra `border-radius: 20px;` y la caja lo aplica. | RF-01, RF-08 |
| **CP-03** | Dos valores | SI = ID = `10`; SD = II = `30` | `border-radius: 10px 30px;` | RF-08 |
| **CP-04** | Tres valores | SI = `10`; SD = II = `20`; ID = `30` | `border-radius: 10px 20px 30px;` | RF-08 |
| **CP-05** | Cuatro valores | SI = `10`, SD = `20`, ID = `30`, II = `40` | `border-radius: 10px 20px 30px 40px;` | RF-08 |
| **CP-06** | Cero sin unidad | SI = ID = `0`; SD = II = `20` | `border-radius: 0 20px;` | RF-09 |
| **CP-07** | Deslizador y campo sincronizados | Mover el deslizador SI a `50`; luego escribir `75` en el campo SI | El campo muestra `50`; luego el deslizador queda en `75`. | RF-02 |
| **CP-08** | Unidad porcentaje | Elegir `%`; las 4 esquinas en `50` | `border-radius: 50%;` y la caja es una elipse. | RF-05 |
| **CP-09** | Ajuste al cambiar de unidad | SI = `150` en `px`; elegir `%` | El campo SI queda en `100` y el código muestra `border-radius: 100% 0 0;`. | RF-05 |
| **CP-10** | Tecleo inválido | SI = `5`; teclear `a` al final del campo SI | El campo sigue en `5` y aparece la advertencia. | RF-06 |
| **CP-11** | Pegado inválido | Pegar `1x2` en el campo SI vacío | El campo queda en `12` y aparece la advertencia. | RF-06 |
| **CP-12** | Valor mayor que el máximo | Escribir `250` en el campo SI con unidad `px` | El campo queda en `200` y aparece la advertencia. | RF-06 |
| **CP-13** | La advertencia se oculta | Después de CP-10, teclear `0` | El campo queda en `50` y la advertencia desaparece. | RF-06 |
| **CP-14** | Campo vacío | SI = `20`; borrar el campo SI y luego mover el foco a otro control | Con el campo vacío, el código muestra `border-radius: 0;`. Al salir, el campo muestra `0`. | RF-07 |
| **CP-15** | Activar el modo avanzado | Datos de CP-05; activar el modo avanzado | Aparecen 8 controles; los verticales valen `10`, `20`, `30` y `40`, y el código no cambia. | RF-03, RF-10 |
| **CP-16** | Radios elípticos iguales | Modo avanzado; horizontales en `10`; verticales en `20` | `border-radius: 10px / 20px;` | RF-08, RF-10 |
| **CP-17** | Ocho valores | Modo avanzado; horizontales SI-SD-ID-II en `10`, `20`, `30`, `40`; verticales en `40`, `30`, `20`, `10` | `border-radius: 10px 20px 30px 40px / 40px 30px 20px 10px;` | RF-08 |
| **CP-18** | Volver al modo simple | Después de CP-16, desactivar el modo avanzado | Quedan 4 controles en `10` y el código muestra `border-radius: 10px;`. | RF-04 |
| **CP-19** | Copiar | Datos de CP-05; pulsar "Copiar CSS" y pegar en un editor de texto | Se pega `border-radius: 10px 20px 30px 40px;`. Aparece "CSS copiado al portapapeles." y desaparece a los 3 s. | RF-12 |
| **CP-20** | Error al copiar | Permiso del portapapeles denegado (o API simulada que rechaza la escritura); pulsar "Copiar CSS" | Aparece "No se pudo copiar. Seleccione el código y cópielo manualmente.". | RF-13 |

---

## 7. Matriz de Trazabilidad

### 7.1 Casos de uso, requisitos funcionales y pruebas

| Caso de Uso | Requisitos Funcionales | Casos de Prueba |
| :--- | :--- | :--- |
| CU-01 Ajustar las esquinas | RF-01, RF-02, RF-05, RF-07, RF-08, RF-09, RF-11 | CP-01 a CP-09, CP-14 |
| CU-02 Copiar el CSS | RF-12 | CP-19 |
| CU-03 Forma compleja | RF-03, RF-04, RF-10 | CP-15 a CP-18 |
| CU-04 Entrada inválida | RF-06 | CP-10 a CP-13 |
| CU-05 Falla al copiar | RF-13 | CP-20 |

### 7.2 Requisitos no funcionales y verificación

| Requisito | Verificación | Método |
| :--- | :--- | :--- |
| RNF-01 Solo cliente | PNF-01 | Ejecutar CP-01 a CP-20 con la pestaña Red de las herramientas de desarrollo abierta: no hay peticiones después de la carga. |
| RNF-02 Separación de la lógica | PNF-02 | Revisión de código: el módulo de generación no importa React y los componentes no construyen la declaración por su cuenta. |
| RNF-03 Pruebas | PNF-03 | Ejecución de las pruebas unitarias con Vitest para los casos definidos en RNF-03. |
| RNF-04 Accesibilidad | PNF-04 | Auditoría con Lighthouse o axe, medición de contraste, recorrido completo con la tecla `Tab`, ajuste de los deslizadores con las flechas y prueba con un lector de pantalla. |
| RNF-05 Compatibilidad | PNF-05 | Ejecutar CP-01 a CP-20 en la versión actual de Chrome. |
| RNF-06 Seguridad de contenido | PNF-06 | Revisión de que el cliente no usa `dangerouslySetInnerHTML`. |

---

## 8. Apéndices

### 8.1 Glosario
* **Clipboard API:** interfaz del navegador que permite leer y escribir el portapapeles. Solo funciona en contextos seguros.
* **Contexto seguro:** página servida por `https://` o desde `localhost`.
* **Declaración CSS:** par propiedad-valor terminado en punto y coma, como `border-radius: 10px;`.
* **DOM (Document Object Model):** representación estructurada del documento HTML que JavaScript modifica en vivo.
* **Forma abreviada (*shorthand*):** propiedad CSS que fija varios valores a la vez. `border-radius` abrevia los radios de las 4 esquinas.
* **Frontend:** capa de presentación y experiencia de usuario; aquí se implementa con React, una librería de JavaScript para construir interfaces a partir de componentes.
* **Función pura:** función cuyo resultado depende solo de sus argumentos y que no produce efectos secundarios.
* **Radio horizontal y vertical:** semiejes de la elipse que redondea una esquina. Si son iguales, la esquina es un arco de circunferencia.
* **Vitest:** biblioteca de pruebas unitarias para proyectos de JavaScript que usan Vite.

### 8.2 Reglas de la forma abreviada

Sean `a`, `b`, `c` y `d` los radios de las esquinas superior izquierda, superior derecha, inferior derecha e inferior izquierda, en ese orden. La lista se abrevia con la primera regla que se cumpla:

| Condición | Resultado | Ejemplo |
| :--- | :--- | :--- |
| `a = b = c = d` | `a` | `20px 20px 20px 20px` → `20px` |
| `a = c` y `b = d` | `a b` | `10px 30px 10px 30px` → `10px 30px` |
| `b = d` | `a b c` | `10px 20px 30px 20px` → `10px 20px 30px` |
| Ninguna de las anteriores | `a b c d` | `10px 20px 30px 40px` → `10px 20px 30px 40px` |

En modo avanzado, la lista horizontal y la vertical se abrevian por separado y se unen con ` / `: `<horizontal> / <vertical>`. Si cada radio vertical es igual al horizontal de su esquina, se escribe solo la lista horizontal (RF-10).

Ejemplo: horizontales `10px 10px 10px 10px` y verticales `20px 20px 20px 20px` → `border-radius: 10px / 20px;`.

### 8.3 Mensajes de la interfaz

| Situación | Mensaje | Requisito |
| :--- | :--- | :--- |
| Se eliminó un carácter que no es un dígito | Solo se permiten números enteros. | RF-06 |
| Se ajustó un valor al máximo en `px` | El valor máximo es 200 px. | RF-06 |
| Se ajustó un valor al máximo en `%` | El valor máximo es 100 %. | RF-06 |
| La copia tuvo éxito | CSS copiado al portapapeles. | RF-12 |
| La copia falló | No se pudo copiar. Seleccione el código y cópielo manualmente. | RF-13 |
