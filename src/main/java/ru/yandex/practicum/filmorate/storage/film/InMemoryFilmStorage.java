package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected static int idGenerator = 1;


    public Film addFilm(Film film) throws ValidationException {
        logger.debug("Добавление фильма: {}", film.getName());
        int id = idGenerator++;
        film.setId(id);
        return getFilm(film);
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        logger.debug("Обровление фильма: {}", film.getName());
        for (int id : films.keySet()) {
            if (film.getId() == id) {
                return getFilm(film);
            }
        }
        logger.debug("такого фильма нет");
        throw new NotFoundException("такого фильма нет");
    }

    public List<Film> findAll() {
        logger.debug("Текущее количество пользователей: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Validated
    private Film getFilm(@Valid @NotNull Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            idGenerator--;
            logger.debug("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else {
            films.put(film.getId(), film);
            logger.info("Сохранен фильм: {}", film.getId());
            return film;
        }
    }

}
