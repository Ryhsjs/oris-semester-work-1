package ru.itis.flavorful_book.service;

import ru.itis.flavorful_book.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    void save(Long userId, Long recipeId, Integer rating, String comment);

    void update(Long id, Long userId, Integer rating, String comment);

    void deleteById(Long id, Long userId);

    ReviewDTO findById(Long id);

    ReviewDTO findByUserIdRecipeId(Long userId, Long recipeId);

    List<ReviewDTO> findAllByRecipeId(Long recipeId);
}
