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

## 4. Evolución a la Parte B: Explosión Combinatoria

### El Desafío: Rutas vs. Estados
El requisito cambió de "¿Está activa la celda?" a "¿Cuántas rutas llegan aquí?".
En un sistema cuántico, cada divisor duplica las rutas. Una recursividad ingenua tendría complejidad **O(2^N)**, lo cual es computacionalmente inviable.

### Solución: Programación Dinámica (Mapas)
Cambié la estructura de datos de un Set (binario) a un Map (cuantitativo).

`Map<Integer, Long> activeTimelines = new HashMap<>();`

* **Clave (Integer):** La columna (posición).
* **Valor (Long):** El número de líneas de tiempo que convergen en ese punto.

### Algoritmo de Acumulación
Proceso fila por fila acumulando contadores. Si dos rutas llegan al mismo punto, sumo sus valores.

**Código de Fusión (Merge):**
```
private void addTimelines(Map<Integer, Long> map, int col, long count) {
map.merge(col, count, Long::sum);
}
```

> **Defensa:** Esto reduce la complejidad exponencial a lineal. En lugar de simular millones de partículas individuales, simplemente "arrastro" un contador numérico (Long). Usar long es vital para evitar el desbordamiento numérico (Overflow) dado el crecimiento exponencial.

