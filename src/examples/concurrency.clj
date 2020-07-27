(ns examples.concurrency
  (:require [examples.chat :refer [messages ->Message]]))

(def counter (ref 0))

(defn next-counter []
  (dosync (alter counter inc)))

(next-counter)                                              ; => 1
(next-counter)                                              ; => 2

(def backup-agent (agent "output/messages-backup.clj"))

(defn add-message-with-backup [msg]
  (dosync
    (let [snapshot (commute messages conj msg)]
      (send-off backup-agent (fn [filename]
                               (spit filename snapshot)
                               filename))
      snapshot)))

(add-message-with-backup (->Message "John" "Message One"))
(add-message-with-backup (->Message "Jane" "Message Two"))
