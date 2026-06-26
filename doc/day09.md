---
title: "Advent of Code - Día 9: La Sala de Cine (Optimización y Clean Code)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 9: Optimización de Áreas (Ray Casting y Clean Code)

## Descripción del Problema
El último desafío consiste en encontrar el rectángulo de mayor área posible que se puede formar a partir de una lista de puntos (baldosas rojas). En la **Parte A**, el área es libre. En la **Parte B**, el rectángulo debe estar estrictamente contenido dentro de un polígono ortogonal definido por esos mismos puntos. La complejidad radica en la validación geométrica: asegurar que el rectángulo no sobresale ni es atravesado por las paredes del polígono.

---

## 1. Patrones de Diseño

### Patrón Strategy (Desacoplamiento de Algoritmos)
* **Dónde está en el código:** Se ha implementado la interfaz `AreaOptimizationStrategy`.
* **Justificación:** El orquestador principal (`MovieTheaterManager`) delega el cálculo a esta interfaz. Esto nos permite tener dos motores completamente distintos (`MaxAreaOptimizer` y `PolygonConstrainedOptimizer`) que se pueden intercambiar sin modificar el núcleo de la aplicación, cumpliendo estrictamente con el **Principio de Responsabilidad Única (SRP)**.

---

## 2. Principios de Clean Code y Refactorización

Para evitar el "Código Espagueti" (lógica anidada compleja y difícil de leer), el motor de la Parte B fue sometido a una refactorización profunda basada en los principios de Clean Code:

### Single Level of Abstraction Principle (SLAP) y Extract Method
* **Dónde está en el código:** El método principal de validación geométrica (`isFullyInside`) ya no contiene bucles matemáticos complejos. Se ha reescrito de forma declarativa:
  1. `isCenterInsidePolygon(...)`
  2. `hasPolygonVerticesInsideRectangle(...)`
  3. `hasPolygonEdgesCrossingRectangle(...)`
* **Justificación:** Al extraer la lógica matemática a métodos privados descriptivos, el método principal se lee como un índice o una regla de negocio. Esto reduce drásticamente la carga cognitiva.

### Guard Clauses (Cláusulas de Guarda)
* **Dónde está en el código:** En lugar de anidar múltiples sentencias `if`, se utilizan retornos tempranos (`if (!condicion) return false;`). Esto aplana la estructura del código y hace que el flujo de ejecución sea evidente a simple vista.

---

## 3. Algoritmos y Geometría Computacional

### Ray Casting Algorithm
* **Dónde está en el código:** En el método `isCenterInsidePolygon`.
* **Justificación:** Para determinar si el rectángulo está dentro del polígono, trazamos un "rayo" imaginario desde el centro geométrico del rectángulo. Si el rayo cruza las paredes del polígono un número impar de veces, el centro está dentro; si es par, está fuera. Este es un algoritmo estándar de la industria, extremadamente rápido $O(N)$ y eficiente en memoria.

### Validaciones Geométricas de Borde
Incluso si el centro está dentro, las esquinas del rectángulo podrían sobresalir de un polígono en forma de "C" o "U". Para garantizar la contención estricta:
1. Se verifica que **ningún vértice** del polígono caiga dentro de los límites del rectángulo.
2. Se verifica que **ninguna arista** (horizontal o vertical) atraviese el rectángulo de un extremo a otro.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Alta Cohesión e Inmutabilidad
* **Dónde está en el código:** Se ha creado el `record Rectangle`.
* **Justificación:** En lugar de pasar variables sueltas (`minX, maxX, minY, maxY`) por todo el código, se agrupan en una entidad de dominio inmutable. Además, esta entidad tiene **alta cohesión**, ya que es capaz de calcular sus propias propiedades derivadas, como su `area()`, `centerX()` y `centerY()`. Esto evita que las fórmulas matemáticas se dispersen por las clases de servicio.