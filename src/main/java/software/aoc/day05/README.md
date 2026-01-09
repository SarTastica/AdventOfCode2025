# DÍA 5: Cafetería (Inventory Management)

## 1. Explicación del Enunciado

El problema nos sitúa en la cocina de la cafetería, donde debemos ayudar a los Elfos a distinguir ingredientes frescos basándonos en identificadores numéricos.

**La Entrada:**

Nos dan dos bloques de datos:
1. Una lista de **rangos de ingredientes frescos** (ej: 3-5, 10-14).
2. Una lista de **IDs disponibles** (números sueltos: 1, 5, 8...).

**Los Objetivos:**

* **Parte A:** Debemos filtrar la lista de IDs disponibles y contar cuántos de ellos caen dentro de **al menos uno** de los rangos de frescura.
* **Parte B:** Ignoramos la lista de IDs disponibles. El objetivo ahora es calcular el **volumen total** de números únicos que cubren los rangos de frescura, teniendo en cuenta que muchos se superponen (ej: 10-20 y 15-25).


## 2. Arquitectura y Estructura

Para resolver este problema, he diseñado una estructura basada en el **Agrupamiento Lógico** y el principio **SRP** (Responsabilidad Única).

**Clases Principales:**

1. **InventoryManager:** Es la clase principal encargada de la lógica de negocio y del procesamiento de los datos (Streams).
2. **Range (Clase Interna Estática):** He modelado el concepto de "intervalo" (inicio y fin) en una clase propia.

**¿Por qué una Clase Interna Estática?**

Definí private static class Range dentro del manager por dos razones:

* **Cohesión:** Un Range solo tiene sentido en este contexto. Mantenerlo dentro evita contaminar el paquete con clases diminutas.
* **Eficiencia:** Al ser static, no necesita acceder a la instancia de InventoryManager, lo que optimiza la memoria y deja claro que es una entidad de datos independiente.


## 3. Parte A: Validación y Filtrado

### La Solución Técnica

El objetivo es validar pertenencia. Para ello, delegué la lógica matemática a la clase Range.

**Código de Validación (en clase Range):**

`public boolean contains(long id) { return id >= start && id <= end; }`

En la clase principal, utilicé **Streams** que nos facilita el procesamiento funcional de las colecciones de datos, en lugar de bucles for anidados.


**Código de Procesamiento:**

Para una mayor abstracción, extraje la lógica de comprobación al método auxiliar isFresh. Esto oculta la complejidad del anyMatch y permite que el 
código principal se lea casi como lenguaje natural.

`return availableIds.stream()
.filter(id -> isFresh(id, freshRanges))
.count();`

**Código de Parseo:**

Para una mayor rebustez, el parseo del texto (ej: "3-5") se realiza dentro del **constructor** de Range. Esto garantiza que nunca trabajamos con objetos en estado inválido (Principio de Mínima Sorpresa).


## 4. Evolución a la Parte B: Geometría de Intervalos

### El Desafío

Aquí el problema cambia de "filtrar" a "fusionar". No podemos simplemente sumar las longitudes de los rangos porque hay **solapamientos**. Si sumamos el rango 10-20 y el 15-25, contaríamos dos veces los números del 15 al 20.

### Implementación: Algoritmo "Merge Intervals"

Para resolver esto, primero ordenamos los rangos y luego los fusionamos linealmente.

**1. Contrato de Ordenación (Comparable):**

Hice que la clase Range implementara Comparable. Esto me permite usar Collections.sort(ranges), ordenándolos por su punto de inicio. Sin este paso, el algoritmo de fusión es imposible.

**2. Lógica de Fusión (Bucle Explícito):**

A diferencia de la Parte A, aquí **no usé Streams** para la lógica central. Usé un bucle for tradicional.

**Código de Fusión:**

if (next.start <= current.end) {
current.end = Math.max(current.end, next.end); // Hay solapamiento: extiendo el final
} else {
mergedRanges.add(current); // No hay solapamiento: guardo y sigo
current = next;
}

### Defensa Teórica (KISS)

Elegí un bucle for porque este algoritmo requiere mantener y mutar un **estado** entre iteraciones (la variable current). Intentar hacer esto con Streams habría resultado en un código complejo y difícil de leer. Apliqué el principio **KISS (Keep It Simple)**: un bucle tradicional es la herramienta más eficiente (O(N)) y legible para este caso específico.

