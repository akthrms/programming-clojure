(ns examples.eager)

(defn square [x]
  (* x x))

(defn sum-squares-seq [n]
  (vec (map square (range n))))

(defn sum-squares [n]
  (into [] (map square) (range n)))

(defn preds-seq []
  (->> (all-ns)
       (map ns-publics)
       (mapcat vals)
       (filter #(clojure.string/ends-with? % "?"))
       (map #(str (.-sym %)))
       vec))

(defn preds []
  (into []
        (comp (map ns-publics)
              (mapcat vals)
              (filter #(clojure.string/ends-with? % "?"))
              (map #(str (.-sym %))))
        (all-ns)))

(defn non-blank? [s]
  (not (clojure.string/blank? s)))

(defn non-blank-lines-seq [file-name]
  (let [reader (clojure.java.io/reader file-name)]
    (filter non-blank? (line-seq reader))))

(defn non-blank-lines [file-name]
  (with-open [reader (clojure.java.io/reader file-name)]
    (into [] (filter non-blank?) (line-seq reader))))

(defn non-blank-lines-eduction [reader]
  (eduction (filter non-blank?) (line-seq reader)))

(defn line-count [file-name]
  (with-open [reader (clojure.java.io/reader file-name)]
    (reduce (fn [cnt el] (inc cnt)) 0 (non-blank-lines-eduction reader))))
