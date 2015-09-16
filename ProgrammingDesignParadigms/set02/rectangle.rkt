;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname rectangle) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; start with (run 0)

(require rackunit)
(require rackunit/text-ui)
(require 2htdp/universe)
(require 2htdp/image)
(require "extras.rkt")

(provide run)
(provide initial-world)
(provide world-to-center)
(provide world-selected?)
(provide world-after-mouse-event)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; CONSTANTS

; dimensions of the canvas
(define CANVAS-WIDTH 400)
(define CANVAS-HEIGHT 300)

; dimensions of the rectangle
(define RECT-WIDTH 100)
(define RECT-HEIGHT 60)
(define HALF-RECT-WIDTH (/ RECT-WIDTH 2))
(define HALF-RECT-HEIGHT (/ RECT-HEIGHT 2))
(define SOLID-GREEN-RECT (rectangle RECT-WIDTH RECT-HEIGHT "solid" "green"))
(define OUTLN-GREEN-RECT (rectangle RECT-WIDTH RECT-HEIGHT "outline" "green"))
(define SOLID-RED-CIRC (circle 5 "solid" "red"))
(define EMPTY-CANVAS (empty-scene CANVAS-WIDTH CANVAS-HEIGHT))
(define DUMMY-VALUE 100)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; DATA DEFINITIONS

(define-struct world (rect rect-x rect-y mouse-dist-x mouse-dist-y 
                           circ-x circ-y selected?))
; A World is a (make-world Rectangle Number Number Number Number Number
;                Boolean)
; Interpretation: 
; -- rect is the rectangle which is drawn on the screen
; -- rect-x, rect-y give the position of the rectangle. 
; -- mouse-dist-x, mouse-dist-y are fields for that store the distance between
;    the original position of the rectangle from the current mouse position 
; -- circ-x, circ-y give the position of the small red circle.
; -- selected? describes whether or not the rectangle is selected.

; template:
; world-fn : World -> ??
; (define (world-fn w)
;   (... (world-rect w)
;        (world-rect-x w)
;        (world-rect-y-w)
;        (world-mouse-dist-x w)
;        (world-mouse-dist-y w)
;        (world-circ-x w)
;        (world-circ-y w)
;        (world-selected? w)))
   

; A RectangleMouseEvent is a partition of MouseEvent into the
; following categories:
; -- "button-down"   (interp: maybe select the rectangle)
; -- "drag"          (interp: maybe drag the rectangle)
; -- "button-up"     (interp: unselect the rectangle)
; -- any other mouse event (interp: ignored)

