#lang scheme

;; crear-gusano
;; Fabrica y retorna un "gusano" (una funcion lambda) capaz de recorrer un arbol N-ario.
;; Reemplaza con X los nodos que no cumplan la condicion dada (vulnerabilidad), manteniendo intactos los vulnerables y la estructura.
;; Parametros:
;;   -vulnerabilidad: funcion (predicado) que evalua un numero y retorna #t o #f.
;;    La funcion interna "gusano" devuelta tiene ademas el siguiente parametro:
;;       +red-neuronal: lista N-aria (puede contener sublistas) a analizar y procesar.
;; Retorno: Una funcion lambda que, al ejecutarse sobre una red neuronal, devuelve la lista con las marcas.
(define (crear-gusano vulnerabilidad)
  (letrec ((gusano
            (lambda (red-neuronal)
              (cond
                ((null? red-neuronal) '())
                ((number? (car red-neuronal)) 
                 (if (vulnerabilidad (car red-neuronal))    
                     (cons (car red-neuronal) (gusano (cdr red-neuronal)))
                     (cons 'X (gusano (cdr red-neuronal)))))
                ((pair? (car red-neuronal))
                 (cons (gusano (car red-neuronal)) (gusano (cdr red-neuronal))))))))
    gusano))