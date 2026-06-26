---
title: "Advent of Code - Día 11: Flujo de Datos en el Reactor"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# ⚛Día 11: Flujo de Datos en el Reactor (Grafos y Memoización)

## Descripción del Problema
En el Día 11, el desafío consiste en modelar una red de dispositivos en un reactor y analizar el flujo de datos. Representado como un grafo dirigido, el objetivo de la **Parte A** es contar el número total de caminos posibles desde un nodo de inicio hasta un nodo de salida. En la **Parte B**, se añade una restricción crítica: solo son válidos aquellos caminos que atraviesen obligatoriamente dos nodos específicos (DAC y FFT). El principal obstáculo de este problema es la explosión combinatoria de caminos, lo que hace que un enfoque de fuerza bruta escale a un tiempo de ejecución exponencial.

---

## 1. Patrones de Diseño

### Patrón Strategy
* **Dónde está en el código:** La interfaz `PathCounterStrategy` y sus implementaciones concretas `MemoizedDfsPathCounter` y `MandatoryNodesPathCounter`.
* **Justificación:** Aisla completamente los algoritmos de travesía de grafos. Esto permite al orquestador cambiar las reglas de conteo de caminos de manera dinámica sin tener que reescribir la lógica de gestión del reactor.

### Patrón Factory (Especialización de Creación)
* **Dónde está en el código:** La clase `NetworkParser`.
* **Justificación:** Encapsula toda la lógica compleja de creación y validación. Transforma un input en crudo (cadenas de texto) en una entidad de dominio pura (`NetworkGraph`), centralizando el manejo de expresiones regulares y divisiones de cadenas fuera de las clases de negocio.

### Patrón Composite Key (Clave Compuesta)
* **Dónde está en el código:** En la Parte B, mediante la entidad `record State(String node, boolean seenReq1, boolean seenReq2)`.
* **Justificación:** Evita el antipatrón *String Typing* (concatenar valores en un String para usarlos como clave en un diccionario). El `record` genera internamente algoritmos `hashCode()` y `equals()` exactos y eficientes en memoria, creando una clave compuesta perfecta para el motor de memoización.
---

## 2. Inyecciones y Acoplamiento

### Inyección de Dependencias por Constructor
* **Dónde está en el código:** La clase `ReactorManager` recibe la abstracción `PathCounterStrategy` a través de su constructor.
* **Justificación:** El manager no es responsable de decidir cómo se cuentan los caminos. Al inyectar la dependencia, el sistema se vuelve altamente testeable, permitiendo la introducción de estrategias falsas (*mocks*) durante las pruebas unitarias.

### Bajo Acoplamiento mediante Abstracción de Datos
* **Dónde está en el código:** La creación de la clase `NetworkGraph` y su separación de `NetworkParser`.
* **Justificación:** A diferencia de diseños monolíticos (God Classes) donde una sola clase parsea texto, guarda los datos y ejecuta el algoritmo, aquí el `NetworkParser` traduce texto puro en una estructura matemática abstracta (`NetworkGraph`). Los algoritmos operan exclusivamente sobre el grafo, ignorando el origen de los datos.

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
* **Dónde está en el código:** La arquitectura se divide en capas con una sola razón para cambiar:
  1. `NetworkParser`: Cambia solo si el formato del `.txt` cambia.
  2. `NetworkGraph`: Cambia solo si la representación matemática del grafo (ej. cambiar lista de adyacencia por matriz) cambia.
  3. `PathCounterStrategy`: Cambia solo si las reglas del puzzle cambian.

### Principio Abierto/Cerrado (OCP)
* **Dónde está en el código:** El `ReactorManager`.
* **Justificación:** Está cerrado a modificaciones (su código no se toca si hay nuevas reglas en una hipotética Parte C), pero está abierto a extensiones mediante la inyección de nuevas implementaciones de la estrategia.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Inmutabilidad Defensiva
* **Dónde está en el código:** En el constructor de `NetworkGraph`, se utiliza `Map.copyOf(adjacencyList)` y, en el parser, `List.of(...)`.
* **Justificación:** Una vez construido el grafo, su estructura topológica no puede ser alterada de forma accidental o maliciosa por otras clases. Esto es una garantía crítica de seguridad de los datos.

### Fundamento: Estrategias Puras (Stateless)
* **Dónde está en el código:** Las clases de estrategia no tienen el diccionario de estado (`Map<String, Long> memo`) como atributo de la clase, sino que se instancia en el método público y se pasa de forma recursiva como argumento en los métodos privados.
* **Justificación:** Esto convierte al algoritmo en una función pura. Al no tener un estado mutable compartido, la estrategia es 100% segura para concurrencia (*Thread-Safe*).
