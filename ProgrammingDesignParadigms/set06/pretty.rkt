;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname pretty) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require "extras.rkt")
(require rackunit)
(require rackunit/text-ui)


(provide expr-to-strings
         make-sum-exp
         make-mult-exp
         sum-exp-exprs
         mult-exp-exprs)
; CONSTANTS:
(define INDENT-WIDTH 3)

(define-struct sum-exp (exprs))
(define-struct mult-exp (exprs))

; An Expr is one of
; -- Number
; -- (make-sum-exp NELOExpr)
;  interp:
;   exprs is a list of non empty list of Expr which represents a sum
; -- (make-mult-exp NELOExpr)
;   exprs is a list of non empty list of Expr which represents a multiplication

; expr-fn : Expr -> ??
;(define (expr-fn e)
;  (cond
;    [(number? e) ...]
;    [(sum-exp? e) (... 
;                  (neloe-fn (sum-exp-exprs e)))]
;    [(mult-exp? e)(...
;                   (neloe-fn (mult-exp-exprs e)))]))

; A LOExpr is one of
; -- empty                interp: the LOExpr has no element
; -- (cons Expr LOExpr)   interp: LOExpr has at least one element.
; template:
; loexpr-fn : LOExpr -> ??
;(define (loexpr-fn loe)
;  (cond
;    [(empty? loe) ...]
;    [else (...
;           (expr-fn (first loe))
;           (loexpr-fn (rest loe)))]))
;
; Definition 1
; NELOExpr = (cons Expr LOExpr)
; template:
; neloe-fn : NELOExpr -> ??
;(define (neloe-fn neloe)
;  (... (expr-fn (first neloe))
;       (loexpr-fn (rest neloe))))

; Definition 2
; NELOExpr is one of:
; -- (cons Expr empty)      interp: the NELOExpr has only one Expr
; -- (cons Expr NELOExpr)   interp: the NELOExpr has more than one element.
; template:
; neloe-fn : NELOExpr -> ??
;(define (neloe-fn neloe)
;  (cond
;    [(empty? (rest neloe)) (...(first neloe))]
;    [(cons? (rest neloe))
;     (...(expr-fn (first neloe))
;         (neloe-fn (rest neloe)))]))
;
; Definition 1
; NELOS is a (cons String LOS)
; template:
; nelos-fn : NELOS -> ??
;(define (nelos-fn los)
;  (... (first los) (los-fn (rest los))))

; Definition 2
; NELOS is one of:
; -- (cons String empty)     interp: the NELOS has only one String
; -- (cons String NELOS)     interp: the NELOS has more than one element.
; nelos-fn : NELOS -> ??
;(define (nelos-fn nelos)
;  (cond
;    [(empty? (rest nelos)) (...(first neloe))]
;    [(cons? (rest nelos))
;     (...(first nelos) (nelos-fn (rest nelos)))]))

; A ListOf<String> (LOS) is either
; -- empty              interp: the LOS has no elements in it.
; -- (cons String LOS)  interp: the LOS has at least one element.
; template:
; los-fn : LOS -> ??
;(define (los-fn los)
;  (cond
;    [(empty? los) ...]
;    [else (...
;           (first los) (los-fn (rest los)))]))

; A Nat is a natural number.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; single-line : Expr -> String
; GIVEN: an expr.
; RETURNS: the string of the expr in one line
; EXAMPLES:
;   (single-line (make-sum-exp (list 2 3 4))) => "(+ 2 3 4)"
; STRATEGY: Function Composition.
(define (single-line e)
  (substring (single-line-helper e)
             1 (string-length (single-line-helper e))))

; single-line-helper : Expr -> String
; GIVEN: an expr
; RETURNS: the string of the expr in one line. The return value
; is not process. It has a whitespace in front of it.
; EXAMPLES:
;   (single-line (make-sum-exp (list 2 3 4))) => " (+ 2 3 4)"
; STRATEGY: Struct Decomp on e : Expr.

(define (single-line-helper e)
  (cond
    [(number? e) (string-append " " (number->string e))]
    [(sum-exp? e) (single-sum-exp e)]
    [(mult-exp? e) (single-mult-exp e)]))

