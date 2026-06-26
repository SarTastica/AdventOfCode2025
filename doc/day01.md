---
title: "Advent of Code - Día 1: La Caja Fuerte"
author: "Salwa Madani Lazaar"
date: "`r Sys.Date()`"
output: 
  html_document:
    toc: true
    theme: flatly
    highlight: tango
---

# Día 1: La Caja Fuerte (Inmutabilidad y Contextos Delimitados)

## Descripción del Problema
En el Día 1 nos enfrentamos a una caja fuerte con un dial giratorio. La **Parte A** requiere contar cuántas veces el dial termina exactamente en la posición cero *después* de completar un movimiento. Sin embargo, la **Parte B** cambia radicalmente la física del problema: ahora debemos contar cuántas veces el dial *cruza* la posición cero *durante* el movimiento. Debido a este cambio en las reglas de negocio, la arquitectura del código evolucionó de un diseño clásico orientado a objetos a un enfoque funcional e inmutable.

---

## 1. Patrones de Diseño

### De *Command* a *Value Object*
Inicialmente, el sistema fue diseñado utilizando el **Patrón Command** (con interfaces y clases como `RotateLeftCommand`). Sin embargo, al descubrir que las reglas de la Parte A y la Parte B diferían tanto, se decidió refactorizar.
Reemplazamos el patrón Command por el patrón **Value Object** (implementado a través de `records` en Java). En lugar de tener comandos que mutan el estado de una interfaz `CajaFuerte`, ahora tenemos objetos de valor inmutables (`Rotation`) que representan la pura intención del movimiento sin efectos secundarios.

---

## 2. Inyecciones y Acoplamiento

### Eliminación de Interfaces Compartidas y Bajo Acoplamiento
En una fase temprana, existía una carpeta `General` con una interfaz `CajaFuerte` compartida entre ambas partes. Al darnos cuenta de que la Parte A y la Parte B evaluaban el paso por cero de formas incompatibles, decidimos eliminar la interfaz compartida.
* **Bajo Acoplamiento:** Al eliminar la carpeta general, hemos desacoplado por completo la Parte A de la Parte B. Si los requisitos de la Parte B cambian mañana, el código de la Parte A ni siquiera se enterará, logrando un diseño de dependencias cero entre módulos.

---

## 3. Principios de Diseño

### Principio de Responsabilidad Única (SRP)
Cada módulo o clase debe tener una sola razón para cambiar.
* **Dónde está en el código:** Al igual que en días posteriores, se han separado los dominios en `software.aoc.day01.a` y `software.aoc.day01.b`. Si intentáramos usar una clase general `Dial` para ambas partes, tendríamos que llenarla de condicionales para saber qué regla aplicar, violando el SRP. Ahora, cada `Dial` tiene una única responsabilidad dictada por su propia carpeta.

### Principio YAGNI (You Aren't Gonna Need It)
* **Dónde está en el código:** Se eliminaron todas las clases de parseo complejas y estructuras de comandos. Se demostró que no íbamos a necesitar una arquitectura de comandos orientada a objetos para un problema que se resolvía de forma más segura mediante transformaciones matemáticas simples e inmutables.

---

## 4. Normas, Leyes y Fundamentos

### Fundamento: Inmutabilidad (El eje central de la refactorización)
Un objeto es inmutable cuando su estado no puede ser modificado una vez creado.
* **Dónde está en el código:** En lugar de utilizar variables mutables (`this.currentPosition = ...`) y métodos `void`, el record `Dial` es 100% inmutable. El método `execute(Rotation)` no altera el dial actual; en su lugar, calcula la nueva posición matemática y devuelve una instancia completamente nueva de `Dial`. Esto elimina los efectos secundarios y hace que el flujo de datos sea predecible y seguro.

### Fundamentos: Alta Cohesión y Código Expresivo
* **Dónde está en el código:** El uso de `records` de Java agrupa de forma natural y cohesiva la posición actual y los aciertos (`zeroHits`). Al eliminar las interfaces mutables, el código de test se vuelve altamente expresivo: `estadoFinal = estadoInicial.executeAll(orders);`. Se lee como una transformación pura (Entrada $\rightarrow$ Proceso $\rightarrow$ Salida), facilitando enormemente su lectura y mantenimiento.