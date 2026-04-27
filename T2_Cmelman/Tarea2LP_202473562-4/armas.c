#include "armas.h"
#include "main.h"
#include "piezas.h"
#include "tablero.h"

#include <stdlib.h>
#include <stdio.h>

bool escopeta(struct Juego *j, int dir_x, int dir_y){
/*
 * Función: escopeta
 * -----------------
 * Arma de corto alcance. Inflige 2 puntos de daño a la casilla inmediatamente 
 * adyacente en la dirección indicada y 1 punto de daño de área a las 3 casillas 
 * que se encuentran detrás del objetivo inicial (efecto cono).
 *
 * j: Puntero a la estructura principal del juego.
 * dir_x, dir_y: Vector direccional del disparo (ej. 0, 1 para Norte).
 *
 * retorna: true si el disparo fue exitoso, false si falló (out of bounds o sin munición).
 */
    if (j->arsenal.municion_actual[0] == 0) return false; // no hay balas

    int target_x = j->jugador->x + dir_x;
    int target_y = j->jugador->y + dir_y;

    if (target_x < 0 || target_x >= j->t->W || target_y < 0 || target_y >= j->t->H) { // out of bounds
        printf("Estás usando tus lentes? No le vas a dar a nada!\n");
        return false;
    }

    Celda *c = (Celda *)j->t->celdas[target_y][target_x];
    // daño frontal
    if (c != NULL && c->pieza != NULL) {
        c->pieza->hp -= 2;
    }
    // daño trasero
    int perp_x = -dir_y; // perpendicular
    int perp_y = dir_x;
    for (int i = -1; i <= 1; ++i) { // recorre las 3 casillas
        int atras_x = j->jugador->x + (2 * dir_x) + (i * perp_x); // se aplica el cambio de coord con el indice del for
        int atras_y = j->jugador->y + (2 * dir_y) + (i * perp_y);

        // check de que el impacto este dentro del tablero
        if (atras_x >= 0 && atras_x < j->t->W && atras_y >= 0 && atras_y < j->t->H) {
            Celda *c_atras = (Celda *)j->t->celdas[atras_y][atras_x];

            if (c_atras != NULL && c_atras->pieza != NULL) {
                c_atras->pieza->hp -= 1;
            }
        }
    }

    j->arsenal.municion_actual[0]--;
    return true;
}

bool francotirador(struct Juego *j, int dir_x, int dir_y) {
/*
 * Función: francotirador
 * ----------------------
 * Arma de largo alcance. Recorre el tablero en línea recta desde la posición 
 * del jugador hasta chocar con el primer enemigo o salir de los límites, 
 * infligiendo 3 puntos de daño al primer objetivo impactado.
 *
 * j: Puntero al estado del juego.
 * dir_x, dir_y: Vector direccional del disparo.
 *
 * retorna: true al disparar exitosamente.
 */
    if (j->arsenal.municion_actual[1] <= 0) return false;

    int target_x = j->jugador->x + dir_x;
    int target_y = j->jugador->y + dir_y;
    // recorre hasta encontrar un objetivo
    while (target_x >= 0 && target_x < j->t->W && target_y >= 0 && target_y < j->t->H) {
        Celda *c = (Celda *)j->t->celdas[target_y][target_x];
        if (c != NULL && c->pieza != NULL) {
            c->pieza->hp -= 3;
            break; // solo le pega a el primero y sale del while
        }
        // si no hay impacto, avanza otra casilla en la direccion
        target_x += dir_x;
        target_y += dir_y;
    }

    j->arsenal.municion_actual[1]--;
    return true;
}

bool granada(struct Juego *j, int dir_x, int dir_y) {
/*
 * Función: granada
 * ----------------
 * Arma de área. Calcula un epicentro a exactamente 3 casillas de distancia 
 * del jugador y aplica 2 de daño a todas las entidades dentro de un área de 3x3 
 * alrededor de ese punto.
 *
 * j: Puntero al estado del juego.
 * dir_x, dir_y: Vector direccional hacia donde lanzar la granada.
 *
 * retorna: true si detonó en el tablero, false si el centro es inválido.
 */
    if (j->arsenal.municion_actual[2] <= 0) return false;

    int center_x = j->jugador->x + (3 * dir_x);
    int center_y = j->jugador->y + (3 * dir_y);

    if (center_x < 0 || center_x >= j->t->W || center_y < 0 || center_y >= j->t->H) {
        printf("No le des comida a las entidades del vacío por favor\n");
        return false;
    }
    // area 3x3 de explosion
    for (int dy = -1; dy <= 1; dy++) {
        for (int dx = -1; dx <= 1; dx++) {
            int target_x = center_x + dx;
            int target_y = center_y + dy;

            if (target_x >= 0 && target_x < j->t->W && target_y >= 0 && target_y < j->t->H) {
                Celda *c = (Celda *)j->t->celdas[target_y][target_x];
                if (c != NULL && c->pieza != NULL) {
                    c->pieza->hp -= 2;
                }
            }
        }
    }

    j->arsenal.municion_actual[2]--;
    return true;
}

// La logica de funcionamiento va a estar en piezas.c
bool escudito(struct Juego *j, int dir_x, int dir_y) {
/*
 * Función: escudito
 * -----------------
 * Solamente activa el estado del escudo
 *
 * j: Puntero al estado del juego.
 * dir_x, dir_y: Parámetros ignorados, requeridos por la firma FuncArma.
 *
 * retorna: true si se activó, false si no hay cargas.
 */
    (void)dir_x; // no las utilizo en este arma, casteo a void para que el compilador no me lesee con que no las uso
    (void)dir_y; // no puedo quitarlas de los parametros tampoco porque tiraria error con la firma de FuncArma

    if (j->arsenal.municion_actual[3] <= 0) {
        printf("Paga la luz, que no queda nada para prender el escudo!\n");
        return false;
    }

    j->escudo_activo = true;
    printf("Escudito activado! Deja que te hagan daño el siguiente turno\n");

    j->arsenal.municion_actual[3]--;
    return true;
}