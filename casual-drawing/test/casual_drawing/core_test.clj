(ns casual-drawing.core-test
  (:require [clojure.test :refer :all]
            [casual-drawing.core :refer :all]
            [casual-drawing.shapes :refer :all]))

;minimalistic image
(def sample-image {[1 2] [10 10 10]
                   [1 3] [20 20 20]})

(deftest test-draw-pixel
  (is (=
        {[1 2] [10 10 10],
         [1 3] [20 20 20],
         [1 4] [100 100 100]}
        (draw-pixel sample-image [100 100 100] [1 4])))
  (is (=
        {[1 2] [100 100 100],
         [1 3] [20 20 20]}
        (draw-pixel sample-image [100 100 100] [1 2]))))

(deftest test-draw-pixels
  (is (=
        {[1 2] [10 10 10],
         [1 3] [20 20 20],
         [0 0] [100 150 200],
         [1 1] [100 150 200],
         [2 2] [100 150 200]}
        (draw-pixels sample-image [100 150 200] (new-line [0 0] [2 2])))))