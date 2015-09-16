;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname |robot 3|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require "extras.rkt")

(provide path)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; For the problem, we first restrict the moves the robot can make by
; finding the maximum and minimum x and y values of target and blocks. The 
; robot can move 1 unit distance outside these maximum and minimum values.
; We then find the moves the robot is eligible to make by considering the 
; locations of the blocks. The can-move-east?, can-move-west?, etc. functions
; tell us whether or not the robot can move 1 unit distance in these 
; directions. We also have a robot-after-move function which returns
; the position of the robot after applying the move.
;
; The path function first checks if the robot is on a block or if in the 
; starting position, the robot has no eligible moves. If either of these
; is true the function returns false. If the start position is equal to 
; the target position then empty is returned. 
;
; The algorithm for the path-helper function works like this:
; -- We first find the new nodes by applying all the eligible moves on the 
;   current robot position
; -- The first of these nodes is pos and the move that led to this position
;   is stored in move
; -- Trivial Case 1: If pos is the target, then we return all the moves played
;   in this path and end the recursion.
; -- Trivial Case 2: If pos is in the list of positions seen on the previous 
;   path, we discard it and move on to search the other candidates in the list
; -- Non-trivial case: If neither of the above is true, we go down the path 
;   where pos is the starting node and search there.
;   If the above path results in false, then we discard it and search the other
;   candidates in the list
; -- Whenever the candidate-list becomes empty, it means that the target was
;   not reached on the current path, so return false
; -- If all the paths are searched and the target is not reached, then the
;   function returns false
;   
; The list of moves returned by the path-helper function are of unit-distance
; form. e.g. (list (list "east" 1) (list "east" 1) (list "south" 1))
; These are clubbed together by the club-moves function. So the above example
; becomes:  (list (list "east" 2) (list "south" 1))
;
; And this plan is returned by the path function if the target is reachable, 
; else false is returned.


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; CONSTANTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define ONE 1)
(define THREE 3)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; DATA DEFINITIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; A Position is a (list PosInt PosInt)
; Interp: A Position is a list containing two positive integers
; -- the first element is the x coordinate
; -- the second element is the y coordinate
; (list (x y)) represents the position at position x, y.

; TEMPLATE:
; position-fn : Position -> ??
; (define (position-fn p)
;  (... (first p) (second p)))

;------------------------------------------------------------------------------

; A ListOf<Position> (LOP) is either
; -- empty                interp: the list contains no position
; -- (cons Position LOP)  interp: the list contains at least one position

; TEMPLATE:
; lop-fn : LOP -> ??
; (define (lop-fn lop)
;  (cond
;    [(empty? lop) ...]
;    [else (... (position-fn (first lop))
;               (lop-fn (rest lop)))]))

;------------------------------------------------------------------------------

; A Direction is one of
; -- "north"  (interp: robot moving in north direction)
; -- "east"   (interp: robot moving in north direction)
; -- "south"  (interp: robot moving in north direction)
; -- "west"   (interp: robot moving in north direction)

; TEMPLATE:
; dir-fn : Direction -> ??
; (define (dir-fn dir)
; (cond
;  [(string=? dir "north") ...]
;  [(string=? dir "east") ...]
;  [(string=? dir "south") ...]
;  [(string=? dir "west") ...]))

;------------------------------------------------------------------------------

; A Move is a (list Direction PosInt)
; Interp:
; A Move is a list containing a Direction and a PosInt
; -- the first element is the direction in which the robot should move
; -- the second element is a positive integer which is the number of steps
;    the robot should move

; TEMPLATE:
; move-fn : Move -> ??
; (define (move-fn m)
;  (... (dir-fn (first m)) (second m)))

;------------------------------------------------------------------------------

; A ListOf<Move> (LOM) is either:
; -- empty                     interp: the LOM has no move
; -- (cons Move LOM)           interp: the LOM has at least one move

; TEMPLATE:
; lom-fn : LOM -> ??
; (define (lom-fn lom)
;  (cond
;    [(empty? lom) ...]
;    [else (... (move-fn (first lom))
;               (lom-fn (rest lom)))]))


;------------------------------------------------------------------------------

