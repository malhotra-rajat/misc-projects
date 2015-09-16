;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname |1|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; Goal : compute the number of seconds in a leap year

(require rackunit)

;; constants
(define LEAP 366)

;; DATA DEFINITIONS: none

;; days: Number -> Number
;; GIVEN: Number of days in a year
;; RETURNS: Number of seconds in a year
;; EXAMPLES: 
;; (days 365)=31536000
;; (days 366)=31622400
;; DESIGN STRATEGY: Domain Knowledge

(define (days x)
   (* x (* 24 60 60)))

;; TESTS:
(check-equal? (days 365) 31536000 "Number of seconds in a normal year should be 31536000")
(check-equal? (days 366) 31622400 "Number of seconds in a leap year should be 31622400")
