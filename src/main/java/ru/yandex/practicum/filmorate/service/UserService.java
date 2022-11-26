package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class UserService extends InMemoryUserStorage {

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
    }

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @NotNull
    public User addFriend(int userId, int friendId) {
        if (users.get(userId) != null & users.get(friendId) != null) {
            logger.debug("Добавление в друзья: {}", users.get(friendId).getName());
            users.get(userId).addFriend(friendId);
            users.get(friendId).addFriend(userId);
            return users.get(userId);
        } else throw new NotFoundException("Пользователь не найден");
    }

    public User deleteFriend(int userId, int friendId) {
        logger.debug("Удаление из друзей: {}", users.get(friendId).getName());
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
        return users.get(userId);
    }

    public Set<User> getAllFriends(int userId) {
        logger.debug("Текущее количество друзей: {}", users.get(userId).getFriends().size());
        Set<User> friends = new LinkedHashSet<>();
        for (Integer friend : users.get(userId).getFriends()) {
            friends.add(users.get(friend));
        }
        return friends;
    }

    public Set<User> getCommonFriends(int userId, int friendId) {
        Set<User> commonFriends = new LinkedHashSet<>();
        if (users.get(userId).getFriends() != null & users.get(friendId).getFriends() != null) {
            for (Integer user1 : users.get(userId).getFriends()) {
                for (Integer user2 : users.get(friendId).getFriends()) {
                    if (user1.equals(user2)) {
                        commonFriends.add(users.get(user2));
                    }
                }
            }
        }
        //if (commonFriends != null) {
        //return commonFriends;
        //} else throw new NotFoundException("У пользователей нет общих друзей");
        return commonFriends;
    }

    public User findUserById(int userId) {
        if (users.get(userId) != null) {
            return users.get(userId);
        }
        throw new NotFoundException("Пользователь не найден");
    }
}