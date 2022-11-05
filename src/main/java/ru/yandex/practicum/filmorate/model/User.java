package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    private int id;
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
