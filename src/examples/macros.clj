(ns examples.macros)

(defn unless [expr form]
  (if expr nil form))

(unless false (println "this should print"))
; this should print
; => nil
(unless true (println "this should not print"))
; this should not print
; => nil

(defn unless' [expr form]
  (println "About to test...")
  (if expr nil form))

(unless' false (println "this should print"))
; this should print
; About to test...
; => nil
(unless' true (println "this should not print"))
; this should not print
; About to test...
; => nil

(defmacro unless'' [expr form]
  (list 'if expr nil form))

(unless'' false (println "this should print"))
; this should print
; => nil

(unless'' true (println "this should not print"))
; => nil

(macroexpand-1 '(unless'' false (println "this should print")))
; => (if false nil (println "this should print"))

(defmacro bad-unless [expr form]
  (list 'if 'expr nil form))

(macroexpand-1 '(bad-unless false (println "this should print")))
; => (if expr nil (println "this should print"))

; (bad-unless false (println "this should print"))
; => Unable to resolve symbol: expr in this context

(macroexpand '(and 1 2 3))
; => (let* [and__5514__auto__ 1] (if and__5514__auto__ (clojure.core/and 2 3) and__5514__auto__))
