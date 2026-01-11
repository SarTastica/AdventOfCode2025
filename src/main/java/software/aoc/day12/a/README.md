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



**Estrategia Algorítmica:**
Se trata de un problema clásico de **Exact Cover / 2D Bin Packing**. Dado que el espacio de búsqueda es pequeño pero complejo, la mejor herramienta es la **Fuerza Bruta con Backtracking** optimizada con podas (pruning).

***

## 3. Implementación: Parsing y Geometría

### Parsing Robusto con Iterator
En lugar de gestionar índices manuales propensos a errores (`i++`), utilizo un `Iterator<String>`.

**Código:**
```
Iterator<String> it = lines.iterator();
while (it.hasNext()) {
    // Detectamos cambio de sección: de formas a puzzles
    if (line.contains("x") && line.contains(":")) return solvePuzzles(line, it);
    parseShape(line, it);
}
```
>Defensa: El uso de un iterador convierte el parseo en una máquina de estados secuencial. Leemos formas hasta que detectamos el patrón NxM:, momento en el que cambiamos el modo de operación sin necesidad de reiniciar bucles.

Geometría y Normalización (Shape)
Para que el algoritmo sea rápido, necesitamos "normalizar" las formas (moverlas a la coordenada 0,0) y generar sus mutaciones.

Código:
```
// Generamos 4 rotaciones x 2 estados (normal y espejo)
current = current.stream().map(Point::rotate).collect(Collectors.toSet());
uniqueVars.add(normalize(current));
```
> Defensa: Utilizo Java Streams para aplicar transformaciones geométricas de forma declarativa. Al almacenar las variaciones en un HashSet, elimino duplicados automáticamente (ej: rotar un cuadrado 90 grados produce la misma figura, no necesitamos comprobarla dos veces).


***

## 4. Algoritmo de Resolución: Backtracking Optimizado

El núcleo de la solución es el método `canFit`. Intenta colocar las piezas una a una recursivamente.

### Optimizaciones Críticas (Fail Fast)

Antes de empezar la recursión costosa, aplico dos optimizaciones fundamentales:

1.  **Chequeo de Área:**
    `if (totalArea > W * H) return false;`
    Si la suma del área de los regalos es mayor que la rejilla, es físicamente imposible. Abortamos inmediatamente.

2.  **Ordenamiento Heurístico (La clave del rendimiento):**
    ```
    pieces.sort((a, b) -> Integer.compare(shapeRegistry.get(b).getArea(), shapeRegistry.get(a).getArea()));
    ```
    > **Defensa:** Ordeno los regalos de **mayor a menor**.
    > * *Analogía:* Es más difícil colocar rocas grandes en un frasco que verter arena.
    > * *Efecto:* Si las piezas grandes no caben, el algoritmo falla en los primeros niveles de recursión, podando ramas enormes del árbol de búsqueda. Si ordenáramos al revés, perderíamos tiempo colocando piezas pequeñas para descubrir al final que la grande no cabe.

### Lógica Recursiva (`tryToggle`)

```
// 1. Intentar poner la pieza en (r, c)
if (tryToggle(grid, r, c, variant, true)) {
    // 2. Recurser: Intentar poner la siguiente
    if (canFit(grid, pieces, idx + 1)) return true;
    // 3. Backtrack: Si falla, quitamos la pieza y probamos otra posición
    tryToggle(grid, r, c, variant, false);
}
```
> Defensa Técnica: He unificado la lógica de colocar (true) y quitar (false) en un solo método tryToggle. Esto reduce la duplicación de código y minimiza bugs. El algoritmo prueba exhaustivamente cada variación en cada celda válida hasta encontrar una solución o descartar todas las posibilidades.