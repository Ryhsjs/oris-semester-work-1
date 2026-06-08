package ru.itis.flavorful_book.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, Object> {

    private String passwordField;
    private String passwordRepeatField;

    @Override
    public void initialize(PasswordsMatch annotation) {
        passwordField = annotation.password();
        passwordRepeatField = annotation.passwordRepeat();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapper wrapper = new BeanWrapperImpl(value);
        String password = (String) wrapper.getPropertyValue(passwordField);
        String repeat = (String) wrapper.getPropertyValue(passwordRepeatField);

        boolean valid = password != null && password.equals(repeat);
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(passwordRepeatField)
                    .addConstraintViolation();
        }
        return valid;
    }
}
