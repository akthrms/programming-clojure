(ns examples.macros.chain-1)

(defmacro chain [x form]
  (list '. x form))
