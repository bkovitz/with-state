(defproject farg/with-state "0.0.1-SNAPSHOT"
  :description
    "The with-state macro: when '->' isn't good enough."
  :url "https://github.com/bkovitz/with-state"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [farg/pmatch "0.1.0-SNAPSHOT"]]
  :deploy-repositories [["releases" :clojars]])
