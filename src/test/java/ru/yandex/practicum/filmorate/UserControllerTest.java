package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;

import static ru.yandex.practicum.filmorate.controller.UserController.users;

public class UserControllerTest {

    @BeforeEach
    void beforeEach() throws ValidationException {
        users.clear();
        UserController.idGenerator = 1;
        User user1 = new User();
        user1.setEmail("user1@user.com");
        user1.setLogin("User1");
        user1.setName("User1 User1");
        user1.setBirthday("1991-01-01");
        User user2 = new User();
        user2.setEmail("user2@user.com");
        user2.setLogin("User2");
        user2.setName("User2 User2");
        user2.setBirthday("1992-02-02");
        UserController.create(user1);
        UserController.create(user2);
    }

    @Test
    @DisplayName("Проверка GET запроса")
    void getUsersTest() throws ValidationException {
        HashMap<Integer, User> users = UserController.findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 2),
                () -> Assertions.assertEquals(users.get(2).getName(), "User2 User2")
        );
    }

    @Test
    @DisplayName("Проверка POST запроса")
    void postUserTest() throws ValidationException {
        User user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday("1993-03-03");
        UserController.create(user3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 3),
                () -> Assertions.assertEquals(users.get(user3.getId()).getName(), "User3 User3")
        );
        users.remove(user3.getId());
    }

    @Test
    @DisplayName("Проверка PUT запроса")
    void putUserTest() throws ValidationException {
        User user3 = new User();
        user3.setId(2);
        user3.setEmail("user3@user.com");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday("1993-03-03");
        UserController.update(user3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 2),
                () -> Assertions.assertEquals(users.get(2).getName(), "User3 User3")
        );
    }

    @Test
    @DisplayName("Проверка создания с пустыми именем")
    void createWithNullNameTest() throws ValidationException {
        User user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin("User3");
        user3.setName(" ");
        user3.setBirthday("1993-03-03");

        UserController.create(user3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 3),
                () -> Assertions.assertEquals(users.get(user3.getId()).getName(), "User3")
        );
        users.remove(user3.getId());
    }

    @Test
    @DisplayName("Проверка создания с пустым email")
    void createWithNullEmailTest() {
        User user3 = new User();
        user3.setEmail(" ");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday("1993-03-03");

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> UserController.create(user3));
        Assertions.assertEquals("электронная почта не может быть пустой и должна содержать символ '@'", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 2)
        );
    }

    @Test
    @DisplayName("Проверка создания с пустым логином")
    void createWithNullLoginTest() {
        User user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin(" ");
        user3.setName("User3 User3");
        user3.setBirthday("1993-03-03");

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> UserController.create(user3));
        Assertions.assertEquals("логин не может быть пустым и содержать пробелы", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 2)
        );
    }

    @Test
    @DisplayName("Проверка создания с неправильной датой рождения")
    void createWithIncorrectBirthdayTest() {
        User user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday("2222-03-03");

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> UserController.create(user3));
        Assertions.assertEquals("дата рождения не может быть в будущем", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 2)
        );
    }
}
