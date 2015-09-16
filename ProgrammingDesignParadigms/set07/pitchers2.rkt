;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname pitchers) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t write repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require "extras.rkt")

(provide list-to-pitchers
         pitchers-to-list
         pitchers-after-moves
         make-move
         move-src
         move-tgt
         solve)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;                               Data Definition
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define-struct pitcher (contents capacity))
; A Pitcher is a (make-pitcher Number Number)
; interp:
; -- the contents is the amount of liquid the pitcher holds.
; -- the capacity is the capacity of the pitcher.
; where the contents and capacity must follow the following relation:
; 0 <= contents <= capacity.

; pitcher-fn : Pitcher -> ??
;(define (pitcher-fn p)
;  (...(pitcher-contents p) (pitcher-capacity p)))

; A ListOf<Pitcher> (LOP) is either
; -- empty                    interp: the LOP has no elements.
; -- (cons Pitcher LOP)       interp: the LOP has at least one element.
; lop-fn -> ??
;(define (lop-fn lop)
;  (cond
;    [(empty? lop) ...]
;    [else (...(pitcher-fn (first lop))
;              (lop-fn (rest lop)))]))

; Definition 1
; A NonEmptyListOf<Pithcer> NELOP is one of:
; -- (cons Pitcher empty)          interp: the NELOP has only one element
; -- (cons Pitcher NELOP)          interp: the NELOP has more than one element.
; nelop-fn : NELOP -> ??
;(define (nelop-fn nelop)
;  (cond
;    [(empty? (rest nelop)) (...(pitcher-fn (first nelop)))]
;    [(cons? (rest nelop)) (...(pitcher-fn (first nelop))
;              (nelop-fn (rest nelop)))]))

; Definition 2
; A NonEmptyListOf<Pitcher> NELOP is a (cons Pitcher LOP)
; nelop-fn ; NELOP -> ??
;(define (nelop-fn nelop)
;  (...(pitcher-fn (first nelop)) (lop-fn (rest nelop))))


; A Pitchers is a non empty list of pitchers (NELOP).

; A ListOf<Pitchers> (LOPS) is either:
; -- Pitchers          interp: the ListOf<Pitchers> has no elements.
; -- (cons Pitchers empty) interp: the LOPS has at least two elements.
; lops-fn : LOPS -> ??
;(define (lops-fn lops)
;  (cond
;    [(empty? lops) ...]
;    [else (...(nelop-fn (first lops))
;              (lops-fn (rest lops)))]))


; A Rep is a list of number (list Number Number)
; where there are only two elements in the list.
; the first element represents the contents and the second element represents
; the capacity of a pitcher. the first element must be smaller or equal than
; the second element.
; rep-fn : Rep -> ??
;(define (rep-fn r)
;  (...(first r) (second r)))

; A NonEmptyListOf<Rep> (NELOR) is one of:
; -- (cons Rep empty)            interp: the NELOR has only one element.
; -- (cons Rep NELOR)            interp: the NELOR has more than one elements.
; nelor-fn : NELOR -> ??
;(define (nelor-fn nelor)
;  (cond
;    [(empty? (rest nelor)) (...(rep-fn (first nelor)))]
;    [(cons? (rest nelor)) (...(rep-fn (first nelor))
;                            (nelor-fn (rest nelor)))]))

; A ListRep is a NonEmptyListOf<Rep> (NELOR)

(define-struct move (src tgt))
; A Move is a (make-move PosInt PosInt)
; WHERE: src and tgt are different
; INTERP: (make-move i j) means pour from pitcher i to pitcher j.
; move-fn : Move -> ??
;(define (move-fn mv)
;  (...(move-src mv) (move-tgt mv)))

; A ListOf<Move> (LOM) is either:
; -- empty                     interp: the LOM has no elements.
; -- (cons Move LOM)           interp: the LOM has at least one element.
; lom-fn : LOM -> ??
;(define (lom-fn lom)
;  (cond
;    [(empty? lom) ...]
;    [else (... (move-fn (first lom))
;               (lom-fn (rest lom)))]))

; A PosInt is a positive integer.(1,2,3,...)

; A NEListOf<PosInt> (NELOPI) is one of
; -- (cons PosInt empty)         interp: the NELOPI has only one element.
; -- (cons PosInt NELOPI)        interp: the NELOPI has at least two elements.
; nelop-fn : nelop -> ??
;(define (nelop-fn nelop)
;  (cond
;    [(empty? (rest nelop)) (...(first nelop))]
;    [(cons? (rest nelop)) (...
;                           (first (nelop))
;                           (nelop-fn (rest nelop)))]))