; template:
; rect-mev-fn : RectangleMouseEvent -> ??
; (define (rect-mev-fn mev)
;  (cond
;    [(mouse=? mev "button-down") ...]
;    [(mouse=? mev "drag") ...]
;    [(mouse=? mev "button-up") ...]
;    [else w]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Examples of worlds and images for testing

(define unselected-world-rect-70-70 (make-world SOLID-GREEN-RECT 70
                                 70 DUMMY-VALUE 
                                 DUMMY-VALUE DUMMY-VALUE 
                                 DUMMY-VALUE false))
(define unselected-world-rect-71-71 (make-world SOLID-GREEN-RECT 71
                                 71 DUMMY-VALUE DUMMY-VALUE 
                                 DUMMY-VALUE DUMMY-VALUE false))

(define selected-world-rect-70-70-circ-80-80 (make-world OUTLN-GREEN-RECT 70
                                 70 10  10 80 80 true))

(define selected-world-rect-70-70 (make-world OUTLN-GREEN-RECT 70
                                  70 10   10 DUMMY-VALUE 
                                 DUMMY-VALUE true))

(define  selected-world-rect-71-71-circ-81-81 (make-world OUTLN-GREEN-RECT 71
                                 71 10 10 81 81 true))
 
(define world-with-rect-in-middle (make-world SOLID-GREEN-RECT
                                         (/ CANVAS-WIDTH 2)(/ CANVAS-HEIGHT 2)
                                         DUMMY-VALUE DUMMY-VALUE DUMMY-VALUE 
                                         DUMMY-VALUE false))

(define image-unselected-rect-at-70-70 
  (place-image SOLID-GREEN-RECT 70 70 EMPTY-CANVAS))

(define image-selected-rect-at-70-70 
  (place-image SOLID-RED-CIRC 80 80 
   (place-image OUTLN-GREEN-RECT 70 70 EMPTY-CANVAS)))
  
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; run : Any -> World
; GIVEN: any value
; EFFECT: Ignores its argument and runs the world.
; EXAMPLES: (run 0) -> starts a world program
; STRATEGY: Function Composition
(define (run any-val)
  (big-bang (initial-world any-val)
            (on-draw world->scene)
            (on-mouse world-after-mouse-event)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; world->scene : World -> Scene
; GIVEN : a world
; RETURNS: a Scene that portrays the given world.
; EXAMPLES: world->scene unselected-world-rect-70-70 
;             => image-unselected-rect-at-70-70
; STRATEGY: Structural Decomposition on w: World

(define (world->scene w)
  (if (world-selected? w)
    (place-image SOLID-RED-CIRC (world-circ-x w) (world-circ-y w) 
                  (place-image (world-rect w) (world-rect-x w)
                               (world-rect-y w) EMPTY-CANVAS))
    (place-image (world-rect w) (world-rect-x w)
                 (world-rect-y w) EMPTY-CANVAS)))

; world->scene tests

(define-test-suite world->scene-tests

  (check-equal? (world->scene unselected-world-rect-70-70) 
              image-unselected-rect-at-70-70
              "world->scene should call place-image correctly")

  (check-equal? (world->scene selected-world-rect-70-70-circ-80-80) 
              image-selected-rect-at-70-70
              "world->scene should call place-image correctly"))

(run-tests world->scene-tests)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; initial-world : Any -> World
; GIVEN: any value
; RETURNS: the initial world. Ignores its argument.
; EXAMPLES: (initial-world 50) => world-with-rect-in-middle 
; STRATEGY: Structural Decomposition on w: World
(define (initial-world any-val)
  (make-world SOLID-GREEN-RECT (/ CANVAS-WIDTH 2)(/ CANVAS-HEIGHT 2) 
              DUMMY-VALUE DUMMY-VALUE DUMMY-VALUE DUMMY-VALUE false))

;tests

(define-test-suite initial-world-test
  
  (check-equal? (initial-world 50) world-with-rect-in-middle 
              "initial world should make a world with rect in the middle"))

(run-tests initial-world-test)
    
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; world-to-center : World -> Posn
; GIVEN: a world
; RETURNS: the coordinates of the center of the rectangle as a Posn
; EXAMPLES: (world-to-center world-with-rect-in-middle) =>
;           (make-posn (/ CANVAS-WIDTH 2)(/ CANVAS-HEIGHT 2))
; STRATEGY: Structural Decomposition on w: World
(define (world-to-center w)
  (make-posn (world-rect-x w) (world-rect-y w)))

; TESTS

(define-test-suite world-to-center-test
  
  (check-equal? (world-to-center world-with-rect-in-middle) 
              (make-posn (/ CANVAS-WIDTH 2)(/ CANVAS-HEIGHT 2))
              "world-to-center should return 
               coordinates of the center of the rectangle as a Posn"))

(run-tests world-to-center-test)  

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; world-after-mouse-event : World Number Number RectangleMouseEvent -> World
; GIVEN: A world, a RectangleMouseEvent and its coordinates
; RETURNS: the world that follows the given mouse event.
; EXAMPLES:  
;  (world-after-mouse-event unselected-world-rect-70-70 80 80   
;      "button-down")
;    =>  selected-world-rect-70-70-circ-80-80
; STRATEGY: Structural Decomposition on mev: RectangleMouseEvent
(define (world-after-mouse-event w mx my mev)
  (cond
    [(mouse=? mev "button-down") (world-after-button-down w mx my)]
    [(mouse=? mev "drag") (world-after-drag w mx my)]
    [(mouse=? mev "button-up") (world-after-button-up w mx my)]
    [else w]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; in-rectangle? : World Number Number -> World
; GIVEN : a world and the coordinates of the mouse
; RETURNS: true iff the given coordinate is inside the rectangle
; EXAMPLES: (in-rectangle? world-with-rect-in-middle 200 150) => true
; STRATEGY: Structural Decomposition on w: World
(define (in-rectangle? w x y)
  (and
    (<= 
      (- (world-rect-x w) HALF-RECT-WIDTH)
      x
      (+ (world-rect-x w) HALF-RECT-WIDTH))
    (<= 
      (- (world-rect-y w) HALF-RECT-HEIGHT)
      y
      (+ (world-rect-y w) HALF-RECT-HEIGHT))))


; world-after-button-down : World Number Number -> World
; GIVEN:  a world and the coordinates of the mouse
; RETURNS: if the coordinates are inside the rectangle, then 
;          returns a hollow rectangle with a green outline and a 
;          red circle created at the position of the mouse
; EXAMPLES: (world-after-mouse-event unselected-world-rect-70-70 
;           80 80 "button-down") =>  selected-world-rect-70-70-circ-80-80
; STRATEGY: Structural Decomposition on w: World
(define (world-after-button-down w mx my)
  (if (in-rectangle? w mx my)
      (make-world OUTLN-GREEN-RECT 
                  (world-rect-x w) (world-rect-y w) 
                  (- mx (world-rect-x w)) (- my (world-rect-y w))
                  mx my true)
       w))


; world-after-drag : World Number Number -> World
; GIVEN:  a world and the coordinates of the mouse
; RETURNS: the world following a drag at the given location.
;          if the world is selected, then return a world with 
;          a hollow rectangle with a green outline and a 
;          red circle created at the position of the mouse
; EXAMPLES: (world-after-mouse-event unselected-world-rect-70-70 
;           81 81 "drag") => unselected-world-rect-70-70
; STRATEGY: Structural Decomposition on w: World
(define (world-after-drag w mx my)
 (if (world-selected? w) 
   (make-world OUTLN-GREEN-RECT 
               (- mx (world-mouse-dist-x w)) (- my (world-mouse-dist-y w))
               (world-mouse-dist-x w) (world-mouse-dist-y w)
               mx my true)
    w))


; world-after-button-up : World Number Number -> World
; GIVEN:  a world and the coordinates of the mouse
; RETURNS: the world following a button-up at the given location.
;          if the rectangle is selected, make a solid green rectangle,
;          which is no longer selected.
; EXAMPLES:  (world-after-mouse-event unselected-world-rect-70-70 81 81
;            "button-up") => unselected-world-rect-70-70
; STRATEGY: Structural Decomposition on w: World
(define (world-after-button-up w mx my)
  (if (world-selected? w)
      (make-world SOLID-GREEN-RECT 
                  (- mx (world-mouse-dist-x w)) (- my (world-mouse-dist-y w))
                  DUMMY-VALUE DUMMY-VALUE
                  DUMMY-VALUE DUMMY-VALUE false)
      w))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; MOUSE TESTS

(define-test-suite mouse-tests 

  (check-equal?
    (world-after-mouse-event unselected-world-rect-70-70 
      80 80    ;; a coordinate inside the rectangle
      "button-down")
    selected-world-rect-70-70-circ-80-80
    "button down inside the rectangle should select it")

  (check-equal?
    (world-after-mouse-event unselected-world-rect-70-70 
      200 200    ;; a coordinate outside the rectangle
      "button-down")
     unselected-world-rect-70-70 
    "button down outside the rectangle should not select it")

  (check-equal?
    (world-after-mouse-event unselected-world-rect-70-70 
      81 81    ;; a coordinate inside the rectangle
      "drag")
    unselected-world-rect-70-70
    "moving the mouse if the rectangle is not 
     selected should not move the rectangle")

  (check-equal?
    (world-after-mouse-event selected-world-rect-70-70 
      81 81    ;; a coordinate inside the rectangle
      "drag")
   selected-world-rect-71-71-circ-81-81
    "moving the mouse if the rectangle is not 
     selected should not move the rectangle")

  (check-equal?
    (world-after-mouse-event unselected-world-rect-70-70 
      81 81    ;; a coordinate inside the rectangle
      "button-up")
    unselected-world-rect-70-70
    "button up outside the rectangle should have no effect")

  (check-equal?
    (world-after-mouse-event selected-world-rect-70-70 
      81 81    ;; a coordinate inside the rectangle
      "button-up")
    unselected-world-rect-71-71
    "button up inside the rectangle should make it solid green
     and unselect it")

  (check-equal?
    (world-after-mouse-event unselected-world-rect-70-70 
      81 81    ;; a coordinate inside the rectangle
      "leave")
    unselected-world-rect-70-70
    "a leave mouse event should have no effect"))

(run-tests mouse-tests)
