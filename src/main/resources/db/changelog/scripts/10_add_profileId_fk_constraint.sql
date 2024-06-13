ALTER TABLE crime_journal
    ADD CONSTRAINT FK_crime_journal_profileId FOREIGN KEY (profile_id) REFERENCES profile (id);