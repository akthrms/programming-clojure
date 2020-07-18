(ns examples.male-female-seq)

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

(def m-seq (map memoized-m (iterate inc 0)))

(def f-seq (map memoized-f (iterate inc 0)))
