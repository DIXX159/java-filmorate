package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Validated
@RestController
@Slf4j
public class UserController {
    public static final HashMap<Integer, User> users = new HashMap<>();
    public static int idGenerator = 1;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

    @GetMapping("/users")
    public static List<User> findAll() throws ValidationException {
        log.debug("Текущее количество пользователей: {}", users.size());
        if (!users.isEmpty()) {
            return new ArrayList<>(users.values());
        }
        throw new ValidationException("список пользователей пуст");
    }

    @PostMapping(value = "/users")
    public static User create(@Valid @RequestBody @NotNull User user) throws ValidationException {
        int id = idGenerator++;
        user.setId(id);
        return getUser(user);
    }

    @PutMapping(value = "/users")
    public static User update(@Valid @RequestBody @NotNull User user) throws ValidationException {
        for (int id : users.keySet()) {
            if (user.getId() == id) {
                return getUser(user);
            }
        }
        throw new ValidationException("такого пользователя нет");
    }

    private static User getUser(@Valid @RequestBody @NotNull User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            idGenerator--;
            log.debug("электронная почта не может быть пустой и должна содержать символ '@'");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ '@'");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            idGenerator--;
            log.debug("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (LocalDate.parse(user.getBirthday(), formatter).isAfter(LocalDate.now())) {
            idGenerator--;
            log.debug("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            users.put(user.getId(), user);
            log.info("Сохранен пользователь: {}", user.getId());
            return user;
        } else {
            users.put(user.getId(), user);
            log.info("Сохранен пользователь: {}", user.getId());
            return user;
        }
    }
}
