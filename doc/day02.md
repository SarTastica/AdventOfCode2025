---
title: "Advent of Code - Día 2: La Tienda de Regalos"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 2: La Tienda de Regalos (Patrón Strategy y OCP)

## Descripción del Problema
En el Día 2 debemos procesar una base de datos de una tienda de regalos que viene en formato de rangos numéricos (ej. `10-20, 50-60`). El objetivo es sumar todos los IDs que se consideren "inválidos". En la **Parte A**, un ID es inválido si su primera mitad es exactamente igual a su segunda mitad. En la **Parte B**, la regla cambia: el ID es inválido si está formado por un patrón que se repite múltiples veces. Al compartir el mismo flujo de datos pero cambiar la regla matemática, este día es el escenario perfecto para aplicar el Patrón Strategy.

---

## 1. Patrones de Diseño

### Patrón Strategy
En lugar de crear dos clases orquestadoras distintas (una para la Parte A y otra para la Parte B), hemos implementado el **Patrón Strategy** a través de la interfaz genérica `ValidationRule`.
* **Dónde está en el código:** La clase general `GiftShop` no sabe cómo validar un ID. Simplemente confía en la interfaz `ValidationRule`. Las implementaciones concretas (`RepeatedHalfRule` y `RepeatedPatternRule`) encapsulan el algoritmo específico, permitiendo intercambiar la lógica de validación dinámicamente sin alterar el motor principal de la base de datos.

---

## 2. Inyecciones y Acoplamiento

### Inyección por Constructor e Inversión de Dependencias (DIP)
El orquestador principal delega el comportamiento de filtrado.
* **Dónde está en el código:** `GiftShop` recibe la interfaz `ValidationRule` a través de su constructor (`this.rule = rule;`). Esto garantiza que el módulo de alto nivel (el lector de rangos) dependa de una abstracción pura (la interfaz) en lugar de depender de detalles de bajo nivel (las expresiones regulares). Esto mantiene un nivel de **Bajo Acoplamiento** óptimo.

---

## 3. Principios de Diseño

### Principio Abierto Cerrado (OCP)
Las entidades de software deben estar abiertas para su extensión, pero cerradas para su modificación.
* **Dónde está en el código:** Si los elfos deciden añadir una "Parte C" con una nueva regla de validación de IDs, el código de `GiftShop` y `Range` no se tocará en absoluto (cerrado a modificación). Simplemente crearemos una nueva clase que implemente `ValidationRule` y se la inyectaremos (abierto a extensión).

### Principio DRY (Don't Repeat Yourself)
Cada pieza de conocimiento debe tener una representación única.
* **Dónde está en el código:** A diferencia de diseños menos maduros que duplican toda la clase de la base de datos para separar la Parte A de la Parte B, nuestra arquitectura centraliza la lectura, desempaquetado de rangos y suma matemática en una única clase general (`GiftShop`). Esto elimina la redundancia y previene errores de mantenimiento.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Alta Cohesión
Las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Dónde está en el código:** Se extrajo la manipulación de las cadenas de texto (`split("-")`) y la lógica de inicio/fin a un record inmutable dedicado llamado `Range`. De este modo, los datos de los límites y el método para generar un flujo de números (`stream()`) viven juntos de forma altamente cohesiva, liberando a `GiftShop` de la manipulación de bajo nivel.

### Fundamento: Código Expresivo (Streams y Regex)
El código debe ser claro y comprensible, casi como lenguaje natural.
* **Dónde está en el código:** 1. Se sustituyeron los bucles `for` anidados por la API de Streams de Java (`flatMapToLong`, `filter`, `sum`), haciendo que la lectura de la secuencia de operaciones sea declarativa.
  2. Las validaciones matemáticas complejas se sustituyeron por **Expresiones Regulares (Regex)** (`^([0-9]+)\\1+$`). Estas expresiones son soluciones elegantes, estándar y directas para buscar patrones repetidos en cadenas, reduciendo drásticamente la complejidad ciclomática del algoritmo.