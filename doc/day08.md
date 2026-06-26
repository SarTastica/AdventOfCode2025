---
title: "Advent of Code - Día 8: Redes de Conexión"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 8: Redes de Conexión (Grafos y DSU)

## Descripción del Problema
El desafío consiste en conectar puntos 3D minimizando las distancias entre ellos para formar circuitos (grafos conexos). La **Parte A** pide calcular el producto de los tamaños de los 3 circuitos más grandes una vez realizadas un número fijo de conexiones. La **Parte B** solicita identificar el producto de las coordenadas X de los dos puntos cuya conexión finaliza la unificación de todo el sistema en un único circuito total.

---

## 1. Patrones de Diseño

### Implementación del Algoritmo Disjoint Set Union (DSU / Union-Find)
Hemos descartado el enfoque ingenuo basado en `Set<Set<T>>` por ser ineficiente y presentar una complejidad algorítmica elevada. En su lugar, hemos implementado una estructura **DSU** que gestiona la conectividad de grafos de forma nativa.



* **Optimización 1 (Path Compression):** En el método `find(int i)`, aplanamos la estructura del árbol para que las futuras consultas sean prácticamente instantáneas.
* **Optimización 2 (Union by Size):** Al unir conjuntos, siempre añadimos el árbol más pequeño al más grande, manteniendo el árbol equilibrado y evitando el peor escenario de complejidad.

---

## 2. Inyecciones y Acoplamiento

### Separación de Responsabilidades (SRP)
* **JunctionParser:** Responsable exclusivo de la transformación de texto a entidades del dominio.
* **GraphBuilder:** Centraliza la lógica de generación de aristas y ordenamiento por distancia, aplicando el **Principio DRY**. Esto evita que los optimizadores de la Parte A y B dupliquen cálculos.
* **CircuitUnionFind:** Encapsula la lógica pura de grafos, totalmente desacoplada de la semántica de "puntos 3D".

---

## 3. Principios de Diseño

### Principio DRY (Don't Repeat Yourself)
* **Dónde está en el código:** La lógica de cálculo de distancias y ordenamiento de conexiones es común a ambas partes. Al extraerla en una clase utilitaria, cualquier mejora o corrección de errores en el motor de grafos se aplica automáticamente a todo el proyecto.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Inmutabilidad (Records)
El uso de `Point3D` y `Connection` como `records` inmutables asegura que, una vez que un punto se carga en memoria, sus coordenadas son inalterables. Esto previene errores de estado inconsistente durante las iteraciones del algoritmo DSU, donde la integridad de los datos es crítica.
