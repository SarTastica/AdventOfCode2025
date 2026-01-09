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

**¿Por qué Estática?**
Una conexión es un dato puro que no necesita acceder al estado del `CircuitManager`. Al hacerla estática y `Comparable`, facilito su ordenación (`Collections.sort`) y optimizo la memoria.

---

## 3. Parte A: Union-Find (Conjuntos Disjuntos)

### El Algoritmo
Para gestionar qué cajas están conectadas con cuáles, utilicé la estructura de datos **Union-Find**. Es el estándar para gestionar grupos de elementos que se fusionan dinámicamente.

**Implementación (Find & Union):**
Utilizo un array `parent` donde cada índice apunta a su nodo padre.

**Código (Métodos Auxiliares):**
```
private int find(int[] parent, int i) {
if (parent[i] == i) return i;
return find(parent, parent[i]); // Recursión para encontrar la raíz
}

private void union(int[] parent, int i, int j) {
int rootA = find(parent, i);
int rootB = find(parent, j);
if (rootA != rootB) {
parent[rootA] = rootB; // Fusión de conjuntos
}
}
```

### Cálculo con Streams
Para el resultado final (producto de los 3 grupos más grandes), utilicé un flujo declarativo: `agrupar -> ordenar -> limitar -> reducir`. Esto evita un "código espagueti" de bucles y contadores temporales.

---

## 4. Evolución a la Parte B: Árbol de Expansión Mínima (MST)

### El Desafío
El objetivo cambió de "hacer 1000 conexiones" a "conectar todo el sistema con el mínimo cable". Esto es la definición de libro del **Algoritmo de Kruskal**.

### Implementación: Kruskal con Parada Temprana
Adapté el algoritmo anterior. En lugar de iterar un número fijo de veces, llevo un contador de componentes (`numComponents`).
Inicialmente hay N componentes (cada caja aislada). Cada vez que uno dos grupos distintos, resto 1.

**Condición de Parada (Eficiencia):**
En el momento exacto en que `numComponents == 1`, sé que el grafo es totalmente conexo (MST completo). Detengo el algoritmo inmediatamente.

**Código de la Lógica Central:**
```
for (Connection conn : allConnections) {
int rootA = find(parent, conn.indexA);
int rootB = find(parent, conn.indexB);

    if (rootA != rootB) {
        parent[rootA] = rootB;
        numComponents--; // Reducimos el número de islas
        
        if (numComponents == 1) {
            // ¡Conectado! Devolvemos el resultado de esta última conexión
            return calculateResult(points, conn); 
        }
    }
}
```
### Optimización: Path Compression
En el método `find`, uso la técnica de **Compresión de Caminos**: `parent[i] = find(...)`. Esto "aplana" el árbol cada vez que buscamos, haciendo que las futuras búsquedas sean casi instantáneas (O(1) amortizado).

---