; single-sum-exp : Sum-Exp -> String
; GIVEN: a sum-exp.
; RETURNS: the string representation of the sum-exp.
; EXAMPLES:
;   (single-sum-exp (make-sum-exp (list 1 2 3))) => " (+ 1 2 3)"
; STRATEGY: Struct Decomp on e : Sum-Exp.
(define (single-sum-exp e)
  (string-append " (+" (single-sum-exprs (sum-exp-exprs e))))

; single-sum-exprs : NELOExpr -> String
; GIVEN: an non empty list of expression
; RETURNS: a string representation of all the elements in the list
; with a right parenthesis at the end.
; EXAMPLES:
;   (single-sum-exprs (list 1 2 3)) => " 1 2 3)"
; STRATEGY: Structural Decomp on neloe : NELOExpr. (using second template)
(define (single-sum-exprs neloe)
  (cond
    [(empty? (rest neloe))
     (string-append (single-line-helper (first neloe)) ")")]
    [(cons? (rest neloe))
     (string-append (single-line-helper (first neloe))
                    (single-sum-exprs (rest neloe)))]))

; single-mult-exp : Mult-Exp -> String
; GIVEN: a mult-exp.
; RETURNS: the string representation of the mult-exp.
; EXAMPLES:
;   (single-mult-exp (make-mult-exp (list 1 2 3))) => " (* 1 2 3)"
; STRATEGY: Struct Decomp on e : Mult-Expr.
(define (single-mult-exp e)
  (string-append " (*" (single-mult-exprs (mult-exp-exprs e))))

; single-mult-exprs : NELOExpr -> String
; GIVEN: an non empty list of expression
; RETURNS: a string representation of all the elements in the list
; with a right parenthesis at the end.
; EXAMPLES:
;   (single-mult-exprs (list 1 2 3)) => " 1 2 3)"
; STRATEGY: Structural Decomp on neloe : NELOExpr. (using second template)

(define (single-mult-exprs neloe)
  (cond
    [(empty? (rest neloe))
     (string-append (single-line-helper (first neloe)) ")")]
    [(cons? (rest neloe))
     (string-append (single-line-helper (first neloe))
                    (single-mult-exprs (rest neloe)))]))

; expr-to-strings : Expr Nat -> ListOf<String>
; GIVEN: An expression and a width
; RETURNS: A representation of the expression as a sequence of lines, with
; each line represented as a string of length not greater than the width.
; EXAMPLES:
;   see tests.
; STRATEGY: Function Composition.
(define (expr-to-strings e w)
  (expr-to-strings-helper e w 0 0))


; expr-to-strings-helper : Expr Nat Number Number -> ListOf<String>
; GIVEN: An expression e, width restriction.
; WHERE: the expression has been indented d times so far. and there are
; l close parenthesis at the end of the expression.
; RETURNS: A representation of the expression as a sequence of lines, with
; each line represented as a string of length not greater than the width.
; EXAMPLES:
;   (expr-to-strings-helper expr1 10 0 0) => expr-1-10w
;   (expr-to-strings-helper expr2 20 0 0) => expr-1-20w
; STRATEGY: Struct Decomp on e : Expr.
(define (expr-to-strings-helper e w d l)
  (cond
    [(number? e) (render-number e w d l)]
    [(sum-exp? e) (render-sum-expr e w d l)]
    [(mult-exp? e)(render-mult-expr e w d l)]))


