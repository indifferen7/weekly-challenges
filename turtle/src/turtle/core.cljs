(ns turtle.core
  (:require [reagent.core :as reagent :refer [atom]]
            [turtle.turtle :as turtle]))

(enable-console-print!)

(def centre-of-screen
  [(/ (.-innerWidth js/window) 2)
   (/ (.-innerHeight js/window) 2)])

(defonce app-state (atom {:result [] :turtle (turtle/new-turtle centre-of-screen) }))

(defn draw-square
  [size]
  (into []
        (flatten
         (repeat 4 [{:type :move :distance size}
                    {:type :turn :degrees 90}]))))

(defn draw-square-flower
  [size]
  (into []
        (flatten
         (repeat 18 (conj (draw-square size) { :type :turn :degrees 20 })))))

(def sample-commands
  (-> []
      (conj {:type :style :style "solid"})
      (conj {:type :color :color "green"})
      (conj {:type :width :width 3 })
      (into (draw-square-flower 200))
      (conj {:type :turn :degrees 10})
      (conj {:type :style :style "dotted"})
      (conj {:type :color :color "purple"})
      (conj {:type :width :width 2 })
      (into (draw-square-flower 100))))

(reset! app-state (turtle/process (:turtle @app-state) sample-commands))

(defn surface-element
  "Returns the drawing surface element, i.e.
  the SVG element."
  []
  (. js/document (getElementById "surface")))

(defn surface-component []
   [:svg {:id "surface" }
    (for [{:keys [from to color width style]} (:result @app-state)]
      [:line {:x1 (first from)
              :y1 (second from)
              :x2 (first to)
              :y2 (second to)
              :stroke-width width
              :stroke color
              :stroke-dasharray style
              }])
   ]
  )

(reagent/render-component
  [surface-component]
  (. js/document (getElementById "app")))
