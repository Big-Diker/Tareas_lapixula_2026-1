Nombre: Iker Renato Ortiz Labraña
Rol: 202473562-4

Características del sistema:
- OS: CachyOS (arch-based btw)
- Entorno: DrRacket
- Lenguaje: Scheme

Instrucciones de uso:
    -Simplemente usar DrRacket para abrir las funciones y probarlas con los argumentos extra que se le añada a las funciones para demostrar correctamente el tiempo de ejecución.
    -También fueron testeadas en un entorno de CODE-OSS con la extension "magic racket". Sin embargo, era medio inestable por lo tanto no recomiendo tanto ejecutarlo así

Supuestos:

Para que todo funcione bien y respete las restricciones, asumí estas cositas:

1- P1 ROMPEHIELOS
- Asumo que el daemon ingresado siempre es una función matemática de un solo argumento.
- Ambas versiones mantienen el orden original de la lista.
- En la versión de cola, al final le hago un reverse normal al acumulador para arreglar el orden. Como no es destructivo (no es reverse!), no rompe las reglas de mutación de memoria. Además, en el foro del aula se permitió su uso.

2- P2 FABRICA DE GUSANOS
- Asumo que la red neuronal solo tiene números o sublistas.
- La función principal retorna la clausura (el lambda puro).
- Usé letrec para que la función interna se reconozca a sí misma y poder hacer la recursividad del árbol N-ario sin declarar variables globales.

3- P3 GUSANO EJECUTOR
- La lista de daemons se aplica en orden estricto al mismo número.
- Para no ensuciar la recursión principal del árbol, hice una función auxiliar que aplica la cascada de daemons. Esto también me sirvió para no usar reverse.
- Cuando pillo un cortafuegos ('X), simplemente no le hago cons y sigo con la recursión, borrándolo de la lista resultante.

4- P4 HASH DE DESCONEXION
- Asumo el estándar ASCII para pasar las letras a números.
- Resolví todo usando únicamente filter, map, foldl y las funciones de string/char nativas. No hay nada de recursión manual en esta parte.


Gracias!! :D