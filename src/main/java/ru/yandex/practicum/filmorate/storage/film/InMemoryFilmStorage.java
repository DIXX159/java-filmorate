package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    protected static int idGenerator = 1;

    public void resetIdGenerator() {
        idGenerator = 1;
    }

    public Film addFilm(Film film) throws ValidationException {
        log.debug("Добавление фильма: {}", film.getName());
        int id = idGenerator++;
        film.setId(id);
        return getFilm(film);
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        log.debug("Обровление фильма: {}", film.getName());
        for (int id : films.keySet()) {
            if (film.getId() == id) {
                return getFilm(film);
            }
        }
        log.debug("такого фильма нет");
        throw new NotFoundException(Constants.filmNotFound);
    }

    public List<Film> findAll() {
        log.debug("Текущее количество пользователей: {}", films.size());
        return new ArrayList<>(films.values());
    }


    private Film getFilm(@Valid @NotNull Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            idGenerator--;
            log.debug("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else {
            films.put(film.getId(), film);
            log.info("Сохранен фильм: {}", film.getId());
            return film;
        }
    }
}
