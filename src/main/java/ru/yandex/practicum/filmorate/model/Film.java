package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Validated
public class Film {
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Length(max = 200)
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть положительной")
    private int duration;

    private final Set<Integer> likes = new LinkedHashSet<>();

    public void addLike(int userId) {
        likes.add(userId);
    }

}
