ALTER TABLE profile
    ADD CONSTRAINT UQ_profile_login UNIQUE(login);