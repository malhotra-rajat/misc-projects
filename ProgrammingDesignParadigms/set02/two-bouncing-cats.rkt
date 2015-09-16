;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname two-bouncing-cats) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
;; start with (main 0)

(require rackunit)
(require rackunit/text-ui)
(require 2htdp/universe)
(require 2htdp/image)
(require "extras.rkt")

(provide initial-world)
(provide world-after-mouse-event)
(provide world-after-key-event)
(provide world-cat1)
(provide world-cat2)
(provide world-paused?)
(provide cat-x-pos)
(provide cat-y-pos)
(provide cat-selected?)
(provide cat-north?)
(provide cat-east?)
(provide cat-south?)
(provide cat-west?)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; main : Number -> World
; GIVEN: the initial y-position of the cats
; EFFECT: runs the simulation, starting with the cats falling 
; RETURNS: the final state of the world
; EXAMPLES: (main 0) -> starts a world program with the cats from the y=0 
;           coordinate
(define (main initial-pos)
  (big-bang (initial-world initial-pos)
            (on-tick world-after-tick 0.5)
            (on-draw world-to-scene)
            (on-key world-after-key-event)
            (on-mouse world-after-mouse-event)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; CONSTANTS

(define CAT-IMAGE (bitmap "cat.png"))

; how fast the cat falls, in pixels/tick
(define CATSPEED 8)

; dimensions of the canvas

(define CANVAS-WIDTH 450)
(define CANVAS-HEIGHT 400)
(define EMPTY-CANVAS (empty-scene CANVAS-WIDTH CANVAS-HEIGHT))
(define CAT1-X-COORD (/ CANVAS-WIDTH 3))
(define CAT2-X-COORD (* 2 CAT1-X-COORD))

; halved dimensions of the cat 
(define HALF-CAT-WIDTH  (/ (image-width  CAT-IMAGE) 2))
(define HALF-CAT-HEIGHT (/ (image-height CAT-IMAGE) 2))

; addiing 1 to the halved dimensions of the cat so that the simulation
; runs as seen in the video
(define HALF-CAT-WIDTH-PLUS1  (+ (/ (image-width  CAT-IMAGE) 2) 1))
(define HALF-CAT-HEIGHT-PLUS1 (+ (/ (image-height CAT-IMAGE) 2) 1))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; DATA DEFINITIONS

; A Direction is one of:
; -- "east"
; -- "west"
; -- "north"
; -- "south"

; TEMPLATE;
;(define (dir-fn dir)
;(cond
;  [(string=? dir "east") ...]
;  [(string=? dir "west") ...]
;  [(string=? dir "north") ...]
;  [(string=? dir "south") ...]))

(define-struct cat (x-pos y-pos selected? direction))
; A Cat is a (make-cat Number Number Boolean Direction)
; Interpretation: 
; -- x-pos, y-pos give the position of the cat. 
; -- selected? describes whether or not the cat is selected.
; -- direction is the direction in which the cat is moving.

; template:
; cat-fn : Cat -> ??
; (define (cat-fn c)
;  (... 
;   (cat-x-pos c)
;   (cat-y-pos c) 
;   (cat-selected? c)
;   (cat-direction c)))

(define-struct world (cat1 cat2 paused?))
; A World is a (make-world Cat Cat Boolean)
; -- cat1 and cat2 are the two cats
; -- paused? describes whether or not the world is paused

; template:
; world-fn : World -> ??
;  (define (world-fn w)
;    (... (world-cat1 w) 
;         (world-cat2 w) 
;         (world-paused? w)))


; A FallingCatKeyEvent is a KeyEvent, which is one of
; -- " "                 (interp: pause/unpause)
; -- "left"              (interp: move the cat left if it is selected)
; -- "right"             (interp: move the cat right if it is selected)
; -- "up"                (interp: move the cat up if it is selected)
; -- "down"              (interp: move the cat down if it is selected)
; -- any other KeyEvent  (interp: ignore)

; template:
; falling-cat-kev-fn : FallingCatKeyEvent -> ??
; (define (falling-cat-kev-fn kev)
;  (cond 
;    [(key=? kev " ")...]
;    [(key=? kev "left")...]
;    [(key=? kev "right")...]
;    [(key=? kev "up")...]
;    [(key=? kev "down")...]
;    [else ...]))

 
; A FallingCatMouseEvent is a MouseEvent that is one of:
; -- "button-down"   (interp: maybe select the cat)
; -- "drag"          (interp: maybe drag the cat)
; -- "button-up"     (interp: unselect the cat)
; -- any other mouse event (interp: ignored)

;(define (mev-fn mev)
;  (cond
;    [(mouse=? mev "button-down") ...]
;    [(mouse=? mev "drag") ...]
;    [(mouse=? mev "button-up") ...]
;    [else ...]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Examples of World, Cat, FallingCatKeyEvent, FallingCatMouseEvent, 
; images for testing
 
(define selected-cat-south-at-20 (make-cat CAT1-X-COORD 20 true "south"))
(define unselected-cat-south-at-20 (make-cat CAT1-X-COORD 20 false "south"))
 
(define selected-cat-south-at-28 (make-cat CAT1-X-COORD 28 true "south"))
(define unselected-cat-south-at-28 (make-cat CAT1-X-COORD 28 false "south"))
 
(define unselected-cat-north-at-100 (make-cat CAT1-X-COORD 100 false "north"))
(define unselected-cat-north-at-92 (make-cat CAT1-X-COORD 92 false "north"))
(define selected-cat-north-at-100 (make-cat CAT1-X-COORD 100 true "north"))
 
(define pause-key-event " ")

(define image-at-20 (place-image CAT-IMAGE CAT1-X-COORD 20 EMPTY-CANVAS))
 
(define image-at-20-20 
  (place-image CAT-IMAGE CAT1-X-COORD 20 
   (place-image CAT-IMAGE CAT2-X-COORD 20 EMPTY-CANVAS)))
 
(define init-world-20 (make-world (make-cat CAT1-X-COORD 20 false "south")
                                  (make-cat CAT2-X-COORD 20 false "south")
                                   false))
 
(define paused-world-at-20-20-south (make-world selected-cat-south-at-20 
                                                selected-cat-south-at-20
                                                true))

(define unselected-unpaused-world-at-20-20-south (make-world
                                                  unselected-cat-south-at-20
                                                  unselected-cat-south-at-20
                                                  false))
(define unpaused-world-at-20-20-south 
  (make-world selected-cat-south-at-20 selected-cat-south-at-20 false)) 

(define unpaused-world-at-28-28-south 
  (make-world unselected-cat-south-at-28 unselected-cat-south-at-28 false))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; initial-world : Number -> World
; GIVEN: a y-coordinate
; RETURNS: a world with two unselected cats, spaced evenly across the
; canvas in the x-direction, and falling, and placed at the given y
; coordinate.
; EXAMPLES: (initial-world 20) => init-world-20
; STRATEGY: Function Composition
(define (initial-world y)
  (make-world
    (make-cat CAT1-X-COORD y false "south")
    (make-cat CAT2-X-COORD y false "south")
    false)) 


; world-to-scene : World -> Scene
; GIVEN : a world
; RETURNS: a Scene that portrays the given world.
; EXAMPLES: (world->scene (make-world 20 ??))
;          = (place-image CAT-IMAGE CAT-X-COORD 20 EMPTY-CANVAS)
; STRATEGY: Structural Decomposition on w: World
(define (world-to-scene w)
  (place-cat (world-cat1 w)
             (place-cat (world-cat2 w) EMPTY-CANVAS)))



; place-cat : Cat Scene -> Scene
; GIVEN: a cat and a scene
; RETURNS: a scene like the given one, but with the given cat painted
;          on it.
; EXAMPLES: (place-cat unselected-cat-south-at-20) => image-at-20
; STRATEGY: structural decomposition on c: Cat
(define (place-cat c s)
  (place-image CAT-IMAGE (cat-x-pos c) (cat-y-pos c) s))

; world-after-tick : World -> World
; GIVEN: a world w
; RETURNS: the world that should follow w after a tick.
; EXAMPLES: 
;  world-after-tick paused-world-at-20-20-south =>
;  paused-world-at-20-20-south
; STRATEGY: structural decomposition on w : World
(define (world-after-tick w)
  (if (world-paused? w)
    w
    (make-world
      (cat-after-tick (world-cat1 w))
      (cat-after-tick (world-cat2 w))
      (world-paused? w))))


; world-with-paused-toggled : World -> World
; GIVEN: a world
; RETURNS: a world just like the given one, but with paused? toggled
; EXAMPLES: 
;  (world-with-paused-toggled init-world-20) =>
;   (make-world (make-cat CAT1-X-COORD 20 false "south")
;               (make-cat CAT2-X-COORD 20 false "south") true)
; STRATEGY: structural decomposition on w : World
(define (world-with-paused-toggled w)
  (make-world
   (world-cat1 w)
   (world-cat2 w)
   (not (world-paused? w))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; cat-after-tick : Cat -> Cat
; GIVEN: a cat c
; RETURNS: the state of the given cat after a tick if it were in an
;          unpaused world.
; EXAMPLES: 
;  (cat-after-tick selected-cat-at-20) => selected-cat-at-20
;  (cat-after-tick unselected-cat-at-20) => unselected-cat-at-28
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-tick c)
  (cond
    [(string=? (cat-direction c) "south")(cat-after-tick-helper-south 
                                          (cat-x-pos c) (cat-y-pos c) 
                                          (cat-selected? c) 
                                          )]
    [(string=? (cat-direction c) "north")(cat-after-tick-helper-north 
                                          (cat-x-pos c) (cat-y-pos c) 
                                          (cat-selected? c)
                                          )]
    [(string=? (cat-direction c) "west")(cat-after-tick-helper-west 
                                         (cat-x-pos c) (cat-y-pos c) 
                                         (cat-selected? c) 
                                         )]
    [(string=? (cat-direction c) "east")(cat-after-tick-helper-east 
                                         (cat-x-pos c) (cat-y-pos c) 
                                         (cat-selected? c) 
                                         )]))


; cat-after-tick-helper-south : Number Number Boolean -> Cat
; GIVEN: a position and a value for selected?
; RETURNS: the cat moved south if not selected. If the boundary is touched,
;          the cat starts moving north           
; EXAMPLES: 
;  (cat-after-tick-helper-south 100 100 true) =>
;   (make-cat 100 100 true "south") 
; STRATEGY: Function Composition
(define (cat-after-tick-helper-south x-pos y-pos selected?)
  (if selected?
    (make-cat x-pos y-pos selected? "south")
    
    (if (>= (+ y-pos CATSPEED) (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1))
      (cat-after-tick-helper-north  
         x-pos (+ (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1) CATSPEED)
         selected?)
      
      (make-cat x-pos (+ y-pos CATSPEED) selected? "south"))))


; cat-after-tick-helper-west : Number Number Boolean -> Cat
; GIVEN: a position and a value for selected?
; RETURNS: the cat moved west if not selected. If the boundary is touched,
;          the cat starts moving east           
; EXAMPLES:
;  (cat-after-tick-helper-north 100 100 true) =>
;   (make-cat 100 100 true "north") 
; STRATEGY: Function Composition
(define (cat-after-tick-helper-west x-pos y-pos selected?)
  (if selected?
   (make-cat x-pos y-pos selected? "west")
   
   (if (<= (- x-pos CATSPEED) HALF-CAT-WIDTH-PLUS1)
       (cat-after-tick-helper-east 
          (- HALF-CAT-WIDTH-PLUS1 CATSPEED) y-pos selected?)
       
       (make-cat (- x-pos CATSPEED) y-pos selected? "west"))))


; cat-after-tick-helper-east : Number Number Boolean -> Cat
; GIVEN: a position and a value for selected?
; RETURNS: the cat moved east if not selected. If the boundary is touched,
;          the cat starts moving west           
; EXAMPLES: 
;  (cat-after-tick-helper-east 100 100 true) =>
;   (make-cat 100 100 true "east") 
; STRATEGY: Function Composition
(define (cat-after-tick-helper-east x-pos y-pos selected?)
  (if selected?
    (make-cat x-pos y-pos selected? "east")
    
    (if (>= (+ x-pos CATSPEED) (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1))
      (cat-after-tick-helper-west 
         (+ (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) CATSPEED)
         y-pos selected?)
      
      (make-cat (+ x-pos CATSPEED) y-pos selected? "east"))))


; cat-after-tick-helper-north : Number Number Boolean-> Cat
; GIVEN: a position and a value for selected?
; RETURNS: the cat moved north if not selected. If the boundary is touched,
;          the cat starts moving south           
; EXAMPLES: 
;  (cat-after-tick-helper-north 100 100 true) =>
;    (make-cat 100 100 true "north") 
; STRATEGY: Function Composition
(define (cat-after-tick-helper-north x-pos y-pos selected?)
  (if selected?
    (make-cat x-pos y-pos selected? "north")
     
    (if (<= (- y-pos CATSPEED) HALF-CAT-HEIGHT-PLUS1)
       (cat-after-tick-helper-south 
          x-pos (- HALF-CAT-HEIGHT-PLUS1 CATSPEED) selected?)
      
       (make-cat x-pos (- y-pos CATSPEED) selected? "north"))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; world-after-key-event : World FallingCatKeyEvent -> World
; GIVEN: a world w
; RETURNS: the world that should follow the given world
;          after the given key event.
;          on space, toggle paused?, left, right, up, down
;          -- ignore all others
; EXAMPLES: 
; (world-after-key-event unpaused-world-at-20-20-south "q") =>
;    unpaused-world-at-20-20-south
; STRATEGY: Structural Decomposition on kev: FallingCatKeyEvent
(define (world-after-key-event w kev)
  (cond
    [(key=? kev " ")
     (world-with-paused-toggled w)]
    
    [(key=? kev "left") (make-world (cat-after-left-key (world-cat1 w))
                                    (cat-after-left-key (world-cat2 w))
                                    (world-paused? w))]
    [(key=? kev "right") (make-world (cat-after-right-key (world-cat1 w))
                                    (cat-after-right-key (world-cat2 w))
                                    (world-paused? w))]
    [(key=? kev "up") (make-world  (cat-after-up-key (world-cat1 w))
                                    (cat-after-up-key (world-cat2 w))
                                    (world-paused? w))]
    [(key=? kev "down") (make-world (cat-after-down-key (world-cat1 w))
                                    (cat-after-down-key (world-cat2 w))
                                    (world-paused? w))]
    [else w]))


; cat-after-left-key : Cat -> Cat
; GIVEN: a cat c
; RETURNS: a cat just like the original, except that it is moving in the
;           west direction if it is selected
; EXAMPLES: 
;   (cat-after-left-key selected-cat-south-at-20) =>
;     (make-cat CAT1-X-COORD 20 true "west")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-left-key c)
  (if (cat-selected? c)
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) "west")
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) 
                (cat-direction c))))

