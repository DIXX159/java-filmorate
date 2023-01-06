package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User findOne(int id) {
        log.debug("Поиск юзера: {}", id);
        String sqlQuery = "select USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "from USERS where USER_ID = ?";
        List<User> user = jdbcTemplate.query(sqlQuery, this::mapRow, id);
        if (user.size() != 1) {
            log.debug("Юзер не найден: {}", id);
            throw new NotFoundException(Constants.userNotFound);
        }
        return user.get(0);
    }

    @Override
    public User addUser(User user) {
        log.debug("Добавление юзера: {}", user.getName());
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        jdbcTemplate.update("insert into USERS (email, login, name, birthday) values (?, ?, ?, ?)",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return jdbcTemplate.queryForObject("select * from USERS where NAME = ?", this::mapRow, user.getName());
    }

    public User updateUser(User user) {
        log.debug("Обновление юзера: {}", user.getName());
        try {
            findOne(user.getId());
            String sqlQuery = "update USERS set " +
                    "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                    "where USER_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return user;
        } catch (NotFoundException e) {
            log.debug("Юзер не найден: {}", user.getName());
            throw new NotFoundException(Constants.userNotFound);
        }
    }

    public List<User> findAll() {
        String sqlQuery = "select * from USERS";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRow);
        log.debug("Поиск всех юзеров: {}", users.size());
        return users;
    }

    public boolean deleteUser(int id) {
        log.debug("Удаление юзера: {}", id);
        String sqlQuery = "delete from USERS where USER_ID = ?";
        return jdbcTemplate.update(sqlQuery, id) > 0;
    }

    public User addFriend(int userId, int friendId) {
        log.debug("Запрос в друзья: {}", userId);
        try {
            findOne(userId);
            findOne(friendId);
            jdbcTemplate.update("insert into FRIENDS values (?, ?, ?)", userId, friendId, "Запрос");
            return findOne(userId);
        } catch (NotFoundException e) {
            log.debug("Юзер не найден: {}", userId);
            throw new NotFoundException(Constants.userNotFound);
        }
    }

    public boolean deleteFriend(int userId, int friendId) {
        log.debug("Удаление из друзей: {}", userId);
        try {
            findOne(userId);
            findOne(friendId);
            String sqlQuery = "delete from FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
            return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
        } catch (NotFoundException e) {
            log.debug("Юзер не найден: {}", userId);
            throw new NotFoundException(Constants.userNotFound);
        }
    }

    public List<User> getAllFriends(int userId) {
        log.debug("Поиск всех друзей по юзеру: {}", userId);
        String sqlQuery = "select USERS.USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM USERS " +
                "JOIN FRIENDS ON USERS.USER_ID = FRIENDS.FRIEND_ID " +
                "WHERE FRIENDS.USER_ID = " + userId;
        return jdbcTemplate.query(sqlQuery, this::mapRow);
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        log.debug("Поиск общих друзей по юзерам: {}, {}", userId, friendId);
        try {
            findOne(userId);
            findOne(friendId);
            String sqlQuery = "select USERS.USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                    "FROM USERS " +
                    "JOIN FRIENDS ON USERS.USER_ID = FRIENDS.FRIEND_ID " +
                    "WHERE FRIENDS.USER_ID = " + friendId;
            return jdbcTemplate.query(sqlQuery, this::mapRow);
        } catch (NotFoundException e) {
            log.debug("Пользователь не найден: {}", users.get(userId));
            throw new NotFoundException(Constants.userNotFound);
        }
    }

    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("USER_ID"));
        user.setEmail(rs.getString("email"));
        user.setLogin(rs.getString("login"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }
}