package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.Constants;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        log.debug("Добавление фильма: {}", film.getName());
        jdbcTemplate.update("insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA) " +
                        "values (?, ?, ?, ?, ?, ?)",
                film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getRate(), film.getMpa().getId());
        Film film2 = jdbcTemplate.queryForObject("select * from FILMS where FILM_NAME = ?", this::mapRow, film.getName());
        updateGenre(film);
        assert film2 != null;
        film2.setGenres(findGenresByFilmId(film.getId()));
        updateGenre(film2);
        jdbcTemplate.update("insert into LIKES values (?, ?, ?)", film2.getId(), null, film.getRate());
        return film2;
    }

    public Film findOne(int id) {
        log.debug("Поиск фильма с ID : {}", id);
        String sqlQuery = "select FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA " +
                "from FILMS where FILM_ID = ?";
        List<Film> film = jdbcTemplate.query(sqlQuery, this::mapRow, id);
        if (film.size() != 1) {
            log.debug("Фильм не найден: {}", id);
            throw new NotFoundException(Constants.filmNotFound);
        }
        film.get(0).setGenres(findGenresByFilmId(film.get(0).getId()));
        return film.get(0);
    }

    public List<Film> findAll() {
        String sqlQuery = "select * from FILMS";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::mapRow);
        for (Film film : films) {
            film.setGenres(findGenresByFilmId(film.getId()));
        }
        log.debug("Поиск всех фильмов: {}", films.size());
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Обновление фильма с ID : {}", film.getId());
        try {
            findOne(film.getId());
            String sqlQuery = "update FILMS set " +
                    "FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ?, MPA = ? " +
                    "where FILM_ID = ?";
            jdbcTemplate.update(sqlQuery,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getRate(),
                    film.getMpa().getId(),
                    film.getId());
            film.setMpa(findMpa(film.getMpa().getId()));
            jdbcTemplate.update("insert into LIKES values (?, ?, ?)", film.getId(), null, film.getRate());
            updateGenre(film);
            film.setGenres(findGenresByFilmId(film.getId()));
            return film;
        } catch (NotFoundException e) {
            log.debug("Фильм не найден: {}", film.getId());
            throw new NotFoundException(Constants.filmNotFound);
        }
    }

    public Film addLike(int filmId, int userId) {
        log.debug("Добавление лайка фильму с ID : {}", filmId);
        try {
            Film film = findOne(filmId);
            findOne(userId);
            jdbcTemplate.update("insert into LIKES values (?, ?, ?)", filmId, userId, 1);
            film.setRate(film.getRate() + 1);
            return film;

        } catch (NotFoundException e) {
            log.debug("Фильм или юзер не найдены: {}", filmId);
            throw new NotFoundException(Constants.filmOrUserNotFound);
        }
    }

    public boolean deleteLike(int filmId, int userId) {
        log.debug("Удаление лайка фильму с ID : {}", filmId);
        try {
            Film film = findOne(filmId);
            findOne(userId);
            String sqlQuery = "delete from LIKES where FILM_ID = ? AND USER_ID = ?";
            film.setRate(film.getRate() - 1);
            return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
        } catch (NotFoundException e) {
            log.debug("Фильм или юзер не найдены: {}", filmId);
            throw new NotFoundException(Constants.filmOrUserNotFound);
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        log.debug("Текущее количество фильмов: {}", films.size());
        String sqlQuery = "select FILMS.FILM_ID, FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, SUM(LIKES.RATE) AS RATE, MPA " +
                "FROM FILMS " +
                "join LIKES on FILMS.FILM_ID = LIKES.FILM_ID " +
                "group by FILMS.FILM_ID " +
                "ORDER BY SUM(LIKES.RATE) DESC";
        List<Film> popularFilms = jdbcTemplate.query(sqlQuery, this::mapRow);
        return popularFilms.stream()
                .limit(Objects.requireNonNullElse(count, 10))
                .collect(Collectors.toList());
    }

    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getInt("DURATION"));
        film.setRate(rs.getInt("RATE"));
        film.setMpa(findMpa(rs.getInt("MPA")));
        return film;
    }

    public Mpa findMpa(int id) {
        log.debug("Поиск МРА: {}", id);
        String sqlQuery = "select MPA_ID, MPA " +
                "from MPA where MPA_ID = ?";
        List<Mpa> mpa = jdbcTemplate.query(sqlQuery, this::mapRowMpa, id);
        if (mpa.size() != 1) {
            log.debug("МРА не найден: {}", id);
            throw new NotFoundException(Constants.filmNotFound);
        }
        return mpa.get(0);
    }

    public List<Mpa> findAllMpa() {
        String sqlQuery = "select * from MPA";
        List<Mpa> mpa = jdbcTemplate.query(sqlQuery, this::mapRowMpa);
        log.debug("Поиск МРА: {}", mpa.size());
        return mpa;
    }

    public Genre findGenre(int id) {
        log.debug("Поиск жанра: {}", id);
        String sqlQuery = "select GENRE_ID, GENRE " +
                "from GENRE where GENRE_ID = ?";
        List<Genre> genre = jdbcTemplate.query(sqlQuery, this::mapRowGenre, id);
        if (genre.size() != 1) {
            log.debug("Жанр не найден: {}", id);
            throw new NotFoundException(Constants.filmNotFound);
        }
        return genre.get(0);
    }

    public List<Genre> findGenresByFilmId(int filmId) {
        log.debug("Поиск жанров по фильму: {}", filmId);
        String sqlQuery = "select DISTINCT GENRE.GENRE_ID, GENRE.GENRE " +
                "FROM FILM_GENRE " +
                "JOIN GENRE ON FILM_GENRE.GENRE_ID = GENRE.GENRE_ID " +
                "WHERE FILM_GENRE.FILM_ID = " + filmId;
        return jdbcTemplate.query(sqlQuery, this::mapRowGenre);
    }

    public List<Genre> findAllGenre() {
        String sqlQuery = "select * from GENRE";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, this::mapRowGenre);
        log.debug("Поиск жанров по фильму: {}", genres);
        return genres;
    }

    public void updateGenre(Film film) {
        log.debug("Обновление жанров по фильму: {}", film.getId());
        String sqlQuery = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update("insert into FILM_GENRE (FILM_ID, GENRE_ID) " + "values (?, ?)", film.getId(), genre.getId());
        }
        film.setGenres(findGenresByFilmId(film.getId()));
    }

    public Mpa mapRowMpa(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(rs.getInt("MPA_ID"));
        mpa.setMpa(rs.getString("MPA"));
        return mpa;
    }

    public Genre mapRowGenre(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("GENRE_ID"));
        genre.setGenre(rs.getString("GENRE"));
        return genre;
    }
}
