package ru.itis.flavorful_book.util.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.itis.flavorful_book.form.RecipeForm;

public class CookingTimeValidator implements ConstraintValidator<CookingTimeValid, RecipeForm> {

    @Override
    public boolean isValid(RecipeForm form, ConstraintValidatorContext context) {
        Integer active = form.getActiveCookingTime();
        Integer total = form.getTotalCookingTime();
        if (active == null || total == null) {
            return true; // @NotNull на полях отловит это отдельно
        }
        boolean valid = active <= total;
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("activeCookingTime")
                    .addConstraintViolation();
        }
        return valid;
    }
}
