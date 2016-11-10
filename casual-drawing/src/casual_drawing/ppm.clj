(ns casual-drawing.ppm
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(defn width
  "Returns the width of the image (in pixels)."
  [image]
  (inc (apply max (map first (keys image)))))

(defn height
  "Returns the height of the image (in pixels)."
  [image]
  (inc (apply max (map second (keys image)))))

(defn- color2p3
  "Stringify a color into a format suitable
  for the P3 ppm format, e.g. ' 255 255 255 '."
  [c]
  (str " " (str/join " " c) " "))

(defn- colors2p3
  "Stringify colors into a format suitable
  for a row in the P3 ppm format, e.g.
  ' 255 255 255   122 122 122 '."
  [cs]
  (str/join (map color2p3 cs)))

(defn- get-row-colors
  "Returns a sequence of all colors at the given
  row (specified by y) in an image, from left to
  right. If a coordinate lacks a color it will fall
  back on white color."
  [image width y]
  (for [x (range 0 width)]
    (get image [x y] [255 255 255])))

(defn save
  "Saves the image into P3 ppm format, optionally
  with a provided file name."
  ([image] (save image "image"))
  ([image title]
   (let [width (width image)
         height (height image)
         image-rows (map #(colors2p3 (get-row-colors image width %)) (range 0 height))]
     (with-open [wrtr (io/writer (str title ".ppm"))]
       (.write wrtr (str "P3\n" width " " height "\n255\n"))
       (doseq [row image-rows]
         (.write wrtr (str row "\n")))))))