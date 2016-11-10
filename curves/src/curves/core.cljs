(ns curves.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

; Bezier calculation functions

(defn calculate
  "Perform a quadratic Bezier curve calculation."
  [[a b c] t]
  (+ (* a (.pow js/Math (- 1 t) 2))
     (* 2 b (- 1 t) t)
     (* c (.pow js/Math t 2))))

(defn bezier-point
  "Calculates a Bezier curve point for the provided
  reference coordinate."
  [[[ax ay] [bx by] [cx cy]] t]
  [(calculate [ax bx cx] t) (calculate [ay by cy] t)])

(defn bezier-points
  "Calculates Bezier curve points for the provided
  refrence coordinates."
  [abc]
  (map #(bezier-point abc (* % 0.01)) (range 0 101)))

; Application goes here

(defonce data (atom { :abc [] :curves []}))

(defn complete?
  "Determines if all control points are available."
  [abc] (if (= 3 (count abc)) true false))

(defn alter-data
  "Alters the data atom to a new state. Intended to be
  used with swap! so that the state transition happens
  within a transaction."
  [{abc :abc, curves :curves} point]
  (let [new-abc (if (= 3 (count abc)) [point] (conj abc point))]
    (if (complete? new-abc)
      {:abc new-abc :curves (conj curves (bezier-points new-abc))}
      {:abc new-abc :curves curves}
    )))

(defn surface-element
  "Returns the drawing surface element, i.e.
  the SVG element."
  []
  (. js/document (getElementById "surface")))

(defn register-click
  "Extracts the point coordinate and swaps the
  state of the data atom."
  [e]
  (let [rect (.getBoundingClientRect (surface-element))
        x (int (- (.-clientX e) (.-left rect)))
        y (int (- (.-clientY e) (.-top rect)))]
    (swap! data alter-data [x y])))

(defn curve->string
  "Converts a curve into a string that fits nicely into
  a polyline element."
  [curve]
  (let [strings (for [[x y] curve] (str x "," y))]
    (clojure.string/join " " strings)))

(defn surface-component
  "A reagent component that reacts on updates
  to the data atom."
  []
  [:div
   [:svg {:width 400 :height 200 :id "surface"}
    (if (and (empty? (:abc @data)) (empty? (:curves @data)))
      [:text {:x 50 :y 50 :font-size 30} "Click anywhere to start drawing!"])
    (for [curve (:curves @data)]
      [:polyline {:points (curve->string curve) :style {:fill "none" :stroke "black" :stroke-width 2}}])
    (if-let [[x y] (get (:abc @data) 0)]
      [:text {:x x :y y :font-size 20} "a"])
    (if-let [[x y] (get (:abc @data) 1)]
      [:text {:x x :y y :font-size 20} "b"])
    (if-let [[x y] (get (:abc @data) 2)]
      [:text {:x x :y y :font-size 20} "c"])
    ]
   ]
  )

(reagent/render-component
  [surface-component]
  (. js/document (getElementById "app")))

(.addEventListener (surface-element) "click" (fn [e] (register-click e)))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
