CREATE TABLE profile (
    id uuid NOT NULL PRIMARY KEY,
    login character varying(20) NOT NULL,
    password character varying(50) NOT NULL,
    email character varying(256) NOT NULL
);