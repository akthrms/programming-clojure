(ns examples.concurrency)

(def counter (ref 0))

(defn next-counter []
  (dosync (alter counter inc)))

(next-counter)                                              ; => 1
(next-counter)                                              ; => 2
