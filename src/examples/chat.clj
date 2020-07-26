(ns examples.chat)

(defrecord Message [sender text])

(def messages (ref ()))

(defn add-message [msg]
  (dosync (alter messages conj msg)))

(add-message (->Message "user 1" "hello"))                  ; => (#examples.chat.Message{:sender "user 1", :text "hello"})
(add-message (->Message "user 2" "howdy"))                  ; => (#examples.chat.Message{:sender "user 1", :text "hello"} #examples.chat.Message{:sender "user 2", :text "howdy"})

(defn add-message-commute [msg]
  (dosync (commute messages conj msg)))

(defn valid-message? [msg]
  (and (:sender msg) (:text msg)))

(def validate-message-list #(every? valid-message? %))

(def messages (ref () :validator validate-message-list))

(add-message "not a valid message")                         ; => Syntax error (IllegalStateException) compiling at (src/examples/chat.clj:23:1). Invalid reference state
@messages                                                   ; => ()