; A Maybe<ListOf<Move>> (MBLOM) is one of
; -- false               interp: the MBLOM is not  a ListOf<Move>
; -- ListOf<Move>        interp: the MBLOM is a ListOf<Move>
; mblom-fn : MBLOM -> ??
;(define (mnbom-fn lom)
;  (cond
;    [(false? lom) ...]
;    [else (...(lom-fn (lom)))]))

(define-struct pack (lops path))
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;                     Functions Definitions
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; list-to-pitchers : ListRep -> Pitchers
; GIVEN: a list of rep.
; RETURNS: my internal representation of the given input.
; EXAMPLES:
;   see tests.
; STRATEGY: Struct Decomp on nelor : NELOR.
(define (list-to-pitchers nelor)
  (cond
    [(empty? (rest nelor)) (list (rep->pitcher (first nelor)))]
    [(cons? (rest nelor)) (cons (rep->pitcher (first nelor))
                                (list-to-pitchers (rest nelor)))]))

; rep->pitcher : Rep -> Pitcher
; GIVEN: a rep
; RETURNS: a pitcher representation of the rep.
; EXAMPLES:
;   (rep-pitcher (list 1 2)) => (make-pitcher 1 2)
;   (rep-pitcher (list 2 8)) => (make-pitcher 2 8)
; STRATEGY: Struct Decomp on rep : Rep
(define (rep->pitcher rep)
  (make-pitcher (first rep) (second rep)))

; pitchers-to-list : Pitchers -> ListRep
; GIVEN: an internal representation of a set of pitchers
; RETURNS: a listRep that represents them.
; EXAMPLES:
;   see tests.
; STRATEGY: Struct Decomp on nelop : NELOP.
(define (pitchers-to-list nelop)
  (cond
    [(empty? (rest nelop)) (list (pitcher->rep (first nelop)))]
    [(cons? (rest nelop)) (cons (pitcher->rep (first nelop))
                                (pitchers-to-list (rest nelop)))]))

; pitcher->rep : Pitcher -> Rep
; GIVEN: a pitcher.
; WHERE: the contents of pitcher must be less or equal than its capacity.
; RETURNS; a rep representation of the given pitcher.
; EXAMPLES:
;   (pitcher->rep (make-pitcher 2 3)) => '(2 3)
; STRATEGY: Struct Decomp on p : Pitcher.
(define (pitcher->rep p)
  (list (pitcher-contents p) (pitcher-capacity p)))

; pitchers-after-moves : Pitchers ListOf<Move> -> Pitchers
; GIVEN: An internal representation of a set of pitchers, and a sequence
;    of moves
; WHERE: every move refers only to pitchers that are in the set of pitchers.
; RETURNS: the internal representation of the set of pitchers that should
;    result after executing the given list of moves, in order, on the given
;    set of pitchers.
; EXAMPLES:
;    see tests.
; STRATEGY: HOFC + Struct Decomp on [Move].
(define (pitchers-after-moves pts lom)
  (foldl
   ; Move Pitchers -> Pitchers
   ; GIVEN: a move and a pitchers
   ; WHERE: the pts-sofar is the results of applying first
   ; few moves in the lom to the original pitchers pts.
   ; RETURNS: the result of applying all the moves in the lom
   ; to the original pitchers pts.
   (lambda (m pts-sofar)
     (pitchers-after-move 
      m
      pts-sofar
      (diff-pitchers (list-ref pts-sofar (sub1 (move-src m)))
                     (list-ref pts-sofar (sub1 (move-tgt m)))) 1))
   pts
   lom))

; pitchers-after-move : Move Pitchers Number Number -> Pitchers
; GIVEN: a move m, a pitchers pts, the amount of water that should be
;    poured from one pitcher to another.
; WHERE: the n is the number of elements in th original pts0
;    that are above the current pts.
; RETURNS: a pitchers after the move action. there should be diff amount
;    of water moved from src to dst which are specified by m Move.
; STRATEGY: Struct Decomp on pts : NELOP.
(define (pitchers-after-move m pts dif n)
  (cond
    [(empty? (rest pts)) (list (pitch-after-move m (first pts) dif n))]
    [(cons? (rest pts))
     (cons (pitch-after-move m (first pts) dif n)
           (pitchers-after-move m (rest pts) dif (add1 n)))]))

