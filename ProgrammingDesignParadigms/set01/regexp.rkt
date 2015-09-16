;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname regexp) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require "extras.rkt")

(provide initial-state)
(provide next-state)
(provide accepting-state?)
(provide error-state?)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; DATA DEFINITIONS:

; A state is one of:

; -- "state 1"        interp: this is the initial state
; -- "state 2"        interp: this is the state after the user presses "a" or "b" when on state 1
; -- "state 3"        interp: this is the state after the user presses "c" when on state 2 or state 1
; -- "state 4"        interp: this is the state after the user presses "d" when on state 3
; -- "state 5"        interp: this is the state after the user presses "e" when on state 4 or state 3 
; -- "error state"    interp: this is state which occurs when the machine encounters a bad letter 
;                             or a letter out of sequence

; TEMPLATE:
; state-fn : state -> ??
;  (define (state-fn state)
;   (cond
;    [(string=? state "state 1")...]
;    [(string=? state "state 2")...]
;    [(string=? state "state 3")...]
;    [(string=? state "state 4")...]
;    [(string=? state "state 5")...]
;    [(string=? state "error state")...]))

; A MyKeyEvent is a KeyEvent that is one of
; -- "a"     interp: the user pressed 'a' on the keyboard
; -- "b"     interp: the user pressed 'b' on the keyboard
; -- "c"     interp: the user pressed 'c' on the keyboard
; -- "d"     interp: the user pressed 'd' on the keyboard
; -- "e"     interp: the user pressed 'e' on the keyboard

