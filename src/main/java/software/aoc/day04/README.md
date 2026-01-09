# DÍA 4: (Printing Department)

## 1. Contexto del Problema
Nos enfrentamos a un problema de optimización espacial en una matriz bidimensional (el almacén). Un rollo es accesible 
solo si tiene menos de 4 vecinos ocupados en sus 8 posiciones adyacentes
* **Entrada:** `List<String>`
* **Parte A:** Identificar qué rollos de papel (`@`) son accesibles según reglas de vecindad (menos de 4 vecinos ocupados).
* **Parte B (Simulación):** Simular la retirada continua de rollos. Al quitar uno, los vecinos cambian de estado, pudiendo volverse accesibles en la siguiente ronda (tipo "Autómata Celular").

---

## 2. Arquitectura: Cohesión y SRP
He creado una única clase `Warehouse` basándome en el principio de **Alta Cohesión**.

* **Responsabilidad Única (SRP):** La matriz `grid` y la lógica de vecindad están íntimamente ligadas.
* **Encapsulamiento:** Si hubiera sacado la lógica a una clase externa, `Warehouse` tendría que exponer su estructura interna, rompiendo el encapsulamiento. `Warehouse` es el único responsable de conocer y mutar el estado del almacén.

---

## 3. Implementación Técnica (Parte A)

### Estructuras de Datos y DRY
Uso de **Arrays de Desplazamiento (`DX`, `DY`)** para la lógica de vecindad:
```java
private static final int[] DX = {-1, -1, -1,  0, 0,  1, 1, 1};
private static final int[] DY = {-1,  0,  1, -1, 1, -1, 0, 1};
````

Esto me permite usar un bucle simple `for (int i = 0; i < 8; i++)` en el metodo `isAccessible`

**Defensa (DRY):** Esto reduce la **Complejidad Ciclomática** y evita errores de "copia y pega", haciendo el código más mantenible.



### Abstracción y Métodos Auxiliares
He encapsulado la validación de límites en `isValidPosition(r, c)`.
* **Beneficio:** Evita ensuciar la lógica principal con comprobaciones de índices (`r >= 0 && r < rows...`).

### Eficiencia
Uso de `char[][]` (primitivos).
* **Justificación:** Acceso aleatorio **$O(1)$** y menor consumo de memoria que una `List<List<Character>>`.

---

## 4. Evolución a la Parte B: Simulación por Generaciones

El requisito cambió de una lectura estática a una **simulación dinámica iterativa**.

### El Desafío: Actualización Síncrona (Side Effects)
No se pueden eliminar los rollos *mientras* se itera el tablero, porque borrar un rollo afecta al conteo de vecinos de la siguiente celda en la misma iteración.

### Solución Algorítmica (Batch Update)
Implementé un bucle `do-while` con una estrategia de dos fases:
1.  **Fase de Análisis:** Recorro todo el almacén e identifico los candidatos en una lista temporal `toRemove`.
2.  **Fase de Actualización:** Solo *después* de revisar todo el tablero, aplico los cambios en masa.

```java
do {
    // 1. Identificar candidatos (sin modificar grid)
    // 2. Aplicar cambios (grid[r][c] = '.')
} while (changesMade);
````

### Estructuras de Datos (KISS / YAGNI)
Para la lista temporal usé `List<int[]>`.

> **Defensa (KISS):** Podría haber creado una clase `Point` o `Coordinate`, pero al ser una variable local de vida corta (scope limitado al bucle), un array de primitivos es la solución más simple y directa sin sobre-ingeniería.

### Diseño por Contrato
El método público `removeAllAccessibleRolls()` oculta toda esta complejidad mecánica. El resto de la aplicación solo sabe que "se eliminan los rollos", sin conocer los detalles de la simulación.

---

## 5. Conclusión
He transformado un problema de índices en una solución robusta.

* Simplifiqué la lógica espacial con **Vectores de Dirección**.
* Implementé una **Simulación Segura** (evitando condiciones de carrera lógicas) mediante actualizaciones por lotes, manteniendo un encapsulamiento estricto.