; pitch-after-move : Move Pitcher Number Number -> Pitcher
; GIVEN: a move m, a pitcher, pt, the amount of water should be poured
;    and a Number.
; WHERE: n is the number indicating how many pitcher(s) in the original
;    pitchers are above the input pitcher.
; RETURNS: if the pitcher is the source, then deduct dif from the source 
;    contents, then if the pitcher is the dst, then add dif to its contents.
; EXAMPLES:
;    (pitch-after-move (make-move 1 2) pitcher-1-2 2 1)) => (make-pitcher 0 4)
;    (pitch-after-move (make-move 1 2) pitcher-1-2 2 2)) => (make-pitcher 4 4)
; STRATEGY: Function Composition.
(define (pitch-after-move m pt dif n)
  (if (= n (move-src m))
      (pitcher-after-pour pt dif)
      (if (= n (move-tgt m))
          (pitcher-after-poured pt dif)
          pt)))

; diff-pitchers : Pitcher Pitcher -> Number
; GIVEN: the source pitcher and the target pitcher.
; RETURNS: the amount of liquid that we can move from the source
; pitcher to the dst pitcher during one move.
; EXAMPLES:
;   (diff-pitchers pitcher-1-2 pitcher-1-1) => 2
;   (diff-pitchers pitcher-1-3 pitcher-1-1) => 2
; STRATEGY: Function Composition.
(define (diff-pitchers src tgt)
  (min (pitcher-contents src)
       (- (pitcher-capacity tgt) (pitcher-contents tgt))))

; pitcher-after-pour : Pitcher Number -> Pitcher
; GIVEN: A source pitcher and the amount of water to pour.
; WHERE: the amount of water to pour must be smaller than the current amount
;    of contents in the given pitcher.
; RETURNS: the pitcher after pouring the amount of liquid given in the input
;    from the given pitcher.
; EXAMPLES:
;    (pitcher-after-pour pitcher-1-3 1) => (make-pitcher 2 3)
;    (pitcher-after-pour pitcher-1-3 2) => (make-pitcher 1 3)
; STRATEGY: Function Composition.
(define (pitcher-after-pour pt dif)
  (make-pitcher (- (pitcher-contents pt) dif)
                (pitcher-capacity pt)))

; pitcher-after-poured : Pitcher Number -> Pitcher
; GIVEN: a dst pitcher and the amount of liquid that will be poured to it.
; WHERE: the total amount of water after pouring to the given pitcher should
; be smaller than the capacity of the given pitcher.
; RETURNS: the new pitcher after being poured the amount of liquid to the
; original given pitcher.
; EXAMPLES:
;    (pitcher-after-poured pitcher-1-1 2) => (make-pitcher 3 3)
;    (pitcher-after-poured pitcher-1-2 2) => (make-pitcher 4 4)
; STRATEGY: Function Composition.
(define (pitcher-after-poured pt dif)
  (make-pitcher (+ (pitcher-contents pt) dif)
                (pitcher-capacity pt)))

; solve : NEListOf<PosInt> PosInt -> Maybe<ListOf<Move>>
; GIVEN: a list of the capacities of the pitchers and the goal amount
; RETURNS: a sequence of moves which, when executed from left to right,
;    results in one pitcher (not necessarily the first pitcher) containing
;    the goal amount.  Returns false if no such sequence exists.
; EXAMPLES:
;    see tests.
; STRATEGY: Function Composition.
(define (solve loi g)
  (if (solution? (initial-pitchers loi) g)
      empty
      (pack-path
       (solve-helper.v3 empty
                        (initial-moves (length loi))
                        (initial-moves (length loi))
                        (initial-pitchers loi)
                        g
                        empty))))

#;(define (solve loi g)
  (pack-path 
   (solve-helper empty
                 (initial-moves (length loi))
                 (initial-moves (length loi))
                 (initial-pitchers loi)
                 g
                 empty)))

; solve-helper : ListOf<Pitchers> LOM LOM Pitchers PosInt LOM -> ListOf<Move>
(define (solve-helper.v3 lops mvs-rest mvs newest g path)
  (cond
    [(empty? mvs-rest) (make-pack lops false)]
    [else (local
            ((define cur-mv (list (first mvs-rest)))
             (define candidate (pitchers-after-moves newest cur-mv)))
            (cond
              [(solution? candidate g) (make-pack lops (append path cur-mv))]
              [(member? candidate lops) 
               (solve-helper.v3 lops (rest mvs-rest) mvs newest g path)]
              [else (local
                      ((define res
                         (solve-helper.v3 (append lops (list newest)) mvs mvs
                                          candidate
                                          g (append path cur-mv))))
                      (if (false? (pack-path res))
                          (solve-helper.v3 (pack-lops res) (rest mvs-rest) mvs newest g path)
                          res))]))]))

