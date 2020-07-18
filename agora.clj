;; load name-spaces
(load-file "app/add_post.clj")

(ns agora
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [cheshire.core :as json]
            [clojure.java.io :as io]
            [add-post :refer [post!]]))

(post!)
