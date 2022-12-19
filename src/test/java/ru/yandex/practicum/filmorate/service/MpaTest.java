package ru.yandex.practicum.filmorate.service;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.MPA;
import org.assertj.core.api.Assertions;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.services.MpaService;

import java.util.Collection;
import java.util.Arrays;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaTest {
    private final MpaService mpaService;

    @Test
    public void testGetAllMpa() {
        Collection<MPA> mpaRatingStorage = mpaService.findAll();
        Assertions.assertThat(mpaRatingStorage)
                .isNotEmpty()
                .extracting(MPA::getName)
                .containsAll(Arrays.asList("G", "PG", "PG-13", "R", "NC-17"));
    }

    @Test
    public void testGetMpaById() {
        MPA mpa = mpaService.getById(2);
        Assertions.assertThat(mpa)
                .hasFieldOrPropertyWithValue("id", 2)
                .hasFieldOrPropertyWithValue("name", "PG");
    }
}
