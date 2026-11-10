# Especificación de Requisitos de Software (SRS)

**Proyecto:** Bin2Dec - Conversor de Binario a Decimal (Arquitectura Cliente-Servidor)

**Autor:** Juan David Villegas Anaya

**Organización:** ...

**Fecha:** Noviembre 2026

**Versión:** 1.2

---

## 1. Introducción

### 1.1 Propósito
Este documento define los requisitos funcionales y no funcionales de Bin2Dec. El objetivo del sistema es permitir a los usuarios ingresar secuencias de dígitos binarios (ceros y unos) para obtener su equivalente matemático en el sistema decimal, utilizando una arquitectura cliente-servidor para separar la interfaz de usuario del procesamiento lógico.

### 1.2 Alcance del Sistema
Bin2Dec es una aplicación web que procesa cadenas binarias de 1 a 8 bits (hasta 1 byte, valor máximo 255). Consta de una interfaz de usuario interactiva que valida la entrada en tiempo real y de un servidor remoto que ejecuta el algoritmo de conversión y vuelve a validar los datos.

### 1.3 Fuera de alcance y Trabajo Futuro
Para esta iteración, no se contemplan:
* Conversión inversa (Decimal a Binario).
* Soporte para números binarios con signo (complemento a 1 o 2) ni fracciones (coma flotante).
* Soporte para cadenas mayores a 8 bits.
* Persistencia de datos en base de datos.
* Autenticación o autorización de usuarios.

### 1.4 Definiciones y Acrónimos
Los términos técnicos están en el Apéndice 8.1. Los principales: Frontend (capa de presentación), Backend (capa de lógica en el servidor), API REST (interfaz basada en HTTP), Bit (unidad mínima de información, 0 o 1) y CORS (Cross-Origin Resource Sharing, mecanismo que permite a una página web solicitar recursos de otro origen).

---

## 2. Descripción General

### 2.1 Perspectiva del Producto
El sistema se compone de dos módulos independientes que se comunican con peticiones HTTP asíncronas:
* **Cliente web:**  React, con una arquitectura modular basada en componentes y estilos en CSS.
* **Servidor (API):** Java con Spring Boot 4.x, que expone la lógica de conversión mediante controladores web.

### 2.2 Funciones principales
* Captura de secuencias binarias mediante un formulario web.
* Validación en tiempo real que elimina caracteres inválidos (letras, dígitos mayores que 1, símbolos) y limita la longitud a 8 caracteres.
* Envío de la cadena al servidor mediante una API.
* Revalidación y conversión matemática en el backend.
* Visualización del resultado decimal o de mensajes de error sin recargar la página.

### 2.3 Características de los Usuarios
La herramienta está dirigida a estudiantes de computación, matemáticas y público general sin conocimientos técnicos avanzados. Se asume un nivel básico de navegación web. El usuario espera una interfaz intuitiva, retroalimentación inmediata si comete un error, y no requiere conocer el funcionamiento de las peticiones HTTP subyacentes.

### 2.4 Entorno Operativo
*   **Cliente:** navegador web moderno con JavaScript habilitado (Chrome, Firefox, Edge y Safari, en sus dos últimas versiones principales).
*   **Servidor:** Java 21 o superior (JRE/JDK), requisito de Spring Boot 4.x.

### 2.5 Restricciones de diseño
* El cliente se desarrolla con React (componentes funcionales), CSS y JavaScript.
* El servidor se desarrolla en Java con Spring Boot.
* La longitud máxima de la entrada es de 8 bits.
* No hay base de datos ni autenticación.
* El idioma de la interfaz y de los mensajes es español.

### 2.6 Suposiciones y dependencias
* El navegador del usuario tiene JavaScript habilitado.
* El cliente web puede alcanzar el servidor por red; por defecto el servidor escucha en el puerto 8080.
* Dependencias del servidor: Spring Boot 4.x para la API y JUnit para las pruebas unitarias.
* Dependencias del cliente: React y su herramienta de compilación (por ejemplo, Vite), con Node.js y npm para el desarrollo.

### 2.7 Casos de Uso
| ID | Nombre | Tipo | Descripción |
| :--- | :--- | :--- | :--- |
| CU-01 | Conversión exitosa | Principal | El usuario ingresa un binario válido (ej. 1011), pulsa "Convertir" o Enter, el cliente llama a la API y se muestra el resultado (11). |
| CU-02 | Entrada inválida en la UI | Alterno | El usuario teclea "A", pega "102" o arrastra texto no binario. La UI elimina los caracteres inválidos y muestra una alerta visual inmediata. |
| CU-03 | Falla de red o servidor | Alterno | El servidor está caído, responde con error 5xx o tarda más de 3 s. La UI muestra "Servidor no disponible en este momento.". |
| CU-04 | Consumo directo de la API | Alterno | Un cliente distinto de la UI envía una entrada vacía, demasiado larga o con caracteres inválidos. El servidor responde HTTP 400 con el código de error correspondiente. |

