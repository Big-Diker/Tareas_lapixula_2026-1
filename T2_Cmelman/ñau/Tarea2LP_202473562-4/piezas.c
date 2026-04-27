#include "piezas.h"
#include "main.h"
#include "tablero.h"

#include <stdlib.h>
#include <stdio.h>
#include <math.h>

static Pieza* crear_colocar(struct Juego *juego, char tipo, int hp, int x, int y) {
/*
 * Función: crear_colocar (Auxiliar Interna)
 * -----------------------------------------
 * Reserva memoria dinámica en el heap tanto para una nueva Pieza como para 
 * su Celda contenedora, asignando sus valores iniciales y vinculándola a la 
 * matriz tridimensional del tablero.
 */
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
/* Función auxiliar para crear específicamente al Rey en la fila inferior (y=0). */
    int y_rey = 0;
    int x_rey = (rand() % (width - 2)) + 1; // ignora los bordes

    juego->jugador = crear_colocar(juego, 'R', hp, x_rey, y_rey);
}

static void spawn_enemigo(struct Juego *juego, int width, char tipo, int hp, int cantidad, int fila) {
/* Función auxiliar para poblar una fila específica con cantidad aleatoria de un tipo de enemigo. */
    for (int i = 0; i < cantidad; ++i){
        int celda_rand_x;
        do {celda_rand_x = rand() % width;} // celda random en la fila
        while (juego->t->celdas[fila][celda_rand_x] != NULL); // veo si hay algo en esa celda
        // el do while termina cuando esta vacia
        crear_colocar(juego, tipo, hp, celda_rand_x, fila);    
    }
}

void spawn_nivel(struct Juego *juego, int nivel) {
/*
 * Función: spawn_nivel
 * --------------------
 * Orquesta la creación del nivel actual. Invoca a las funciones auxiliares de 
 * spawn para colocar al Rey y generar las oleadas de enemigos según la dificultad 
 * y las dimensiones correspondientes a cada nivel.
 *
 * juego: Puntero al estado general del juego.
 * nivel: Nivel a generar (1, 2 o 3).
 */
    int width = juego->t->W;
    int height = juego->t->H;

    // spawn del rey
    spawn_rey(juego, width, 1);

    // spawn enemigos dependiendo del nivel
    if (nivel == 1) {
        spawn_enemigo(juego, width, 'P', 1, 4, height-2);
        spawn_enemigo(juego, width, 'C', 2, 2, height-1);
        spawn_enemigo(juego, width, 'A', 2, 2, height-1);
    }
    else if (nivel == 2) {
        spawn_enemigo(juego, width, 'P', 1, 4, height-2);
        spawn_enemigo(juego, width, 'C', 2, 2, height-1);
        spawn_enemigo(juego, width, 'T', 4, 2, height-1);
    }
    else if (nivel == 3) {
        spawn_enemigo(juego, width, 'P', 1, 2, height-2);
        spawn_enemigo(juego, width, 'Q', 3, 1, height-1);
        spawn_enemigo(juego, width, 'A', 2, 1, height-1);
        spawn_enemigo(juego, width, 'T', 4, 1, height-1);
    }
}

