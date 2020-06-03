(ns perf.nashorn
  (:require
   [clojure.java.io :as io])
  (:import
   [javax.script ScriptEngineManager Invocable]))

(defn create-js-engine []
  (doto (.getEngineByName (ScriptEngineManager.) "nashorn")
    ; React requires either "window" or "global" to be defined.
    (.eval "var global = this")
    (.eval (-> "target/main.js"
               io/file ; resource
               io/reader))))



(defn render-page [js-engine page-id]
  (println "rendering: " page-id)
  (try (.invokeMethod
        ^Invocable js-engine
        (.eval js-engine "perf.render")
        "render_page" (object-array [page-id]))))