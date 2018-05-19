ALTER TABLE ceterus.service_key_values
ALTER COLUMN id TYPE uuid USING id::uuid;

ALTER TABLE ceterus.service_key_values
ALTER COLUMN service TYPE uuid USING id::uuid;

ALTER TABLE ceterus.service_key_values
ALTER COLUMN id SET DEFAULT uuid_generate_v4();