---

## 3. Requisitos Funcionales (RF)

| ID | Nombre | Descripción |
| :--- | :--- | :--- |
| **RF-01** | **Ingreso de Datos** | El sistema debe proporcionar un campo de texto para el ingreso de la cadena binaria. |
| **RF-02** | **Disparo de la conversión** | La conversión se dispara al hacer clic en el botón "Convertir" o al presionar `Enter` con el foco en el campo de texto, siempre que el campo no esté vacío (RF-05). |
| **RF-03** | **Saneamiento de caracteres** | Los componentes React del frontend deben interceptar, mediante sus manejadores de eventos, los eventos de teclado (`keydown`, `input`), pegado (`paste`) y arrastrar-soltar (`drop`), y eliminar de inmediato cualquier carácter distinto de `0` o `1`. |
| **RF-04** | **Notificación visual** | Cuando la UI elimine caracteres inválidos o trunque la entrada por longitud, debe mostrar una advertencia contextual (ej. texto rojo). La advertencia se oculta en el siguiente evento de entrada que no elimine ni trunque nada. |
| **RF-05** | **Campo vacío** | Mientras el campo esté vacío, el botón "Convertir" debe permanecer deshabilitado y la tecla `Enter` no debe enviar ninguna petición a la API. |
| **RF-06a** | **API: vacío o ausente** | El backend debe responder HTTP 400 con `ERR_EMPTY` cuando el parámetro `binary` no se envíe o sea una cadena vacía. |
| **RF-06b** | **API: longitud** | El backend debe responder HTTP 400 con `ERR_LENGTH` cuando la cadena tenga más de 8 caracteres. La longitud mínima de 1 queda cubierta por RF-06a. |
| **RF-06c** | **API: caracteres** | El backend debe responder HTTP 400 con `ERR_CHARS` cuando la cadena contenga caracteres distintos de `0` y `1`. |
| **RF-07** | **Preservación de Ceros** | Los ceros a la izquierda (ej. `0011`) son válidos. Se procesan matemáticamente y se devuelven sin modificar en el campo `binary` |
| **RF-08** | **Cálculo de Conversión** | El backend debe convertir la cadena base 2 a un entero base 10 recorriendo la cadena y sumando las potencias de 2 correspondientes (Apéndice 8.2). |
| **RF-09** | **Renderizado de Éxito** | El cliente debe interpretar el JSON de éxito y mostrar el valor decimal en el área de resultado. |
| **RF-10** | **Manejo de Errores de Red** | Si la petición falla por HTTP 5xx, por tiempo de espera superior a 3 s, por error de red o por bloqueo CORS, el cliente debe mostrar el mensaje "Servidor no disponible en este momento.". |
| **RF-11** | **Precedencia de validaciones** | Si una entrada incumple varias reglas, el backend devuelve solo un error, en este orden: `ERR_EMPTY`, luego `ERR_CHARS`, luego `ERR_LENGTH`. |
| **RF-12** | **Errores 400 en el cliente** | Si la API responde HTTP 400, el cliente debe mostrar el campo `message` de la respuesta en el contenedor de alertas. |
| **RF-13** | **Límite de longitud en la UI** | El campo de entrada no debe admitir más de 8 caracteres. Al pegar o soltar un texto más largo, la UI conserva solo los primeros 8 caracteres válidos. | 
| **RF-14** | **Códigos de estado HTTP** | El backend responde 200 en éxito, 400 por entrada inválida, 405 para métodos distintos de `GET` y 500 con `ERR_INTERNAL` ante excepciones no controladas, sin exponer trazas internas. |

---

## 4. Requisitos No Funcionales (RNF)

