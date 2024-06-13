ALTER TABLE crime_journal
    ADD CONSTRAINT chk_crime_journal_description CHECK(length(description::text) >= 4 AND length(description::text) <= 256);