; A NonEmptyListOf<Move> (NELOM) is:
; -- (cons Move empty)           interp: the NELOM has only one move
; -- (cons Move NELOM)           interp: the NELOM has more than one move

; TEMPLATE:
; nelom-fn : NELOM -> ??
; (define (nelom-fn nelom)
;  (cond
;    [(empty? (rest nelom)) (... (move-fn (first nelom)))]
;    [else  (... 
;             (move-fn (first nelom))
;             (nelom-fn (rest nelom-fn)))]))

;------------------------------------------------------------------------------

; A Maybe<LOM> is either
; -- false         interp: Maybe<LOM> is false                                 
; -- ListOf<Move>  interp: the Maybe<LOM> is a ListOf<Move>

; TEMPLATE:
; mblom-fn : Maybe<LOM> -> ??
; (define (mblom-fn mblom)
;  (cond
;    [(false? mblom) ...]
;    [else (... 
;            (lom-fn mblom))]))

;------------------------------------------------------------------------------

; A Maybe<NELOM> is either
; -- false                 interp: Maybe<LOM> is false 
; -- NonEmptyListOf<Move>  interp: the Maybe<LOM> is a NonEmptyListOf<Move>

; TEMPLATE:
; mbnelom-fn : Maybe<NELOM> -> ??
; (define (mbnelom-fn mbnelom)
;  (cond
;    [(false? mbnelom) ...]
;    [else (... 
;            (nelom-fn mbnelom))]))

;------------------------------------------------------------------------------

; A Plan is a ListOf<Move>
; WHERE: the list does not contain two consecutive moves in the same
; direction.

;------------------------------------------------------------------------------

; A Maybe<Plan> is Maybe<ListOf<Move>>

;------------------------------------------------------------------------------

(define-struct robot-pos-with-prev-move (robot-pos mov))
; A Robot-pos-with-prev-move is a
; (make-robot-pos-with-prev-move Position Move)
; Interp:
; -- robot-pos is the position of the robot
; -- mov is the previous move played which lead to the robot-pos

