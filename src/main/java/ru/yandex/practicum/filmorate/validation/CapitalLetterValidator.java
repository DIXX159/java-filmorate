package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CapitalLetterValidator implements ConstraintValidator<DateValid, LocalDate> {

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext context) {
        return !releaseDate.isBefore(LocalDate.of(1895, 12, 28));
    }
}
