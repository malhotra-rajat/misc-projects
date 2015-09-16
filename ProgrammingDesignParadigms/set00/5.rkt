;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |5|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")))))
(require rackunit)

;; DATA DEFINITIONS: none

; sq : Number  -> Number
; GIVEN: a number
; RETURNS: the square of the inputted number 
; Examples:
; (sq 10) => 100
; (sq 20) => 400
;; DESIGN STRATEGY: Domain Knowledge
(define (sq num)
  ( * num num))


;; TESTS:
(check-equal? (sq 10) 100 "Square of 10 is 100")
(check-equal? (sq 20) 400 "Square of 20 is 400")
