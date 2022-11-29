package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    protected static int idGenerator = 1;

    public void resetIdGenerator() {
        idGenerator = 1;
    }

    @Override
    public User addUser(User user) {
        log.debug("Создание пользователя: {}", user.getName());
        int id = idGenerator++;
        user.setId(id);
        return getUser(user);
    }

    @Override
    public User updateUser(User user) {
        log.debug("Обновление пользователя: {}", user.getName());
        for (int id : users.keySet()) {
            if (user.getId() == id) {
                return getUser(user);
            }
        }
        log.debug("такого пользователя нет");
        throw new NotFoundException(Constants.userNotFound);
    }

    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Validated
    public User getUser(@Valid @RequestBody @NotNull User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        log.info("Сохранен пользователь: {}", user.getId());
        return user;
    }

    public static HashMap<Integer, User> getUsers() {
        return new HashMap<>(users);
    }
}