| ID | Atributo | Descripción |
| :--- | :--- | :--- |
| **RNF-01** | **Validación duplicada** | La validación existe duplicada **a propósito**: en el frontend por usabilidad (evita peticiones inválidas) y en el backend por seguridad (la API puede recibir peticiones directas sin pasar por la UI). El backend nunca debe confiar en la validación del cliente. |
| **RNF-02** | **Rendimiento** | El percentil 95 del tiempo de respuesta de la API no debe exceder 300 ms. Se mide con 1,000 peticiones válidas secuenciales, después de 100 de calentamiento, con cliente y servidor en la misma máquina de referencia (procesador i5, 8 GB de RAM). |
| **RNF-03** | **CORS** | El backend debe aplicar una política CORS restrictiva con orígenes permitidos configurables fuera del código. En desarrollo: `http://localhost:3000` y `http://localhost:5173`. En producción: solo el origen del cliente desplegado. Nunca se usa el comodín *. |
| **RNF-04** | **Accesibilidad (a11y)** | El `<input>` debe tener una etiqueta `<label>` vinculada, el texto debe tener un contraste mínimo de 4.5:1 (WCAG 2.1, criterio 1.4.3), toda la interfaz debe poder usarse con teclado y el resultado y las alertas deben anunciarse a lectores de pantalla (`aria-live`). |
| **RNF-05** | **Compatibilidad** | La UI debe funcionar sin errores en las dos últimas versiones principales de Chrome, Firefox, Edge y Safari. |
| **RNF-06** | **Pruebas** | La lógica de conversión y validación en Java debe tener al menos 80 % de cobertura con pruebas unitarias (JUnit), incluidos los casos límite: `0`, `11111111`, vacío, 9 bits y caracteres inválidos. |
| **RNF-07** | **Seguridad de transporte y de contenido** | En producción, cliente y API se sirven por HTTPS. El cliente trata la entrada y las respuestas siempre como texto (`interpolación JSX`, nunca `dangerouslySetInnerHTML`). Los mensajes de error no exponen información interna del servidor. |
| **RNF-08** | **Separación de capas** | El layout y la manipulación del DOM se resuelven solo en el cliente React (componentes, CSS y JavaScript); el procesamiento de negocio ocurre solo en Java. |

---

## 5. Interfaces externas

### 5.1  Interfaz de usuario
La pantalla principal es minimalista e incluye:
* Un encabezado que identifica el propósito de la herramienta.
* Un `<input>` de texto centrado, con su `<label>` asociada (RF-01, RNF-04).
* Un botón "Convertir" (RF-02, RF-05).
* Un área tipográfica destacada para el número decimal final (RF-09).
* Un contenedor de visibilidad dinámica para advertencias de validación y errores (RF-04, RF-10, RF-12).

### 5.2 Interfaces de software
El cliente React consume la API del servidor con la función `fetch` del navegador. El servidor es una aplicación Spring Boot 3.x que se ejecuta en Java 21 o superior. No hay otras integraciones.

### 5.3 Interfaces de comunicaciones
Protocolo HTTP (HTTPS en producción), formato JSON codificado en UTF-8. El servidor escucha por defecto en el puerto 8080 y aplica la política CORS de RNF-03. Las peticiones del cliente tienen un tiempo de espera de 3 s (RF-10).

### 5.4 API de comunicación

Se usa **GET** porque la operación solo lee y calcula: es segura (no modifica el estado del servidor) e idempotente. El prefijo `/v1/` permite cambiar el contrato en el futuro sin romper a los clientes existentes. El resultado máximo con 8 bits es 255, por lo que `decimal` es un entero de 32 bits (`int`).

* Endpoint: `GET /api/v1/convert`
* Parámetro de consulta: `binary` (cadena), por ejemplo `?binary=1011`

**Respuestas Exitosas (Status 200 OK): `binary` devuelve la entrada sin modificar (RF-07)**
```json
{
  "decimal": 11,
  "binary": "1011"
}
```

Respuestas de error. Todas usan el formato `{"errorCode": "...", "message": "..."}`. Si una entrada incumple varias reglas se devuelve un solo error, según la precedencia de RF-11.

**Respuestas de error:**
| HTTP | `errorCode` | Cuándo ocurre | `message` |
| :--- | :--- | :--- | :--- |
| 400 | `ERR_EMPTY` | Falta `binary` o está vacío | La entrada no puede estar vacía. |
| 400 | `ERR_CHARS` | Hay caracteres distintos de `0` y `1` | La cadena contiene caracteres inválidos. Solo se permiten 0 y 1. |
| 400 | `ERR_LENGTH` | Más de 8 caracteres | La longitud máxima es de 8 bits. |
| 405 | `ERR_METHOD` | Método distinto de GET | Método no permitido. Use `GET`. |
| 500 | `ERR_INTERNAL` | Excepción no controlada | Error interno del servidor. |

---

## 6. Criterios de Aceptación y Casos de Prueba

