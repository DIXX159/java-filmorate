package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private int id;

    @Email(message = "Неправильный Email")
    private String email;

    private String login;
    private String name;
    @NotNull(message = "дата рождения не может быть пустой")
    private String birthday;
}
