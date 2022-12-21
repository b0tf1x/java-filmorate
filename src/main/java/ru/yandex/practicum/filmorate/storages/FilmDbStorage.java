package ru.yandex.practicum.filmorate.storages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_FILMS = "select * from films";

    @Override
    public Collection<Film> findAll() {
        return jdbcTemplate.query(SELECT_FILMS, this::createFilm);
    }

    @Override
    public Map<Integer, Film> getFilms() {
        Map<Integer, Film> filmsMap = new HashMap<>();
        Collection<Film> films = jdbcTemplate.query(SELECT_FILMS, this::createFilm);
        for (Film film : films) {
            filmsMap.put(film.getId(), film);
        }
        return filmsMap;
    }

    private Film createFilm(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("id");
        final String name = resultSet.getString("name");
        final String description = resultSet.getString("description");
        final LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        long duration = resultSet.getLong("duration");
        return new Film(id, name, description, releaseDate, duration, getMpa(id), getGenres(id));
    }

    private Mpa getMpa(int id) {
        final String getMpaSqlQuery = "select mpa.mpa_id, name from mpa " +
                "left join mpa_films as mf on mpa.mpa_id=mf.mpa_id where film_id=?";
        return jdbcTemplate.queryForObject(getMpaSqlQuery, this::createMpa, id);
    }

    private Mpa createMpa(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("mpa_id");
        final String name = resultSet.getString("name");
        return new Mpa(id, name);
    }

    private List<Genre> getGenres(int id) {
        final String getGenreSqlQuery = "select genre.genre_id,genre_name from genre " +
                "left join film_genre as fg on genre.genre_id=fg.genre_id " +
                "where film_id = ? ";
        return jdbcTemplate.query(getGenreSqlQuery, this::createGenre, id);
    }

    private Genre createGenre(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("genre_id");
        final String name = resultSet.getString("genre_name");
        return new Genre(id, name);
    }

    @Override
    public Film create(Film film) {
        final String sqlQuery = "insert into films (name, description, release_date, duration) " +
                "values (?, ?, ?, ?) ";
        KeyHolder generatedId = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            return stmt;
        }, generatedId);
        film.setId(Objects.requireNonNull(generatedId.getKey()).intValue());
        final String mpaSqlQuery = "insert into mpa_films (film_id, mpa_id) VALUES (?, ?)";
        log.info("filmid " + film.getId() + " mpaid " + film.getMpa().getId());
        jdbcTemplate.update(mpaSqlQuery, film.getId(), film.getMpa().getId());
        final String getGenresSqlQuery = "insert into film_genre (film_id, genre_id) values (?, ?)";
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                jdbcTemplate.update(getGenresSqlQuery, film.getId(), g.getId());
            }
        }
        film.setMpa(getMpa(film.getId()));
        film.setGenres(getGenres(film.getId()));
        return film;
    }

    private void checkExists(int id) {
        final String checkQuery = "select * from films where id = ?";
        SqlRowSet filmRowSet = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!filmRowSet.next()) {
            throw new FilmException("Фильм не найден");
        }
    }

    @Override
    public Film put(Film film) {
        checkExists(film.getId());
        final String query = "update films set name = ?, description = ?, release_date = ?, duration = ? " +
                "where id = ?";
        if (film.getGenres() != null) {
            final String delete = "delete from film_genre where film_id=?";
            jdbcTemplate.update(delete, film.getId());
            final String update = "insert into film_genre(film_id, genre_id) values(?,?)";
            for (Genre genre : film.getGenres()) {
                String checkExists = "select * from film_genre where film_id = ? and genre_id=?";
                SqlRowSet checkExistsRow = jdbcTemplate.queryForRowSet(checkExists, film.getId(), genre.getId());
                if (!checkExistsRow.next()) {
                    jdbcTemplate.update(update, film.getId(), genre.getId());
                } else {
                    log.warn("Такой фильм уже существует");
                }
            }
        }
        if (film.getMpa() != null) {
            final String delete = "delete from mpa_films where film_id=?";
            jdbcTemplate.update(delete, film.getId());
            final String update = "insert into mpa_films(film_id, mpa_id) values(?,?)";
            jdbcTemplate.update(update, film.getId(), film.getMpa().getId());
        }
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        film.setMpa(getMpa(film.getId()));
        film.setGenres(getGenres(film.getId()));
        return film;
    }

    @Override
    public Film getById(int id) {
        String checkExists = "select * from films where id=?";
        checkExists(id);
        return jdbcTemplate.queryForObject(checkExists, this::createFilm, id);
    }

    @Override
    public Film deleteById(int id) {
        Film film = getById(id);
        final String genreQuery = "delete from film_genre where film_id=?";
        final String mpaQuery = "delete from mpa_films where film_id=?";
        final String query = "delete from films where id=?";
        jdbcTemplate.update(genreQuery, id);
        jdbcTemplate.update(mpaQuery, id);
        jdbcTemplate.update(query, id);
        return film;
    }

    @Override
    public Film addLike(int filmId, int userId) {
        final String query = "insert into films_likes(film_id,user_id) values(?, ?)";
        jdbcTemplate.update(query, filmId, userId);
        return getById(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        final String query = "delete from films_likes where film_id=? and user_id=?";
        jdbcTemplate.update(query, filmId, userId);
        return getById(filmId);
    }


    @Override
    public List<Film> getTop(int count) {
        final String query = "select id, name,description,release_date,duration from films " +
                "left join films_likes as fl on films.id=fl.film_id " +
                "group by films.id, fl.film_id in (select film_id from films_likes) " +
                "order by count(fl.film_id) asc limit ?";
        return jdbcTemplate.query(query, this::createFilm, count);
    }


}
