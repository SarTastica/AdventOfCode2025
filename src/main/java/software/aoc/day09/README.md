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

### El Desafío: Contención Geométrica
El requisito cambia drásticamente: el rectángulo debe estar contenido estrictamente dentro del perímetro formado por las baldosas rojas. Ya no es solo un cálculo de área, es un problema de topología.

### Estrategia: Reutilización de `java.awt.Polygon`
En lugar de implementar algoritmos manuales complejos (como Ray Casting) que son propensos a errores de "corner cases", decidí aprovechar la potencia de la librería estándar de Java.

### Construcción del Polígono
   Utilizo la clase java.awt.Polygon del JDK para modelar el cine.

Código:
```
Polygon boundary = new Polygon();
for (Tile t : tiles) {
boundary.addPoint(t.x, t.y);
}
```
>Defensa (Don't Reinvent the Wheel): Aplico el principio de ingeniería de reutilización. La clase Polygon ya está altamente optimizada y probada por los ingenieros de Oracle para gestionar vértices y contornos complejos. Escribir esto desde cero sería ineficiente y arriesgado.

### Validación con "Epsilon Shrinking" (La Lógica del Núcleo)
   Este es el punto crítico de la solución. El método nativo `boundary.contains` sigue reglas de rasterizado de gráficos (píxeles), donde los bordes derechos e inferiores suelen excluirse para evitar dibujar doble. Esto causaba falsos negativos en nuestro problema de lógica discreta.

**El Desafío**: Un rectángulo válido que comparte pared con el cine devolvía false.

**La Solución (Ingeniería)**: Implementé una técnica de reducción infinitesimal ("Epsilon Shrinking").

Código:
```
// Preguntamos por un rectángulo virtual encogido un 1% hacia adentro
if (boundary.contains(minX + 0.01, minY + 0.01, width - 0.02, height - 0.02)) {
// ...
}
```
Justificación Técnica:

1. Premisa: Sabemos que las 4 esquinas del rectángulo son válidas a priori porque se construyen iterando sobre la lista de baldosas (tiles.get(i)).

2. Riesgo: El único riesgo real es que el rectángulo atraviese una zona vacía (un hueco) en el centro.

3. Corrección: Al validar un área infinitesimalmente más pequeña (+0.01), evitamos los problemas de exclusión de bordes de la librería AWT. Si el "núcleo" del rectángulo está dentro, y las esquinas son válidas, entonces la figura completa es válida.

### Filtrado de Casos Degenerados
   Antes de realizar la validación costosa, descarto figuras que no forman un área.

Código:
```
if (width == 0 || height == 0) continue;
Defensa: Esto actúa como una cláusula de guarda (Guard Clause). Evita procesar líneas planas (rectángulos con área 0), ahorrando ciclos de CPU y evitando comportamientos indefinidos en la lógica de contención.
```