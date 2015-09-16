;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-beginner-reader.ss" "lang")((modname robots) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f ())))
(require rackunit)
(require rackunit/text-ui)
(require "extras.rkt")

(provide initial-robot)
(provide robot-left) 
(provide robot-right)
(provide robot-forward)
(provide robot-north?)
(provide robot-south?)
(provide robot-east?)
(provide robot-west?)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; DATA DEFINITIONS

; defining constants ROBOT-RADIUS, CANVAS-WIDTH and CANVAS-HEIGHT
(define ROBOT-RADIUS 15)               
(define CANVAS-WIDTH 200)
(define CANVAS-HEIGHT 400)

(define-struct robot (x y face-direction))

; A Robot is a (make-robot Number Number String)
; x and y are the coordinates of the center of the circular robot, in pixels.
; face-direction is the direction which the robot is facing -> north, south, east, west

; TEMPLATE
; robot-fn : robot -> ??
; (define (robot-fn robot)
;  (... 
;   (robot-x robot)
;   (robot-y robot)
;   (robot-face-direction robot)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; initial-robot : Integer Integer -> Robot
; GIVEN: a set of (x,y) coordinates
; RETURNS: a robot with its center at those coordinates, facing north
; (up).
; EXAMPLES: (initial-robot 60 70) => (make-robot 60 70 "north")
; STRATEGY: Structural Decomposition
(define (initial-robot x y)
  (make-robot x y "north"))

; robot-left : Robot -> Robot
; GIVEN: a robot
; RETURNS: a robot like the original, but turned 90 degrees left
; EXAMPLES: 
;  (robot-left (make-robot 15 30 "north")) => (make-robot 15 30 "west")
;  (robot-left (make-robot 15 30 "east")) => (make-robot 15 30 "north")
; STRATEGY: Structural Decomposition
(define (robot-left robot)
  (cond [(string=? (robot-face-direction robot) "north") 
         (make-robot (robot-x robot) (robot-y robot) "west")]
        [(string=? (robot-face-direction robot) "west") 
         (make-robot (robot-x robot) (robot-y robot) "south")]
        [(string=? (robot-face-direction robot) "south") 
         (make-robot (robot-x robot) (robot-y robot) "east")]
        [(string=? (robot-face-direction robot) "east") 
         (make-robot (robot-x robot) (robot-y robot) "north")]))

; robot-right : Robot -> Robot
; GIVEN: a robot
; RETURNS: a robot like the original, but turned 90 degrees right.
; EXAMPLES: 
;  (robot-left (make-robot 15 30 "north")) => (make-robot 15 30 "east")
;  (robot-left (make-robot 15 30 "east")) => (make-robot 15 30 "south")
; STRATEGY: Structural Decomposition
(define (robot-right robot)
  (cond [(string=? (robot-face-direction robot) "north") 
         (make-robot (robot-x robot) (robot-y robot) "east")]
        [(string=? (robot-face-direction robot) "west") 
         (make-robot (robot-x robot) (robot-y robot) "north")]
        [(string=? (robot-face-direction robot) "south") 
         (make-robot (robot-x robot) (robot-y robot) "west")]
        [(string=? (robot-face-direction robot) "east") 
         (make-robot (robot-x robot) (robot-y robot) "south")]))

; robot-forward : Robot PosInt -> Robot
; GIVEN: a robot and a distance
; RETURNS: a robot like the given one, but moved forward by the
; specified number of pixels.  If moving forward the specified number of
; pixels would cause the robot to move from being
; entirely inside the canvas to being even partially outside the canvas,
; then the robot should stop at the wall.
; EXAMPLES: 
;  (robot-forward (make-robot 15 30 "north") 5) => (make-robot 15 25 "north")
;  robot-forward (make-robot 25 30 "south") 370) => (make-robot 25 385 "south")
; STRATEGY: Function Composition
(define (robot-forward robot distance)
  (cond [(string=? (robot-face-direction robot) "north") 
         (move-robot-northwards robot distance)]
        [(string=? (robot-face-direction robot) "east") 
         (move-robot-eastwards robot distance)]
        [(string=? (robot-face-direction robot) "south") 
         (move-robot-southwards robot distance)]
        [(string=? (robot-face-direction robot) "west") 
         (move-robot-westwards robot distance)]))

; move-robot-eastwards : Robot PosInt -> Robot
; GIVEN: a robot and a distance
; RETURNS: a robot like the given one, but moved eastward by the
; specified number of pixels.  If moving forward the specified number of
; pixels would cause the robot to move from being
; entirely inside the canvas to being even partially outside the canvas,
; then the robot should stop at the wall.
; EXAMPLES: 
; (move-robot-eastwards (make-robot 15 25 "east") 5) => (make-robot 20 25 "east")
; (move-robot-eastwards (make-robot 15 30 "east") 270) => (make-robot 185 30 "east")
; STRATEGY: Structural Decomposition
(define (move-robot-eastwards robot distance)
  (if (or (> (robot-x robot) (- CANVAS-WIDTH ROBOT-RADIUS)) 
          (not (> (+ (robot-x robot) distance) (- CANVAS-WIDTH ROBOT-RADIUS)))
          (not (and (>= (robot-y robot) ROBOT-RADIUS) (<= (robot-y robot) (- CANVAS-HEIGHT ROBOT-RADIUS)) 
                    (>= (+ (robot-x robot) distance) (- CANVAS-WIDTH ROBOT-RADIUS)))))  
      (make-robot (+ (robot-x robot) distance) (robot-y robot) (robot-face-direction robot)) 
      (make-robot (- CANVAS-WIDTH ROBOT-RADIUS) (robot-y robot) (robot-face-direction robot)))) 

; move-robot-northwards : Robot PosInt -> Robot
; GIVEN: a robot and a distance
; RETURNS: a robot like the given one, but moved northward by the
; specified number of pixels.  If moving forward the specified number of
; pixels would cause the robot to move from being
; entirely inside the canvas to being even partially outside the canvas,
; then the robot should stop at the wall.
; EXAMPLES: 
; (move-robot-northwards (make-robot 15 30 "north") 5) => (make-robot 15 25 "north")
; (move-robot-northwards (make-robot 20 17 "north") 270) => (make-robot 20 15 "north")
; STRATEGY: Structural Decomposition
(define (move-robot-northwards robot distance)
  (if (or (<(robot-y robot) ROBOT-RADIUS) 
          (not (< (- (robot-y robot) distance) ROBOT-RADIUS))
          (not (and (>= (robot-x robot) ROBOT-RADIUS) (<= (robot-x robot) (- CANVAS-WIDTH ROBOT-RADIUS)) 
                (<= (- (robot-y robot) distance) ROBOT-RADIUS)))) 
      (make-robot (robot-x robot) (- (robot-y robot) distance) (robot-face-direction robot)) 
      (make-robot (robot-x robot) ROBOT-RADIUS (robot-face-direction robot))))

; move-robot-westwards : Robot PosInt -> Robot
; GIVEN: a robot and a distance
; RETURNS: a robot like the given one, but moved westward by the
; specified number of pixels.  If moving forward the specified number of
; pixels would cause the robot to move from being
; entirely inside the canvas to being even partially outside the canvas,
; then the robot should stop at the wall.
; EXAMPLES: 
; (move-robot-westwards (make-robot 40 25 "west") 5) => (make-robot 35 25 "west")
; (move-robot-westwards (make-robot 40 60 "west") 270) => (make-robot 15 60 "west")
; STRATEGY: Structural Decomposition
(define (move-robot-westwards robot distance)
  (if (or (< (robot-x robot) ROBOT-RADIUS) 
          (not (< (- (robot-x robot) distance) ROBOT-RADIUS))
          (not (and (>= (robot-y robot) ROBOT-RADIUS) (<= (robot-y robot) (- CANVAS-HEIGHT ROBOT-RADIUS)) 
                (<= (- (robot-x robot) distance) ROBOT-RADIUS)))) 
      (make-robot (- (robot-x robot) distance) (robot-y robot) (robot-face-direction robot))
      (make-robot ROBOT-RADIUS (robot-y robot) (robot-face-direction robot)))) 

; move-robot-southwards : Robot PosInt -> Robot
; GIVEN: a robot and a distance
; RETURNS: a robot like the given one, but moved southward by the
; specified number of pixels.  If moving forward the specified number of
; pixels would cause the robot to move from being
; entirely inside the canvas to being even partially outside the canvas,
; then the robot should stop at the wall.
; EXAMPLES: 
; (move-robot-southwards (make-robot 25 25 "south") 5) => (make-robot 25 30 "south")
; (move-robot-southwards (make-robot 25 30 "south") 270) => (make-robot 25 385 "south")
; STRATEGY: Structural Decomposition
(define (move-robot-southwards robot distance)
  (if (or (> (robot-y robot) (- CANVAS-HEIGHT ROBOT-RADIUS))  
          (not (> (+ (robot-y robot) distance) (- CANVAS-HEIGHT ROBOT-RADIUS)))
          (not (and (>= (robot-x robot) ROBOT-RADIUS) (<= (robot-x robot) (- CANVAS-WIDTH ROBOT-RADIUS)) 
                (>= (+ (robot-y robot) distance) (- CANVAS-HEIGHT ROBOT-RADIUS))))) 
      (make-robot (robot-x robot)(+ (robot-y robot) distance)(robot-face-direction robot))
      (make-robot (robot-x robot) (- CANVAS-HEIGHT ROBOT-RADIUS)(robot-face-direction robot)))) 

; robot-north? : Robot -> Boolean
; GIVEN: a robot
; ANSWERS: whether the robot is facing north.
; STRATEGY: Structural Decomposition
(define (robot-north? robot)
  (string=? (robot-face-direction robot) "north"))

; robot-south? : Robot -> Boolean
; GIVEN: a robot
; ANSWERS: whether the robot is facing south.
; STRATEGY: Structural Decomposition
(define (robot-south? robot)
  (string=? (robot-face-direction robot) "south"))

; robot-east? : Robot -> Boolean
; GIVEN: a robot
; ANSWERS: whether the robot is facing east.
; STRATEGY: Structural Decomposition
(define (robot-east? robot)
  (string=? (robot-face-direction robot) "east"))

; robot-west? : Robot -> Boolean
; GIVEN: a robot
; ANSWERS: whether the robot is facing west.
; STRATEGY: Structural Decomposition
(define (robot-west? robot)
  (string=? (robot-face-direction robot) "west"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; TESTS

; test to see if the initial-robot function works correctly
(check-equal? (initial-robot 15 30)(make-robot 15 30 "north") 
              "initial-robot function with parameters 4 and 5 
               should make a robot located at (4,5) and facing north")

; tests to check the robot direction functions
(define-test-suite robot-direction-boolean
  (check-equal? (robot-north? (make-robot 40 40 "north")) true "robot-north? should return true 
                                                                if the robot is facing north")
  (check-equal? (robot-north? (make-robot 40 40 "east")) false "robot-north? should return false 
                                                                if the robot is facing east")
  (check-equal? (robot-east? (make-robot 40 40 "east")) true "robot-east? should return true 
                                                                if the robot is facing east")
  (check-equal? (robot-east? (make-robot 40 40 "west")) false "robot-east? should return false 
                                                                if the robot is facing west")
  (check-equal? (robot-south? (make-robot 40 40 "south")) true "robot-south? should return true 
                                                                if the robot is facing south")
  (check-equal? (robot-south? (make-robot 40 40 "north")) false "robot-south? should return false 
                                                                if the robot is facing north")
  (check-equal? (robot-west? (make-robot 40 40 "west")) true "robot-west? should return true 
                                                                if the robot is facing west")
  (check-equal? (robot-west? (make-robot 40 40 "north")) false "robot-west? should return false 
                                                                if the robot is facing north"))

; testing the robot movements outside the canvas boundary
(define-test-suite robot-movement-outside-boundary
  (check-equal? (robot-forward (make-robot 30 -40 "north") 50) (make-robot 30 -90 "north") 
                "robot moves freely outside the canvas")
  (check-equal? (robot-forward (make-robot -30 40 "east") 50) (make-robot 20 40 "east") 
                "robot moves freely outside the canvas")
  (check-equal? (robot-forward (make-robot 30 -40 "south") 50) (make-robot 30 10 "south") 
                "robot moves freely outside the canvas")
  (check-equal? (robot-forward (make-robot 30 10 "north") 50) (make-robot 30 -40 "north")
                "robot moves freely outside the canvas"))
(run-tests robot-movement-outside-boundary)

; tests to see if the robot-left and robot-right functions work correctly
(define-test-suite robot-direction-tests
  (check-equal? (robot-left (make-robot 15 30 "north"))(make-robot 15 30 "west") 
                "If the robot is facing north, then it should face 
                 west after a left turn")
  (check-equal? (robot-left (make-robot 15 30 "east"))(make-robot 15 30 "north") 
                "If the robot is facing east, then it should face north 
                 after a left turn")
  (check-equal? (robot-left (make-robot 15 30 "west"))(make-robot 15 30 "south") 
                "If the robot is facing west, then it should face south 
                 after a left turn")
  (check-equal? (robot-left (make-robot 15 30 "south"))(make-robot 15 30 "east") 
                "If the robot is facing south, then it should face east
                 after a left turn") 
  (check-equal? (robot-right (make-robot 15 30 "north"))(make-robot 15 30 "east")
                "If the robot is facing north, then it should face east 
                 after a right turn")
  (check-equal? (robot-right (make-robot 15 30 "east"))(make-robot 15 30 "south")
                "If the robot is facing east, then it should face south 
                 after a right turn")
  (check-equal? (robot-right (make-robot 15 30 "west"))(make-robot 15 30 "north")
                "If the robot is facing west, then it should face north 
                 after a right turn")
  (check-equal? (robot-right (make-robot 15 30 "south"))(make-robot 15 30 "west")
                "If the robot is facing south, then it should face west 
                 after a right turn"))

; testing northward movement of the robot
(define-test-suite robot-northward-movement-tests
  (check-equal? (robot-forward (make-robot 15 30 "north") 5) (make-robot 15 25 "north") 
                "y coordinate should decrease by 5 when robot 
                is moved 5 distance units facing north")
  (check-equal? (robot-forward (make-robot 20 17 "north") 5) (make-robot 20 15 "north") 
                "y coordinate should stop decreasing at 15 
                when robot is moved facing north")
  (check-equal? (robot-forward (make-robot 20 15 "north") 5) (make-robot 20 15 "north")
                "Boundary Test: y coordinate should stop 
                 decreasing at 15 when robot is moved facing north"))

; testing southward movement of the robot
(define-test-suite robot-southward-movement-tests
  (check-equal? (robot-forward (make-robot 25 25 "south") 5) (make-robot 25 30 "south")
                "y coordinate should increase by 5 when robot 
                 is moved 5 distance units facing south")
  (check-equal? (robot-forward (make-robot 25 30 "south") 370) (make-robot 25 385 "south") 
                "y coordinate should stop increasing at 385 
                 when robot is moved facing south")
  (check-equal? (robot-forward (make-robot 25 385 "south") 5) (make-robot 25 385 "south") 
                "Boundary Test: y coordinate should stop increasing at 
                 385 when robot is moved facing south"))

; testing eastward movement of the robot
(define-test-suite robot-eastward-movement-tests
  (check-equal? (robot-forward (make-robot 15 25 "east") 5) (make-robot 20 25 "east") 
                "x coordinate should increase by 5 when robot is
                 moved 5 distance units facing east")
  (check-equal? (robot-forward (make-robot 15 30 "east") 270) (make-robot 185 30 "east")
                "x coordinate should stop increasing at 185 
                when robot is moved facing east")
  (check-equal? (robot-forward (make-robot 185 30 "east") 17) (make-robot 185 30 "east") 
                "Boundary Test: x coordinate should stop increasing 
                  at 185 when robot is moved facing east"))

; testing westward movement of the robot
(define-test-suite robot-westward-movement-tests
  (check-equal? (robot-forward (make-robot 40 25 "west") 5) (make-robot 35 25 "west")
                "x coordinate should decrease by 5 when robot is
                 moved 5 distance units facing west")
  (check-equal? (robot-forward (make-robot 40 60 "west") 30) (make-robot 15 60 "west")
                "x coordinate should stop decreasing at 15 when 
                 robot is moved facing west")
  (check-equal? (robot-forward (make-robot 15 30 "west") 10) (make-robot 15 30 "west")
                "Boundary Test: x coordinate should stop decreasing 
                 at 15 when robot is moved facing west"))

; testing the trapping of robot if it tries to move across the canvas
(define-test-suite trap-robot
  (check-equal? (robot-forward (make-robot -10 100 "east") 500) (make-robot 185 100 "east") 
                "x coordinate should stop increasing at 185 when robot is
                 moved 500 distance units facing east while being initially 
                 outside the canvas but inside the same vertical line as the canvas")

  (check-equal? (robot-forward (make-robot 50 600 "north") 800) (make-robot 50 15 "north") 
                "y coordinate should stop decreasing at 15 when robot is
                 moved 800 distance units facing north while being initially 
                 outside the canvas but inside the same horizontal line as the canvas")

  (check-equal? (robot-forward (make-robot 500 100 "west") 700) (make-robot 15 100 "west") 
                "x coordinate should stop decreasing at 15 when robot is
                 moved 700 distance units facing west while being initially 
                 outside the canvas but inside the same vertical line as the canvas")

  (check-equal? (robot-forward (make-robot 50 -400 "south") 900) (make-robot 50 385 "south") 
                "y coordinate should stop increasing at 385 when robot is
                 moved 900 distance units facing south while being initially 
                 outside the canvas but inside the same horizontal line as the canvas"))

; running test suites

(run-tests robot-direction-boolean)
(run-tests robot-direction-tests)
(run-tests robot-northward-movement-tests)
(run-tests robot-southward-movement-tests)
(run-tests robot-eastward-movement-tests)
(run-tests robot-westward-movement-tests)
(run-tests trap-robot)