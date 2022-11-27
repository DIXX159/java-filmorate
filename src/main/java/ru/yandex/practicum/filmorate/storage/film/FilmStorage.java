package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

public interface FilmStorage {

    HashMap<Integer, Film> films = new HashMap<>();

    Film addFilm(Film film) throws ValidationException;

    Film updateFilm(Film film) throws ValidationException;
}
