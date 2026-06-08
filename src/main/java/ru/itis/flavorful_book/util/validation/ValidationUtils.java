package ru.itis.flavorful_book.util.validation;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Map;
import java.util.stream.Collectors;

public class ValidationUtils {

    private ValidationUtils() {}

    public static Map<String, String> extractFieldErrors(BindingResult errors) {
        return errors.getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Ошибка",
                        (a, b) -> a
                ));
    }
}
