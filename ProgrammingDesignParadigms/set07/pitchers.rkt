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
;                          Introduction to Algorithms
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;     ALGORITHMS FOR Q1

;     For the data definition of ListRep, we first define a data structrure Rep
; which is a S-exp represention of the a single pitcher, first element in Rep is
; the contents of the pitcher, and second element in Rep represents the capacity
; of the pitcher. Then, we Define ListRep as a non-empty list of Rep.

;     For the internal data definition for Pitchers, we first define a structure
; pitcher (define-struct pitcher contents capacity), which is a
; (make-pitcher Number Number). contents is the the amount of water in the pit-
; cher and capacity is the maximum amount of water a pitch can hold.

;     For function pitchers-after-moves, we pour the liquid from src pitcher to
; the dst pitcher following the order of given LOM. The output of the previous
; move, become the input of current move. For each of the move
; "pitchers-after-move", we first compute the amount of water that should be
; poured, then traverse the list of pitcher to deduct water from the src pitcher
; and add liquid into the dst pitcher.

;     ALGORITHMS FOR Q2
;
;     We use DFS algorithm to solve the pitchers problem.

;     For a given list of pitchers, we first get all possible pouring order,
; that is if there are only two pitchers, the possible moves are 1 -> 2
; and 2 -> 1. If there are three pitchers, the possible moves are 1 -> 2, 1->3
; 2->1, 2->3, 3->1, 3->2. Our algoritm use a list of moves to presents all the
; possible moves.

;     For any state of pitchers, there are always N number fixed moves. every
; move will transit the pitchers to different pitchers. The new pitchers also
; have N possible moves, which will transit the new pitchers to other pitchers
; as well. In our algorithm, for every pitchers, always search along the first
; path and get a new pitchers, for the new pitchers always search the first
; path too. Continue these step recursively. When our program keeps on recursion
; we keep a list of all the state of pitchers we have searched, and moves
; that we have made. For this problem trivial cases and non-trivial cases.

;     trivial case 1: for a given node, if the next step of search generate a
; expected output, then the recursion stops.

;     trivial case 2: for a given state, if the next step of search
; generates a new pitchers that has already existed, then stop searching along
; the current path and jump to the next path. 

;     non-trivial case 1: for a given state, if the results of searching all of
; its N moves generate the an existed state, jump back to the previous state
; that leads to the current state, and search its next path. Then the solution
; to the overall problem is the combination of the searching path made so far
; and the solution to finding the goal from the current state.

;     non-trivial case 2: for a given state, if the next step of search neither
; exists in the list of pitchers we have searched, nor is expected goal, take
; the output of next step of search as input and keep on recursion. The com-
; bination of previous search moves and the future moves is the solution to our
; pitcher problem.
;     
;     So, in our algorithm, each of our recursion generate one more state,
; decreasing the number of states left in all possible states domain. Thus at
; every step, we have fewer states to examine making our problem more easier
; than the previous problem. Thus the number of states left in states domain
; is the halting measure as to our pitcher problem.

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
; -- empty                interp: the LOPS has no elements.
; -- (cons Pitchers LOPS) interp: the LOPS has at least one elements.
; lops-fn : LOPS -> ??
;(define (lops-fn lops)
;  (cond
;    [(empty? lops) ...]
;    [else (...(nelop-fn (first lops))
;              (lops-fn (rest lops)))]))

; A Rep is a
; -- (list Number Number)
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
; A Nat is a natural number.

; A ListOf<PosInt> (LOPI) is either:
; -- empty                       interp: there is no element in the list.
; -- (cons PosInt LOPI)          interp: the LOPI has at least one element.
; lopi-fn : lopi -> ??
;(define (lopi-fn lopi)
;  (cond
;    [(empty? lopi) ...]
;    [else
;     (...(first lopi) (lop-fn (rest lopi)))]))

; A NEListOf<PosInt> (NELOPI) is (cons PosInt LOPI) 
; nelopi-fn : nelopi-fn -> ??
;(define (nelopi-fn nelopi)
;  (...(first nelopi) (lop-fn (rest nelopi))))

; A Maybe<ListOf<Move>> (MBLOM) is one of
; -- false               interp: the MBLOM is not  a ListOf<Move>
; -- ListOf<Move>        interp: the MBLOM is a ListOf<Move>
; mblom-fn : MBLOM -> ??
;(define (mnbom-fn lom)
;  (cond
;    [(false? lom) ...]
;    [else (...(lom-fn lom))]))

(define-struct traversed (lops moves))
; A Traversed is a
; (make-traversed LOPS MBLOM)
; interp:
; --the lops is a list of picthers already seen in the previous path.
; --the moves is a maybe list of moves. If the current move has already
;   existed in the lops or there's no more possible search path, the moves
;   is false. Otherwise, moves is a list of action of moves made so far.
; template:
; traversed-fn : Traversed -> ??
;(define (traversed-fn pmt)
;  (...(traversed-lops pmt)
;      (traversed-moves pmt)))

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
;   (rep->pitcher (list 1 2)) => (make-pitcher 1 2)
;   (rep->pitcher (list 2 8)) => (make-pitcher 2 8)
; STRATEGY: Struct Decomp on rep : Rep
(define (rep->pitcher rep)
  (make-pitcher (first rep) (second rep)))

