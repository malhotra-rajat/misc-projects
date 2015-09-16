;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname snacks) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require "extras.rkt")

(provide initial-machine)
(provide machine-next-state)
(provide machine-chocolates)
(provide machine-carrots)
(provide machine-bank)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; DATA DEFINITIONS:

; costs of chocolate bar and carrot sticks
(define CHOC-BAR-COST 175)
(define CARROT-STICK-COST 70)

(define-struct machine (choc-bars carrot-sticks money-bank cust-money))

; A Machine is a (make-machine Number Number Number Number)
; choc-bars is the number of chocolate bars in the machine
; carrot-sticks is the number of carrot-sticks in the machine
; money-bank is the amount of money that the machine has kept from the customers' purchases
; cust-money is the amount that the customer puts in the machine.

; TEMPLATE:
; machine-fn : machine -> ??
; (define (machine-fn machine)
;  (...
;   (machine-choc-bars machine)
;   (machine-carrot-sticks machine)
;   (machine-money-bank machine)
;   (machine-cust-money machine)))


; A CustomerInput is one of
; -- a positive Number interp: insert the specified number of cents
; -- "chocolate"       interp: request a chocolate bar
; -- "carrots"         interp: request a package of carrot sticks
; -- "release"         interp: return all the coins that the customer has put in

; TEMPLATE:
; cust-input--fn : CustomerInput -> ??
; (define (customer-input-fn cust-input)
;  (cond
;   [(number? cust-input)...]
;   [(string=? cust-input "chocolate")...]
;   [(string=? cust-input "carrots")...]
;   [(string=? cust-input "release")...]))
  
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; initial-machine : Number Number -> Machine
; GIVEN: the number of chocolate bars and the number of packages of
; carrot sticks
; RETURNS: a machine loaded with the given number of chocolate bars and
; carrot sticks, with an empty bank.
; EXAMPLES: (initial-machine 40 40) => (make-machine 40 40 0 0)
;           (initial-machine 90 90)(make-machine 90 90 0 0)
; STRATEGY: Structural Composition
(define (initial-machine choc-bars carrot-sticks)
  (make-machine choc-bars carrot-sticks 0 0))

; machine-next-state : Machine CustomerInput -> Machine
; GIVEN: a machine state and a customer input
; RETURNS: the state of the machine that should follow the customer's input
; EXAMPLES: (machine-next-state (make-machine 24 24 50 0) 25) => (make-machine 24 24 50 25)
;           (machine-next-state (make-machine 24 24 50 60) "carrots") => make-machine 24 24 50 60)
; STRATEGY: Structural Decomposition
(define (machine-next-state machine customer-input)
  (cond 
    [(number? customer-input) 
     (make-machine (machine-choc-bars machine)
     (machine-carrot-sticks machine)(machine-money-bank machine)
     (+ customer-input (machine-cust-money machine)))] 

    [(string=? customer-input "chocolate") 
     (if (and (> (machine-choc-bars machine) 0) (>= (machine-cust-money machine) CHOC-BAR-COST))
       (make-machine (- (machine-choc-bars machine) 1) (machine-carrot-sticks machine)
       (+ (machine-money-bank machine) CHOC-BAR-COST) (- (machine-cust-money machine) CHOC-BAR-COST))
       (make-machine (machine-choc-bars machine)(machine-carrot-sticks machine) 
       (machine-money-bank machine) (machine-cust-money machine)))]

    [(string=? customer-input "carrots") 
     (if (and (> (machine-carrot-sticks machine) 0) (>= (machine-cust-money machine) CARROT-STICK-COST))
       (make-machine (machine-choc-bars machine) ( - (machine-carrot-sticks machine) 1)
       (+ (machine-money-bank machine) CARROT-STICK-COST) (- (machine-cust-money machine) CARROT-STICK-COST))
       (make-machine (machine-choc-bars machine)(machine-carrot-sticks machine) 
       (machine-money-bank machine) (machine-cust-money machine)))]
 
    [(string=? customer-input "release") 
     (if (> (machine-cust-money machine) 0) 
      (make-machine (machine-choc-bars machine) (machine-carrot-sticks machine) (machine-money-bank machine) 0)
      (make-machine (machine-choc-bars machine)(machine-carrot-sticks machine) 
      (machine-money-bank machine) (machine-cust-money machine)))]))

; machine-chocolates : Machine -> Number
; GIVEN: a machine state
; RETURNS: the number of chocolate bars left in the machine
; EXAMPLES: (machine-chocolates (make-machine 24 56 90 0)) => 24
;           (machine-chocolates (make-machine 34 56 90 0)) => 34
; STEATEGY: Structural Decomposition
(define (machine-chocolates machine)
  (machine-choc-bars machine))

; machine-carrots : Machine -> Number
; GIVEN: a machine state
; RETURNS: the number of packages of carrot sticks left in the machine
; EXAMPLES: (machine-carrots (make-machine 24 56 90 0)) => 56
;           (machine-carrots (make-machine 24 66 90 0)) => 66
; STEATEGY: Structural Decomposition

(define (machine-carrots machine)
  (machine-carrot-sticks machine))

; machine-bank : Machine -> Number
; GIVEN: a machine state
; RETURNS: the amount of money in the machine's bank, in cents
; EXAMPLES: (machine-bank (make-machine 24 56 90 0)) => 90
;           (machine-bank (make-machine 24 56 80 0)) => 80
; STEATEGY: Structural Decomposition

(define (machine-bank machine)
  (machine-money-bank machine))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TESTS

(define-test-suite machine-tests
  (check-equal? (machine-carrots (make-machine 24 56 90 0)) 56 
                "number of carrots should be 56 for make-machine 24 56 90 0")
  (check-equal? (machine-chocolates (make-machine 24 56 90 0)) 24 
                "number of chocolates should be 24 for make-machine 24 56 90 0")
  (check-equal? (machine-bank (make-machine 24 56 90 0)) 90 
                "money-bank amount should be 90 for make-machine 24 56 90 0")
  (check-equal? (initial-machine 40 40)(make-machine 40 40 0 0) 
                "initially the machine should have an empty bank and is loaded with 
                 the given amount of chocolates and carrot sticks")
  (check-equal? (machine-next-state (make-machine 24 24 50 0) 25)(make-machine 24 24 50 25) 
                "amount deposited by the customer should reflect in the cust-money field")
  (check-equal? (machine-next-state (make-machine 24 24 50 200) "chocolate")(make-machine 23 24 225 25)
                "no of chocolates in the machine should decrease by 1, 
                 customer money reduced by 175 and machine's money bank increased by 175")
  (check-equal? (machine-next-state (make-machine 24 24 50 200) "carrots")(make-machine 24 23 120 130) 
                "no of carrots in the machine should decrease by 1, 
                 customer money reduced by 70 and machine's money bank increased by 70")
  (check-equal? (machine-next-state (make-machine 24 24 50 60) "carrots")(make-machine 24 24 50 60)
                "nothing should happen if the customer hasn't deposited enough 
                 money to buy the carrot sticks")
  (check-equal? (machine-next-state (make-machine 24 24 50 60) "chocolate")(make-machine 24 24 50 60)
                "nothing should happen if the customer hasn't deposited enough money 
                 to buy the chocolate bar")
  (check-equal? (machine-next-state (make-machine 24 24 50 60) "release")(make-machine 24 24 50 0) 
                "cust-money field should be zeroed after customer presses release")
  (check-equal? (machine-next-state (make-machine 24 24 50 0) "release")(make-machine 24 24 50 0)
                "nothing should happen if the money has no money to release"))

(run-tests machine-tests)

