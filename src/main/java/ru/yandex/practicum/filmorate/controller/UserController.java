package ru.yandex.practicum.filmorate.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RestController
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @GetMapping("/users")
    public List<User> findAll() {
        return inMemoryUserStorage.findAll();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody @NotNull User user) throws ValidationException {
        return inMemoryUserStorage.addUser(user);

    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody @NotNull User user) throws ValidationException {
        return inMemoryUserStorage.updateUser(user);
    }

}
