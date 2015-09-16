;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname outlines) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t write repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require "extras.rkt")

(provide
 nested-to-flat
 flat-rep?)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; CONSTANTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define ONE 1)        
(define TWO 2)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; DATA DEFINITIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; An Sexp is one of the following
; -- a String         interp: a string
; -- a Nat            interp: a natural number
; -- a ListOfSexp     interp: a list of s-expressions

; A Nat is a natural number (1,2,3...

; TEMPLATE:
; sexp-fn : Sexp -> ??
;(define (sexp-fn sexp)
;  (cond
;   [(string? sexp) ...]
;   [(number? sexp) ...]
;   [(cons? sexp) (... (lst-sexp-fn sexp)))


; A ListOfSexp is one of
; -- empty                      interp: the list is empty 
; -- (cons Sexp ListOfSexp)     interp: the list contains at least one
;                                       s-expression

; TEMPLATE:
; lst-sexp-fn : ListOfSexp -> ??
;(define (lst-sexp-fn lst-sexp)
;  (cond
;   [(empty? lst-sexp) ...]
;   [else (...
;          (sexp-fn (first lst-sexp))
;          (lst-sexp-fn (rest lst-sexp)))]))


; A NestedRep is a Sexp that is one of
; -- Sexp                    interp: an sexp which is a string 
; -- (cons Sexp NestedRep)   interp: a list of sexp (string) and a nestedrep


; TEMPLATE: 
; nested-rep-fn : NestedRep -> ??
;(define (nested-rep-fn nested-rep)
;  (cond
;    [(empty? nested-rep) ...]
;    [else (...
;           (sexp-fn (first nested-rep))
;           (nested-rep-fn (rest nested-rep)))]))


; A FlatRepListItem is a 
; -- (list ListOfSexp Sexp)
; interp: 
; a FlatRepListItem is a list containing exactly 2 elements
; -- the first one is a list of natural numbers
; -- the second one is a string


; TEMPLATE:
; flat-rep-list-item-fn : FlatRepListItem -> ??
;(define (flat-rep-list-item-fn litem)
;  (cond
;    [(empty? litem) ...]
;    [else (...
;           (lst-sexp-fn (first litem))
;           (sexp-fn (rest litem))]))

; A FlatRep is a Sexp that is one of
; -- FlatRepListItem                interp: an sexp which is a FlatRepListItem 
; -- (cons FlatRepListItem FlatRep) interp: a list of FlatRepListItem

; TEMPLATE:
; flat-rep-fn : FlatRep -> ??
;(define (flat-rep-fn flat-rep)
;  (cond
;   [(empty? flat-rep) ...]
;   [else (...
;          (flat-rep-list-item-fn (first (flat-rep)))
;          (flat-rep-fn (rest flat-rep)))]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Example for testing
(define nested-list
  '(("The first section"
     ("A subsection with no subsections")
     ("Another subsection"
      ("This is a subsection of 1.2")
      ("This is another subsection of 1.2"))
     ("The last subsection of 1"))
    ("Another section"
     ("More stuff")
     ("Still more stuff"))))

(define flat-rep-list
  '(((1) "The first section")
    ((1 1) "A subsection with no subsections")
    ((1 2) "Another subsection")
    ((1 2 1) "This is a subsection of 1.2")
    ((1 2 2) "This is another subsection of 1.2")
    ((1 3) "The last subsection of 1")
    ((2) "Another section")
    ((2 1) "More stuff")
    ((2 2) "Still more stuff")))

(define correct-adjacent-sections-list '((1) (1 1) (1 2) (1 2 1)
                                             (1 2 2) (1 3) (2) (2 1) (2 2)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; FUNCTION DEFINITIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; nested-to-flat : NestedRep -> FlatRep
; GIVEN: the representation of an outline as a nested list
; RETURNS: the flat representation of the outline
; EXAMPLES: (nested-to-flat nested-list) => flat-rep-list
; STRATEGY: Function Composition
(define (nested-to-flat nested-rep)
  (nested-sublist-to-flat nested-rep empty ONE))

; nested-sublist-to-flat : NestedRep LON Nat -> FlatRep
; GIVEN: a nested list, a section number (natural number) of the section and
; the old which is a list of natural numbers numbering the previous 
; section.
; WHERE: old list is empty initially and section is ONE
; RETURNS: the flat representation of the nested list
; EXAMPLES: (nested-sublist-to-flat nested-list ONE empty) => flat-rep-list
; STRATEGY: Struct Decomp on nested-rep: NestedRep
(define (nested-sublist-to-flat lnr old section)
  (cond
    [(empty? lnr) empty]
    [else (append (nested-sublist-to-flat-helper (first lnr) old section)
                  (nested-sublist-to-flat (rest lnr) old (add1 section)))]))

; nested-sublist-to-flat-helper : NestedRep LON Nat -> FlatRep
; GIVEN: a list of flat rep item, a section number (natural number)
; of the section and the old which is a list of natural numbers numbering
; the previous section.
; WHERE: old list is empty initially and section is ONE
; RETURNS:  the flat representation of flat item of the list following it.
; EXAMPLES:
;(nested-sublist-to-flat-helper 
; '("The first section"
;   ("A subsection with no subsections")
;   ("The last subsection of 1")) empty 1) =>
;'(((1) "The first section")
;  ((1 1) "A subsection with no subsections")
;  ((1 2) "The last subsection of 1"))
; STRATEGY: Function Composition.
(define (nested-sublist-to-flat-helper fr old section)
  (cons (list (make-title old section) (first fr))
        (nested-sublist-to-flat (rest fr) (make-title old section) ONE)))

; make-title : LON Nat -> LON
; GIVEN: a list of number old, numbering the previous section,
; and a section number (natrual number) of the section.
; RETURNS: a list of number which is a new section number.
; EXAMPLES: (make-title (list 1 2) 1) => '(1 2 1)
; STRATEGY: Function composition.
(define (make-title old i)
  (append old (list i)))

; flat-rep? : Sexp -> Boolean
; GIVEN: An Sexp
; RETURNS: true iff it is the flat representation of some outline
; EXAMPLES: (flat-rep? flat-rep-list) => true
; STRATEGY : Function Composition
(define (flat-rep? s-exp)
  (and (flat-rep-format-correct? s-exp)
       (adjacent-sections-correct? (get-lon s-exp))))


; flat-rep-format-correct? : Sexp -> Boolean
; GIVEN: An Sexp
; RETURNS: true iff the Sexp is in the correct format of a flat 
; representation, this function does not check correct adjacent headers
; EXAMPLES: (flat-rep-format-correct? flat-rep-list) => true
; STRATEGY : Struct Decomp on s-exp: Sexp
(define (flat-rep-format-correct? s-exp)
  (cond
    [(string? s-exp) false]
    [(number? s-exp) false]
    [(cons? s-exp) (flat-rep-format-correct-helper? s-exp)]))

; TEST

(define-test-suite flat-rep-format-correct?-tests
  
  (check-equal? (flat-rep-format-correct? "abc") false
                "the function should return false if input is a 
              string")
  
  (check-equal? (flat-rep-format-correct? 5) false
                "the function should return false if input is a 
              number"))

(run-tests flat-rep-format-correct?-tests)

; flat-rep-format-correct-helper? : ListOfSexp -> Boolean
; GIVEN: An list of s-expressions
; RETURNS: true iff the ListOfSexp is in the correct format of a flat 
; representation, this function does not check correct adjacent headers
; EXAMPLES: (flat-rep-format-correct-helper? flat-rep-list) => true
; STRATEGY : Struct Decomp on los : ListOfSexp
(define (flat-rep-format-correct-helper? los)
  (cond
    [(empty? los) true]
    [else (and 
           (cons? (first los))
           (flat-rep-list-item? (first los))
           (flat-rep-format-correct-helper? (rest los)))]))


; flat-rep-list-item? : ListOfSexp -> Boolean
; GIVEN: A list of sexps
; RETURNS: true iff the list item in a flat rep list is in the correct
; format
; EXAMPLES: (flat-rep-list-item? (list (list 1 1) "Section 1.1")) => true
; STRATEGY : Function Composition
(define (flat-rep-list-item? litem)
  (if (= TWO (length litem))
      (and 
       (nat? (first litem))
       (string? (second litem)))
      false))

(check-equal? (flat-rep-format-correct? (list (list 88) "dasasd" "dasd"))
              false
              "the function should return false if input contains 3 items") 


; get-lon : Sexp -> ListOfSexp
; GIVEN: An Sexp
; RETURNS: The list of first items of the Sexp
; EXAMPLES: (get-lon flat-rep-list) =>
; ((1) (1 1) (1 2) (1 2 1) (1 2 2) (1 3) (2) (2 1) (2 2))
; STRATEGY : HOFC
(define (get-lon s-exp)
  (map
   ; Sexp -> Sexp
   ; GIVEN : an s-exp
   ; RETURNS : the first item of the Sexp
   (lambda (lon)
     (first lon))
   s-exp))

; adjacent-sections-correct? : ListOfSexp -> Boolean
; GIVEN: a list of s-expressions
; RETURNS: true iff the input list starts from (1) and all the adjacent
; sections are in the correct order
; EXAMPLES: (adjacent-sections-correct? correct-adjacent-sections-list)
;           => true
; STRATEGY : Struct Decomp on lon: ListOfSexp
(define (adjacent-sections-correct? lon)
  (cond
    [(empty? lon) false]
    [else
     (and (equal? (first lon) (list ONE))
          (adjacent-sections-correct-helper? (rest lon) (list ONE)))]))

; TEST
(check-equal? (adjacent-sections-correct? empty) false
              "an empty input list should return false")

; adjacent-sections-correct-helper? : ListOfSexp ListOfSexp -> Boolean
; GIVEN: a list of s-exp (natural numbers) and the previous list of s-exp
; in the whole list
; RETURNS: true iff all the adjacent sections are in the correct order
; EXAMPLES: (adjacent-sections-correct-helper? 
;              (rest correct-adjacent-sections-list) (list ONE)) => true
; STRATEGY : Struct Decomp on lon: ListOfSexp
(define (adjacent-sections-correct-helper? lon lon-old)
  (cond
    [(empty? lon) true]
    [else
     (and 
      (or
       (equal-length-next-greater-by-one? (first lon) lon-old)
       (smaller-length-next-correct? (first lon) lon-old)
       (greater-length-next-correct? (first lon) lon-old))
      (adjacent-sections-correct-helper? (rest lon) (first lon)))]))


; equal-length-next-greater-by-one? : ListOfSexp ListOfSexp -> Boolean
; GIVEN: two list of s-exp (natural numbers)
;        which are lists of natural numbers
; RETURNS: true iff the length of the two are equal and the last 
; item of the second list (l-next) is greater than the last item
; of the first list (l) by one
; EXAMPLES: (equal-length-next-greater-by-one? '(2) '(1)) => true
; STRATEGY : Function Composition
(define (equal-length-next-greater-by-one? l-next l)
  (and (= (length l-next) (length l))
       (= ONE (- (find-last l-next)
                 (find-last l)))))

; smaller-length-next-correct? : ListOfSexp ListOfSexp -> Boolean
; GIVEN: two lists of s-expressions, which are lists of natural numbers
; RETURNS: true iff the length of the l-next is smaller than l by 1 
; and the last item of l-next is greater than the second last item of l
; by one
; EXAMPLES: (smaller-length-next-correct? '(2) '(1 1)) => true
; STRATEGY : Function Composition
(define (smaller-length-next-correct? l-next l)
  (and (= ONE (- (length l) (length l-next)))
       (= ONE (- (find-last l-next)
                 (find-last-but-one l)))))

; greater-length-next-correct? : ListOfSexp ListOfSexp -> Boolean
; GIVEN: two lists of s-expressions, which are lists of natural numbers
; RETURNS: true iff the length of the l-next is greater than l by 1,
; l-next with last item removed is equal to l and the last item of 
; l-next is 1
; EXAMPLES: (greater-length-next-correct? '(1 1) '(1)) => true
; STRATEGY : Function Composition
(define (greater-length-next-correct? l-next l)
  (and (= ONE (- (length l-next) (length l)))
       (= ONE (find-last l-next))
       (equal? l (last-removed l-next))))

; last-removed : ListOfSexp -> ListOfSexp
; GIVEN: a list of s-exp (natural numbers)
; RETURNS: the s-expression with the last item removed
; EXAMPLES: (last-removed '(1 1)) => '(1)
; STRATEGY : Struct Decomp on l: ListOfSexp
(define (last-removed l)
  (cond
    [(empty? (rest l)) empty]
    [else
     (cons (first l)
           (last-removed (rest l)))]))

; find-last : ListOfSexp -> ListOfSexp
; GIVEN:  a list of s-exp (natural numbers)
; RETURNS: the last item of the s-expression
; EXAMPLES: (find-last '(1 2)) => 2
; STRATEGY : Struct Decomp on lon: Sexp
(define (find-last lon)
  (cond
    [(empty? (rest lon)) (first lon)]
    [else 
     (find-last (rest lon))]))

; find-last-but-one : ListOfSexp -> ListOfSexp
; GIVEN:  a list of s-exp (natural numbers)
; RETURNS : the second-last item of the s-expression
; EXAMPLES: (find-last-but-one '(1 2 3 4)) => 3
; STRATEGY : Function Composition
(define (find-last-but-one lon)
  (find-last-but-one-helper lon (length lon)))

; find-last-but-one-helper : ListOfSexp Number -> ListOfSexp
; GIVEN: an list of s-expressions and the length of it
; WHERE: len-inv is initally the length of the list, but it is
; decremented by 1 as the function calls itself again
; RETURNS : the second last item of the s-expression
; EXAMPLES: (find-last-but-one-helper '(1 2 3) 3) => 2
; STRATEGY : Struct Decomp on lon: ListOfSexp
(define (find-last-but-one-helper lon len-inv)
  (cond
    [(empty? lon) empty]
    [else
     (if (= TWO len-inv) 
         (first lon)
         (find-last-but-one-helper (rest lon) (- len-inv ONE)))]))

; TEST

(check-equal? (find-last-but-one-helper empty 1) empty
              "if lon is empty, then empty should be returned
               from this function")

; nat? : Sexp -> Boolean
; GIVEN : an s-expression
; RETURNS: true iff the s-expression is a list of natural numbers
; EXAMPLES: (nat? '(1 2)) => true
; STRATEGY : Struct Decomp on lon: Sexp
(define (nat? lon)
  (cond
    [(empty? lon) true]
    [else
     (and (integer? (first lon))
          (positive? (first lon))
          (nat? (rest lon)))]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; TESTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define-test-suite function-tests
  
  (check-equal?
   (nested-to-flat
    '(("The first section"
       ("A subsection with no subsections")
       ("Another subsection"
        ("This is a subsection of 1.2")
        ("This is another subsection of 1.2"))
       ("The last subsection of 1"))
      ("Another section"
       ("More stuff")
       ("Still more stuff"))))
   
   '(((1) "The first section")
     ((1 1) "A subsection with no subsections")
     ((1 2) "Another subsection")
     ((1 2 1) "This is a subsection of 1.2")
     ((1 2 2) "This is another subsection of 1.2")
     ((1 3) "The last subsection of 1")
     ((2) "Another section")
     ((2 1) "More stuff")
     ((2 2) "Still more stuff"))
   
   "The output should be the flat representation list of the input list")
  
  (check-equal? (flat-rep? flat-rep-list) true 
                "flat-rep-list is a flat rep list so it should return true")) 

(run-tests function-tests)





