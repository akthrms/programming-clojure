(ns examples.protocols
  (:import [java.net Socket
                     URL]
           [java.io BufferedReader
                    BufferedWriter
                    File
                    FileInputStream
                    FileOutputStream
                    InputStream
                    InputStreamReader
                    OutputStream
                    OutputStreamWriter]))

(defn make-reader [src]
  (-> (condp = (type src)
        InputStream src
        String (FileInputStream. src)
        File (FileInputStream. src)
        Socket (.getInputStream src)
        URL (if (= "file" (.getProtocol src))
              (-> src .getPath FileInputStream.)
              (.openStream src)))
      InputStreamReader.
      BufferedReader.))

(defn make-writer [dst]
  (-> (condp = (type dst)
        OutputStream dst
        String (FileOutputStream. dst)
        File (FileOutputStream. dst)
        Socket (.getOutputStream dst)
        URL (if (= "file" (.getProtocol dst))
              (-> dst .getPath FileOutputStream.)
              (throw (IllegalArgumentException. "Can't write to non-file URL"))))
      OutputStreamWriter.
      BufferedWriter.))

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

(defprotocol IOFactory
  "A protocol for things that can be read from and written to."
  (make-reader [this] "Creates a BufferedReader.")
  (make-writer [this] "Creates a BufferedWriter."))

(extend InputStream
  IOFactory
  {:make-reader (fn [src]
                  (-> src InputStreamReader. BufferedReader.))
   :make-writer (fn [_]
                  (throw (IllegalArgumentException. "Can't open as an InputStream.")))})

(extend OutputStream
  IOFactory
  {:make-reader (fn [_]
                  (throw (IllegalArgumentException. "Can't open as an OutputStream.")))
   :make-writer (fn [dst]
                  (-> dst OutputStreamWriter. BufferedWriter.))})

(extend-type File
  IOFactory
  (make-reader [src]
    (make-reader (FileInputStream. src)))
  (make-writer [dst]
    (make-writer (FileOutputStream. dst))))

(extend-protocol IOFactory
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
