;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |10|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")))))
(require rackunit)

;; DATA DEFINITIONS: none

; sum-larger-two? : Number Number Number -> Number
; GIVEN: three numbers
; RETURNS: sum of the two larger numbers
; Examples:
; (sum-larger-two 1 2 3)  =>  5 
; (sum-larger-two 9 2 6)  =>  15 

(define (sum-larger-two num1 num2 num3)
  (cond [(and (< num1 num2) (< num1 num3)) (+ num2 num3)]
        [(and (< num2 num3) (< num2 num1)) (+ num1 num3)]
        [else (+ num1 num2)]))

;; TESTS:
(check-equal? (sum-larger-two 1 2 3) 5 "The sum of 3 and 2 is 5")
(check-equal? (sum-larger-two 9 2 6) 15 "The sum of 9 and 6 is 15")
