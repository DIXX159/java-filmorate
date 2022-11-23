package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class User {
    private int id;
    @Email(message = "Неправильный Email")
    private String email;
    private String login;
    private String name;
    @NotNull(message = "дата рождения не может быть пустой")
    private String birthday;
}
