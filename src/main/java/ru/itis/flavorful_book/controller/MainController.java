package ru.itis.flavorful_book.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.flavorful_book.dto.ExternalMealDTO;
import ru.itis.flavorful_book.service.ExternalRecipeService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ExternalRecipeService externalRecipeService;

    @GetMapping("/")
    public String welcomePage(Model model) {
        ExternalMealDTO meal = externalRecipeService.getMealOfTheDay();
        if (meal != null) {
            Map<String, String> mealMap = new HashMap<>();
            mealMap.put("name", meal.getName());
            mealMap.put("category", meal.getCategory());
            mealMap.put("area", meal.getArea());
            mealMap.put("thumbnailUrl", meal.getThumbnailUrl());
            mealMap.put("youtubeUrl", meal.getYoutubeUrl());
            mealMap.put("sourceUrl", meal.getSourceUrl());
            model.addAttribute("mealOfTheDay", mealMap);
        }
        return "welcome";
    }
}