; cat-after-right-key : Cat -> Cat
; GIVEN: a cat c
; RETURNS: a cat just like the original, except that it is moving in the
;           east direction if it is selected
; EXAMPLES: (cat-after-right-key selected-cat-south-at-20) =>
;              (make-cat CAT1-X-COORD 20 true "east")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-right-key c)
  (if (cat-selected? c)
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) "east")
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) 
                (cat-direction c))))


; cat-after-up-key : Cat -> Cat
; GIVEN: a cat c
; RETURNS: a cat just like the original, except that it is moving in the
;           north direction if it is selected
; EXAMPLES: (cat-after-up-key selected-cat-south-at-20) =>
;              make-cat CAT1-X-COORD 20 true "north")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-up-key c)
  (if (cat-selected? c)
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) "north")
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) 
                (cat-direction c))))


; cat-after-down-key : Cat -> Cat
; GIVEN: a cat c
; RETURNS: a cat just like the original, except that it is moving in the
;           south direction if it is selected
; EXAMPLES: (cat-after-down-key selected-cat-north-at-100) =>
;             (make-cat CAT1-X-COORD 100 true "south")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-down-key c)
  (if (cat-selected? c)
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) "south")
      (make-cat (cat-x-pos c) (cat-y-pos c) (cat-selected? c) 
                (cat-direction c))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; world-after-mouse-event : World Number Number FallingCatMouseEvent -> World
