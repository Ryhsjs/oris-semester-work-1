package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.dto.RecipeDTO;
import ru.itis.flavorful_book.dto.RecipeInfoDTO;
import ru.itis.flavorful_book.dto.RecipePreviewDTO;
import ru.itis.flavorful_book.entity.Recipe;
import ru.itis.flavorful_book.form.RecipeForm;

import java.util.List;

public interface RecipeService {
    Recipe getEntityById(Long id);

    Long create(RecipeForm form, Long userId);

    void update(Long id, RecipeForm form, Long userId);

    void updateViews(Long id);

    void deleteById(Long id, Long userId);

    boolean isOwner(Long recipeId, Long userId);

    RecipeDTO findByIdDTO(Long id);

    RecipeInfoDTO findByIdInfoDTO(Long id);

    List<RecipePreviewDTO> findAll();

    List<RecipePreviewDTO> findAll(List<Long> ingredients, boolean isStrictMatchIngredients,
                                   List<Long> categories, boolean isStrictMatchCategories,
                                   Integer activeStart, Integer activeEnd, Integer totalStart, Integer totalEnd);

    List<RecipePreviewDTO> findManyByUserFavorites(Long userId);

    List<RecipePreviewDTO> findManyByUserId(Long userId);

    void addToFavorites(Long userId, Long recipeId);

    void deleteFromFavorites(Long userId, Long recipeId);

    boolean isInFavorites(Long userId, Long recipeId);
}