; TEMPLATE:
; (define (my-key-event-fn key-event)
;  (cond
;    [(key=? key-event "a")...]
;    [(key=? key-event "b")...]
;    [(key=? key-event "c")...]
;    [(key=? key-event "d")...]
;    [(key=? key-event "e")...]

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; initial-state : Number -> State
; GIVEN: a number
; RETURNS: a representation of the initial state
; of your machine.  The given number is ignored.
; EXAMPLES: (initial-state 1) => "state 1"
;           (initial-state 2) => "state 1"
; STRATEGY: Domain Knowledge
(define (initial-state num)
  "state 1")

; next-state : State MyKeyEvent -> State
; GIVEN: a state of the machine
; RETURNS: the state that should follow the given key event.  A key
; event that is to be discarded should leave the state unchanged.
; EXAMPLES: 
;  (next-state "state 1" "a") => "state 2"
;  (next-state "state 1" "c") => "state 3"
;  (next-state "state 4" "d") => "state 4"   
; STRATEGY: Domain Knowledge
(define (next-state state my-key-event)
  (cond 
    [ (> (string-length my-key-event) 1) "Key Length greater than 1"]
    
    [(string=? state "state 1") (if (or (string=? my-key-event "a") (string=? my-key-event "b")) 
                                        "state 2" (if(string=? my-key-event "c") "state 3" "error state" ))]
    
    [(string=? state "state 2") (if (or(string=? my-key-event "a")(string=? my-key-event "b")) 
                                        "state 2" (if (string=? my-key-event "c") "state 3" "error state"))]
        
    [(string=? state "state 3") (if (string=? my-key-event "d") "state 4" 
                                        (if (string=? my-key-event "e") "state 5" "error state"))]
        
    [(string=? state "state 4") (if (string=? my-key-event "d") "state 4" 
                                        (if (string=? my-key-event "e") "state 5" "error state"))]
        
    [(string=? state "state 5") (if (string=? my-key-event "d") "error state" "error state")]
    
    [(string=? state "error state") (if (string=? my-key-event "d") "error state" "error state")]))

; accepting-state? : State -> Boolean
; GIVEN: a state of the machine
; RETURNS: true iff the given state is a final (accepting) state
; EXAMPLES: 
;  (accepting-state? "state 3") => true
;  (accepting-state? "state 5") => false 
; STRATEGY: Domain Knowledge
(define (accepting-state? state)
  (or (string=? state "state 1")
      (string=? state "state 2")
      (string=? state "state 3")
      (string=? state "state 4")))


; error-state? : State -> Boolean
; GIVEN: a state of the machine
; RETURNS: true iff the string seen so far does not match the specified
; regular expression and cannot possibly be extended to do so.
; EXAMPLES: 
;  (error-state? "state 1") => false
;  (error-state? "error state") => true
; STRATEGY: Domain Knowledge      
(define (error-state? state)
  (string=? state "error state"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TESTS

(define-test-suite reg-exp-tests
  (check-equal? (initial-state 1)"state 1" "Initial state should be state 1")
  
  (check-equal? (accepting-state? "state 1") true "state 1 is an accepting state")
  (check-equal? (accepting-state? "state 2") true "state 2 is an accepting state")
  (check-equal? (accepting-state? "state 3") true "state 3 is an accepting state")
  (check-equal? (accepting-state? "state 4") true "state 4 is an accepting state")
  (check-equal? (accepting-state? "state 5") false "state 5 is not an accepting state")
  (check-equal? (accepting-state? "error state") false "error state is not an accepting state")
  
  (check-equal? (error-state? "state 1") false "state 1 is not an error state")
  (check-equal? (error-state? "state 2") false "state 2 is not an error state")
  (check-equal? (error-state? "state 3") false "state 3 is not an error state")
  (check-equal? (error-state? "state 4") false "state 4 is not an error state")
  (check-equal? (error-state? "state 5") false "state 5 is not an error state")
  (check-equal? (error-state? "error state") true "'error state' is an error state")
  
  (check-equal? (next-state "state 1" "aaa") "Key Length greater than 1" "Key lengths greater than 1 should be ignored")
  
  (check-equal? (next-state "state 1" "a") "state 2" "state 2 if input is 'a' at state 1")
  (check-equal? (next-state "state 1" "b") "state 2" "state 2 if input is 'b' at state 1")
  (check-equal? (next-state "state 1" "c") "state 3" "state 3 if input is 'c' at state 1")
  (check-equal? (next-state "state 1" "e") "error state" "error state if input is anything other than a, b or c at state 1")
  
  (check-equal? (next-state "state 2" "a") "state 2" "state 2 if input is 'a' at state 2")
  (check-equal? (next-state "state 2" "b") "state 2" "state 2 if input is 'b' at state 2")
  (check-equal? (next-state "state 2" "c") "state 3" "state 3 if input is 'c' at state 2")
  (check-equal? (next-state "state 2" "e") "error state" "error state if input is anything other than a, b or c at state 2")
  
  (check-equal? (next-state "state 3" "d") "state 4" "state 4 if input is 'd' at state 3")
  (check-equal? (next-state "state 3" "e") "state 5" "state 5 if input is 'e' at state 3")
  (check-equal? (next-state "state 3" "a") "error state" "error state if input is anything other than d or e at state 3")
  
  (check-equal? (next-state "state 4" "d") "state 4" "state 4 if input is 'd' at state 4")
  (check-equal? (next-state "state 4" "e") "state 5" "state 5 if input is 'e' at state 4")
  (check-equal? (next-state "state 4" "a") "error state" "error state if input is 'a' at state 4")
  
  (check-equal? (next-state "state 5" "e") "error state" "error state if any input is given at state 5")
  (check-equal? (next-state "error state" "e") "error state" "error state if any input is given at error state")
  
  (check-equal? (next-state "state 5" "d") "error state" "error state if any input is given at state 5")
  (check-equal? (next-state "error state" "d") "error state" "error state if any input is given at error state")
  (check-equal? (next-state "state 5" "5") "error state" "error state if any input is given at state 5")
  (check-equal? (next-state "error state" "6") "error state" "error state if any input is given at error state"))
(run-tests reg-exp-tests)
      
