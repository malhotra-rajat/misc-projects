;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname balls-in-box) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require 2htdp/universe)
(require 2htdp/image)
(require "extras.rkt")

(provide run)
(provide initial-world)
(provide world-after-tick)
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
(define DVALUE 30)
(define HALF-CANVAS-WIDTH (/ CANVAS-WIDTH 2))
(define HALF-CANVAS-HEIGHT (/ CANVAS-HEIGHT 2))
(define EMPTY-CANVAS (empty-scene CANVAS-WIDTH CANVAS-HEIGHT))
(define BALL-RADIUS 20)
(define SOLID-RED-BALL  (circle BALL-RADIUS "solid" "red"))
(define HOLLOW-RED-BALL (circle BALL-RADIUS "outline" "red"))
(define NO-OF-BALL-POSITION-X 64)
(define NO-OF-BALL-POSITION-Y 15)
(define TEXT-SIZE 12)


;============================================================================
; DATA DEFINITIONS
;============================================================================

; A Direction is one of:
; -- right
; -- left

; TEMPLATE:
;(define (dir-fn dir)
;(cond
;  [(string=? dir "right") ...]
;  [(string=? dir "left") ...]))


(define-struct ball (x-pos y-pos mouse-dist-x 
                           mouse-dist-y selected? direction))
; A Ball is a (make-ball Number Number Number Number Boolean Direction)
; Interp:
; -- x-pos, y-pos give the position of the center of the ball
; -- mouse-dist-x, mouse-dist-y give the position of the mouse pointer
;    from the center of the ball
; -- selected? shows whether or not the ball is selected
; -- direction is the direction of the ball (right or left)

; TEMPLATE:
; ball-fn : Ball -> ??
; (define (ball-fn b)
;  (... (ball-x-pos b)
;       (ball-y-pos b)
;       (ball-mouse-dist-x b)
;       (ball-mouse-dist-y b)
;       (ball-selected? b)
;       (ball-direction b)))


; A ListOfBalls is one of:
; -- empty                      interp: the world has no balls to display
; -- (cons Ball ListOfBalls)    interp: the world has at least 
;                                       1 ball to display

; TEMPLATE:
; balls-fn : ListOfBalls -> ??
; (define (balls-fn balls)
;   (cond
;     [(empty? balls) ...]
;     [else (...
;             (ball-fn (first balls))
;             (balls-fn (rest balls)))]))


(define-struct world (balls no-of-balls paused? speed))
; A World is a (make-world ListOfBalls Boolean Number)
; Interp:
; -- balls is the ListOfBalls in the world
; -- no-of-balls is the number of balls in the world
; -- paused? represents whether or not the world is paused
; -- speed is the speed with which the balls move in the world

; TEMPLATE:
; world-fn : World -> ??
; (define (world-fn w)
;   (... (world-balls w)
;        (world-no-of-balls w)
;        (world-paused? w)
;        (world-speed w)))


; A BallKeyEvent is a KeyEvent that is one of:
; -- "n"                  (interp: create a new ball in the 
;                                  middle of the screen)
; -- " "                  (interp: pause/unpause the world)
; -- any other key event  (interp: ignored)

; TEMPLATE:
; (define (ball-key-event-fn kev)
;  (cond
;    [(key=? kev "n") ...]
;    [(key=? kev " ") ...]
;    [else ...]))


; A BallMouseEvent is a MouseEvent that is one of:
; -- "button-down"         (interp: maybe select the ball)
; -- "drag"                (interp: maybe drag the ball)
; -- "button-up"           (interp: unselect the ball)
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

; run : PosInt PosReal -> World
; GIVEN: a ball speed and a frame rate, in secs/tick
; EFFECT: runs the world.
; EXAMPLES: (run 8 .25) creates and runs a world in
;           which each ball travels at 8 pixels per tick and
;           each tick is 0.25 secs.
; STRATEGY: Function Composition
(define (run speed frame-rate)
  (big-bang (initial-world speed)
            (on-draw world->scene)
            (on-tick world-after-tick frame-rate)
            (on-key world-after-key-event)
            (on-mouse world-after-mouse-event)))



