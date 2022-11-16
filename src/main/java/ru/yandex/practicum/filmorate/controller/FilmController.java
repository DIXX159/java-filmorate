package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    public static final HashMap<Integer, Film> films = new HashMap<>();
    protected static int idGenerator = 1;
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");

    @GetMapping("/films")
    public static List<Film> findAll() throws ValidationException {
        log.debug("Текущее количество пользователей: {}", films.size());
        if (!films.isEmpty()) {
            return new ArrayList<>(films.values());
        }
        throw new ValidationException("список фильмов пуст");
    }

    @PostMapping(value = "/films")
    public static Film create(@Valid @RequestBody @NotNull Film film) throws ValidationException {
        int id = idGenerator++;
        film.setId(id);
        return getFilm(film);
    }

    @PutMapping(value = "/films")
    public static Film update(@Valid @RequestBody @NotNull Film film) throws ValidationException {
        for (int id : films.keySet()) {
            if (film.getId() == id) {
                return getFilm(film);
            }
        }
        throw new ValidationException("такого фильма нет");
    }

    private static Film getFilm(@Valid @RequestBody @NotNull Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            idGenerator--;
            log.info("название не может быть пустым");
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            idGenerator--;
            log.debug("максимальная длина описания — 200 символов");
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (LocalDate.parse(film.getReleaseDate(), formatter).isBefore(LocalDate.of(1895, 12, 28))) {
            idGenerator--;
            log.debug("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            idGenerator--;
            log.debug("продолжительность фильма должна быть положительной");
            throw new ValidationException("продолжительность фильма должна быть положительной");
        } else {
            films.put(film.getId(), film);
            log.info("Сохранен пользователь: {}", film.getId());
            return film;
        }
    }
}
