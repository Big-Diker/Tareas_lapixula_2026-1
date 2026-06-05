#lang scheme

(define alfa (lambda (x) (modulo (+ x 15) 10)))

(define (icebreaker-cola daemon puertos)
    (cond
        ((null? puertos) '())
        ((number? (car puertos))
            (append (list (daemon (car puertos))) (icebreaker-cola daemon (cdr puertos))))))

(displayln (icebreaker-cola (lambda (x) (* x 2)) '()))