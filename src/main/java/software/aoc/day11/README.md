# DÍA 11: Reactor

## 1. Resumen del Enunciado

Nos encontramos en el núcleo de la fábrica, donde un reactor toroidal tiene problemas de comunicación con un rack de servidores. El sistema se modela como una red de dispositivos conectados por cables unidireccionales. El objetivo es analizar el flujo de datos a través de esta red.

**Entrada:**
Una lista de conexiones que define la topología de la red (formato de Lista de Adyacencia):
`origen: destino1 destino2 ...`
Ejemplo: `bbb: ddd eee` (El dispositivo `bbb` envía datos a `ddd` y `eee`).

**Parte A:**
El problema es encontrar redundancia o conectividad. Calcular el número total de **caminos únicos** que existen desde un punto de inicio (`you`) hasta la salida principal (`out`).

**Parte B:**
El problema añade restricciones de paso obligatorio. Calcular cuántos caminos desde el servidor (`svr`) hasta la salida (`out`) pasan obligatoriamente por dos componentes específicos: el conversor (`dac`) y la transformada de Fourier (`fft`), sin importar el orden en que se visiten.

***

## 2. Arquitectura

He modelado el problema utilizando **Teoría de Grafos**.
La clase `ReactorManager` encapsula toda la lógica, manteniendo **Alta Cohesión** entre el parseo de los datos y los algoritmos de recorrido.

**Decisiones de Diseño:**
1.  **Grafo Dirigido (Directed Graph):** Los datos fluyen en un solo sentido.
2.  **Lista de Adyacencia:** Utilicé un `Map<String, List<String>>` para representar el grafo ya el mapa es eficiente en espacio y permite acceso constante (O(1)) a los vecinos.
3.  **Gestión de Estado:** La clase limpia sus estructuras internas (`memo.clear()`) antes de cada cálculo, permitiendo la reutilización segura de la instancia.

***

## 3. Parte A: DFS y Memoización

### 1. Método countPaths (El Orquestador y Constructor)

Este método actúa como la Fachada del algoritmo. Tiene dos responsabilidades principales: Parseo y Gestión de Estado.

Defensa Técnica:

* Construcción de Lista de Adyacencia: Transformo la entrada de texto en un Grafo Dirigido representado por un `Map<String, List<String>>`. Elegí un HashMap porque permite búsquedas de nodos en tiempo constante O(1), lo cual es crítico para el rendimiento del DFS posterior.
* `Regex \\s+`: Utilizo esta expresión regular para dividir los destinos. Es robusta porque funciona correctamente tanto si hay un solo espacio como si hay múltiples espacios accidentales entre los nombres de los nodos.
* `memo.clear()`: Esto es fundamental para el ciclo de vida del objeto. Garantiza que si llamo a `countPaths` varias veces (por ejemplo, en tests unitarios o diferentes partes del problema), no estoy reutilizando datos 'sucios' de cálculos anteriores."

### 2. Implementé una **Búsqueda en Profundidad (DFS)** optimizada.

Este es el núcleo de la solución. Implementa una Búsqueda en Profundidad (DFS) optimizada con Memoización

Defensa Técnica (Puntos Clave):

* ¿Qué hace el algoritmo? Si estoy en el nodo X, ¿de cuántas formas puedo llegar a la salida?. Para responderlo, pregunta a todos sus vecinos y suma sus respuestas.
* El Caso Base: Si el nodo actual es 'out', retornamos 1. Esto significa que hemos completado un camino exitoso. Este 1 se propagará hacia atrás sumándose en los nodos anteriores.
* La memoización evita recalcular el mismo trabajo millones de veces. Transforma la complejidad de Exponencial (lenta) a Lineal (rápida). Simplemente, calculamos cada nodo una única vez, guardamos el resultado, y en futuras visitas lo recuperamos instantáneamente.
* Si `adjList` no contiene el nodo o la lista de vecinos está vacía, el bucle for no se ejecuta, `totalPaths` se mantiene en 0 y eso es lo que se retorna. Esto modela correctamente un 'callejón sin salida' en el grafo."

***



## 4. Parte B: Descomposición Combinatoria

### 1. Método countPathsViaComponents

Este método actúa como el punto de entrada principal. Se encarga de reconstruir el grafo y definir las rutas lógicas de alto nivel que cumplen con los requisitos (pasar por `dac` y `fft`).

**Defensa Técnica:**

* **Identificación de Permutaciones:** Dado que el grafo es dirigido y debemos visitar dos nodos intermedios, determiné que solo existen dos secuencias topológicas válidas: `svr -> dac -> fft -> out` y `svr -> fft -> dac -> out`.
* **Reutilización de Parseo:** Mantengo la misma lógica de construcción del `HashMap` (Lista de Adyacencia) que en la Parte A, asegurando consistencia en la representación del grafo.
* **Suma de Resultados:** Calculo los caminos de ambas permutaciones por separado y devuelvo la suma, cubriendo así el espacio total de soluciones posibles.

### 2. Método calculateSequentialPath (Lógica de Segmentación)

Este método implementa la estrategia de "Divide y Vencerás". En lugar de buscar un camino complejo con restricciones de estado, divide el problema en segmentos secuenciales (legs).



**Defensa Técnica (Puntos Clave):**

* **Principio Multiplicativo (Combinatoria):** Aplico la regla fundamental del conteo. Si hay $N$ formas de ir de A a B, y $M$ formas de ir de B a C, el total es $N \times M$. La línea `return leg1 * leg2 * leg3` es infinitamente más eficiente que simular las rutas completas.
* **Fail Fast (Optimización):** Implemento cláusulas de guarda: `if (leg1 == 0) return 0`. Si el primer tramo del camino no tiene solución, aborto inmediatamente el cálculo de los siguientes tramos, ahorrando ciclos de CPU innecesarios.
* **Abstracción de Tramos:** Divido la ruta en tres sub-problemas independientes: Inicio→Intermedio1, Intermedio1→Intermedio2, Intermedio2→Fin.

### 3. Métodos countPaths y dfs (Motor de Búsqueda y Estado)

Aquí adaptamos el motor de búsqueda para ser genérico y gestionamos el ciclo de vida de la memoria caché.

**Defensa Técnica (Puntos Clave):**

* **Limpieza Crítica de Estado (`memo.clear()`):** Esto es el detalle más importante de la Parte B. La memoización almacena "caminos hasta el objetivo". Como el objetivo cambia en cada tramo (primero vamos a `dac`, luego a `fft`), es obligatorio limpiar la caché entre llamadas. Si no, obtendríamos resultados "sucios" del tramo anterior.
* **DFS Genérico (Parametrizado):** Refactoricé el método `dfs` para aceptar un parámetro `target` dinámico. En la Parte A el destino siempre era "out", pero aquí necesitamos calcular caminos hacia cualquier nodo intermedio (`dac`, `fft`), lo que aumenta la flexibilidad del algoritmo.
* **Memoización Contextual:** Al igual que en la Parte A, cacheamos los resultados parciales (`memo.put`), garantizando una complejidad lineal $O(V+E)$ para cada uno de los tramos secuenciales.