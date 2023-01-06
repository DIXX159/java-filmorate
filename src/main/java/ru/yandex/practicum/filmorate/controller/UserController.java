package ru.yandex.practicum.filmorate.controller;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@EnableJpaRepositories
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserDbStorage userDbStorage;

    public UserController(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Integer id) {
        return userDbStorage.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return userDbStorage.getCommonFriends(id, otherId);
    }

    @GetMapping
    public List<User> getAll() {
        return userDbStorage.findAll();
    }

    @PutMapping
    public User update(@Valid @RequestBody @NotNull User user) {
        return userDbStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        return userDbStorage.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        return userDbStorage.deleteFriend(id, friendId);
    }

    @Validated
    @PostMapping
    public User addUser(@Valid @RequestBody @NotNull User user) {
        return userDbStorage.addUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        return userDbStorage.findOne(userId);
    }

    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable("id") Integer userId) {
        return userDbStorage.deleteUser(userId);
    }
}