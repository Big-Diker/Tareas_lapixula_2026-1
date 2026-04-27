#include "tablero.h"
#include "piezas.h"
#include "main.h"
#include "armas.h"

#include <stdlib.h>
#include <stdio.h>

struct Tablero* tablero_crear(int ancho, int alto) {
/*
 * Función: tablero_crear
 * ----------------------
 * Aloja la memoria dinámica inicial para la estructura Tablero y su 
 * matriz tridimensional de punteros void***, asegurando que todos los 
 * punteros inicien en estado NULL mediante calloc.
 *
 * ancho, alto: Dimensiones del grid del tablero.
 *
 * retorna: Puntero al nuevo Tablero listo para ser usado.
 */
    struct Tablero *nuevo_tablero = malloc(sizeof(Tablero));

    nuevo_tablero->W = ancho;
    nuevo_tablero->H = alto;

    nuevo_tablero->celdas = malloc(alto * sizeof(void **));
    for (int i = 0; i < alto; ++i) {
        nuevo_tablero->celdas[i] = calloc(ancho, sizeof(void*));
    }

    return nuevo_tablero;
}

void tablero_imprimir(struct Juego *juego) {
/*
 * Función: tablero_imprimir
 * -------------------------
 * Imprime el estado visual del juego por la salida estándar. 
 * Formatea las coordenadas X e Y en los ejes, ajustando el espaciado para 
 * evitar desfases visuales en tableros de dos dígitos (ej. 12x12).
 *
 * juego: Puntero al estado del juego.
 */
    for (int y = juego->t->H - 1; y >= 0; --y) {
        printf("%2d ", y + 1); // print de las coords en y (+1 para corregir desfase)
        // uso %2d para que no se desfase el tablero cuando el n. de la altura sea de 2 digitos
        
        for (int x = 0; x < juego->t->W; ++x) {
            printf("[");

            Celda *c = (Celda *)juego->t->celdas[y][x]; // coord de la celda actual
            if (c != NULL && c->pieza != NULL) {
                printf("%c", c->pieza->tipo); // P, C, A, T, Q, R
            }
            else printf(" "); // celda vacía
            
            printf("]");
        }
        printf("\n");
    }

    // print de las coords en x
    printf("    "); // 4 saltos de espacio: indice (2digitos), espacio, corchete ([)
    for (int x = 0; x < juego->t->W; x++) {
        printf(" %d ", x + 1); // +1 para corregir el desfase
    }
    printf("\n\n");
}

void tablero_liberar(struct Tablero *tablero) {
/*
 * Función: tablero_liberar
 * ------------------------
 * Rutina exhaustiva de recolección de basura. Itera sobre cada nodo de la 
 * matriz tridimensional liberando secuencialmente Piezas residuales, 
 * Celdas, columnas, filas y finalmente la estructura Tablero en sí, 
 * garantizando cero fugas de memoria entre niveles.
 *
 * tablero: Puntero a la estructura Tablero a destruir.
 */
    for (int i = 0; i < tablero->H; ++i) {
        for (int j = 0; j < tablero->W; ++j) {
            Celda *celda = (Celda *)tablero->celdas[i][j];

            if (celda != NULL) free(celda->pieza); // Por si hay un enemigo al momento de liberar el tablero
            free(celda); // Puntero a casilla (contenido) [1]
        }
        free(tablero->celdas[i]); // Puntero a arreglo en cada fila (columnas) [2]
    }
    free(tablero->celdas); // Puntero a el arreglo de filas (filas) [3]
    free(tablero); // Puntero a la estructura tablero como tal
}