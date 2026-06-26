---
title: "Advent of Code - Día 4: El Almacén y las Carretillas"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output:
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 4: El Almacén y las Carretillas (Análisis Espacial)

## Descripción del Problema
El Día 4 simula un almacén logístico en una cuadrícula 2D (`Grid`). El reto consiste en evaluar qué carretillas (representadas por el carácter `@`) son "accesibles" basándonos en la disposición de los espacios adyacentes. El problema evoluciona desde un simple conteo estático (Parte A) hacia un motor de Autómata Celular que retira carretillas iterativamente hasta que no quede ninguna accesible (Parte B).

---

## 1. Patrones de Diseño Utilizados

### Patrón Strategy (Estrategia)
Se utiliza para extraer la lógica matemática del recorrido espacial, haciendo los algoritmos intercambiables.
* **Implementación en el código:**
  La interfaz abstracta define el contrato, ignorando cómo funciona la cuadrícula:
  ```java
  public interface AccessibilityRule {
      boolean isAccessible(Grid grid, int row, int col);
  }
  ```
  La clase concreta (`FewerThanFourRule`) encapsula la física real del almacén. El orquestador (`ForkliftOptimizer`) invoca la estrategia ciegamente:
  ```java
  if (rule.isAccessible(grid, r, c)) {
      count++;
  }
  ```

### Inyección de Dependencias (DI por Constructor)
* **Implementación en el código:**
  En lugar de crear la regla internamente con un `new`, el orquestador la exige en su constructor:
  ```java
  public class ForkliftOptimizer {
      private final AccessibilityRule rule;

      public ForkliftOptimizer(AccessibilityRule rule) {
          this.rule = rule; // Dependencia inyectada
      }
  }
  ```
  **Ventaja:** Elimina el acoplamiento y permite inyectar simulaciones (*mocks*) desde el Test para comprobar los bucles sin ejecutar la matemática real.

---

## 2. Principios SOLID Aplicados

### Principio de Responsabilidad Única (SRP)
Dividimos el problema en tres piezas independientes, cada una con una única razón para cambiar:
1. `Grid`: Solo cambia si el modelo de datos subyacente pasa de `char[][]` a otro formato.
2. `FewerThanFourRule`: Solo cambia si los elfos cambian la regla de las "4 adyacencias".
3. `ForkliftOptimizer`: Solo cambia si la forma de recorrer el tablero cambia (ej. iteración paralela).

### Principio Abierto/Cerrado (OCP)
El software debe estar abierto a la extensión, pero cerrado a la modificación.
* **La prueba en tu código:** Al llegar a la Parte B, **no tocaste ni una sola coma** de `FewerThanFourRule`. La extendiste conectándola a un nuevo orquestador (`IterativeForkliftOptimizer`), demostrando que la regla base estaba perfectamente blindada.

### Principio de Inversión de Dependencias (DIP)
* **La prueba en tu código:** `ForkliftOptimizer` (módulo de alto nivel) depende de la abstracción `AccessibilityRule`, no de la clase de bajo nivel `FewerThanFourRule`.

---

## 3. Normas de Arquitectura y Clean Code

### Encapsulamiento y Protección de Datos (Information Hiding)
Exponer un arreglo bidimensional (`char[][]`) a todo el programa es peligroso. En su lugar, lo envolvimos en un Objeto de Dominio (`Grid`).
* **Protección contra excepciones (Out of Bounds):**
  El método encapsula la lógica de los bordes del mapa. Si el optimizador pide unas coordenadas fuera de los límites, la cuadrícula no lanza un `ArrayIndexOutOfBoundsException`, sino que responde con gracia:
  ```java
  public char getCharAt(int row, int col) {
      if (row < 0 || row >= rows || col < 0 || col >= cols) {
          return '.'; 
      }
      return map[row][col];
  }
  ```

### DRY (Don't Repeat Yourself)
Para verificar las celdas adyacentes, evitamos escribir 8 bloques `if/else` repetitivos.
* **La solución en el código:** Utilizamos una matriz de vectores direccionales constante.
  ```java
  private static final int[][] DIRECTIONS = {
          {-1, -1}, {-1, 0}, {-1, 1},
          { 0, -1},          { 0, 1},
          { 1, -1}, { 1, 0}, { 1, 1}
  };
  // ...
  for (int[] dir : DIRECTIONS) {
      int newRow = row + dir[0];
      int newCol = col + dir[1];
  }
  ```

---

## 4. Ingeniería de Simulación 

La Parte B presentaba el clásico problema de la **Modificación Concurrente**. Si encontramos un `@` y lo borramos al instante, alteraríamos el tablero para las validaciones del mismo "turno", provocando una reacción en cadena errónea.

Para evitarlo, aplicamos el patrón de **Mutación Diferida (Two-Phase Processing)**:
1. **Fase de Evaluación (Solo Lectura):** Guardamos las coordenadas válidas sin tocar el mapa.
   ```java
   List<int[]> rollsToRemove = new ArrayList<>();
   if (rule.isAccessible(grid, r, c)) {
       rollsToRemove.add(new int[]{r, c});
   }
   ```
2. **Fase de Mutación (Escritura en Bloque):** Aplicamos los cambios todos a la vez.
   ```java
   for (int[] coord : rollsToRemove) {
       grid.setCharAt(coord[0], coord[1], '.');
   }
   ```
Este enfoque asegura la integridad del estado generacional, siendo el estándar en física de videojuegos y simulaciones celulares.