; GIVEN: a world and a description of a mouse event
; RETURNS: the world that should follow the given mouse event
; EXAMPLES: 
; (world-after-mouse-event unselected-unpaused-world-at-20-20-south
;                           100 100 "button-down") =>
;  (make-world (cat-after-mouse-event unselected-cat-south-at-20 
;                                    100 100 "button-down")
; STRATEGY: Function Composition
(define (world-after-mouse-event w mx my mev)
  (make-world
    (cat-after-mouse-event (world-cat1 w) mx my mev)
    (cat-after-mouse-event (world-cat2 w) mx my mev)
    (world-paused? w)))


; cat-after-mouse-event : Cat Number Number FallingCatMouseEvent -> Cat
; GIVEN: a cat and a description of a mouse event
; RETURNS: the cat that should follow the given mouse event
; EXAMPLES: (cat-after-mouse-event selected-cat-south-at-20 
;                                  100 100 "button-down") =>
;  (cat-after-button-down selected-cat-south-at-20 100 100) 
; STRATEGY: Structural Decomposition on mev: FallingCatMouseEvent
(define (cat-after-mouse-event c mx my mev)
  (cond
    [(mouse=? mev "button-down") (cat-after-button-down c mx my)]
    [(mouse=? mev "drag") (cat-after-drag c mx my)]
    [(mouse=? mev "button-up")(cat-after-button-up c)]
    [else c]))


; cat-after-button-down : Cat Number Number -> Cat
; GIVEN: a cat and the coordinates of the mouse position
; RETURNS: the cat following a button-down at the given location.
; EXAMPLES: (cat-after-button-down (make-cat 20 20 false "north") 20 20) =>
;             (make-cat 20 20 true "north")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-button-down c x y)
  (if (in-cat? c x y)
      (make-cat (cat-x-pos c) (cat-y-pos c) true (cat-direction c))
      c))