El sistema se acepta cuando los 21 casos siguientes pasan. Cada requisito funcional tiene al menos una prueba. Los casos de interfaz se ejecutan en el navegador; los de API, con `curl` o una herramienta equivalente.

| ID Prueba | Descripción / Requisito | Entrada (UI / API) | Salida Esperada | Requisitos |
| :--- | :--- | :--- | :--- | :--- |
| **CP-01** | Conversión normal | `1010` + Click botón | LA UI muestra `10`. HTTP 200. | RF-01, RF-08, RF-09 |
| **CP-02** | Límite superior | `11111111` | La UI muestra `255`. HTTP 200. | RF-08 |
| **CP-03** | Límite inferior | `0` | La UI muestra `0`. HTTP 200. | RF-08 |
| **CP-04** | Ceros a la izquierda | `0011` | La UI muestra `3`. El JSON devuelve `"binary": "0011"`. | RF-07 |
| **CP-05** | Tecla Enter | `11` + `Enter` | La UI muestra `3`. | RF-02 |
| **CP-06** | Tecleo inválido | Teclear `A` en el campo vacío | El campo sigue vacío y aparece la advertencia roja | RF-03, RF-04 |
| **CP-07** | Pegado inválido | Pegar `1021` | El campo queda en `101` y aparece la advertencia. | RF-03, RF-04 |
| **CP-08** | Arrastrar y soltar inválido | Soltar `1a1` en el campo | El campo queda en `11` y aparece la advertencia. | RF-03, RF-04 |
| **CP-09** | La advertencia se oculta | Después de CP-07, teclear `0` | El campo queda en `1010` y la advertencia desaparece. | RF-04 |
| **CP-10** | Campo vacío | Campo vacío, clic en el botón y `Enter` | El botón está deshabilitado y no se envía ninguna petición. | RF-05 |
| **CP-11** | Límite de longitud en la UI |Pegar `1111111111` (10 caracteres) | El campo queda en `11111111` y aparece la advertencia. | RF-13, RF-04 |
| **CP-12** | API: cadena vacía | `?binary=` | HTTP 400. `ERR_EMPTY` | RF-06a |
| **CP-13** | API: parámetro ausente | `GET /api/v1/convert` sin consulta | HTTP 400. `ERR_EMPTY` | RF-06a |
| **CP-14** | API: longitud excedida | `?binary=100000000` | HTTP 400. `ERR_LENGTH` | RF-06b |
| **CP-15** | API: caracteres inválidos | `?binary=101a` y `?binary=102` | HTTP 400. `ERR_CHARS` en ambos. | RF-06c |
| **CP-16** | API: precedencia | `?binary=1021111111` (10 caracteres, uno inválido) | HTTP 400. `ERR_CHARS` (no `ERR_LENGTH`) | RF-11 |
| **CP-17** | API: método no permitido | `POST /api/v1/convert?binary=11` | HTTP 405. `ERR_METHOD` | RF-14 |
| **CP-18** | Error 400 en el cliente | API simulada que responde 400 con `ERR_CHARS` | El contenedor de alertas muestra el `message` recibido. | RF-12 |
| **CP-19** | Servidor apagado | Servidor detenido; `101` + "Convertir" | La UI muestra "Servidor no disponible en este momento.". | RF-10 |
| **CP-20** | Error 500 | API simulada que responde 500 con `ERR_INTERNAL` | La UI muestra "Servidor no disponible en este momento.". | RF-10, RF-14 |
| **CP-21** | Tiempo de espera | API simulada que responde con más de 3 s de retraso | A los 3 s la UI muestra "Servidor no disponible en este momento.". | RF-10 |

---

## 7. Matriz de Trazabilidad

### 7.1 Casos de uso, requisitos funcionales y pruebas

| Caso de Uso | Requisitos Funcionales | Casos de Prueba |
| :--- | :--- | :--- |
| CU-01 Conversión exitosa | RF-01, RF-02, RF-07, RF-08, RF-09 | CP-01 a CP-05 |
| CU-02 Entrada inválida en la UI | RF-03, RF-04, RF-05, RF-13 | CP-06 a CP-11 |
| CU-03 Falla de red o servidor | RF-10 | CP-19, CP-20, CP-21 |
| CU-04 Consumo directo de la API | RF-06a, RF-06b, RF-06c, RF-11, RF-14 | CP-12 a CP-17 |
| Sin caso de uso (respuesta 400 inesperada en la UI) | RF-12 | CP-18 |

### 7.2 Requisitos no funcionales y verificación

