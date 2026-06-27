---
title: "Advent of Code - Día 5: La Cafetería (Fusión de Intervalos)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 5: Gestión de Inventario en la Cafetería

## Descripción del Problema
En el Día 5 nos enfrentamos a la validación de inventario en una cafetería. Se nos proporciona una lista de rangos válidos (IDs de ingredientes frescos) y una lista de IDs a evaluar. El reto algorítmico principal radica en que los rangos pueden solaparse, lo que requiere una lógica de fusión (merge) matemática. La **Parte A** nos pide contar cuántos IDs disponibles caen dentro de los rangos frescos, mientras que la **Parte B** nos pide calcular la cantidad total de IDs frescos únicos sumando el tamaño de los rangos fusionados.

---

## 1. Patrones de Diseño Aplicados

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software que sirven como plantillas.

### Patrón Factory Method
* **Teoría:** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método estático que encapsula la creación del objeto.
* **Aplicación en el código:** Se implementa en la clase `InventoryParser` mediante el método estático `parse(List<String> lines)`.
* **Justificación y Utilidad:** Este método actúa como una fábrica que procesa la materia prima (líneas de texto) y devuelve un objeto de dominio estructurado (`ParsedData`). Encapsular esta lógica es extremadamente útil porque separa la compleja lectura del archivo de texto de la lógica de negocio.

### Patrón Iterator
* **Teoría:** Proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente. Separa la lógica de iteración de las estructuras de datos, mejorando así la modularidad.
* **Aplicación en el código:** La clase `CafeteriaManager` y el `TotalFreshnessCalculator` utilizan la API de Streams de Java (`availableIds.stream().filter(...)` y `merged.stream().mapToLong(...)`).
* **Justificación y Utilidad:** Este patrón es vital en colecciones de programación, permitiendo recorrer y manipular conjuntos de datos de manera eficiente y uniforme. Evita la exposición de contadores de bucles y hace que el código sea puramente declarativo.

---

## 2. Principios de Diseño

Los principios actúan como restricciones o reglas que orientan cómo aplicar los fundamentos de diseño.

### Principio de Inversión de Dependencias (DIP)
* **Teoría:** Módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación en el código:** El módulo de alto nivel `CafeteriaManager` no depende de la implementación algorítmica `MergedIntervalRule` (bajo nivel). En su lugar, depende exclusivamente de la interfaz `FreshnessRule`.
* **Justificación y Utilidad:** Esto asegura que el orquestador principal se mantenga limpio y enfocado únicamente en contar resultados, independientemente de cómo se defina matemáticamente la "frescura".

### Principio Abierto Cerrado (OCP)
* **Teoría:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación en el código:** Gracias al uso de interfaces, `CafeteriaManager` está cerrado a cambios. Si mañana el criterio de frescura cambia, extenderemos el sistema creando una nueva clase que implemente `FreshnessRule` sin alterar el código existente.

### Principio de no repetir código (DRY)
* **Teoría:** Cada pieza de conocimiento en un software debería tener una representación única inequívoca.
* **Aplicación en el código:** A diferencia de diseños menos maduros que duplican la lógica, la compleja fusión matemática de rangos superpuestos se ha extraído a una única clase de utilidad: `RangeMerger.merge()`.
* **Justificación y Utilidad:** Tanto la Parte A como la Parte B consumen esta misma función. Si se descubre un caso límite en la matemática de solapamientos, solo habrá que corregir un único archivo, asegurando un mantenimiento seguro.

---

## 3. Fundamentos de Diseño e Inyecciones

Los fundamentos de diseño son las cualidades inherentes deseables en el software.

### Inyección de Dependencias y Bajo Acoplamiento
* **Teoría:** El bajo acoplamiento busca diseñar componentes con pocas interdependencias.
* **Aplicación en el código:** El `CafeteriaManager` recibe la regla de evaluación mediante Inyección por Constructor (`this.rule = rule;`).
* **Justificación y Utilidad:** La clase ignora cómo se instancian las reglas o los datos, logrando que la lógica de iteración esté completamente desacoplada de la lógica de evaluación.

### Alta Cohesión
* **Teoría:** La alta cohesión requiere que las partes de un módulo estén estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** Se ha dotado a la entidad `Range` (un `record`) de alta cohesión añadiéndole el comportamiento `size()`.
* **Justificación y Utilidad:** Un rango no es solo un contenedor pasivo de números; sabe calcular su propia longitud espacial. Esto alinea los datos con su comportamiento matemático y evita esparcir fórmulas por el orquestador.

### Código Expresivo y Abstracción
* **Teoría:** El código debe ser claro y comprensible. La abstracción consiste en ocultar los detalles complejos detrás de una interfaz simple.
* **Aplicación en el código:** La clase `MergedIntervalRule` oculta el bucle complejo que verifica si un ID cae dentro de los rangos fusionados. El exterior solo ve un método expresivo y claro: `isFresh(id)`.