; balls-after-tick : ListOfBalls PosInt -> ListOfBalls
; GIVEN: a list of balls and a speed
; RETURNS: the balls after the tick moved by the given speed (right or left)
; EXAMPLES: (balls-after-tick (list (make-ball 25 25 0 0 false "right")) 8) =>
;                (list (make-ball 33 25 0 0 false "right")) 
; STRATEGY: Higher-Order Function Composition
(define (balls-after-tick balls speed)
  (map 
   ; Ball -> Ball
   ; RETURNS: the ball after a tick
   (lambda (b)
     (ball-after-tick b speed))
   balls))



; ball-after-tick : Ball PosInt -> Ball
; GIVEN: a ball and a speed
; RETURNS: the ball after the tick moved by the given speed (right or left)
; EXAMPLES:  (ball-after-tick (make-ball 33 25 0 0 false "right") 8) =>
;            (make-ball 41 25 0 0 false "right")
; STRATEGY: Sructural Decomposition on b: Ball
(define (ball-after-tick b speed)
  (ball-after-tick-helper b (ball-direction b) speed))


; ball-after-tick-helper : Ball Direction PosInt -> Ball
; GIVEN: a ball, a direction and a speed
; RETURNS: the ball after the tick moved by the given speed (right or left)
; EXAMPLES:  (ball-after-tick-helper (make-ball 33 25 0 0 false "right")
;             "right" 8) => (make-ball 41 25 0 0 false "right")
; STRATEGY: Struct Decomp on direction: Direction
(define (ball-after-tick-helper b direction speed)
  (cond
    [(string=? direction "right") (ball-after-tick-helper-right b speed)]
    [(string=? direction "left") (ball-after-tick-helper-left b speed)]))



; move-ball-rightwards : Ball PosInt -> Ball
; GIVEN: a ball and a speed
; RETURNS: the ball moved rightwards by the given speed
; EXAMPLES: (move-ball-rightwards (make-ball 355 131 100 100 false "right") 8)
;          =>  (make-ball 363 131 100 100 false "right")
; STRATEGY: Structural Decomposition on b: Ball
(define (move-ball-rightwards b speed)
  (make-ball 
   (+ (ball-x-pos b) speed) (ball-y-pos b)
   (ball-mouse-dist-x b) (ball-mouse-dist-y b)
   (ball-selected? b) (ball-direction b)))


; move-ball-leftwards : Ball PosInt -> Ball
; GIVEN: a ball and a speed
; RETURNS: the ball moved leftwards by the given speed
; EXAMPLES: (move-ball-leftwards (make-ball 355 131 100 100 false "left") 8)
;          =>  (make-ball 347 131 100 100 false "left")
; STRATEGY: Structural Decomposition on b: Ball
(define (move-ball-leftwards b speed)
  (make-ball 
   (- (ball-x-pos b) speed) (ball-y-pos b)
   (ball-mouse-dist-x b) (ball-mouse-dist-y b)
   (ball-selected? b) (ball-direction b)))


; ball-after-tick-helper-right : Ball PosInt -> Ball
; GIVEN: a ball and a speed
; RETURNS: the ball moved rightwards by the given speed. 
;          If the ball is at the right boundary, then it bounces back and
;          moves leftwards
; EXAMPLES: (ball-after-tick-helper-right 
;              (make-ball 355 131 100 100 false "right") 8)
;          =>  (make-ball 363 131 100 100 false "right")
; STRATEGY: Structural Decomposition on b: Ball
(define (ball-after-tick-helper-right b speed)
  (if (ball-selected? b)
      b
      (if (>= (+ (ball-x-pos b) speed)
              (- CANVAS-WIDTH BALL-RADIUS))
          
          (bounce-ball-from-right-boundary b speed)
          (move-ball-rightwards b speed))))

; ball-after-tick-helper-left : Ball PosInt -> Ball
; GIVEN: a ball and a speed
; RETURNS: the ball moved leftwards by the given speed. 
;          If the ball is at the left boundary, then it bounces back and
;          moves rightwards
; EXAMPLES: (ball-after-tick-helper-left
;              (make-ball 355 131 100 100 false "left") 8)
;          =>  (make-ball 347 131 100 100 false "left")
; STRATEGY: Structural Decomposition on b: Ball
(define (ball-after-tick-helper-left b speed)
  (if (ball-selected? b)
      b
      (if (<= (- (ball-x-pos b) speed)
              BALL-RADIUS)
          (bounce-ball-from-left-boundary b speed)
          (move-ball-leftwards b speed))))


