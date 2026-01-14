# DÍA 6: Trash Compactor (Math Worksheets)

## 1. Explicación del Enunciado

Nos enfrentamos a una hoja de ejercicios alienígena donde el desafío no es la complejidad matemática, sino el **formato visual** de los datos.

**La Entrada:**
Un archivo de texto que representa "hojas de cálculo" donde los problemas matemáticos no están línea por línea, sino distribuidos espacialmente.

* **Formato General:** Los problemas están agrupados en bloques visuales separados por **columnas verticales vacías** (espacios en blanco).
* **Parte A (Horizontal):** Debemos leer los problemas de izquierda a derecha. Los números y operadores se extraen cortando el texto horizontalmente.
* **Parte B (Vertical):** Descubrimos que la lectura correcta es de arriba a abajo. Los números se forman leyendo los dígitos verticalmente en cada columna, lo que complica el parseo debido a que las líneas de texto tienen longitudes irregulares ("ragged arrays").

---

## 2. Arquitectura: Polimorfismo y SRP

El diseño se basa en separar la **Lógica Espacial** (dónde está el problema) de la **Lógica Matemática** (cómo se resuelve).

### Responsabilidad Única (SRP)
Dividí el problema en dos métodos especialistas:
1.  **calculateGrandTotal (El Escáner):** Solo sabe de coordenadas y espacios vacíos. Detecta bloques.
2.  **solveProblem (El Intérprete):** Solo sabe de números y operadores. Procesa un bloque aislado.

### Enum con Comportamiento (Polimorfismo)
En lugar de usar condicionales (`if/switch`) dispersos para calcular, encapsulé la lógica en un `enum Operation`.

**Código del Enum:**

```private enum Operation {
ADD { 
@Override 
long apply(long a, long b) { return a + b; } },

MULTIPLY { 
@Override 
long apply(long a, long b) { return a * b; } };

abstract long apply(long a, long b);
}
```

> **Defensa (OCP):** Esto respeta el principio **Open/Closed**. Si mañana añaden divisiones, solo añado una constante al Enum sin tocar el algoritmo de resolución.


---

## 3. Parte A: Escaneo Visual y Parsing

### Algoritmo de Barrido
Identificar dónde empieza y termina un problema en un flujo de texto continuo.

**Estrategia:**
Utilizo una lista temporal (`currentBlockCols`) que actúa como **buffer**.
1.  Si la columna tiene datos, se añade al buffer
```
} else {
    currentBlockCols.add(col); // Aquí se añade al buffer si NO está vacía
}
```
2.  Si la columna está vacía (separador) **Y** el buffer tiene datos, disparamos la resolución (`solveProblem`) y limpiamos el buffer.

**Código de Control:**
```
if (isEmptyColumn(col, lines)) {
    if (!currentBlockCols.isEmpty()) {
        grandTotal += solveProblem(currentBlockCols, lines);
        currentBlockCols.clear();
    }
```
### Abstracción
El archivo de entrada las líneas tienen distintas longitudes, por lo que acceder a `line.charAt(col)` sin precauciones lanzaría una `StringIndexOutOfBoundsException`.

Para mitigar esto, encapsulé la lógica de acceso seguro en `isEmptyColumn`.

Para una mayor robustez, se verifica `col < line.length()` antes de leer permitiendo que el algoritmo principal trate la entrada como una matriz rectangular perfecta, delegando la complejidad de los límites a este método auxiliar.

### Estrategia de Parsing 
Dentro de `solveProblem`, extraigo el "segmento" de texto correspondiente al bloque detectado.
Para distinguir entre números y operadores (`*`, `+`) en un entorno visualmente sucio, utilizo una estrategia de **Fallback**.

**Lógica de Decisión:**
1.  Se extrae y limpia el segmento (`trim()`).
2.  Se intenta parsear como `Long`.
3.  Si falla (bloque `catch` o validación), se asume que es una `Operation` (Enum).

> **Justificación:** Dado que la alineación visual puede variar, esta estrategia es resiliente. No dependemos de posiciones fijas, sino del **tipo de dato** contenido en el segmento para decidir el comportamiento.

---

## 4. Evolución a la Parte B: Lectura Vertical

El requisito cambió a leer los números verticalmente. El problema técnico es que el archivo de texto no es un rectángulo perfecto; las líneas tienen **longitudes variables**.

Si intentamos acceder a la columna 10 en una fila que solo tiene 5 caracteres (fila corta), Java lanzará una `StringIndexOutOfBoundsException`. No podemos asumir una matriz cuadrada.

### Solución: Diseño Defensivo (`getSafeChar`)
Para evitar llenar el código principal de `if-else` para comprobar longitudes, implementé el método `getSafeChar`.

**Código de Seguridad:**
```
private char getSafeChar(String line, int col) {
    if (col < line.length()) {
        return line.charAt(col);
    }
    return ' '; // Valor por defecto seguro (Null Object Pattern visual)
}
```

> **Defensa:** Este método normaliza la entrada virtualmente. El algoritmo principal puede "fingir" que la matriz es un rectángulo perfecto lleno de espacios, simplificando drásticamente la lógica de los bucles y eliminando el riesgo de excepciones.

### Eficiencia: Construcción Mutable
Para leer los números, debemos bajar fila por fila en una misma columna.

```
**Construcción Vertical:**

StringBuilder numBuilder = new StringBuilder();
for (int row = 0; row < lastRowIndex; row++) { 
    char c = getSafeChar(lines.get(row), col);
    if (Character.isDigit(c)) numBuilder.append(c);
}
```
> **Defensa Técnica:** Utilicé `StringBuilder` en lugar de la concatenación normal (`s += c`).
> En Java, los Strings son **inmutables**. Concatenar en un bucle crea un nuevo objeto String en memoria por cada carácter, lo que tendría una complejidad **O(N²)**. `StringBuilder` es mutable, permitiendo una construcción eficiente **O(N)**.
