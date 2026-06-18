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

**Algoritmo**
* La Cola (queue): Es mi lista de tareas pendientes. Empiezo poniendo el estado inicial (el 0).
* El Bucle: Saco un número de la lista y miro: '¿A qué otros números puedo llegar desde aquí pulsando un botón?'.
* El Mapa (visited): Si encuentro un número nuevo, lo apunto y lo meto al final de la cola. Si ya lo había visto antes, lo ignoro para no dar vueltas en círculos.

***

## 4. Parte B: Recursión y Descomposición Binaria

### 1. El Constructor: Parsing y Pre-cálculo

Vamos a ver cómo inicializamos cada máquina. Este constructor hace dos cosas clave: 
* primero interpreta los datos de entrada
* segundo, realiza un pre-cálculo masivo para optimizar el rendimiento después.

### Parsing y Limpieza

Aquí tomamos la cadena de texto cruda `line` y extraemos dos cosas:
* Los targets: que son los valores a los que queremos llegar
* Los buttons: que convertimos en listas de enteros.

Cada lista nos dice qué registros incrementa ese botón en concreto.

### La Estrategia de Bitmasking

Fíjense en esta línea: `int limit = 1 << buttons.size();` Lo que estoy preparando es un bucle que va a generar todas las combinaciones posibles de botones pulsados simultáneamente. 
> Si tengo 3 botones, probaré las 8 combinaciones posibles de golpe.

### El Bucle de Simulación)

Entramos en el bucle `for (int mask...)`. Aquí recorremos cada combinación. 

* Dentro, uso esta condición: `if ((mask & (1 << i)) != 0)` Esto traduce la máscara binaria a la realidad: '¿Está el botón i activado en esta combinación?'. Si lo está, aumento el coste (cost++) y sumamos su impacto a la lista de efectos (effect). 

### La Tabla de Optimización

Finalmente, guardamos el resultado en nuestro mapa `patterns`. Pero solo guardamos un efecto si es nuevo o si hemos encontrado una forma más barata (menos botones) de conseguir el mismo resultado.

### 2. Método solve: Ingeniería Inversa (Recursividad)

Para resolver la Parte B, tuve que cambiar radicalmente de estrategia. Simular hacia adelante generaba un árbol exponencial infinito, así que apliqué Ingeniería Inversa: en lugar de multiplicar para llegar al objetivo, empiezo en el objetivo y divido hasta llegar a cero.

El algoritmo se basa en 4 pilares:

1. Gestión de Estado y Memoización (La Caché): Lo primero es la eficiencia. Antes de calcular nada, consulto mi mapa `memo`. Si ya sé cuánto cuesta llegar a cero desde el estado actual, devuelvo el dato inmediatamente.

2. El Filtro de Paridad: Itero sobre los patrones pre-calculados en el constructor, pero aplico una regla de oro:

'Solo puedo deshacer una multiplicación por 2 si el número resultante es PAR'. Por eso tengo la condición `if (diff % 2 != 0)`. Si al restar el patrón me queda un número impar, significa que ese camino es matemáticamente imposible (porque en el paso anterior debió haber una multiplicación por 2). Corto esa rama inmediatamente.

3. La Transición Recursiva: Si el filtro pasa, procedo a la recursión. La línea `nextState.add(diff / 2)` es donde deshago la operación de multiplicación dividiendo entre dos.

4. El Coste Ponderado: Al volver de la recursión, calculo el precio total con: `patternCost + 2 * costRest`.
