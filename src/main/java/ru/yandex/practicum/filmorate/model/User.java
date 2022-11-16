package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class User {
    @NotNull
    private int id;
    @NotBlank
    @Email(message = "Неправильный Email")
    private String email;
    @NotNull
    private String login;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private String birthday;
}
