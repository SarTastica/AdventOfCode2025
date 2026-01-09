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

### Responsabilidad Única (SRP)
Dividí el problema en dos métodos especialistas:
1.  **calculateGrandTotal (El Escáner):** Solo sabe de coordenadas y espacios vacíos. Detecta bloques.
2.  **solveProblem (El Intérprete):** Solo sabe de números y operadores. Procesa un bloque aislado.

---

## 3. Parte A: Escaneo Visual

### Detección de Bloques
El reto principal es identificar dónde empieza y termina un problema. Utilicé una estrategia de "barrido" columna por columna.

**Código de Detección:**
```
if (isEmptyColumn(col, lines)) {
// Si la columna está vacía, procesamos el bloque acumulado
grandTotal += solveProblem(currentBlockCols, lines);
currentBlockCols.clear();
}
```
### Abstracción (Lectura Expresiva)
Creé el método auxiliar `isEmptyColumn`.
* **Beneficio:** Oculta los detalles sucios de iterar caracteres y comprobar límites. El código principal se lee como lenguaje natural: "Si es columna vacía, resuelve".

### Robustez (Parsing)
Dado que el formato es visual, distinguir un operador (`*`) de un dato inválido es crítico. Utilizo un bloque `try-catch` al intentar parsear números (`Long.parseLong`). Si falla, asumo que es el operador. Esto hace al sistema resiliente a pequeñas variaciones de alineación.

---

## 4. Evolución a la Parte B: Lectura Vertical

### El Desafío: Arrays Irregulares
El requisito cambió a leer los números verticalmente. El problema técnico es que el archivo de texto no es un rectángulo perfecto; las líneas tienen **longitudes variables**.
Acceder a la columna 10 en una línea de longitud 5 provocaría una `StringIndexOutOfBoundsException`.

### Solución: Diseño Defensivo (`getSafeChar`)
Implementé un método que actúa como barrera de seguridad.

**Código de Seguridad:**

private char getSafeChar(String line, int col) {
if (col < line.length()) {
return line.charAt(col);
}
return ' '; // Valor por defecto seguro (Null Object Pattern visual)
}

> **Defensa:** Este método normaliza la entrada. El resto del algoritmo puede "fingir" que la matriz es rectangular, simplificando drásticamente la lógica de los bucles.

### Eficiencia: StringBuilder
Para construir los números verticalmente, iteramos por filas.

**Construcción Vertical:**
```
StringBuilder numBuilder = new StringBuilder();
for (int row = 0; row < lastRowIndex; row++) { ... }
```
Usé `StringBuilder` en lugar de concatenación de Strings (`+`) porque estamos modificando el valor repetidamente dentro de un bucle. Esto reduce la complejidad de memoria y tiempo de ejecución.