;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |3|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require 2htdp/image)
(require "extras.rkt")

(provide make-diff-exp) 
(provide diff-exp-rand1) 
(provide diff-exp-rand2)
(provide make-mult-exp)
(provide mult-exp-rand1)
(provide mult-exp-rand2)
(provide expr-to-image)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; DATA DEFINITIONS:

(define-struct diff-exp (rand1 rand2))
(define-struct mult-exp (rand1 rand2))

; An Expr is one of
; -- (make-diff-exp Number Number)
; -- (make-mult-exp Number Number)
; Interpretation: a diff-exp represents a difference,
; and a mult-exp represents a multiplication

; TEMPLATE:
; expr-fn : expr -> ??
;(define (expr-fn expr)
; (cond
;   [(diff-exp? expr) true ...]
;   [(mult-exp? expr) true ...]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; expr-to-image : Expr Boolean -> Image
; GIVEN: A difference expression (diff-exp) or multiplication expression (mult-exp) and a Boolean Value
; RETURNS: An image which is the mathematical representation of the multiplication and difference operation.
;          The numbers in the image are rand1 and rand2 fields in the diff-exp and mult-exp structures.
;          The image is rendered as a prefix or an infix expression, based on the Boolean value.
; EXAMPLES: 
; (expr-to-image (make-mult-exp 5 6) true) => an image of (5 * 6)
; (expr-to-image (make-mult-exp 5 6) false) => an image of (* 5 6)
; (expr-to-image (make-diff-exp 5 6) true) => an image of (5 - 6)
; (expr-to-image (make-diff-exp 5 6) false) => an image of (- 5 6)
; STRATEGY: Structural Decomposition
(define (expr-to-image expr flag)
  (cond [(diff-exp? expr) (if (boolean=? flag true) 
          (text (string-append "(" (number->string (diff-exp-rand1 expr)) " - " 
                               (number->string (diff-exp-rand2 expr)) ")") 11 "black")
          (text (string-append "(- " (number->string (diff-exp-rand1 expr)) " " 
                               (number->string (diff-exp-rand2 expr)) ")") 11 "black"))]
        
        [(mult-exp? expr) (if (boolean=? flag true)
          (text (string-append "(" (number->string (mult-exp-rand1 expr)) " * " 
                               (number->string (mult-exp-rand2 expr)) ")") 11 "black")
          (text (string-append "(* " (number->string (mult-exp-rand1 expr)) " " 
                               (number->string (mult-exp-rand2 expr)) ")") 11 "black"))]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TESTS:

; defining correct images for use in test cases
(define correct-mult-exp-inifix-image (text "(5 * 6)" 11 "black"))
(define correct-mult-exp-prefix-image (text "(* 5 6)" 11 "black"))
(define correct-diff-exp-inifix-image (text "(5 - 6)" 11 "black"))
(define correct-diff-exp-prefix-image (text "(- 5 6)" 11 "black"))

(define-test-suite exp-to-image-tests
 (check-equal? (expr-to-image (make-mult-exp 5 6) true) correct-mult-exp-inifix-image "Images not equal")
 (check-equal? (expr-to-image (make-mult-exp 5 6) false) correct-mult-exp-prefix-image "Images not equal")
 (check-equal? (expr-to-image (make-diff-exp 5 6) true) correct-diff-exp-inifix-image "Images not equal")
 (check-equal? (expr-to-image (make-diff-exp 5 6) false) correct-diff-exp-prefix-image "Images not equal"))

(run-tests exp-to-image-tests)

