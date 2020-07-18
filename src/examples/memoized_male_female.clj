(ns examples.memoized-male-female)

(declare m f)

(defn m [n]
  (if (zero? n)
    0
    (- n (f (m (dec n))))))

(defn f [n]
  (if (zero? n)
    1
    (- n (m (f (dec n))))))

(def memoized-m (memoize m))

(def memoized-f (memoize f))
