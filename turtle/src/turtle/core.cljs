(ns turtle.core
  (:require [reagent.core :as reagent :refer [atom]]
            [turtle.turtle :as turtle]
	    [cljs.reader :as reader]))

;;;; Turtle graphics example using turtle.cljs

(defn centre-of-screen
  []
  [(/ (.-innerWidth js/window) 2)
   (/ (.-innerHeight js/window) 2)])

(defn new-app-state
  "Create a fresh app state"
  []
  {:result [] :turtle (turtle/new-turtle (centre-of-screen)) })

(defonce app-state (atom (new-app-state)))

(defn reset-state!
  "Reset the app state"
  []
  (reset! app-state (new-app-state)))

(defn update-state!
  "Update the app state through processing the provided actions"
  [actions]
  (swap! app-state into (turtle/process (:turtle @app-state) actions)))

;; sample shapes

(defn new-square
  [size]
  (into []
        (flatten
         (repeat 4 [{:type :move :distance size}
                    {:type :turn :degrees 90}]))))

(defn new-square-flower
  [size]
  (into []
        (flatten
         (repeat 18 (conj (new-square size) { :type :turn :degrees 20 })))))

(def sample-actions
  (-> []
      (conj {:type :style :style "solid"})
      (conj {:type :color :color "green"})
      (conj {:type :width :width 3 }) 
      (into (new-square-flower 200))
      (conj {:type :turn :degrees 10})
      (conj {:type :style :style "dotted"})
      (conj {:type :color :color "purple"})
      (conj {:type :width :width 2 })
      (into (new-square-flower 100))))

(defn surface-component
  "The SVG component."
  []
  [:svg {:id "surface" }
   (for [{:keys [from to color width style]} (:result @app-state)]
     [:line {:x1 (first from)
             :y1 (second from)
             :x2 (first to)
             :y2 (second to)
             :stroke-width width
             :stroke color
             :stroke-dasharray (case style
                                 "dotted" "0.1% 0.1%"
                                 "dashed" "1% 0.1%"
                                 "0% 0%")}])])

(defn controls-component
  "Component for controlling the ui."
  []
  [:div
   [:h3 "Turtle graphics"]
   [:div.description
    "In this demo, actions are added as a vector of maps in the 
    below textbox. For more info, checkout "
    [:a { :href "https://github.com/indifferen7/weekly-challenges/tree/master/turtle"}
     " the github repo"]
    ". Some sample commands are already present, click 'Draw' to see what happens."]
   [:textarea {:id "actions" :defaultValue (str sample-actions)}]
   [:input {:type "button" :value "Draw" :onClick #(update-state! (reader/read-string (.-value (. js/document (getElementById "actions")))))}]
   [:input {:type "button" :value "Reset" :onClick #(reset-state!) }]])

(reagent/render-component
  [surface-component]
  (. js/document (getElementById "app")))

(reagent/render-component
  [controls-component]
  (. js/document (getElementById "controls")))
