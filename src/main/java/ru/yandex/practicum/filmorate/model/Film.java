package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
public class Film {
    private int id;
    @NotBlank(message = "!дата рождения не может быть пустой")
    private String name;
    private String description;
    private String releaseDate;
    private Double duration;

}
