---
title: "Advent of Code - Día 11: La Red de Reactores"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 11: La Red de Reactores (Grafos y Memoización)

## Descripción del Problema
En el Día 11 analizamos el flujo de datos a través de una red de reactores. El sistema está representado como un grafo dirigido. En la **Parte A**, el objetivo es contar cuántos caminos únicos y válidos existen desde un nodo de inicio hasta un nodo destino. En la **Parte B**, el nivel de exigencia sube: debemos contar únicamente aquellos caminos que pasen obligatoriamente por dos nodos críticos específicos antes de llegar a su destino, obligándonos a rastrear el estado del viaje durante la travesía.

---

## 1. Patrones de Diseño

### Patrón Iterator (Uso Implícito)
A la hora de explorar el grafo, hacemos un uso implícito del patrón Iterator mediante los bucles `for-each` de Java (`for (String neighbor : graph.getNeighbors(current))`).
Este patrón proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente[cite: 52]. Su uso separa la lógica de iteración (recorrer los vecinos) de las estructuras de datos (la lista interna), mejorando así la modularidad del código[cite: 54].

---

## 2. Inyecciones y Acoplamiento

### Inyección de Dependencias, DIP y COI
La clase `ReactorManager` no contiene la lógica para explorar el grafo, sino que exige que se le inyecte el comportamiento a través de su constructor: `public ReactorManager(PathCounterStrategy strategy)`.
* Esto respeta el **Principio de Inversión de Dependencias (DIP)**: los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones[cite: 36].
* También aplica el **Principio de Composición sobre Herencia (COI)**: en lugar de heredar código de una clase base, se recomienda tener un objeto con esa funcionalidad como propiedad[cite: 37].
* Finalmente, garantiza un **Bajo Acoplamiento**, al diseñar módulos o componentes que tienen pocas interdependencias[cite: 17].

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión[cite: 30].
* **Dónde está en el código:** 1. `NetworkParser`: Su única responsabilidad es convertir las líneas de texto en un diccionario en memoria.
    2. `NetworkGraph`: Su única responsabilidad es custodiar los datos topológicos.
    3. `MemoizedDfsPathCounter`: Su única responsabilidad es aplicar el algoritmo matemático DFS.

### Principio Abierto Cerrado (OCP)
Las clases deben estar abiertas para la extensión, pero cerradas para la modificación[cite: 31, 32].
* **Dónde está en el código:** Para resolver la Parte B (que requería una nueva regla de negocio para visitar nodos obligatorios), no se modificó ni una sola línea de `ReactorManager`. El motor se extendió pasándole la nueva clase `MandatoryNodesPathCounter`, manteniendo el sistema cerrado a modificaciones destructivas.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Abstracción
La abstracción consiste en ocultar los detalles complejos detrás de una interfaz simple[cite: 21].
* **Dónde está en el código:** La clase `NetworkGraph` encapsula un complejo `Map<String, List<String>>`. En lugar de exponer este mapa para que el orquestador lidie con claves nulas o listas vacías, expone un método limpio: `getNeighbors(String nodeId)`. Si el nodo no existe, devuelve una lista vacía de forma segura (`List.of()`), ocultando esta protección interna.

### Ley de Demeter
Sugiere que una unidad de software debe tener un conocimiento limitado sobre otras unidades ("no aceptes caramelos de extraños")[cite: 38, 39].
* **Dónde está en el código:** El algoritmo DFS no interactúa directamente con el diccionario interno de `NetworkGraph` (ej. `graph.adjacencyList.get(...)`). En su lugar, pide la información directamente a la entidad que conoce (`graph.getNeighbors(...)`), respetando los límites de conocimiento del objeto.

### Fundamentos: Alta Cohesión y Código Expresivo
En la Parte B, necesitamos memorizar tres variables a la vez para saber si ya hemos evaluado un camino: el nodo actual, si vimos el requisito 1 y si vimos el requisito 2.
* **Dónde está en el código:** En lugar de concatenar cadenas confusas (ej. `current + "_" + seen1 + "_" + seen2`), creamos un `record State(String node, boolean seenReq1, boolean seenReq2)`. Esto promueve la **Alta Cohesión** (partes estrechamente relacionadas enfocadas en una única tarea) [cite: 16] y logra un **Código Expresivo** que debería ser claro y comprensible, facilitando la lectura y el mantenimiento[cite: 20]. 