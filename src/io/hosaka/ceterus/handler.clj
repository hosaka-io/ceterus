(ns io.hosaka.ceterus.handler
  (:require [com.stuartsierra.component :as component]
            [yada.yada :as yada]
            [manifold.deferred :as d]
            [io.hosaka.common.db.health :as health]
            [io.hosaka.ceterus.orchestrator :as orchestrator]))

(defn get-service-key-values [orchestrator {:keys [body response] :as ctx}]
  (let [service (-> ctx :parameters :path :service)]
    (orchestrator/get-service-key-values orchestrator service)))

(defn get-db-health [health {:keys [response]}]
  (->
   (health/get-health health)
   (d/chain #(if (= (:health %1) "HEALTHY")
               (assoc response :body %1 :status 200)
               (assoc response :body %1 :status 503)))))

(defn build-routes [orchestrator health]
  ["/" [
        [["configs/" :service]
         (yada/resource {:parameters {:path {:service String}}
                         :methods {
                                   :get {:response (partial get-service-key-values orchestrator)
                                         :produces "application/json"}}})]
        ["health"
         (yada/resource {:methods {:get {:response (partial get-db-health health)
                                         :produces "application/json"}}})]
        ]])


(defrecord Handler [orchestrator routes health]
  component/Lifecycle

  (start [this]
    (assoc this :routes (build-routes orchestrator health)))

  (stop [this]
    (assoc this :routes nil)))


(defn new-handler []
  (component/using
   (map->Handler {})
   [:orchestrator :health])
)


