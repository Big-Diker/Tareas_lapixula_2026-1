#lang scheme

;; aplicar-daemons
;; Funcion auxiliar que aplica secuencialmente una lista de funciones (daemons) a un numero.
;; Actua como un acumulador donde el resultado de cada funcion es la entrada de la siguiente.
;; Parametros:
;;   -lista-daemons: lista de funciones matematicas (lambdas) a aplicar.
;;   -nodo: el numero base al que se le aplicaran las transformaciones.
;; Retorno: El numero entero final tras pasar por toda la cascada de daemons.
(define (aplicar-daemons lista-daemons nodo)
  (if (null? lista-daemons)
      nodo
      (aplicar-daemons (cdr lista-daemons) ((car lista-daemons) nodo))))

;; ejecutor-cascada
;; Recorre el arbol N-ario eliminando los cortafuegos (X) y atacando los nodos vulnerables.
;; Parametros:
;;   -mapa: lista N-aria (puede contener sublistas) que representa la red neuronal.
;;   -daemons: lista de funciones de ataque que se aplicaran a cada numero encontrado.
;; Retorno: El arbol N-ario con las X eliminadas y los numeros mutados por la cascada.
(define (ejecutor-cascada mapa daemons)
  (cond
    ((null? mapa) '())
    ((equal? 'X (car mapa))
     (ejecutor-cascada (cdr mapa) daemons))
    ((number? (car mapa))
     (cons (aplicar-daemons daemons (car mapa)) (ejecutor-cascada (cdr mapa) daemons)))
    ((pair? (car mapa))
     (cons (ejecutor-cascada (car mapa) daemons) (ejecutor-cascada (cdr mapa) daemons)))))