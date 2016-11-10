(ns casual-drawing.core
  (:require [casual-drawing.shapes :refer :all]))

(defn draw-pixel
  "Returns a new image with a pixel drawn at
  the given coordinate in the specified color."
  [image color xy] (into image (new-pixel color xy)))

(defn draw-pixels
  "Returns a new image with pixels drawn at
  the given coordinates in the specified color."
  [image color xys]
  (into {} (map #(draw-pixel image color %) xys)))