package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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
    public static List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/users")
    public static User create(@Valid @RequestBody @NotNull User user) throws ValidationException {
        log.debug("Создание пользователя: {}", user.getName());
        int id = idGenerator++;
        user.setId(id);
        return getUser(user);
    }

    @PutMapping(value = "/users")
    public static User update(@Valid @RequestBody @NotNull User user) throws ValidationException {
        log.debug("Обновление пользователя: {}", user.getName());
        for (int id : users.keySet()) {
            if (user.getId() == id) {
                return getUser(user);
            }
        }
        log.debug("такого пользователя нет");
        throw new ValidationException("такого пользователя нет");
    }

    @Validated
    private static User getUser(@Valid @RequestBody @NotNull User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            idGenerator--;
            log.debug("электронная почта не может быть пустой и должна содержать символ '@'");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ '@'");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            idGenerator--;
            log.debug("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday() == null) {
            log.debug("дата рождения не может быть пустой");
            throw new ValidationException("дата рождения не может быть пустой");
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
