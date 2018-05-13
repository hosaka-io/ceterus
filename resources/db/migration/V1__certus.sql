CREATE TABLE IF NOT EXISTS ceterus.service_key_values
(
    id text not null,
    service text not null,
    k text not null,
    val text not null,
    current_value boolean NOT NULL,
    created_on timestamp without time zone NOT NULL DEFAULT now(),
    created_by text NOT NULL,
    removed_on timestamp without time zone NOT NULL DEFAULT now(),
    removed_by text NOT NULL,
    CONSTRAINT ceterus_service_key_values_pk PRIMARY KEY (id),
    UNIQUE(service,k,val),
    CONSTRAINT ceterus_service_key_current_value EXCLUDE USING btree (service WITH =, k WITH =) WHERE (current_value) DEFERRABLE INITIALLY DEFERRED
);
