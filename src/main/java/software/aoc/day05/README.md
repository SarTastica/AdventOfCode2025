---
title: "Advent of Code - Día 5: La Cafetería y los Ingredientes Frescos"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output:
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 5: La Cafetería (Fusión de Intervalos)

## Descripción del Problema
El Día 5 nos presenta un problema de inventario en una cafetería. Recibimos un archivo de texto con dos bloques de datos: una lista de rangos numéricos válidos (ej. `10-20`) y una lista de identificadores de ingredientes a comprobar. El objetivo en la Parte A es contar cuántos ingredientes son "frescos" (caen dentro de algún rango válido), y en la Parte B, calcular la capacidad total matemática de esos rangos.

---

## Patrones de Diseño Utilizados

### Patrón Iterator (Uso implícito)
Según la teoría, este patrón proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente. En Java, este patrón está profundamente integrado en las colecciones de programación[cite: 53].
* **Dónde está en el código:**
  Lo utilizamos implícitamente de dos formas. Primero, al usar el bucle iterador mejorado (`for-each`) para recorrer los intervalos sin manejar índices manuales:
  ```java
  for (Range r : mergedRanges) {
      totalCapacity += (r.getEnd() - r.getStart() + 1);
  }
  ```
  Y segundo, a través de la API de Streams en el orquestador, que internamente utiliza un iterador para procesar la colección de forma eficiente y uniforme separando la lógica de iteración de las estructuras de datos[cite: 53, 54]:
  ```java
  return availableIds.stream().filter(rule::isFresh).count();
  ```
  
## 1. Fundamentos y Principios Estructurales

### Principio de Composición sobre Herencia (COI) y DIP
Para que el orquestador no estuviera acoplado a la fórmula matemática, apliqué el **Principio de Composición sobre Herencia (COI)**. En lugar de heredar de una clase base, el orquestador (`CafeteriaManager`) tiene un objeto con esa funcionalidad como propiedad inyectada.
Además, esto cumple con el **Principio de Inversión de Dependencias (DIP)**, ya que el módulo de alto nivel depende de una abstracción (`FreshnessRule`) y no de detalles de bajo nivel.
* **Dónde está en el código:**
  La interfaz actúa como la abstracción:
  ```java
  public interface FreshnessRule {
      boolean isFresh(long id);
  }
  ```
  Y se inyecta como composición en el orquestador:
  ```java
  public CafeteriaManager(FreshnessRule rule) { this.rule = rule; }
  ```

### Fundamentos: Alta Cohesión y Código Expresivo
Cuando el parser necesita devolver múltiples conjuntos de datos distintos (la lista de rangos y la lista de IDs), la forma "sucia" sería devolver un arreglo genérico `Object[]`.
En su lugar, he creado la clase `ParsedData` para agrupar estos datos y aplicar el fundamento de **Alta Cohesión**, ya que las partes de este componente están estrechamente relacionadas para la tarea de transferencia. Además, esto promueve un **Código Expresivo** que facilita la lectura y comprensión de lo que viaja entre las clases.
* **Dónde está en el código:**
  ```java
  public static class ParsedData {
      public final List<Range> ranges;
      public final List<Long> availableIds;
      public ParsedData(List<Range> r, List<Long> i) { ... }
  }
  ```

---

## 2. Principios SOLID Aplicados

### Principio de Responsabilidad Única (SRP)
Cada módulo o clase debe tener una sola razón para cambiar, reflejando una alta cohesión.
* **La prueba en tu código:** Separamos el "Parseo" de la "Lógica de Negocio". La clase `InventoryParser` es la única que sabe que el archivo de texto separa las secciones mediante una línea en blanco (`if (line.trim().isEmpty())`). Si los elfos deciden cambiar el formato del archivo `.txt`, el algoritmo matemático de los intervalos no sufrirá ningún cambio.

### Principio Abierto Cerrado (OCP)
Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **La prueba en tu código:** La clase `CafeteriaManager` delega el cálculo a la abstracción. Gracias a esto, está cerrada a modificaciones: podríamos crear una nueva regla `StrictFreshnessRule` que exija que los números sean primos, inyectarla, y el `CafeteriaManager` seguirá funcionando y filtrando ingredientes sin alterar una sola coma de su interior.

---
