---
title: "Advent of Code - Día 10: Inicialización de la Fábrica"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 10: Inicialización de la Fábrica (Contextos Delimitados)

## Descripción del Problema
En el Día 10 debemos inicializar una serie de máquinas en una fábrica. En la **Parte A**, el problema es un puzzle de bits donde buscamos la secuencia mínima de botones para encender las luces correctas. En la **Parte B**, la física del problema cambia radicalmente hacia una gestión recursiva de voltajes ("Joltage") y arreglos de cableado. Debido a que la naturaleza de los datos cambia por completo entre ambas partes, se ha tomado la decisión arquitectónica de separar sus dominios por completo.

---

## 1. Patrones de Diseño

### Patrón Iterator (Uso Implícito)
En la Parte A, utilizamos un bucle tradicional `for (Machine machine : machines)` dentro del `FactoryManager`. En la Parte B, empleamos la API de Streams (`machines.stream().mapToInt(...)`).
Ambas son implementaciones del **Patrón Iterator**, que proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente[cite: 52]. Este patrón separa la lógica de iteración de las estructuras de datos[cite: 54], mejorando la modularidad de nuestro orquestador.

---

## 2. Inyecciones y Acoplamiento

### Inyección por Constructor, DIP y COI
Tanto en la Parte A como en la Parte B, los orquestadores (`FactoryManager`) no instancian sus propios motores matemáticos. En su lugar, reciben una abstracción (`InitializationStrategy` o `MachineSolver`) a través de su constructor.
* **Principio de Composición sobre Herencia (COI):** En lugar de heredar código de una clase base, se recomienda tener un objeto con esa funcionalidad como propiedad[cite: 37].
* **Principio de Inversión de Dependencias (DIP):** Módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones[cite: 36].
* **Bajo Acoplamiento:** Esto logra la idea de diseñar módulos o componentes que tienen pocas interdependencias[cite: 17].

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión[cite: 30].
* **Dónde está en el código:** Se aplica a dos niveles. Primero, tenemos clases separadas para leer el texto (`FactoryParser`) y para hacer matemáticas (`BfsInitializationOptimizer`). Segundo, al separar la Parte A y la Parte B en sus propias carpetas, evitamos tener una superclase `Machine` o un super-parser que tenga que cambiar por dos motivos distintos (las reglas de la Parte A y las reglas de la Parte B).

### Principio Abierto Cerrado (OCP)
Las clases deben estar abiertas para la extensión, pero cerradas para la modificación[cite: 31, 32].
* **Dónde está en el código:** El `FactoryManager` de la Parte B está diseñado para iterar máquinas y sumar resultados. Si mañana los elfos nos dan un nuevo optimizador cuántico, simplemente crearíamos una nueva clase que implemente `MachineSolver` y se la inyectaríamos. El código del orquestador no se modificaría en absoluto.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Modularidad
Promueve la división del sistema en módulos o componentes independientes[cite: 18].
* **Dónde está en el código:** Es la decisión arquitectónica más importante del Día 10. Al descubrir que las máquinas de la Parte A (`targetMask`, `numLights`) y las de la Parte B (`JoltageState`, `Button`) eran entidades conceptualmente diferentes, creamos dos módulos completamente independientes (`software.aoc.day10.a` y `software.aoc.day10.b`). Esto evita cruzar dependencias y protege la Parte A de los cambios radicales de la Parte B.

### Fundamentos: Alta Cohesión y Código Expresivo
La **Alta Cohesión** refiere a la idea de que las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea[cite: 16]. El **Código Expresivo** dicta que el código debería ser claro y comprensible, facilitando la lectura y el mantenimiento[cite: 20].
* **Dónde está en el código:** El uso masivo de `records` inmutables (como `JoltageState` o `JoltageEffect` en la Parte B, y el record `Machine` en la Parte A) agrupa datos que pertenecen exclusivamente juntos. Además, métodos autodescriptivos como `joltageState.isSolved()` o `joltageState.canApply(effect)` hacen que el dominio se lea casi como lenguaje natural.

### Principio de no repetir código (DRY)
Cada pieza de conocimiento en un software debería tener una representación única inequívoca[cite: 40].
* **Dónde está en el código:** En el `FactoryParser` de la Parte B, múltiples componentes necesitaban limpiar el texto de entrada. En lugar de repetir operaciones de subcadenas (`substring(1, length - 1)`), centralizamos esta lógica en un único método privado `stripParentheses()`, evitando la duplicación de conocimiento.