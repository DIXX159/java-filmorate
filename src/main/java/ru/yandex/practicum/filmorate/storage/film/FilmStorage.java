package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {

    Film addFilm(Film film) throws ValidationException;

    void deleteFilm(int id);

    Film updateFilm(Film film) throws ValidationException;
}
