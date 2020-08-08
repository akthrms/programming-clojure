(ns hangman.specs
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [hangman.core :refer :all]))

; --------------------
; Letter and Word
; --------------------

(s/def ::letter (set letters))

(s/def ::word
  (s/with-gen
    (s/and string? #(pos? (count %)) #(every? valid-letter? (seq %)))
    #(gen/fmap
       (fn [letters] (apply str letters))
       (s/gen (s/coll-of ::letter :min-count 1)))))

; --------------------
; Progress
; --------------------

(s/def ::progress-letter
  (conj (set letters) \_))

(s/def ::progress
  (s/coll-of ::progress-letter :min-count 1))

; --------------------
; Player
; --------------------

(defn- player? [p]
  satisfies? Player p)

(s/def ::player
  (s/with-gen player? #(s/gen #{random-player shuffled-player alpha-player freq-player})))

; --------------------
; Game
; --------------------

(s/def ::verbose
  (s/with-gen boolean? #(s/gen false?)))

(s/def ::score pos-int?)

; --------------------
; Functions
; --------------------

(defn- letters-left [progress]
  (->> progress (keep #{\_}) count))

(s/fdef new-progress
        :args (s/cat :word ::word)
        :ret ::progress
        :fn (fn [{:keys [args ret]}]
              (= (count (:word args)) (count ret) (letters-left ret))))

(s/fdef update-progress
        :args (s/cat :progress ::progress
                     :word ::word
                     :guess ::letter)
        :ret ::progress
        :fn (fn [{:keys [args ret]}]
              (>= (-> args :progress letters-left) (-> ret letters-left))))

(s/fdef complete?
        :args (s/cat :progress ::progress
                     :word ::word)
        :ret boolean?)

(s/fdef game
        :args (s/cat :word ::word
                     :player ::player
                     :opts (s/keys* :opt-un [::verbose]))
        :ret ::score)
