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


## 3. Parte A: Validación con Streams (Clase Range y Filtrado)

### 1. Clase Interna Range (Encapsulamiento)

Primero, definí una clase interna estática `Range`. Su responsabilidad es parsear su propia entrada y validar si un número le pertenece.

> Defensa: Al mover el `split` y el `parseLong` al constructor, libero a la clase principal de lidiar con formatos de texto. 

### 2. Método isFresh (Búsqueda Eficiente)

Este método auxiliar es el motor de búsqueda.

Defensa: Utilicé `anyMatch` de la API de Streams porque no recorre toda la lista, sino que en el momento exacto en que encuentra el primer rango que contiene al ID, devuelve true y detiene la búsqueda. 

### 3. Método Principal countFreshIngredients (Orquestación)

Finalmente, el método principal conecta los datos.
```
return availableIds.stream()
        .filter(id -> isFresh(id, freshRanges))
        .count();
```

> Defensa: el algoritmo principal es puramente declarativo 'de los IDs disponibles, filtra los que sean frescos y cuéntalos'. 

## 4. Evolución a la Parte B: Fusión de Intervalos (Merge Intervals)

### 1. Preparación: Interfaz Comparable

Para fusionar intervalos eficientemente, primero deben estar ordenados. Por eso modifiqué la clase Range para implementar Comparable.
```
@Override
public int compareTo(Range other) {
    return Long.compare(this.start, other.start);
}
````

> Defensa: Esto me permite usar `Collections.sort(ranges)`. Ordenar los rangos por su punto de inicio es un prerrequisito matemático para poder resolver el problema en una sola pasada lineal (O(N))

### 2. Algoritmo de Fusión (El Núcleo Lógico)

Iteramos la lista ordenada comparando el rango `current` (actual) con el `next` (siguiente).
```
if (next.start <= current.end) {
    // Caso A: Solapamiento
    current.end = Math.max(current.end, next.end);
} else {
    // Caso B: Hueco (Gap)
    mergedRanges.add(current);
    current = next;
}
```

Defensa: 
* `next.start <= current.end` "Si el siguiente rango empieza antes de que termine el actual, significa que se solapan o son contiguos.
* Usamos `Math.max` porque un rango podría estar totalmente contenido dentro de otro.
* El else: Si no se tocan, significa que hay un hueco. Guardamos el rango actual como 'cerrado' en la lista de resultados `mergedRanges` y convertimos el siguiente rango en el nuevo current.

### 3. Cálculo Final (Suma de Longitudes)

Una vez que tenemos los rangos fusionados (sin solapamientos), solo queda sumar sus tamaños.
```
return mergedRanges.stream()a
        .mapToLong(Range::length)
        .sum();

// En Range:
public long length() { return end - start + 1; }
```

> Defensa: Delegué el cálculo matemático a Range::length. Nótese el +1: en rangos inclusivos (ej: 5-5 tiene longitud 1), esta aritmética es propensa a errores si se hace manualmente en el bucle principal. Encapsularlo garantiza la corrección."
