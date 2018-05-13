ALTER TABLE ceterus.service_key_values
ADD COLUMN IF NOT EXISTS encrypted boolean;

UPDATE ceterus.service_key_values
   SET encrypted = false
 WHERE encrypted IS NULL;

ALTER TABLE ceterus.service_key_values
ALTER COLUMN encrypted SET DEFAULT FALSE;

ALTER TABLE ceterus.service_key_values
ALTER COLUMN encrypted SET NOT NULL;

