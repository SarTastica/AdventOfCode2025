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

Para representar la red de dispositivos, elegí un enfoque clásico de Teoría de Grafos.

El Código:
```
private final Map<String, List<String>> adjList = new HashMap<>();
private final Map<String, Long> memo = new HashMap<>();
```
* Grafo Dirigido (adjList): Implementé una Lista de Adyacencia usando un HashMap. El mapa me permite acceso O(1) para obtener los vecinos de cualquier nodo dado su nombre (String).
* Caché (memo): Almacena NombreNodo -> NúmeroDeCaminos, permitiendo que el algoritmo recuerde lo que ya calculó (Programación Dinámica).

### 2. Método countPaths: Ingesta y Limpieza

Este método actúa como el Constructor del Grafo y orquestador. Su responsabilidad es transformar texto crudo en una estructura navegable.
```
String[] parts = line.split(":");
// ...
String[] destinations = parts[1].trim().split("\\s+");
adjList.put(source, Arrays.asList(destinations));
```

* Primero divido por : para separar el nodo origen de sus conexiones.
* Sanitización (\\s+): Al procesar los destinos, uso la expresión regular `\\s+` en lugar de un espacio simple " ". Esto hace el código robusto ante entradas "sucias". 

Manejo de Nodos Hoja (Edge Cases)
```
} else {
    adjList.put(source, new ArrayList<>());
}
```
* Prevención de NullPointer: Si un dispositivo aparece a la izquierda pero no tiene conexiones a la derecha (es un callejón sin salida), lo añado al mapa con una lista vacía. Esto simplifica enormemente el DFS posterior: no tengo que comprobar si adjList.get(nodo) es null, solo itero sobre una lista vacía y el bucle termina naturalmente.

Gestión de Estado (Idempotencia)
```
memo.clear();
return dfs("you");
```
* La llamada a `memo.clear()` es crítica. Dado que memo es un campo de instancia, debo limpiarlo antes de cada ejecución. Esto garantiza que el objeto ReactorManager sea reutilizable y que cálculos de tests anteriores no corrompan el resultado actual.
***

### DFS
Este método dfs resuelve el problema utilizando Programación Dinámica. 
* Defino un caso base: llegar a 'out' cuenta como 1 camino.
* Para cualquier otro nodo, el número de caminos es la suma recursiva de los caminos de sus vecinos.
* El grafo tiene muchas rutas que convergen. Al almacenar el resultado de cada nodo en el mapa memo la primera vez que lo visito, transformo una complejidad exponencial en una lineal respecto al número de aristas y vértices

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

* Optimización: `if (leg1 == 0) return 0;`
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

> Ees obligatorio limpiar la caché `memo.clear()` entre llamadas. Si no lo hiciera, el tramo 2 reutilizaría cálculos 'sucios' del tramo 1 apuntando al destino equivocado.