; cat-after-drag : Cat Number Number -> Cat
; GIVEN: a cat and the coordinates of the mouse position
; RETURNS: the cat following a drag at the given location.
; EXAMPLES: (cat-after-drag (make-cat 20 20 true "north") 50 50) =>
;             (make-cat 50 50 true "north")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-drag c x y)
  (if (cat-selected? c)
      (make-cat x y true (cat-direction c))
      c))


; cat-after-button-up : Cat -> Cat
; GIVEN: a cat
; RETURNS: the cat following a button-up at the given location.
; EXAMPLES: (cat-after-button-up (make-cat 450 0 true "north")) =>
;           (cat-outside-boundary-next (make-cat 450 0 true "north"))
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-after-button-up c)
  (if (cat-selected? c)
    (if (cat-dragged-outside-boundary? c)
      (cat-outside-boundary-next c)
      (make-cat (cat-x-pos c) (cat-y-pos c) false (cat-direction c)))
      c))


; cat-dragged-outside-boundary? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the boundary of the canvas
; EXAMPLES: (cat-after-drag (make-cat 20 20 true "north") 50 50) =>
;             (make-cat 50 50 true "north")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-dragged-outside-boundary? c) 
  (or (>= (cat-x-pos c) (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1))
      (>= (cat-y-pos c) (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1))
      (<= (cat-x-pos c) HALF-CAT-WIDTH-PLUS1)
      (<= (cat-y-pos c) HALF-CAT-HEIGHT-PLUS1)))


; cat-outside-boundary-next : Cat -> Cat
; GIVEN: a cat
; RETURNS: the new cat when the old cat is dragged outside a boundary
; EXAMPLES: 
; (cat-outside-boundary-next (make-cat 450 0 true "north")) =>
;   (cat-outside-right-boundary-next (make-cat 450 0 true "north"))
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-boundary-next c)
  (if (cat-outside-right-boundary? c) ;right boundary
   (cat-outside-right-boundary-next c)
   
   (if (cat-outside-left-boundary? c) ;left boundary
    (cat-outside-left-boundary-next c)
    
    (if (cat-outside-bottom-boundary? c) ;bottom boundary
      (make-cat (cat-x-pos c) (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1) false 
                (cat-direction c))
      
      (if (cat-outside-top-boundary? c) ;top boundary
        (make-cat (cat-x-pos c) HALF-CAT-HEIGHT-PLUS1 false (cat-direction c))
        c)))))
                                        

; cat-outside-right-boundary-next : Cat -> Cat
; GIVEN: a cat
; RETURNS: the new cat when the old cat is dragged outside the right boundary
; EXAMPLES: 
; (cat-outside-right-boundary-next (make-cat 450 200 false "south")) =>
;   (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) 200 false "south")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-right-boundary-next c)
  (if (cat-outside-bottom-right-corner? c) ;;bottom right corner
      (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) 
                (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1) false 
                (cat-direction c))
       
      (if (cat-outside-top-right-corner? c) ;;top right corner
          (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) 
                    HALF-CAT-HEIGHT-PLUS1 false (cat-direction c))
          
          (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) ;cat outside 
                    (cat-y-pos c) false (cat-direction c)))));right boundary


; cat-outside-left-boundary-next : Cat -> Cat
; GIVEN: a cat
; RETURNS: the new cat when the old cat is dragged outside the left boundary
; EXAMPLES: (cat-outside-left-boundary-next (make-cat 0 0 false "south"))
;            => make-cat HALF-CAT-WIDTH-PLUS1 HALF-CAT-HEIGHT-PLUS1 
;                        false "south")
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-left-boundary-next c)
  (if (cat-outside-bottom-left-corner? c) 
      (make-cat  HALF-CAT-WIDTH-PLUS1 
                 (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1)
                 false (cat-direction c))
                  
        (if (cat-outside-top-left-corner? c) 
              (make-cat HALF-CAT-WIDTH-PLUS1 
                        HALF-CAT-HEIGHT-PLUS1 false
                        (cat-direction c))
              
          (make-cat HALF-CAT-WIDTH-PLUS1 (cat-y-pos c) 
                    false (cat-direction c)))))


