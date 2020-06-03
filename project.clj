(defproject org.pinkgorilla/gorilla-plot "0.9.11-SNAPSHOT"
  :description "A simple data-driven plotting library using Gorilla UI."
  :url "https://github.com/pink-gorilla/gorilla-plot"
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

    ;; TODO: prep tasks breaks alias???
  ;; :prep-tasks ["build-shadow-ci"]

  :source-paths ["src"]
  :test-paths ["test"]

  :dependencies   [[org.clojure/clojure "1.10.1"]
                   [clj-time "0.14.3"] ;time axis creation 
                   [com.andrewmcveigh/cljs-time "0.5.2"]]

  :pinkgorilla {:runtime-config "./notebooks/config.edn"}

  :profiles {:ci  {:source-paths ["dev"]}

             :perf {:source-paths ["dev"]
                    :dependencies [[thheller/shadow-cljs "2.8.80"]
                                   [thheller/shadow-cljsjs "0.0.21"]
                                    [reagent "0.10.0"]
                                   [metasoarous/darkstar "0.1.0"]
                                   [cheshire "5.10.0"]
                                   [com.taoensso/tufte "2.1.0"]]}

             :dev {:dependencies [[thheller/shadow-cljs "2.8.80"]
                                  [thheller/shadow-cljsjs "0.0.21"]
                                  [clj-kondo "2019.11.23"]]
                   :plugins      [[lein-cljfmt "0.6.6"]
                                  [lein-cloverage "1.1.2"]
                                  [lein-shell "0.5.0"]
                                  [lein-ancient "0.6.15"]
                                  [min-java-version "0.1.0"]
                                  [org.pinkgorilla/lein-pinkgorilla "0.0.13"]]
                   :aliases      {"clj-kondo" ["run" "-m" "clj-kondo.main"]}
                   :cloverage    {:codecov? true
                                  ;; In case we want to exclude stuff
                                  ;; :ns-exclude-regex [#".*util.instrument"]
                                  ;; :test-ns-regex [#"^((?!debug-integration-test).)*$$"]
                                  }
                   ;; TODO : Make cljfmt really nice : https://devhub.io/repos/bbatsov-cljfmt
                   :cljfmt       {:indents {as->                [[:inner 0]]
                                            with-debug-bindings [[:inner 0]]
                                            merge-meta          [[:inner 0]]
                                            try-if-let          [[:block 1]]}}}}



  :aliases {"bump-version"
            ["change" "version" "leiningen.release/bump-version"]

            "lint"            ^{:doc "Lint for dummies"}
            ["clj-kondo" "--lint" "src"]

            "build-shadow-ci" ^{:doc "Build shadow-cljs ci"}
            ["run" "-m" "shadow.cljs.devtools.cli" "compile" ":ci"]

            "test-run"         ^{:doc "Test compiled JavaScript."}
            ["shell" "./node_modules/karma/bin/karma" "start" "--single-run"]

            "test-js"          ^{:doc "Compile & Run JavaScript."}
            ["do" "build-shadow-ci" ["test-run"]]

            "build-shadow-perf" ^{:doc "Build shadow-cljs perf js bundle"}
            ["with-profile" "perf" "run" "-m" "shadow.cljs.devtools.cli" "compile" ":perf"]

            "perf"              ^{:doc "performance tests"}
            ["with-profile" "perf" "run" "-m" "perf.core"]})
