#include "main.h"
#include "tablero.h"
#include "piezas.h"
#include "armas.h"

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <stdbool.h>

void tecla_a_vector(char tecla, int *dx, int *dy) {
/*
 * Función: tecla_a_vector
 * -----------------------
 * Transforma un caracter de entrada del usuario en un vector de movimiento 
 * matemático direccional para ser usado en cálculos de coordenadas (ej. W = [0, 1]).
 *
 * tecla: Caracter ingresado por el teclado.
 * dx, dy: Punteros donde se almacenarán los desplazamientos cartesianos resultantes.
 */
    *dx = 0; *dy = 0;
    switch(tecla) {
        case 'w': *dy = 1; break;  case 's': *dy = -1; break;
        case 'a': *dx = -1; break; case 'd': *dx = 1; break;
        case 'q': *dx = -1; *dy = 1; break; case 'e': *dx = 1; *dy = 1; break;
        case 'z': *dx = -1; *dy = -1; break; case 'c': *dx = 1; *dy = -1; break;
    }
}

int contar_enemigos(struct Juego *j) {
/*
 * Función: contar_enemigos
 * ------------------------
 * Realiza un barrido completo del tablero y contabiliza el total de piezas 
 * activas ignorando al Rey ('R'). Se utiliza para determinar la condición 
 * de victoria del nivel actual.
 *
 * j: Puntero al estado del juego.
 *
 * retorna: La cantidad entera de enemigos vivos restantes.
 */
    int total = 0;
    for (int y = 0; y < j->t->H; y++) {
        for (int x = 0; x < j->t->W; x++) {
            Celda *c = (Celda *)j->t->celdas[y][x];
            if (c && c->pieza && c->pieza->tipo != 'R') total++;
        }
    }
    return total;
}

int main() {
/*
 * Función: main
 * -------------
 * Punto de entrada del programa. Inicializa las semillas de aleatoriedad, enlaza 
 * el arsenal a sus punteros de función y gestiona el bucle (game loop) principal:
 * lecturas de I/O, recarga de nivel, validación de turnos y condiciones de término.
 *
 * retorna: 0 al finalizar la ejecución exitosamente.
 */
    srand(time(NULL));
    struct Juego juego;
    juego.nivel_actual = 1;
    juego.turno_enemigos = 0;
    juego.escudo_activo = false;
    juego.vivo = true;

    // cargar funciones de armas
    juego.arsenal.disparar[0] = escopeta;
    juego.arsenal.disparar[1] = francotirador;
    juego.arsenal.disparar[2] = granada;
    juego.arsenal.disparar[3] = escudito;

    int tamaños[3] = {12, 8, 6}; // niveles 1, 2 y 3

    while (juego.nivel_actual <= 3) {
        int dim = tamaños[juego.nivel_actual - 1];
        juego.t = tablero_crear(dim, dim);
        spawn_nivel(&juego, juego.nivel_actual);
        
        // recarga de munición al iniciar nivel
        juego.arsenal.municion_actual[0] = 2; // Escopeta
        juego.arsenal.municion_actual[1] = 1; // Sniper
        juego.arsenal.municion_actual[2] = 2; // Granada
        juego.arsenal.municion_actual[3] = 1; // Escudito

        printf("\n--- ENTRANDO AL NIVEL %d ---\n", juego.nivel_actual);

        while (juego.vivo) {
            tablero_imprimir(&juego);

            if (verificar_estado_rey(&juego)) {
                printf("CUIDADO, EL REY ESTA EN JAQUE!!!\n");
            }

            printf("Municion: [1]Escopeta(%d/2) [2]Sniper(%d/1) [3]Granada(%d/2) [4]Escudito(%d/1)\n", 
                   juego.arsenal.municion_actual[0], juego.arsenal.municion_actual[1], 
                   juego.arsenal.municion_actual[2], juego.arsenal.municion_actual[3]);
            printf("Ingrese accion (WASD/QEZC para mover, 1-4 para disparar): ");
            
            char input;
            scanf(" %c", &input);
            int dx = 0, dy = 0;
            bool turno_valido = false;

            if (input >= '1' && input <= '4') {
                int arma_idx = input - '1';
                if (arma_idx != 3) { // solo si es algun arma normal, no el escudo
                    printf("Direccion de disparo (WASD...): ");
                    char dir_input;
                    scanf(" %c", &dir_input);
                    tecla_a_vector(dir_input, &dx, &dy);
                }
                // llamada a arsenal, para el escudo dx y dy = 0, lo cual da igual
                if (juego.arsenal.disparar[arma_idx](&juego, dx, dy)) turno_valido = true;
            } else {
                tecla_a_vector(input, &dx, &dy);
                // logica movimiento del rey
                int nx = juego.jugador->x + dx;
                int ny = juego.jugador->y + dy;
                
                if (nx >= 0 && nx < juego.t->W && ny >= 0 && ny < juego.t->H && juego.t->celdas[ny][nx] == NULL) {
                    // mover al rey en el tablero
                    juego.t->celdas[ny][nx] = juego.t->celdas[juego.jugador->y][juego.jugador->x];
                    juego.t->celdas[juego.jugador->y][juego.jugador->x] = NULL;
                    juego.jugador->x = nx; juego.jugador->y = ny;
                    
                    // recarga de escopeta al moverse
                    if (juego.arsenal.municion_actual[0] < 2) juego.arsenal.municion_actual[0]++;
                    turno_valido = true;
                }
            }

            if (turno_valido) {
                limpiar_muertos(&juego); // antes de contar, limpiamos los cadaveres

                if (contar_enemigos(&juego) == 0) {
                    printf("¡Nivel %d completado!\n", juego.nivel_actual);
                    break; 
                }
                mover_enemigos(&juego);
                if (!juego.vivo) break;
            } else {
                printf("Accion invalida o sin municion.\n");
            }
        }

        tablero_liberar(juego.t);
        if (!juego.vivo) break; // si mueres, no pasas de nivel
        juego.nivel_actual++;
    }

    if (juego.vivo) printf("FELICIDADES! Has recuperado el castillo.\n");
    return 0;
}