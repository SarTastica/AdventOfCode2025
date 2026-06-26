---
title: "Advent of Code - Día 8: Conectando Nodos"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 8: Conectando Nodos (Optimización de Grafos)

## Descripción del Problema
En el Día 8 nos enfrentamos a un problema espacial en 3D. Recibimos una lista de coordenadas (nodos o cruces) y debemos conectarlos mediante cables. Para optimizar recursos, siempre se priorizan las conexiones más cortas. En la **Parte A**, simulamos la conexión de un número limitado de cables y calculamos una métrica basada en los circuitos resultantes. En la **Parte B**, conectamos iterativamente hasta que todo el sistema forma una red ininterrumpida, identificando la última conexión crítica.

---

## 1. Patrones de Diseño

### Patrón Iterator (Uso Implícito)
En este día, trabajamos intensamente con colecciones de datos, como la lista de conexiones (`List<Connection>`). Utilizamos el **Patrón Iterator** de manera implícita mediante el bucle `for-each` de Java (`for (Connection conn : connections)`).
Este patrón proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente[cite: 52]. Al usarlo, separamos la lógica de iteración de las estructuras de datos, mejorando así la modularidad del código y permitiendo recorrer conjuntos de manera eficiente y uniforme[cite: 53, 54].

---

## 2. Inyecciones y Acoplamiento

### Inyección de Datos y Bajo Acoplamiento
En las clases orquestadoras (`PlaygroundOptimizer` y `PlaygroundCompleter`), evitamos que estas instancien directamente al `JunctionParser` para obtener los datos. En su lugar, la lista de puntos (`List<Point3D>`) se inyecta directamente a través de los parámetros del método `calculateLastConnectionXProduct(List<Point3D> junctions)`.
Esto apoya directamente el fundamento de **Bajo Acoplamiento**, promoviendo la idea de diseñar módulos o componentes que tienen pocas interdependencias[cite: 17].

### Principio de Composición sobre Herencia (COI)
Para resolver la conectividad del grafo, no hacemos que nuestro optimizador herede de una clase compleja de matemáticas. En su lugar, instanciamos y utilizamos `CircuitUnionFind` internamente. Esto respeta el **Principio de Composición sobre Herencia (COI)**, ya que en lugar de heredar código de una clase base, se recomienda tener un objeto con esa funcionalidad para componer el comportamiento deseado[cite: 37].

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
Aplicamos estrictamente que cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión[cite: 30].
* **Dónde está en el código:** 1. `JunctionParser`: Su única responsabilidad es transformar texto en objetos 3D.
    2. `CircuitUnionFind`: Su única responsabilidad es gestionar la lógica matemática de conjuntos disjuntos.
    3. `PlaygroundCompleter`: Su única responsabilidad es orquestar la simulación.
       Si el formato de entrada cambia, solo se modifica el parser. Si la métrica del puzzle cambia, solo se modifica el orquestador.

---

## 4. Normas, Leyes y Fundamentos

### Fundamentos: Alta Cohesión y Código Expresivo (Uso de Records)
Para representar los puntos y las conexiones, utilizamos la estructura `record` de Java (`Point3D` y `Connection`). Esto promueve la **Alta Cohesión**, ya que las variables internas (x, y, z) de estas partes están estrechamente relacionadas y enfocadas en una única tarea: representar una coordenada espacial[cite: 16].
Además, esto nos ayuda a mantener un **Código Expresivo**, haciendo que el código sea claro y comprensible, facilitando la lectura y el mantenimiento frente a otras alternativas como usar simples *arrays* de números[cite: 20].

### Principio de no repetir código (DRY)
Este principio dicta que cada pieza de conocimiento en un software debería tener una representación única inequívoca[cite: 40].
* **Dónde está en el código:** La lógica para calcular la distancia espacial al cuadrado requiere varias operaciones matemáticas. En lugar de repetir esta fórmula cada vez que evaluamos dos puntos en los bucles anidados del orquestador, la hemos encapsulado una única vez en el método `distanceSquaredTo` dentro de `Point3D`.

### Fundamento: Abstracción
La abstracción consiste en ocultar los detalles complejos detrás de una interfaz simple[cite: 21].
* **Dónde está en el código:** La clase `CircuitUnionFind` maneja internamente la manipulación de arreglos (`parent`, `size`) y una optimización algorítmica compleja mediante recursividad. Sin embargo, oculta toda esa complejidad exponiendo únicamente una interfaz muy simple y limpia al orquestador a través del método `dsu.union(id1, id2)`.