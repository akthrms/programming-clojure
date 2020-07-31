(ns examples.multimethods.account)

(alias 'acc 'examples.multimethods.account)

(def test-saving {:id 1 :tag ::acc/savings :balance 100M})

(def test-checking {:id 2 :tag ::acc/checking :balance 250M})

(defmulti interest-rate :tag)

(defmethod interest-rate ::acc/checking [_] 0M)

(defmethod interest-rate ::acc/savings [_] 0.05M)

(interest-rate test-saving)
; => 0.05M
(interest-rate test-checking)
; => 0M

(defmulti account-level :tag)

(defmethod account-level ::acc/checking [acct]
  (if (>= (:balance acct) 5000) ::acc/premium ::acc/basic))

(defmethod account-level ::acc/savings [acct]
  (if (>= (:balance acct) 1000) ::acc/premium ::acc/basic))

(account-level {:id 1 :tag ::acc/savings :balance 2000M})
; => :examples.multimethods.account/premium
(account-level {:id 2 :tag ::acc/checking :balance 2000M})
; => :examples.multimethods.account/basic
