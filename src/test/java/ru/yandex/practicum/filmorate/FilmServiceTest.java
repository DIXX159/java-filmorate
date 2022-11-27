/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.storage.film.FilmStorage.films;


public class FilmServiceTest {

    static InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    @BeforeAll
    static void beforeAll() throws ValidationException {
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
        inMemoryFilmStorage.addFilm(film1);
        inMemoryFilmStorage.addFilm(film2);
    }

    @Test
    @DisplayName("Проверка GET запроса")
    void getFilmsTest() {
        List<Film> films = inMemoryFilmStorage.findAll();
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2),
                () -> Assertions.assertEquals(films.get(1).getName(), "Фильм 2")
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
        inMemoryFilmStorage.addFilm(film3);
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
        inMemoryFilmStorage.updateFilm(film3);
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
        film3.setReleaseDate(LocalDate.parse("1993-03-03"));
        film3.setDuration(103);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> inMemoryFilmStorage.addFilm(film3));
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
                () -> inMemoryFilmStorage.addFilm(film3));
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
        film3.setReleaseDate(LocalDate.parse("1895-12-27"));
        film3.setDuration(103);

        final ValidationException qwe = Assertions.assertThrows(
                ValidationException.class,
                () -> inMemoryFilmStorage.addFilm(film3));
        Assertions.assertEquals("дата релиза — не раньше 28 декабря 1895 года", qwe.getParameter());
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
                () -> inMemoryFilmStorage.addFilm(film3));
        Assertions.assertEquals("продолжительность фильма должна быть положительной", qwe.getMessage());
        Assertions.assertAll(
                () -> Assertions.assertEquals(films.size(), 2)
        );
    }
}*/