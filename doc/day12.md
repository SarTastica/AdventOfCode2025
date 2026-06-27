---
title: "Advent of Code - Día 12: Empaquetado de Poliominós (Análisis Arquitectónico)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 12: Empaquetado de Poliominós (Análisis de Diseño)

## Descripción del Problema
El Día 12 aborda un problema de *2D Bin Packing* con poliominós. El objetivo es determinar si una lista de figuras (piezas de Tetris) puede encajar en un tablero sin solapamientos. Dado que este es un problema NP-Hard, la arquitectura debe ser extremadamente eficiente. Hemos implementado un motor de búsqueda CSP (*Constraint Satisfaction Problem*) apoyado en *bitmasking* para garantizar el rendimiento.

---

## 1. Patrones de Diseño

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software.

### Patrón Factory Method
* **Concepto:** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método estático que encapsula la creación del objeto.
* **Aplicación:** La clase `PackingParser` implementa este patrón en el método `parse()`. Este método actúa como factoría que transforma la entrada de texto cruda en una estructura compleja de `ParsedData` (variaciones de formas y regiones), ocultando la lógica de parseo a las clases de negocio.

### Patrón Iterator
* **Concepto:** Proporciona una manera de acceder secuencialmente a los elementos de una colección sin exponer su representación subyacente, separando la lógica de iteración de la estructura de datos.
* **Aplicación:** El `RecursiveCspSolver` y el `BitmaskPlacementGenerator` utilizan flujos de datos (`Stream`) y listas para iterar sobre las `validPlacements`.
* **Justificación:** Esto permite al solver recorrer las opciones de colocación sin conocer cómo se generaron las piezas ni cómo se almacenan, cumpliendo el principio de separar la lógica de iteración de los datos.

---

## 2. Inyecciones y Acoplamiento

### Inyección de Dependencias
* **Aplicación:** El `DfsPackingOptimizer` recibe sus dependencias (`PackingHeuristics`, `BitmaskPlacementGenerator`, `RecursiveCspSolver`) a través del constructor.
* **Justificación:** Esto permite que el optimizador esté desacoplado de las implementaciones concretas. Si necesitamos cambiar el algoritmo de resolución (por ejemplo, cambiar el `RecursiveCspSolver` por una versión con Heurística diferente), no es necesario modificar la clase orquestadora. Esto contribuye al **Bajo Acoplamiento**, el cual busca diseñar módulos con pocas interdependencias.

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
* **Concepto:** Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión.
* **Aplicación:** * `Polyomino` solo se encarga de la geometría y variaciones.
  * `Grid` solo gestiona los límites espaciales.
  * `RecursiveCspSolver` solo se ocupa del backtracking.
* **Justificación:** Al separar estas responsabilidades, cada clase es más fácil de mantener y testear individualmente.

### Principio Abierto/Cerrado (OCP)
* **Concepto:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación:** Gracias a la interfaz `PackingStrategy`, nuestro sistema está abierto a nuevas estrategias de empaquetado sin tener que modificar el código existente en `PackingParser` o `Grid`.

### Principio de Composición sobre Herencia (COI)
* **Concepto:** Se recomienda tener un objeto con funcionalidad como propiedad en lugar de heredar de una clase base.
* **Aplicación:** `Polyomino` utiliza composición (`Set<Point>`) en lugar de heredar de una clase geométrica genérica. Esto aporta flexibilidad y ligereza al dominio.

---

## 4. Normas, Leyes y Fundamentos

### Alta Cohesión
* **Concepto:** Refiere a que las partes de un módulo deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación:** El `record Polyomino` encapsula su estado y su lógica de transformación (`normalize`, `rotate90`, `flipHorizontal`). Esta entidad es altamente cohesiva porque no necesita ayuda externa para definir su propia forma.

### Abstracción
* **Concepto:** Consiste en ocultar los detalles complejos detrás de una interfaz simple.
* **Aplicación:** La interfaz `PackingStrategy` abstrae la complejidad de la búsqueda CSP. El usuario del sistema solo necesita invocar `canPackAll(...)` sin preocuparse de si se están usando `BitSets`, recursión o heurísticas avanzadas.

### Ley de Demeter
* **Concepto:** Una unidad de software debe tener conocimiento limitado sobre otras unidades; "no aceptes caramelos de extraños".
* **Aplicación:** El solver de restricciones no intenta acceder a los campos internos de `Polyomino` o `Grid` mediante llamadas encadenadas. Solicita la información necesaria (como los `BitSets` de `ShapeGroup`) mediante contratos claros, manteniendo el conocimiento de la estructura interna del sistema al mínimo estricto.