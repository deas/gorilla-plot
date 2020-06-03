 (ns perf.render
  (:require 
   [reagent.core :as r]
   [reagent.dom.server :refer [render-to-string]]))

; stolen from:
; https://github.com/reagent-project/reagent-cookbook/blob/master/recipes/reagent-server-rendering/src/clj/reagent_server_rendering/handler.clj

(defn about-page []
  [:div [:h2 "About reagent-server-rendering"]
   [:div [:a {:href "/"} "go to the home page"]]])

(def pages 
  {1 about-page})

(defn ^:export render-page [page-id]
  (render-to-string [(get pages page-id)]))
