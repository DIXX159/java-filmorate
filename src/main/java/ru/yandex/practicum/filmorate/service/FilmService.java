package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;


@Service
public class FilmService extends InMemoryFilmStorage {

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
    }

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public Film findFilmById(int filmId) {
        if (films.get(filmId) != null) {
            return films.get(filmId);
        }
        throw new NotFoundException("Фильм не найден");
    }
}
