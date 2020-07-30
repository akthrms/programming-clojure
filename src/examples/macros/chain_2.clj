(ns examples.macros.chain-2)

(defmacro chain
  ([x form] (list '. x form))
  ([x form & more] (concat (list 'chain (list '. x form)) more)))

(macroexpand '(chain arm getHand))
; => (. arm getHand)
(macroexpand '(chain arm getHand getFinger))
; => (. (. arm getHand) getFinger)
