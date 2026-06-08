package ru.itis.flavorful_book.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.itis.flavorful_book.entity.Ingredient;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Query("SELECT ir.ingredient FROM IngredientRecipe ir WHERE ir.recipe.id = :recipeId")
    List<Ingredient> findAllByRecipeId(@Param("recipeId") Long recipeId);

    @Override
    List<Ingredient> findAll(Sort sort);
}
