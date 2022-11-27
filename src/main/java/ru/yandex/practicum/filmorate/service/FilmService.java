package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService extends InMemoryFilmStorage {

    @Autowired
    public FilmService() {
    }

    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public Film findFilmById(int filmId) {
        logger.debug("Поиск фильма: {}", films.get(filmId));
        if (films.get(filmId) != null) {
            return films.get(filmId);
        }
        logger.debug("Фильм не найден: {}", films.get(filmId));
        throw new NotFoundException("Фильм не найден");
    }

    public Film addLike(int filmId, int userId) {
        if (films.get(filmId) != null & InMemoryUserStorage.getUsers().get(userId) != null) {
            logger.debug("Cтавим лайк фильму: {}", films.get(filmId));
            films.get(filmId).addLike(userId);
            return films.get(filmId);
        }
        logger.debug("Фильм не найден: {}", films.get(filmId));
        throw new NotFoundException("Фильм не найден");
    }

    public Film deleteLike(int filmId, int userId) {
        if (films.get(filmId) != null & InMemoryUserStorage.getUsers().get(userId) != null) {
            logger.debug("Удаление лайка фильму: {}", films.get(filmId));
            films.get(filmId).getLikes().remove(userId);
            return films.get(filmId);
        }
        logger.debug("Фильм не найден: {}", films.get(filmId));
        throw new NotFoundException("Фильм не найден");
    }

    public Set<Film> getPopularFilms(Integer count) {
        logger.debug("Список из фильмов: {}", count);
        List<Film> popularFilm = new ArrayList<>(List.copyOf(films.values()));
        popularFilm.sort(new MyComparator());
        return popularFilm.stream()
                .limit(Objects.requireNonNullElse(count, 10))
                .collect(Collectors.toSet());
    }

    static class MyComparator implements Comparator<Film> {

        @Override
        public int compare(Film o1, Film o2) {
            return Integer.compare(o2.getLikes().size(), o1.getLikes().size());
        }
    }

}