void detonar_escudito(struct Juego *j) {
/*
 * Función: detonar_escudito
 * -------------------------
 * Ejecuta el efecto reactivo del arma especial. Rastrea un área adyacente al Rey (3x3), 
 * daña a los enemigos presentes y usa números aleatorios (rand) para reubicarlos
 * en posiciones libres dentro de un radio de 2 bloques, salvando al jugador de la captura.
 *
 * j: Puntero al estado del juego.
 */
    if (!j->escudo_activo) return;
    
    printf("ESCUDITO ACTIVADO!!!!\n");
    int rey_x = j->jugador->x;
    int rey_y = j->jugador->y;

    // busca enemigos en un area 3x3 (radio 2)
    for (int dy = -1; dy <= 1; dy++) {
        for (int dx = -1; dx <= 1; dx++) {
            if (dx == 0 && dy == 0) continue;

            int ady_x = rey_x + dx;
            int ady_y = rey_y + dy;

            if (ady_x >= 0 && ady_x < j->t->W && ady_y >= 0 && ady_y < j->t->H) {
                Celda *c = (Celda *)j->t->celdas[ady_y][ady_x];
                
                if (c != NULL && c->pieza != NULL) {
                    // si le pega a una pieza
                    int rand_x, rand_y; 
                    int intentos = 0; // 
                    bool exito = false;

                    while (!exito && intentos < 15) { // max de intentos para reposicionar la pieza en una celda valida
                        // (rand() % 5) - 2 genera: -2, -1, 0, 1, 2
                        int off_x = (rand() % 5) - 2;
                        int off_y = (rand() % 5) - 2;

                        rand_x = rey_x + off_x;
                        rand_y = rey_y + off_y;

                        // validacion out of bounds y que la celda este vacia
                        if (rand_x >= 0 && rand_x < j->t->W && rand_y >= 0 && rand_y < j->t->H) {
                            if (j->t->celdas[rand_y][rand_x] == NULL && (rand_x != rey_x || rand_y != rey_y)) {
                                exito = true;
                            }
                        }
                        intentos++; // si la celda candidata no es valida
                    }

                    if (exito) {
                        // movemos la memoria a la celda nueva y dejamos en null la vieja
                        j->t->celdas[rand_y][rand_x] = j->t->celdas[ady_y][ady_x];
                        j->t->celdas[ady_y][ady_x] = NULL;
                        
                        // movemos de posicion a la pieza
                        c->pieza->x = rand_x;
                        c->pieza->y = rand_y;
                        c->pieza->hp -= 1;
                        printf("%c repelido a (%d, %d)!\n", c->pieza->tipo, rand_x + 1, rand_y + 1); // +1 para mostrar la posicion en vez del indice del arreglo
                    }
                }
            }
        }
    }
}

bool verificar_estado_rey(struct Juego *j) {
/*
 * Función: verificar_estado_rey
 * -----------------------------
 * Escanea el tablero evaluando las distancias y reglas de movimiento de los 
 * enemigos activos para determinar si el Rey se encuentra bajo amenaza inminente 
 * ("Jaque") para el siguiente turno.
 *
 * j: Puntero al estado del juego.
 *
 * retorna: true si el Rey está amenazado por al menos un enemigo, false de lo contrario.
 */
    int rey_x = j->jugador->x;
    int rey_y = j->jugador->y;

    for (int y = 0; y < j->t->H; y++) {
        for (int x = 0; x < j->t->W; x++) {
            Celda *c = (Celda *)j->t->celdas[y][x];
            if (c == NULL || c->pieza == NULL || c->pieza->tipo == 'R') continue;

            Pieza *p = c->pieza;
            int dx = abs(rey_x - p->x);
            int dy = abs(rey_y - p->y);

            // peon
            if (p->tipo == 'P') {
                if (dx <= 1 && dy <= 1) return true;
            }
            // caballo
            else if (p->tipo == 'C') {
                if ((dx == 1 && dy == 2) || (dx == 2 && dy == 1)) return true;
            }
            // alfil
            else if (p->tipo == 'A') {
                if (dx == dy && dx <= 3) return true;
            }
            // torre
            else if (p->tipo == 'T') {
                if ((rey_x == p->x || rey_y == p->y) && (dx <= 3 && dy <= 3)) return true;
            }
            // reina
            else if (p->tipo == 'Q') {
                if ((dx == dy || rey_x == p->x || rey_y == p->y) && (dx <= 4 && dy <= 4)) return true;
            }
        }
    }
    return false;
}

