(ns maze.core-test
  (:require [clojure.test :refer :all]
            [maze.core :refer :all]))

(def sample-maze {[8 8] \#, [7 6] \#, [8 7] \#, [9 8] \space, [7 1] \space, [8 9] \space, [10 5] \#, [4 3] \space, [2 2] \#, [0 0] \#, [3 9] \space, [7 7] \space, [2 8] \#, [1 0] \#, [8 4] \#, [2 3] \#, [2 5] \#, [7 2] \space, [6 7] \space, [7 4] \#, [8 3] \space, [0 6] \#, [3 3] \space, [10 9] \#, [5 4] \space, [10 8] \#, [5 10] \#, [1 1] \space, [6 3] \#, [0 5] \#, [3 4] \space, [7 3] \space, [8 6] \#, [4 2] \#, [7 8] \space, [3 0] \#, [9 0] \#, [6 6] \#, [9 6] \space, [1 9] \space, [8 10] \#, [5 3] \space, [9 9] \space, [9 3] \space, [4 7] \#, [4 10] \#, [4 9] \#, [1 10] \O, [2 9] \#, [6 5] \space, [0 9] \#, [8 0] \#, [4 1] \space, [5 2] \#, [4 6] \#, [1 4] \space, [10 2] \#, [5 7] \space, [8 2] \#, [10 7] \#, [10 0] \#, [1 3] \space, [4 8] \#, [1 5] \space, [1 8] \space, [1 7] \space, [6 4] \#, [8 1] \space, [0 3] \#, [5 1] \space, [6 1] \space, [5 6] \#, [5 8] \space, [8 5] \space, [0 7] \#, [6 8] \#, [10 1] \#, [5 5] \space, [7 9] \space, [2 7] \#, [5 9] \space, [2 4] \#, [3 6] \space, [7 10] \#, [10 6] \#, [9 2] \#, [4 5] \#, [9 1] \space, [9 7] \space, [10 4] \#, [10 10] \#, [7 0] \#, [0 2] \#, [6 9] \#, [2 0] \#, [0 4] \#, [0 10] \#, [3 1] \space, [3 10] \X, [2 1] \space, [9 5] \space, [6 10] \#, [3 8] \space, [9 4] \space, [1 6] \space, [4 4] \#, [3 7] \space, [2 10] \#, [7 5] \space, [2 6] \#, [5 0] \#, [6 2] \#, [9 10] \#, [6 0] \#, [1 2] \space, [10 3] \#, [3 5] \space, [0 8] \#, [3 2] \#, [0 1] \#, [4 0] \#})

(deftest test-maze-stuff
  (is (= (end-coord sample-maze) [3 10]))
  (is (= (start-coord sample-maze) [1 10]))
  (is (traversable? sample-maze [9 8]))
  (is (not (traversable? sample-maze [8 8]))))

(deftest test-neighbours
  (is (= (neighbours sample-maze [7 7]) [[7 8] [6 7]])))

(deftest test-keep?
  (is (= (keep? { [1 10] 0} [1 10] [1 9]) true))
  (is (= (keep? { [1 10] 0 [1 9] 2} [1 10] [1 9]) true))
  (is (= (keep? { [1 10] 0 [1 9] 0} [1 10] [1 9]) false)))

(deftest test-neighbours-to-keep
  (is (= (neighbours-to-keep
           { [1 10] 0 [1 9] 1}
           [1 9]
           (end-coord sample-maze)
           (neighbours sample-maze [1 9]))
         {[1 8] {:cost 2, :f 6, :parent [1 9]}})))