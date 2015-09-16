;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-abbr-reader.ss" "lang")((modname balls-in-box) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require 2htdp/universe)
(require 2htdp/image)
(require "extras.rkt")

(provide run)
(provide initial-world)
(provide world-after-key-event)
(provide world-after-mouse-event)
(provide world-balls)
(provide ball-x-pos)
(provide ball-y-pos)
(provide ball-selected?)


;============================================================================
; CONSTANTS
;============================================================================

(define CANVAS-WIDTH 400)
(define CANVAS-HEIGHT 300)
(define DUMMY-VALUE 100)
(define HALF-CANVAS-WIDTH (/ CANVAS-WIDTH 2))
(define HALF-CANVAS-HEIGHT (/ CANVAS-HEIGHT 2))
(define EMPTY-CANVAS (empty-scene CANVAS-WIDTH CANVAS-HEIGHT))
(define BALL-RADIUS 20)
(define SOLID-RED-BALL  (circle BALL-RADIUS "solid" "red"))
(define HOLLOW-RED-BALL (circle BALL-RADIUS "outline" "red"))
(define NO-OF-BALL-POSITION-X 64)
(define NO-OF-BALL-POSITION-Y 15)


;============================================================================
; DATA DEFINITIONS
;============================================================================

(define-struct ball (x-pos y-pos mouse-dist-x mouse-dist-y selected?))
; A Ball is a (make-ball Number Number Number Number Boolean)
; Interp:
; -- x-pos, y-pos give the position of the center of the ball
; -- mouse-dist-x, mouse-dist-y give the position of the mouse pointer
;    from the center of the ball
; -- selected? shows whether or not the ball is selected

; TEMPLATE:
; ball-fn : Ball -> ??
; (define (ball-fn b)
;  (... (ball-x-pos b)
;       (ball-y-pos b)
;       (ball-mouse-dist-x b)
;       (ball-mouse-dist-y b)
;       (ball-selected? b)))


; A ListOfBalls is one of:
; -- empty
; -- (cons Ball ListOfBalls)

; TEMPLATE:
; balls-fn : ListOfBalls -> ??
; (define (balls-fn balls)
;   (cond
;     [(empty? balls) ...]
;     [else (...
;             (first balls)
;             (balls-fn (rest balls)))]))


(define-struct world (balls no-of-balls))
; A World is a (make-world ListOfBalls Number)
; Interp:
; -- balls is the ListOfBalls in the world
; -- no-of-balls is the number of balls in the world

; TEMPLATE:
; world-fn : World -> ??
; (define (world-fn w)
;   (... (world-balls w)
;        (world-no-of-balls w)))


; A BallKeyEvent is a KeyEvent that is one of:
; -- "n"                  (interp: create a new ball in the 
;                                  middle of the screen)
;
; -- any other key event  (interp: ignored)

; TEMPLATE:
; (define (ball-key-event-fn kev)
;  (cond
;    [(key=? kev "n") ...]
;    [else ...]))


; A BallMouseEvent is a MouseEvent that is one of:
; -- "button-down"   (interp: maybe select the ball)
; -- "drag"          (interp: maybe drag the ball)
; -- "button-up"     (interp: unselect the ball)
; -- any other mouse event (interp: ignored)

; TEMPLATE:
; (define (ball-mouse-event-fn mev)
;  (cond
;    [(mouse=? mev "button-down") ...]
;    [(mouse=? mev "drag") ...]
;    [(mouse=? mev "button-up") ...]
;    [else ...]))


;============================================================================
; FUNCTION DEFINITIONS
;============================================================================

; run : Any -> World
; GIVEN: any value
; EFFECT: Ignores its argument and runs the world.
; EXAMPLES: (run 0) -> starts a world program
; STRATEGY: Function Composition
(define (run any-val)
  (big-bang (initial-world any-val)
            (on-draw world->scene)
            (on-key world-after-key-event)
            (on-mouse world-after-mouse-event)))


; initial-world : Any -> World
; GIVEN: An argument, which is ignored.
; RETURNS: a world with no balls.
; EXAMPLES: See tests below
; STRATEGY: Function Composition
(define (initial-world any-val)
  (make-world empty 0))

