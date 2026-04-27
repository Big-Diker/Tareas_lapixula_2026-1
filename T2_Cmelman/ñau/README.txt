Nombre: Iker Renato Ortiz Labraña
Rol: 202473562-4

Características de mi sistema:
- OS: CachyOS
- Compilador: GCC (estándar C11)
- Librerías: Todo estándar de C

Instrucciones para jugar:
1. Abre la terminal en la carpeta del proyecto.
2. Escribe 'make' para compilar todo de una.
3. Ejecuta el juego con: ./reydestronado
4. Si quieres limpiar los archivos temporales (.o) y el ejecutable, usa 'make clean'.

COSAS IMPORTANTES Y SUPUESTOS
Para que el juego funcione bien y no explote la memoria, asumí estas cositas:

1- MOVIMIENTOS Y TURNOS
- Si intentas moverte fuera del tablero o le pegas a una pared, el juego te avisa y no te quita el turno.

2- LIMPIEZA DE ENEMIGOS (LOS "MUERTOS")
- Cuando le bajas toda la vida a un enemigo, lo borro del tablero. Libero la memoria de la pieza y de la celda, y dejo el espacio en NULL para que no estorben ni se sigan moviendo como zombis.

3- INTELIGENCIA DE LAS PIEZAS (PATHFINDING)
- Los enemigos son "greedy": siempre miran dónde está el Rey y tratan de dar el paso (normal o diagonal) que los deje lo más cerca posible.
- El Caballo es el más listo: calcula sus 8 posibles saltos en 'L', mira cuáles caen en lugares vacíos y pega el salto que lo deje a la distancia más corta del Rey usando la fórmula de la distancia.
- No se pueden pisar: si el lugar a donde quiere ir un enemigo ya tiene a otro, se queda quieto ese turno.

4- ARMA ESPECIAL: EL ESCUDITO
Este es el "salvavidas" del juego y funciona de forma reactiva:

- Gameplay: No disparas a nadie. En tu turno activas el escudo y te quedas ahí. Si en el turno de los enemigos alguien te intenta comer, el escudo explota. Te salva de morir, le hace 1 de daño a todos los que estén pegados a ti y los manda a volar a una posición aleatoria en un radio de 2 casillas (área de 5x5).
- Como funciona a nivel codigo:
  1. Primero, la función 'escudito' en armas.c prende un booleano llamado 'escudo_activo'.
  2. En mover_enemigos (piezas.c), si un enemigo llega a la casilla del Rey, reviso si el escudo está prendido.
  3. Si lo está, llamo a 'detonar_escudito', que busca piezas en el área 3x3 y usa un 'rand()' para encontrarles un lugar nuevo que esté vacío. 
  4. El escudo dura todo el turno de los enemigos, así que si vienen varios a la vez, a todos los manda lejos. Al final del turno se apaga solo.