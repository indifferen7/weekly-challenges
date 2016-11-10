(ns casual-drawing.core-test
  (:require [clojure.test :refer :all]
            [casual-drawing.core :refer :all]
            [casual-drawing.ppm :refer :all]))

(deftest dimensions
  (is (= 2 (width sample-image)))
  (is (= 4 (height sample-image))))
