# DÍA 2: Gift Shop

## 1. Contexto del Problema
El objetivo es procesar una lista de rangos de identificadores (IDs) para detectar y sumar aquellos que son "inválidos".

* **Entrada:** Rangos numéricos en formato texto (ej: `11-22`).
* **Parte A:** Un ID es inválido si consiste en una secuencia repetida **exactamente dos veces** (ej: `1212`).
* **Parte B (Generalización):** Un ID es inválido si consiste en una secuencia repetida **al menos dos veces** (ej: `123123123`).

---

## 2. Paradigma: Programación Declarativa (Streams)
Para resolver este problema, he evitado los bucles `for` tradicionales y he optado por la **API de Streams** que nos
facilita el procesamiento funcional de las colecciones de datos siendo asi mas eficiente y legible. 
La tubería (pipeline) funcional quedaria asi:

1.  **Split:** Dividir la entrada.
2.  **FlatMap:** Convertir rangos en flujos de números.
3.  **Filter:** Aplicar la regla de negocio.
4.  **Sum:** Reducir el resultado.

### Rendimiento: Streams de Primitivos
He utilizado `LongStream` y `flatMapToLong`.
> **Justificación Técnica:** Esta especialización evita el **Autoboxing/Unboxing** (convertir `long` a `Long`). Dado que iteramos sobre rangos que pueden contener millones de números, trabajar con tipos primitivos es una optimización crítica de memoria y CPU.

---

## 3. Arquitectura: Cohesión y Abstracción
He aplicado el **Principio de Responsabilidad Única (SRP)** extrayendo la complejidad a métodos privados (Helper Methods):

* `rangeToStream(String)`: Se encarga exclusivamente del parseo y generación del rango.
* `isRepeatedSequence(long)`: Encapsula la regla de negocio pura (validación).

> **Beneficio:** Esto cumple con el principio de **Abstracción**. El método principal no sabe cómo se parsea un rango ni cómo se valida un número, solo orquesta el flujo.

---

## 4. Evolución a la Parte B: El Refactor
El requisito cambió de "repetición exacta (mitades)" a "patrones periódicos ($N \ge 2$)".

### Adaptación del Algoritmo (Fuerza Bruta Inteligente)
Refactoricé `isRepeatedSequence` para buscar patrones:
1.  Itero sobre posibles longitudes de patrón.
2.  Compruebo divisibilidad (`len % patternLen == 0`).
3.  Reconstruyo el ID esperado usando **`String.repeat()`**.

### Código Expresivo
El uso de `String.repeat(repeats)` es un ejemplo de **Código Expresivo**. En lugar de un bucle manual para concatenar strings, uso un método de la API estándar que describe claramente la intención.

---

## 5. Conclusión: Bajo Acoplamiento
Lo más destacable de la Parte B es lo que **NO** cambió.

* El método principal `calculateInvalidIdSum` permaneció idéntico.
* La lógica de Streams y parseo permaneció idéntica.

Gracias al **Bajo Acoplamiento**, el cambio en la regla de negocio (validación matemática) no afectó a la infraestructura de procesamiento de datos.