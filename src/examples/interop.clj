(ns examples.interop)

(defn class-available? [class-name]
  (Class/forName class-name))

(defn class-available?' [class-name]
  (try
    (Class/forName class-name)
    (catch ClassNotFoundException _ false)))