| Requisito | Verificación | Método |
| :--- | :--- | :--- |
| RNF-01 Validación duplicada | PNF-01 | Revisión de código, más CP-12 a CP-16 ejecutados directamente contra la API sin la UI. |
| RNF-02 Rendimiento | PNF-02 | Prueba de carga (JMeter, k6 u otra) con 1,000 peticiones válidas tras 100 de calentamiento; el percentil 95 debe ser de 300 ms o menos. |
| RNF-03 CORS | PNF-03 | Petición con un origen permitido (la respuesta incluye `Access-Control-Allow-Origin`) y con un origen no listado (el navegador la bloquea); revisión de la configuración. |
| RNF-04 Accesibilidad | PNF-04 | Auditoría con Lighthouse o axe, medición de contraste, recorrido completo con la tecla `Tab` y prueba con un lector de pantalla. |
| RNF-05 Compatibilidad | PNF-05 | Ejecutar CP-01 a CP-11 en las dos últimas versiones de Chrome, Firefox, Edge y Safari. |
| RNF-06 Pruebas | PNF-06 | Informe de cobertura (por ejemplo JaCoCo) con 80 % o más en la lógica de conversión y validación. |
| RNF-07 Seguridad de transporte y de contenido | PNF-07 | Revisión de que el cliente no usa `dangerouslySetInnerHTML`, comprobación de HTTPS en el despliegue y de que una respuesta 500 no incluye trazas. |
| RNF-08 Separación de capas | PNF-08 | Revisión de código: sin lógica de conversión en JavaScript ni manipulación del DOM en Java. |

---

## 8. Apéndices

### 8.1 Glosario
* **API REST:** interfaz de programación basada en el protocolo HTTP.
* **Backend:** capa de lógica de negocio y procesamiento en el servidor.
* **Bit:** unidad mínima de información, con valor 0 o 1.
* **CORS (Cross-Origin Resource Sharing):** mecanismo que permite que una página web solicite recursos a un origen distinto del suyo.
* **DOM (Document Object Model):** representación estructurada del documento HTML que JavaScript modifica en vivo.
* **Endpoint:** URL específica donde una API recibe peticiones.
* **Frontend:** capa de presentación y experiencia de usuario; aquí se implementa con React, una librería de JavaScript para construir interfaces a partir de componentes.
* **HTTP status code:** código de 3 dígitos con el resultado de una petición (`200` éxito, `400` error del cliente, `405` método no permitido, `500` error del servidor).
* **Idempotente:** propiedad de una operación según la cual repetir la misma petición produce en el servidor el mismo efecto que ejecutarla una vez.
* **Método seguro:** método HTTP que no modifica el estado del servidor, como `GET`.
* **JSON (JavaScript Object Notation):** formato ligero de intercambio de datos basado en texto.
* **JUnit:** biblioteca de pruebas unitarias para Java.
* **Percentil 95 (p95):** valor por debajo del cual queda el 95 % de las mediciones.
* **Spring Boot:** framework de Java para construir aplicaciones y APIs web.
* **Timeout (tiempo de espera):** tiempo máximo que el cliente espera una respuesta antes de darla por fallida.

### 8.2 Ejemplo Matemático del Algoritmo

Conversión de la cadena `1011` (longitud = 4):
1. **Posición 0 (Bit `1`):** Potencia $2^{(4-1-0)} = 2^3 = 8$. Suma parcial = $8$.
2. **Posición 1 (Bit `0`):** Potencia $2^{(4-1-1)} = 2^2 = 4$. Suma parcial = $8 + 0 = 8$.
3. **Posición 2 (Bit `1`):** Potencia $2^{(4-1-2)} = 2^1 = 2$. Suma parcial = $8 + 2 = 10$.
4. **Posición 3 (Bit `1`):** Potencia $2^{(4-1-3)} = 2^0 = 1$. Suma parcial = $10 + 1 = 11$.
*Resultado final:* **11**

### 8.3 Mensajes de la interfaz

Los mensajes de error de la API están en la sección 5.4. Los mensajes que genera el propio cliente son:

| Situación | Mensaje | Requisito |
| :--- | :--- | :--- |
| Se eliminó un carácter distinto de 0 o 1 | Solo se permiten los dígitos 0 y 1. | RF-04 |
| Se truncó la entrada a 8 caracteres | La longitud máxima es de 8 dígitos. | RF-04, RF-13 |
| Falla de red, 5xx, timeout o CORS | Servidor no disponible en este momento. | RF-10 |
