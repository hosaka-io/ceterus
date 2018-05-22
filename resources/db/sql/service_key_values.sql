-- db/sql/service_key_values.sql

-- :name get-service-key-values-sql :? :*
SELECT k, val, encrypted
FROM ceterus.service_key_values
WHERE current_value AND service = CAST(:service AS UUID)
