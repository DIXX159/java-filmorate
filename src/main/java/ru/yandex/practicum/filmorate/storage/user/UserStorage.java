package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;
}
