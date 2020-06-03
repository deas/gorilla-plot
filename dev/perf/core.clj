(ns perf.core
  (:require
   [applied-science.darkstar :as darkstar]
   [taoensso.tufte :as tufte :refer (p profile)]
   [perf.nashorn :refer [create-js-engine render-page]]
   ;[cheshire.core :as json]
   #_[pinkgorilla.ui.gorilla-plot.core :refer [list-plot]])
  (:gen-class))

        ;d [1 3 5 7 9 5 4 6 9 8 3 5 6]
        ;spec (list-plot d)
        ;spec-json (json/generate-string spec)

(def spec-json (slurp "notebooks/stacked-bar.vg.json"))
;(println "spec: " spec-json)

(defn vega->svg-str []
  (->> spec-json
       darkstar/vega-spec->svg))

(defn vega->svg-file []
  (let [path "/tmp/"]
    (->> spec-json
         darkstar/vega-spec->svg
         (spit (str path "vg-example.svg")))))

(defn run-vega-darkstar []
  (vega->svg-str)
  (vega->svg-file) ; pre jit
  (profile ; Profile any `p` forms called during body execution
   {} ; Profiling options; we'll use the defaults for now
   (dotimes [_ 100]
     (p :vega->svg-str (vega->svg-str))
     (p :vega->svg-file (vega->svg-file)))))

(defn run-vega-nashorn []
  (println "nashorn / vega -> svg performance tests..")
  (let [js-engine (create-js-engine)]
    (println render-page js-engine 1)))

(defn -main []
  (println "vega -> svg performance tests..")
  ;; We'll request to send `profile` stats to `println`:
  (tufte/add-basic-println-handler! {})
  (run-vega-nashorn)
  ;(run-vega-darkstar)
  (Thread/sleep 100))