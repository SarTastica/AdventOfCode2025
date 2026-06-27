---
title: "Advent of Code - Día 10: Configuración de la Fábrica (Análisis Teórico)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 10: Optimización de la Fábrica (Análisis de Diseño)

## Descripción del Problema
El desafío consiste en configurar una serie de máquinas donde cada una tiene botones que alteran los voltajes. El objetivo es alcanzar un estado objetivo mediante el mínimo número de pulsaciones. Arquitectónicamente, esto implica separar la lógica de parseo del archivo de entrada de la lógica de optimización (búsqueda de caminos y memoización).

---

## 1. Patrones de Diseño

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software.

### Patrón Factory Method
* **Justificación:** Se utiliza para encapsular la creación de objetos complejos.
* **Aplicación:** La clase `FactoryParser` implementa este patrón al transformar cadenas de texto crudas en entidades de negocio (`Machine`, `Button`).
* **Utilidad:** Esta centralización permite que el sistema maneje cambios en el formato de entrada (como cambios en los separadores o paréntesis) sin necesidad de modificar las entidades de dominio, desacoplando la lógica de creación de la representación de los objetos[cite: 50].


---

## 2. Inyecciones y Acoplamiento

### Principio de Inversión de Dependencias (DIP)
* **Justificación:** Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación:** El `FactoryManager` y los solvers no dependen de implementaciones concretas de resolución. Se utiliza la inyección por constructor para recibir las estrategias (`InitializationStrategy` o `MachineSolver`).
* **Utilidad:** Esto garantiza un **Bajo Acoplamiento**, permitiendo probar la lógica de negocio independientemente del método de parseo o de las implementaciones algorítmicas.

---

## 3. Principios de Diseño

Los principios de diseño actúan como normas que orientan cómo aplicar los fundamentos en la práctica.

### Principio de Responsabilidad Única (SRP)
* **Justificación:** Cada módulo o clase debe tener una sola razón para cambiar, reflejando la alta cohesión
* **Aplicación:** Se ha dividido el sistema en capas: `FactoryParser` (transformación), `Machine` (dominio) y `MachineSolver` (lógica).
* **Utilidad:** Evita el antipatrón de la "Clase Dios", asegurando que si la lógica de negocio cambia, no sea necesario tocar el código de lectura de archivos.

### Principio Abierto/Cerrado (OCP)
* **Justificación:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación.
* **Aplicación:** El `FactoryManager` es un sistema cerrado a modificaciones; su código no cambia cuando añadimos nuevos solvers. Está abierto a la extensión, ya que podemos inyectar nuevos `MachineSolver` sin alterar el orquestador principal.

### Principio DRY (Don't Repeat Yourself)
* **Justificación:** Cada pieza de conocimiento debe tener una representación única inequívoca.
* **Aplicación:** La lógica de parseo y la creación de las `Machine` se centraliza en una única clase, evitando la duplicación de código en la Parte A y Parte B.

---

## 4. Fundamentos de Diseño

Son las cualidades inherentes deseables en cualquier sistema de software.

### Alta Cohesión
* **Justificación:** Refiere a que las partes de un módulo deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación:** El uso de `records` (como `JoltageState` y `Machine`) permite agrupar datos y comportamiento relacionado de forma natural.
* **Utilidad:** Al encapsular el estado de voltaje junto con métodos como `isSolved()`, logramos una entidad altamente cohesiva que no depende de factores externos para validar su propia integridad.

### Código Expresivo y Abstracción
* **Justificación:** El código debe ser claro y la abstracción ayuda a ocultar detalles complejos.
* **Aplicación:** La interfaz `MachineSolver` oculta la complejidad del algoritmo de memoización. El orquestador interactúa con una interfaz simple `solve(Machine machine)`, sin preocuparse por si la implementación utiliza `HashMap`, memoización o fuerza bruta.
* **Utilidad:** Facilita el mantenimiento y la lectura del sistema, permitiendo que cualquier desarrollador entienda la intención del código sin necesidad de descifrar la implementación de bajo nivel.