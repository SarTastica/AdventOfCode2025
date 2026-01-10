# DÍA 4: Departamento de Impresión (Printing Department)

## 1. Contexto del Problema

Nos enfrentamos a un problema de optimización espacial en una matriz bidimensional (un almacén de papel).

* **Entrada:** Recibimos una `List<String>`, donde cada String es una fila del mapa.
* **Regla de Negocio:** Un rollo de papel (`@`) es "accesible" (se puede sacar) solo si tiene **menos de 4 vecinos** ocupados en sus 8 posiciones adyacentes.
* **Parte A:** Contar cuántos rollos son accesibles en el estado inicial.
* **Parte B (Simulación):** Simular la retirada continua. Al quitar un rollo, los vecinos que antes estaban bloqueados podrían liberarse. Debemos repetir el proceso hasta que ningún rollo más pueda ser retirado.

---

## 2. Arquitectura: Cohesión y Estructuras de Datos

He encapsulado toda la lógica en la clase `Warehouse`.

**Transformación de Datos (Input):**
Aunque recibo una `List<String>`, en el constructor la transformo inmediatamente a una matriz de caracteres `char[][]`.

**Justificación Técnica:**
1.  **Mutabilidad:** En la Parte B necesito borrar rollos (cambiar `@` por `.`). Los Strings en Java son inmutables; la matriz de `char` me permite modificaciones directas.
2.  **Rendimiento:** El acceso a `grid[r][c]` es **O(1)** y mucho más rápido que `list.get(r).charAt(c)`.

---

## 3. Implementación Técnica (Parte A)

### Patrón de Vectores de Dirección (DX/DY)
Para verificar los 8 vecinos, en lugar de escribir 8 condicionales `if` manuales (lo que aumentaría la Complejidad Ciclomática y el riesgo de errores), utilicé **Arrays de Desplazamiento**:

**Código (Definición):**
```
private static final int[] DX = {-1, -1, -1,  0, 0,  1, 1, 1};
private static final int[] DY = {-1,  0,  1, -1, 1, -1, 0, 1};
```
**Código (Uso en isAccessible):**
```
for (int i = 0; i < 8; i++) {
int newRow = r + DX[i];
int newCol = c + DY[i];
// Validar límites y verificar contenido
}
```
**Defensa (DRY):**
Esto reduce drásticamente el código repetitivo. Convierte la lógica espacial en datos iterables, haciendo el algoritmo más limpio y mantenible.

### Abstracción de Límites

Delegué la comprobación de índices (`r >= 0 && r < rows...`) al método `isValidPosition`. Esto limpia el bucle principal y evita excepciones `IndexOutOfBounds` de forma centralizada.

---
## 4. Evolución a la Parte B: Simulación por Generaciones

El requisito cambió de un conteo estático a una simulación dinámica (similar al "Juego de la Vida").

### El Desafío: Efectos Colaterales (Side Effects)
No podemos eliminar los rollos *mientras* iteramos el tablero en el mismo bucle.
* **Problema:** Si borro el rollo en la posición (0,0), el rollo en (0,1) vería un vecino menos inmediatamente. Esto alteraría el resultado de la simulación dentro de la misma "generación", produciendo datos incorrectos.

### Solución: Actualización en Dos Fases (Batch Update)
Implementé un bucle `do-while` que separa la **lectura** de la **escritura**:

1.  **Fase de Análisis:** Recorro la matriz (solo lectura) y guardo las coordenadas de los rollos a eliminar en una lista temporal `toRemove`.
2.  **Fase de Ejecución:** Una vez analizado todo el tablero, recorro la lista `toRemove` y aplico los cambios (`grid[r][c] = '.'`).

**Código de la Estrategia:**
```
do {
changesMade = false;
// 1. Identificar (Lectura)
if (isAccessible(r, c)) { toRemove.add(new int[]{r, c}); }

    // 2. Ejecutar (Escritura)
    for (int[] pos : toRemove) {
        grid[pos[0]][pos[1]] = '.';
        changesMade = true;
    }
} while (changesMade);
```

### Estructuras de Datos (KISS)
Para la lista temporal usé `List<int[]>`.
> **Defensa:** Podría haber creado una clase `Point`, pero dado que estas coordenadas son objetos efímeros que solo viven dentro del bucle `do-while`, un array de primitivos es la solución más eficiente y directa (KISS), evitando la sobre-ingeniería.
---
***

## 5. Conclusión

He transformado un problema de índices en una solución robusta y mantenible.

* **Legibilidad:** El uso de vectores `DX/DY` elimina el "código espagueti" de los condicionales direccionales.
* **Corrección:** La estrategia de actualización en dos fases en la Parte B garantiza que la simulación sea determinista y correcta, evitando condiciones de carrera lógicas al modificar el estado del tablero.