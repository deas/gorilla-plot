(ns perf.core
  (:require
   [applied-science.darkstar :as darkstar]
   [taoensso.tufte :as tufte :refer (p profile)]
   ;[cheshire.core :as json]
   #_[pinkgorilla.ui.gorilla-plot.core :refer [list-plot]])
  (:gen-class))

(defn once []
  (let [path "/tmp/"
        ;d [1 3 5 7 9 5 4 6 9 8 3 5 6]
        ;spec (list-plot d)
        ;spec-json (json/generate-string spec)
        spec-json (slurp "notebooks/stacked-bar.vg.json")
        _ (println "spec: " spec-json)]
    (println "vega -> svg performance tests..")

  ;; write an SVG from a Vega spec
    (->> spec-json
         darkstar/vega-spec->svg
         (spit (str path "vg-example.svg")))))

(defn -main []
  ;; We'll request to send `profile` stats to `println`:
  (tufte/add-basic-println-handler! {})
  (once) ; pre jit
  (profile ; Profile any `p` forms called during body execution
   {} ; Profiling options; we'll use the defaults for now
   (dotimes [_ 100]
     (p :vega-render (once))))
  (Thread/sleep 100))