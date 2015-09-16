;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |9|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")))))
(require rackunit)

;; DATA DEFINITIONS: none

; is-even? : Number -> Boolean
; GIVEN: a number
; RETURNS: true if the number is even, false otherwise
; Examples:
; (even? 4)  =>  true 
; (even? 5)  =>  false

(define (is-even? number)
  (= (remainder number 2) 0))

;; TESTS:
 (check-equal? (is-even? 4) true "4 is even, so true")
 (check-equal? (is-even? 5) false "5 is odd, so false")
