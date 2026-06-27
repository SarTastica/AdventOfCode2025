---
title: "Advent of Code - Día 9: Optimización en el Teatro (Arquitectura de Estrategias)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 9: Optimización de Áreas (Ray Casting y Diseño Modular)

## Descripción del Problema
El desafío consiste en encontrar el rectángulo de mayor área posible dentro de un conjunto de baldosas. En la **Parte A**, el área es libre; en la **Parte B**, el rectángulo debe estar estrictamente contenido dentro de un polígono ortogonal. La complejidad radica en la validación geométrica (evitar colisiones con los bordes del polígono). Arquitectónicamente, el reto es orquestar algoritmos geométricos complejos sin acoplar la lógica de gestión de la sala de cine con la matemática de los optimizadores.

---

## 1. Patrones de Diseño

El diseño de software utiliza patrones como soluciones típicas a problemas comunes.

### Patrón Factory Method
* **Teoría:** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método que encapsula la creación.
* **Aplicación en el código:** Se utiliza en la clase `MovieTheaterParser`. El parseo de los datos crudos (cadenas de texto con comas) se centraliza en este método, que actúa como fábrica para instanciar los objetos `Tile`.
* **Justificación:** Centralizar la creación permite ocultar cómo se estructuran los datos de entrada, cumpliendo con la necesidad de encapsulación.

---

## 2. Inyecciones y Bajo Acoplamiento

### Inversión de Dependencias (DIP)
* **Teoría:** Módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación en el código:** El orquestador `MovieTheaterManager` no depende de `MaxAreaOptimizer` ni de `PolygonConstrainedOptimizer`. Depende exclusivamente de la interfaz `AreaOptimizationStrategy`.
* **Justificación:** Esta abstracción  permite un bajo acoplamiento. El orquestador puede trabajar con cualquier optimizador actual o futuro sin conocer sus detalles internos.

---

## 3. Principios de Diseño

### Principio Abierto Cerrado (OCP)
* **Teoría:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación en el código:** El orquestador está "cerrado" a modificaciones: no tenemos que cambiar `MovieTheaterManager` cuando añadimos nuevas formas de optimizar. Está "abierto" a la extensión: podemos crear nuevas clases que implementen `AreaOptimizationStrategy` e inyectarlas dinámicamente.

### Principio de Responsabilidad Única (SRP)
* **Teoría:** Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión.
* **Aplicación en el código:**
  * `MovieTheaterParser`: Única razón de cambio: que el formato del archivo de entrada cambie.
  * `PolygonConstrainedOptimizer`: Única razón de cambio: que las reglas geométricas del polígono cambien.
  * `Rectangle` (Record): Única razón de cambio: que las propiedades matemáticas de un rectángulo cambien.
* **Justificación:** Esto evita la creación de una "Clase Dios" que maneje parseo, geometría y control de flujo simultáneamente.

---

## 4. Normas, Leyes y Fundamentos

### Alta Cohesión
* **Teoría:** Refiere a la idea de que las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** Se ha creado el record `Rectangle`. En lugar de tener variables dispersas de coordenadas, este record agrupa matemáticamente el ancho, alto y centro del rectángulo. Es un componente altamente cohesivo.

### Abstracción y Ray Casting
* **Teoría:** Consiste en ocultar los detalles complejos detrás de una interfaz simple.
* **Aplicación:** El método `isFullyInside` abstrae la complejidad de la validación geométrica.
* **Justificación:** Internamente, el código utiliza el algoritmo de *Ray Casting* . Este algoritmo es una implementación matemática eficiente que abstrae la complejidad de la contención geométrica tras una respuesta booleana simple, facilitando la legibilidad.

### Código Expresivo
* **Teoría:** El código debe ser claro y comprensible, facilitando la lectura y el mantenimiento.
* **Aplicación:** El uso de *Guard Clauses* (retornos tempranos `if (...) return false;`) elimina la necesidad de anidación excesiva, haciendo que el flujo de control sea evidente a simple vista.