(ns examples.multimethods.service-charge-1
  (:require [examples.multimethods.account :refer [account-level]]))

(alias 'acc 'examples.multimethods.account)

(defmulti service-charge account-level)

(defmethod service-charge ::acc/basic [acct]
  (if (= (:tag acct) ::checking) 25 10))

(defmethod service-charge ::acc/premium [_] 0)
