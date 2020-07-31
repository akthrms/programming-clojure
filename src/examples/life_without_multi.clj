(ns examples.life-without-multi)

(defn my-print [ob]
  (.write *out* ob))

(defn my-println [ob]
  (my-print ob)
  (.write *out* "\n"))

(my-println "hello")
; hello
; => nil

; (my-println nil)
; Execution error (NullPointerException) at java.io.PrintWriter/write (PrintWriter.java:443).

(defn my-print' [ob]
  (cond
    (nil? ob) (.write *out* "nil")
    (string? ob) (.write *out* ob)))

(defn my-println' [ob]
  (my-print' ob)
  (.write *out* "\n"))

(my-println' nil)
; nil
; => nil

(defn my-print-vector [ob]
  (.write *out* "[")
  (.write *out* (clojure.string/join " " ob))
  (.write *out* "]"))

(defn my-print'' [ob]
  (cond
    (vector? ob) (my-print-vector ob)
    (nil? ob) (.write *out* "nil")
    (string? ob) (.write *out* ob)))

(defn my-println'' [ob]
  (my-print'' ob)
  (.write *out* "\n"))

(my-println'' [1 2 3])
; [1 2 3]
; => nil
