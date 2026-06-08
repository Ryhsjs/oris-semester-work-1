package ru.itis.flavorful_book.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.itis.flavorful_book.api.MealDbClient;
import ru.itis.flavorful_book.dto.ExternalMealDTO;

@Service
public class ExternalRecipeServiceImpl implements ExternalRecipeService {

    private final MealDbClient mealDbClient;

    public ExternalRecipeServiceImpl(MealDbClient mealDbClient) {
        this.mealDbClient = mealDbClient;
    }

    @Override
    @Cacheable(value = "mealOfTheDay", key = "'today'", unless = "#result == null")
    public ExternalMealDTO getMealOfTheDay() {
        return mealDbClient.getRandomMeal().orElse(null);
    }
}
