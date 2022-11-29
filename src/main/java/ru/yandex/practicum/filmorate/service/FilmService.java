package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends InMemoryFilmStorage {

    public Film findFilmById(int filmId) {
        log.debug("Поиск фильма: {}", films.get(filmId));
        if (films.get(filmId) != null) {
            return films.get(filmId);
        }
        log.debug("Фильм не найден: {}", films.get(filmId));
        throw new NotFoundException(Constants.filmNotFound);
    }

    public Film addLike(int filmId, int userId) {
        if (films.get(filmId) != null && InMemoryUserStorage.getUsers().get(userId) != null) {
            log.debug("Cтавим лайк фильму: {}", films.get(filmId));
            films.get(filmId).addLike(userId);
            return films.get(filmId);
        }
        log.debug("Фильм не найден: {}", films.get(filmId));
        throw new NotFoundException(Constants.filmNotFound);
    }

    public Film deleteLike(int filmId, int userId) {
        if (films.get(filmId) != null && InMemoryUserStorage.getUsers().get(userId) != null) {
            log.debug("Удаление лайка фильму: {}", films.get(filmId));
            films.get(filmId).getLikes().remove(userId);
            return films.get(filmId);
        }
        log.debug("Фильм не найден: {}", films.get(filmId));
        throw new NotFoundException(Constants.filmNotFound);
    }

    public Set<Film> getPopularFilms(Integer count) {
        log.debug("Список из фильмов: {}", count);
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
