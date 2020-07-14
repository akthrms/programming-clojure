(ns examples.sequences
  (:import (java.io File))
  (:require [clojure.java.io :refer [reader]]
            [clojure.set :refer :all]
            [clojure.string :refer [blank?]]))

(let [m (re-matcher #"\w+" "the quick brown fox")]
  (loop [match (re-find m)]
    (when match
      (println match)
      (recur (re-find m)))))
; the
; quick
; brown
; fox
; => nil

(defn minutes-to-millis [minutes] (* minutes 1000 60))

(defn recently-modified? [file]
  (> (.lastModified file)
     (- (System/currentTimeMillis) (minutes-to-millis 30))))

(filter recently-modified? (file-seq (File. ".")))
; => (#object[java.io.File 0x315eb765 "./.idea"]
;     #object[java.io.File 0x6d1b1059 "./.idea/workspace.xml"]
;     #object[java.io.File 0x43a59034 "./src/examples"]
;     #object[java.io.File 0xcc13fcd "./src/examples/sequences.clj"]
;     #object[java.io.File 0x486054af "./src/examples/primes.clj"])

(defn non-blank? [line] (not (blank? line)))

(defn non-svn? [file] (not (.contains (.toString file) ".svn")))

(defn clojure-source? [file] (.endsWith (.toString file) ".clj"))

(defn clojure-loc [base-file]
  (reduce
    +
    (for [file (file-seq base-file)
          :when (and (clojure-source? file) (non-svn? file))]
      (with-open [rdr (reader base-file)]
        (count (filter non-blank? (line-seq rdr)))))))

(def song {:name   "Agnus Dei"
           :artist "Krzysztof Penderecki"
           :album  "Polish Requiem"
           :genre  "Classical"})

(assoc song :kind "MPEG Audio File")
; => {:name "Agnus Dei",
;     :artist "Krzysztof Penderecki",
;     :album "Polish Requiem",
;     :genre "Classical",
;     :kind "MPEG Audio File"}

(dissoc song :genre)
; => {:name "Agnus Dei",
;     :artist "Krzysztof Penderecki",
;     :album "Polish Requiem"}

(select-keys song [:name :artist])
; => {:name "Agnus Dei",
;     :artist "Krzysztof Penderecki"}

(merge song {:size 8118166 :time 507245})
; => {:name "Agnus Dei",
;     :artist "Krzysztof Penderecki",
;     :album "Polish Requiem",
;     :genre "Classical",
;     :size 8118166,
;     :time 507245}

(def languages #{"java" "c" "d" "clojure"})
(def beverages #{"java" "chai" "pop"})

(union languages beverages)                                 ; => #{"d" "clojure" "pop" "java" "chai" "c"}
(difference languages beverages)                            ; => #{"d" "clojure" "c"}
(intersection languages beverages)                          ; => #{"java"}
(select #(= 1 (count %)) languages)                         ; => #{"d" "c"}

(def compositions #{{:name "The Art of the Fugue" :composer "J. S. Bach"}
                    {:name "Musical Offering" :composer "J. S. Bach"}
                    {:name "Requiem" :composer "Giuseppe Verdi"}
                    {:name "Requiem" :composer "W. A. Mozart"}})

(def composers #{{:composer "J. S. Bach" :country "Germany"}
                 {:composer "W. A. Mozart" :country "Austria"}
                 {:composer "Giuseppe Verdi" :country "Italy"}})

(def nations #{{:nation "Germany" :language "German"}
               {:nation "Austria" :language "German"}
               {:nation "Italy" :language "Italian"}})

(rename compositions {:name :title})
; => #{{:composer "Giuseppe Verdi", :title "Requiem"}
;      {:composer "W. A. Mozart", :title "Requiem"}
;      {:composer "J. S. Bach", :title "The Art of the Fugue"}
;      {:composer "J. S. Bach", :title "Musical Offering"}}

(select #(= (:name %) "Requiem") compositions)
; => #{{:name "Requiem", :composer "Giuseppe Verdi"}
;      {:name "Requiem", :composer "W. A. Mozart"}}

(project compositions [:name])
; => #{{:name "The Art of the Fugue"}
;      {:name "Musical Offering"}
;      {:name "Requiem"}}

(join compositions composers)
; => #{{:composer "W. A. Mozart", :country "Austria", :name "Requiem"}
;      {:composer "J. S. Bach", :country "Germany", :name "Musical Offering"}
;      {:composer "Giuseppe Verdi", :country "Italy", :name "Requiem"}
;      {:composer "J. S. Bach", :country "Germany", :name "The Art of the Fugue"}}

(join composers nations {:country :nation})
; => #{{:composer "W. A. Mozart", :country "Austria", :nation "Austria", :language "German"}
;      {:composer "J. S. Bach", :country "Germany", :nation "Germany", :language "German"}
;      {:composer "Giuseppe Verdi", :country "Italy", :nation "Italy", :language "Italian"}}

(project
  (join
    (select #(= (:name %) "Requiem") compositions)
    composers)
  [:country])                                               ; => #{{:country "Italy"} {:country "Austria"}}
