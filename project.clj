(defproject org.pinkgorilla/ui-vega "0.0.17-SNAPSHOT"
  :description "Vega renderer and a simple data-driven plotting dsl."
  :url "https://github.com/pink-gorilla/ui-vega"
  :license {:name "MIT"}
  :min-lein-version "2.9.3"
  :min-java-version "1.11"
  :deploy-repositories [["releases" {:url "https://clojars.org/repo"
                                     :username :env/release_username
                                     :password :env/release_password
                                     :sign-releases false}]]
  :release-tasks [["vcs" "assert-committed"]
                  ["bump-version" "release"]
                  ["vcs" "commit" "Release %s"]
                  ["vcs" "tag" "v" "--no-sign"]
                  ["deploy"]
                  ["bump-version"]
                  ["vcs" "commit" "Begin %s"]
                  ["vcs" "push"]]

  :source-paths ["src"]
  :test-paths ["test"]
  :target-path  "target/jar"

  :resource-paths ["resources" ; vega demo data and specs
                   "target/node_modules"] ; vega specs json

  :managed-dependencies [; conflict resolution for notebook
                         [borkdude/sci "0.2.6"]
                         [com.fasterxml.jackson.core/jackson-core "2.12.4"]
                         [cljs-ajax "0.8.3"]]

  :dependencies [[org.clojure/clojure "1.10.3"]
                 [clj-time "0.15.2"] ;time axis creation 
                 [com.andrewmcveigh/cljs-time "0.5.2"]
                 [org.pinkgorilla/pinkie "0.3.11"]] ; box
  
  :profiles {:ci  {:target    :karma
                   :output-to "target/ci.js"}
             :perf {:source-paths ["dev"]
                    :dependencies [[metasoarous/darkstar "0.1.0"]
                                   [cheshire "5.10.0"]
                                   [com.taoensso/tufte "2.1.0"]]}

             :notebook {:dependencies [[org.clojure/clojure "1.10.3"]
                                       [org.pinkgorilla/notebook "0.5.40"]
                                       [org.pinkgorilla/ui-input "0.0.12"]
                                       ]}

             :goldly {:dependencies [[org.clojure/clojure "1.10.3"]
                                     [org.pinkgorilla/goldly "0.3.22"]
                                     [org.pinkgorilla/ui-input "0.0.12"]
                                     ]}

             :dev {:dependencies [[org.pinkgorilla/webly "0.3.45"] ; brings shadow
                                  [clj-kondo "2021.04.23"]]
                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]
                                  [lein-shell "0.5.0"]
                                  [lein-ancient "0.6.15"]
                                  [min-java-version "0.1.0"]]
                   :aliases      {"clj-kondo"
                                  ["run" "-m" "clj-kondo.main"]

                                  "bump-version"
                                  ["change" "version" "leiningen.release/bump-version"]

                                  "lint"            ^{:doc "Lint for dummies"}
                                  ["clj-kondo" "--config" "clj-kondo.edn" "--lint" "src"]

                                  "build-shadow-ci" ^{:doc "Build shadow-cljs ci"}
                                  ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]

                                  "test-run"         ^{:doc "Test compiled JavaScript."}
                                  ["shell" "./node_modules/karma/bin/karma" "start" "--single-run"]

                                  "test-js"          ^{:doc "Compile & Run JavaScript."}
                                  ["do" "build-shadow-ci" ["test-run"]]

                                  "perf"              ^{:doc "performance tests"}
                                  ["with-profile" "perf" "run" "-m" "perf.core"]

                                   ;; APP

                                  :cloverage    {:codecov? true
                                  ;; In case we want to exclude stuff
                                  ;; :ns-exclude-regex [#".*util.instrument"]
                                  ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                                                 }
                   ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
                                  :cljfmt       {:indents {as->                [[:inner 0]]
                                                           with-debug-bindings [[:inner 0]]
                                                           merge-meta          [[:inner 0]]
                                                           try-if-let          [[:block 1]]}}}}}
  :aliases
  {"goldly"
   ["with-profile" "-dev,+goldly" ; dev is excluded because clj-kondo has old sci
    "run" "-m" "goldly-server.app"
    "goldly-plot.edn"
    "watch"]

   "notebook" ; used for ui-demo and npm-install to provide resources
   ["with-profile" "+notebook"
    "run" "-m" "pinkgorilla.notebook-ui.app.app"
    "notebook-plot.edn"]
   
   })
