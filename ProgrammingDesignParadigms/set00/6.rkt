;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |6|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")))))
(require rackunit)

;; DATA DEFINITIONS: none

; solve_quadratic_equation : Number Number Number  -> Number
; GIVEN: three numbers a, b and c
; RETURNS: one of the solutions of the quadratic equation 
; Examples:
; (solve_quadratic_equation 1 2 1) => -1
; (solve_quadratic_equation 3 4 5) => #i-0.6666666666666666+1.1055415967851332i
;; DESIGN STRATEGY: One of the solutions of the quadratic equation is given by the formula x = ( / (+ (- b)(sqrt (- (* b b) (* 4 a c)))) (* 2 a)))
(define (solve_quadratic_equation a b c)
  ( / (+ (- b)(sqrt (- (* b b) (* 4 a c)))) (* 2 a)))
  
;; TESTS:
 (check-equal? (solve_quadratic_equation 1 2 1) -1 "Solution of the quadratic equation with a=1,b=2,c=3 is -1 ")
 (check-equal? (solve_quadratic_equation 3 4 5) #i-0.6666666666666666+1.1055415967851332i "Solution of the quadratic equation with a=4,b=5,c=6 is #i-0.6666666666666666+1.1055415967851332i")
