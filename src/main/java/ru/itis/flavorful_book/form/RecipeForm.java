package ru.itis.flavorful_book.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import ru.itis.flavorful_book.dto.IngredientDTO;
import ru.itis.flavorful_book.util.validation.CookingTimeValid;

import java.util.ArrayList;
import java.util.List;

@Data
@CookingTimeValid
public class RecipeForm {

    @NotBlank
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    @NotBlank
    private String instructions;

    @NotNull
    @Min(1)
    @Max(1440)
    private Integer activeCookingTime;

    @NotNull
    @Min(1)
    @Max(10080)
    private Integer totalCookingTime;

    @NotNull
    @Min(1)
    @Max(100)
    private Integer servings;

    private String imageUrl;

    private List<Long> categories = new ArrayList<>();

    @Valid
    private List<IngredientDTO> ingredients = new ArrayList<>();
}
