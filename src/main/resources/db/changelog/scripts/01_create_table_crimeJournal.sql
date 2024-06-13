CREATE TABLE crime_journal (
    id uuid NOT NULL PRIMARY KEY,
    description character varying(100) NOT NULL,
    date_crime date NOT NULL,
    is_closed boolean NOT NULL,
    profile_id uuid NOT NULL
);