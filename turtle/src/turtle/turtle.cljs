(ns turtle.turtle)

(defn new-turtle
  [[x y]]
  {:pos [x y]
   :degrees 0
   :down? true
   :color "black"
   :width 2
   :style "solid"})

(defn turn
  "Calculates the new degree value of the turtle"
  [turtle-degrees degrees]
  (let [result (mod (+ turtle-degrees degrees) 360)]
    (if (< result 0)
      (+ result 360)
      result)))

(defn move
  "Calculates the destination coordinate of a turtle move"
  [[x y] turtle-degrees distance]
  (let [radians (* (- turtle-degrees 90) (/ Math/PI 180))]
    [(Math/round (+ x (* distance (Math/cos radians))))
     (Math/round (+ y (* distance (Math/sin radians))))]))

(defn resolve-style
  [style]
  (case style
    "dotted" "0.1% 0.1%"
    "dashed" "1% 0.1%"
    "0% 0%"))

(defn next-turtle
  [turtle action]
  (case (:type action)
    :up (assoc turtle :down? false)
    :down (assoc turtle :down? true)
    :turn (assoc turtle :degrees (turn (:degrees turtle) (:degrees action)))
    :move (assoc turtle :pos (move (:pos turtle) (:degrees turtle) (:distance action)))
    :color (assoc turtle :color (:color action))
    :width (assoc turtle :width (:width action))
    :style (assoc turtle :style (resolve-style (:style action)))
    turtle))

(defn next-result
  [turtle result action]
  (if (and (:down? turtle) (= :move (:type action)))
    (let [from (:pos turtle)
          to (move (:pos turtle) (:degrees turtle) (:distance action))
          color (:color turtle)
          width (:width turtle)
          style (:style turtle)]
      (conj result {:from from :to to :color color :width width :style style}))
    result))

(defn process-actions
  [turtle actions]
  (loop [remaining actions
         result {:turtle turtle :result []}]
    (if (empty? remaining)
      result
      (let [head (first remaining)]
        (recur (rest remaining)
               {:turtle (next-turtle (:turtle result) head)
                :result (next-result (:turtle result) (:result result) head)})))))

(defn process
  [turtle actions]
  (process-actions turtle actions))
