---
title: "Advent of Code - Día 12: Empaquetado de Poliominós (Defensa Teórica)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 12: Empaquetado de Poliominós (Análisis Arquitectónico)

## Descripción del Problema
El desafío del Día 12 consiste en un problema de geometría computacional complejo (*2D Bin Packing*). El diseño en el desarrollo de software es un aspecto crítico que determina la calidad, mantenibilidad y escalabilidad de un sistema[cite: 4]. A continuación, se defiende la solución propuesta contrastándola con el enfoque de código acoplado (espagueti) de otros compañeros, estructurando el análisis en las cuatro áreas clave de la ingeniería de software: Patrones, Inyecciones, Principios y Leyes.

---

## 1. Patrones de Diseño

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software y funcionan como plantillas aplicables a contextos particulares[cite: 44, 45].

* **Patrón Factory Method:** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método que encapsula la creación del objeto[cite: 50].
    * *Aplicación en el código:* En lugar de instanciar cada rotación a mano, el método `generateUniqueVariations()` de la clase `Polyomino` actúa como una fábrica. Encapsula la lógica de aplicar transformaciones matriciales (rotar, voltear, normalizar) y devuelve las variaciones válidas.
* **Patrón Iterator:** Proporciona una manera de acceder secuencialmente a los elementos de un objeto agregado sin exponer su representación subyacente[cite: 52].
    * *Aplicación en el código:* El motor `DfsPackingOptimizer` separa la lógica de iteración (recorrer los `BitSets` de `validPlacements`) de las estructuras de datos[cite: 54]. Esto permite iterar y manipular el conjunto de posiciones de manera eficiente sin que el optimizador necesite conocer cómo se dibujan los píxeles en el tablero.

---

## 2. Inyecciones (y Bajo Acoplamiento)

Las inyecciones de dependencias son el mecanismo práctico para alcanzar un bajo acoplamiento, es decir, diseñar módulos o componentes que tienen pocas interdependencias[cite: 17].

* **Inyección de Abstracciones:** * *Aplicación en el código:* El motor de resolución no se acopla a una implementación rígida. Se inyecta la interfaz `PackingStrategy` con la implementación `DfsPackingOptimizer`. Si la lógica de empaquetado debe cambiar, se inyecta una nueva estrategia sin alterar el resto del sistema.
    * *Justificación teórica:* Esto obedece al **Principio de Inversión de Dependencias (DIP)**, el cual dicta que los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones[cite: 36].

---

## 3. Principios de Diseño

Los principios de diseño son normas o guías que orientan cómo se deben aplicar los fundamentos (alta cohesión, bajo acoplamiento, etc.) en la práctica[cite: 26].

* **Principio de Responsabilidad Única (SRP):** Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión[cite: 30].
    * *Aplicación:* La clase `Grid` solo maneja los límites del tablero. La clase `Polyomino` solo maneja su forma geométrica. El `DfsPackingOptimizer` solo maneja el cálculo matemático. (A diferencia del código de la compañera, donde `ChristmasTreeFarm` mezcla la lectura de *regex*, la inicialización y la lógica del puzzle).
* **Principio de no repetir código (DRY):** Cada pieza de conocimiento en un software debería tener una representación única inequívoca[cite: 40].
    * *Aplicación:* El método `normalize()` en la clase `Polyomino` asegura que no existan piezas duplicadas tras rotarlas.
* **Principio YAGNI (You Aren't Gonna Need It):** Aconseja a los desarrolladores no añadir funcionalidad hasta que sea realmente necesaria[cite: 42].
    * *Aplicación:* Nuestras clases geométricas (`Point`, `Polyomino`) son registros simples y directos, evitando crear complejas jerarquías de herencia geométrica innecesarias.
* **Principio de Composición sobre Herencia (COI):** En lugar de heredar código de una clase base, se recomienda tener un objeto con esa funcionalidad como propiedad[cite: 37].
    * *Aplicación:* Nuestro `Polyomino` no hereda de una clase abstracta genérica de matrices, sino que *se compone* de un `Set<Point>`, lo que lo hace mucho más ligero y expresivo.

---

## 4. Leyes de Diseño

* **Ley de Demeter:** Sugiere que una unidad de software debe tener un conocimiento limitado sobre otras unidades; se resume en la frase "no aceptes caramelos de extraños"[cite: 38, 39].
    * *Aplicación en el código:* El `DfsPackingOptimizer` respeta estrictamente esta ley. No accede de forma encadenada a las profundidades de los objetos (ej. no hace `polyomino.getBlocks().get(0).getX()`). En su lugar, pide a la entidad que le devuelva directamente la representación que necesita (o las variaciones) y las transforma inmediatamente en su propio modelo de `BitSet` para operar de forma aislada y privada. El código de la compañera, al acceder a `region.grid()[][]` constantemente desde fuera de la clase, rompe frontalmente esta ley de encapsulamiento.