; solution? : Pitchers PosInt -> Boolean
; GIVEN: a pitchers and goal amount that we expect.
; ANSWERS: if the one of the pitcher in the pitchers has the goal amount
; that we expect to have.
; EXAMPLES:
;    (solution? pitchers1 4) => false
;    (solution? pitchers2 8) => true
; STRATEGY: Struct Decomp on pts : NELOP
(define (solution? pts i)
  (cond
    [(empty? (rest pts)) (= (pitcher-contents (first pts)) i)]
    [(cons? (rest pts)) (or (= (pitcher-contents (first pts)) i)
                            (solution? (rest pts) i))]))

; initial-pitchers : NEListOf<PosInt> -> Pitchers
; GIVEN: a non empty list of positive integers which represent the initial
;    capacity of a list of pitchers.
; RETURNS: a pitchers which contains a list of pitcher whose capacity is
;    specified by the given non empty list of positive integers. All the 
;    contents of pitcher are zero except for the first one is full.
; EXAMPLES:
;    (initial-pitchers (list 8 6 4)) => 
;              (list (make-pitcher 8 8) (make-pitcher 0 6) (make-pitcher 0 4))
;    (initial-pitchers (list 9 8)) => 
;              (list (make-pitcher 9 9) (make-pitcher 0 8))

; STRATEGY: Struct Decomp on nelop : NELOP (using definition 2)
(define (initial-pitchers nelop)
  (cons (make-pitcher (first nelop) (first nelop))
        (initial-pitchers-helper (rest nelop))))


; initial-pitcher-helper : ListOf<PosInt> -> ListOf<Pitcher>
; GIVEN: a list of positive integer which represents the capacity of a sublist
;    of pitcher. the sublist is part of a larger original list without the
;    first element.
; RETURNS: a list of initial state of pitcher, whose capacity is specified by
;    by the input and contents are zeros.
; EXAMPLES:
;    (initial-pitcher-helper (list 8 5 4)) =>
;               (list (make-pitcher 0 8) (make-pitcher 0 5) (make-pitcher 0 4))
; STRATEGY: HOFC.
(define (initial-pitchers-helper lopi)
  (map
   ; PosInt -> Pitcher
   ; GIVEN: a positive integer
   ; RETURNS: a pitcher whose capacity is the integer and contents is zero.
   (lambda (pi) (make-pitcher 0 pi))
   lopi))

; initial-moves : Number -> ListOf<Move>
; GIVEN: the number of pitcher in the list of pitchers
; RETURNS: the possible moves generated given the size of pitchers.
; EXAMPLES:
;    (initial-moves 3) => (list (make-move 2 3) (make-move 1 3) (make-move 3 2)
;                              (make-move 1 2) (make-move 3 1) (make-move 2 1))
; STRATEGY: Function Composition.
(define (initial-moves n)
  (initial-moves-helper n n))

; initial-moves-helper : Number Number -> ListOf<Move>
; GIVEN: two numbers.
; WHERE: the first number represents the maximum possible index of src pitcher
;    the second numbe represents the maximum possible index of dst pitcher.
; RETURNS: a list of all possible moves under the given input. For each
;    combination of move, the src can be any number less or equal than i
;    and dst can be any number less or equal than j. But src and dst can't
;    be the same for a certain move.
; EXAMPLES:
;   (initial-moves-helper 2 2) => (list (make-move 1 2) (make-move 2 1))
;   (initial-moves-helper 2 3) => 
;      (list (make-move 1 2) (make-move 1 3) (make-move 2 1) (make-move 2 3))
; STRATEGY: General Recursion.
; TERMINATION ARGUMENT: at each recursive call, the index for dst decrease one.
;   if j decreases to zero, the recursion terminates immediately.
(define (initial-moves-helper i j)
  (cond
    [(= j 0) empty]
    [else (append
           (initial-helper i j)
           (initial-moves-helper i (sub1 j)))]))

