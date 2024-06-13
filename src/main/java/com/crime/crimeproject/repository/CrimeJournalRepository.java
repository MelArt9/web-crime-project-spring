package com.crime.crimeproject.repository;

import com.crime.crimeproject.entity.CrimeJournal;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class CrimeJournalRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Сохраняет новую запись в журнале преступлений в БД
    public boolean save(CrimeJournal crimeJournal) {
        String sql = "INSERT INTO crime_journal (id, description, date_crime, is_closed, profile_id) " +
                "VALUES (:id, :description, :dateCrime, :isClosed, :profileId)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", crimeJournal.getId());
        params.put("description", crimeJournal.getDescription());
        params.put("dateCrime", crimeJournal.getDateCrime());
        params.put("isClosed", crimeJournal.getIsClosed());
        params.put("profileId", crimeJournal.getProfileId());
        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }

    // Возвращает запись журнала по ID, если она существует
    public Optional<CrimeJournal> findById(UUID id) {
        String sql = "SELECT id, description, date_crime, is_closed, profile_id " +
                     "FROM crime_journal " +
                     "WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            CrimeJournal journal = namedParameterJdbcTemplate.queryForObject(sql, params, getCrimeJournalRowMapper());
            return Optional.ofNullable(journal);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();  // Возвращает пустой Optional, если запись не найдена
        }
    }

    // Ищет записи по описанию
    public List<CrimeJournal> findByDescription(String query) {
        String sql = "SELECT id, description, date_crime, is_closed, profile_id " +
                     "FROM crime_journal " +
                     "WHERE description ILIKE :description";

        Map<String, Object> params = new HashMap<>();
        params.put("description", "%" + query + "%");
        return namedParameterJdbcTemplate.query(sql, params, getCrimeJournalRowMapper());
    }

    // Возвращает все записи из журнала
    public List<CrimeJournal> findAll() {
        String sql = "SELECT id, description, date_crime, is_closed, profile_id " +
                     "FROM crime_journal";

        return namedParameterJdbcTemplate.query(sql, getCrimeJournalRowMapper());
    }

    // Возвращает все записи журнала, связанные с указанным ID профиля юзера
    public List<CrimeJournal> findByProfileId(UUID profileId) {
        String sql = "SELECT id, description, date_crime, is_closed, profile_id " +
                     "FROM crime_journal " +
                     "WHERE profile_id = :profileId";

        Map<String, Object> params = new HashMap<>();
        params.put("profileId", profileId);
        return namedParameterJdbcTemplate.query(sql, params, getCrimeJournalRowMapper());
    }

    // Поиск записи по всем атрибутам
    public Optional<CrimeJournal> findByAllAttributes(CrimeJournal crimeJournal) {
        String sql = "SELECT id, description, date_crime, is_closed, profile_id " +
                "FROM crime_journal " +
                "WHERE description = :description AND date_crime = :dateCrime " +
                "AND is_closed = :isClosed AND profile_id = :profileId";

        Map<String, Object> params = new HashMap<>();
        params.put("description", crimeJournal.getDescription());
        params.put("dateCrime", crimeJournal.getDateCrime());
        params.put("isClosed", crimeJournal.getIsClosed());
        params.put("profileId", crimeJournal.getProfileId());

        try {
            CrimeJournal journal = namedParameterJdbcTemplate.queryForObject(sql, params, getCrimeJournalRowMapper());
            return Optional.ofNullable(journal);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Обновляет существующую запись в журнале
    public boolean update(CrimeJournal crimeJournal) {
        String sql = "UPDATE crime_journal SET " +
                "description = :description, " +
                "date_crime = :dateCrime, " +
                "is_closed = :isClosed, " +
                "profile_id = :profileId " +
                "WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", crimeJournal.getId());
        params.put("description", crimeJournal.getDescription());
        params.put("dateCrime", crimeJournal.getDateCrime());
        params.put("isClosed", crimeJournal.getIsClosed());
        params.put("profileId", crimeJournal.getProfileId());

        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }

    // Удаляет запись из журнала по ID
    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM crime_journal WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }

    // Удаляет все записи журнала, связанные с указанным ID профиля юзера
    public int deleteByProfileId(UUID profileId) {
        String sql = "DELETE FROM crime_journal WHERE profile_id = :profileId";
        Map<String, Object> params = new HashMap<>();
        params.put("profileId", profileId);
        return namedParameterJdbcTemplate.update(sql, params);
    }

    // Проверяет существует ли запись по заданному ID
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM crime_journal WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    // Возвращает маппер для преобразования данных из БД в объект CrimeJournal
    private RowMapper<CrimeJournal> getCrimeJournalRowMapper() {
        return (rs, rowNum) -> CrimeJournal.builder()
                .id(rs.getObject("id", UUID.class))
                .description(rs.getString("description"))
                .dateCrime(rs.getDate("date_crime"))
                .isClosed(rs.getBoolean("is_closed"))
                .profileId(rs.getObject("profile_id", UUID.class))
                .build();
    }
}