; cat-outside-bottom-right-corner? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the bottom-right corner
; EXAMPLES: (cat-outside-bottom-right-corner? 
;           (make-cat 450 400 true "west")) => true 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-bottom-right-corner? c)
  (and (>= (cat-x-pos c) (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1)) 
       (>= (cat-y-pos c) (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1))))


; cat-outside-top-right-corner? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the top-right corner
; EXAMPLES: (cat-outside-top-right-corner? 
;                (make-cat 450 0 true "west" )) => true 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-top-right-corner? c)
  (and (>= (cat-x-pos c) (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1)) 
       (<= (cat-y-pos c) HALF-CAT-HEIGHT-PLUS1)))


; cat-outside-top-left-corner? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the top-left corner
; EXAMPLES: (cat-outside-top-left-corner? 
;                 (make-cat 0 0 true "west" )) => true 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-top-left-corner? c)
  (and (<= (cat-y-pos c) HALF-CAT-HEIGHT-PLUS1)
       (<= (cat-x-pos c) HALF-CAT-WIDTH-PLUS1)))


; cat-outside-bottom-left-corner? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the bottom-left corner
; EXAMPLES: cat-outside-bottom-left-corner? 
;                 (make-cat 0 450 true "west" )) => true 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-bottom-left-corner? c)
  (and (>= (cat-y-pos c) (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1))
       (<= (cat-x-pos c) HALF-CAT-WIDTH-PLUS1)))


; cat-outside-right-boundary? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the right boundary
; EXAMPLES: (cat-outside-right-boundary? unselected-cat-north-at-100)
;              =>  false
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-right-boundary? c)
  (>= (cat-x-pos c) (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1)))


; cat-outside-left-boundary? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the left boundary
; EXAMPLES: (cat-outside-left-boundary? unselected-cat-north-at-100) 
;            =>  false 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-left-boundary? c)
  (<= (cat-x-pos c) HALF-CAT-WIDTH-PLUS1))


; cat-outside-bottom-boundary? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the bottom boundary
; EXAMPLES: (cat-outside-bottom-boundary? unselected-cat-north-at-100)
;             =>  false 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-bottom-boundary? c)
  (>= (cat-y-pos c) (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1)))


; cat-outside-top-boundary? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff the cat is dragged outside the top boundary
; EXAMPLES:  (cat-outside-top-boundary? unselected-cat-north-at-100) 
;             =>  false 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-outside-top-boundary? c)
  (<= (cat-y-pos c) HALF-CAT-HEIGHT-PLUS1))


; in-cat? : Cat Number Number -> Cat
; GIVEN: a cat and the coordinates of the mouse.
; RETURNS: true iff the given coordinate is inside the bounding box of
;          the given cat.
; EXAMPLES: (in-cat? unselected-cat-south-at-20 CAT1-X-COORD 20) => true 
; STRATEGY: Structural Decomposition on c: Cat
(define (in-cat? c x y)
  (and
    (<= 
      (- (cat-x-pos c) HALF-CAT-WIDTH)
      x
      (+ (cat-x-pos c) HALF-CAT-WIDTH))
    (<= 
      (- (cat-y-pos c) HALF-CAT-HEIGHT)
      y
      (+ (cat-y-pos c) HALF-CAT-HEIGHT))))


; cat-north? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff c is travelling in the north direction.
; EXAMPLES: (cat-north? unselected-cat-south-at-20) => false 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-north? c)
 (string=? (cat-direction c) "north"))


; cat-east? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff c is travelling in the east direction.
; EXAMPLES: (cat-east? unselected-cat-south-at-20) => false 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-east? c)
 (string=? (cat-direction c) "east"))


; cat-south? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff c is travelling in the south direction.
; EXAMPLES: (cat-south? unselected-cat-south-at-20) => true 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-south? c)
 (string=? (cat-direction c) "south"))


; cat-west? : Cat -> Boolean
; GIVEN: a cat
; RETURNS: true iff c is travelling in the west direction.
; EXAMPLES: (cat-west? unselected-cat-south-at-20) => false 
; STRATEGY: Structural Decomposition on c: Cat
(define (cat-west? c)
 (string=? (cat-direction c) "west"))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TESTS

