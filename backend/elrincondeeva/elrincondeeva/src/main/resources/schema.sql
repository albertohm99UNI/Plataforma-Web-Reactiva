CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE if not exists users (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL CHECK (char_length(nombre) >= 1),
    password VARCHAR(255) NOT NULL CHECK (char_length(password) >= 6),
    apellidos VARCHAR(255) NOT NULL CHECK (char_length(apellidos) >= 1),
    email VARCHAR(255) NOT NULL,
    telefono CHAR(9) NOT NULL CHECK (telefono ~ '^[0-9]{9}$'),
    dni CHAR(9) NOT NULL CHECK (dni ~ '^\d{8}[A-Za-z]$'),
    fecha_nacimiento DATE NOT NULL,
    direccion VARCHAR(255) NOT NULL CHECK (char_length(direccion) >= 10),
    estado BOOLEAN NOT NULL,
    fecha_registro DATE NOT NULL,
    fecha_modificacion DATE NOT NULL,
    fecha_baja DATE
);


CREATE TABLE IF NOT EXISTS user_role (
    user_id INT,
    role_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE if not exists reviews (
    id SERIAL PRIMARY KEY,
    review TEXT NOT NULL,
    rate INTEGER NOT NULL CHECK (rate >= 1 AND rate <= 5),
     rate_servicios INTEGER NOT NULL CHECK (rate >= 1 AND rate <= 5),
     rate_limpieza INTEGER NOT NULL CHECK (rate >= 1 AND rate <= 5),
      rate_ubicacion INTEGER NOT NULL CHECK (rate >= 1 AND rate <= 5),
    user_id BIGINT NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS reservas (
    id SERIAL PRIMARY KEY,  
    nombre VARCHAR(255) NOT NULL CHECK (char_length(nombre) >= 1),
    fecha_inicio DATE NOT NULL, 
    fecha_fin DATE NOT NULL, 
    num_personas INT NOT NULL CHECK (num_personas > 0),  
    email VARCHAR(255) NOT NULL,
    telefono CHAR(9) NOT NULL CHECK (telefono ~ '^[0-9]{9}$'),
    dni CHAR(9) NOT NULL CHECK (dni ~ '^\d{8}[A-Za-z]$'),
    precio_total DECIMAL(10, 2) NOT NULL CHECK (precio_total >= 0), 
    estado VARCHAR(50) NOT NULL CHECK (estado IN ( 'CONFIRMADA', 'PAGADA', 'CANCELADA','FINALIZADA')), 
    usuario_id INT NOT NULL, 
    FOREIGN KEY (usuario_id) REFERENCES users(id) ON DELETE CASCADE 
);





