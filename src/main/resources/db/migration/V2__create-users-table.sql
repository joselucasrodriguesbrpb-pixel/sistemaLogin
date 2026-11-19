CREATE TABLE users
(
    id UUID NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    email varchar(100) NOT NULL UNIQUE,
    senha varchar(255) NOT NULL,
    data_de_nascimento DATE NOT NULL

);