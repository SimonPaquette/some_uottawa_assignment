#lang scheme

#|
Simon Paquette
300044038
CSI2520
Devoir3
Question4
|#

(define eValue 2.71828)

(define (sigmoid v)
  (/ 1 (+ 1 (expt eValue (- v))))
  )

(define (sommePondere list ponderationList)
  (if (null? list)
      0
      (if (null? (cdr list))
          (car list)
          (+ (* (car list) (car ponderationList)) (sommePondere (cdr list) (cdr ponderationList))))))
  
  
(define (neuralNode list function xValue)
  (function (sommePondere list xValue))
  )


(define (neuralLayer list xValue)
  (if (null? (cdr list))
      (cons (neuralNode(car list) sigmoid xValue) '())
      (cons (neuralNode(car list) sigmoid xValue) (neuralLayer(cdr list) xValue))
      )
  )

(define (neuralNet xValue)
  (/ (sommePondere (neuralLayer '((0.1 0.3 0.4)(0.5 0.8 0.3)(0.7 0.6 0.6)) xValue) xValue) 2)
  )


(define (X1 k n)
  (sin(/ (* (* 2 3.14) k) n))
  )
(define (X2 k n)
  (cos(/ (* (* 2 3.14) k) n))
  )


(define (applyNet n)
  (if (= n 0)
      '()
      (let ([a (X1 (+ n 1) n)] [b (X2 (+ n 1) n)]) (cons (neuralNet (list a b)) (applyNet (- n 1)))))
  )


(neuralNode '(0.1 0.3 0.4) sigmoid '(0.5 0.5))
(neuralLayer '((0.1 0.3 0.4)(0.5 0.8 0.3)(0.7 0.6 0.6)) '(0.5 0.5))
(neuralNet '(0.5 0.5))
(applyNet 16)      
  
  
  