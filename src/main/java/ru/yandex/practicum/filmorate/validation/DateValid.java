package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CapitalLetterValidator.class)
@Documented
public @interface DateValid {

    String message() default "{DateValid.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

