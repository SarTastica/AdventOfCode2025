# DÍA 3: (Lobby Batteries)

## 1. Contexto del Problema
El objetivo es extraer la máxima energía posible de bancos de baterías representados por secuencias numéricas.

* **Entrada:** Cadenas de dígitos (ej: `98765...`).
* **Parte A:** Encontrar el mayor número de **2 dígitos** respetando el orden secuencial.
* **Parte B (Cambio de Escala):** Encontrar el mayor número de **12 dígitos**. Esto introduce desafíos de complejidad algorítmica y desbordamiento de tipos de datos.

---

## 2. Arquitectura: Cohesión y SRP
La solución se divide en dos clases para respetar el **Principio de Responsabilidad Única (SRP)**:

* **`BatteryBank`:** Experto en la lógica de negocio de una sola secuencia. Su única razón para cambiar es si varían las reglas de formación del voltaje.
* **`PowerSystem`:** Responsable de la orquestación y el procesamiento de la lista completa (Streams).

> **Nota sobre Cohesión:** Separar la lógica de cálculo (`getMaxJoltage`) de la iteración de la lista (`calculateTotalPower`) facilita las pruebas unitarias y mantiene el código modular.

---

## 3. Implementación Parte A: Optimización


### Algoritmo Heurístico (Greedy Inverso)
En lugar de comprobar todos los pares posibles (O(N^2)), implementé una búsqueda inversa:
1.  Busco el primer dígito (decenas) empezando por `9` hacia abajo.
2.  Busco el segundo dígito (unidades) a la derecha del primero, también de `9` a `1`.
* **Beneficio:** El primer par válido encontrado es matemáticamente el máximo. Es eficiente y evita iteraciones innecesarias.

### Rendimiento con Streams
En `PowerSystem`, utilizo **`mapToInt`** para trabajar con `IntStream`.
* Esto evita el **Autoboxing** (`Integer` vs `int`), optimizando el uso de memoria y CPU al sumar los resultados.

---

## 4. Evolución a la Parte B: Escala y Tipos

El requisito cambió de 2 a 12 dígitos. Esto rompió la solución anterior por dos razones:
1.  **Desbordamiento Numérico:** Un número de 12 dígitos supera el límite de `int` (2 mil millones).
2.  **Complejidad Algorítmica:** Anidar 12 bucles `for` es inviable y no mantenible.

### Refactorización a `long`
Migré todo el sistema a **`long`** y **`LongStream`**.
> **Diseño Defensivo:** Un número de 12 dígitos puede llegar a los 9 billones. Usar `long` (64 bits) es obligatorio para evitar *Overflow*.

### Algoritmo Voraz con Límites (Greedy with Bounds)
Implementé un algoritmo constructivo iterativo:
* Calculo un **Límite de Búsqueda (`searchLimit`)**: `longitud_total - caracteres_pendientes`.
* Busco el dígito más alto posible *dentro* de ese rango seguro.
* "Bloqueo" ese dígito y avanzo al siguiente.

Este enfoque garantiza que siempre reservamos espacio suficiente para completar la secuencia de 12 dígitos, maximizando el valor en cada paso.

### Código Expresivo y KISS
* **Constantes:** Uso `private static final int TARGET_LENGTH = 12;` para evitar "números mágicos".
* **KISS (Keep It Simple):** Preferí un bucle `while` iterativo sobre una solución recursiva compleja. Es más eficiente en memoria (evita *StackOverflow*) y más fácil de razonar (principio **YAGNI**).

---

## 5. Conclusión
La Parte B requirió rediseñar el algoritmo para manejar restricciones espaciales (reservar caracteres) y de capacidad (`long`).

He mantenido la **Arquitectura Modular**, asegurando que la complejidad creciente se quede encapsulada dentro de `BatteryBank` (Abstracción), sin afectar al orquestador `PowerSystem`.