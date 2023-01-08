package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

@Slf4j
@Service
@Qualifier
public class UserService extends InMemoryUserStorage {
    private final UserDbStorage userDbStorage;
    private final InMemoryUserStorage inMemoryUserStorage;

    public UserService(UserDbStorage userDbStorage, InMemoryUserStorage inMemoryUserStorage) {
        this.userDbStorage = userDbStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }
}




















   /* public User addFriend(int userId, int friendId) {
        if (users.get(userId) != null && users.get(friendId) != null) {
            log.debug("Добавление в друзья: {}", users.get(friendId).getName());
            users.get(userId).addFriend(friendId);
            users.get(friendId).addFriend(userId);
            return users.get(userId);
        } else throw new NotFoundException(Constants.userNotFound);
    }

    public User deleteFriend(int userId, int friendId) {
        if (users.get(userId) != null && users.get(friendId) != null) {
            log.debug("Удаление из друзей: {}", users.get(friendId).getName());
            users.get(userId).getFriends().remove(friendId);
            users.get(friendId).getFriends().remove(userId);
            return users.get(userId);
        }
        throw new NotFoundException(Constants.userNotFound);
    }

    public Set<User> getAllFriends(int userId) {
        if (users.get(userId) != null) {
            log.debug("Текущее количество друзей: {}", users.get(userId).getFriends().size());
            Set<User> friends = new LinkedHashSet<>();
            for (Integer friend : users.get(userId).getFriends()) {
                friends.add(users.get(friend));
            }
            return friends;
        } else throw new NotFoundException(Constants.userNotFound);
    }

    public Set<User> getCommonFriends(int userId, int friendId) {
        Set<User> commonFriends = new LinkedHashSet<>();
        log.debug("Запрос общих друзей");
        if (users.get(userId) != null && users.get(friendId) != null) {
            if (users.get(userId).getFriends() != null & users.get(friendId).getFriends() != null) {
                for (Integer user1 : users.get(userId).getFriends()) {
                    for (Integer user2 : users.get(friendId).getFriends()) {
                        if (user1.equals(user2)) {
                            commonFriends.add(users.get(user2));
                        }
                    }
                }
            }
            return commonFriends;
        }
        log.debug("Пользователь не найден: {}", users.get(userId));
        throw new NotFoundException(Constants.userNotFound);
    }

    public User findUserById(int userId) {
        log.debug("Поиск пользователя: {}", users.get(userId));
        if (users.get(userId) != null) {
            return users.get(userId);
        }
        log.debug("Пользователь не найден: {}", users.get(userId));
        throw new NotFoundException(Constants.userNotFound);
    }*/
