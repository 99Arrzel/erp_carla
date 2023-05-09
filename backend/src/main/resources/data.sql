INSERT INTO public.usuarios
(id, nombre, "password", tipo, usuario)
VALUES(1, 'Carla', '$2a$10$ONH9QIf8POwdu.R3QSbBReOcymI0ofGwwrvjEPwNia2GnyPBHEvE2', 'ADMIN', 'carla')
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.monedas
(id, abreviatura, descripcion, nombre, usuario_id)
VALUES(1, 'BOB', 'Moneda Nacional', 'Bolivianos', 1)
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.monedas
(id, abreviatura, descripcion, nombre, usuario_id)
VALUES(2, 'USD', 'Dólares Estadounidenses', 'Dólares', 1)
ON CONFLICT (id) DO NOTHING;
INSERT INTO public.monedas
(id, abreviatura, descripcion, nombre, usuario_id)
VALUES(3, '€UR', 'Euros', 'Euros', 1)
ON CONFLICT (id) DO NOTHING;
INSERT INTO public.monedas
(id, abreviatura, descripcion, nombre, usuario_id)
VALUES(3, 'R$', 'Reales de Brasil', 'Reales Brasileños', 1)
ON CONFLICT (id) DO NOTHING;