; pitchers-to-list : Pitchers -> ListRep
; GIVEN: an internal representation of a set of pitchers
; RETURNS: a listRep that represents them.
; EXAMPLES:
;    (pitchers-to-list pitchers1) => '((1 3) (2 4) (3 3))
; STRATEGY: Struct Decomp on nelop : NELOP. (template 1)
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
   ;   few moves in the lom to the original pitchers pts.
   ; RETURNS: the result of applying the current move to
   ;   the output of the first few moves.
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
;    poured from source pitcher to destination pitcher.
; WHERE: the n is the number of elements in th original pts0
;    that are above the current pts.
; RETURNS: a pitchers after the move action. there should be diff amount
;    of water moved from src to dst which are specified by m Move.
; STRATEGY: Struct Decomp on pts : NELOP (template 1).
(define (pitchers-after-move m pts dif n)
  (cond
    [(empty? (rest pts)) (list (pitcher-after-move m (first pts) dif n))]
    [(cons? (rest pts))
     (cons (pitcher-after-move m (first pts) dif n)
           (pitchers-after-move m (rest pts) dif (add1 n)))]))

; pitcher-after-move : Move Pitcher Number Number -> Pitcher
; GIVEN: a move m, a pitcher, pt, the amount of water should be poured
;    and a Number.
; WHERE: n is the number indicating how many pitcher(s) in the original
;    pitchers are above the input pitcher.
; RETURNS: if the pitcher is the source, then deduct dif from the source 
;    contents, then if the pitcher is the dst, then add dif to its contents.
; EXAMPLES:
;    (pitcher-after-move (make-move 1 2) pitcher-1-2 2 1)) => (make-pitcher 0 4)
;    (pitcher-after-move (make-move 1 2) pitcher-1-2 2 2)) => (make-pitcher 4 4)
; STRATEGY: Struct Decomp on m : Move
(define (pitcher-after-move m pt dif n)
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
; STRATEGY: Struct Decomp on [Pitcher]
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
; STRATEGY: Struct Decomp on [Pitcher]
(define (pitcher-after-pour pt dif)
  (make-pitcher (- (pitcher-contents pt) dif)
                (pitcher-capacity pt)))

; pitcher-after-poured : Pitcher Number -> Pitcher
; GIVEN: a dst pitcher and the amount of liquid that will be poured to it.
; WHERE: the total amount of water after pouring to the given pitcher should
;    be smaller than the capacity of the given pitcher.
; RETURNS: the new pitcher after being poured the amount of liquid to the
;    original given pitcher.
; EXAMPLES:
;    (pitcher-after-poured pitcher-1-1 2) => (make-pitcher 3 3)
;    (pitcher-after-poured pitcher-1-2 2) => (make-pitcher 4 4)
; STRATEGY: Struct Decomp on [Pitcher]
(define (pitcher-after-poured pt dif)
  (make-pitcher (+ (pitcher-contents pt) dif)
                (pitcher-capacity pt)))

; solve : NEListOf<PosInt> PosInt -> Maybe<ListOf<Move>>
; GIVEN: a list of the capacities of the pitchers and the goal amount
; RETURNS: a sequence of moves which, when executed from left to right,
;    results in one pitcher (not necessarily the first pitcher) containing
;    the goal amount.  Returns false if no such sequence exists.
; EXAMPLES:
;    (solve (list 8 5 3) 2) => 
;              (list (make-move 1 3) (make-move 3 2) (make-move 1 3))
; STRATEGY: Function Composition.
(define (solve loi g)
  (if (solution? (initial-pitchers loi) g)
      empty
      (check-first-solve loi g)))

; check-first-solve : NEListOf<PosInt> PosInt -> Maybe<ListOf<Move>>
; GIVEN: a list of the capacities of the pitchers and the goal amount.
; RETURNS: a sequence of moves which, when executed from left to right,
;    results in one pitcher (not necessarily the first pitcher) containing
;    the goal amount.  Returns false if no such sequence exists. 
;    When the goal is greater than the initial amount of liquid in the first
;    pitcher, return false immediately.
; EXAMPLES:
;    (check-first-solve (list 8 5 3) 10) => false.
; STRATEGY: Fucntion Composition.
(define (check-first-solve loi g)
  (if (> g (first loi))
      false
      (traversed-moves
       (solve-helper (list (initial-pitchers loi))
                     (initial-moves (length loi))
                     (initial-moves (length loi))
                     (initial-pitchers loi)
                     g
                     empty))))
; solve-helper : LOP LOM LOM Pitchers PosInt LOM -> Traversed
; GIVEN: mvs, a list of all possible moves as to the given
;    list of pitchers. newest, new state of pitcher that hasn't seen
;    in our past search, it is the state we are working on. g is a
;    number indicate our goal.
; WHERE: 
;    -- lops is a list of pitchers seen so far. In our recursion we
;    add new state that hasn't seen so far into the lops.
;    -- mvs-rest is a list of moves that haven't been searched for the
;    newest state we're working on. when the current searching path
;    doesn't has a solution, it is removed from the list.
;    -- path is a list of moves we made sofar to reach the current
;    the newest state. if the next state neither existed in history
;    record nor is a solution, nor has no remaining paths to search
;    we add current search path into path.
; RETURNS: a Travered which contains information of our recursion.
;    the lops field is a list of all pitchers state that we have seen
;    so far. If in the current recursion our algoritm doesn't halt,
;    the field moves is a list of moves we made so far to reach the goal.
;    Otherwise, it is false.
; EXAMPLES:
;    (solve-helper (list (initial-pitchers '(8 5 3)))
;                (initial-moves 3)
;                (initial-moves 3)
;                (initial-pitchers '(8 5 3))
;                2 empty) 
;    returns all the pitchers it traversed and the path to reach the goal.
; STRATEGY: General Recursion.
; TERMINATION ARGUMENT: at every recursion, the number of states left in
; all possible states domain decreases. If it decreases to zero, the
; recursion halts. Before it decreases to zero, if we our search find the
; goal, the recursion also halts. After each recursion, because the number
; of possible pitchers state left decreases, the problem becomes easier.
(define (solve-helper lops mvs-rest mvs newest g path)
  (if (empty? mvs-rest)      
      (make-traversed lops false)
      (local
        ((define cur-mv (list (first mvs-rest)))
         (define candidate (pitchers-after-moves newest cur-mv)))
        (cond
          ; trivial case: the recursion returns searching path immediately 
          ; after we find an solution.
          [(solution? candidate g)
           (make-traversed lops (append path cur-mv))]
          ; non-trivial case: if the next step has existed in the search
          ; path, jump to search the next path of the current state.
          [(member? candidate lops)
           (solve-helper lops (rest mvs-rest) mvs newest g path)]
          ; non-trivial case: other wise keep on calling solve-helper
          ; on the new state(candidate) we generate in this recursion.
          [else (local
                  ((define res
                     (solve-helper (append lops (list candidate)) mvs mvs
                                   candidate g (append path cur-mv))))
                  (if (false? (traversed-moves res))
                      (solve-helper (traversed-lops res) (rest mvs-rest)
                                    mvs newest g path)
                      res))]))))

; solution? : Pitchers PosInt -> Boolean
; GIVEN: a pitchers and goal amount that we expect.
; ANSWERS: if the one of the pitcher in the pitchers has the goal amount
; that we expect to have.
; EXAMPLES:
;    (solution? pitchers1 4) => false
;    (solution? pitchers2 8) => true
; STRATEGY: Struct Decomp on pts : NELOP (template 1).
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
;    contents of pitcher are zero except for that the first one is full.
; EXAMPLES:
;    (initial-pitchers (list 8 6 4)) => 
;              (list (make-pitcher 8 8) (make-pitcher 0 6) (make-pitcher 0 4))
;    (initial-pitchers (list 9 8)) => 
;              (list (make-pitcher 9 9) (make-pitcher 0 8))
; STRATEGY: Struct Decomp on loi : NELOPI.
(define (initial-pitchers loi)
  (cons (make-pitcher (first loi) (first loi))
        (initial-pitchers-helper (rest loi))))


; initial-pitcher-helper : ListOf<PosInt> -> ListOf<Pitcher>
; GIVEN: a list of positive integers which represents the capacity of a sublist
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

; initial-moves : PosInt -> ListOf<Move>
; GIVEN: the number of pitcher in the list of pitchers
; RETURNS: the possible moves generated given the size of pitchers.
; EXAMPLES:
;    (initial-moves 3) => (list (make-move 2 3) (make-move 1 3) (make-move 3 2)
;                              (make-move 1 2) (make-move 3 1) (make-move 2 1))
; STRATEGY: Function Composition.
(define (initial-moves n)
  (initial-moves-helper n n))

; initial-moves-helper : PosInt PosInt -> ListOf<Move>
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
; STRATEGY: Structual Decomp on j : Nat.
(define (initial-moves-helper i j)
  (cond
    [(= j 0) empty]
    [else (append
           (initial-helper i j)
           (initial-moves-helper i (sub1 j)))]))

; initial-helper : PosInt PosInt -> ListOf<Move>
; GIVEN: two numbers.
; WHERE: the first number i is the maximum possible index of src pitcher.
;    the second number j is the index for dst pitcher.
; RETURNS: a list of all possible moves under the given input. For each
;    combination of move, the src can be any number less or equal than i,
;    and dst is j. For each move, src and dst cannot be the same.
; STRATEGY: Struct Decomp on i : Nat.
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
(define lopi5 (list 10 8 6 4))
; four goals, i1, i2, i3 is reachable
; i4 is not reachable.
(define i1 2)
(define i2 8)
(define i3 4)
(define i4 10)
(define i5 6)
(define i6 3)

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
                "solution to list of posint #3 when goal=10 has no solution")
  (check-equal? (solve lopi5 i6)
                false
                "solution to list of posint #5 when goal=3 has no solution"))

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