; bounce-ball-from-left-boundary : Ball PosInt -> Ball
; GIVEN: a ball and a speed
; RETURNS: when the ball tries to move across the left boundary,
;          it is stopped and the direction is reversed
; EXAMPLES: (bounce-ball-from-left-boundary  
;             (make-ball 5 131 100 100 false "left") 8)
;          =>  (make-ball 20 131 100 100 false "right")
; STRATEGY: Structural Decomposition on b: Ball
(define (bounce-ball-from-left-boundary b speed)
  (ball-after-tick-helper-right 
   (make-ball 
    (- BALL-RADIUS speed) (ball-y-pos b)
    (ball-mouse-dist-x b) (ball-mouse-dist-y b)
    (ball-selected? b) "right")
   speed))


; bounce-ball-from-right-boundary : Ball PosInt -> Ball
; GIVEN: a ball and a speed
; RETURNS: when the ball tries to move across the right boundary,
;          it is stopped and the direction is reversed
; EXAMPLES: (bounce-ball-from-right-boundary  
;            (make-ball 385 131 100 100 false "right") 8)
;          =>  (make-ball 380 131 100 100 false "left")
; STRATEGY: Structural Decomposition on b: Ball 
(define (bounce-ball-from-right-boundary b speed)
  (ball-after-tick-helper-left 
   (make-ball 
    (+ (- CANVAS-WIDTH BALL-RADIUS) speed)
    (ball-y-pos b) (ball-mouse-dist-x b)
    (ball-mouse-dist-y b) (ball-selected? b) "left")
   speed))



; Examples for testing

(define unselected-ball-in-the-middle-moving-right 
  (make-ball HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT  
             DVALUE DVALUE false "right"))

(define selected-ball-in-the-middle-moving-right 
  (make-ball HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT  
             DVALUE DVALUE true "right"))

(define selected-ball-in-the-middle-moving-left 
  (make-ball HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT  
             DVALUE DVALUE true "left"))

(define unselected-ball-in-the-middle-image 
  (place-image HOLLOW-RED-BALL HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT 
               EMPTY-CANVAS))

(define selected-ball-in-the-middle-image 
  (place-image SOLID-RED-BALL HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT 
               EMPTY-CANVAS))

;(define ball-10 (make-ball 25 25 0 0 false "right"))
(define ball-almost-at-right-boundary 
  (make-ball 395 50 DVALUE DVALUE false "right"))
(define ball-almost-at-left-boundary 
  (make-ball 5 50 DVALUE DVALUE false "left"))

; TESTS

