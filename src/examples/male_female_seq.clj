(ns examples.male-female-seq
  (:require [examples.memoized-male-female :refer [memoized-m memoized-f]]))

(defn m-seq [] (map memoized-m (iterate inc 0)))

(defn f-seq [] (map memoized-f (iterate inc 0)))
