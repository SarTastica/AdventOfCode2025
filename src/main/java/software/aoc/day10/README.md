# DÍA 10: Luces y Alto Voltaje (Lights Out)

## 1. Resumen del Enunciado

Te encuentras frente a una compleja máquina o panel de control de los Elfos que necesita ser configurado correctamente 
para funcionar. El panel tiene varias salidas y una serie de botones, pero el cableado es confuso: presionar un botón 
afecta a varias salidas a la vez. Tu tarea es descubrir la secuencia correcta de pulsaciones para dejar la máquina en el estado exacto que requieren los Elfos.

**Entrada:**
Una cadena de texto que contiene:
1.  El **Target**: El estado al que debemos llegar ( un patrón visual `[#.#]` y numérico `{10,2}`).
2.  Los **Botones**: Una lista de configuraciones (ej: `(0,1,5)`), indicando qué índices afecta cada botón.

**Parte A:**
Interpreta el sistema como **lógica binaria** (interruptores). Pulsar un botón invierte el estado de las luces.
Debemos encontrar el **mínimo número de pulsaciones** para replicar el patrón de luces objetivo partiendo de cero.

**Parte B:**
Interpreta el sistema como **lógica aritmética** (acumuladores). Pulsar un botón suma `+1` al voltaje de las salidas.
Pide calcular el **mínimo número de pulsaciones totales** necesarias para alcanzar valores de voltaje exactos (y muy grandes).

***


## 2. Arquitectura del Problema

La solución se estructura en dos capas para separar la orquestación de la lógica algorítmica:

1.  **`Factory` (Cliente):**
    * Utiliza **Java Streams** (`map`, `mapToInt`, `sum`) para procesar la lista de entradas de forma declarativa.

2.  **`Machine` (Dominio):**
    * Contiene el estado inmutable (`target`, `buttons`) parseado en el constructor.
    * Implementa el algoritmo de búsqueda (BFS) optimizado con estructuras de datos eficientes (`ArrayDeque`, `Bitmasking`).

***

## 3. Parte A: Parsing Manual y Optimización de Cola

Se encarga de traducir la entrada cruda de texto (formato humano) a estructuras de datos binarias optimizadas 
(formato máquina) para que el algoritmo pueda trabajar a máxima velocidad.

### 1. Método Machine
**Extracción del Objetivo (Target State)**
* La primera mitad del método busca lo que hay entre corchetes [...] (ej: [#..#]). 
* Convierte un patrón visual de caracteres en un solo número entero (long).
* Recorre el string. Si encuentra un # en la posición i, enciende el bit correspondiente usando `1L << i` y el operador `OR (|=)`.

Resultado: Transforma el texto "#..#" en el valor binario 1001 (o el decimal 9).

**Extracción de los Botones (Button Wiring)**
* Busca lo que hay en dentro de los paréntesis (...) (ej: (0,2)) y convertirlo en listas de índices numéricos en máscaras de bits.
* Corta el string manualmente usando substring e indexOf. 
* Separa los números por comas. 
* Para cada número (ej: 2), enciende ese bit en una máscara (1L << 2).

Resultado: Transforma el texto "(0, 2)" en una máscara binaria 101 (bits 0 y 2 encendidos) y la añade a la lista buttons.


### 2. Método solveMinPresses

Implementa el algoritmo BFS (Búsqueda en Anchura) para encontrar el camino más corto desde "todo apagado" hasta el estado objetivo.

* Utiliza una cola (ArrayDeque) para explorar los estados nivel por nivel y un mapa (HashMap) para registrar los visitados y evitar ciclos. 
* La transición de estados (pulsar botón) se realiza mediante la operación XOR (^), que invierte los bits (enciende/apaga luces) de forma instantánea y eficiente.

***

## 4. Parte B: Recursión y Descomposición Binaria

### 1. Método Machine (Constructor)
**Parsing y Pre-cálculoExtracción:**
* Convierte los Strings de entrada en Listas de Long (objetivos) y Integer (botones).
* Generación de Patrones: Calcula todas las combinaciones posibles de pulsar los botones una sola vez (2^N) utilizando un bucle de máscaras de bits.
* Almacena en un Mapa (patterns) qué "efecto" produce cada combinación y su coste mínimo. Esto evita recalcular combinaciones durante la recursión.

### 2. Método solve (Recursivo)
**Algoritmo: Divide y Vencerás**
* Memoización: Usa un mapa (memo) para guardar estados resueltos y podar ramas del árbol. 
* Lógica de Paridad: Busca un patrón pre-calculado tal que (objetivo - patrón) sea par y no negativo.
* Reducción de Estado: Si es válido, divide el residuo por 2 (diff / 2), resolviendo el problema bit a bit (de menor a mayor peso).
* Cálculo de Coste: Coste = Coste_Patrón + (2 * Coste_Recursivo). Multiplica por 2 porque el siguiente nivel recursivo afecta a una potencia binaria superior.