(check-equal? (initial-world 20) init-world-20 
                "initial-world 20 should create a world with two 
                 cats falling (unpaused) from the y=20 coordinate")
 
(check-equal? (world-to-scene init-world-20) image-at-20-20  
                "world-to-scene init-world 20 should create an image with two 
                 cats falling (unpaused) from the y=20 coordinate")

(check-equal? (in-cat? unselected-cat-south-at-20  CAT1-X-COORD 20) true 
              "in-cat? should return true if the coordinates are
               inside the cat")

(check-equal? (world-with-paused-toggled init-world-20)
                (make-world (make-cat CAT1-X-COORD 20 false "south")
                            (make-cat CAT2-X-COORD 20 false "south") true)
                "the function should return the given world with the pause
                 toggled")

;; ----------------------------------------------------------------------------

(define-test-suite world-after-tick-tests
  
  (check-equal? (world-after-tick paused-world-at-20-20-south)
                paused-world-at-20-20-south
                "paused world should not change after tick")

  (check-equal? (world-after-tick unselected-unpaused-world-at-20-20-south)
               unpaused-world-at-28-28-south
                "unpaused world with cats moving south 
                 should make cats at y=28 after tick"))
 
(run-tests world-after-tick-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-outside-left-boundary-next-tests

  (check-equal? (cat-outside-left-boundary-next (make-cat 0 0 false "south"))
                (make-cat HALF-CAT-WIDTH-PLUS1
                          HALF-CAT-HEIGHT-PLUS1 false "south")
                "if the cat is outside the left-top corner, then it
                 should bounce back inside the canvas")
  
  (check-equal? (cat-outside-left-boundary-next (make-cat 0 400 false "south"))
                (make-cat HALF-CAT-WIDTH-PLUS1 
                          (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1) 
                          false "south")
                "if the cat is outside the left-bottom corner, then it
                 should bounce back inside the canvas")
  
  (check-equal? (cat-outside-left-boundary-next (make-cat 0 200 false "south"))
                (make-cat HALF-CAT-WIDTH-PLUS1 200 false "south")
                "if the cat is outside the left boundary, then it
                 should bounce back inside the canvas"))

(run-tests cat-outside-left-boundary-next-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-outside-right-boundary-next-tests
  
  (check-equal? (cat-outside-right-boundary-next 
                 (make-cat 450 400 false "south")) 
                (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) 
                (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1) false "south")
                "if the cat is outside the right-bottom corner, then it
                 should bounce back inside the canvas")
  
  (check-equal? (cat-outside-right-boundary-next 
                 (make-cat 450 0 false "south")) 
                (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) 
                    HALF-CAT-HEIGHT-PLUS1 false "south")
                "if the cat is outside the top-right corner, then it
                 should bounce back inside the canvas")
  
  (check-equal? (cat-outside-right-boundary-next
                 (make-cat 450 200 false "south"))
                (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1) 
                          200 false "south")
                "if the cat is outside the right boundary, then it
                 should bounce back inside the canvas"))

(run-tests cat-outside-right-boundary-next-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-outside-boundary-next-tests

  (check-equal? (cat-outside-boundary-next (make-cat 450 0 true "north"))
             (cat-outside-right-boundary-next (make-cat 450 0 true "north"))
             "cat-outside-right-boundary-next should be called when the 
              cat is outside the right boundary")

  (check-equal? (cat-outside-boundary-next (make-cat 0 200 true "north"))
             (cat-outside-left-boundary-next (make-cat 0 200 true "north"))
             "cat-outside-left-boundary-next should be called when the 
              cat is outside the left boundary")

  (check-equal? (cat-outside-boundary-next (make-cat 200 400 true "north"))
             (make-cat 200 (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1) false 
                       "north")
             "if the cat is outside the south boundary then it is bounced
               back inside the canvas")

  (check-equal? (cat-outside-boundary-next (make-cat 200 0 true "north"))
            (make-cat 200 HALF-CAT-HEIGHT-PLUS1 false "north")
            "if the cat is outside the north boundary then it is bounced
             back inside the canvas")

  (check-equal? (cat-outside-boundary-next (make-cat 200 200 true "north"))
            (make-cat 200 200 true "north")
            "if the cat is inside the boundary then the 
             cat-outside-boundary-next function has no effect"))

(run-tests cat-outside-boundary-next-tests)


;; ----------------------------------------------------------------------------

(define-test-suite cat-after-button-up-tests
  
  (check-equal? (cat-after-button-up (make-cat 450 0 true "north"))
              (cat-outside-boundary-next (make-cat 450 0 true "north"))
              "cat-outside-boundary-next is called if the cat is 
               dropped outside the boundary")

  (check-equal? (cat-after-button-up (make-cat 200 200 true "north"))
              (make-cat 200 200 false "north")
              "cat is unselected when the cat is dropped inside the
                boundary")

  (check-equal? (cat-after-button-up (make-cat 200 200 false "north"))
              (make-cat 200 200 false "north")
              "if cat is not selected then the button up should not
               have any effect"))

(run-tests cat-after-button-up-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-button-down-tests
  
  (check-equal? (cat-after-button-down (make-cat 20 20 false "north") 20 20) 
              (make-cat 20 20 true "north")
              "cat should be selected if mouse is pressed inside the cat")

  (check-equal? (cat-after-button-down (make-cat 20 20 false "north") 100 100) 
              (make-cat 20 20 false "north")
              "cat should not be selected if mouse is
               pressed outside the cat"))

(run-tests cat-after-button-down-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-mouse-event-tests

  (check-equal? (cat-after-mouse-event selected-cat-south-at-20 
                                       100 100 "button-down")
              (cat-after-button-down selected-cat-south-at-20 100 100)
              "cat-after-button-down should be called on a 
               button-down mouse event")

  (check-equal? (cat-after-mouse-event selected-cat-south-at-20 
                                       100 100 "drag")
              (cat-after-drag selected-cat-south-at-20 100 100)
                "cat-after-drag should be called on a 
               drag mouse event")

  (check-equal? (cat-after-mouse-event selected-cat-south-at-20 
                                       100 100 "button-up")
              (cat-after-button-up selected-cat-south-at-20)
                "cat-after-button-up should be called on a 
               button-up mouse event")

  (check-equal? (cat-after-mouse-event selected-cat-south-at-20 
                                       100 100 "leave")
                 selected-cat-south-at-20
                "Mouse Events not in the data definition of 
                 FallingCatMouseEvent should be ignored"))

(run-tests cat-after-mouse-event-tests)


;; ----------------------------------------------------------------------------

(define-test-suite world-after-mouse-event-tests
  
  (check-equal? (world-after-mouse-event 
               unselected-unpaused-world-at-20-20-south 100 100 "button-down")
              (make-world (cat-after-mouse-event unselected-cat-south-at-20
                                                 100 100 "button-down")
                          (cat-after-mouse-event unselected-cat-south-at-20
                                                 100 100 "button-down")
                          false)
              "cat-after-mouse-event function should be called correctly")

  (check-equal? (world-after-mouse-event unselected-unpaused-world-at-20-20-south
                                       100 100 "button-down")
              (make-world (cat-after-mouse-event unselected-cat-south-at-20 
                                                 100 100 "button-down")
               (cat-after-mouse-event unselected-cat-south-at-20 
                                      100 100 "button-down")
               false)
              "cat-after-mouse-event function should be called correctly"))

(run-tests world-after-mouse-event-tests)

;; ----------------------------------------------------------------------------

(define-test-suite world-after-key-event-tests
  
  (check-equal? (world-after-key-event unpaused-world-at-20-20-south
                                       pause-key-event)
                paused-world-at-20-20-south
                "world should be paused after the pause key event")
  
  (check-equal? (world-after-key-event unpaused-world-at-20-20-south "q")
                unpaused-world-at-20-20-south
                "key events not in the data definition of FallingCatKeyEvent
                 should be ignored") 
  
  (check-equal? (world-after-key-event 
                 unselected-unpaused-world-at-20-20-south "left") 
                 (make-world (cat-after-left-key unselected-cat-south-at-20)
                             (cat-after-left-key unselected-cat-south-at-20)
                              false)
                 "cat-after-left-key function should be called correctly") 
   
   (check-equal? (world-after-key-event 
                  unselected-unpaused-world-at-20-20-south "right") 
                 (make-world (cat-after-right-key unselected-cat-south-at-20)
                             (cat-after-right-key unselected-cat-south-at-20)
                              false)
                 "cat-after-right-key function should be called correctly") 
   
   (check-equal? (world-after-key-event
                  unselected-unpaused-world-at-20-20-south "up") 
                 (make-world (cat-after-up-key unselected-cat-south-at-20)
                             (cat-after-up-key unselected-cat-south-at-20)
                              false)
                 "cat-after-up-key function should be called correctly") 
   
   (check-equal? (world-after-key-event 
                  unselected-unpaused-world-at-20-20-south "down") 
                 (make-world (cat-after-down-key unselected-cat-south-at-20)
                             (cat-after-down-key unselected-cat-south-at-20)
                              false)
                 "cat-after-down-key function should be called correctly")) 
   
(run-tests world-after-key-event-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-tick-tests
  
  (check-equal? (cat-after-tick (make-cat 100 100 true "north"))
                (cat-after-tick-helper-north 100 100 true) 
                "north helper function should be called correctly")

  (check-equal? (cat-after-tick (make-cat 100 100 true "west"))
                (cat-after-tick-helper-west 100 100 true)
                "west helper function should be called correctly")
  
  (check-equal? (cat-after-tick (make-cat 100 100 true "south"))
                (cat-after-tick-helper-south 100 100 true) 
                "south helper function should be called correctly")
  
  (check-equal? (cat-after-tick (make-cat 100 100 true "east"))
                (cat-after-tick-helper-east 100 100 true) 
                "east helper function should be called correctly"))

(run-tests cat-after-tick-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-tick-helper-south-tests
  
  (check-equal? (cat-after-tick-helper-south 100 100 true) 
                (make-cat 100 100 true "south") 
                "If cat is selected then it should not move")
  
  (check-equal? (cat-after-tick-helper-south 100 100 false) 
                (make-cat 100 108 false "south") 
                "If cat is unselected, after tick it should move
                 south if it is moving south")
  
  (check-equal? (cat-after-tick-helper-south 100 390 false) 
                (make-cat 100 (- CANVAS-HEIGHT HALF-CAT-HEIGHT-PLUS1) 
                          false "north") 
                "if cat goes tries to cross the bottom boundary,
                 it's feet should stop at it and direction reversed"))

(run-tests cat-after-tick-helper-south-tests)


;; ----------------------------------------------------------------------------

(define-test-suite cat-after-tick-helper-north-tests
  
  (check-equal? (cat-after-tick-helper-north 100 100 true) 
                (make-cat 100 100 true "north") 
                "If cat is selected then it should not move")
  
  (check-equal? (cat-after-tick-helper-north 100 390 false) 
                (make-cat 100 (- 390 CATSPEED) false "north") 
                "if cat is unselected, after tick it should 
                 move north if it is moving north")
  
  (check-equal? (cat-after-tick-helper-north 100 55 false) 
                (make-cat 100 HALF-CAT-HEIGHT-PLUS1 false "south") 
                "if cat tries to go across the top boundary, 
                 it should stop at it and direction reversed"))

(run-tests cat-after-tick-helper-north-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-tick-helper-west-tests
  
  (check-equal? (cat-after-tick-helper-west 100 100 true) 
                (make-cat 100 100 true "west") 
                "If cat is selected then it should not move")
  
  (check-equal? (cat-after-tick-helper-west 100 390 false) 
                (make-cat (- 100 CATSPEED) 390 false "west") 
                "if cat is unselected, after tick it should
                 move west if it is moving west")
  
  (check-equal? (cat-after-tick-helper-west 15 100 false) 
                (make-cat HALF-CAT-WIDTH-PLUS1 100 false "east")
                "if cat tries to go across the west boundary, 
                 it should stop at it and direction reversed"))

(run-tests cat-after-tick-helper-west-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-tick-helper-east-tests
  
  (check-equal? (cat-after-tick-helper-east 100 100 true)
                (make-cat 100 100 true "east") 
                "If cat is selected then it should not move")
  
  (check-equal? (cat-after-tick-helper-east 100 390 false) 
                (make-cat (+ 100 CATSPEED) 390 false "east") 
                "if cat is unselected, after tick it should 
                 move west if it is moving west")
  
  (check-equal? (cat-after-tick-helper-east 445 100 false)
                (make-cat (- CANVAS-WIDTH HALF-CAT-WIDTH-PLUS1)
                          100 false "west") 
                "if cat tries to go across the west boundary, 
                 it should stop at it and direction reversed"))

(run-tests cat-after-tick-helper-east-tests)

;; ----------------------------------------------------------------------------
      
(define-test-suite cat-after-left-key-tests
  
  (check-equal? (cat-after-left-key selected-cat-south-at-20) 
                (make-cat CAT1-X-COORD 20 true "west")
                "a selected cat should change direction
                 after direction key press")
  
  (check-equal? (cat-after-left-key unselected-cat-south-at-20) 
                (make-cat CAT1-X-COORD 20 false "south")
                "an unselected cat should not change direction
                 after direction key press"))

(run-tests cat-after-left-key-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-right-key-tests
 
  (check-equal? (cat-after-right-key selected-cat-south-at-20)
                (make-cat CAT1-X-COORD 20 true "east")
                "a selected cat should change direction 
                 after direction key press")
  (check-equal? (cat-after-right-key unselected-cat-south-at-20) 
                (make-cat CAT1-X-COORD 20 false "south")
                "an unselected cat should not change direction after
                 direction key press"))

(run-tests cat-after-right-key-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-up-key-tests
 
  (check-equal? (cat-after-up-key selected-cat-south-at-20) 
                (make-cat CAT1-X-COORD 20 true "north")
                "a selected cat should change direction 
                 after direction key press")
  (check-equal? (cat-after-up-key unselected-cat-south-at-20) 
                (make-cat CAT1-X-COORD 20 false "south")
                "an unselected cat should not change direction
                 after direction key press"))

(run-tests cat-after-up-key-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-down-key-tests
 
  (check-equal? (cat-after-down-key selected-cat-north-at-100) 
                (make-cat CAT1-X-COORD 100 true "south")
                "a selected cat should change direction 
                 after direction key press")
  (check-equal? (cat-after-down-key unselected-cat-north-at-92) 
                (make-cat CAT1-X-COORD 92 false "north")
                "an unselected cat should not 
                 change direction after direction key press"))

(run-tests cat-after-down-key-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-after-drag-tests
  
  (check-equal? (cat-after-drag (make-cat 20 20 true "north") 50 50) 
              (make-cat 50 50 true "north")
              "A cat at 50 50 should be returned if it is selected and 
               dragged at 50 50")

  (check-equal? (cat-after-drag (make-cat 20 20 false "north") 50 50) 
              (make-cat 20 20 false "north")
              "The cat should not move if it is not selected "))

(run-tests cat-after-drag-tests)

;; ----------------------------------------------------------------------------
  
(define-test-suite cat-dragged-outside-boundary-tests

  (check-equal? (cat-dragged-outside-boundary? (make-cat 450 0 true "north"))
                 true 
                 "cat-dragged-outside-boundary should return true if the cat
                  is dragged outside the east boundary")  
  (check-equal? (cat-dragged-outside-boundary? 
                 (make-cat 200 400 true "north"))
                true
                "cat-dragged-outside-boundary should return true if the cat
                  is dragged outside the south boundary")
  (check-equal? (cat-dragged-outside-boundary? (make-cat 0 200 true "north"))
                true
                 "cat-dragged-outside-boundary should return true if the cat
                  is dragged outside the west boundary")
  (check-equal? (cat-dragged-outside-boundary? (make-cat 200 0 true "north"))
                true
                 "cat-dragged-outside-boundary should return true if the cat
                  is dragged outside the north boundary"))

(run-tests cat-dragged-outside-boundary-tests)

;; ----------------------------------------------------------------------------

(define-test-suite cat-outside-boundary-tests
  
  (check-equal? (cat-outside-bottom-right-corner? 
                 (make-cat 450 400 true "west")) true 
              "cat-outside-bottom-right-corner? should return true if the 
               cat is outside the bottom-right-corner")

  (check-equal? (cat-outside-top-right-corner? 
                 (make-cat 450 0 true "west" )) true 
              "cat-outside-top-right-corner? should return true if the 
               cat is outside the top-right-corner")

  (check-equal? (cat-outside-top-left-corner? 
                 (make-cat 0 0 true "west" )) true 
              "cat-outside-top-left-corner? should return true if the 
               cat is outside the top-left-corner")

  (check-equal? (cat-outside-bottom-left-corner? 
                 (make-cat 0 450 true "west" )) true 
              "cat-outside-bottom-left-corner? should return true if the 
               cat is outside the bottom-left-corner")

  (check-equal? (cat-outside-right-boundary? unselected-cat-north-at-100)
                false 
              "cat-outside-right-boundary? should return false if the 
               cat is inside the boundary")

  (check-equal? (cat-outside-left-boundary? unselected-cat-north-at-100) 
                false 
              "cat-outside-left-boundary? should return false if the 
               cat is inside the boundary")

  (check-equal? (cat-outside-bottom-boundary? unselected-cat-north-at-100)
                false 
              "cat-outside-bottom-boundary? should return false if the 
               cat is inside the boundary")

  (check-equal? (cat-outside-top-boundary? unselected-cat-north-at-100) 
                false 
              "cat-outside-top-boundary? should return false if the 
               cat is inside the boundary"))

(run-tests cat-outside-boundary-tests)

;; ----------------------------------------------------------------------------
  
(define-test-suite cat-direction-tests
 
  (check-equal? (cat-north? unselected-cat-south-at-20) false 
              "cat-north? should return false if the cat is facing south")

  (check-equal? (cat-east? unselected-cat-south-at-20) false 
              "cat-east? should return false if the cat is facing south")

  (check-equal? (cat-south? unselected-cat-south-at-20) true 
              "cat-south? should return true if the cat is facing south")

  (check-equal? (cat-west? unselected-cat-south-at-20) false 
              "cat-west? should return false if the cat is facing south"))
  
(run-tests cat-direction-tests)

;; ----------------------------------------------------------------------------




