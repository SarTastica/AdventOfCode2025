---
title: "Advent of Code - Día 3: Sistema de Energía (Abstracción y Cohesión)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 3: Sistema de Energía de las Escaleras

## Descripción del Problema
En el Día 3, el objetivo es procesar bancos de baterías (`BatteryBank`) extrayendo y calculando niveles de voltaje (joltages). La **Parte A** nos exige encontrar el voltaje máximo combinando dos dígitos, mientras que la **Parte B** requiere un algoritmo más complejo para extraer el número de 12 dígitos más alto posible manteniendo el orden original. El reto arquitectónico radica en aplicar estas dos reglas de cálculo tan dispares sobre un mismo flujo de datos sin duplicar el código del orquestador.

---

## 1. Patrones de Diseño Aplicados

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software que sirven como plantillas.

### Patrón Iterator
* **Teoría:** Proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente. Separa la lógica de iteración de las estructuras de datos, mejorando la modularidad del código.
* **Aplicación en el código:** Se utiliza en la clase `EscalatorPowerSystem` a través de la API de Streams de Java (`Stream<String> batteryBanks`).
* **Justificación:** El orquestador recorre y transforma las líneas de texto mediante métodos como `.filter()`, `.map()` y `.sum()`. Esto es una implementación pura del patrón Iterator, que permite manipular el conjunto de datos de manera eficiente y uniforme, sin necesidad de gestionar bucles `for` tradicionales ni contadores de índices.

---

## 2. Principios de Diseño

Los principios de diseño actúan como reglas o restricciones que ayudan a alcanzar los fundamentos en la práctica.

### Principio de Inversión de Dependencias (DIP)
* **Teoría:** Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación en el código:** La clase de alto nivel `EscalatorPowerSystem` no depende de `MaxTwoDigitJoltage` ni de `MaxTwelveDigitJoltage`. Depende únicamente de la abstracción `JoltageCalculator`.
* **Justificación:** Al inyectar la dependencia a través del constructor, el orquestador delega el cálculo matemático. Si la lógica de las baterías cambia, el módulo principal no sufre ningún impacto.

### Principio Abierto Cerrado (OCP)
* **Teoría:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación en el código:** El orquestador `EscalatorPowerSystem` está completamente cerrado a modificaciones. Si surge un nuevo requerimiento para calcular baterías de 5 dígitos, no tocaremos esta clase; simplemente crearemos una nueva implementación de la interfaz (abierto a extensión).

### Principio de Responsabilidad Única (SRP) y DRY
* **Teoría:** SRP dicta que cada clase debe tener una sola razón para cambiar, reflejando alta cohesión. DRY establece que cada pieza de conocimiento debe tener una representación única.
* **Aplicación en el código:** La lectura, el filtrado y el sumatorio final se centralizan en una única clase, evitando repetir el código orquestador en dos carpetas distintas (DRY). Al mismo tiempo, cada algoritmo matemático reside en su propia clase, teniendo una única responsabilidad (SRP).

---

## 3. Fundamentos de Diseño e Inyecciones

Los fundamentos de diseño son las cualidades inherentes deseables en cualquier sistema de software

### Inyección de Dependencias y Bajo Acoplamiento
* **Teoría:** El bajo acoplamiento es la idea de diseñar componentes que tienen pocas interdependencias.
* **Aplicación en el código:** La dependencia `JoltageCalculator` se inyecta en el constructor de `EscalatorPowerSystem`. El orquestador ignora si está ejecutando la Parte A o la Parte B, garantizando un acoplamiento mínimo.

### Abstracción
* **Teoría:** Consiste en ocultar los detalles complejos detrás de una interfaz simple.
* **Aplicación en el código:** La interfaz `JoltageCalculator` expone un único método: `long calculate(BatteryBank bank)`. El orquestador no necesita saber que la Parte B utiliza un algoritmo avanzado con un `StringBuilder` actuando como pila (stack) para descartar dígitos menores. Toda esa complejidad queda oculta.

### Alta Cohesión
* **Teoría:** Refiere a que las partes de un componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** Se ha evitado el antipatrón de *Obsesión por los primitivos*. En lugar de pasar un tipo genérico `String` a las calculadoras, la información se envuelve en el record `BatteryBank`. Esto proporciona alta cohesión al encapsular los datos de la batería y su comportamiento (como el método `length()`) en una entidad propia del dominio.