; render-number : Expr Nat Number Number -> ListOf<String>
; GIVEN: An expression e, width restriction
; WHERE: the expression has been indented d times so far. and there are
; l close parenthesis at the end of the expression.
; RETURNS: if the number can be put within width restriction, return a
; list of the given expression. If there is not enough space, send error
; information.
; EXAMPLES:
;   (render-number 999 10 0 0) => (list "999")
;   (render-number 999 3 0 0) => error incured
; STRATEGY: Function Composition
(define (render-number e w d l)
  (if (enough-space? e w d l)
      (list (string-append (make-string (* INDENT-WIDTH d) #\ ) 
                           (number->string e)))
      (error "NO SPACE")))


; render-sum-expr : Expr Nat Number Number -> ListOf<String>
; GIVEN: An expression e and width restriction.
; WHERE: the expression has been indented d times so far. and there are
;   l close parenthesis at the end of the expression.
; RETURNS: the expression represented by a list of strings.
; EXAMPLES:
;   (render-sum-expr (make-sum-exp (list 1 2 3) 10 0))
;        => (list "(+ 1 2 3)")
;   (render-sum-expr (make-sum-exp (list 1 2 3) 6 0))
;        => (list "(+ 1" "   2" "   3)")
; STRATEGY: Struct Decomp on e : Sum-Exp.
(define (render-sum-expr e w d l)
  (if (enough-space? e w d l)
      (list 
       (string-append 
        (make-string (* INDENT-WIDTH d) #\ )
        (single-line e)))
      (append
       (add-to-sum-head
        (append-to-tail
         (stack-line (sum-exp-exprs e) w (add1 d) l)) d))))

; enough-space? : Expr Nat Number Number -> Boolean
; GIVEN: An expression e and width restriction w, the number of indention d
;   and the number of right parenthesis at the end of the line.
; RETURNS: if the sum length of the expression, white space in front of it
; and right parenthesis behind it exceeds width, return false. else return
; true.
; EXAMPLES:
;   (single-line-length (make-mult-exp (list 1 2 3 4) 10 1 0)) => false
; STRATEGY: Function Composition.
(define (enough-space? e w d l)
  (<= (+ (* d INDENT-WIDTH)
         (+ (string-length (single-line e)) l))
      w))

; stack-line : NELOExpr Nat Number Number -> ListOf<String>
; GIVEN: a non empty list of expression e and restriction width.
; WHERE: each expression in the non empty list has been indented
;   d times so far. and there are l close parenthesis at the end
;   of the last expression.
; RETURNS: the expression represented by a list of strings.
; EXAMPLES:
;   (stack-line (list 1 9 8) 5 0 0) => (list "1" "9" "8")
;   (stack-line (list 1 9 8) 1 0 0) => ERROR
; STRATEGY: Struct Decomp on es : NELOExpr (using second template)
(define (stack-line es w d l)
  (cond
    [(empty? (rest es)) 
     (expr-to-strings-helper (first es) w d (add1 l))]
    [(cons? (rest es)) (append (expr-to-strings-helper (first es) w d 0)
                  (stack-line (rest es) w d l))]))

; append-to-tail : NELOS -> NELOS
; GIVEN: a list of strings.
; RETURNS: a list of strings with a close parenthesis adding
; to the end of the last element in the list.
; EXAMPLES:
;   (append-to-tail (list "1" "3" "4") => (list "1" "3" "4)")
; STRATEGY: Structural Composition on los : NELOS (using second template)
(define (append-to-tail los)
  (cond
    [(empty? (rest los)) (list (string-append (first los) ")"))]
    [(cons? (rest los)) (cons (first los)
                              (append-to-tail (rest los)))]))


; add-to-sum-head : LOS Number -> LOS
; GIVEN: a list of string
; WHERE: d is the number of indention for each element in the list 
; of string
; RETURNS: a list of string where "(+ " is added to the head of the
; frist string in the original list.
; EXAMPLES:
;   (add-to-sum-head (list "1" " 2" " 3)")) => (list "(+ 1" " 2" " 3)")
; STRATEGY: Function Composition.
(define (add-to-sum-head los d)
  (cons (string-append  (make-string (* d INDENT-WIDTH) #\ )
                        "(+ " 
                        (substring (first los) (* (add1 d) INDENT-WIDTH)))
        (rest los)))


; render-mult-expr : Expr Nat Number Number -> ListOf<String>
; GIVEN: An expression e and width restriction.
; WHERE: the expression has been indented d times so far. and there are
; l close parenthesis at the end of the expression.
; RETURNS: the mult expression represented by a list of strings.
; EXAMPLES:
;   (render-mult-expr (make-mult-exp (list 1 2 3) 10 0))
;        => (list "(* 1 2 3)")
;   (render-mult-expr (make-mult-exp (list 1 2 3) 6 0))
;        => (list "(+ 1" "   2" "   3)")
; STRATEGY: Struct Decomp on e : Mult-Exp.
(define (render-mult-expr e w d l)
  (if (enough-space? e w d l)
      (list 
       (string-append 
        (make-string (* INDENT-WIDTH d) #\ )
        (single-line e)))
      (append
       (add-to-mult-head 
        (append-to-tail
         (stack-line (mult-exp-exprs e) w (add1 d) l)) d))))

; add-to-mult-head : LOS Number -> LOS
; GIVEN: a list of string
; WHERE: d is the number of indention for each element in the list 
; of string
; RETURNS: a list of string where "(* " is added to the head of the
; frist string in the original list.
; EXAMPLES:
;   (add-to-sum-head (list "1" " 2" " 3)")) => (list "(+ 1" " 2" " 3)")
; STRATEGY: Function Composition.
(define (add-to-mult-head los d)
  (cons (string-append  (make-string (* d INDENT-WIDTH) #\ )
                        "(* " 
                        (substring (first los) (* (add1 d) INDENT-WIDTH)))
        (rest los)))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; tests:
; examples for tests

(define expr1 (make-sum-exp (list 22 333 44)))

(define expr2 (make-sum-exp
               (list
                (make-mult-exp (list 22 3333 44))
                (make-mult-exp
                 (list
                  (make-sum-exp (list 66 67 68))
                  (make-mult-exp (list 42 43))))
                (make-mult-exp (list 77 88)))))

(define expr3 (make-mult-exp
               (list
                (make-mult-exp (list 10 20 30 40 50)))))

(define expr-1-15w (list "(+ 22 333 44)"))
(define expr-1-10w (list "(+ 22" "   333" "   44)"))
(define expr-2-100w 
  (list "(+ (* 22 3333 44) (* (+ 66 67 68) (* 42 43)) (* 77 88))"))
(define expr-2-50w
  (list "(+ (* 22 3333 44)"
        "   (* (+ 66 67 68) (* 42 43))"
        "   (* 77 88))"))
(define expr-2-20w
  (list "(+ (* 22 3333 44)"
        "   (* (+ 66 67 68)"
        "      (* 42 43))"
        "   (* 77 88))"))
(define expr-2-15w
  (list "(+ (* 22"
        "      3333"
        "      44)"
        "   (* (+ 66"
        "         67"
        "         68)"
        "      (* 42"
        "         43))"
        "   (* 77 88))"))

(define expr-3-10w
  (list "(* (* 10"
        "      20"
        "      30"
        "      40"
        "      50))"))

(define-test-suite expr-to-strings-tests
  
  ; call expr-to-strings with expr1 and 15 as width should return expr-1-15w
  (check-equal? (expr-to-strings expr1 15)
                expr-1-15w
                "expr-to-strings error! error occurs when width=15 for expr1")
  
  ; call expr-to-strings with expr1 and 10 as width should return expr-1-10w
  (check-equal? (expr-to-strings expr1 10)
                expr-1-10w
                "expr-to-strings error! error occurs when width=10 for expr1")
  
  ; call expr-to-strings with expr2 and 100 as width should return expr-2-100w
  (check-equal? (expr-to-strings expr2 100)
                expr-2-100w
                "expr-to-strings error! error occurs when width=100 for expr2")
  
  ; call expr-to-strings with expr2 and 50 as width should return expr-2-50w
  (check-equal? (expr-to-strings expr2 50)
                expr-2-50w
                "expr-to-strings error! error occurs when width=50 for expr2")
  
  ; call expr-to-strings with expr2 and 20 as width should return expr-2-20w
  (check-equal? (expr-to-strings expr2 20)
                expr-2-20w
                "expr-to-strings error! error occurs when width=20 for expr2")
  
  ; call expr-to-strings with expr2 and 15 as width should return expr-2-15w
  (check-equal? (expr-to-strings expr2 15)
                expr-2-15w
                "expr-to-strings error! error occurs when width=15 for expr2")
  
  ; call expr-to-strings with expr3 and 10 as width should return expr-3-10w
  (check-equal? (expr-to-strings expr3 10)
                expr-3-10w
                "expr-to-strings error! error occurs when width=10 for expr3"))

(define-test-suite error-tests
  
  ; Print Expr1 of width 2 will cause an error
  (check-error (expr-to-strings expr1 2)
               "an error will occur for expr1 when width=2")
  
  ; Print Expr2 of width 5 will cause an error
  (check-error (expr-to-strings expr2 5)
               "an error will occur for expr2 when width=5")
  
  ; Print Expr3 of width 4 will cause an error
  (check-error (expr-to-strings expr3 4)
               "an error will occur for expr3 when width=4"))

(run-tests expr-to-strings-tests)
(run-tests error-tests)