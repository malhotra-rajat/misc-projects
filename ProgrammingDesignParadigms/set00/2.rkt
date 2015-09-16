;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |2|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; Goal: An expression that tests if the result of 100/3 is greater than the result of (100 + 3) / (3 + 3 )

(require rackunit)

;; DATA DEFINITIONS: none

;; days: Number -> Number
;; GIVEN: Two Expressions
;; RETURNS: Expression that tests whether the first is greater than the second
;; EXAMPLES: 
;; (days 365)=31536000
;; (days 366)=31622400
;; DESIGN STRATEGY: Domain Knowledge
(define (compare exp1 exp2)
  (>  exp1 exp2))

;; TESTS:
(check-equal? (compare (/ 100 3) (/ (+ 100 3) (+ 3 3))) true "First expression is greater")
