(ns examples.exploring
  (:require [clojure.string :as str]))

(defn greeting
  "Returns a greeting of the form 'Hello, username'
  Default username is 'world'."
  ([] (greeting "world"))
  ([username] (str "Hello, " username)))

(greeting "world")                                          ; => "Hello, world"
(greeting)                                                  ; => "Hello, world"

(defn date [person-1 person-2 & chaperones]
  (println person-1 "and" person-2 "went out with" (count chaperones) "chaperones."))

(date "Romeo" "Juliet" "Friar Lawrence" "Nurse")
; "Romeo and Juliet went out with 2 chaperones."
; => nil

(defn indexable-word? [word]
  (> (count word) 2))

(filter indexable-word? (str/split "A fine day it is" #"\W+")) ; => ("fine" "day")
(filter (fn [w] (> (count w) > 2)) (str/split "A fine day it is" #"\W+"))
(filter #(> (count %) 2) (str/split "A fine day it is" #"\W+"))

(defn indexable-words [text]
  (let [indexable-word? (fn [w] (> (count w) > 2))]
    (filter indexable-word? (str/split text #"\W+"))))

(indexable-words "A fine day it is")                        ; => ("fine" "day")

(defn make-greeter [greeting-prefix]
  (fn [username] (str greeting-prefix ", " username)))

(def hello-greeting (make-greeter "Hello"))
(def aloha-greeting (make-greeter "Aloha"))

(defn square-corners [bottom left size]
  (let [top (+ bottom size)
        right (+ left size)]
    [[bottom left] [top left] [top right] [bottom right]]))

(defn greet-author-1 [author]
  (println "Hello, " (:first-name author)))

(defn greet-author-2 [{first-name :first-name}]
  (println "Hello, " first-name))

(defn ellipsize [words]
  (let [[w1 w2 w3] (str/split words #"\s+")]
    (str/join " " [w1 w2 w3 "..."])))

(ellipsize "The quick brown fox jumps over the lazy dog.")  ; => "The quick brown ..."

(defn small? [number]
  (if (< number 100)
    "yes"
    (do
      (println "Saw a big number" number)
      "no")))

(small? 50)                                                 ; => "yes"
(small? 50000)
; "Saw a big number 50000"
; => "no"

(loop [result []
       x 5]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))                       ; => [5 4 3 2 1]

(defn countdown [result x]
  (if (zero? x)
    result
    (recur (conj result x) (dec x))))

(countdown [] 5)                                            ; => [5 4 3 2 1]

(into [] (take 5 (iterate dec 5)))
(into [] (drop-last (reverse (range 6))))
(vec (reverse (rest (range 6))))

(defn indexed [coll] (map-indexed vector coll))             ; => ([0 \a] [1 \b] [2 \c] [3 \d] [4 \e])

(defn index-filter [pred coll]
  (when pred
    (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

(index-filter #{\a \b} "abcdbbb")                           ; => (0 1 4 5 6)
(index-filter #{\a \b} "xyz")                               ; => ()
