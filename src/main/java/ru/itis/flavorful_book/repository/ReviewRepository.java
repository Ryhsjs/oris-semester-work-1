package ru.itis.flavorful_book.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.flavorful_book.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @NotNull
    @EntityGraph(attributePaths = {"user", "recipe"})
    Optional<Review> findById(@NotNull Long id);

    @EntityGraph(attributePaths = "user")
    Optional<Review> findByUser_IdAndRecipe_Id(Long userId, Long recipeId);

    @EntityGraph(attributePaths = "user")
    List<Review> findAllByRecipe_Id(Long recipeId);

    boolean existsByUser_IdAndRecipe_Id(Long userId, Long recipeId);

    long countByRecipe_Id(Long recipeId);
}
