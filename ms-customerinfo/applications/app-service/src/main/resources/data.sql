INSERT INTO person (nombre, genero, edad, identificacion, direccion, telefono)
VALUES
    ('Jose Lema', 'M', 30, '098254785', 'Otalvo sn y principal', '098254785'),
    ('Marianela Montalvo', 'F', 30, '097548965', 'Amazonas y NNUU', '097548965'),
    ('Juan Osorio', 'M', 25, '098874587', '13 junio y equinoccial', '098874587');
INSERT INTO client (contrasena, estado, person_id)
VALUES
    ('1234', 'true',1 ),
    ('5678', 'true',2 ),
    ('1245', 'true',3 );