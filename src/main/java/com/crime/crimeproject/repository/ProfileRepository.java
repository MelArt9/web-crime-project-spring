package com.crime.crimeproject.repository;

import com.crime.crimeproject.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ProfileRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // Сохранение нового профиля в базу данных
    public boolean save(Profile profile) {
        String sql = "INSERT INTO profile (id, login, password, email) " +
                     "VALUES (:id, :login, :password, :email)";

        return executeUpdate(profile, sql);
    }

    // Поиск профиля по ID
    public Optional<Profile> findById(UUID id) {
        String sql = "SELECT id, login, password, email " +
                     "FROM profile " +
                     "WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            Profile profile = namedParameterJdbcTemplate.queryForObject(sql, params, getProfileRowMapper());
            return Optional.ofNullable(profile);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    // Поиск всех профилей
    public List<Profile> findAll() {
        String sql = "SELECT id, login, password, email FROM profile";
        return namedParameterJdbcTemplate.query(sql, getProfileRowMapper());
    }

    // Обновляет существующий профиль
    public boolean update(Profile profile) {
        String sql = "UPDATE profile " +
                     "SET login = :login, password = :password, email = :email " +
                     "WHERE id = :id";

        return executeUpdate(profile, sql);
    }

    // Удаление профиля по ID
    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM profile WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }

    // Поиск профиля по логину
    public Optional<Profile> findByLogin(String login) {
        String sql = "SELECT id, login, password, email " +
                     "FROM profile " +
                     "WHERE login = :login";

        Map<String, Object> params = new HashMap<>();
        params.put("login", login);
        List<Profile> profiles = namedParameterJdbcTemplate.query(sql, params, getProfileRowMapper());
        return profiles.stream().findFirst();
    }

    // Поиск профиля по почте
    public Optional<Profile> findByEmail(String email) {
        String sql = "SELECT id, login, password, email " +
                     "FROM profile " +
                     "WHERE email = :email";

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        List<Profile> profiles = namedParameterJdbcTemplate.query(sql, params, getProfileRowMapper());
        return profiles.stream().findFirst();
    }

    // Проверка существования профиля по ID
    public boolean existsById(UUID id) {
        String sql = "SELECT COUNT(*) FROM profile WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        Integer count = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    private boolean executeUpdate(Profile profile, String sql) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", profile.getId());
        params.put("login", profile.getLogin());
        params.put("password", profile.getPassword());
        params.put("email", profile.getEmail());

        return namedParameterJdbcTemplate.update(sql, params) > 0;
    }


    // Маппер для преобразования ResultSet в объект Profile
    private RowMapper<Profile> getProfileRowMapper() {
        return (rs, rowNum) -> Profile.builder()
                .id(rs.getObject("id", UUID.class))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .email(rs.getString("email"))
                .build();
    }

}
