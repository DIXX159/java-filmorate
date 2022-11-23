package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    public static final HashMap<Integer, User> users = new HashMap<>();
    protected static int idGenerator = 1;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

    @Override
    public User addUser(User user) throws ValidationException {
        logger.debug("Создание пользователя: {}", user.getName());
        int id = idGenerator++;
        user.setId(id);
        return getUser(user);
    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public User updateUser(User user) throws ValidationException {
        logger.debug("Обновление пользователя: {}", user.getName());
        for (int id : users.keySet()) {
            if (user.getId() == id) {
                return getUser(user);
            }
        }
        logger.debug("такого пользователя нет");
        throw new ValidationException("такого пользователя нет");
    }

    public List<User> findAll() {
        logger.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Validated
    public User getUser(@Valid @RequestBody @NotNull User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            idGenerator--;
            logger.debug("электронная почта не может быть пустой и должна содержать символ '@'");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ '@'");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            idGenerator--;
            logger.debug("логин не может быть пустым и содержать пробелы");
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (user.getBirthday() == null) {
            logger.debug("дата рождения не может быть пустой");
            throw new ValidationException("дата рождения не может быть пустой");
        } else if (LocalDate.parse(user.getBirthday(), formatter).isAfter(LocalDate.now())) {
            idGenerator--;
            logger.debug("дата рождения не может быть в будущем");
            throw new ValidationException("дата рождения не может быть в будущем");
        } else if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            users.put(user.getId(), user);
            logger.info("Сохранен пользователь: {}", user.getId());
            return user;
        } else {
            users.put(user.getId(), user);
            logger.info("Сохранен пользователь: {}", user.getId());
            return user;
        }
    }
}
