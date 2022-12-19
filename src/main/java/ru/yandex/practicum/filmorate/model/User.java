package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @PositiveOrZero
    private int id;
    private Set<Integer> friends = new HashSet<>();

    public void add(int id) {
        friends.add(id);
    }

    public void remove(int id) {
        friends.remove(id);
    }

    @NotNull
    @Email
    private final String email;

    @NotNull
    @NotBlank
    private final String login;
    private String name;

    @NotNull
    @PastOrPresent
    private final LocalDate birthday;

}
