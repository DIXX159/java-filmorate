package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.yandex.practicum.filmorate.storage.user.UserStorage.users;


/*public class UserControllerTest {


    static UserService userService = new UserService();
    @MockBean
    static UserController userController = new UserController(userService);

    @BeforeEach
    void beforeEach() {
        users.clear();
        userService.resetIdGenerator();
        User user1 = new User();
        user1.setEmail("user1@user.com");
        user1.setLogin("User1");
        user1.setName("User1 User1");
        user1.setBirthday(LocalDate.parse("1991-01-01"));
        User user2 = new User();
        user2.setEmail("user2@user.com");
        user2.setLogin("User2");
        user2.setName("User2 User2");
        user2.setBirthday(LocalDate.parse("1992-02-02"));
        userController.create(user1);
        userController.create(user2);
    }

    @Test
    @DisplayName("Проверка GET запроса")
    void getUsersTest() {
        List<User> users = userController.findAll();
        Assertions.assertAll(
                () -> assertEquals(users.size(), 2),
                () -> assertEquals(users.get(1).getName(), "User2 User2")
        );
    }

    @Test
    @DisplayName("Проверка GET запроса по id")
    void getUserByIdTest() {
        User user = userController.getUser(2);
        Assertions.assertAll(
                () -> assertEquals(user.getName(), "User2 User2")
        );
        final NotFoundException error = Assertions.assertThrows(
                NotFoundException.class,
                () -> userController.getUser(99));
        assertEquals("Пользователь не найден", error.getMessage());
    }

    @Test
    @DisplayName("Проверка добавления друзей")
    void addFriendTest() {
        userController.addFriend(1, 2);
        Assertions.assertAll(
                () -> Assertions.assertTrue(users.get(1).getFriends().contains(users.get(2).getId())),
                () -> Assertions.assertTrue(users.get(2).getFriends().contains(users.get(1).getId()))
        );
    }

    @Test
    @DisplayName("Проверка удаления друзей")
    void deleteFriendTest() {
        userController.addFriend(1, 2);
        userController.deleteFriend(1, 2);
        Assertions.assertAll(
                () -> assertEquals(users.get(1).getFriends().size(), 0),
                () -> assertEquals(users.get(2).getFriends().size(), 0)
        );
        final NotFoundException error = Assertions.assertThrows(
                NotFoundException.class,
                () -> userController.deleteFriend(88, 99));
        assertEquals("Пользователь не найден", error.getMessage());
    }

    @Test
    @DisplayName("Проверка запроса всех друзей")
    void getFriendsTest() {
        User user3 = new User();
        user3.setEmail("user@user.com");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday(LocalDate.parse("1993-03-03"));
        userController.create(user3);
        userController.addFriend(1, 2);
        userController.addFriend(1, 3);
        Assertions.assertAll(
                () -> assertEquals(userController.getAllFriends(1).size(), 2),
                () -> Assertions.assertTrue(userController.getAllFriends(1).contains(users.get(3)))
        );
        final NotFoundException error = Assertions.assertThrows(
                NotFoundException.class,
                () -> userController.getAllFriends(99));
        assertEquals("Пользователь не найден", error.getMessage());
    }

    @Test
    @DisplayName("Проверка запроса общих друзей")
    void getCommonFriendsTest() {
        User user3 = new User();
        user3.setEmail("user@user.com");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday(LocalDate.parse("1993-03-03"));
        userController.create(user3);
        userController.addFriend(1, 2);
        userController.addFriend(2, 3);
        Assertions.assertAll(
                () -> assertEquals(userController.getCommonFriends(1, 3).size(), 1),
                () -> Assertions.assertTrue(userController.getCommonFriends(1, 3).contains(users.get(2)))
        );
        final NotFoundException error = Assertions.assertThrows(
                NotFoundException.class,
                () -> userController.getCommonFriends(88, 99));
        assertEquals("Пользователь не найден", error.getMessage());
    }

    @Test
    @DisplayName("Проверка POST запроса")
    void postUserTest() {
        User user3 = new User();
        user3.setEmail("user3user.com@");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday(LocalDate.parse("1993-03-03"));
        userController.create(user3);
        Assertions.assertAll(
                () -> assertEquals(users.size(), 3),
                () -> assertEquals(users.get(user3.getId()).getName(), "User3 User3")
        );
        users.remove(user3.getId());
    }

    @Test
    @DisplayName("Проверка PUT запроса")
    void putUserTest() {
        User user3 = new User();
        user3.setId(2);
        user3.setEmail("user3@user.com");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday(LocalDate.parse("1993-03-03"));
        userController.update(user3);
        Assertions.assertAll(
                () -> assertEquals(users.size(), 2),
                () -> assertEquals(users.get(user3.getId()).getName(), "User3 User3")
        );
    }

    @Test
    @DisplayName("Проверка создания с пустыми именем")
    void createWithNullNameTest() {
        User user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin("User3");
        user3.setName("");
        user3.setBirthday(LocalDate.parse("1993-03-03"));

        userController.create(user3);
        Assertions.assertAll(
                () -> assertEquals(users.size(), 3),
                () -> assertEquals(users.get(user3.getId()).getName(), "User3")
        );
        users.remove(user3.getId());
    }
}
*/















  /*  @Test
    @Valid
    @DisplayName("Проверка создания с пустым email")
    void createWithNullEmailTest() {
        User user3 = new User();
        user3.setEmail("ы");
        user3.setLogin("User3");
        user3.setName("User3 User3");
        user3.setBirthday(LocalDate.parse("1993-03-03"));
        userController.create(user3);
        System.out.println(user3);

       /* final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user3));
        Assertions.assertEquals("электронная почта не может быть пустой и должна содержать символ '@'", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 2)
        );
    }*/

/*
    @Test
    @DisplayName("Проверка создания с пустым логином")
    void createWithNullLoginTest() {
        User user3 = new User();
        user3.setEmail("user3@user.com");
        user3.setLogin(" ");
        user3.setName("User3 User3");
        user3.setBirthday(LocalDate.parse("1993-03-03"));

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user3));
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
        user3.setBirthday(LocalDate.parse("2222-03-03"));

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> userController.create(user3));
        Assertions.assertEquals("дата рождения не может быть в будущем", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(users.size(), 2)
        );
    }*/

