package ru.itis.flavorful_book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.itis.flavorful_book.entity.Category;
import ru.itis.flavorful_book.entity.Ingredient;
import ru.itis.flavorful_book.security.CustomeUserDetails;
import ru.itis.flavorful_book.service.CategoryService;
import ru.itis.flavorful_book.service.IngredientService;

import java.util.List;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAdvice {

    private final CategoryService categoryService;
    private final IngredientService ingredientService;

    @ModelAttribute("currentUser")
    public CustomeUserDetails currentUser(@AuthenticationPrincipal CustomeUserDetails user) {
        return user;
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        return categoryService.findAll();
    }

    @ModelAttribute("ingredients")
    public List<Ingredient> ingredients() {
        return ingredientService.findAll();
    }
}
