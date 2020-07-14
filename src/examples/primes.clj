(ns examples.primes)

(def primes
  (concat
    [2 3 5 7]
    (lazy-seq
      (let [primes-form
            (fn primes-form [n [f & r]]
              (if (some #(zero? (rem n %))
                        (take-while #(<= (* % %) n) primes))
                (recur (+ n f) r)
                (lazy-seq (cons n (primes-form (+ n f) r)))))
            wheel (cycle [2 4 2 4 6 2 6 4 2 4 6 6 2 6 4 2
                          6 4 6 8 4 2 4 2 4 8 6 4 6 2 4 6
                          2 6 6 4 2 4 6 2 6 4 2 4 2 10 2 10])]
        (primes-form 11 wheel)))))

(def ordinals-and-primes
  (map vector (iterate inc 1) primes))
