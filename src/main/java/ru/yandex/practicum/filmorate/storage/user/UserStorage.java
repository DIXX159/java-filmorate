package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

public interface UserStorage {


    HashMap<Integer, User> users = new HashMap<>();

    User addUser(User user) throws ValidationException;

    User updateUser(User user) throws ValidationException;

}