; TESTS

(check-equal? (initial-world 2) (make-world empty 0)
              "initial-world should be a world with an empty
               list and number of balls e")


; place-ball : Ball Scene -> Scene
; GIVEN: a Ball and a scene
; RETURNS: a scene like the given one, but with the given Ball painted
;          on it.
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on b: Ball
(define (place-ball b s)
  (if (ball-selected? b)
      (place-image SOLID-RED-BALL (ball-x-pos b) (ball-y-pos b) s)
      (place-image HOLLOW-RED-BALL (ball-x-pos b) (ball-y-pos b) s)))


; Examples for testing

(define ball-1 (make-ball HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT  
              DUMMY-VALUE DUMMY-VALUE false))

(define unselected-ball-in-the-middle 
  (make-ball HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT  
              DUMMY-VALUE DUMMY-VALUE false))

(define selected-ball-in-the-middle 
  (make-ball HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT  
              DUMMY-VALUE DUMMY-VALUE true))

(define unselected-ball-in-the-middle-image 
  (place-image HOLLOW-RED-BALL HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT 
               EMPTY-CANVAS))

(define selected-ball-in-the-middle-image 
  (place-image SOLID-RED-BALL HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT 
               EMPTY-CANVAS))

; TESTS

(define-test-suite place-ball-tests
  
  (check-equal? (place-ball unselected-ball-in-the-middle EMPTY-CANVAS)
                unselected-ball-in-the-middle-image
                "The image created by the function should be
                 UNSELECTED-BALL-IN-THE-MIDDLE-IMAGE")

  (check-equal? (place-ball selected-ball-in-the-middle EMPTY-CANVAS)
                selected-ball-in-the-middle-image
                "The image created by the function should be
                 SELECTED-BALL-IN-THE-MIDDLE-IMAGE"))

(run-tests place-ball-tests)
                

; world->scene : World -> Scene
; GIVEN : a world
; RETURNS: a Scene that portrays the given world.
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on w: World
(define (world->scene w)
  (cond
    [(empty? (world-balls w)) 
     (place-image 
      (text 
       (string-append "Number of Balls: " 
                      (number->string(world-no-of-balls w)))12 "Black") 
      
      NO-OF-BALL-POSITION-X NO-OF-BALL-POSITION-Y
      EMPTY-CANVAS)]
    
    [else (place-ball (first (world-balls w)) 
                      (world->scene (make-world (rest (world-balls w)) 
                                                (world-no-of-balls w))))]))
; Eamples for testing

(define world-no-balls-image 
  (place-image  
   (text (string-append "Number of Balls: " 
          (number->string 0)) 12 "Black")
      NO-OF-BALL-POSITION-X NO-OF-BALL-POSITION-Y
      EMPTY-CANVAS))

(define world-1-ball-image 
  (place-image  
   (text (string-append "Number of Balls: " 
          (number->string 1)) 12 "Black")
      NO-OF-BALL-POSITION-X NO-OF-BALL-POSITION-Y
      (place-ball unselected-ball-in-the-middle EMPTY-CANVAS) ))

; TESTS

(define-test-suite world->scene-tests
  (check-equal? (world->scene (make-world empty 0))
                world-no-balls-image "a world with no balls should
                                      be displayed as world-no-balls-image")

 (check-equal? (world->scene (make-world 
                              (cons unselected-ball-in-the-middle empty) 1))
                world-1-ball-image "a world with 1 unselected ball should
                                      be displayed as world-1-ball-image"))
                            
(run-tests world->scene-tests)


; distance-from-center : Ball Number Number -> Number
; GIVEN : A Ball and a mouse coordinate
; RETURNS: The distance of the mouse coordinate from the center of
;          the ball
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on b: Ball
(define (distance-from-center b mx my)
  (sqrt 
   (+ (sqr (- mx (ball-x-pos b)))
      (sqr (- my (ball-y-pos b))))))
      
; TESTS

(check-equal? (distance-from-center ball-1 20 30)
              #i216.33307652783935 "For the given ball location
              and mouse coordinate #i216.33307652783935 should
              be returned")


; in-ball? : Ball Number Number -> Boolean
; GIVEN : A Ball and a mouse coordinate
; RETURNS: true iff the given coordinate is inside the ball
; EXAMPLES: See tests below
; STRATEGY: Function Composition
(define (in-ball? b mx my)
  (if (<= (distance-from-center b mx my) BALL-RADIUS)
      true
      false))

; TESTS

(define-test-suite in-ball-tests
  
  (check-equal? (in-ball? ball-1 20 30) false 
                "false should be returned for a coordinate outside the ball")
  (check-equal? (in-ball? ball-1 HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT)
              true 
              "true should be returned for a coordinate inside the ball"))

(run-tests in-ball-tests)
 

; ball-after-button-down : Ball Number Number -> Ball
; GIVEN:  a Ball and a mouse coordinate
; RETURNS: if the coordinates are inside the ball, then 
;          the ball becomes selected
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on b: Ball
(define (ball-after-button-down b mx my)
  (if (in-ball? b mx my)
      (make-ball (ball-x-pos b) (ball-y-pos b) 
                  (- mx (ball-x-pos b)) (- my (ball-y-pos b))
                  true)
       b))

; TESTS

(define-test-suite ball-after-button-down-tests
  
  (check-equal? (ball-after-button-down ball-1 5 5) ball-1
                "If the mouse coordinate is outside the ball then the same
                  ball is returned")
  (check-equal? 
   (ball-after-button-down ball-1 HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT)
   (make-ball 200 150 0 0 true)
   "If the mouse coordinate is inside the ball then the ball becomes 
    selected"))

(run-tests ball-after-button-down-tests)


; ball-after-drag : Ball Number Number -> Ball
; GIVEN:  a Ball and a mouse coordinate
; RETURNS: the Ball following a drag at the given location.
;          if the Ball is selected, the new ball is made at the
;          mouse location
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on b: Ball
(define (ball-after-drag b mx my)
 (if (ball-selected? b) 
   (make-ball (- mx (ball-mouse-dist-x b)) (- my (ball-mouse-dist-y b))
               (ball-mouse-dist-x b) (ball-mouse-dist-y b)
               true)
    b))

; TESTS

(define-test-suite ball-after-drag-tests
  
  (check-equal? (ball-after-drag ball-1 5 5) ball-1
                "If the mouse coordinate is outside the ball then the same
                  ball is returned")
  (check-equal? (ball-after-drag selected-ball-in-the-middle 
                                 HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT) 
                (make-ball 100 50 100 100 true)
                "If the ball is selected a new ball is made at 
                 the mouse location selected"))

(run-tests ball-after-drag-tests)


; ball-after-button-up : Ball Number Number -> Ball
; GIVEN:   a Ball and a mouse coordinate
; RETURNS: the ball following a button-up at the given location.
;          if the ball is selected, then unselect the ball.
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on b: Ball
(define (ball-after-button-up b mx my)
  (if (ball-selected? b)
      (make-ball  (- mx (ball-mouse-dist-x b)) 
                  (- my (ball-mouse-dist-y b))
                  DUMMY-VALUE DUMMY-VALUE false)
      b))

(define-test-suite world-after-button-up-tests
  
  (check-equal? (ball-after-button-up ball-1 5 5) ball-1
                "If the ball is unselected then the same
                  ball is returned")
  (check-equal? (ball-after-button-up selected-ball-in-the-middle 
                                      HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT) 
             (make-ball 100 50 100 100 false)
              "If the ball is selected then the ball becomes unselected"))

(run-tests world-after-button-up-tests)


; ball-after-mouse-event : Ball Number Number BallMouseEvent -> Ball
; GIVEN:   a Ball, a mouse coordinate and a BallMouseEvent
; RETURNS: the ball following the BallMouseEvent at the given location.
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on mev: BallMouseEvent
(define (ball-after-mouse-event b mx my mev)
  (cond
    [(mouse=? mev "button-down") (ball-after-button-down b mx my)]
    [(mouse=? mev "drag") (ball-after-drag b mx my)]
    [(mouse=? mev "button-up") (ball-after-button-up b mx my)]
    [else b]))

; TESTS

(define-test-suite ball-after-mouse-event-tests
  
  (check-equal? 
   (ball-after-mouse-event ball-1 4 5 "button-down")
   (ball-after-button-down ball-1 4 5)
   "If button-down BallKeyEvent occurs, then ball-after-button-down
    function is called")
  
  (check-equal? 
   (ball-after-mouse-event ball-1 4 5 "drag")
   (ball-after-drag ball-1 4 5)
   "If drag BallKeyEvent occurs, then ball-after-drag
    function is called")
  
  (check-equal? 
   (ball-after-mouse-event ball-1 4 5 "button-up")
   (ball-after-button-up ball-1 4 5)
   "If button-up BallKeyEvent occurs, then ball-after-button-up
    function is called")

  
  (check-equal? 
   (ball-after-mouse-event ball-1 4 5 "leave")
   ball-1)
  "If a leave BallKeyEvent occurs then it is ignored")

(run-tests ball-after-mouse-event-tests)
              

; balls-after-mouse-event : ListOfBalls Number Number BallMouseEvent 
;                           -> ListOfBalls
; GIVEN:   a ListOfBalls, a mouse coordinate and a BallMouseEvent
; RETURNS: the ListOfBalls following the BallMouseEvent at the 
;          given location.
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on balls: ListOfBalls
(define (balls-after-mouse-event balls mx my mev)
  (cond [(empty? balls) empty]
        [else (cons (ball-after-mouse-event (first balls) mx my mev)
                    (balls-after-mouse-event (rest balls) mx my mev))]))

; TESTS

(check-equal? (balls-after-mouse-event (list ball-1) 80 80 "button-up")
              (list (make-ball 200 150 100 100 false))
              "The button-up mouse event is outside any ball, so the
                same ball should be returned")


; world-after-mouse-event : World Number Number BallMouseEvent 
;                           -> World
; GIVEN:   a World, a mouse coordinate and a BallMouseEvent
; RETURNS: the World following the BallMouseEvent at the 
;          given location.
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on w: World
(define (world-after-mouse-event w mx my mev)
  (make-world (balls-after-mouse-event (world-balls w) mx my mev)
               (world-no-of-balls w)))

; TESTS

(check-equal? (world-after-mouse-event 
               (make-world (list ball-1) 1) 70 70 "drag")
              (make-world (list (make-ball 200 150 100 100 false)) 1)
              "Since the ball is unselected there should be no effect
              of the drag mouse event")


; world-after-key-event-n : World -> World
; GIVEN:   a World
; RETURNS: the World following the BallKeyEvent = "n"
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on w: World
(define (world-after-key-event-n w)
  (make-world 
   (cons unselected-ball-in-the-middle (world-balls w)) 
   (+ 1 (world-no-of-balls w))))
  

; TESTS

(check-equal? (world-after-key-event-n (make-world (list ball-1) 1))
              (make-world
               (list
                (make-ball 200 150 100 100 false)
                (make-ball 200 150 100 100 false))
               2)
              "world-after-key-event-n should add another ball to the world")


; world-after-key-event : World BallKeyEvent -> World
; GIVEN: a World
; RETURNS: the world that should follow the given world
;          after the given key event.
;          -- "n" -> make a new ball
;          -- ignore all others
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on kev: BallKeyEvent
(define (world-after-key-event w kev)
  (cond
    [(key=? kev "n") (world-after-key-event-n w)]  
    [else w]))

; TESTS

(define-test-suite world-after-key-event-tests
  
  (check-equal? 
   (world-after-key-event (make-world empty 0) "n") 
   (world-after-key-event-n (make-world empty 0))
   "n BallKeyEvent should call the function world-after-key-event-n") 
  
  (check-equal? 
   (world-after-key-event (make-world empty 0) "k") (make-world empty 0)
   "any event other than n should be ignored"))

(run-tests world-after-key-event-tests)
