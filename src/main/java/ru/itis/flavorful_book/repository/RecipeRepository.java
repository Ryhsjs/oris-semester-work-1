package ru.itis.flavorful_book.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.entity.Recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long>, RecipeRepositoryCustom {

    @EntityGraph(attributePaths = "author")
    List<Recipe> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "author")
    List<Recipe> findAllByAuthor_IdOrderByCreatedAtDesc(Long authorId);

    @NotNull
    @EntityGraph(attributePaths = "author")
    Optional<Recipe> findById(@NotNull Long id);

    @EntityGraph(attributePaths = "author")
    @Query("SELECT DISTINCT r FROM User u JOIN u.favorites r JOIN FETCH r.author WHERE u.id = :userId ORDER BY r.createdAt DESC")
    List<Recipe> findAllFavoritesByUser(@Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("UPDATE Recipe r SET r.views = r.views + 1 WHERE r.id = :id")
    void incrementViews(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO favorites (user_id, recipe_id) VALUES (:userId, :recipeId)", nativeQuery = true)
    void addToFavorites(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM favorites WHERE user_id = :userId AND recipe_id = :recipeId", nativeQuery = true)
    void removeFromFavorites(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM favorites WHERE user_id = :userId AND recipe_id = :recipeId)", nativeQuery = true)
    boolean isInFavorites(@Param("userId") Long userId, @Param("recipeId") Long recipeId);

    @Query(value = "SELECT COUNT(*) FROM favorites WHERE recipe_id = :recipeId", nativeQuery = true)
    long countFavoritesByRecipeId(@Param("recipeId") Long recipeId);
}
