(ns examples.macros.chain-3)

(defmacro chain [x form]
  `(. ~x ~form))

(macroexpand '(chain arm getHand))
; => (. arm getHand)
