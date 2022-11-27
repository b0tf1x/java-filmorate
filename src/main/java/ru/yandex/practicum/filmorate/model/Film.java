package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @PositiveOrZero
    private int id;
    private Set<Integer> likes = new HashSet<>();

    public void add(int id) {
        likes.add(id);
    }

    public void remove(int id) {
        likes.remove(id);
    }

    @NotBlank
    private final String name;
    @NotNull
    @Size(max = 200, message = "слишком длинное описание")
    private final String description;

    @NotNull
    private final LocalDate releaseDate;
    @Min(value = 1, message = "некорректная длина")
    @Positive
    private final long duration;
}
