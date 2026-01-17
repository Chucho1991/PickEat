CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (
  id,
  nombres,
  correo,
  username,
  password_hash,
  rol,
  activo,
  deleted,
  deleted_at,
  deleted_by,
  created_at,
  updated_at
)
SELECT
  uuid_generate_v4(),
  'JESUS GUERRA CEDENO',
  'lml.chucho.lml@gmail.com',
  'jaguerra1991',
  crypt('Qwer1234.', gen_salt('bf')),
  'SUPERADMINISTRADOR',
  true,
  false,
  NULL,
  NULL,
  now(),
  now()
WHERE NOT EXISTS (
  SELECT 1
  FROM users
  WHERE username = 'jaguerra1991'
     OR correo = 'lml.chucho.lml@gmail.com'
);
