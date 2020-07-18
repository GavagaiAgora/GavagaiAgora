(ns utils
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [cheshire.core :as json]
            [clojure.java.io :as io]))

(defn get-date-params []
  (let [raw (->> (sh "date" "+%Y-%m-%d")
                 (:out)
                 (str/trim-newline))]
    (->> (str/split raw #"-")
         (zipmap [:y :m :d]))))

(defn get-current-time []
  (->> (sh "date" "+%T")
       (:out)
       (str/trim-newline)))

(defn get-levels [date-params]
  (let [{y :y m :m d :d} date-params]
    ["agora-hub" "contents" y m d]))

(defn make-dir [path]
  (.mkdir (java.io.File. path)))

(defn make-path [levels]
  (apply str (interpose "/" levels)))

(defn list-elements [path]
  (map #(.getPath %)
       (.listFiles (io/file path))))

(defn make-index [path contents]
  (let [index (apply str (interpose "\n" contents))]
    (io/make-parents (str path "/0.index.txt"))
    (spit (str path "/0.index.txt") index)))

(defn sync-index [levels]
  (if (empty? levels)
    (println "Indexes synched")
    (let [current-path (make-path levels)
          contents (sort (list-elements current-path))]
      (make-index current-path contents)
      (recur (drop-last levels)))))
