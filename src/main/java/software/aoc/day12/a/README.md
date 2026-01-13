# DÍA 12: Granja de Árboles de Navidad (Christmas Tree Farm)

## 1. Resumen del Enunciado

**Resumen breve:**
Tras colarnos por un conducto de ventilación, llegamos a una caverna llena de Elfos decorando árboles. El problema es logístico: hay regalos con formas geométricas extrañas (poliminós) que deben colocarse debajo de los árboles. Debemos determinar si un conjunto específico de regalos cabe en una región rectangular dada, siguiendo reglas estrictas de empaquetado.

**Entrada:**
El archivo tiene dos secciones diferenciadas:
1.  **Definición de Formas:** Bloques de texto visuales (ASCII art con `#` y `.`) que definen la geometría de cada tipo de regalo, identificados por un ID numérico.
2.  **Puzzles (Queries):** Líneas con el formato `WxH: c0 c1 c2...`.
    * `WxH`: Dimensiones de la rejilla (ancho x alto).
    * `c0 c1...`: Cantidad de regalos de cada ID que deben encajar en esa rejilla.

**Reglas de Empaquetado:**
* Los regalos **no pueden superponerse**.
* Los regalos deben alinearse perfectamente a la rejilla.
* **Flexibilidad:** Los regalos pueden **rotarse** (90°, 180°, 270°) y **voltearse** (efecto espejo) para encajar.
* **Objetivo:** Contar cuántas de las regiones propuestas tienen una solución válida donde quepan *todos* los regalos listados.

***

## 2. Arquitectura del Problema

He diseñado una solución orientada a objetos que separa la geometría de la lógica de resolución, utilizando un algoritmo de **Backtracking**.

**Componentes:**

1.  **`ChristmasTreeFarm` (Solver):**
    * Controla el flujo principal.
    * Parsea el archivo utilizando un `Iterator` para manejar el cambio de contexto entre "lectura de formas" y "resolución de puzzles".
    * Ejecuta el algoritmo recursivo de empaquetado.

2.  **`Shape` (Geometría):**
    * Representa un tipo de regalo.
    * **Responsabilidad clave:** Pre-calcula y almacena todas las **variaciones** posibles (rotaciones y espejos) al instanciarse. Esto evita recalcular la geometría millones de veces durante la recursión.

3.  **`Point` (Value Object):**
    * Representa una coordenada `(r, c)`.
    * Encapsula la lógica de transformación (`rotate`, `flip`) y comparación.

***

## 3. Implementación: Parsing y Geometría

### 1. Modelado: La Clase Point y Shape
"Para no complicarme con arrays multidimensionales complejos, decidí modelar la geometría desde cero.

Primero, creé la clase Point. La clave aquí es que la hice inmutable y le añadí capacidades de transformación vectorial. En lugar de calcular senos y cosenos, uso operaciones simples de intercambio de coordenadas para rotar 90 grados."
```
public Point rotate() { return new Point(c, -r); }
public Point flip()   { return new Point(r, -c); }
```

Luego construí la clase Shape (Forma). Como rotar las piezas durante la búsqueda recursiva es muy costoso computacionalmente, decidí calcular las 8 variaciones posibles (4 rotaciones y sus espejos) una sola vez al inicio."
```
private List<Set<Point>> generateVariations(Set<Point> base) {
    // Genero y guardo las 8 permutaciones posibles normalizadas al origen (0,0)
    // Esto hace que el backtracking sea O(1) al pedir una variante.
}
```

***

## 2. Orquestador: `‎ChristmasTreeFarm‎`

Su responsabilidad es leer el archivo, entender qué formas existen y resolver los puzzles de empaquetado utilizando fuerza bruta inteligente.

A continuación, el desglose por responsabilidades:

***

### 1. El Cerebro: `solve` y el Registro de Formas

El método `solve` actúa como un **Dispatcher (Despachador)**.

* **Gestión de Memoria (`shapeRegistry`):** Mantiene un `Map` que actúa como la memoria del programa, guardando las definiciones de las piezas (`ID -> Objeto Shape`).
* **Lectura y Despacho:** Lee el archivo línea por línea:
    * Si encuentra una definición de pieza (bloque de `#`) llama a `parseShape`.
    * Si encuentra una definición de puzzle (ej: `5x6: 1 1 2`) llama a `solvePuzzles`.

***

### 2. La Preparación: `solveSinglePuzzle`

Este método prepara el terreno antes de comenzar la fase costosa de probar combinaciones.

1.  **Decodificación:** Convierte la entrada compacta (ej: "2 piezas del tipo 0") en una lista explícita de piezas a colocar (ej: `[0, 0]`).
2.  **Optimización Heurística (CLAVE):**

```
pieces.sort((a, b) -> Integer.compare(shapeRegistry.get(b).getArea(), shapeRegistry.get(a).getArea()));
```

Estrategia Voraz (Greedy): Ordenamos las piezas de mayor a menor tamaño. Es mucho más difícil encajar una pieza gigante al final cuando el tablero está casi lleno.

Si intentamos poner las grandes primero y no caben, el algoritmo se da cuenta rápido y corta esa rama (Fail-fast), ahorrando millones de cálculos innecesarios.

### 3. El Motor Algorítmico: canFit (Backtracking)
Este es el método más importante de la clase. Implementa Recursividad con Vuelta Atrás (Backtracking). Funciona probando caminos en un árbol de decisiones:

Caso Base:
```
if (idx == pieces.size()) return true;
Si he logrado colocar todas las piezas, he ganado (retorno true).
```
**Exploración:**

* Toma la pieza actual.
* Prueba todas sus variaciones (rotaciones/volteos).
* Prueba todas las casillas del tablero (r, c).

**Acción y Backtracking:**

* Intenta colocar la pieza: tryToggle(..., true).
* Si entra, se llama a sí mismo para la siguiente pieza: canFit(..., idx + 1).
* Deshacer: Si la llamada recursiva devuelve false (el camino no llevó a una solución), quita la pieza (tryToggle(..., false)) y prueba en la siguiente casilla.

### 4. El Validador: tryToggle

Este método auxiliar cumple una doble función para mantener el código limpio y respetar el principio DRY (Don't Repeat Yourself):

Validación (Forward Checking): Antes de modificar el tablero, verifica:

* Si la pieza se sale de los límites (nr < 0, etc.).
* Si choca con otra pieza ya colocada (grid[nr][nc]).

**Escritura:**

* Si placing es true: Marca las casillas como ocupadas.
* Si placing es false: Las libera (borra la pieza para el backtracking).

