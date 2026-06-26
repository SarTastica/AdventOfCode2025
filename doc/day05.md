---
title: "Advent of Code - Día 5: Inventario de la Cafetería"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 5: Inventario de la Cafetería (Principio DRY y Eficiencia Algorítmica)

## Descripción del Problema
En el Día 5 debemos gestionar el inventario de ingredientes frescos de la cafetería, el cual viene definido por una serie de rangos numéricos. En la **Parte A**, el sistema debe verificar cuántos IDs de una lista específica caen dentro de los rangos válidos. En la **Parte B**, el problema cambia a calcular la capacidad total (cardinalidad) de todos los rangos válidos sumados. El principal desafío de este día es el solapamiento de rangos: si no se fusionan correctamente, los cálculos de frescura y capacidad serán erróneos.

---

## 1. Patrones de Diseño

### Patrón Strategy (Parte A)
* **Dónde está en el código:** La clase `CafeteriaManager` delega la lógica de validación a la interfaz `FreshnessRule`. La implementación `MergedIntervalRule` se encarga de aplicar la matemática de intervalos. Esto permite que el orquestador principal se mantenga limpio y enfocado únicamente en contar resultados, independientemente de cómo se defina la "frescura".

---

## 2. Inyecciones y Acoplamiento

### Inversión de Dependencias (DIP) y Bajo Acoplamiento
* **Dónde está en el código:** El orquestador `CafeteriaManager` no instancia la regla de validación ni los datos. Recibe la regla a través de Inyección por Constructor (`this.rule = rule;`) y la lista de IDs a evaluar por parámetro en su método de ejecución. Esto garantiza que la lógica de iteración esté completamente desacoplada de la lógica de evaluación.

---

## 3. Principios de Diseño

### Principio DRY (Don't Repeat Yourself)
Cada pieza de conocimiento en un sistema debe tener una representación única.
* **Dónde está en el código:** A diferencia de diseños menos experimentados que duplican la compleja lógica de fusión de rangos en la Parte A y la Parte B, nuestra arquitectura centraliza este comportamiento matemático en la clase de utilidad compartida `RangeMerger`. Si se descubre un caso límite en la matemática de solapamientos, solo habrá que corregir un único archivo, eliminando el riesgo de inconsistencias.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Alta Cohesión (Records Inteligentes)
Las partes de un módulo deben estar estrechamente relacionadas.
* **Dónde está en el código:** Se ha transformado la entidad `Range` en un `record` de Java, pero no como un simple contenedor pasivo de datos (Data Class). Se le ha dotado de cohesión añadiéndole el comportamiento `size()`, permitiendo que el rango sepa calcular su propia longitud (`end - start + 1`). Esto evita tener que esparcir fórmulas matemáticas de capacidad por las clases orquestadoras.
