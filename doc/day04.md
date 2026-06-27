---
title: "Advent of Code - Día 4: Optimización del Almacén (Arquitectura y Principios)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 4: Optimización de Carretillas en el Almacén

## Descripción del Problema
En el Día 4, debemos simular el proceso de limpieza de un almacén (representado por una cuadrícula o `Grid`). En la **Parte A**, necesitamos contar cuántos elementos (`rolls`) son accesibles según una regla matemática específica. En la **Parte B**, la simulación se vuelve iterativa: debemos retirar los elementos accesibles en oleadas hasta que no quede ninguno. El reto arquitectónico es evitar que la simulación iterativa corrompa el estado del almacén y mantener el código libre de código duplicado entre ambas partes.

---

## 1. Patrones de Diseño

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software.

### Patrón Iterator
* **Teoría:** Proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente. El patrón Iterator separa la lógica de iteración de las estructuras de datos, mejorando así la modularidad del código.
* **Aplicación en el código:** En lugar de obligar a los optimizadores a hacer bucles `for` anidados sobre la matriz `char[][]`, la clase `Grid` expone el método `getRollPositions()`, el cual devuelve un `Stream<Position>`.
* **Justificación:** Esto permite a los clientes recorrer y manipular conjuntos de datos de manera eficiente y uniforme. El optimizador puede simplemente hacer un `.filter(...)` sin conocer los detalles de las filas y columnas internas del mapa.

---

## 2. Principios de Diseño

Los principios de diseño son normas o guías que orientan cómo se deben aplicar los fundamentos en la práctica.

### Principio de Inversión de Dependencias (DIP)
* **Teoría:** Módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación en el código:** `ForkliftOptimizer` y `IterativeForkliftOptimizer` (alto nivel) no dependen de la implementación concreta `FewerThanFourRule` (bajo nivel). Dependen exclusivamente de la interfaz `AccessibilityRule`.
* **Justificación:** Los orquestadores no instancian sus propias reglas matemáticas. Las reciben inyectadas por el constructor, logrando un diseño flexible.

### Principio Abierto Cerrado (OCP)
* **Teoría:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación en el código:** Si en el futuro las reglas de accesibilidad del almacén cambian (por ejemplo, exigir que un bloque tenga solo 2 vecinos), los optimizadores no tendrán que ser modificados. Simplemente se creará una nueva clase que implemente `AccessibilityRule` (abierto a extensión).

### Principio de no repetir código (DRY)
* **Teoría:** Cada pieza de conocimiento en un software debería tener una representación única inequívoca.
* **Aplicación en el código:** Se centralizó el conocimiento compartiendo la clase `Grid`, el record `Position` y la estrategia `FewerThanFourRule` en el paquete general `software.aoc.day04`. Ambas partes consumen estas clases, evitando la duplicación de código.

---

## 3. Fundamentos de Diseño

Los fundamentos de diseño son los conceptos o creencias básicas que subyacen en la práctica del diseño de software.

### Abstracción y Modularidad
* **Teoría:** La abstracción consiste en ocultar los detalles complejos detrás de una interfaz simple. La modularidad promueve la división del sistema en módulos o componentes independientes.
* **Aplicación en el código:** El manejo interno del mapa bidimensional (`char[][] map`) está completamente encapsulado dentro de la clase `Grid`.
* **Justificación:** Los orquestadores no saben cómo se parsean las cadenas de texto ni cómo se clona la matriz. Solo interactúan con los métodos públicos limpios de la clase, manteniendo los módulos independientes.

### Alta Cohesión
* **Teoría:** Refiere a la idea de que las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** Se eliminó la gestión de primitivos sueltos (como `int row, int col`) al crear el record `Position`. Esta entidad agrupa las coordenadas lógicamente, dándole cohesión espacial a los datos. Por su parte, la clase `FewerThanFourRule` tiene una única tarea matemática: evaluar los vecinos en 8 direcciones.

### Código Expresivo (Inmutabilidad del Estado)
* **Teoría:** El código debería ser claro y comprensible, facilitando la lectura y el mantenimiento.
* **Aplicación en el código:** Para la Parte B, la clase `Grid` fue diseñada para evitar la mutabilidad. En lugar de borrar los caracteres de la matriz original (`map[r][c] = '.'`), el método `removeRolls` clona la matriz y devuelve una nueva instancia de `Grid`.
* **Justificación:** Modificar una matriz global durante un bucle iterativo causa efectos secundarios impredecibles que dificultan la comprensión del flujo. Devolver un nuevo estado en cada iteración hace que la simulación se lea como una transformación matemática pura y cristalina.