package ru.itis.flavorful_book.dto;

import ru.itis.flavorful_book.entity.Review;

import java.time.LocalDateTime;

public record ReviewDTO(
        Long id,
        Long userId,
        String recipeTitle,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String username,
        String userAvatarUrl
) {
    public static ReviewDTO from(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getUser().getId(),
                review.getRecipe().getTitle(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt(),
                review.getUser().getUsername(),
                review.getUser().getAvatarUrl()
        );
    }
}
