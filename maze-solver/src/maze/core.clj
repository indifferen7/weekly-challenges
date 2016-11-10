(ns maze.core
  (:gen-class)
  (:require [clojure.string :as str]))

(defn new-maze-map
  "Parses the input file and creates
  a map with tile coordinates as keys."
  [path-to-file]
  (let [lines (into [] (str/split-lines (slurp path-to-file)))]
    (into {}
          (for [y (range (count lines))
                x (range (count (get lines y)))]
            [[x y] (get (get lines y) x)]))))

(defn start-coord
  "Extracts the start coordinate in the maze."
  [maze]
  (first (filter #(= \O (get maze %)) (keys maze))))

(defn end-coord
  "Extracts the end coordinate in the maze."
  [maze]
  (first (filter #(= \X (get maze %)) (keys maze))))

(defn traversable?
  "Checks if the coord is traversable, i.e. a space or
  the end character X."
  [maze coord]
  (or
    (= \space (get maze coord))
    (= \X (get maze coord))))

(defn coord-in-direction
  "Finds the coordinate relative to the provided direction."
  [[x y] direction]
  (case direction
    :up [x (dec y)]
    :right [(inc x) y]
    :down [x (inc y)]
    :left [(dec x) y]))

(defn neighbours
  "Finds all traversable neighbours to the current
  maze within the maze."
  [maze current]
  (filter #(traversable? maze %) (map (partial coord-in-direction current) [:up :right :down :left])))

(defn manhattan-distance
  "Calculates the manhattan distance between two coords."
  [[start-x start-y] [end-x end-y]]
  (+ (Math/abs (- start-x end-x))
     (Math/abs (- start-y end-y))))

(defn keep?
  "Decides whether or not we should keep the neighbour
  when traversing the maze."
  [costs current neighbour]
  (let [new-cost (inc (get costs current 0))]
    (or
      (not (contains? costs neighbour))
      (< new-cost (get costs neighbour)))))

(defn neighbours-to-keep
  "Finds all neighbours that we need to have a closer
  look at when traversing the maze."
  [costs current end neighbours]
  (into {} (for [neighbour (filter #(keep? costs current %) neighbours)]
             (let [cost (inc (get costs current 0))]
               {neighbour
                {:cost cost
                 :f (+ cost (manhattan-distance neighbour end))
                 :parent current }}))))

(defn build-candidates
  "Builds a vector of maps with coords that we may have
  a look at during the algorithm. The coords are sorted
  by :f according to the path score in A* determined by
  the formula F = G + H. This code could probably be
  refactored to something nicer by someone with awesome
  clojure skills.

  The result format looks like [{:f 3 :coord [1 1]} ... ]"
  [neighbours candidates]
  (let [new-coords (map #(into {} {:f (:f (get neighbours %)) :coord %}) (keys neighbours))]
    (sort-by :f (into candidates new-coords))))

(defn build-costs
  "Calculates the cost (e.g. G in the path score formula)
  for coordinates. The result format looks like this:
  {[1 1] 4, [1 2] 5, ... }"
  [costs neighbours]
  (into costs (map #(into {} {% (:cost (get neighbours %))}) (keys neighbours))))

(defn build-paths
  "Assembles the paths that we have ventured along the
  way to success in a map, where key is the coord and
  value is it's parent, e.g. {[1 2] [1 1], ...}"
  [paths neighbours current]
  (into paths (map #(into {} {% current}) (keys neighbours))))

(defn extract-solution
  "Extracts the solution within paths by walking the
  path backwards, starting from end."
  [paths end]
  (loop [current end
         result []]
    (let [next (get paths current)]
      (if (nil? next)
        (reverse (into [end] result))
        (recur next (conj result next))))))

(defn direction-string
  "Translates the movement between two coords into a
  string indicating its direction."
  [prev current]
  (if (> (first current) (first prev))
    "east"
    (if (< (first current) (first prev))
      "west"
      (if (< (second current) (second prev))
        "north"
        "south"))))

(defn as-directions
  "Translates the movements in the provided path into
  a vector of strings, indicating the way between start
  and end."
  [path]
  (loop [prev (first path)
         remaining (rest path)
         result []]
    (let [current (first remaining)]
      (if (nil? current)
        result
        (recur current (rest remaining) (conj result (direction-string prev current)))))))

(defn as-maze
  "Translates the solution into a sequence of strings,
  where the shortest path between start and end has been
  marked out."
  [maze start end solution]
  (let [plotted-unsorted-maze (into maze (dissoc (zipmap solution (repeat \*)) start end))
        sorted-maze (into (sorted-map) plotted-unsorted-maze)
        num-rows (inc (last (key (last sorted-maze))))]
    (map str/join (apply map list (partition num-rows (vals sorted-maze))))))

(defn print-solution
  "Prints the solution, both as directions and as a maze."
  [maze start end paths]
  (let [solution (extract-solution paths end)]
    (dorun (map println (as-directions solution)))
    (println (count solution) "steps")
    (println "\nThe shortest path looks like this (marked with '*'):")
    (dorun (map println (as-maze maze start end solution)))))

(defn solve-maze
  "This is the function to use to solve the maze. The result of
  the maze (if well formed) will be a series of directions and
  a maze with a marked solution that is just printed out."
  [maze]
  (let [start (start-coord maze)
        end (end-coord maze)]
    (loop [open [{:f 0 :coord start}]
           costs { start 0 }
           paths { start nil }]
      (let [current (:coord (first open))
            neighbours (neighbours-to-keep costs current end (neighbours maze current))]
        (if (= end current)
          (print-solution maze start end paths)
          (recur (build-candidates neighbours (rest open))
                 (build-costs costs neighbours)
                 (build-paths paths neighbours current)))))))

(defn -main
  [path-to-file]
  (solve-maze (new-maze-map path-to-file)))