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


---

## 4. Evolución a la Parte B: Simulación Cuántica (Dynamic Programming)

### Cambio de Estructura: De Existencia a Cantidad
En la Parte A usábamos un `Set` (lógica booleana: ¿hay haz?). En la Parte B, el requisito cambia a contar "líneas de tiempo". Esto requiere cambiar la estructura de datos a un **Mapa de Frecuencias**.

**Código:**
```
Map<Integer, Long> activeTimelines = new HashMap<>();
// Key (Integer): Columna (Posición)
// Value (Long): Número de universos paralelos en ese punto
```

### Algoritmo:
En lugar de simular partículas individuales, implementé un algoritmo de **Programación Dinámica iterativa**. Procesamos la cuadrícula fila por fila. En cada iteración, `activeTimelines` representa el estado comprimido de todo el sistema en esa altura específica.

### Map.merge()
La parte más crítica es gestionar la convergencia: ¿Qué pasa cuando dos divisores envían partículas a la misma celda?En lugar de escribir lógica verbosa (`if containsKey... get... put...`), utilicé el método merge.

```
private void addTimelines(Map<Integer, Long> map, int col, long count) {
    // Si la clave 'col' no existe, inserta 'count'.
    // Si ya existe, ejecuta la función de remapping: (oldVal, newVal) -> oldVal + newVal
    map.merge(col, count, Long::sum); 
}
```
* `Map.merge`: Atomiza la operación de "buscar, sumar y actualizar", haciendo el código más limpio y eficiente.
* `Long::sum`: Utilizo una Method Reference como función de acumulación. Esto es programación funcional pura aplicada a una estructura de datos clásica.

### Prevención de Desbordamiento (Overflow)
Dado el crecimiento exponencial de las rutas, el contador supera rápidamente el límite de un int (2 mil millones). Decisión de Diseño: El uso estricto de Long (y primitivos long) en todo el mapa garantiza que la simulación no falle por desbordamiento aritmético silencioso.


