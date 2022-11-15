package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

import static ru.yandex.practicum.filmorate.controller.FilmController.films;

public class FilmControllerTest {

    @BeforeAll
    static void beforeAll() throws ValidationException {
        Film film1 = new Film();
        film1.setName("Фильм 1");
        film1.setDescription("Описание фильма 1");
        film1.setReleaseDate("1991-01-01");
        film1.setDuration(101.0);
        Film film2 = new Film();
        film2.setName("Фильм 2");
        film2.setDescription("Описание фильма 2");
        film2.setReleaseDate("1992-02-02");
        film2.setDuration(102.0);
        FilmController.create(film1);
        FilmController.create(film2);
    }

    @Test
    @DisplayName("Проверка GET запроса")
    void getFilmsTest() throws ValidationException {
        HashMap<Integer, Film> films = FilmController.findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2),
                () -> Assertions.assertEquals(films.get(2).getName(), "Фильм 2")
        );
    }

    @Test
    @DisplayName("Проверка POST запроса")
    void postFilmTest() throws ValidationException {
        Film film3 = new Film();
        film3.setName("Фильм 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate("1993-03-03");
        film3.setDuration(103.0);
        FilmController.create(film3);
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
        film3.setReleaseDate("1993-03-03");
        film3.setDuration(103.0);
        FilmController.update(film3);
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2),
                () -> Assertions.assertEquals(films.get(2).getName(), "Фильм 3")
        );
    }

    @Test
    @DisplayName("Проверка создания с пустыми полями")
    void createWithNullNameTest() {
        Film film3 = new Film();
        film3.setName(" ");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate("1993-03-03");
        film3.setDuration(103.0);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> FilmController.create(film3));
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
        film3.setReleaseDate("1993-03-03");
        film3.setDuration(103.0);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> FilmController.create(film3));
        Assertions.assertEquals("максимальная длина описания — 200 символов", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2)
        );
    }

    @Test
    @DisplayName("Проверка создания с ранней датой релиза")
    void createWithEarlyReleaseDateTest() {
        Film film3 = new Film();
        film3.setName("Фильм 3");
        film3.setDescription("Описание фильма 3");
        film3.setReleaseDate("1895-12-27");
        film3.setDuration(103.0);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> FilmController.create(film3));
        Assertions.assertEquals("дата релиза — не раньше 28 декабря 1895 года", qwe.getMessage());
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
        film3.setReleaseDate("1993-03-03");
        film3.setDuration(-1.0);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> FilmController.create(film3));
        Assertions.assertEquals("продолжительность фильма должна быть положительной", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2)
        );
    }
}