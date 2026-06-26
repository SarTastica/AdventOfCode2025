---
title: "Advent of Code - Día 6: Matemáticas para Cefalópodos"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 6: Matemáticas para Cefalópodos (Estrategias y Matrices)

## Descripción del Problema
En el Día 6 nos encontramos ayudando a unos cefalópodos a resolver problemas matemáticos. El reto consiste en leer una "hoja de cálculo" en texto plano donde las operaciones y los números están dispuestos visualmente en el espacio. En la Parte A, los problemas se leen en columnas adyacentes de izquierda a derecha. En la Parte B, las reglas cambian drásticamente: el formato visual rota 90 grados y los problemas se leen verticalmente (de arriba a abajo).

---

## 1. Patrones de Diseño Utilizados

### Patrón Strategy (Estrategia)
Se utiliza para aislar las operaciones matemáticas. Si el día de mañana los cefalópodos descubren la resta o la división, el motor principal no sufrirá cambios.
* **Dónde está en el código:**
  La interfaz `OperationStrategy` es la abstracción. Sus implementaciones concretas usan el paradigma funcional (Streams) para resolver el cálculo:
  ```java
  public class AdditionStrategy implements OperationStrategy {
      @Override
      public long execute(List<Long> operands) {
          return operands.stream().mapToLong(Long::longValue).sum();
      }
  }
  ```

### Patrón Factory
En lugar de instanciar la estrategia directamente, delegamos la responsabilidad de "decidir qué estrategia usar basándose en un símbolo" a una fábrica proveedora.
* **Dónde está en el código:**
  La interfaz `StrategyProvider` y su implementación `StandardMathProvider` aplican el nuevo y limpio `switch` de Java para devolver objetos polimórficos:
  ```java
  public OperationStrategy getStrategyFor(char symbol) {
      return switch (symbol) {
          case '+' -> new AdditionStrategy();
          case '*' -> new MultiplicationStrategy();
          default -> throw new IllegalArgumentException(...);
      };
  }
  ```

### Inyección de Dependencias (DI)
* **Dónde está en el código:** El motor `CephalopodCalculator` recibe la fábrica a través de su constructor: `public CephalopodCalculator(StrategyProvider provider)`. Esto significa que si en otra parte del puzzle cambian los símbolos (ej. `#` para sumar), solo inyectamos un proveedor distinto.

---

## 2. Principios SOLID Aplicados

### Principio Abierto/Cerrado (OCP)
* **La prueba en tu código:** La clase `CephalopodCalculator` orquesta la resolución de problemas. Al pasar a la Parte B (donde la forma de leer la hoja de papel cambia por completo), **no fue necesario tocar este orquestador en absoluto**. El sistema acepta la nueva lista de `MathProblem` y sigue funcionando de manera idéntica. Está abierto a la extensión de parsers y reglas, pero cerrado a la modificación de su núcleo.

### Principio de Responsabilidad Única (SRP) mediante Composición
En la Parte A, tuvimos un `WorksheetParser` monolítico. Para la Parte B, en lugar de crear un monstruo ilegible, dividimos el problema en tres componentes altamente cohesivos:
1. `MatrixTransposer`: Su única responsabilidad es rotar la matriz de strings.
2. `ProblemExtractor`: Su única responsabilidad es convertir un string limpio en un `MathProblem`.
3. `VerticalWorksheetParser`: Orquesta a los dos anteriores (Patrón Facade/Composición).

---

## 3. Normas de Arquitectura y Clean Code

### Tell, Don't Ask (Delega, no preguntes)
En lugar de que el calculador extraiga los operandos del objeto y haga la matemática él mismo, el calculador le pasa la estrategia al problema para que este se resuelva a sí mismo.
* **Dónde está en el código:**
  En la clase `MathProblem`:
  ```java
  public long solve(OperationStrategy strategy) {
      // El objeto MathProblem oculta su lista de operandos y simplemente invoca la estrategia
      return strategy.execute(this.operands);
  }
  ```

### Programación Funcional y Reducción (MapReduce)
En la clase `MultiplicationStrategy`, evitamos el clásico y propenso a errores bucle `for` acumulativo.
* **Dónde está en el código:**
  Usamos la operación terminal `reduce` de la API de Streams, pasando la semilla `1L` y la función lambda multiplicadora:
  ```java
  return operands.stream().reduce(1L, (a, b) -> a * b);
  ```

---

