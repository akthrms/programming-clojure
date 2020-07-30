(ns examples.macros.bench-1)

(defmacro bench [expr]
  `(let [start (System/nanoTime)
         result ~expr]
     {:result result :elapsed (- (System/nanoTime) start)}))

; (bench (str "a" "b"))

(macroexpand-1 '(bench (str "a" "b")))
; =>
; (clojure.core/let [examples.macros.bench-1/start (java.lang.System/nanoTime)
;                    examples.macros.bench-1/result (str "a" "b")]
;   {:result examples.macros.bench-1/result,
;    :elapsed (clojure.core/- (java.lang.System/nanoTime) examples.macros.bench-1/start)})

(defmacro bench' [expr]
  `(let [start# (System/nanoTime)
         result# ~expr]
     {:result result# :elapsed (- (System/nanoTime) start#)}))

(bench' (str "a" "b"))
; => {:result "ab", :elapsed 65770}

(macroexpand-1 '(bench' (str "a" "b")))
; =>
; (clojure.core/let [start__2027__auto__ (java.lang.System/nanoTime)
;                    result__2028__auto__ (str "a" "b")]
;   {:result  result__2028__auto__,
;    :elapsed (clojure.core/- (java.lang.System/nanoTime) start__2027__auto__)})
