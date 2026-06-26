---
title: "Advent of Code - Día 9: El Cine y la Optimización de Área"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 9: El Cine y la Optimización de Área (Geometría Computacional)

## Descripción del Problema
En el Día 9 debemos optimizar el área de proyección o asientos en un cine. Recibimos una lista de coordenadas (`Tile`). En la **Parte A**, el objetivo es encontrar el área rectangular delimitada más grande posible (Bounding Box) entre dos coordenadas cualesquiera de la cuadrícula. En la **Parte B**, se añade una restricción de geometría computacional extrema: el área máxima no solo debe calcularse, sino que el rectángulo resultante debe estar estrictamente contenido dentro del polígono irregular formado por el perímetro de las coordenadas iniciales.

---

## 1. Patrones de Diseño

### Patrón Iterator (Uso Implícito)
En este día procesamos y transformamos un volumen de texto hacia objetos de dominio. Aplicamos el **Patrón Iterator** de forma implícita a través del uso de la API de *Streams* de Java en nuestro `MovieTheaterParser`.
Este patrón proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente. Al usar `.stream().map(...)`, separamos la lógica de iteración de la estructura del archivo, logrando recorrer y convertir los datos de manera altamente eficiente y declarativa.
* **Dónde está en el código:**
  ```java
  public List<Tile> parse(List<String> lines) {
      return lines.stream()
              .map(line -> {
                  String[] parts = line.split(",");
                  return new Tile(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
              })
              .toList();
  }
  ```

---

## 2. Inyecciones y Acoplamiento

### Principio de Composición sobre Herencia (COI) y DIP
Para resolver el problema sin acoplar el orquestador a los distintos algoritmos geométricos, aplicamos el **Principio de Composición sobre Herencia (COI)**. En lugar de heredar de una clase matemática, la clase `MovieTheaterManager` tiene un objeto con esa funcionalidad como propiedad inyectada en su constructor.
A su vez, esto satisface el **Principio de Inversión de Dependencias (DIP)**: el módulo de alto nivel (`MovieTheaterManager`) no depende de los módulos de bajo nivel (`MaxAreaOptimizer` o `PolygonConstrainedOptimizer`), sino que ambos dependen de la abstracción `AreaOptimizationStrategy`.
* **Dónde está en el código:**
  ```java
  public class MovieTheaterManager {
      private final AreaOptimizationStrategy strategy; // Propiedad inyectada (COI y DIP)

      public MovieTheaterManager(AreaOptimizationStrategy strategy) {
          this.strategy = strategy;
      }
      // ...
  }
  ```

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
Cada módulo debe tener una sola razón para cambiar, asegurando una alta cohesión en todo el diseño.
* **Dónde está en el código:** 1. `MovieTheaterParser`: Su única razón para cambiar es si el formato del archivo `.txt` cambia de CSV a otro separador.
    2. `MovieTheaterManager`: Su única razón para cambiar es si el flujo general de orquestación cambia.
    3. Clases de Optimización (`PolygonConstrainedOptimizer`): Su única razón para cambiar es si cambian las leyes de la geometría computacional.

### Principio Abierto Cerrado (OCP)
Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **La prueba en tu código:** Al enfrentarnos al brutal requerimiento geométrico de la Parte B, **no modificaste en absoluto** la clase `MovieTheaterManager`. El orquestador original quedó cerrado a la modificación, y el sistema se extendió simplemente inyectándole la nueva implementación matemática (`PolygonConstrainedOptimizer`).

---

## 4. Normas, Leyes y Fundamentos

### Principio de no repetir código (DRY)
Este principio dicta que cada pieza de conocimiento en un software debería tener una representación única inequívoca.
* **Dónde está en el código:** Tanto en la Parte A como en la Parte B, se requiere calcular constantemente el área del rectángulo delimitador (Bounding Box) entre dos puntos. En lugar de escribir esta fórmula matemática (`width * height`) repetidas veces en los optimizadores, la abstrajimos y la colocamos en un único lugar como responsabilidad del objeto `Tile`:
  ```java
  public long calculateBoundingArea(Tile other) {
      long width = Math.abs(this.x - other.x) + 1;
      long height = Math.abs(this.y - other.y) + 1;
      return width * height;
  }
  ```

### Fundamentos: Alta Cohesión y Código Expresivo
Hemos representado los puntos del cine como un `record Tile(long x, long y)`. Esto apoya la **Alta Cohesión**, ya que `x` e `y` y el método `calculateBoundingArea` están estrechamente relacionados y enfocados en representar el espacio. A su vez, garantiza un **Código Expresivo** que facilita la lectura y el mantenimiento en comparación con gestionar arrays de números enteros primitivos flotando sueltos por el código.