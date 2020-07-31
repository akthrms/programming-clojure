(ns examples.multimethods)

(defmulti my-print class)

(defmethod my-print String [s]
  (.write *out* s))

(defmethod my-print nil [_]
  (.write *out* "nil"))

(defmethod my-print Number [n]
  (.write *out* (.toString n)))

(defmethod my-print :default [s]
  (.write *out* "#<")
  (.write *out* (.toString s))
  (.write *out* ">"))

(defmethod my-print java.util.Collection [c]
  (.write *out* "(")
  (.write *out* (clojure.string/join " " c))
  (.write *out* ")"))

(defmethod my-print clojure.lang.IPersistentVector [c]
  (.write *out* "[")
  (.write *out* (clojure.string/join " " c))
  (.write *out* "]"))

(prefer-method my-print clojure.lang.IPersistentVector java.util.Collection)

(my-print "stu")
; stu
; => nil

(my-print 42)
; 42
; => nil

(my-print (java.sql.Date. 0))
; #<1970-01-01>
; => nil

(my-print (java.util.Random.))
; #<java.util.Random@7fc89e1>
; => nil

(my-print (take 6 (cycle [1 2 3])))
; (1 2 3 1 2 3)
; => nil

(my-print [1 2 3])
; [1 2 3]
; => nil

(defmulti my-class identity)

(defmethod my-class nil [_] nil)

(defmethod my-class :default [x] (.getClass x))
