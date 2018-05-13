ALTER TABLE ceterus.service_key_values
ALTER COLUMN removed_on DROP DEFAULT;

ALTER TABLE ceterus.service_key_values
ALTER COLUMN removed_on DROP NOT NULL;

ALTER TABLE ceterus.service_key_values
ALTER COLUMN removed_by DROP NOT NULL;
