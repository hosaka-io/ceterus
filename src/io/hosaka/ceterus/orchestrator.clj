(ns io.hosaka.ceterus.orchestrator
  (:require [com.stuartsierra.component :as component]
            [io.hosaka.ceterus.db.service-key-values :as skv]))

(defrecord Orchestrator [db]
  component/Lifecycle

  (start [this]
    this)

  (stop [this]
    this))

(defn new-orchestrator []
  (component/using
   (map->Orchestrator {})
   [:db]))

(defn get-service-key-values [{:keys [db]} service]
  (skv/get-service-key-values db service))
