---
title: "Advent of Code - Día 7: El Manifold de Taciones"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 7: Manifold de Taciones (Programación Dinámica y State Tracking)

## Descripción del Problema
En el Día 7 nos enfrentamos a una matriz que representa el flujo de partículas a través de una cuadrícula. En la **Parte A**, debemos identificar cuántas veces un rayo de partículas se divide al impactar con un "splitter" (`^`). En la **Parte B**, el reto es calcular la cantidad total de posibles líneas temporales (caminos) que resultan de todas las divisiones. El desafío principal aquí no es solo la lógica de propagación, sino la **eficiencia**: procesar una cuadrícula completa celda por celda es extremadamente lento y consume memoria innecesaria.

---

## 1. Patrones de Diseño

### Alta Cohesión Semántica (Enums)
* **Dónde está en el código:** Se ha encapsulado la semántica de la cuadrícula en el `Enum TachyonCell`.
* **Justificación:** En lugar de esparcir caracteres mágicos como `'^'` o `'S'` por toda la lógica de negocio (lo cual es un riesgo grave si el símbolo cambia), usamos una representación de dominio robusta. Esto centraliza la definición de los elementos de la cuadrícula y facilita el mantenimiento.

---

## 2. Inyecciones y Acoplamiento

### Bajo Acoplamiento mediante Abstracción de Acceso
* **Dónde está en el código:** La clase `Manifold` centraliza el acceso a la matriz y el cálculo de límites.
* **Resultado:** Los simuladores (`TachyonSimulator` y `QuantumTachyonSimulator`) no acceden a la matriz de caracteres crudos ni gestionan las excepciones de límites (índices negativos o fuera de rango). Dependen del contrato proporcionado por `Manifold`, lo que permite cambiar la estructura de almacenamiento interna de la cuadrícula en el futuro sin que los simuladores tengan que cambiar ni una sola línea de código.

---

## 3. Principios de Diseño

### Principio DRY (Don't Repeat Yourself)
* **Dónde está en el código:** Hemos unificado la representación del mapa en la clase compartida `Manifold` en el paquete general. La Parte A y la Parte B consumen esta misma entidad. Al no duplicar la lógica de lectura y gestión de coordenadas, evitamos la fragmentación del conocimiento y aseguramos que cualquier mejora en la lectura de datos beneficie a ambos simuladores.

---

## 4. Normas, Leyes y Fundamentos

### Algoritmo: Programación Dinámica y *State Tracking* (Eficiencia $O(N)$)
A diferencia de los enfoques de fuerza bruta que recorren toda la matriz, hemos implementado una técnica de seguimiento de estado.
* **Dónde está en el código:** 1. **Parte A:** Usamos un `HashSet` para rastrear solo las columnas donde existe un rayo activo. Si una fila de 1000 celdas solo tiene 2 rayos, nuestro algoritmo solo realiza 2 operaciones, ignorando el resto de la matriz vacía.
  2. **Parte B:** Usamos un `HashMap` para realizar el seguimiento del número de caminos activos (`pathCount`). El método `Map.merge()` permite acumular rutas de forma exponencialmente eficiente, evitando recursiones innecesarias.
* **Resultado:** El coste de procesamiento es proporcional al número de eventos (rayos activos) en lugar de al tamaño total de la matriz, reduciendo la complejidad temporal de $O(filas \times columnas)$ a $O(filas \times eventos\_activos)$.



### Fundamento: Código Expresivo
* **Dónde está en el código:** Se ha eliminado la "Obsesión por los Primitivos" al sustituir los caracteres del mapa por el tipo `TachyonCell`. Ahora el código se lee como un flujo de negocio: `if (cell == TachyonCell.SPLITTER) { ... }`. Esto reduce drásticamente la carga cognitiva de quien deba leer este código en el futuro.