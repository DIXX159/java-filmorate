package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping()
public class FilmController {

    private final FilmDbStorage filmDbStorage;

    public FilmController(FilmDbStorage filmDbStorage) {
        this.filmDbStorage = filmDbStorage;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Поиск всех фильмов:");
        return filmDbStorage.findAll();
    }

    @GetMapping("/films/{filmId}")
    public Film getFilm(@PathVariable("filmId") Integer filmId) {
        log.info("Поиск фильма:");
        return filmDbStorage.findOne(filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Integer count) {
        log.info("Поиск популярных фильмов:");
        return filmDbStorage.getPopularFilms(count);
    }

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Добавление фильма:");
        return filmDbStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма:");
        return filmDbStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Добавление лайка фильму:");
        return filmDbStorage.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Boolean deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        log.info("Удаление лайка фильму:");
        return filmDbStorage.deleteLike(id, userId);
    }

    @GetMapping("/mpa/{mpaId}")
    public Mpa findMpa(@PathVariable("mpaId") Integer mpaId) {
        log.info("Поиск MPA:");
        return filmDbStorage.findMpa(mpaId);
    }

    @GetMapping("/mpa")
    public List<Mpa> findAllMpa() {
        log.info("Поиск всех MPA:");
        return filmDbStorage.findAllMpa();
    }

    @GetMapping("/genres/{genreId}")
    public Genre findGenre(@PathVariable("genreId") Integer genreId) {
        log.info("Поиск жанра:");
        return filmDbStorage.findGenre(genreId);
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenre() {
        log.info("Поиск всех жанров:");
        return filmDbStorage.findAllGenre();
    }
}
