(ns turtle.turtle)

;;;; Turtle graphics!

(defn- turn
  "Calculates the new degree value of the turtle."
  [turtle-degrees degrees]
  (let [result (mod (+ turtle-degrees degrees) 360)]
    (if (< result 0)
      (+ result 360)
      result)))

(defn- move
  "Calculates the destination coordinate of a turtle move."
  [[x y] turtle-degrees distance]
  (let [radians (* (- turtle-degrees 90) (/ Math/PI 180))]
    [(Math/round (+ x (* distance (Math/cos radians))))
     (Math/round (+ y (* distance (Math/sin radians))))]))


(defn- next-turtle
  "Determines the next turtle state based on the provided action."
  [turtle action]
  (case (:type action)
    :up (assoc turtle :down? false)
    :down (assoc turtle :down? true)
    :turn (assoc turtle :degrees (turn (:degrees turtle) (:degrees action)))
    :move (assoc turtle :pos (move (:pos turtle) (:degrees turtle) (:distance action)))
    :color (assoc turtle :color (:color action))
    :width (assoc turtle :width (:width action))
    :style (assoc turtle :style (:style action))
    turtle))

(defn next-result
  "Determines the next result state based on the provided action."
  [turtle result action]
  (if (and (:down? turtle) (= :move (:type action)))
    (let [from (:pos turtle)
          to (move (:pos turtle) (:degrees turtle) (:distance action))
          color (:color turtle)
          width (:width turtle)
          style (:style turtle)]
      (conj result {:from from :to to :color color :width width :style style}))
    result))

(defn new-turtle
  "Creates a new turtle with default values."
  [[x y]]
  {:pos [x y]
   :degrees 0
   :down? true
   :color "black"
   :width 2
   :style "solid"})

(defn process
  "This function takes a turtle and a vector of actions. The output
  of the function is a map containing the resulting turtle and all 
  lines generated from the action."
  [turtle actions]
  (reduce (fn [result current]
            {:turtle (next-turtle (:turtle result) current)
             :result (next-result (:turtle result) (:result result) current)})
          {:turtle turtle :result []}
          actions))