; TEMPLATE:
; robot-pos-with-prev-move-fn : Robot-pos-with-prev-move -> ??
;(define (robot-pos-with-prev-move-fn rpm)
;  (... (position-fn (robot-pos-with-prev-move-robot-pos rpm))
;       (move-fn (robot-pos-with-prev-move-mov rpm)))

;------------------------------------------------------------------------------

; A LORPM is a ListOf<Robot-pos-with-prev-move>. It is either
; -- empty                                 interp: the list contains no element
; -- (cons Robot-pos-with-prev-move LORPM) interp: the list contains at 
;                                                  least 1 element

; TEMPLATE:
;(define (lorpm-fn lorpm)
;  (cond
;    [(empty? lorpm) ...]
;    [else (... (robot-pos-with-prev-move-fn (first lorpm))
;               (lorpm-fn (rest lorpm)))]))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; FUNCTION DEFINITIONS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


; path : Position Position ListOf<Position> -> Maybe<Plan>
; GIVEN:
;  1. the starting position of the robot,
;  2. the target position that robot is supposed to reach
;  3. A list of the blocks on the board
; RETURNS: a plan that, when executed, will take the robot from
;  the starting position to the target position without passing over any
;  of the blocks, or false if no such sequence of moves exists.
; EXAMPLES: (path (list 2 2) (list 3 2) empty) =>
;              (list
;                (list "north" 1)
;                (list "east" 2)
;                (list "south" 2)
;                (list "west" 1)
;                (list "north" 1)) 
; STRATEGY: Function Composition
(define (path start target blocks)
  (if
   (or (robot-on-block? start blocks)
       (empty? (all-moves-robot start target blocks)))
   false
   (if (equal? start target)
     empty
     (club-moves 
      (path-helper target blocks  
                  empty empty 
                  (list (make-robot-pos-with-prev-move start empty)))))))



; path-helper : Position ListOf<Position> Maybe<LOM> ListOf<Position> LORPM
;               -> Maybe<NELOM>
; GIVEN: the target position, a list of blocks, the list of moves played,
;   the list of positions played, and the list of candidates
; WHERE
;  -- the target position is the position that robot is supposed to reach
;  -- blocks is the list of blocks on the board
;  -- moves-played is either false or stores the list of moves which
;     are played
;  -- positions-played is the positions played in the previous path
;  -- candidate-list is a ListOf<Robot-pos-with-prev-move> which
;     contains the list of positions we are currently exploring along
;     with the respective moves that led to these positions
; RETURNS: a Maybe<NELOM> that, when executed, will take the robot from
;  the starting position to the target position without passing over any
;  of the blocks, or false if no such sequence of moves exists.
; EXAMPLES: 
;  (path-helper (list 2 2) empty empty empty 
;               (list (make-robot-pos-with-prev-move (list 2 1) empty))) =>
;  (list
;   (list "east" 1)
;   (list "south" 1)
;   (list "south" 1)
;   (list "west" 1)
;   (list "north" 1))
;
; STRATEGY: General Recursion
; TERMINATION ARGUMENT: At every recursion, the number of states left in the
; possible states domain decreases. If it decreases to zero, the termination 
; halts. Before all the paths result in false, if we find our target, then the
; recursion also halts. After each recursion, because the number of possible
; paths left for the robot decrease, the problem also becomes easier.
(define (path-helper target blocks moves-played 
                     positions-played candidate-list)
  (cond     
    [(empty? candidate-list) false]  
    [else
     (local
       ((define pos (robot-pos-with-prev-move-robot-pos 
                     (first candidate-list)))
        (define all-moves-pos (all-moves-robot pos target blocks))
        (define new-candidates (robot-list-with-moves pos all-moves-pos))
        (define move (robot-pos-with-prev-move-mov (first candidate-list))))
       (cond     
         ; trivial case: the recursion returns the moves played immediately 
         ; after we find the target
         [(equal? target pos) (append moves-played (list move))]
         ; non-trivial case: pos has existed in the previous postions
         [(member? pos positions-played)
          (path-helper target blocks  moves-played 
                       positions-played (rest candidate-list))]
         ; non-trivial case: otherwise place a recursive call passing the 
         ; new-candidates on the function and append the previous move to 
         ; moves-played and pos to positions-played
         [else
          (local ((define result
                    (path-helper target blocks 
                                 ; if previous move is empty (for start)
                                 ; then don't add it to the moves-splayed
                                 (if (empty? move) moves-played
                                     (append moves-played (list move)))
                                 (append positions-played (list pos)) 
                                 new-candidates)))
            (if (false? result) 
                ; if result is false (no candidates down a path),
                ; then don't go down that path, parse over the rest of 
                ; the candidate-list
                (path-helper target blocks moves-played positions-played
                             (rest candidate-list))
                result))]))]))


;------------------------------------------------------------------------------

; club-moves : Maybe<NELOM> -> Maybe<Plan>
; GIVEN: a Maybe<NELOM> which is either false or contains a list of single step
;        moves such as (list (list "east" 1) (list "east" 1))
; RETURNS: clubs the consecutive moves which are in the same direction. Returns
;          false if moves is false
; EXAMPLES: (club-moves-helper (list (list "east" 1) (list "east" 1))) =>
;                              (list (list "east" 2))
; STRATEGY: Function Composition
(define (club-moves moves)
  (if (false? moves)
      false
      (club-moves-helper moves empty)))


; club-moves-helper : NELOM Plan -> Plan
; GIVEN: a NELOM which contains a list of single step moves such as 
;       (list (list "east" 1) (list "east" 1)) and a Plan which is used for
;       saving the current state of the list
; RETURNS: clubs the consecutive moves which are in the same direction. 
; EXAMPLES: (club-moves-helper (list (list "east" 1)
;                                    (list "east" 1)
;                                    (list "south" 1)) empty) =>
;          (list (list "east" 2) (list "south" 1))
; STRATEGY: Struct Decomp on moves: NonEmptyListOf<Move>
(define (club-moves-helper moves cur-lst)
  (cond
    ; if the list moves contains only one move, append it to cur-lst
    [(empty? (rest moves)) (append cur-lst (list (first moves)))]
    [else
     (if
      (equal? (first (first moves))
              (first (second moves)))
      ; if direction of first two are same
      ; then pass the list (which contains adding 1 to the distance 
      ; of the first element plus the rest of the list 
      ; (which starts from the third element)) to the same function
      (club-moves-helper
       (cons
        (list (first (first moves)) (+ ONE (second (first moves))))
        (third-to-last moves))
       cur-lst)
      
      ; else append the first element to the cur-lst and work on the
      ; rest of moves
      (club-moves-helper (rest moves) 
                         (append cur-lst (list (first moves)))))]))


; third-to-last : NELOM -> ListOf<Move>
; GIVEN: a NELOM 
; RETURNS: the third to last elements of the list. Returns empty if the
;         list length is smaller than 3.
; EXAMPLES: (third-to-last (list (list "east" 1)
;                                (list "west" 1)
;                                (list "south" 1))) =>
;                          (list (list "south" 1))
; STRATEGY: Function Composition
(define (third-to-last moves)
  (if
   (< (length moves) THREE)
   empty
   (third-to-last-helper moves ONE)))


; third-to-last-helper : NELOM PosInt -> NELOM
; GIVEN: a NELOM and a positive integer
; WHERE: n is the position of the current move in the list
; RETURNS: the third to last elements of the list. 
; EXAMPLES: (third-to-last-helper 
;             (list (list "east" 1)
;                   (list "west" 1)
;                   (list "south" 1)
;                   (list "north" 1)
;                   (list "east" 1)) 1) =>
;                
;             (list (list "south" 1) 
;                   (list "north" 1)
;                   (list "east" 1))
; STRATEGY: Function Composition
(define (third-to-last-helper moves n)
 (if (= n THREE)
     moves
     (third-to-last-helper (rest moves) (add1 n))))

;------------------------------------------------------------------------------

; robot-list-with-moves : Position ListOf<Move> -> LORPM
; GIVEN: a position and a list of moves
; RETURNS: a list of robot-pos-with-prev-move which contains the positions
;    after applying the move along with the move applied.
; EXAMPLES: 
; (robot-list-with-moves (list 2 2) (list 
;                                     (list "east" 1)
;                                     (list "west" 1)
;                                     (list "south" 1))) =>
; (list
;  (make-robot-pos-with-prev-move (list 3 2) (list "east" 1))
;  (make-robot-pos-with-prev-move (list 1 2) (list "west" 1))
;  (make-robot-pos-with-prev-move (list 2 3) (list "south" 1)))
;
; STRATEGY: Struct Decomp on moves: ListOf<Move>
(define (robot-list-with-moves robot-pos moves)
  (cond
    [(empty? moves) empty]
    [else (cons (make-robot-pos-with-prev-move 
                 (robot-after-move robot-pos (first moves)) (first moves))
                (robot-list-with-moves robot-pos (rest moves)))]))

;------------------------------------------------------------------------------

; robot-on-block? : Position ListOf<Position> -> Boolean
; GIVEN: a robot position and a list of block positions
; RETURNS: true iff the robot is on a block
; EXAMPLES: (robot-on-block? (list 2 2) (list (list 2 2) (list 2 3))) => true
; STRATEGY: HOFC
(define (robot-on-block? robot-pos blocks)
  (ormap
   ; Position -> Boolean
   ; GIVEN: a block or an occupied position
   ; RETURNS: true iff the robot position is on the occupied position
   ; strategy: function composition
   (lambda (b)
     (equal? robot-pos b))
   blocks))

;------------------------------------------------------------------------------

; robot-after-move : Position Move -> Position
; GIVEN: a robot position and a move
; RETURNS: the robot position after the move
; EXAMPLES: (robot-after-move (list 2 2) (list "east" 1)) => (list 3 2)
; STRATEGY: Struct Decomp on move: Move
(define (robot-after-move robot-pos move)
  (robot-after-move-helper robot-pos (first move) (second move)))

; robot-after-move-helper : Position Direction PosInt -> Position
; GIVEN: a robot position, a direction and a distance
; RETURNS: the robot position after the move
; EXAMPLES: (robot-after-move-helper (list 2 2) "east" 1) => (list 3 2)
; STRATEGY: Struct Decomp on direction: Direction
(define (robot-after-move-helper robot-pos direction distance)
  (cond
    [(string=? direction "north") (robot-after-move-north robot-pos distance)]
    [(string=? direction "east") (robot-after-move-east robot-pos distance)]
    [(string=? direction "south") (robot-after-move-south robot-pos distance)]
    [(string=? direction "west") (robot-after-move-west robot-pos distance)]))


;------------------------------------------------------------------------------

; robot-after-move-east : Position PosInt -> Position
; GIVEN: a robot position and a distance
; RETURNS: the robot position after moving it distance units east
; EXAMPLES: (robot-after-move-east (list 2 2) 1) => (list 3 2)
; STRATEGY: Struct Decomp on robot-pos: Position 
(define (robot-after-move-east robot-pos distance)
  (list (+ (first robot-pos) distance) (second robot-pos)))


; robot-after-move-west : Position PosInt -> Position
; GIVEN: a robot position and a distance
; RETURNS: the robot position after moving it distance units west
; EXAMPLES: (robot-after-move-west (list 2 2) 1) => (list 1 2)
; STRATEGY: Struct Decomp on robot-pos: Position 
(define (robot-after-move-west robot-pos distance)
  (list (- (first robot-pos) distance) (second robot-pos)))


; robot-after-move-south : Position PosInt -> Position
; GIVEN: a robot position and a distance
; RETURNS: the robot position after moving it distance units south
; EXAMPLES: (robot-after-move-south (list 2 2) 1) => (list 2 3)
; STRATEGY: Struct Decomp on robot-pos: Position 
(define (robot-after-move-south robot-pos distance)
  (list (first robot-pos) (+ (second robot-pos) distance)))


; robot-after-move-north : Position PosInt -> Position
; GIVEN: a robot position and a distance
; RETURNS: the robot position after moving it distance units north
; EXAMPLES: (robot-after-move-north (list 2 2) 1) => (list 2 1)
; STRATEGY: Struct Decomp on robot-pos: Position 
(define (robot-after-move-north robot-pos distance)
  (list (first robot-pos) (- (second robot-pos) distance)))


;------------------------------------------------------------------------------

; block-exists-eastwards? : Position Position -> Boolean
; GIVEN: a robot position and a block position
; RETURNS: true iff the block is immediately eastwards of the robot
; EXAMPLES: (block-exists-eastwards? (list 4 5) (list 3 2)) => false
; STRATEGY: Struct Decomp on Position 
(define (block-exists-eastwards? robot-pos block-posn)
  (if (= (second block-posn) (second robot-pos))
      (= (- (first block-posn) (first robot-pos)) ONE)
      false))


; block-exists-westwards? : Position Position -> Boolean
; GIVEN: a robot position and a block position
; RETURNS: true iff the block is immediately westwards of the robot
; EXAMPLES: (block-exists-westwards? (list 4 5) (list 3 2)) => false
; STRATEGY: Struct Decomp on Position 
(define (block-exists-westwards? robot-pos block-posn)
  (if (= (second block-posn) (second robot-pos))
      (= (- (first robot-pos) (first block-posn)) ONE)
      false))


; block-exists-northwards? : Position Position -> Boolean
; GIVEN: a robot position and a block position
; RETURNS: true iff the block is immediately northwards of the robot
; EXAMPLES: (block-exists-northwards? (list 4 5) (list 3 2)) => false
; STRATEGY: Struct Decomp on Position 
(define (block-exists-northwards? robot-pos block-posn)
  (if (= (first block-posn) (first robot-pos))
      (= (- (second robot-pos) (second block-posn)) ONE)
      false))


; block-exists-southwards? : Position Position -> Boolean
; GIVEN: a robot position and a block position
; RETURNS: true iff the block is immediately southwards of the robot
; EXAMPLES: (block-exists-southwards? (list 4 5) (list 3 2)) => false
; STRATEGY: Struct Decomp on Position 
(define (block-exists-southwards? robot-pos block-posn)
  (if (= (first block-posn) (first robot-pos))
      (= (- (second block-posn) (second robot-pos)) ONE)
      false))

;------------------------------------------------------------------------------

; block-eastwards? : Position ListOf<Position> -> Boolean
; GIVEN: a robot position and a list of block positions
; RETURNS: true iff a block from the list is immediately southwards of the robot
; EXAMPLES: (block-eastwards? (list 4 5) (list (list 3 2))) => false
; STRATEGY: HOFC
(define (block-eastwards? robot-pos blocks)
  (ormap 
   ; Position -> Boolean
   ; GIVEN: a block or an occupied position
   ; RETURNS: true iff the block is immediately eastwards of the robot
   ; strategy: function composition
   (lambda (b) 
     (block-exists-eastwards? robot-pos b))
         blocks))

; block-westwards? : Position ListOf<Position> -> Boolean
; GIVEN: a robot position and a list of block positions
; RETURNS: true iff a block from the list is immediately westwards of the robot
; EXAMPLES: (block-westwards? (list 4 5) (list (list 3 2))) => false
; STRATEGY: HOFC
(define (block-westwards? robot-pos blocks)
  (ormap 
   ; Position -> Boolean
   ; GIVEN: a block or an occupied position
   ; RETURNS: true iff the block is immediately westwards of the robot
   ; strategy: function composition
   (lambda (b) 
           (block-exists-westwards? robot-pos b))
         blocks))

; block-northwards? : Position ListOf<Position> -> Boolean
; GIVEN: a robot position and a list of block positions
; RETURNS: true iff a block from the list is immediately northwards of the robot
; EXAMPLES: (block-northwards? (list 4 5) (list (list 3 2))) => false
; STRATEGY: HOFC
(define (block-northwards? robot-pos blocks)
  (ormap 
   ; Position -> Boolean
   ; GIVEN: a block or an occupied position
   ; RETURNS: true iff the block is immediately northwards of the robot
   ; strategy: function composition
   (lambda (b) 
           (block-exists-northwards? robot-pos b))
         blocks))


; block-southwards? : Position ListOf<Position> -> Boolean
; GIVEN: a robot position and a list of block positions
; RETURNS: true iff a block from the list is immediately southwards of the robot
; EXAMPLES: (block-southwards? (list 4 5) (list (list 3 2))) => false
; STRATEGY: HOFC
(define (block-southwards? robot-pos blocks)
  (ormap 
   ; Position -> Boolean
   ; GIVEN: a block or an occupied position
   ; RETURNS: true iff the block is immediately southwards of the robot
   ; strategy: function composition
   (lambda (b) 
           (block-exists-southwards? robot-pos b))
         blocks))

;------------------------------------------------------------------------------

; find-right-most-x : ListOf<Position> Position -> PosInt
; GIVEN: a list of block positions and the target position
; RETURNS: the right-most x-coordinate of the blocks and target
; EXAMPLES: (find-right-most-x (list (list 3 2)) (list 4 5)) => 4
; STRATEGY: HOFC
(define (find-right-most-x blocks target)
 (foldr
  ; Position PosInt -> PosInt
  ; GIVEN : a block position and a positive integer
  ; RETURNS : the max of the x coordinate of the block and the target
  ; STRATEGY: Struct Decomp on Position
  (lambda (b right-most-x-so-far)
    (max
     (first b)
     (first target)
     right-most-x-so-far))
    (first target) blocks))
  

; find-down-most-y : ListOf<Position> Position -> PosInt
; GIVEN: a list of block positions and the target position
; RETURNS: the down-most y-coordinate of the blocks and target
; EXAMPLES: (find-down-most-y (list (list 3 2)) (list 4 5)) => 5
; STRATEGY: HOFC
(define (find-down-most-y blocks target)
 (foldr
  ; Position PosInt -> PosInt
  ; GIVEN : a block position and a positive integer
  ; RETURNS : the max of the y coordinate of the block and the target
  ; STRATEGY: Struct Decomp on Position
  (lambda (b down-most-y-so-far)
    (max
     (second b)
     (second target)
     down-most-y-so-far))
    (second target) blocks))


; find-left-most-x : ListOf<Position> Position -> PosInt
; GIVEN: a list of block positions and the target position
; RETURNS: the left-most x-coordinate of the blocks and target
; EXAMPLES: (find-left-most-x (list (list 3 2)) (list 4 5)) => 3
; STRATEGY: HOFC
(define (find-left-most-x blocks target)
 (foldr
  ; Position PosInt -> PosInt
  ; GIVEN : a block position and a positive integer
  ; RETURNS : the min of the x coordinate of the block and the target
  ; STRATEGY: Struct Decomp on Position
  (lambda (b left-most-x-so-far)
    (min
     (first b)
     (first target)
     left-most-x-so-far))
    (first target) blocks))


; find-top-most-y : ListOf<Position> Position -> PosInt
; GIVEN: a list of block positions and the target position
; RETURNS: the top-most y-coordinate of the blocks and target
; EXAMPLES: (find-top-most-y (list (list 3 2)) (list 4 5)) => 2
; STRATEGY: HOFC
(define (find-top-most-y blocks target)
 (foldr
  ; Position PosInt -> PosInt
  ; GIVEN : a block position and a positive integer
  ; RETURNS : the min of the y coordinate of the block and the target
  ; STRATEGY: Struct Decomp on Position
  (lambda (b top-most-y-so-far)
    (min
     (second b)
     (second target)
     top-most-y-so-far))
    (second target) blocks))

;------------------------------------------------------------------------------

; can-move-east? : Position Position ListOf<Position> -> Boolean
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: true iff there is no block eastwards or the x of robot-pos is not 
;    greater than the right-most x-coordinate of the blocks and target
; EXAMPLES: (can-move-east? (list 1 1) (list 1 2) empty) => true
; STRATEGY: Function Composition
(define (can-move-east? robot-pos target blocks)
  (and (not (> (first robot-pos) (find-right-most-x blocks target)))
       (not (block-eastwards? robot-pos blocks))))


; can-move-south? : Position Position ListOf<Position> -> Boolean
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: true iff there is no block southwards or y of the robot-pos is not 
;    greater than the down-most y-coordinate of the blocks and target
; EXAMPLES: (can-move-south? (list 1 1) (list 1 2) empty) => true
; STRATEGY: Function Composition
(define (can-move-south? robot-pos target blocks)
  (and (not (> (second robot-pos) (find-down-most-y blocks target)))
       (not (block-southwards? robot-pos blocks))))


; can-move-west? : Position Position ListOf<Position> -> Boolean
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: true iff there is no block westwards or x of the robot-pos is not 
;    lesser than the left-most x-coordinate of the blocks and target or
;    if it is at x coordinate = 1
; EXAMPLES: (can-move-west? (list 1 1) (list 1 2) empty) => false
; STRATEGY: Function Composition
(define (can-move-west? robot-pos target blocks)
  (and (not (= ONE (first robot-pos)))
       (not (< (first robot-pos) (find-left-most-x blocks target)))
       (not (block-westwards? robot-pos blocks))))


; can-move-north? : Position Position ListOf<Position> -> Boolean
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: true iff there is no block northwards or y of the robot-pos is not 
;    lesser than the top-most y-coordinate of the blocks and target or
;    if it is at y coordinate = 1
; EXAMPLES: (can-move-north? (list 1 1) (list 1 2) empty) => false
; STRATEGY: Function Composition
(define (can-move-north? robot-pos target blocks)
  (and (not (= ONE (second robot-pos)))
       (not (< (second robot-pos) (find-top-most-y blocks target)))
       (not (block-northwards? robot-pos blocks))))


;------------------------------------------------------------------------------

; all-moves-robot : Position Position ListOf<Position> -> ListOf<Move>
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: all the eligible moves for the robot for a distance of 1. This 
;  function filters out the empty elements from the helper function
; EXAMPLES: (all-moves-robot (list 1 1) (list 1 2) empty) =>
;           (list (list "east" 1) (list "south" 1))
; STRATEGY: HOFC
(define (all-moves-robot robot-pos target blocks)
  (filter 
   ; Move -> Boolean
   ; GIVEN: a move
   ; RETURNS: true iff the move is not empty.
   (lambda (m)
     (not (empty? m)))
   (all-moves-helper robot-pos target blocks)))


; all-moves-helper : Position Position ListOf<Position> -> ListOf<Move>
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: all the eligible moves for the robot for a distance of 1
; EXAMPLES: (all-moves-helper (list 1 1) (list 1 2) empty) =>
;           (list empty (list "east" 1) (list "south" 1) empty)
; STRATEGY: Function Composition
(define (all-moves-helper robot-pos target blocks)
  (list
   (all-moves-helper-north robot-pos target blocks)
   (all-moves-helper-east robot-pos target blocks)
   (all-moves-helper-south robot-pos target blocks)
   (all-moves-helper-west robot-pos target blocks)))


; all-moves-helper-north : Position Position ListOf<Position> -> ListOf<Move>
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: empty if the robot cannot move in the north direction, else 
;        (list "north" 1)
; EXAMPLES: (all-moves-helper-north (list 1 1) (list 1 2) empty) =>
;           empty
; STRATEGY: Function Composition
(define (all-moves-helper-north robot-pos target blocks)
  (if (false? (can-move-north? robot-pos target blocks))
      empty
      (list "north" 1)))


; all-moves-helper-east : Position Position ListOf<Position> -> ListOf<Move>
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: empty if the robot cannot move in the east direction, else 
;        (list "east" 1)
; EXAMPLES: (all-moves-helper-east (list 1 1) (list 1 2) empty) =>
;           (list "east" 1)
; STRATEGY: Function Composition
(define (all-moves-helper-east robot-pos target blocks)
  (if (false? (can-move-east? robot-pos target blocks))
      empty
      (list "east" 1)))


; all-moves-helper-south : Position Position ListOf<Position> -> ListOf<Move>
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: empty if the robot cannot move in the south direction, else 
;        (list "south" 1)
; EXAMPLES: (all-moves-helper-south (list 1 1) (list 1 2) empty) =>
;           (list "south" 1)
; STRATEGY: Function Composition
(define (all-moves-helper-south robot-pos target blocks)
  (if (false? (can-move-south? robot-pos target blocks))
      empty
      (list "south" 1)))


; all-moves-helper-west : Position Position ListOf<Position> -> ListOf<Move>
; GIVEN: a robot position, the target position and a list of block positions
; RETURNS: empty if the robot cannot move in the west direction, else 
;        (list "west" 1)
; EXAMPLES: (all-moves-helper-west (list 1 1) (list 1 2) empty) =>
;           empty
; STRATEGY: Function Composition
(define (all-moves-helper-west robot-pos target blocks)
  (if (false? (can-move-west? robot-pos target blocks))
      empty
      (list "west" 1)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; TESTS
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Examples for Testing
(define blocks-1 (list (list 2 2) (list 1 2)))

(define blocks-2 (list (list 3 4) (list 3 2)
                       (list 2 3) (list 4 3)))

(define blocks-3 (list (list 3 4) (list 3 2)
                       (list 1 1) (list 4 3)))

(define robot-pos-1 (list 1 1))

(define target-pos-1 (list 3 3))
(define target-pos-2 (list 1 1))

(define move-list-1 
  (list
   (list "east" 3)
   (list "south" 3)
   (list "west" 1)
   (list "north" 1)))

(define move-list-5
  (list (list "east" 1)
        (list "east" 1)
        (list "south" 1)))

(define move-list-6
  (list (list "east" 1)
        (list "east" 1)))

(define move-list-6-after-club
  (list (list "east" 2)))

        
(define move-list-5-after-club
  (list (list "east" 2) 
        (list "south" 1)))

(define-test-suite path-tests
  
  (check-equal? (path robot-pos-1 target-pos-1 blocks-1)
                move-list-1
                "move-list-1 should be returned when the inputs are
                robot-pos-1, target-pos-1 and blocks-1")
  
  (check-equal? (path robot-pos-1 target-pos-1 blocks-2)
                false
                "false should be returned when the inputs are
                robot-pos-1, target-pos-1 and blocks-2 because the target
                is surrounded by blocks")

  (check-equal? (path robot-pos-1 target-pos-2 blocks-1)
                empty
                "empty should be returned when the inputs are
                robot-pos-1, target-pos-2 and blocks-1 because the target
                is the current robot-pos")
  
  (check-equal? (path robot-pos-1 target-pos-2 blocks-3)
                false
                "false should be returned when the inputs are
                robot-pos-1, target-pos-2 and blocks-1 because the robot
                is on a block")

  (check-equal? (club-moves move-list-5)
                move-list-5-after-club
                "move-list-5-after-club should be returned if
                move-list-5 is passed")
  
  (check-equal? (club-moves move-list-6)
                move-list-6-after-club
                "move-list-6-after-club should be returned if
                move-list-6 is passed"))


(run-tests path-tests)




