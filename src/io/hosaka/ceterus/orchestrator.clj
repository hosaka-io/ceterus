(ns io.hosaka.ceterus.orchestrator
  (:require [com.stuartsierra.component :as component]
            [io.hosaka.ceterus.db.service-key-values :as skv]
            [io.hosaka.common.keychain :as keychain]
            [clojure.string :as s]
            [manifold.deferred :as d]))

(defrecord Orchestrator [db keychain]
  component/Lifecycle

  (start [this]
    this)

  (stop [this]
    this))

(defn new-orchestrator []
  (component/using
   (map->Orchestrator {})
   [:db :keychain]))

(defn get-service-key-values [{:keys [db keychain]} service jwt]
  (d/chain
   (d/zip
    (keychain/validate keychain jwt)
    (skv/get-service-key-values db service))
   (fn [[{:keys [sub]} v]]
     (if (= sub service)
       v
       (throw (Exception. "Token invalid for service"))))
   (fn [v] (assoc
            (apply hash-map
                   (reduce concat
                           (map
                            #(vector
                              (:k %1)
                              (:val %1)) v)))
            :encrypted
            (s/join ";" (map :k (filter :encrypted v)))))))
