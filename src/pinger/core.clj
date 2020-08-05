(ns pinger.core
  (:import [java.net URL HttpURLConnection])
  (:require [pinger.scheduler :as scheduler]))

(defn response-code [address]
  (let [conn ^HttpURLConnection (.openConnection (URL. address))
        code (.getResponseCode conn)]
    (when (< code 400)
      (-> conn .getInputStream .close))
    code))

(defn available? [address]
  (= 200 (response-code address)))

(available? "https://www.google.co.jp/")
; => true
(available? "https://www.google.co.jp/badurl")
; => false

(defn check []
  (let [addresses ["https://www.google.co.jp/"
                   "https://clojure.org/"
                   "https://www.google.co.jp/badurl"]]
    (doseq [address addresses]
      (println address ":" (available? address)))))

(def immediately 0)

(def every-minute (* 60 1000))

(defn start [e]
  "REPL helper. Start pinger on executor e."
  (scheduler/periodically e check immediately every-minute))

(defn stop [e]
  "REPL helper. Stop executor e."
  (scheduler/shutdown-executor e))

(defn -main []
  (start (scheduler/scheduled-executor 1)))
