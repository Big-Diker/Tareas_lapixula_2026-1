# Tarea 3: Final Fantasy VII - UTFSM Edition

Iker Ortiz 
202473562-4

## Compilación y Ejecución
Puede ser relevante mecionar que la tarea se desarrolló, compiló y ejecutó en CachyOS (distro basado en arch btw).

OpenJDK version "26.0.2" y javac 26.0.2 utilizados.

El proyecto incluye un makefile para facilitar su ejecución. Abrir la terminal en la raíz de la tarea y usar los siguientes comandos:

* Para compilar todo el código: `make`
* Para ejecutar el juego: `make run`
* Para eliminar los archivos .class: `make clean`

Notas importantes:

    -Es estrictamente necesario que la terminal esté configurada en UTF-8. El juego utiliza bloques de texto con ASCII art en algunas partes, los cuales se desfasarán si se usa otra codificación.

    -En el diseño, se usó una terminal con una anchura de la mitad de un display de 2k 27". Lo digo pues si se usa la terminal en pantalla completa, se verá feo y raro algunas cosas.
    
    -Recomiendo correr el código con una terminal dedicada, no con la del vscode o algun ide, en lo personal usé Alacritty.


## Supuestos y Decisiones de Diseño

Se definieron los siguientes supuestos:

1. Inicio del Juego: El flujo principal comienza directamente en el Sector 7, actuando como la base de operaciones antes de poder abrir el mapa para viajar.
2. Simulador: El ataque fatal de simulador NO termina el combate, se realizó esta elección de diseño pues de esta manera se podía idlear apretando la tecla 1 para farmear xp (existe la mejor manera usando un cheat code detallado en la sección de abajo, pero aún así deje la mecánica implementada de esta forma).
3. Mecánica de Muerte: Al morir en Gongaga o en el Núcleo del Planeta, Cloud pierde la chatarra y las materias de su mochila. Sin embargo, las materias que ya estén equipadas en la Buster Sword se mantienen intactas. En el Simulador del Sector 7, como fue detallado en el punto anterior, Cloud no recibe daño si es que este lo dejaría sin vida.
4. Ataque Limit Breaker: Este ataque hace daño en área a todos los enemigos presentes. Se maneja internamente con el pseudo-elemento "LIMITE", el cual ignora cualquier cálculo de debilidad o resistencia de los monstruos.
5. Materias Físicas: No se consideró la existencia de una materia de tipo "FISICO" (como botín en Gongaga), ya que el enunciado no la especifica en los drops de la selva.
6. Ranuras del Arma: La Buster Sword tiene un límite de 5 materias equipadas en total.
7. Combates Múltiples: La probabilidad de ataque conjunto (50% para 2 enemigos, 33% para 3) se calcula por turno. Si la probabilidad falla o solo queda un enemigo, atacará un único enemigo elegido al azar, evitando que ataquen todos en fila por defecto.
8. Boss Final (Sephiroth): Para entrar al Núcleo se exige nivel 20 y tener mínimo 2 materias equipadas. El contador de la Supernova aumenta con cada turno que pasa. Al llegar a 10, Sephiroth lanza la Supernova de manera inmediata en ese mismo turno (Es decir, tiene un doble turno).
9. En clases como Jugador.java, se dejaron algunos métodos en el cementerio de getters y setters, esto con la razón de que son métodos similares a estos. En el sentido de que consisten en un setter personalizado para mejorar la legibilidad en los otros métodos por ejemplo (tal como subirHP()), mientras que hay otros (como recibirXP) que hacen algo distinto pero de manera muy breve. El primer tipo mencionado no fue comentado, mientras que el segundo si.
10. Prints: El juego está lleno de comentarios meta con el fin de ojalá darle algo de personalidad al mismo (y de mantener mi sanidad al programarlo, pues me hacía reir poner estupideces).
11. Mensaje final de victoria: Se eligió la ruta de mostrar un ascii, prefiero no spoilear que es, echarle un ojo!
12. Se utilizó `@SuppressWarnings("resource")` en algunas instacias del código para suprimir el warning de scanner sin cerrar (No es leak de memoria, pues se cierra al final del main o al terminar la ejecución de una clase)

## Códigos de Depuración (Cheats)
Se dejaron habilitados inputs ocultos en los menús de selección para facilitar la revisión del código y testear el final sin necesidad de farmear:

* En el menú del Sector 7:
  * Input `777`: Sube a Cloud directamente a Nivel 20 y le entrega 2 materias en la mochila (A LA MOCHILA, se deben equipar al arma).

* En los menús de combate:
  * Input `99`: God Mode. Fija el HP de Cloud en 10.000 para poder sobrevivir pasivamente a los ataques (Usar en conjunto con el cheat de abajo).
  * Input `727`: Pasar Turno. Cloud no hace nada. Se recomienda usar este input de forma repetida en la pelea contra Sephiroth (después de usar el 99) para ver la animación de la Supernova sin matarlo por error con ataques básicos (Me gasté una innecesaria cantidad de tiempo en hacerla, está inspirada en la secuencia de la supernova del ff7 [Así que recomiendo verla antes de], pero no es nada del otro mundo, me quería entretener un poco nada más xdd).