; initial-helper : Number Number -> ListOf<Move>
; GIVEN: two numbers.
; WHERE: the first number i is the maximum possible index of src pitcher.
;    the second number j is the index for dst pitcher.
; RETURNS: a list of all possible moves under the given input. For each
;    combination of move, the src can be any number less or equal than i,
;    and dst is j. For each move, src and dst cannot be the same.
; STRATEGY: General Recursion.
; TERMINATION ARGUMENT: at each recursive call, the index for src decrease
;    by one. if i decreases to zero, the recursion terminates immediately.
(define (initial-helper i j)
  (cond
    [(= i 0) empty]
    [else (if (= i j)              
              (initial-helper (sub1 i) j)
              (append (list (make-move i j)) (initial-helper (sub1 i) j)))]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;                                  TESTS
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(define listrep1 '((1 3) (2 4) (3 3)))
(define pitcher-1-1 (make-pitcher 1 3))
(define pitcher-1-2 (make-pitcher 2 4))
(define pitcher-1-3 (make-pitcher 3 3))
(define pitchers1 (list pitcher-1-1
                        pitcher-1-2
                        pitcher-1-3))

(define listrep2 '((8 8) (0 7) (0 5) (0 2)))
(define pitchers2 (list (make-pitcher 8 8)
                        (make-pitcher 0 7)
                        (make-pitcher 0 5)
                        (make-pitcher 0 2)))

; three list of moves
(define lom1 (list (make-move 1 2) (make-move 2 3)))
(define lom2 (list (make-move 3 2)))
(define lom3 (list (make-move 1 4) (make-move 1 3)))

; three pitchers after list of move action.
(define pitchers1-after-mv1 
  (list (make-pitcher 0 3)
        (make-pitcher 3 4)
        (make-pitcher 3 3)))

(define pitchers1-after-mv2
  (list (make-pitcher 1 3)
        (make-pitcher 4 4)
        (make-pitcher 1 3)))

(define pitchers2-after-mv3
  (list (make-pitcher 1 8)
        (make-pitcher 0 7)
        (make-pitcher 5 5)
        (make-pitcher 2 2)))

; a non empty list of positive integer
(define lopi3 (list 8 5 3))
(define lopi4 (list 979 203 78))
; four goals, i1, i2, i3 is reachable
; i4 is not reachable.
(define i1 2)
(define i2 8)
(define i3 4)
(define i4 10)
(define i5 6)

(define pitchers3 (list (make-pitcher 8 8)
                        (make-pitcher 0 5) 
                        (make-pitcher 0 3)))
(define pitchers4 (list (make-pitcher 979 979)
                       (make-pitcher 0 203)
                       (make-pitcher 0 78)))

(define solution-3-1 (solve lopi3 i1))
(define solution-3-2 (solve lopi3 i2))
(define solution-3-3 (solve lopi3 i3))
(define solution-3-4 (solve lopi3 i4))
(define solution-4-5 (solve lopi4 i5))

(define-test-suite pitchers-after-moves-tests
  
  (check-equal? (pitchers-after-moves pitchers1 lom1)
                pitchers1-after-mv1
                "should return pitchers1-after-mv1")
  
  (check-equal? (pitchers-after-moves pitchers1 lom2)
                pitchers1-after-mv2
                "should return pitchers1-after-mv2")
  
  (check-equal? (pitchers-after-moves pitchers2 lom3)
                pitchers2-after-mv3
                "should return pitchers2-after-mv3"))

(define-test-suite solve-tests
  
  (check-equal? (solution? (pitchers-after-moves pitchers3 solution-3-1) i1)
                true
                "solution-3-1 should be a solution for pitchers3 when goal=2")
  
  (check-equal? (solution? (pitchers-after-moves pitchers3 solution-3-2) i2)
                true
                "solution-3-2 should be a solution for pitchers3 when goal=8")
  
  (check-equal? (solution? (pitchers-after-moves pitchers3 solution-3-3) i3)
                true
                "solution-3-2 should be a solution for pitchers3 when goal=4")
  
  (check-equal? (solution? (pitchers-after-moves pitchers4 solution-4-5) i5)
                true
                "solution-4-5 should be a solution for pitchers4 when goal=6")
  (check-equal? (solve lopi3 i4)
                false
                "solution to list of posint 3 when goal=10 has no solution"))

(define-test-suite pitchers-to-list-tests
  
  (check-equal? (pitchers-to-list pitchers1)
                listrep1
                "pitchers-to-list called upon pitchers1 should return rep1")
  
  (check-equal? (pitchers-to-list pitchers2)
                listrep2
                "ptichers-to-list called upon pitchers2 should return rep2"))

(define-test-suite list-to-pitchers-tests
  
  (check-equal? (list-to-pitchers listrep1)
                pitchers1
                "list-to-pitchers with listrep1 should return pitchers1")
  
  (check-equal? (list-to-pitchers listrep2)
                pitchers2
                "list-to-pitchers with listrep2 should return pitchers2"))

(run-tests pitchers-after-moves-tests)
(run-tests solve-tests)
(run-tests pitchers-to-list-tests)
(run-tests list-to-pitchers-tests)