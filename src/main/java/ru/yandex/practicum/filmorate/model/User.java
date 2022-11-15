package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private int id;
    @NotBlank
    @Email(message = "Неправильный Email")
    private String email;
    private String login;
    @NotNull
    private String name;
    private String birthday;
}
