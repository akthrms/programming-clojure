(ns examples.male-female)

(declare m f)

(defn m [n]
  (if (zero? n)
    0
    (- n (f (m (dec n))))))

(defn f [n]
  (if (zero? n)
    0
    (- n (m (f (dec n))))))
