(ns examples.io
  (:import [java.net Socket URL]
           [java.io BufferedReader
                    BufferedWriter
                    File
                    FileInputStream
                    FileOutputStream
                    InputStream
                    InputStreamReader
                    OutputStream
                    OutputStreamWriter]))

(defprotocol IOFactory
  "A protocol for things that can be read from and written to."
  (make-reader [this] "Creates a BufferedReader.")
  (make-writer [this] "Creates a BufferedWriter."))

(defn gulp [src]
  (let [sb (StringBuilder.)]
    (with-open [reader (make-reader src)]
      (loop [c (.read reader)]
        (if (neg? c)
          (str sb)
          (do
            (.append sb (char c))
            (recur (.read reader))))))))

(defn expectorate [dst content]
  (with-open [writer (make-writer dst)]
    (.write writer (str content))))

(extend-protocol IOFactory
  InputStream
  (make-reader [src]
    (-> src InputStreamReader. BufferedReader.))
  (make-writer [_]
    (throw (IllegalArgumentException. "Can't open as an InputStream.")))

  OutputStream
  (make-reader [_]
    (throw (IllegalArgumentException. "Can't open as an OutputStream.")))
  (make-writer [dst]
    (-> dst OutputStreamWriter. BufferedWriter.))

  File
  (make-reader [src]
    (make-reader (FileInputStream. src)))
  (make-writer [dst]
    (make-writer (FileOutputStream. dst)))

  Socket
  (make-reader [src]
    (make-reader (.getInputStream src)))
  (make-writer [dst]
    (make-writer (.getOutputStream dst)))

  URL
  (make-reader [src]
    (make-reader
      (if (= "file" (.getProtocol src))
        (-> src .getPath FileInputStream.)
        (.openStream src))))
  (make-writer [dst]
    (make-writer
      (if (= "file" (.getProtocol dst))
        (-> dst .getPath FileOutputStream.)
        (throw (IllegalArgumentException. "Can't write to non-file URL"))))))
