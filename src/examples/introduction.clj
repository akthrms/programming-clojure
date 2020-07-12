(ns examples.introduction)

(defn blank? [str]
  (every? #(Character/isWhitespace %) str))

(defrecord Person [first-name last-name])

(def foo (->Person "Aaron" "Bedra"))
(:first-name foo)                                           ; => "Aaron"

(defn hello-world [username]
  (println (format "Hello, %s" username)))

(def visitors (atom #{}))

(defn hello
  "Writes hello message to *out*. Calls you by username.
  Knows if you have been here before."
  [username]
  (swap! visitors conj username)
  (str "Hello, " username))

(hello "Rich")                                              ; => "Hello, Rich"
@visitors                                                   ; => #{"Rich"}
