package ru.itis.flavorful_book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.flavorful_book.dto.RecipeDTO;
import ru.itis.flavorful_book.dto.RecipePreviewDTO;
import ru.itis.flavorful_book.entity.enums.Unit;
import ru.itis.flavorful_book.security.CustomeUserDetails;
import ru.itis.flavorful_book.service.CategoryService;
import ru.itis.flavorful_book.service.IngredientRecipeService;
import ru.itis.flavorful_book.service.RecipeService;
import ru.itis.flavorful_book.service.ReviewService;

import java.util.List;

@Controller
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final CategoryService categoryService;
    private final IngredientRecipeService ingredientRecipeService;
    private final ReviewService reviewService;

    @GetMapping
    public String recipeListPage(
            @RequestParam(required = false) List<Long> ingredientId,
            @RequestParam(required = false, defaultValue = "false") boolean isStrictMatchIngredients,
            @RequestParam(required = false) List<Long> categoryId,
            @RequestParam(required = false, defaultValue = "false") boolean isStrictMatchCategories,
            @RequestParam(required = false) Integer activeCookingTimeStart,
            @RequestParam(required = false) Integer activeCookingTimeEnd,
            @RequestParam(required = false) Integer totalCookingTimeStart,
            @RequestParam(required = false) Integer totalCookingTimeEnd,
            Model model) {

        boolean hasFilters = (ingredientId != null && !ingredientId.isEmpty())
                || (categoryId != null && !categoryId.isEmpty())
                || activeCookingTimeStart != null || activeCookingTimeEnd != null
                || totalCookingTimeStart != null || totalCookingTimeEnd != null;

        List<RecipePreviewDTO> recipes;
        if (hasFilters) {
            recipes = recipeService.findAll(
                    ingredientId != null ? ingredientId : List.of(), isStrictMatchIngredients,
                    categoryId != null ? categoryId : List.of(), isStrictMatchCategories,
                    activeCookingTimeStart, activeCookingTimeEnd,
                    totalCookingTimeStart, totalCookingTimeEnd);
        } else {
            recipes = recipeService.findAll();
        }

        model.addAttribute("recipes", recipes);
        return "recipes";
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("units", Unit.values());
        return "recipe-edit";
    }

    @GetMapping("/{id}")
    public String recipePage(@PathVariable Long id,
                             @AuthenticationPrincipal CustomeUserDetails currentUser,
                             Model model) {
        recipeService.updateViews(id);
        model.addAttribute("recipe", recipeService.findByIdInfoDTO(id));
        model.addAttribute("reviews", reviewService.findAllByRecipeId(id));
        model.addAttribute("recipeCategories", categoryService.findAllByRecipeId(id));
        model.addAttribute("recipeIngredients", ingredientRecipeService.findAllDTOByRecipeId(id));

        if (currentUser != null) {
            model.addAttribute("isFavorite", recipeService.isInFavorites(currentUser.getId(), id));
            model.addAttribute("userReview", reviewService.findByUserIdRecipeId(currentUser.getId(), id));
        }
        return "recipe-info";
    }

    @GetMapping("/{id}/edit")
    public String editPage(@PathVariable Long id,
                           @AuthenticationPrincipal CustomeUserDetails currentUser,
                           Model model) {
        if (!recipeService.isOwner(id, currentUser.getId())) {
            return "redirect:/recipes/" + id;
        }
        RecipeDTO recipe = recipeService.findByIdDTO(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("units", Unit.values());
        return "recipe-edit";
    }
}
