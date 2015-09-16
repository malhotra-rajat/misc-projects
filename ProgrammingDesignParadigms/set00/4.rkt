;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |4|) (read-case-sensitive #t) (teachpacks ((lib "image.rkt" "teachpack" "2htdp"))) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ((lib "image.rkt" "teachpack" "2htdp")))))
(require rackunit)

;; DATA DEFINITIONS: none

; tip : Number Number -> Number
; GIVEN: the amount of the bill in dollars and the
; percentage of tip
; RETURNS: the amount of the tip in dollars.
; Examples:
; (tip 10 0.15)  => 1.5
; (tip 20 0.17)  => 3.4
;; DESIGN STRATEGY: Domain Knowledge
(define (tip amount perc)
  ( * perc amount))


;; TESTS:
(check-equal? (tip 10 0.15) 1.5 "Tip for a bill of $10 at 15% should be 1.5")
(check-equal? (tip 20 0.17) 3.4 "Tip for a bill of $20 at 17% should be 3.4")
