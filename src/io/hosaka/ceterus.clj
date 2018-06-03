(ns io.hosaka.ceterus
  (:require [config.core :refer [env]]
            [com.stuartsierra.component :as component]
            [manifold.deferred :as d]
            [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as nrepl]
            [io.hosaka.ceterus.handler :refer [new-handler]]
            [io.hosaka.ceterus.orchestrator :refer [new-orchestrator]]
            [io.hosaka.common.keychain :refer [new-keychain]]
            [io.hosaka.common.db.health :refer [new-health]]
            [io.hosaka.common.db :refer [new-database]]
            [io.hosaka.common.server :refer [new-server]] )
  (:gen-class))


(defn init-system [env]
  (component/system-map
   :db (new-database "ceterus" env)
   :keychain (new-keychain env)
   :orchestrator (new-orchestrator)
   :handler (new-handler)
   :server (new-server env)
   :health (new-health env)
   ))

(defn get-port [port]
  (cond
    (string? port) (try (Integer/parseInt port)
                        (catch Exception e nil))
    (integer? port) port
    :else nil))

(defonce system (atom {}))

(defonce repl (atom nil))

(defn -main [& args]
  (let [semaphore (d/deferred)]
    (reset! system (init-system env))

    (swap! system component/start)
    (reset! repl (if-let [nrepl-port (get-port (:nrepl-port env))] (nrepl/start-server :port nrepl-port) nil))
    (log/info "Ceterus booted")
    (deref semaphore)
    (log/info "Ceterus going down")
    (component/stop @system)
    (swap! repl (fn [server] (do (if server (nrepl/stop-server server)) nil)))

    (shutdown-agents)
    ))

(comment
  (require '[clj-crypto.core :as crypto])
  (map #(-> % second  :bytes crypto/encode-base64-as-str)
       (crypto/get-key-pair-map
        (crypto/generate-key-pair :key-size 256 :algorithm "ECDSA")))



  )
