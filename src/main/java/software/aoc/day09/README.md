# DÍA 9: Cine (Movie Theater)

## 1. Explicación del Problema

El desafío de hoy nos sitúa en el cine del Polo Norte, donde debemos ayudar a los Elfos a optimizar la disposición de las baldosas del suelo.

* **La Entrada:**
  Una lista de coordenadas `x,y` (ej: `7,1`, `11,1`) que representan la ubicación de baldosas rojas.

* **Parte A:**
  Ignorando lo que hay entre ellas, debemos elegir dos baldosas rojas cualesquiera como vértices opuestos y calcular el área del rectángulo más grande posible que se pueda formar. Es un problema de **maximización simple**.

* **Parte B:**
  Se introduce una restricción topológica: las baldosas rojas forman el perímetro de un polígono cerrado. El rectángulo candidato debe estar formado exclusivamente por baldosas rojas o verdes (que están dentro del perímetro o en los bordes).
  Matemáticamente, esto se traduce en encontrar el rectángulo más grande **estrictamente contenido** dentro de un polígono irregular.


***

## 2. Arquitectura y Estructura

El objetivo es encontrar el rectángulo de mayor área posible dados dos puntos cualesquiera.

He modelado el problema utilizando una **Clase Interna Estática** para representar los datos.

**Clase `Tile` (Value Object):**
```
private static class Tile {
final int x;
final int y;
// ... constructor con parseo
}
```
* **Inmutabilidad:** Los campos `x` e `y` son `final`. Una coordenada no debe cambiar una vez creada. 
* **Static Inner Class:** La definí como estática porque una "Baldosa" es una entidad de datos pura independiente de la lógica del cine (`MovieTheater`). Esto optimiza memoria (no guarda referencia a la clase externa) y reduce el acoplamiento.
* **Alta Cohesión:** La lógica de parseo (`split`, `parseInt`) vive dentro del constructor de `Tile`, liberando a la clase principal de detalles de bajo nivel.

***

## 3. Parte A: Fuerza Bruta y Geometría Discreta

### Método findLargestRectangleArea

1. Transformación de Datos (Streams):
   Utilicé un pipeline de Streams (filter -> map -> collect) para limpiar la entrada y convertirla en objetos Tile.
   Esto separa la "preparación de datos" del "procesamiento".

2. Cálculo de Área Inclusiva (Geometría Discreta):

```
long width = Math.abs(t1.x - t2.x) + 1;
long height = Math.abs(t1.y - t2.y) + 1;
```

***


## 4. Evolución a la Parte B: Geometría Computacional

### 1. Modelado del Problema (Polygon API)

En la Parte B, el problema se convierte en un reto de **Topología**: verificar si un rectángulo está estrictamente contenido dentro de un perímetro irregular. Utilizo la clase java.awt.Polygon del JDK, que ya contiene algoritmos de intersección altamente optimizados y probados.

**Código:**
```
java.awt.Polygon boundary = new java.awt.Polygon();
for (Tile t : tiles) {
    boundary.addPoint(t.x, t.y);
}
```

### 2. Estrategia de Búsqueda y Optimización

Para encontrar la solución, itero sobre todos los pares posibles de baldosas usándolas como esquinas opuestas del rectángulo candidato.

Código (Cláusula de Guarda):
```
int width = Math.abs(t1.x - t2.x);
int height = Math.abs(t1.y - t2.y);
// Descartar líneas planas inmediatamente
if (width == 0 || height == 0) continue;
```

> Defensa (Eficiencia):Antes de realizar cualquier cálculo geométrico costoso, aplico una Cláusula de Guarda. Descarto inmediatamente figuras con dimensiones nulas, ahorrando ciclos de CPU valiosos y evitando comprobaciones innecesarias.

### 3. El Núcleo Lógico: Validación con "Epsilon Shrinking"

Las librerías gráficas suelen excluir los bordes exactos. Si mi rectángulo toca la pared, el método nativo `contains` devolvería false incorrectamente.
Código:
```
// Contracción infinitesimal (0.01) para evitar falsos negativos en bordes
double epsilon = 0.01;
if (boundary.contains(minX + epsilon, minY + epsilon, 
                      width - 2*epsilon, height - 2*epsilon)) {
    // Es válido
}
```

> Defensa Técnica (Robustez Matemática): Implementé una técnica de Contracción de Límites. Al encoger el rectángulo virtualmente un margen de seguridad (epsilon = 0.01), alejo los bordes de la "zona de conflicto". Si el "núcleo" del rectángulo contraído está dentro, garantizamos matemáticamente que la figura entera es válida, solucionando la limitación de la API gráfica.

