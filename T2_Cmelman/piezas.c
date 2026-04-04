#include "piezas.h"
#include "main.h"
#include "tablero.h"
#include <stdlib.h>

static Pieza* crear_colocar(struct Juego *juego, char tipo, int hp, int x, int y) {
    Pieza *p = malloc(sizeof(Pieza));
    p->tipo = tipo;
    p->hp = hp;
    p->x = x;
    p->y = y;

    Celda *c = malloc(sizeof(Celda));
    c->pieza = p;

    juego->t->celdas[y][x] = (void *)c;

    return p;
}

static void spawn_rey(struct Juego *juego, int width, int hp){
    int y_rey = 0;
    int x_rey = (rand() % (width - 2)) + 1; // ignora los bordes

    juego->jugador = crear_colocar(juego, 'R', hp, x_rey, y_rey);
}

static void spawn_enemigo(struct Juego *juego, int width, int height, char tipo, int hp, int cantidad, int fila) {
    for (int i = 0; i < cantidad; ++i){
        int celda_random_x;
        do {celda_random_x = rand() % width;} // celda random en la fila
        while (juego->t->celdas[fila][celda_random_x] != NULL); // veo si hay algo en esa celda
        // el do while termina cuando esta vacia
        crear_colocar(juego, tipo, hp, celda_random_x, fila);    
    }
}

void spawn_nivel(struct Juego *juego, int nivel) {
    int width = juego->t->W;
    int height = juego->t->H;

    // spawn del rey
    spawn_rey(juego, width, 1);

    // spawn enemigos dependiendo del nivel
    if (nivel == 1) {
        spawn_enemigo(juego, width, height, 'P', 1, 4, height-2);
        spawn_enemigo(juego, width, height, 'C', 2, 2, height-1);
        spawn_enemigo(juego, width, height, 'A', 2, 2, height-1);
    }
    else if (nivel == 2) {
        spawn_enemigo(juego, width, height, 'P', 1, 4, height-2);
        spawn_enemigo(juego, width, height, 'C', 2, 2, height-1);
        spawn_enemigo(juego, width, height, 'T', 4, 2, height-1);
    }
    else if (nivel == 3) {
        spawn_enemigo(juego, width, height, 'P', 1, 2, height-2);
        spawn_enemigo(juego, width, height, 'Q', 3, 1, height-1);
        spawn_enemigo(juego, width, height, 'A', 2, 1, height-1);
        spawn_enemigo(juego, width, height, 'T', 4, 1, height-1);
    }
}