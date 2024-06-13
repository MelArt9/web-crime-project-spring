ALTER TABLE profile
    ADD CONSTRAINT UQ_profile_email UNIQUE(email);