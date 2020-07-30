(ns examples.macros.chain-4)

(defmacro chain
  ([x form] `(. ~x ~form))
  ([x form & more] `(chain (. ~x ~form) ~more)))

(macroexpand '(chain arm getHand getFinger))
; => (. (. arm getHand) (getFinger))
