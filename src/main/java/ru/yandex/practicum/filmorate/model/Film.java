package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Film {
    private int id;
    @NotNull
    private String name;
    private String description;
    private String releaseDate;
    private Double duration;

}
