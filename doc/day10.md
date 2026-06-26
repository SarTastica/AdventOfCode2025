---
title: "Advent of Code - Día 10: Configuración de la Fábrica"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output:
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 10: Configuración de la Fábrica

## Descripción del Problema
En el Día 10 debemos optimizar la configuración de una serie de máquinas en una fábrica. Cada máquina cuenta con botones que alteran los niveles de energía (joltages) mediante un cableado específico. El objetivo es encontrar la cantidad mínima de pulsaciones necesarias para alcanzar un estado objetivo. La dificultad reside en la explosión combinatoria del espacio de estados y en la necesidad de mantener el código estructurado ante una lectura de datos compleja.

---

## 1. Patrones de Diseño

### Patrón Strategy
* **Dónde está en el código:** Creación de las interfaces `InitializationStrategy` (Parte A) y `MachineSolver` (Parte B), y sus implementaciones concretas `BfsInitializationOptimizer` y `MemoizedMachineSolver`.
* **Justificación:** Aisla completamente la algoritmia matemática del flujo de control principal. Permite intercambiar estrategias de resolución sin afectar al resto del sistema.

### Patrón Factory (Especializado en Parsing)
* **Dónde está en el código:** La clase `FactoryParser`.
* **Justificación:** Centraliza la lógica de creación de objetos complejos. Al delegar la interpretación de las expresiones regulares y la limpieza de cadenas (`stripParentheses`) a esta clase, las entidades de dominio quedan libres de lógica de creación.

---

## 2. Inyecciones y Acoplamiento

### Inyección de Dependencias por Constructor
* **Dónde está en el código:** Las clases `FactoryManager` (tanto en la parte A como en la B) reciben sus estrategias (`InitializationStrategy` o `MachineSolver`) a través del constructor.
* **Justificación:** Garantiza que el orquestador (`FactoryManager`) no tenga la responsabilidad de instanciar los motores matemáticos. Esto facilita enormemente el testing, ya que se pueden inyectar estrategias *mockeadas* o de prueba.

### Bajo Acoplamiento Estructural
* **Dónde está en el código:** El flujo de datos va desde `FactoryParser` $\rightarrow$ `List<Machine>` $\rightarrow$ `FactoryManager`.
* **Justificación:** El orquestador desconoce cómo se leen los datos del archivo de texto, y el *parser* desconoce cómo se resuelven los puzzles. Si el formato del `.txt` cambia mañana, los algoritmos matemáticos no sufren ni un solo cambio.

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
* **Dónde está en el código:** La solución está fuertemente dividida en capas:
    1. **Lectura y Transformación:** `FactoryParser`.
    2. **Almacenamiento de Estado:** `Machine`, `Button`, `JoltageState`, `JoltageEffect`.
    3. **Lógica de Negocio:** Optimizadores y Solvers.
    4. **Orquestación:** `FactoryManager`.
* **Justificación:** Evita el antipatrón de "Clase Dios" (God Class) o *Fat Domain Model*, donde una sola clase hace de todo.

### Principio Abierto/Cerrado (OCP)
* **Dónde está en el código:** La relación entre `FactoryManager` y `MachineSolver`.
* **Justificación:** La clase manager está *cerrada* a la modificación (no necesitamos tocarla) pero *abierta* a la extensión (podemos crear una nueva clase `HeuristicMachineSolver` e inyectarla sin problemas).

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Inmutabilidad (Records)
* **Dónde está en el código:** Se han utilizado `records` para modelar el dominio puro (`Machine`, `Button`, `JoltageState`, `JoltageEffect`).
* **Justificación:** Garantiza que una vez creada la máquina o el estado de voltaje, nadie puede mutarlo accidentalmente. Esto es crítico en algoritmos que exploran múltiples caminos simultáneamente (como BFS).

### Fundamento: Funciones Puras y Thread-Safety (Sin Estado Mutable)
* **Dónde está en el código:** En el `MemoizedMachineSolver`, el mapa de memoización (`knownStates`) se inicializa en el método `solve` y se pasa como argumento recursivo, en lugar de ser un atributo de la clase.
* **Justificación:** Convierte a la estrategia en una entidad *Stateless*. Esto significa que el algoritmo es seguro para hilos (*thread-safe*) y permitiría procesar múltiples máquinas en paralelo (`parallelStream()`) sin corrupción de memoria.

