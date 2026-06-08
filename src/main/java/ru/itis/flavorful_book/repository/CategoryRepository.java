package ru.itis.flavorful_book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.entity.Category;

import java.util.Collection;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Recipe r JOIN r.categories c WHERE r.id = :recipeId")
    List<Category> findAllByRecipeId(@Param("recipeId") Long recipeId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO recipe_categories (recipe_id, category_id) VALUES (:recipeId, :categoryId) ON CONFLICT DO NOTHING", nativeQuery = true)
    void addToRecipe(@Param("recipeId") Long recipeId, @Param("categoryId") Long categoryId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM recipe_categories WHERE recipe_id = :recipeId AND category_id = :categoryId", nativeQuery = true)
    void removeFromRecipe(@Param("recipeId") Long recipeId, @Param("categoryId") Long categoryId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM recipe_categories WHERE recipe_id = :recipeId AND category_id IN :categoryIds", nativeQuery = true)
    void removeAllFromRecipe(@Param("recipeId") Long recipeId, @Param("categoryIds") Collection<Long> categoryIds);
}
