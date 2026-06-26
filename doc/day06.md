---
title: "Advent of Code - Día 6: Los Cálculos de los Cefalópodos"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 6: Cálculos de Cefalópodos (Patrón Strategy vs Enums y Principio OCP)

## Descripción del Problema
En el Día 6, debemos ayudar a unos cefalópodos a resolver hojas de cálculo matemáticas. El núcleo del problema radica en cómo se leen los datos: en la **Parte A**, las ecuaciones se leen de forma estándar (horizontalmente), mientras que en la **Parte B**, la hoja de cálculo ha sido rotada y las ecuaciones deben extraerse leyendo las columnas verticalmente de derecha a izquierda. Sin embargo, las operaciones matemáticas subyacentes (suma y multiplicación) son idénticas en ambas partes. El reto arquitectónico es aislar el algoritmo de parseo visual de la lógica matemática.

---

## 1. Patrones de Diseño

### Patrón Strategy (El antídoto contra los Enums estáticos)
* **Dónde está en el código:** Se ha implementado un motor de cálculo basado en el **Patrón Strategy** (`OperationStrategy`, `AdditionStrategy`, `MultiplicationStrategy`) provisto por una fábrica abstracta (`StrategyProvider`).
* **Justificación Arquitectónica:** En diseños menos maduros, es común ver las operaciones matemáticas incrustadas dentro de un `Enum` mediante lambdas (ej. `SUM(LongStream::sum)`). Aunque parece compacto, este enfoque acopla los datos estáticos con la lógica de negocio y obliga a modificar el archivo del `Enum` cada vez que surge un nuevo operador, violando las reglas más básicas de diseño orientado a objetos. El Patrón Strategy resuelve este problema aislando cada operación en su propia clase.

---

## 2. Inyecciones y Acoplamiento

### Inversión de Dependencias (DIP)
* **Dónde está en el código:** La clase orquestadora `CephalopodCalculator` ignora por completo cómo se suma o se multiplica, y tampoco sabe qué operador le corresponde a cada símbolo. Toda esta responsabilidad es inyectada en su constructor a través del `StrategyProvider`. Esto genera un **Bajo Acoplamiento** extremo: el motor matemático puede procesar cualquier tipo de hoja de cálculo sin importar su origen.

---

## 3. Principios de Diseño

### Principio Abierto Cerrado (OCP)
El código debe estar abierto a la extensión pero cerrado a la modificación.
* **Dónde está en el código:** Gracias a la arquitectura de estrategias, si en un hipotético Día 7 se introduce la operación de resta (`-`), el código existente no se tocará. Simplemente crearemos una clase `SubtractionStrategy` y la registraremos en el provider, cumpliendo el OCP a la perfección.

### Principio DRY (Don't Repeat Yourself) y YAGNI (You Aren't Gonna Need It)
* **Dónde está en el código:** 1. **DRY:** Las estrategias matemáticas se ubicaron en el paquete general (`software.aoc.day06`), ya que ambas partes del problema suman y multiplican. Duplicar estas estrategias en las carpetas `a` y `b` habría sido redundante.
  2. **YAGNI (Simplicidad):** En la Parte B, se refactorizó un sistema complejo de tres clases (`MatrixTransposer`, `ProblemExtractor`, `VerticalWorksheetParser`) en una única clase altamente cohesiva (`VerticalWorksheetParser`). Evitar la sobreingeniería en el parseo de textos mantiene la base del código limpia y legible.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Alta Cohesión e Inmutabilidad (Records)
* **Dónde está en el código:** La entidad `MathProblem` se ha transformado en un `record` de Java. Agrupa de forma cohesiva e inmutable la lista de operandos y el símbolo del operador. Además, se le ha otorgado comportamiento mediante el método `solve(OperationStrategy)`, permitiendo que el propio problema aplique la estrategia sobre sus datos, en lugar de ser un mero contenedor pasivo.