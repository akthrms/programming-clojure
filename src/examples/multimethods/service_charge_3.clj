(ns examples.multimethods.service-charge-3
  (:require [examples.multimethods.account :refer [account-level]]))

(alias 'acc 'examples.multimethods.account)

(derive ::acc/savings ::acc/account)

(derive ::acc/checking ::acc/account)

(defmulti service-charge (fn [acct] [(account-level acct) (:tag acct)]))

(defmethod service-charge [::acc/basic ::acc/checking] [_] 25)

(defmethod service-charge [::acc/basic ::acc/savings] [_] 10)

(defmethod service-charge [::acc/premium ::acc/account] [_] 0)
