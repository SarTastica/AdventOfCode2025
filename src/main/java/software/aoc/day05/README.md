# DÍA 5: Cafetería (Inventory Management)

## 1. Explicación del Enunciado

El problema nos sitúa en la cocina de la cafetería, donde debemos ayudar a los Elfos a distinguir ingredientes frescos basándonos en identificadores numéricos.

* **La Entrada:**
* 1. Una lista de **rangos de ingredientes frescos** (ej: 3-5, 10-14)
* 2. Una lista de **IDs disponibles** (números sueltos: 1, 5, 8...).


* **Parte A:** Debemos filtrar la lista de IDs disponibles y contar cuántos de ellos caen dentro de **al menos uno** de los rangos de frescura.
* **Parte B:** Ignoramos la lista de IDs disponibles. El objetivo ahora es calcular el **volumen total** de números únicos que cubren los rangos de frescura, teniendo en cuenta que muchos se superponen (ej: 10-20 y 15-25).


## 2. Arquitectura y Estructura

Para resolver este problema, he diseñado una estructura basada en el **Agrupamiento Lógico** y el principio **SRP** (Responsabilidad Única).

**Clases Principales:**

1. **InventoryManager:** Es la clase principal encargada de la lógica de negocio y del procesamiento de los datos (Streams).
2. **Range (Clase Interna Estática):** He modelado el concepto de "intervalo" (inicio y fin) en una clase propia.


* **Cohesión:** Un Range solo tiene sentido en este contexto. Mantenerlo dentro evita contaminar el paquete con clases diminutas.
* **Eficiencia:** Al ser static, no necesita acceder a la instancia de InventoryManager, lo que optimiza la memoria y deja claro que es una entidad de datos independiente.


## 3. Parte A: Validación y Filtrado con Streams

### Clase Range

Para garantizar la integridad de los datos, delegué la lógica de parseo al constructor de la clase `Range`.

```
public Range(String rangeStr) {
String[] parts = rangeStr.split("-");
this.start = Long.parseLong(parts[0]);
this.end = Long.parseLong(parts[1]);
}
```

### Abstracción de la Lógica de Negocio
El método `isFresh` actúa como una fachada que oculta la complejidad de la búsqueda.


```
private boolean isFresh(long id, List<Range> ranges) {
return ranges.stream().anyMatch(range -> range.contains(id));
}
```

Utilicé `anyMatch` en lugar de filtrar toda la lista. `anyMatch` es una operación de **cortocircuito**: en cuanto encuentra el *primer* rango que contiene al ID, detiene la búsqueda y devuelve `true`. Esto es mucho más eficiente que recorrer toda la lista innecesariamente.

### Pipeline
El método principal orquesta el flujo sin bucles explícitos.


```
return availableIds.stream()
.filter(id -> isFresh(id, freshRanges))
.count();
````

La lectura es directa: "Del flujo de IDs, filtra los que sean frescos y cuéntalos". Esto reduce la carga cognitiva al leer el código (Programación Declarativa).

## 4. Evolución a la Parte B: Algoritmo de Fusión

### Preparación de Datos (Comparable)
El algoritmo de fusión requiere que los intervalos estén ordenados cronológicamente.

**Código (Interfaz Comparable):**
```
public int compareTo(Range other) {
return Long.compare(this.start, other.start);
}
```
Implementar `Comparable` permite delegar el ordenamiento a `Collections.sort(ranges)`. Java utiliza internamente **Timsort** (un híbrido de MergeSort y InsertionSort), que es altamente eficiente (O(N log N)) para datos parcialmente ordenados.

### Lógica de Fusión (Merge Logic)
El núcleo de la solución es un bucle que gestiona el solapamiento.

**Código (Fusión):**
```
if (next.start <= current.end) {
// Caso: Solapamiento
current.end = Math.max(current.end, next.end);
} else {
// Caso: Hueco (Gap)
mergedRanges.add(current);
current = next;
}
```
* **Solapamiento:** Si el siguiente rango empieza *antes* de que termine el actual, son parte del mismo bloque. Extendemos el final del actual usando `Math.max` (para cubrir ambos).
* **Hueco:** Si no se tocan, cerramos el rango `current`, lo guardamos en la lista de resultados y comenzamos uno nuevo.

### Cálculo Final (Encapsulamiento)
Para obtener el resultado, sumamos las longitudes de los rangos ya fusionados.

**Código (Suma):**
```
return mergedRanges.stream()
.mapToLong(Range::length)
.sum();

// En clase Range:
public long length() { return end - start + 1; }
````
(SRP):El cálculo de la longitud (`end - start + 1`) está encapsulado en `Range`. La clase principal no necesita saber si los rangos son inclusivos (+1) o exclusivos. Esto protege al algoritmo principal de cambios en la definición de "longitud".