(define-test-suite ball-after-tick-tests
  
  (check-equal? 
   (ball-after-tick-helper-left ball-almost-at-left-boundary 8)
   (make-ball 20 50 DVALUE DVALUE false "right")
   "A ball almost at left boundary should be bounced
              back and direction changed")
  
  (check-equal? 
   (bounce-ball-from-right-boundary ball-almost-at-right-boundary 8)
   (make-ball 380 50 DVALUE DVALUE false "left")
   "A ball almost at right boundary should be bounced
              back and direction changed")
  
  (check-equal? 
   (ball-after-tick-helper-right selected-ball-in-the-middle-moving-left 8)
   (make-ball 200 150 DVALUE DVALUE true "left")
   "A selected ball should not move after tick")
  
  (check-equal? 
   (ball-after-tick-helper-right ball-almost-at-right-boundary 8)
   (make-ball 380 50 DVALUE DVALUE false "left")
   "A ball almost at right boundary should be bounced
              back and direction changed"))

(run-tests ball-after-tick-tests)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; world-after-tick : World -> World
; GIVEN: a ball and a speed
; RETURNS: when the ball tries to move across the right boundary,
;          it is stopped and the direction is reversed
; EXAMPLES: (bounce-ball-from-right-boundary  
;            (make-ball 385 131 100 100 false "right") 8)
;          =>  (make-ball 380 131 100 100 false "left")
; STRATEGY: Structural Decomposition on w: World           
(define (world-after-tick w)
  (if (world-paused? w)
      w
      (make-world
       (balls-after-tick (world-balls w) (world-speed w))
       (world-no-of-balls w) (world-paused? w)
       (world-speed w))))


; TESTS

(define world-10 (make-world 
                  (list unselected-ball-in-the-middle-moving-right
                        selected-ball-in-the-middle-moving-left) 2 false 8))

(define world-20 (make-world 
                  (list unselected-ball-in-the-middle-moving-right 
                        selected-ball-in-the-middle-moving-left) 2 true 8))

(define world-10-after-tick 
  (make-world 
   (list
    (make-ball 208 150 DVALUE DVALUE false "right")
    (make-ball 200 150 DVALUE DVALUE true "left")) 2 false 8))


(define-test-suite world-after-tick-tests
  
  (check-equal? (world-after-tick world-10) world-10-after-tick
                "world-10-after-tick should be returned")
  
  (check-equal? (world-after-tick world-20) world-20
                "world-20 should be returned since the world is paused"))

(run-tests world-after-tick-tests)


; initial-world : PosInt -> World
; GIVEN: a ball speed
; RETURNS: a world with no balls, but with the
;          property that any balls created in that world
;          will travel at the given speed.
; EXAMPLES: See tests below
; STRATEGY: Function Composition
(define (initial-world speed)
  (make-world empty 0 false speed))

; TESTS

(check-equal? (initial-world 2) (make-world empty 0 false 2)
              "initial-world function here should make an unpaused world 
               with an empty list of balls and speed 2")


; pause-toggle : Boolean -> Boolean
; GIVEN:   a pause value
; RETURNS: the opposite of that value
; EXAMPLES: See tests below
; STRATEGY: Domain Knowledge
(define (pause-toggle pause)
  (not pause))

; TESTS

(check-equal? (pause-toggle true) false "pause-toggle should return false
                                         when input is true")


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


; TESTS

(define-test-suite place-ball-tests
  
  (check-equal? (place-ball unselected-ball-in-the-middle-moving-right 
                            EMPTY-CANVAS)
                unselected-ball-in-the-middle-image
                "The image created by the function should be
                 unselected-ball-in-the-middle-image")
  
  (check-equal? (place-ball selected-ball-in-the-middle-moving-right 
                            EMPTY-CANVAS)
                selected-ball-in-the-middle-image
                "The image created by the function should be
                 selected-ball-in-the-middle-image"))

(run-tests place-ball-tests)


; empty-world-image : World -> Scene
; GIVEN : an world with no balls
; RETURNS: a Scene that portrays the given world.
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on w: World
(define (empty-world->scene w)
  (place-image 
   (text 
    (string-append "Number of Balls: " 
                   (number->string
                    (world-no-of-balls w))) TEXT-SIZE "Black")
   NO-OF-BALL-POSITION-X NO-OF-BALL-POSITION-Y
   EMPTY-CANVAS)) 

; TESTS

; Example for testing

(define empty-world-image 
  (place-image
   (text (string-append "Number of Balls: " (number->string 0)) 
         TEXT-SIZE "Black")
   NO-OF-BALL-POSITION-X NO-OF-BALL-POSITION-Y
   EMPTY-CANVAS)) 

(define-test-suite empty-world->scene-test
  
  (check-equal? (empty-world->scene (initial-world 3))
                empty-world-image
                "the function should display the empty-world-image"))

(run-tests empty-world->scene-test)


; world->scene : World -> Scene
; GIVEN : a world
; RETURNS: a Scene that portrays the given world.
; EXAMPLES: See tests below
; STRATEGY: Higher-Order Function Composition
(define (world->scene w)
  (foldr 
   ; Ball Scene -> Scene
   ; RETURNS: the scene after the ball is placed on it
   (lambda (b s)
     (place-ball b s))
   (empty-world->scene w) (world-balls w)))


; Eamples for testing

(define world-1-ball-image 
  (place-image  
   (text (string-append "Number of Balls: " 
                        (number->string 1)) 12 "Black")
   NO-OF-BALL-POSITION-X NO-OF-BALL-POSITION-Y
   (place-ball unselected-ball-in-the-middle-moving-right EMPTY-CANVAS)))


; TESTS

(define-test-suite world->scene-tests
  
  (check-equal? (world->scene 
                 (make-world 
                  (cons unselected-ball-in-the-middle-moving-right empty)
                  1 false 8))
                world-1-ball-image "a world with 1 unselected ball should
                                      be displayed as world-1-ball-image"))

(run-tests world->scene-tests)


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

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

(check-equal? (distance-from-center 
               unselected-ball-in-the-middle-moving-right 20 30)
              #i216.33307652783935 "For the given ball location
              and mouse coordinate #i216.33307652783935 should
              be returned")


; in-ball? : Ball Number Number -> Boolean
; GIVEN : A Ball and a mouse coordinate
; RETURNS: true iff the given coordinate is inside the ball
; EXAMPLES: See tests below
; STRATEGY: Function Composition
(define (in-ball? b mx my)
  (<= (distance-from-center b mx my) BALL-RADIUS))

; TESTS

(define-test-suite in-ball-tests
  
  (check-equal? (in-ball? unselected-ball-in-the-middle-moving-right 20 30) 
                false 
                "false should be returned for a coordinate outside the ball")
  
  (check-equal? (in-ball? unselected-ball-in-the-middle-moving-right
                          HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT)
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
                 true (ball-direction b))
      b))

; TESTS

(define-test-suite ball-after-button-down-tests
  
  (check-equal? 
   (ball-after-button-down 
    unselected-ball-in-the-middle-moving-right 5 5) 
   unselected-ball-in-the-middle-moving-right
   "If the mouse coordinate is outside the ball then the same
    ball is returned")
  
  (check-equal? 
   (ball-after-button-down unselected-ball-in-the-middle-moving-right 
                           HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT)
   (make-ball 200 150 0 0 true "right")
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
                 true (ball-direction b))
      b))

; TESTS

(define-test-suite ball-after-drag-tests
  
  (check-equal? 
   (ball-after-drag unselected-ball-in-the-middle-moving-right 5 5) 
   unselected-ball-in-the-middle-moving-right 
   "If the mouse coordinate is outside the ball then the same
   ball is returned")
  
  (check-equal? 
   (ball-after-drag selected-ball-in-the-middle-moving-right 
                    HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT) 
   (make-ball 170 120 30 30 true "right") 
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
                  DVALUE DVALUE false (ball-direction b))
      b))

(define-test-suite ball-after-button-up-tests
  
  (check-equal? (ball-after-button-up 
                 unselected-ball-in-the-middle-moving-right 5 5)
                unselected-ball-in-the-middle-moving-right
                "If the ball is unselected then the same
                  ball is returned")
  
  (check-equal? (ball-after-button-up selected-ball-in-the-middle-moving-right
                                      HALF-CANVAS-WIDTH HALF-CANVAS-HEIGHT) 
                (make-ball 170 120 DVALUE DVALUE false "right")
                "If the ball is selected then the ball becomes unselected"))

(run-tests ball-after-button-up-tests)


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
   (ball-after-mouse-event 
    unselected-ball-in-the-middle-moving-right 4 5 "button-down")
   (ball-after-button-down unselected-ball-in-the-middle-moving-right 4 5)
   "If button-down BallKeyEvent occurs, then ball-after-button-down
    function is called")
  
  (check-equal? 
   (ball-after-mouse-event 
    unselected-ball-in-the-middle-moving-right 4 5 "drag")
   (ball-after-drag unselected-ball-in-the-middle-moving-right 4 5)
   "If drag BallKeyEvent occurs, then ball-after-drag
    function is called")
  
  (check-equal? 
   (ball-after-mouse-event 
    unselected-ball-in-the-middle-moving-right 4 5 "button-up")
   (ball-after-button-up unselected-ball-in-the-middle-moving-right 4 5)
   "If button-up BallKeyEvent occurs, then ball-after-button-up
    function is called")
  
  
  (check-equal? 
   (ball-after-mouse-event 
    unselected-ball-in-the-middle-moving-right 4 5 "leave")
   unselected-ball-in-the-middle-moving-right)
  "If a leave BallKeyEvent occurs then it is ignored")

(run-tests ball-after-mouse-event-tests)


; balls-after-mouse-event : ListOfBalls Number Number BallMouseEvent 
;                           -> ListOfBalls
; GIVEN:   a ListOfBalls, a mouse coordinate and a BallMouseEvent
; RETURNS: the ListOfBalls following the BallMouseEvent at the 
;          given location.
; EXAMPLES: See tests below
; STRATEGY: Higher-Order Function Composition
(define (balls-after-mouse-event balls mx my mev)
  (map 
   ; Ball -> Ball
   ; RETURNS: the ball after the mouse event
   (lambda (b)
     (ball-after-mouse-event b mx my mev))
   balls))

; TESTS

(check-equal? (balls-after-mouse-event 
               (list unselected-ball-in-the-middle-moving-right)
               80 80 "button-up")
              
              (list (make-ball 200 150 DVALUE DVALUE false "right"))
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
              (world-no-of-balls w)
              (world-paused? w)
              (world-speed w)))

; TESTS

(check-equal? 
 (world-after-mouse-event 
  (make-world (list unselected-ball-in-the-middle-moving-right)
              1 false 8) 70 70 "drag")
 
 (make-world (list 
              (make-ball 200 150 DVALUE DVALUE false "right"))
             1 false 8)
 "Since the ball is unselected there should be no effect
  of the drag mouse event")


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; world-after-key-event-n : World -> World
; GIVEN:   a World
; RETURNS: the World following the BallKeyEvent = "n"
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on w: World
(define (world-after-key-event-n w)
  (make-world 
   (cons unselected-ball-in-the-middle-moving-right (world-balls w)) 
   (+ 1 (world-no-of-balls w))
   (world-paused? w)
   (world-speed w)))

; TESTS

(check-equal? (world-after-key-event-n 
               (make-world 
                (list unselected-ball-in-the-middle-moving-right) 
                1 false 8))
              (make-world
               (list
                (make-ball 200 150 DVALUE DVALUE false "right")
                (make-ball 200 150 DVALUE DVALUE false "right"))
               2 false 8)
              "world-after-key-event-n should add another ball to the world")


; world-after-key-event-space : World -> World
; GIVEN:   a World
; RETURNS: the World following the BallKeyEvent = " "
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on w: World
(define (world-after-key-event-space w)
  (make-world 
   (world-balls w)
   (world-no-of-balls w)
   (pause-toggle (world-paused? w))
   (world-speed w)))

; TESTS
(define world-10-after-space
  (make-world
   (list
    (make-ball 200 150 DVALUE DVALUE false "right")
    (make-ball 200 150 DVALUE DVALUE true "left")) 2 true 8))

(check-equal? (world-after-key-event-space world-10)
              world-10-after-space
              "world-after-key-event-n should pause/unpause the world")


; world-after-key-event : World BallKeyEvent -> World
; GIVEN: a World
; RETURNS: the world that should follow the given world
;          after the given key event.
;          -- "n" -> make a new ball
;          -- " " -> pause/unpause the world
;          -- ignore all others
; EXAMPLES: See tests below
; STRATEGY: Structural Decomposition on kev: BallKeyEvent
(define (world-after-key-event w kev)
  (cond
    [(key=? kev "n") (world-after-key-event-n w)]
    [(key=? kev " ") (world-after-key-event-space w)]
    [else w]))

; TESTS

(define-test-suite world-after-key-event-tests
  
  (check-equal? 
   (world-after-key-event (make-world empty 0 false 8) "n") 
   (world-after-key-event-n (make-world empty 0 false 8))
   "n BallKeyEvent should call the function world-after-key-event-n") 
  
  (check-equal? 
   (world-after-key-event (make-world empty 0 false 8) " ") 
   (world-after-key-event-space (make-world empty 0 false 8))
   "Space BallKeyEvent should call the function world-after-key-event-space") 
  
  (check-equal? 
   (world-after-key-event (make-world empty 0 false 8) "k") 
   (make-world empty 0 false 8)
   "any event other than n should be ignored"))

(run-tests world-after-key-event-tests)
