package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.entity.Category;

import java.util.List;

public interface CategoryService {
    void save(Long recipeId, Long category);

    void saveAll(Long recipeId, List<Long> categories);

    void delete(Long recipeId, Long category);

    void deleteAll(Long recipeId, List<Long> categories);

    List<Category> findAll();

    List<Category> findAllByRecipeId(Long recipeId);
}
