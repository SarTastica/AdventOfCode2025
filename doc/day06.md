---
title: "Advent of Code - Día 6: Matemáticas de Cefalópodos (Fábricas y DIP)"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 6: Hojas de Cálculo y Matemáticas de Cefalópodos

## Descripción del Problema
En el Día 6 nos enfrentamos a la evaluación de problemas matemáticos (`MathProblem`) a partir de unas hojas de cálculo algo peculiares. La **Parte A** requiere leer las operaciones de forma horizontal, mientras que la **Parte B** introduce una complejidad de parseo leyendo las columnas de forma vertical. En ambas partes, el objetivo es aplicar la operación correcta (suma o multiplicación) a una lista de operandos. El desafío arquitectónico es evitar que la lógica de parseo se enrede con el motor matemático, permitiendo que las operaciones sean escalables.

---

## 1. Patrones de Diseño Aplicados

Los patrones de diseño son soluciones típicas a problemas comunes en el diseño de software[cite: 44]. Son una especie de plantilla que se puede aplicar para resolver un problema en un contexto particular.


### Patrón Factory Method
* **Teoría:** En lugar de usar directamente el constructor de una clase para crear objetos, se llama a un método estático que encapsula la creación del objeto.
* **Aplicación en el código:** Se ha implementado a través de la clase `StandardMathProvider`. El método `getStrategyFor(char symbol)` evalúa el símbolo (ej. `'+'` o `'*'`) y se encarga de crear e instanciar la clase concreta adecuada (`AdditionStrategy` o `MultiplicationStrategy`).
* **Justificación:** En diseños acoplados, el cálculo matemático suele incrustarse con `if-else` o enumerados estáticos dentro de la clase principal. Al usar el *Factory Method*, encapsulamos la creación. El sistema pide "la operación para este símbolo" y la fábrica le devuelve el objeto correcto, ocultando la complejidad de la instanciación.

---

## 2. Inyecciones y Bajo Acoplamiento

### Principio de Inversión de Dependencias (DIP) e Inyección por Constructor
* **Teoría:** Los módulos de alto nivel no deben depender de módulos de bajo nivel, sino de abstracciones.
* **Aplicación en el código:** La clase orquestadora `CephalopodCalculator` no depende de las implementaciones concretas de suma o multiplicación. Toda su dependencia recae en abstracciones puras: `StrategyProvider` y `OperationStrategy`. Estas abstracciones son inyectadas a través de su constructor (`this.provider = provider;`).
* **Justificación (Bajo Acoplamiento):** Esto genera un bajo acoplamiento, que es la idea de diseñar módulos o componentes que tienen pocas interdependencias. El motor matemático puede procesar cualquier tipo de hoja de cálculo y operación sin saber cómo están implementadas por debajo.

---

## 3. Principios de Diseño

Los principios de diseño actúan como restricciones o reglas que ayudan a alcanzar los fundamentos.

### Principio Abierto Cerrado (OCP)
* **Teoría:** Las clases deben estar abiertas para la extensión, pero cerradas para la modificación
* **Aplicación en el código:** Gracias a que la dependencia matemática está invertida (DIP), el `CephalopodCalculator` cumple a la perfección el OCP. Si en un hipotético Día 7 se introduce la operación de resta, el orquestador principal no se tocará en absoluto. Simplemente añadiremos una nueva implementación a nuestra fábrica.

### Principio YAGNI y DRY
* **Teoría YAGNI:** Aconseja a los desarrolladores no añadir funcionalidad hasta que sea realmente necesaria.
* **Teoría DRY:** Cada pieza de conocimiento en un software debería tener una representación única inequívoca[.
* **Aplicación en el código:** 1. **YAGNI:** En la Parte B, se refactorizó un sistema de parseo complejo en una única clase `VerticalWorksheetParser` altamente cohesiva, evitando sobreingeniería.
  2. **DRY:** Las estrategias matemáticas (`OperationStrategy`) se ubicaron en el paquete general. Duplicarlas en las carpetas `a` y `b` habría violado este principio, ya que ambas partes suman y multiplican.

---

## 4. Normas, Leyes y Fundamentos

Los fundamentos de diseño son los conceptos o creencias básicas que subyacen en la práctica del diseño de software.

### Alta Cohesión
* **Teoría:** Refiere a la idea de que las partes de un módulo o componente deben estar estrechamente relacionadas y enfocadas en una única tarea.
* **Aplicación en el código:** La entidad `MathProblem` se ha diseñado como un `record` de Java. Agrupa de forma estrictamente cohesiva la lista de operandos y el símbolo del operador. Además, expone un único método (`solve`) enfocado en una tarea: ejecutar la estrategia inyectada sobre sus propios datos.

### Código Expresivo y Abstracción
* **Teoría (Código Expresivo):** El código debería ser claro y comprensible, facilitando la lectura y el mantenimiento.
* **Teoría (Abstracción):** Consiste en ocultar los detalles complejos detrás de una interfaz simple.
* **Aplicación en el código:** La clase `MultiplicationStrategy` utiliza un enfoque funcional (`operands.stream().reduce(1L, (a, b) -> a * b)`). Esto hace que la lógica se exprese de forma declarativa y comprensible. Al mismo tiempo, la abstracción oculta a las clases superiores si se están usando flujos (streams) o bucles for tradicionales para resolver el problema matemático.