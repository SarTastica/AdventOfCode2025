---
title: "Advent of Code - Día 4: El Almacén de Rollos"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 4: El Almacén de Rollos (Inmutabilidad y Principio DRY)

## Descripción del Problema
En el Día 4 gestionamos un almacén representado por una cuadrícula. En la **Parte A**, debemos contar cuántos rollos (`@`) son accesibles (tienen menos de 4 vecinos inmediatos). En la **Parte B**, debemos realizar una simulación iterativa: encontrar los rollos accesibles, eliminarlos del mapa, y repetir el proceso hasta que no se puedan eliminar más. El gran desafío arquitectónico de este día es compartir la lógica espacial entre ambas partes sin duplicar código y, sobre todo, evitar la mutación accidental del mapa original durante la simulación.

---

## 1. Patrones de Diseño

### Patrón Strategy
La lógica que define si una coordenada es accesible o no se ha abstraído en una interfaz.
* **Dónde está en el código:** Se ha creado la interfaz genérica `AccessibilityRule`. Su implementación concreta, `FewerThanFourRule`, calcula los vecinos en las 8 direcciones. Tanto el optimizador de la Parte A como el de la Parte B reciben esta estrategia. Si en el futuro las reglas de accesibilidad cambian, los optimizadores no tendrán que ser modificados.

---

## 2. Inyecciones y Acoplamiento

### Inyección de Dependencias
Los orquestadores no instancian sus propias reglas ni sus propios mapas.
* **Dónde está en el código:** `ForkliftOptimizer` y `IterativeForkliftOptimizer` reciben la regla matemática a través de su constructor y el mapa de datos (`Grid`) a través del método de ejecución. Esto garantiza un **Bajo Acoplamiento**, aislando la lógica de simulación de las estructuras de datos.

---

## 3. Principios de Diseño

### Principio DRY (Don't Repeat Yourself)
A diferencia de arquitecturas que duplican las clases de representación de mapas, aquí se ha centralizado el conocimiento.
* **Dónde está en el código:** La clase `Grid`, el record `Position` y la estrategia `FewerThanFourRule` se encuentran en el paquete general `software.aoc.day04`. Ambas partes del problema consumen estas clases. De este modo, si la forma de parsear el almacén cambia, solo hay que modificar un único archivo, cumpliendo estrictamente con el principio DRY.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Inmutabilidad (Prevención de Side-Effects)
Modificar un objeto de dominio global durante una simulación (como hacer `grid.setCharAt(...)`) puede corromper los datos para evaluaciones futuras y causar *efectos secundarios* impredecibles.
* **Dónde está en el código:** Para la Parte B, la clase `Grid` fue diseñada para ser inmutable. En lugar de borrar los caracteres de la matriz original, el método `removeRolls(List<Position>)` clona la matriz y devuelve una **nueva instancia** de `Grid`. El orquestador (`IterativeForkliftOptimizer`) simplemente avanza su estado apuntando a esta nueva instancia. Esto hace que el código sea seguro para lambdas y concurrencia.

### Fundamentos: Alta Cohesión y Código Expresivo
* **Dónde está en el código:** 1. **Cohesión:** Se eliminó la gestión de primitivos `int r, int c` introduciendo el record `Position`, agrupando las coordenadas lógicamente.
  2. **Expresividad:** Se ocultaron los bucles for anidados de la cuadrícula detrás de un método `getRollPositions()` que devuelve un `Stream<Position>`. Esto permite que los orquestadores se lean de forma puramente declarativa (ej. `grid.getRollPositions().filter(...).count()`).