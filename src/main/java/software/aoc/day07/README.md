---
title: "Advent of Code - Día 7: El Colector de Taquiones"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output:
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 7: El Colector de Taquiones (Simulación Cuántica)

## Descripción del Problema
En el Día 7 debemos modelar el comportamiento de un haz de taquiones que viaja a través de un colector bidimensional (`Manifold`). El rayo comienza en la coordenada `S` y desciende fila por fila. Si encuentra el carácter `^`, el rayo se divide en dos.
En la **Parte A**, simulamos la propagación espacial para contar cuántas divisiones ocurren. En la **Parte B**, debemos calcular la cantidad de líneas temporales (rutas únicas) que el rayo ha tomado para llegar a cada punto, sumando el total histórico.

---

## 1. Patrones de Diseño

### Patrón Iterator (Uso Implícito)
En este día, interactuamos constantemente con colecciones complejas (`Set<Integer>` y `Map<Integer, Long>`). Al utilizar los bucles mejorados (`for-each`) de Java para recorrer los haces activos, estamos haciendo uso implícito del patrón Iterator.
Este patrón proporciona una manera de acceder secuencialmente a los elementos sin exponer su representación subyacente[cite: 52]. Al aplicarlo, se separa la lógica de iteración de las estructuras de datos, mejorando así la modularidad del código y permitiendo recorrer los conjuntos de manera eficiente y uniforme[cite: 53, 54].
* **Dónde está en el código:**
  ```java
  // Iteración limpia sin exponer cómo se almacena la memoria internamente
  for (Map.Entry<Integer, Long> entry : activeTimelines.entrySet()) {
      int col = entry.getKey();
      long pathCount = entry.getValue();
      // ...
  }
  ```

---

## 2. Inyecciones

### Inyección por Constructor y Bajo Acoplamiento
En lugar de que los motores de simulación creen su propia matriz internamente, reciben el objeto `Manifold` listo para usar a través de su constructor.
Esta inyección aplica la idea de diseñar módulos o componentes que tienen pocas interdependencias (Bajo Acoplamiento)[cite: 17]. Además, aplica el Principio de Composición sobre Herencia (COI), ya que en lugar de heredar código de una clase base, el simulador tiene un objeto con esa funcionalidad como propiedad[cite: 37].
* **Dónde está en el código:**
  ```java
  public class QuantumTachyonSimulator {
      private final Manifold manifold; // Propiedad inyectada
      
      public QuantumTachyonSimulator(Manifold manifold) {
          this.manifold = manifold;
      }
  }
  ```

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión[cite: 30].
* **Dónde está en el código:** Separamos estrictamente la topología del mapa de la simulación matemática. La clase `Manifold` solo es responsable de gestionar las coordenadas espaciales y abstraer la matriz. Por otro lado, los simuladores (`TachyonSimulator`) no saben leer archivos de texto ni procesar Strings; solo se encargan de la física generacional de los taquiones.

---

## 4. Normas y Leyes

### Ley de Demeter
Esta ley sugiere que una unidad de software debe tener un conocimiento limitado sobre otras unidades (se resume en "no aceptes caramelos de extraños")[cite: 38, 39].
* **Dónde está en el código:** En lugar de que el simulador le pida a `Manifold` que le entregue todo el arreglo `char[][]` para manipularlo directamente (lo cual violaría la ley al acceder a los datos internos de otro objeto), el simulador solo hace peticiones limitadas a través de una interfaz simple, apoyando el fundamento de la Abstracción[cite: 21]:
  ```java
  char cell = manifold.getCharAt(r, col);
  ```

### Principio de no repetir código (DRY)
Este principio dicta que cada pieza de conocimiento en un software debería tener una representación única inequívoca[cite: 40].
* **Dónde está en el código:** A lo largo de la simulación, es posible que el rayo intente buscar coordenadas fuera de los bordes del mapa. En lugar de escribir la comprobación de los límites (`if (r < 0 || r >= rows...)`) repetidas veces en los bucles del orquestador, esta lógica se escribió una única vez dentro del método `getCharAt` de `Manifold`, centralizando el control y evitando la duplicación de código.