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

El problema pasa de ser un simple conteo a un problema de **topología**: debemos encontrar el rectángulo de mayor área que pueda inscribirse dentro de un perímetro irregular (las baldosas rojas). Esto implica validar si una figura geométrica está **estrictamente contenida** dentro de otra.

### Estrategia: Reutilización de Librerías Estándar (`java.awt`)

En lugar de implementar algoritmos geométricos complejo, opté por utilizar la robustez de la API gráfica de Java.

**Código:**
```
Polygon boundary = new Polygon(xPoints, yPoints, nPoints);
```

Defensa (Fiabilidad y Eficiencia):
Aplico el principio de "No Reinventar la Rueda". La clase `java.awt.Polygon` implementa internamente algoritmos de intersección altamente optimizados.

### El Problema Crítico: Las Reglas de Rasterizado

Al usar `boundary.contains(rect)`, nos enfrentamos a una limitación técnica de la librería: Las reglas de inclusión de bordes. Para evitar que dos polígonos adyacentes compartan los mismos píxeles en el borde, los métodos `contains` suelen considerar los bordes derechos e inferiores como "abiertos" (excluidos).

* Consecuencia: Si nuestro rectángulo válido toca exactamente la pared del cine (comparte coordenadas), el método nativo devuelve false (falso negativo), descartando una solución válida.

### La Solución Epsilon

Es un número muy pequeño que se utiliza como margen de seguridad para mitigar la limitación del rasterizado sin perder precisión lógica implementando una técnica de contracción de límites.

Código:
```
// Verificamos un rectángulo virtual contraído infinitesimalmente (epsilon = 0.01)
double epsilon = 0.01;
if (boundary.contains(minX + epsilon, minY + epsilon, width - 2*epsilon, height - 2*epsilon)) {
    // Es válido
}
```
Defensa Técnica:
* Al contraer el rectángulo un valor epsilon (0.01), alejamos los bordes de la "zona de conflicto" del límite matemático, llevándolos hacia el interior seguro de la figura. Si el rectángulo contraído está dentro, el rectángulo entero también lo está

### Optimización: Filtrado de Casos Degenerados

Antes de invocar la costosa lógica de geometría computacional, aplico una cláusula de guarda.

Código:
```
if (width <= 0 || height <= 0) continue;
```

> Defensa: Descarto inmediatamente figuras con dimensiones nulas o negativas.
