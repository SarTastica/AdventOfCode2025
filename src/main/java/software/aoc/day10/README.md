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

**Fase de Parsing:**
Extraigo los números objetivo (`targets`) y convierto la definición de los botones en listas de enteros. Cada lista indica qué registros incrementa ese botón específico.

**Estrategia: Pre-cálculo de Combinaciones**
Calculo todas las combinaciones posibles de pulsaciones simultáneas.

**Bucle de Máscaras (Bitmasking):**
Utilizo un desplazamiento de bits (`1 << N`) para iterar sobre el conjunto potencia de los botones.

```
int limit = 1 << buttons.size(); // 2 elevado a N (Total de combinaciones)
int numRegisters = targets.size();
for (int mask = 0; mask < limit; mask++) { ... }
```

**Cálculo de Efecto y Coste**: 
Para cada máscara (combinación), sumo los efectos de los botones activos y calculo su coste acumulado.

```
// Si el bit 'i' está encendido en la máscara...
if ((mask & (1 << i)) != 0) { 
    cost++; // Incremento el coste
    // Aplico el efecto a los registros correspondientes
    // ...
}
```
**Tabla de Optimización (patterns)**: El resultado se almacena en un HashMap.

* Clave: El cambio neto en los registros (Efecto).
* Valor: El coste mínimo de pulsaciones para lograr ese efecto.

> Defensa Técnica: Durante la fase recursiva, en lugar de probar combinaciones de botones y recalcular sus efectos, simplemente consulto este mapa pre-calculado.

### 2. Método solve: Ingeniería Inversa (Recursividad)

El enunciado implica que en cada paso el estado se multiplica por 2. Simular esto hacia adelante genera un árbol de búsqueda que crece exponencialmente hacia el infinito.

**La Solución: Ingeniería Inversa**

Empiezo en el Target e intento llegar a 0 invirtiendo la operación matemática: dividiendo por 2.

**El Algoritmo Paso a Paso:**
* Caso Base y Memoización: Si llego a 0, el coste es 0. Utilizo un mapa memo para almacenar estados ya resueltos y evitar re-calcular ramas idénticas.
* Lógica de Paridad (Poda): Itero sobre los patterns pre-calculados. Para que un movimiento sea válido en reversa, la diferencia debe respetar la paridad binaria: `if ((EstadoActual - Patrón) % 2 == 0)`
* Transición de Estado: Si es válido, el siguiente estado recursivo es `diff / 2`. Estamos deshaciendo la multiplicación implícita del problema, desplazándonos al siguiente bit de significancia.
* Cálculo de Coste Ponderado: `minCost = PatrónCost + (2 * RestoCost);`

