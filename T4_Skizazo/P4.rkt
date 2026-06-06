#lang scheme

;; hash-desconexion
;; Procesa un token corrupto para extraer su hash de desconexion usando funciones de orden superior.
;; Parametros:
;;   - token: cadena de texto (String) que representa la clave interceptada.
;; Retorno: Un numero entero que corresponde a la suma de los valores ASCII de solo las letras del token.
(define (hash-desconexion token)
  (foldl + 0 
         (map char->integer 
              (filter char-alphabetic? 
                      (string->list token)))))