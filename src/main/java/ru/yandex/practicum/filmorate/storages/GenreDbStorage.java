package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements  GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> findAll() {
        String sqlQuery = "select * from genre";

        return jdbcTemplate.query(sqlQuery, this::createGenre);
    }
    private Genre createGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("genre_id");
        String name = resultSet.getString("genre_name");
        return new Genre(id, name);
    }
    @Override
    public Genre getById(int id) {
        final String sqlQuery = "select * from genre where genre_id = ? ";
        SqlRowSet genreRowSet = jdbcTemplate.queryForRowSet(sqlQuery, id);

        if (!genreRowSet.next()) {
            throw new FilmException("Жанр не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::createGenre, id);
    }
}
