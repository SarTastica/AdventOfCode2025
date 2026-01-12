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

### 1. Estructuras de Datos y Modelado

Para modelar el problema, elegí representar el sistema de reactores como un Grafo Dirigido.
* `adjList` (Lista de Adyacencia): Utilicé un `HashMap<String, List<String>>` porque necesito recuperar los vecinos de cualquier nodo en tiempo constante O(1). Una matriz de adyacencia habría sido ineficiente en memoria para un grafo disperso como este.
* `memo` (Caché): Un segundo mapa para almacenar resultados parciales. Esto es la base de la Programación Dinámica que explicaré en el método dfs.

### 2. Método countPaths: Ingesta y Limpieza

Robustez en el Parseo:`String[] destinations = parts[1].trim().split("\\s+");`
* Uso la expresión regular `\\s+` en lugar de un espacio simple. Esto hace el código resistente a entradas 'sucias'

Gestión de Estado: `memo.clear();`
* Crucial para la corrección. Limpio la caché antes de cada ejecución para evitar efectos colaterales si el objeto `ReactorManager` se reutiliza, garantizando que cada cálculo sea independiente.

### 3. Método dfs: El Algoritmo Núcleo

Aquí implementé una Búsqueda en Profundidad (DFS) optimizada. La lógica es recursiva: 'El número de caminos desde aquí es la suma de los caminos de mis vecinos'.

Caso Base (Condición de Parada): `if ("out".equals(current)) return 1;`
* Si llegamos a la salida, hemos encontrado 1 camino válido. Este valor se propagará hacia atrás en la recursión.
* Optimización (Memoización): `if (memo.containsKey(current)) return memo.get(current);`
* Este es el principio más importante del ejercicio. Sin esto, la complejidad sería exponencial (O(2^N)). Con esta línea, transformamos el algoritmo a lineal (O(V+E)). Si ya calculé un nodo, devuelvo el resultado instantáneamente, podando ramas enteras del árbol de búsqueda.
* Acumulación Recursiva: `totalPaths += dfs(neighbor);`
Itero sobre los vecinos y acumulo sus resultados. Si un nodo no tiene vecinos (callejón sin salida), el bucle no corre y retorna 0, manejando correctamente los caminos inválidos."

***

## 4. Parte B: Descomposición Combinatoria

### 1. Método countPathsViaComponents: Estrategia y Topología

En la Parte B, el requisito cambia: debemos pasar obligatoriamente por dos nodos intermedios (dac y fft). En lugar de complicar el DFS con estados extra, apliqué una Descomposición Topológica.

* Análisis de Permutaciones:
```
long path1 = calculateSequentialPath("svr", "dac", "fft", "out");
long path2 = calculateSequentialPath("svr", "fft", "dac", "out");
```
> Dado que el grafo es dirigido, solo existen dos secuencias lógicas posibles para visitar los componentes. Calculo ambas rutas independientemente y las sumo (path1 + path2) para cubrir todo el espacio de soluciones.

### 2. Método calculateSequentialPath: Divide y Vencerás 

Para resolver cada permutación, utilicé la estrategia de Divide y Vencerás, fragmentando el problema en 3 tramos independientes (Legs).
* Principio Multiplicativo (Combinatoria): `return leg1 * leg2 * leg3;`
> Si hay N formas de hacer el tramo 1 y M formas de hacer el tramo 2, el total es NxM. Esto es computacionalmente mucho más eficiente que simular la ruta completa de una sola vez.

*Optimización (Fail Fast): `if (leg1 == 0) return 0;`
> Aplico una Cláusula de Guarda. Si el primer tramo es imposible (0 caminos), corto la ejecución inmediatamente.

### 3. Adaptación del Motor dfs y Gestión de Memoria

Finalmente, tuve que refactorizar el motor de búsqueda para hacerlo reutilizable.

* Invalidación de Caché (Cache Invalidation):
```
private long countPaths(String start, String end) {
    memo.clear(); // CRÍTICO
    return dfs(start, end);
}
```

> La memoización guarda 'caminos hacia un destino'. Como en cada tramo el destino cambia (primero es dac, luego fft, luego out), es obligatorio limpiar la caché `memo.clear()` entre llamadas. Si no lo hiciera, el tramo 2 reutilizaría cálculos 'sucios' del tramo 1 apuntando al destino equivocado.

> Generalización del Algoritmo: Cambié la firma a `dfs(String current, String target)`. Ahora el algoritmo es agnóstico; no busca siempre 'out', sino que busca dinámicamente el nodo objetivo que le pida el orquestador."

