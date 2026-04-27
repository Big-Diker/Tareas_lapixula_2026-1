CC = gcc
CFLAGS = -Wall -Wextra -Wshadow -std=c11 -g
LDFLAGS = -lm
EXEC = reydestronado

OBJS = main.o tablero.o piezas.o armas.o

all: $(EXEC)

$(EXEC): $(OBJS)
	$(CC) $(CFLAGS) -o $(EXEC) $(OBJS) $(LDFLAGS)

main.o: main.c main.h tablero.h piezas.h armas.h
	$(CC) $(CFLAGS) -c main.c

tablero.o: tablero.c tablero.h piezas.h main.h armas.h
	$(CC) $(CFLAGS) -c tablero.c

piezas.o: piezas.c piezas.h main.h tablero.h
	$(CC) $(CFLAGS) -c piezas.c

armas.o: armas.c armas.h main.h piezas.h tablero.h
	$(CC) $(CFLAGS) -c armas.c

clean:
	rm -f *.o $(EXEC)