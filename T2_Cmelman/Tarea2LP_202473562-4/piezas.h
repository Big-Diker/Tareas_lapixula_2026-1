#ifndef PIEZAS_H
#define PIEZAS_H

#include <stdbool.h>

struct Juego;

typedef struct Pieza {
    char tipo; /* ’P’=Peon, ’C’=Caballo, ’A’=Alfil, ’T’=Torre, ’Q’=Reina, ’R’=Rey */
    int hp;
    int x, y;
} Pieza;

typedef struct Celda {
    Pieza *pieza; /* NULL si la celda esta vacia */
} Celda;

void spawn_nivel(struct Juego *juego, int nivel);
void detonar_escudito(struct Juego *j);
bool verificar_estado_rey(struct Juego *juego); /* Revisa si el Rey esta en Jaque */
void mover_enemigos(struct Juego *j);
void limpiar_muertos(struct Juego *j);
#endif