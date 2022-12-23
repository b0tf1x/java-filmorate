package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> findAll() {
        String query = "select * from mpa";
        return jdbcTemplate.query(query, this::createMpa);
    }

    private Mpa createMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("mpa_id");
        String name = resultSet.getString("name");
        return new Mpa(id, name);
    }

    @Override
    public Mpa getById(int id) {
        String sqlQuery = "select * from mpa where mpa_id = ?";
        SqlRowSet mpaRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRowSet.next()) {
            throw new FilmException("Рейтинг не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::createMpa, id);
    }
}
