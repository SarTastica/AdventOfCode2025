# DÍA 2: Tienda de Regalos (Gift Shop)

## 1. Explicación del Problema

El objetivo de este ejercicio es procesar una lista de inventario para detectar identificadores (IDs) con patrones numéricos específicos y sumar sus valores.

* **La Entrada:**
  Recibimos una lista de rangos numéricos en formato texto (ej: 11-22, 45-55).

* **Parte A:**
  Debemos encontrar los IDs "inválidos" que es una secuencia repetida **exactamente dos veces**.

* **Parte B:**
  La regla se generaliza. Ahora un ID es inválido si consiste en una secuencia repetida **al menos dos veces**.

---

***

## 2. Arquitectura y Estructura

Para este ejercicio, he distribuido la lógica basándome en la **Cohesión y la Abstracción**. He evitado tener un único método gigante y he aplicado el **Principio de Responsabilidad Única (SRP)** creando métodos auxiliares especializados:

1. **El Orquestador (calculateInvalidIdSum):** Es el método principal. No conoce los detalles de validación ni de parseo; solo define el flujo de datos.
2. **El Generador (rangeToStream):** Se encarga exclusivamente de transformar el texto X-Y en un flujo numérico usable.
3. **El Validador (isRepeatedSequence):** Encapsula la regla de negocio pura (la lógica matemática para saber si un número es un patrón repetido).

> **Beneficio:** Esta arquitectura desacopla la generación de datos de su validación. Si cambia el formato de entrada, solo toco el generador. Si cambia la regla de negocio, solo toco el validador.

---

***

## 3. Parte A: Streams y Optimización

Para la implementación técnica, he evitado los bucles for tradicionales y he optado por la **Programación Declarativa** usando la API de Streams.

**La Tubería (Pipeline):**
El flujo de datos sigue estos pasos: Split (dividir entrada) -> FlatMap (convertir a números) -> Filter (validar) -> Sum (acumular).

**Rendimiento Crítico (Streams Primitivos):**
He utilizado LongStream y flatMapToLong en lugar de Stream<Long>.

> **Defensa Técnica:** Esta decisión es vital para el rendimiento. Evita el **Autoboxing/Unboxing** (la conversión costosa entre el primitivo long y el objeto Long). Dado que iteramos sobre rangos que pueden contener millones de números, trabajar con tipos primitivos optimiza drásticamente el uso de memoria y CPU.

---

***

## 4. Evolución a la Parte B: Generalización de Patrones

En la Parte B, el requisito cambió de "mitades exactas" a "patrones periódicos" (N >= 2).

**Adaptación del Algoritmo:**
Refactoricé únicamente el método validador isRepeatedSequence utilizando una estrategia de búsqueda de patrones:
1. Itero sobre las posibles longitudes del patrón.
2. Compruebo la divisibilidad matemática (len % patternLen == 0).
3. Reconstruyo el ID esperado usando el método **String.repeat()**.

**Código Expresivo:**
El uso de String.repeat(repeats) es un ejemplo de código limpio y expresivo. En lugar de escribir un bucle manual para concatenar cadenas, utilizo la API estándar de Java, lo que hace que la intención del código sea clara al primer vistazo.

---

***

## 5. Conclusión

Lo más destacable de este ejercicio es la demostración de **Bajo Acoplamiento**.

Gracias a la arquitectura definida en el punto 2, la transición de la Parte A a la Parte B fue limpia. El método principal y la lógica de Streams permanecieron **idénticos**. El cambio en la regla de negocio (validación matemática) quedó aislado en el método validador, sin romper ni afectar a la infraestructura de procesamiento de datos.