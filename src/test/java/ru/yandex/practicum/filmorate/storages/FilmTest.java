package ru.yandex.practicum.filmorate.storages;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.AssertionsForClassTypes;
import ru.yandex.practicum.filmorate.exceptions.FilmException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import org.assertj.core.api.Assertions;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmTest {

    private final FilmDbStorage filmDbStorage;

    private final UserDbStorage userDbStorage;
    private Film film = new Film(1, "test", "descr", LocalDate.of(2022, 1, 1), 120, new MPA(1, "A"), List.of());

    @Test
    void addFilm() {
        filmDbStorage.create(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
    }

    @Test
    void updateFilm() {
        filmDbStorage.create(film);
        film.setName("testUpdateFilm");
        film.setDescription("testUpdateDesc");
        filmDbStorage.put(film);
        AssertionsForClassTypes.assertThat(filmDbStorage.getById(film.getId()))
                .hasFieldOrPropertyWithValue("name", "testUpdateFilm")
                .hasFieldOrPropertyWithValue("description", "testUpdateDesc");
    }

    @Test
    void getFilm() {
        filmDbStorage.create(film);
        filmDbStorage.getById(film.getId());
        AssertionsForClassTypes.assertThat(filmDbStorage.getById(film.getId())).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void deleteByIdFilm() {
        filmDbStorage.create(film);
        filmDbStorage.deleteById(film.getId());
        AssertionsForClassTypes.assertThat(film).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void updateFilmNotFound() {
        Film newFilm = new Film(100, "test", "descr", LocalDate.of(2022, 1, 1), 120, new MPA(1, "A"), List.of());
        Assertions.assertThatThrownBy(() -> filmDbStorage.put(newFilm))
                .isInstanceOf(FilmException.class);
    }

    @Test
    void addLikeFilm() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2020, 10, 2));
        Film film1 = new Film(1, "test", "descr", LocalDate.of(2022, 1, 1), 120, new MPA(1, "A"), List.of());
        userDbStorage.create(user);
        filmDbStorage.create(film1);
        filmDbStorage.addLike(film1.getId(), user.getId());
        assertThat(filmDbStorage.getTop(film1.getId()).isEmpty());
        assertThat(filmDbStorage.getTop(film1.getId())).isNotNull();
        Assertions.assertThat(filmDbStorage.getTop(film1.getId()).size() == 2);
    }

    @Test
    void removeLikeDuplicate() {
        User user = new User(1, "email", "login", "name", LocalDate.of(2020, 10, 2));
        Film film2 = new Film(1, "test", "descr", LocalDate.of(2022, 1, 1), 120, new MPA(1, "A"), List.of());
        userDbStorage.create(user);
        filmDbStorage.create(film2);
        filmDbStorage.create(film2);
        filmDbStorage.addLike(film2.getId(), user.getId());
        filmDbStorage.removeLike(film2.getId(), user.getId());
        assertThat(filmDbStorage.getTop(film2.getId()).isEmpty());
        assertThat(filmDbStorage.getTop(film2.getId())).isNotNull();
        Assertions.assertThat(filmDbStorage.getTop(film2.getId()).size() == 1);
    }

    @Test
    void getTopFilm() {
        Film film3 = new Film(1, "test", "descr", LocalDate.of(2022, 1, 1), 120, new MPA(1, "A"), List.of());
        Film film4 = new Film(2, "test2", "descr2", LocalDate.of(2020, 1, 1), 50, new MPA(2, "B"), List.of());

        filmDbStorage.create(film);
        filmDbStorage.create(film3);
        filmDbStorage.create(film4);

        User user1 = new User(1, "email", "login", "name", LocalDate.of(2020, 10, 2));
        User user2 = new User(2, "email", "login", "name", LocalDate.of(2020, 10, 2));
        User user3 = new User(3, "email", "login", "name", LocalDate.of(2020, 10, 2));

        userDbStorage.create(user1);
        userDbStorage.create(user2);
        userDbStorage.create(user3);

        filmDbStorage.addLike(film.getId(), user1.getId());
        filmDbStorage.addLike(film3.getId(), user2.getId());
        filmDbStorage.addLike(film4.getId(), user3.getId());
        filmDbStorage.addLike(film.getId(), user2.getId());
        filmDbStorage.addLike(film.getId(), user3.getId());
        assertThat(filmDbStorage.getTop(film.getId())).isNotNull();
        Assertions.assertThat(filmDbStorage.getTop(film.getId()).size() == 6);
    }
}
