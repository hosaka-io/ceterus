(ns io.hosaka.ceterus
  (:require [config.core :refer [env]]
            [com.stuartsierra.component :as component]
            [manifold.deferred :as d]
            [clojure.tools.logging :as log]
            [io.hosaka.ceterus.handler :refer [new-handler]]
            [io.hosaka.ceterus.orchestrator :refer [new-orchestrator]]
            [io.hosaka.common.db.health :refer [new-health]]
            [io.hosaka.common.db :refer [new-database]]
            [io.hosaka.common.server :refer [new-server]] )
  (:gen-class))

(defonce system (atom {}))

(defn init-system [env]
  (component/system-map
   :db (new-database "ceterus" env)
   :orchestrator (new-orchestrator)
   :handler (new-handler)
   :server (new-server env)
   :health (new-health)
   ))

(defn -main [& args]
  (let [semaphore (d/deferred)]
    (reset! system (init-system env))

    (swap! system component/start)
    (log/info "Ceterus booted")
    (deref semaphore)
    (log/info "Ceterus going down")
    (component/stop @system)

    (shutdown-agents)
    ))
