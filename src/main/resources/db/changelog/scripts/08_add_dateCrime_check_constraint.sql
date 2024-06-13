ALTER TABLE crime_journal
    ADD CONSTRAINT chk_crime_journal_date_crime CHECK(date_crime >= '1900-01-01'::date AND date_crime <= CURRENT_DATE);