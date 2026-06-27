---
title: "Advent of Code - Día 7: Simulador de Taquiones (Abstracción y Eficiencia)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 7: Simulador de Rayos de Taquiones

## Descripción del Problema
En el Día 7 debemos simular la propagación de rayos de taquiones a través de un colector (Manifold) bidimensional. En la **Parte A**, el objetivo es contar cuántas veces se divide un rayo al impactar contra los separadores (`^`). En la **Parte B**, la complejidad aumenta, ya que debemos calcular el total de líneas temporales (caminos) generadas por estas divisiones. El desafío consiste en modelar la cuadrícula de forma segura y diseñar un algoritmo que no colapse por el crecimiento exponencial de los rayos.

---

## 1. Patrones de Diseño Aplicados

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software.

### Patrón Factory Method
* **Teoría:** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método estático que encapsula la creación del objeto.
* **Aplicación en el código:** Se ha implementado en el enumerado `TachyonCell` mediante el método estático `TachyonCell.from(char c)`.
* **Justificación:** En lugar de dejar que el generador de la matriz intente averiguar qué significa cada carácter, delegamos la creación en esta fábrica. Toma un carácter en crudo (como `'S'` o `'^'`) y devuelve la entidad de dominio correcta (`BEAM` o `SPLITTER`), encapsulando la lógica de conversión en un solo lugar.

---

## 2. Inyecciones y Bajo Acoplamiento

### Abstracción de Acceso e Inyección por Constructor
* **Teoría (Bajo Acoplamiento):** Es la idea de diseñar módulos o componentes que tienen pocas interdependencias.
* **Teoría (Abstracción):** Consiste en ocultar los detalles complejos detrás de una interfaz simple.
* **Aplicación en el código:** Los simuladores (`TachyonSimulator` y `QuantumTachyonSimulator`) no instancian su propio mapa. Reciben el entorno a través del constructor (`this.manifold = manifold;`).
* **Justificación:** Gracias a la abstracción, los simuladores no acceden a la matriz de caracteres crudos ni gestionan las excepciones de límites. Dependen de métodos seguros como `manifold.getCellAt(r, c)`. Esto permite cambiar la estructura interna de la cuadrícula en el futuro sin alterar los simuladores.

---

## 3. Principios de Diseño y Leyes

Los principios de diseño actúan como restricciones o reglas que ayudan a alcanzar los fundamentos.

### Ley de Demeter
* **Teoría:** Sugiere que una unidad de software debe tener un conocimiento limitado sobre otras unidades. Se resume en la frase "no aceptes caramelos de extraños".
* **Aplicación en el código:** Los simuladores respetan estrictamente esta ley. No encadenan llamadas inseguras hacia la estructura interna de datos (es decir, evitan hacer `manifold.getGrid()[r][c]`). Le piden directamente a `Manifold` la información que necesitan a través de un método limpio, manteniendo un conocimiento limitado sobre cómo se almacenan los datos.

### Principio de no repetir código (DRY)
* **Teoría:** Cada pieza de conocimiento en un software debería tener una representación única inequívoca.
* **Aplicación en el código:** Se ha unificado la representación del mapa en la clase `Manifold` y `TachyonCell` dentro del paquete general. Al no duplicar la lógica de lectura y coordenadas en la Parte A y la Parte B, evitamos la fragmentación del conocimiento.

---

## 4. Fundamentos de Diseño

Los fundamentos de diseño son las cualidades inherentes que se consideran deseables en cualquier sistema de software.

### Alta Cohesión Semántica
* **Teoría:** Refiere a la idea de que las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** Se ha encapsulado la semántica de la cuadrícula en el tipo `TachyonCell`. En lugar de esparcir caracteres mágicos por la lógica de negocio, se agrupa la definición de los elementos en una entidad altamente cohesiva.

### Código Expresivo (Eliminación de Obsesión por los Primitivos)
* **Teoría:** El código debería ser claro y comprensible, facilitando la lectura y el mantenimiento.
* **Aplicación en el código:** Al usar el enumerado, el código se lee como un flujo de reglas de negocio natural: `if (cell == TachyonCell.SPLITTER) { ... }`. Esto resulta mucho más expresivo que evaluar caracteres abstractos (`if (c == '^')`).

### Nota Algorítmica: Eficiencia mediante State Tracking
Aunque pertenece al ámbito de la optimización matemática, el diseño soporta algoritmos de alta eficiencia. En lugar de fuerza bruta matricial, se implementó un seguimiento de estado (State Tracking) mediante el uso de conjuntos (`HashSet`) y diccionarios (`HashMap` con `Map.merge`). Esto permite que el coste de procesamiento sea proporcional únicamente a los rayos activos en lugar de iterar inútilmente sobre celdas vacías de la cuadrícula.