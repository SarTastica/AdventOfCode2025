# DÍA 8: Parque Infantil (Playground Circuits)

## 1. Explicación del Enunciado

Aterrizamos en un parque subterráneo donde debemos conectar cajas de conexiones eléctricas distribuidas en un espacio 3D.

**La Entrada:**
Una lista de coordenadas X, Y, Z (ej: `162,817,812`) que representan la posición física de las cajas.

**Los Objetivos:**
* **Parte A (Clustering):** Conectar los pares de cajas más cercanos entre sí (limitado a 1000 conexiones) y analizar el tamaño de los circuitos aislados resultantes.
* **Parte B (Conectividad Total):** Seguir conectando las cajas más cercanas hasta que **todas** formen parte de un único circuito gigante. El objetivo es identificar la conexión crítica que unifica todo el sistema.

---

## 2. Arquitectura: Cohesión y Clases Internas

He separado la geometría de la lógica de grafos para respetar el principio **SRP** (Responsabilidad Única).

**1. Point3D (Value Object):**
Clase inmutable encargada exclusivamente de guardar coordenadas y calcular la distancia euclídea (`Math.sqrt`).

**2. CircuitManager (Lógica de Grafos):**
Encargada del algoritmo de conexión.

**3. Connection (Clase Interna Estática):**
Modelé la conexión (punto A, punto B, distancia) como una `private static class`.

* Una conexión es un dato puro que no necesita acceder al estado del `CircuitManager`. Al hacerla estática y `Comparable`, facilito su ordenación (`Collections.sort`) y optimizo la memoria.

---

## 3. Parte A: Union-Find (Conjuntos Disjuntos)

### El Algoritmo
Para gestionar qué cajas están conectadas con cuáles, utilicé la estructura de datos **Union-Find**. Es el estándar para gestionar grupos de elementos que se fusionan dinámicamente.

**Implementación (Find & Union):**
Utilizo un array `parent` donde cada índice apunta a su nodo padre.

El método Find busca el lider i y el método Union une dos grupos.


### Método calculateCircuitScore

Este método osquesta todo:
* Utilizo un Stream para mapear cada línea de texto a una nueva instancia de la clase Point3D y recolecto todos esos objetos en una lista. También guardo n, que es la cantidad total de puntos
* Genera todas las conexiones
* `for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        // Calcula distancia entre TODOS los pares posibles
        allConnections.add(new Connection(i, j, dist));
    }
}` 


* Se ordena la lista de la distancia de la más corta a la más larga
* `Collections.sort(allConnections);`


* Toma solo las primeras 1000 conexiones (más cortas) y las une con el método find-union
* `int limit = Math.min(1000, allConnections.size());
for (int i = 0; i < limit; i++) {
    union(parent, conn.indexA, conn.indexB);
}`


### Cálculo con Streams
Para el resultado final (producto de los 3 grupos más grandes), utilicé un flujo declarativo: `agrupar -> ordenar -> limitar -> reducir`. Esto evita un "código espagueti" de bucles y contadores temporales.

---

## 4. Evolución a la Parte B: Árbol de Expansión Mínima (MST)

### El Desafío
El objetivo cambió de "hacer 1000 conexiones" a "conectar todo el sistema con el mínimo cable". Esto es la definición del algoritmo **Algoritmo de Kruskal**.

### Preparación
Genero primero un grafo completo para ordenar todas las conexiones de más cortas a más larga
```
List<Connection> allConnections = new ArrayList<>();
for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        // ... calcula distancia y añade a la lista
    }
}
Collections.sort(allConnections);
```

### Implementación
Adapté el algoritmo anterior. En lugar de iterar un número fijo de veces, llevo un contador de componentes (`numComponents`).
Inicialmente hay N componentes (cada caja aislada). Cada vez que uno dos grupos distintos, resto 1.

**Condición de Parada (Eficiencia):**
En el momento exacto en que `numComponents == 1`, sé que el grafo es totalmente conexo. Detengo el algoritmo inmediatamente.

**Código de la Lógica Central:**
```
for (Connection conn : allConnections) {
int rootA = find(parent, conn.indexA);
int rootB = find(parent, conn.indexB);

    if (rootA != rootB) {
        parent[rootA] = rootB;
        numComponents--; // Reducimos el número de islas
        
        if (numComponents == 1) {
            // Conectado, devolvemos el resultado de esta última conexión
            return calculateResult(points, conn); 
        }
    }
}
```

