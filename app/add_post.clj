#!/usr/bin/env bb

;; loading libs
(load-file "app/libs/utils.clj") ; utils/

(ns add-post
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [utils :refer [get-date-params
                           get-levels
                           get-current-time
                           make-path
                           sync-index
                           list-elements]]))

(defn sync-post [levels id post]
  (let [path (make-path
              (conj levels (str id ".post.json")))]
    (io/make-parents path)
    (spit path (json/generate-string
                {:text post
                 :time (get-current-time)}))
    (sync-index levels)
    (spit "agora-hub/history.txt" (str path "\n") :append true)))

(defn make-post [post]
  (let [levels (get-levels (get-date-params))
        path (make-path levels)
        post-id (count (list-elements path))]
    (if (zero? post-id)
      (sync-post levels (inc post-id) post)
      (sync-post levels post-id post))))

(defn get-lines [text]
  (let [line (read-line)]
    (if (empty? line)
        (str/trim-newline text)
        (recur (str text line "\n")))))


(defn post! []
    (make-post (get-lines "")))
