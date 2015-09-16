;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |3|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; Goal: convert farenheit to celsius

(require rackunit)

;; DATA DEFINITIONS: none

; f->c : Number -> Number
; GIVEN: a temperature in degrees Fahrenheit as an argument
; RETURNS: the equivalent temperature in degrees Celcius.
;; EXAMPLES:
; (f->c 32)  => 0
; (f->c 100) => 37.77777777777778
;; DESIGN STRATEGY: Domain Knowledge
(define (f->c f)
  (+ (* 5/9 f) -160/9))


;; TESTS:
(check-= (f->c 32) 0 0.01 "32 degrees Farenheit should be 0 degrees celsius")
(check-= (f->c 100) 37.77777777777778 0.01 "100 degrees Farenheit should be 37.77777777777778 degrees celsius")