void mover_enemigos(struct Juego *j) {
/*
 * Función: mover_enemigos
 * -----------------------
 * Motor principal de Inteligencia Artificial (Pathfinding). 
 * Calcula la ruta más corta ("Greedy") hacia el Rey para cada pieza enemiga 
 * respetando sus reglas de movimiento. Gestiona las colisiones, restringe los turnos 
 * de piezas lentas (Torre) y gatilla el Game Over o el Escudo si capturan al Rey.
 *
 * j: Puntero al estado general del juego.
 */
    j->turno_enemigos++;
    int rey_x = j->jugador->x;
    int rey_y = j->jugador->y;

    // matriz para evitar que un enemigo se mueva 2 veces en el mismo turno
    bool ya_se_movio[j->t->H][j->t->W];
    for(int y=0; y<j->t->H; y++) for(int x=0; x<j->t->W; x++) ya_se_movio[y][x] = false;

    for (int y = 0; y < j->t->H; y++) {
        for (int x = 0; x < j->t->W; x++) {
            // si la casilla ya fue procesada (el enemigo acaba de llegar aqui), la saltamos
            if (ya_se_movio[y][x]) continue;

            Celda *c = (Celda *)j->t->celdas[y][x];
            if (c == NULL || c->pieza == NULL || c->pieza->tipo == 'R') continue;

            Pieza *p = c->pieza;
            if (p->tipo == 'T' && j->turno_enemigos % 2 != 0) continue;

            int dest_x = p->x;
            int dest_y = p->y;

            if (p->tipo == 'C') {
                int saltos[8][2] = {{1,2}, {2,1}, {2,-1}, {1,-2}, {-1,-2}, {-2,-1}, {-2,1}, {-1,2}};
                double min_dist = 9999.0;
                for (int i = 0; i < 8; i++) {
                    int nx = p->x + saltos[i][0];
                    int ny = p->y + saltos[i][1];
                    if (nx >= 0 && nx < j->t->W && ny >= 0 && ny < j->t->H) {
                        if (j->t->celdas[ny][nx] == NULL || (nx == rey_x && ny == rey_y)) {
                            double dist = sqrt(pow(rey_x - nx, 2) + pow(rey_y - ny, 2));
                            if (dist < min_dist) {
                                min_dist = dist;
                                dest_x = nx;
                                dest_y = ny;
                            }
                        }
                    }
                }
            } else {
                int dir_x = (rey_x > p->x) ? 1 : (rey_x < p->x ? -1 : 0);
                int dir_y = (rey_y > p->y) ? 1 : (rey_y < p->y ? -1 : 0);
                if (p->tipo == 'T') { 
                    if (abs(rey_x - p->x) > abs(rey_y - p->y)) dir_y = 0; else dir_x = 0;
                } else if (p->tipo == 'A') { 
                    if (dir_x == 0 || dir_y == 0) continue; 
                }
                dest_x = p->x + dir_x;
                dest_y = p->y + dir_y;
            }

            if (dest_x >= 0 && dest_x < j->t->W && dest_y >= 0 && dest_y < j->t->H) {
                if (dest_x == rey_x && dest_y == rey_y) {
                    if (j->escudo_activo) {
                        detonar_escudito(j);
                    } else {
                        printf("GAME OVER\nEl Rey ha sido capturado por %c\n", p->tipo);
                        j->vivo = false;
                        return;
                    }
                } 
                else if (j->t->celdas[dest_y][dest_x] == NULL) {
                    j->t->celdas[dest_y][dest_x] = j->t->celdas[p->y][p->x];
                    j->t->celdas[p->y][p->x] = NULL;
                    p->x = dest_x;
                    p->y = dest_y;
                    
                    // MARCAMOS LA CASILLA DESTINO COMO MOVIDA
                    ya_se_movio[dest_y][dest_x] = true;
                }
            }
        }
    }
    j->escudo_activo = false; 
}

void limpiar_muertos(struct Juego *j) {
/*
 * Función: limpiar_muertos
 * ------------------------
 * Recorre todo el tablero localizando estructuras de tipo Pieza cuyos puntos 
 * de vida (hp) sean 0 o inferiores. Procede a liberar la memoria heap (free) 
 * de la Pieza y la Celda correspondiente, dejando el puntero en NULL.
 *
 * j: Puntero al estado del juego.
 */
    for (int y = 0; y < j->t->H; y++) {
        for (int x = 0; x < j->t->W; x++) {
            Celda *c = (Celda *)j->t->celdas[y][x];
            if (c != NULL && c->pieza != NULL && c->pieza->tipo != 'R') {
                if (c->pieza->hp <= 0) {
                    printf("¡%c ha sido destruido!\n", c->pieza->tipo);
                    free(c->pieza);
                    free(c);
                    j->t->celdas[y][x] = NULL;
                }
            }
        }
    }
}