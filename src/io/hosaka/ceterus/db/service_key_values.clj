(ns io.hosaka.ceterus.db.service-key-values
  (:require [manifold.deferred :as d]
            [hugsql.core :as hugsql]
            [io.hosaka.common.db :refer [get-connection]]))

(hugsql/def-db-fns "db/sql/service_key_values.sql")

(defn get-service-key-values [db service]
  (d/future
    (get-service-key-values-sql (get-connection db) {:service service})))
