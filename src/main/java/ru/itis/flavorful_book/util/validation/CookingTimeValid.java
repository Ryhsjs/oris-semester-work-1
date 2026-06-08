package ru.itis.flavorful_book.util.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CookingTimeValidator.class)
public @interface CookingTimeValid {
    String message() default "Активное время не может превышать общее";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
