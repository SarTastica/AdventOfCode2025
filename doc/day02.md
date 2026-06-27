---
title: "Advent of Code - Día 2: La Tienda de Regalos (Validaciones y Principios SOLID)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 2: La Tienda de Regalos (Análisis Arquitectónico)

## Descripción del Problema
El Día 2 nos plantea el reto de procesar una base de datos de la tienda de regalos (`GiftShop`), donde debemos validar identificadores numéricos generados a partir de rangos. La complejidad radica en que las reglas de validación cambian entre la **Parte A** y la **Parte B**. El objetivo del diseño es crear un orquestador central que pueda leer los rangos y sumar los resultados sin acoplarse a las reglas matemáticas específicas de cada parte.

---

## 1. Patrones de Diseño Aplicados

Según la teoría, los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software. Actúan como plantillas para resolver problemas contextuales.

### Patrón Iterator
* **Teoría:** Proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente. Separa la lógica de iteración de las estructuras de datos, mejorando la modularidad.
* **Aplicación en el código:** Se aplica de forma nativa y moderna a través de la API de Streams de Java. En la clase `GiftShop`, métodos como `Arrays.stream(...)` y `flatMapToLong(Range::stream)` recorren y procesan las secuencias de IDs y rangos de forma secuencial y transparente.
* **Justificación:** Gracias a este patrón, el orquestador no necesita gestionar índices de matrices ni controlar bucles `for` manualmente. Oculta la complejidad de la estructura subyacente, permitiendo encadenar operaciones (`map`, `filter`, `sum`) de forma limpia, eficiente y libre de errores de límites de iteración.

## 2. Principios de Diseño

Los principios de diseño son normas o guías que orientan cómo se deben aplicar los fundamentos en la práctica. Actúan como restricciones o reglas que ayudan a alcanzar los fundamentos

### Principio de Inversión de Dependencias (DIP)
* **Teoría:** Módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación en el código:** En lugar de que la clase `GiftShop` (alto nivel) dependa directamente de `RepeatedHalfRule` o `RepeatedPatternRule` (bajo nivel), depende exclusivamente de la abstracción `ValidationRule`.
* **Justificación:** Esto permite que el orquestador principal delegue el comportamiento de filtrado. Si la lógica de las expresiones regulares cambia, el módulo principal de la tienda de regalos no sufre ningún impacto.

### Principio Abierto Cerrado (OCP)
* **Teoría:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación en el código:** Si los requisitos del problema añaden una nueva regla de validación de IDs, el código de `GiftShop` y `Range` no se modificará en absoluto (cerrado a modificación). Simplemente crearemos una nueva clase que implemente la interfaz `ValidationRule` y se la inyectaremos (abierto a extensión).

### Principio de no repetir código (DRY) y SRP
* **Teoría:** DRY indica que cada pieza de conocimiento en un software debería tener una representación única inequívoca. SRP establece que cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión
* **Aplicación en el código:** En diseños menos maduros, se duplicaría toda la clase de la base de datos para separar la Parte A de la Parte B. Nuestra arquitectura centraliza la lectura, el desempaquetado de rangos y la suma matemática en una única clase general (`GiftShop`). Cada regla de validación reside en su propia clase, teniendo una única responsabilidad y eliminando la redundancia.

---

## 3. Fundamentos de Diseño

Los fundamentos de diseño son los conceptos o creencias básicas que subyacen en la práctica del diseño de software. Son las cualidades inherentes que se consideran deseables en cualquier sistema de software

### Alta Cohesión
* **Teoría:** Refiere a la idea de que las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** Se extrajo la manipulación de las cadenas de texto (`split("-")`) y la lógica de los límites de inicio y fin a un record inmutable dedicado llamado `Range`. De este modo, los datos de los límites y el método para generar un flujo de números (`stream()`) viven juntos de forma altamente cohesiva.

### Bajo Acoplamiento y Abstracción
* **Teoría:** Bajo acoplamiento es la idea de diseñar módulos o componentes que tienen pocas interdependencias. La abstracción consiste en ocultar los detalles complejos detrás de una interfaz simple.
* **Aplicación en el código:** Al utilizar la interfaz `ValidationRule`, ocultamos la complejidad algorítmica de las Expresiones Regulares. Las clases que definen las reglas no saben de dónde vienen los IDs, y la clase que genera los IDs no sabe cómo se validan, logrando interdependencias mínimas.

### Código Expresivo
* **Teoría:** El código debería ser claro y comprensible, facilitando la lectura y el mantenimiento.
* **Aplicación en el código:** * Se sustituyeron los bucles anidados por la API de Streams de Java (`flatMapToLong`, `filter`, `sum`), haciendo que la lectura de la secuencia de operaciones sea completamente declarativa.
  * Las validaciones matemáticas complejas se sustituyeron por Expresiones Regulares (Regex) puras (ej. `^([0-9]+)\\1+$`). Estas expresiones reducen drásticamente la complejidad ciclomática del algoritmo, haciendo el código más claro y conciso.

---

## 4. Inyecciones de Dependencia

* **Aplicación en el código:** La clase `GiftShop` recibe la interfaz `ValidationRule` a través de su constructor (`this.rule = rule;`).
* **Justificación:** La inyección por constructor es el mecanismo arquitectónico que hace posible el Principio de Inversión de Dependencias (DIP). Garantiza que las dependencias necesarias para que el objeto funcione estén presentes desde el momento de su instanciación, evitando estados inválidos y facilitando la creación de pruebas unitarias al permitir inyectar reglas simuladas (mocks).