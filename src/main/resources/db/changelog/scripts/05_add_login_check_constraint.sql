ALTER TABLE profile
    ADD CONSTRAINT chk_profile_login CHECK(length(login::text) >= 3 AND length(login::text) <= 20);