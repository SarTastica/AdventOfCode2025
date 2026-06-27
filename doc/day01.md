---
title: "Advent of Code - Día 1: La Caja Fuerte (Inmutabilidad y Diseño Puro)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 1: La Caja Fuerte (Análisis Arquitectónico)

## Descripción del Problema
En el Día 1, nos enfrentamos a la simulación de una caja fuerte de combinación (`Dial`). Se recibe una lista de instrucciones de giro (hacia la izquierda o la derecha) y debemos calcular cuántas veces el dial pasa por la posición cero. La complejidad arquitectónica radica en que las reglas matemáticas y de conteo para la **Parte A** y la **Parte B** son diferentes. El reto es modelar el estado de la caja fuerte de manera predecible, segura y fácil de mantener.

---

## 1. Patrones de Diseño Aplicados

Según la teoría, los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software.

### Patrón Factory Method
* **¿Qué es?** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método estático que encapsula la creación del objeto.
* **¿Dónde está en el código?** En el `record Rotation`, específicamente en el método estático `Rotation.from(String order)`.
* **¿Por qué se ha implementado y cuál es su utilidad?** Transforma una cadena de texto cruda (ej. `"L5"`) en un objeto de dominio estructurado (`Rotation`). Su utilidad es inmensa: aísla la lógica de parseo (extraer la dirección y la cantidad) del resto del sistema. Si mañana el formato de las instrucciones cambia, solo se modifica este Factory Method, dejando la clase `Dial` completamente intacta.

---

## 2. Principios de Diseño

Los principios de diseño actúan como reglas que orientan cómo se deben aplicar los fundamentos.

### Principio de Responsabilidad Única (SRP)
* **¿Qué es?** Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión.
* **¿Dónde está en el código?** En la estricta separación estructural de los paquetes `software.aoc.day01.a` y `software.aoc.day01.b`, teniendo cada uno su propia versión de la clase `Dial`.
* **¿Por qué se ha implementado y cuál es su utilidad?** La lógica para evaluar el paso por el cero varía drásticamente entre ambas partes. Si usáramos una única clase general, tendríamos que llenarla de sentencias condicionales, dándole a la clase múltiples razones para cambiar. Al separar los dominios, respetamos el SRP, asegurando que un cambio en las reglas de la Parte B nunca rompa el funcionamiento de la Parte A.

### Principio YAGNI (You Aren't Gonna Need It)
* **¿Qué es?** Aconseja a los desarrolladores no añadir funcionalidad hasta que sea realmente necesaria.
* **¿Dónde está en el código?** En la decisión de **no** utilizar un complejo Patrón Command ni interfaces compartidas (`CajaFuerte`) para este problema.
* **¿Por qué se ha implementado y cuál es su utilidad?** En versiones tempranas se suele pecar de sobreingeniería. Al aplicar YAGNI, eliminamos interfaces innecesarias y clases abstractas, reduciendo la solución a operaciones matemáticas directas. La utilidad es un código mucho más ligero, directo y fácil de entender.

---

## 3. Fundamentos de Diseño

Los fundamentos son las cualidades inherentes deseables en el software.

### Código Expresivo e Inmutabilidad (Records)
* **¿Qué es?** El código expresivo debe ser claro y comprensible. Se ha apoyado en la inmutabilidad: objetos cuyo estado no puede ser modificado una vez creados.
* **¿Dónde está en el código?** Al definir `Dial` y `Rotation` como `records` nativos de Java. El método `execute(Rotation)` no altera el estado interno (no hace `this.currentPosition = ...`), sino que devuelve un `new Dial(...)`.
* **¿Por qué se ha implementado y cuál es su utilidad?** Elimina los "efectos secundarios" (side effects). En lugar de mutar variables, el sistema procesa datos como una tubería pura (`current = current.execute(...)`). Esto hace que el código sea extremadamente expresivo y matemáticamente predecible, facilitando enormemente el testing y evitando bugs relacionados con estados inválidos.

### Bajo Acoplamiento
* **¿Qué es?** Diseñar componentes que tienen pocas interdependencias.
* **¿Dónde está en el código?** La Parte A no comparte absolutamente ningún archivo, interfaz ni clase base con la Parte B.
* **¿Por qué se ha implementado y cuál es su utilidad?** A veces, compartir código (DRY) introduce un acoplamiento artificial. Al aislar completamente las dos partes, logramos un acoplamiento cero entre dominios. La utilidad es la máxima independencia: un dominio puede evolucionar, ser refactorizado o incluso eliminado sin afectar al otro.