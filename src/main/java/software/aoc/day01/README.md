# DÍA 1: La Caja Fuerte (Safe Cracking)

## 1. Introducción y Contexto
El objetivo es descifrar la combinación de una caja fuerte simulando el movimiento de su dial con posiciones del **0 al 99**.

* **Parte A:** El desafío es contar cuántas veces exactas el dial se **detiene** en la posición `0`.
* **Parte B (Cambio de requisitos):** El "método 0x434C49434B" nos obliga a contar cuántas veces el dial **pasa** por el `0` durante el movimiento (clics intermedios).

---

## 2. Arquitectura y Diseño: SRP y Cohesión
He diseñado dos clases principales (`Order` y `Dial`) basándome en el **Principio de Responsabilidad Única (SRP)**. Esto asegura que cada clase tenga una única razón para cambiar, haciendo el código más mantenible.

### Desglose de Clases
* **Clase `Order`:** Su única responsabilidad es **parsear y encapsular** la instrucción bruta (ej: `L68`) y validar que sea correcta.
* **Clase `Dial`:** Su responsabilidad es **gestionar el estado** de la caja fuerte (posición actual) y aplicar la lógica de movimiento.

> **Nota sobre Cohesión:** Si hubiera metido el parseo de strings dentro de la clase `Dial`, habría bajado la **Cohesión**, mezclando lógica de negocio (matemáticas del dial) con lógica de infraestructura (procesamiento de texto).

---

## 3. Implementación Técnica (Parte A)
Destaco tres puntos clave apoyados en la teoría de diseño de software:

1.  **Diseño por Contrato (Fail Fast):**
    En el constructor de `Order`, valido que la entrada no sea nula ni vacía. Si es inválida, lanzo una excepción inmediatamente. Esto garantiza el **Principio de Mínima Sorpresa**

2.  **Código Expresivo (Clean Code):**
    En la clase `Dial`, he definido:
    ```java
    private static final int MAX_POSITIONS = 100;
    ```
    Evité poner el "número mágico" `100` directamente en las fórmulas. Usar constantes con nombre facilita la lectura y el mantenimiento.

3.  **Encapsulamiento Riguroso:**
    Las variables `currentPosition` y `zeroHits` son **privadas**. La única forma de modificarlas es a través del método público `apply()`. Esto impide que una clase externa modifique la posición del dial arbitrariamente sin pasar por las reglas de negocio.

---

## 4. Evolución a la Parte B: Refactorización
El enunciado introdujo un cambio crítico: **contar los pasos intermedios**. Esto invalidó mi enfoque matemático de la Parte A (que usaba aritmética modular $O(1)$ para calcular solo el destino).

### Estrategia: Simulación Discreta (KISS)
He cambiado la estrategia a una simulación paso a paso. En el método `apply`, ahora utilizo un bucle que itera `amount` veces.

> **Defensa Teórica (KISS):** Podría haber intentado derivar una fórmula matemática compleja para calcular intersecciones en un anillo. Sin embargo, apliqué el principio **KISS (Keep It Simple, Stupid)**.
> La lógica de un bucle que simula el 'clic' físico es trivial de entender y verificar. Dado que el rango de entrada no es excesivamente grande, la simulación es la solución más robusta.

### Manejo Explícito de Límites
Dentro del bucle, sustituí la fórmula matemática por una comprobación explícita:

```java
if (currentPosition < 0) currentPosition = 99;
if (currentPosition >= MAX_POSITIONS) currentPosition = 0;