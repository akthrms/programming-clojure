(ns examples.functional)

(defn stack-consume-fibo [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (stack-consume-fibo (- n 1)) (stack-consume-fibo (- n 2)))))

(defn tail-fibo [n]
  (letfn [(fib [current next n]
            (if (zero? n)
              current
              (fib next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn recur-fibo [n]
  (letfn [(fib [current next n]
            (if (zero? n)
              current
              (recur next (+ current next) (dec n))))]
    (fib 0N 1N n)))

(defn lazy-seq-fibo
  ([]
   (concat [0 1] (lazy-seq-fibo 0N 1N)))
  ([a b]
   (let [n (+ a b)]
     (lazy-seq (cons n (lazy-seq-fibo b n))))))

(defn fibo []
  (map first (iterate (fn [[a b]] [b (+ a b)]) [0N 1N])))

(def lots-of-fibs (take 1000000000 (fibo)))

(def head-fibo (lazy-cat [0N 1N] (map + head-fibo (rest head-fibo))))

(defn count-heads-pairs [coll]
  (loop [cnt 0
         coll coll]
    (if (empty? coll)
      cnt
      (recur (if (= :h (first coll) (second coll))
               (inc cnt)
               cnt)
             (rest coll)))))

(defn by-pairs [coll]
  (let [take-pair (fn [c] (when (next c) (take 2 c)))]
    (lazy-seq
      (when-let [pair (seq (take-pair coll))]
        (cons pair (by-pairs (rest coll)))))))

(defn count-heads-pairs-2 [coll]
  (count (filter (fn [pair] (every? #(= :h %) pair))
                 (by-pairs coll))))

(def ^{:doc "Count items matching a filter"}
  count-if (comp count filter))

(defn count-runs
  "Count runs of length n where pred is true in coll."
  [n pred coll]
  (count-if #(every? pred %) (partition n 1 coll)))

(count-runs 2 #(= % :h) [:h :t :t :h :h :h])                ; => 2
(count-runs 3 #(= % :h) [:h :t :t :h :h :h])                ; => 1

(def ^{:doc "Count runs of length two that are both heads"}
  count-heads-pairs-3 (partial count-runs 2 #(= % :h)))

(declare my-odd? my-even?)

(defn my-odd? [n]
  (if (= n 0)
    false
    (my-even? (dec n))))

(defn my-even? [n]
  (if (= n 0)
    true
    (my-odd? (dec n))))

(defn parity [n]
  (loop [n n
         par 0]
    (if (= n 0)
      par
      (recur (dec n) (- 1 par)))))

(map parity (range 10))                                     ; => (0 1 0 1 0 1 0 1 0 1)

(defn my-odd-2? [n] (= 1 (parity n)))

(defn my-even-2? [n] (= 0 (parity n)))
