package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Validated
public class User {
    private int id;
    @Email(message = "Неправильный Email")
    private String email;
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    private String login;
    private String name;
    @Past(message = "дата рождения не может в будущем")
    private LocalDate birthday;

    private final Set<Integer> friends = new LinkedHashSet<>();

    public void addFriend(int friendId){
        friends.add(friendId);
    }

}
