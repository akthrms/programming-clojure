(ns examples.expectorate
  (:import [java.io BufferedWriter
                    FileOutputStream
                    OutputStreamWriter]))

(defn expectorate [dst content]
  (with-open [writer (-> dst
                         FileOutputStream.
                         OutputStreamWriter.
                         BufferedWriter.)]
    (.write writer (str content))))
