#lang scheme
;; icebreaker-simple
;; Aplica una funcion de descifrado (daemon) a una lista.
;; Parametros: 
;;   -daemon: funcion matemática a definir.
;;   -puertos: lista de numeros a procesar.
;; Retorno: Nueva lista con el daemon aplicado. Mantiene su orden original 
(define (icebreaker-simple daemon puertos)
  (cond
    ((null? puertos) '())
    ((number? (car puertos))
     (append (list (daemon (car puertos))) (icebreaker-simple daemon (cdr puertos))))))

;; icebreaker-cola
;; Hace lo mismo que icebreaker-simple, pero implementado con recursion de cola, 
;; es decir, usa menos memoria y es mas eficiente.
;; Parametros:
;;   -daemon: funcion matemática a definir.
;;   -puertos: lista de numeros a procesar.
;;    La funcion de recursividad interna "rec" tiene ademas, los siguientes parametros:
;;       +resto: copia de la lista puertos, el cual va decreciendo en tamaño por cada recursion
;;       +acum: lista en la que se van guardando los numeros procesados por cada recursion
;; Retorno: Nueva lista con el daemon aplicado. Mantiene su orden original
(define (icebreaker-cola daemon puertos)
  (let rec ((resto puertos) (acum '()))
  (cond
    ((null? resto)
     (reverse acum))
    ((number? (car resto))
     (rec (cdr resto) (cons (daemon (car resto)) acum))))))