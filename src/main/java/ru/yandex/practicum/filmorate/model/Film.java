package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @PositiveOrZero
    private int id;

    public Film(int id, String name, String description, LocalDate releaseDate, long duration, MPA mpa, List<Genre> genre) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genre = genre;
    }

    @NotBlank
    private String name;
    @NotNull
    @Size(max = 200, message = "слишком длинное описание")
    private String description;

    @NotNull
    private LocalDate releaseDate;
    @Min(value = 1, message = "некорректная длина")
    @Positive
    private long duration;
    private List<Genre> genre;
    private MPA mpa;

}
