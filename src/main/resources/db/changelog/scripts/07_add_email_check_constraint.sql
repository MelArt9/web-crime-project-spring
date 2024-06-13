ALTER TABLE profile
    ADD CONSTRAINT chk_profile_email CHECK(length(email::text) >= 1 AND length(email::text) <= 256);