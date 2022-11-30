package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.film.FilmStorage.films;
import static ru.yandex.practicum.filmorate.storage.user.UserStorage.users;

public class FilmControllerTest {

    static UserService userService = new UserService();
    static UserController userController = new UserController(userService);
    static InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    static FilmService filmService = new FilmService();
    static FilmController filmController = new FilmController(filmService, inMemoryFilmStorage);

    @BeforeEach
    void beforeEach() throws ValidationException {
        films.clear();
        users.clear();
        filmService.resetIdGenerator();
        userService.resetIdGenerator();
        Film film1 = new Film();
        film1.setName("Фильм 1");
        film1.setDescription("Описание фильма 1");
        film1.setReleaseDate(LocalDate.parse("1991-01-01"));
        film1.setDuration(101);
        Film film2 = new Film();
        film2.setName("Фильм 2");
        film2.setDescription("Описание фильма 2");
        film2.setReleaseDate(LocalDate.parse("1992-02-02"));
        film2.setDuration(102);
        filmController.createFilm(film1);
        filmController.createFilm(film2);

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
    void getFilmsTest() {
        List<Film> films = filmController.findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2),
                () -> Assertions.assertEquals(films.get(1).getName(), "Фильм 2")
        );
    }

    @Test
    @DisplayName("Проверка GET запроса по id")
    void getFilmsByIdTest() {
        Film film = filmController.getFilm(2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(film.getName(), "Фильм 2")
        );
        final NotFoundException error = Assertions.assertThrows(
                NotFoundException.class,
                () -> filmController.getFilm(99));
        Assertions.assertEquals("Фильм не найден", error.getMessage());

    }

    @Test
    @DisplayName("Проверка добавления лайка")
    void addLikeTest() {
        filmController.addLike(1, 1);
        filmController.addLike(1, 2);
        filmController.addLike(2, 2);
        filmController.addLike(2, 2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.get(1).getLikes().size(), 2),
                () -> Assertions.assertEquals(films.get(2).getLikes().size(), 1),
                () -> Assertions.assertTrue(films.get(1).getLikes().contains(users.get(1).getId())),
                () -> Assertions.assertTrue(films.get(2).getLikes().contains(users.get(2).getId()))
        );
    }

    @Test
    @DisplayName("Проверка удаления лайка")
    void deleteLikeTest() {
        filmController.addLike(1, 1);
        filmController.addLike(1, 2);
        filmController.addLike(2, 2);
        filmController.addLike(2, 2);
        filmController.deleteLike(1, 1);
        filmController.deleteLike(2, 2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.get(1).getLikes().size(), 1),
                () -> Assertions.assertEquals(films.get(2).getLikes().size(), 0),
                () -> Assertions.assertTrue(films.get(1).getLikes().contains(users.get(2).getId()))
        );
    }

    @Test
    @DisplayName("Проверка запроса популярных фильмов")
    void getPopularFilmsTest() {
        filmController.addLike(1, 1);
        filmController.addLike(1, 2);
        filmController.addLike(2, 2);
        filmController.addLike(2, 2);
        Assertions.assertAll(
                () -> Assertions.assertEquals(filmController.getPopularFilms(5).size(), 2),
                () -> Assertions.assertTrue(filmController.getPopularFilms(5).contains(films.get(2)))
        );
    }

    @Test
    @DisplayName("Проверка POST запроса")
    void postFilmTest() throws ValidationException {
        Film film3 = new Film();
        film3.setName("Фильм 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate(LocalDate.parse("1993-03-03"));
        film3.setDuration(103);
        filmController.createFilm(film3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 3),
                () -> Assertions.assertEquals(films.get(3).getName(), "Фильм 3")
        );
        films.remove(3);
    }

    @Test
    @DisplayName("Проверка PUT запроса")
    void putFilmTest() throws ValidationException {
        Film film3 = new Film();
        film3.setId(2);
        film3.setName("Фильм 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate(LocalDate.parse("1993-03-03"));
        film3.setDuration(103);
        filmController.updateFilm(film3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2),
                () -> Assertions.assertEquals(films.get(2).getName(), "Фильм 3")
        );
    }

    @Test
    @DisplayName("Проверка создания с ранней датой релиза")
    void createWithEarlyReleaseDateTest() {
        Film film3 = new Film();
        film3.setName("Фильм 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate(LocalDate.parse("1895-12-27"));
        film3.setDuration(103);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film3));
        Assertions.assertEquals("дата релиза — не раньше 28 декабря 1895 года", qwe.getParameter());
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2)
        );
    }
}

















/*
    @Test
    @DisplayName("Проверка создания с пустыми полями")
    void createWithNullNameTest() {
        Film film3 = new Film();
        film3.setName(" ");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate(LocalDate.parse("1993-03-03"));
        film3.setDuration(103);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film3));
        Assertions.assertEquals("название не может быть пустым", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2)
        );
    }

    @Test
    @DisplayName("Проверка создания с длинным описанием")
    void createWithLongDescriptionTest() {
        Film film3 = new Film();
        film3.setName("Фильм 3");
        film3.setDescription("Описание фильма 33333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333");
        film3.setReleaseDate(LocalDate.parse("1993-03-03"));
        film3.setDuration(103);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.createFilm(film3));
        Assertions.assertEquals("максимальная длина описания — 200 символов", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2)
        );
    }

    @Test
    @DisplayName("Проверка создания с отрицательной продолжительностью")
    void createWithMinusDurationTest() {
        Film film3 = new Film();
        film3.setName("Фильм 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate(LocalDate.parse("1993-03-03"));
        film3.setDuration(-1);

        final MethodArgumentNotValidException qwe = Assertions.assertThrows(
                MethodArgumentNotValidException.class,
                () -> filmController.createFilm(film3));
        Assertions.assertEquals("продолжительность фильма должна быть положительной", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2)
        );
    }*/

