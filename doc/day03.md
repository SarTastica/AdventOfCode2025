---
title: "Advent of Code - Día 3: Energía de Emergencia"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 3: Energía de Emergencia (Strategy y Pila Monótona)

## Descripción del Problema
En el Día 3 debemos calcular el máximo voltaje ("Joltage") que se puede extraer de diferentes bancos de baterías representados por largas secuencias de dígitos. En la **Parte A**, el objetivo es encontrar la combinación de 2 dígitos que maximice el valor. En la **Parte B**, el reto escala exponencialmente: debemos extraer la mejor secuencia de exactamente 12 dígitos manteniendo el orden relativo original. El enfoque arquitectónico resuelve la orquestación del flujo de datos, mientras que el diseño algorítmico resuelve la enorme complejidad de la Parte B sin sacrificar el rendimiento.

---

## 1. Patrones de Diseño

### Patrón Strategy
El sistema principal no contiene la lógica de cálculo, sino que esta se ha encapsulado en clases independientes que comparten la misma interfaz.
* **Dónde está en el código:** La clase `EscalatorPowerSystem` orquesta la lectura y el sumatorio final, pero delega la lógica de cálculo a la interfaz genérica `JoltageCalculator`. Las implementaciones `MaxTwoDigitJoltage` (Parte A) y `MaxTwelveDigitJoltage` (Parte B) encapsulan sus respectivos algoritmos. Esto permite cambiar dinámicamente la estrategia de cálculo en tiempo de ejecución.

---

## 2. Inyecciones y Acoplamiento

### Inyección por Constructor e Inversión de Dependencias (DIP)
El orquestador depende exclusivamente de abstracciones, no de detalles de implementación concretos.
* **Dónde está en el código:** `EscalatorPowerSystem` recibe su estrategia a través del constructor (`this.calculator = calculator;`). Esto asegura un **Bajo Acoplamiento**, ya que el sistema principal ignora si está resolviendo la Parte A o la Parte B, limitándose a aplicar el contrato de la interfaz.

---

## 3. Principios de Diseño

### Principio DRY (Don't Repeat Yourself) y OCP (Abierto Cerrado)
Se evita la duplicación de código estructural compartiendo la clase orquestadora.
* **Dónde está en el código:** En lugar de crear dos orquestadores idénticos en carpetas separadas (una práctica que violaría DRY), el flujo de entrada de datos se centraliza. Además, gracias al Strategy, el sistema cumple con **OCP**: si se requiere una nueva longitud de extracción en el futuro, el orquestador permanecerá cerrado a la modificación y simplemente se añadirá una nueva implementación de la interfaz.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Alta Cohesión (Evitando la Obsesión por los Primitivos)
El uso exclusivo de tipos primitivos (como `String` o `int`) para representar conceptos del dominio empobrece la semántica del sistema.
* **Dónde está en el código:** En lugar de pasar un simple `String` a los calculadores, el texto se envuelve en el record inmutable `BatteryBank`. Esto aporta Alta Cohesión al encapsular los datos de la batería y su comportamiento (como `length()`) en una entidad propia del dominio.

### Diseño Algorítmico: Eficiencia de Tiempo Lineal $O(N)$
Para la Parte B, una solución basada en recursividad o bucles anidados habría colapsado el sistema debido a la complejidad combinatoria de buscar 12 dígitos.
* **Dónde está en el código:** Se ha implementado un algoritmo avanzado de **Pila Monótona** (*Monotonic Stack*) en `MaxTwelveDigitJoltage`. Utilizando un `StringBuilder` como pila de tamaño dinámico, el algoritmo recorre la secuencia original una sola vez, eliminando iterativamente los dígitos anteriores si el dígito actual es mayor y aún nos sobran elementos para descartar. Esto reduce la complejidad del algoritmo de tiempo exponencial a un tiempo lineal $O(N)$, demostrando una gran madurez técnica.