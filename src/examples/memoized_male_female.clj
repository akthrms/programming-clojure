(ns examples.memoized-male-female
  (:require [examples.male-female :refer [m f]]))

(defn memoized-m [] (memoize m))

(defn memoized-f [] (memoize f))
