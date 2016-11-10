(ns casual-drawing.shapes-test
  (:require [clojure.test :refer :all]
            [casual-drawing.core :refer :all]
            [casual-drawing.shapes :refer :all]))

(deftest test-new-line
  (is (=
        [[1 1] [2 2] [3 3] [4 4] [5 5]]
        (new-line [1 1] [5 5]))))

(deftest test-new-circle
  (is (=
        [[15 10] [15 11] [15 12] [14 13] [10 15] [11 15] [12 15] [13 14] [10 15] [9 15] [8 15] [7 14] [5 10] [5 11] [5 12] [6 13] [5 10] [5 9] [5 8] [6 7] [10 5] [9 5] [8 5] [7 6] [10 5] [11 5] [12 5] [13 6] [15 10] [15 9] [15 8] [14 7]]
        (new-circle [10 10] 5))))

(deftest test-dot
  (is (=
        [[0 0] [2 2] [4 4] [6 6] [8 8] [10 10]]
        (dot (new-line [0 0] [10 10])))))

(deftest test-dash
  (is (=
        [[1 1] [2 2] [3 3] [4 4] [5 5] [6 6] [7 7] [8 8] [9 9]]
        (dash (new-line [0 0] [10 10])))))