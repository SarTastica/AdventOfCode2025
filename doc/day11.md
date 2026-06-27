---
title: "Advent of Code - Día 11: Flujo de Datos en el Reactor"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output:
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 11: Flujo de Datos en el Reactor (Análisis Arquitectónico)

## Descripción del Problema
El Día 11 modela una red de dispositivos (`NetworkGraph`) donde se requiere contar el flujo de datos. La complejidad reside en la travesía de grafos, gestionando tanto el conteo simple de caminos (**Parte A**) como la validación de nodos obligatorios (**Parte B**). El reto ha sido implementar una arquitectura que soporte algoritmos de búsqueda (DFS) eficientes sin acoplar la estructura del grafo con la lógica de resolución.

---

## 1. Patrones de Diseño

Los patrones de diseño son soluciones típicas a problemas comunes en el desarrollo.

### Patrón Factory Method
* **Justificación:** Se utiliza para encapsular la creación de objetos, evitando llamar directamente al constructor.
* **Aplicación:** El `NetworkParser` actúa como una factoría que toma la lista de cadenas de texto y devuelve una estructura `NetworkGraph` completamente construida. Esto centraliza la lógica de parseo y validación.

### Patrón Iterator
* **Justificación:** Permite acceder a elementos de un objeto agregado sin exponer su representación interna, mejorando la modularidad.
* **Aplicación:** Se implementa de forma nativa mediante la API de Streams de Java en los métodos de cálculo de caminos (`graph.getNeighbors(...).stream()`). Esto permite recorrer las aristas del grafo de forma eficiente y uniforme, separando la lógica de iteración de la estructura de datos.

---

## 2. Inyecciones y Acoplamiento

### Principio de Inversión de Dependencias (DIP)
* **Justificación:** Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación:** El `ReactorManager` no conoce la implementación de `MemoizedDfsPathCounter` o `MandatoryNodesPathCounter`. Depende exclusivamente de la interfaz `PathCounterStrategy`. Las estrategias se inyectan en el constructor, garantizando que el sistema sea flexible y testeable.

### Bajo Acoplamiento
* **Justificación:** Se refiere a diseñar componentes con pocas interdependencias entre sí.
* **Aplicación:** La separación entre `NetworkGraph` (datos) y `PathCounterStrategy` (lógica) asegura que el grafo no tenga ni idea de cómo se cuentan los caminos. Esto minimiza el impacto de los cambios: si la estructura del grafo cambia, el algoritmo no requiere modificaciones.

---

## 3. Principios de Diseño

Los principios de diseño actúan como normas que guían la aplicación de los fundamentos.

### Principio de Responsabilidad Única (SRP)
* **Justificación:** Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión.
* **Aplicación:**
  * `NetworkParser`: Única razón de cambio: que el formato del fichero de entrada cambie.
  * `NetworkGraph`: Única razón de cambio: que la representación matemática del grafo cambie.
  * `PathCounterStrategy`: Única razón de cambio: que las reglas del puzzle (Parte A/B) cambien.

### Principio Abierto/Cerrado (OCP)
* **Justificación:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación:** Si mañana se añade una Parte C con nuevas reglas, no modificaremos `ReactorManager`. Solo extenderemos el sistema creando una nueva clase que implemente `PathCounterStrategy`.

### Principio de no repetir código (DRY)
* **Justificación:** Cada pieza de conocimiento en un software debe tener una representación única inequívoca.
* **Aplicación:** La lógica de representación del grafo (`NetworkGraph`) es compartida. Al centralizarla, eliminamos redundancia en el parseo y manejo de datos entre la Parte A y la Parte B.

---

## 4. Normas, Leyes y Fundamentos

Los fundamentos son las cualidades inherentes y deseables en todo sistema de software.

### Alta Cohesión
* **Justificación:** Las partes de un módulo deben estar estrechamente relacionadas y enfocadas en una sola tarea.
* **Aplicación:** El uso de `record State(String node, boolean seenReq1, boolean seenReq2)` en la Parte B agrupa los datos necesarios para el estado de búsqueda de forma cohesiva, manteniendo la lógica de la "clave compuesta" donde reside el dato.

### Abstracción e Inmutabilidad
* **Justificación:** La abstracción oculta detalles complejos, y la inmutabilidad garantiza integridad.
* **Aplicación:** El uso de `Map.copyOf(adjacencyList)` en `NetworkGraph` crea una copia defensiva. Una vez creado, el grafo no puede ser corrompido, lo que proporciona una base segura para la búsqueda DFS.

### Ley de Demeter
* **Justificación:** Una unidad de software debe tener conocimiento limitado sobre otras unidades; no aceptes "caramelos de extraños".
* **Aplicación:** Los algoritmos de travesía (`dfs`) nunca acceden directamente a la estructura interna de `adjacencyList` del grafo. Utilizan el método intermedio `getNeighbors(...)`, manteniendo el acoplamiento al mínimo permitido por el diseño.