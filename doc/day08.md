---
title: "Advent of Code - Día 8: Conectividad de Grafos y DSU"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output:
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 8: Nodos 3D y Conectividad de Redes

## Descripción del Problema
En el Día 8 nos enfrentamos a un problema clásico de grafos: conectar una serie de puntos en un espacio tridimensional (`Point3D`) evaluando sus distancias. La **Parte A** nos exige encontrar el tamaño de los circuitos aislados tras un número limitado de conexiones. La **Parte B** requiere averiguar el momento exacto en el que todo el grafo queda completamente conectado. La dificultad radica en el altísimo coste computacional de verificar conexiones cíclicas, lo que exige una arquitectura modular que soporte estructuras de datos avanzadas como el *Disjoint Set Union (DSU)*.

---

## 1. Patrones de Diseño Aplicados

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software.

### Patrón Factory Method
* **Teoría:** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método estático que encapsula la creación del objeto.
* **Aplicación en el código:** Se implementa en la clase `GraphBuilder` a través del método estático `buildSortedConnections(List<Point3D> junctions)`.
* **Justificación y Utilidad:** Este método actúa como una fábrica especializada. Toma una lista de puntos, calcula todas las permutaciones posibles de aristas, calcula la distancia euclidiana al cuadrado para cada par, ordena la lista y devuelve la estructura final. Encapsular esto libera a los optimizadores de tener que escribir complejos bucles de construcción de grafos.

### Patrón Iterator
* **Teoría:** Proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente. El patrón Iterator separa la lógica de iteración de las estructuras de datos, mejorando así la modularidad del código.
* **Aplicación en el código:** Se observa en el `PlaygroundOptimizer` con la instrucción `circuitSizes.stream().limit(3).mapToLong(...).reduce(...)`.
* **Justificación y Utilidad:** Permite procesar la colección de tamaños de circuitos de forma declarativa, abstrayendo el manejo de índices y bucles, lo cual reduce drásticamente el riesgo de errores (Off-By-One errors).

---

## 2. Inyecciones y Bajo Acoplamiento

### Bajo Acoplamiento Estructural
* **Teoría:** Bajo acoplamiento es la idea de diseñar módulos o componentes que tienen pocas interdependencias.
* **Aplicación en el código:** La clase `CircuitUnionFind` encapsula la lógica pura de conjuntos disjuntos y vectores de estado (`parent`, `size`), totalmente desacoplada de la semántica de los "puntos 3D" o de las coordenadas espaciales.
* **Justificación y Utilidad:** El motor de grafos no sabe qué está conectando (podrían ser ordenadores, ciudades o taquiones). Al separar la topología del grafo de los datos de dominio (`Point3D`), el nivel de interdependencia es mínimo, permitiendo reutilizar `CircuitUnionFind` en cualquier otro proyecto matemático.

---

## 3. Principios de Diseño

Los principios de diseño son normas o guías que orientan cómo se deben aplicar los fundamentos en la práctica.

### Principio de Responsabilidad Única (SRP)
* **Teoría:** Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión.
* **Aplicación en el código:** La arquitectura del Día 8 divide el sistema de forma milimétrica:
    1. `JunctionParser`: Responsable exclusivo de la transformación de texto a entidades del dominio.
    2. `GraphBuilder`: Responsable exclusivo de generar y ordenar las aristas.
    3. `CircuitUnionFind`: Responsable exclusivo de evaluar si dos nodos pertenecen al mismo conjunto.

### Principio de no repetir código (DRY)
* **Teoría:** Cada pieza de conocimiento en un software debería tener una representación única inequívoca.
* **Aplicación en el código:** La lógica de cálculo de distancias y ordenamiento de conexiones es requerida tanto por la Parte A como por la Parte B. Al extraerla a la clase compartida `GraphBuilder`, cumplimos con el principio DRY. Cualquier corrección en el motor de grafos beneficiará automáticamente a ambas implementaciones.

---

## 4. Normas, Leyes y Fundamentos

Los fundamentos de diseño son los conceptos o creencias básicas que subyacen en la práctica del diseño de software.

### Alta Cohesión
* **Teoría:** Refiere a la idea de que las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** El `record Point3D` no es un contenedor pasivo; encapsula la lógica de su propia distancia geométrica (`distanceSquaredTo`). De igual forma, el `record Connection` implementa su propia interfaz comparadora (`Comparable<Connection>`), garantizando que las aristas sepan cómo ordenarse a sí mismas basándose en el peso, sin delegar esta lógica en clases externas.

### Código Expresivo (Inmutabilidad)
* **Teoría:** El código debería ser claro y comprensible, facilitando la lectura y el mantenimiento.
* **Aplicación en el código:** El uso de `Point3D` y `Connection` como *records* inmutables de Java asegura que, una vez que un punto o una arista se cargan en memoria, sus coordenadas y pesos son inalterables. Esto facilita la lectura y previene efectos secundarios impredecibles durante el procesamiento intensivo del algoritmo.

