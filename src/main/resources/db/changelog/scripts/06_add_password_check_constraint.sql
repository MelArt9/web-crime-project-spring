ALTER TABLE profile
    ADD CONSTRAINT chk_profile_password CHECK(length(password::text) >= 8 AND length(password::text) <= 50);