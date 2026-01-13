# DÍA 7: Laboratorios (Tachyon Labs)

## 1. Explicación del Enunciado

Estamos en un laboratorio intentando reparar un teletransportador simulando el comportamiento de partículas en un **Colector de Taquiones**.

**La Física del Problema:**
* **Entorno:** Una cuadrícula 2D donde la energía entra por arriba (`S`) y cae por gravedad.
* **Regla de División:** Si un haz golpea un divisor (`^`), se parte en dos haces nuevos: uno a la izquierda y otro a la derecha.

**Los Objetivos:**
* **Parte A (Física Clásica):** Simular el flujo de energía. Si dos haces chocan en el mismo punto, se fusionan. El objetivo es contar cuántas veces ocurre una división (`^`) activa.
* **Parte B (Física Cuántica):** Simular "Líneas de Tiempo". Aquí los haces no se fusionan, sino que se acumulan las probabilidades. Queremos saber el número total de rutas posibles (líneas de tiempo) que completan el recorrido, lo que genera una explosión combinatoria.

---

## 2. Arquitectura: Cohesión y Simulación Física

He diseñado la clase TachyonLabs basándome en el principio de **Alta Cohesión**.

**Diseño de la Clase:**
* **Datos:** Una matriz de primitivos char[][] que representa el mapa físico.
* **Lógica:** Los algoritmos de simulación (countSplits y countTimelines).

**Justificación SRP:**
Mantuvo los datos y la lógica juntos porque representan un único objeto físico simulado. Separarlos violaría el Principio de Responsabilidad Única, ya que la forma en que se mueven las partículas depende intrínsecamente de la estructura de la cuadrícula.

**Eficiencia:**
El uso de char[][] en lugar de List<String> garantiza un acceso aleatorio **O(1)** a cualquier coordenada, lo cual es crítico para una simulación que consulta celdas constantemente.

---

## 3. Parte A: Simulación Iterativa (BFS)

### Estrategia: Fusión de Haces
En lugar de usar recursividad (que podría causar un StackOverflow), implementé una simulación iterativa "fila a fila".

**Estructura de Datos: Set<Integer>**

Utilicé un Set para guardar las columnas activas en la fila actual.

`Set<Integer> activeColumns = new HashSet<>();`

> **Defensa (KISS):** El Set elimina duplicados automáticamente. Si dos divisores envían energía a la misma celda, el Set solo guarda una entrada. Esto maneja la "fusión de haces" sin necesidad de lógica condicional compleja.

### Abstracción (Límites)
Creé el método `isValid(col)` para encapsular la comprobación de bordes (evitar salir de la matriz por la izquierda o derecha).

***

## 4. Evolución a la Parte B: Simulación de Universos (Dynamic Programming)

Debemos contar **todas las posibles líneas de tiempo** que se generan.

**Solución:** En lugar de rastrear rutas individuales, rastreo **estados comprimidos**.

### 2. Estructura de Datos: El Mapa de Frecuencias
Para lograr esto, cambié la estructura a un `Map<Integer, Long>`.

* **Key (`Integer` - Columna):** Indica "En qué posición estoy".
* **Value (`Long` - Cantidad):** Indica "Cuántas versiones paralelas de la realidad han llegado a este punto".

> **Defensa (Compresión de Estado):** Esto convierte un problema de complejidad exponencial en uno lineal respecto a la altura de la cuadrícula, ya que procesamos fila por fila

### 2. El Algoritmo: Procesamiento por Capas (Double Buffering)
El código utiliza un patrón de **Double Buffering** (búfer doble):

1.  Tengo `activeTimelines` (estado actual).
2.  Creo `nextTimelines` (estado siguiente vacío).
3.  Itero sobre los universos actuales y calculo sus destinos.
4.  Al final de la fila, reemplazo el estado actual con el siguiente (`activeTimelines = nextTimelines`).

> **Defensa (Integridad de Datos):** El uso de dos mapas es vital para evitar "efectos colaterales". No puedo modificar el mapa que estoy leyendo; necesito escribir en uno nuevo para garantizar que los movimientos de una partícula no afecten a sus vecinos en el mismo paso de tiempo lógico.

### 3. `Map.merge()` y Convergencia
La parte más crítica ocurre cuando dos líneas de tiempo convergen en la misma casilla. Aquí brilla el método `addTimelines`:

```
private void addTimelines(Map<Integer, Long> map, int col, long count) {
    // Si la clave no existe, pon 'count'.
    // Si existe, suma el nuevo valor al antiguo (old + new).
    map.merge(col, count, Long::sum);
}
```

Explicación Técnica:

* Si la columna `col` no existe en el mapa, ponle el valor count. Si YA existe (porque otra partícula llegó ahí desde otro lado), suma el nuevo `count` al valor antiguo.

> Defensa (Robustez): El uso de long (64 bits) es obligatorio para evitar un desbordamiento aritmético (Integer Overflow) que daría resultados negativos o incorrectos silenciosamente, garantizando la estabilidad de la simulación.
