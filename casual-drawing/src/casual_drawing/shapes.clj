(ns casual-drawing.shapes)

(defn new-pixel
  "Creates a new pixel."
  [[r g b] [x y]] {[x y] [r g b]})

(defn new-line
  "This function implements the Bresenham algorithm to
  produce a vector of pixels that can be used to
  draw a line."
  [[start-x start-y] [end-x end-y]]
  (let [dx (Math/abs (long (- end-x start-x)))
        dy (Math/abs (long (- end-y start-y)))
        sx (if (< start-x end-x) 1 -1)
        sy (if (< start-y end-y) 1 -1)]

    (loop [[x y] [start-x start-y]
           acc [[x y]]
           err (- dx dy)]
      (if (= [x y] [end-x end-y])
        acc
        (let [e2 (* err 2)
              err-y (if (> e2 (- dy)) (- dy) 0)
              err-x (if (< e2 dx) dx 0)
              err (+ err err-y err-x)
              next-x (if (> e2 (- dy)) (+ x sx) x)
              next-y (if (< e2 dx) (+ y sy) y)
              next [next-x next-y]]
          (recur next (conj acc next) err))))))

(defn- circle-points
  [[midpoint-x midpoint-y] [x y]]
  [[(+ midpoint-x x) (+ midpoint-y y)]
   [(+ midpoint-x y) (+ midpoint-y x)]
   [(- midpoint-x y) (+ midpoint-y x)]
   [(- midpoint-x x) (+ midpoint-y y)]
   [(- midpoint-x x) (- midpoint-y y)]
   [(- midpoint-x y) (- midpoint-y x)]
   [(+ midpoint-x y) (- midpoint-y x)]
   [(+ midpoint-x x) (- midpoint-y y)]])

(defn new-circle
  "This function implements the Midpoint circle algorithm
  to produce a vector of pixels that can be used to draw
   a circle."
  [[midpoint-x midpoint-y] radius]
  (loop [x radius
         y 0
         err 0
         acc []]
    (if (< x y)
      (mapcat identity (apply map list acc))
      (let [new-points (circle-points [midpoint-x midpoint-y] [x y])
            new-y (inc y)
            consider-x (> (inc (* 2 (- err x))) 0)
            new-x (if consider-x (dec x) x)
            err-modifier (if consider-x
                           (+ (+ 1 (* 2 new-y)) (- 1 (* 2 new-x)))
                           (+ 1 (* 2 new-y)))
            new-err (+ err err-modifier)]
        (recur new-x new-y new-err (conj acc new-points))))))

(defn dot
  "Returns a dotted shape."
  [shape]
  (take-nth 2 shape))

(defn dash
  "Returns a dashed shape."
  [shape]
  (remove (into #{} (take-nth 10 shape)) shape))