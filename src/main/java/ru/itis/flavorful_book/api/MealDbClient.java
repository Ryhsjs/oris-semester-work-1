package ru.itis.flavorful_book.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.itis.flavorful_book.dto.ExternalMealDTO;

import java.io.IOException;
import java.util.Optional;

@Component
public class MealDbClient {

    private static final Logger log = LoggerFactory.getLogger(MealDbClient.class);
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1";

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public MealDbClient(ObjectMapper objectMapper) {
        this.httpClient = new OkHttpClient();
        this.objectMapper = objectMapper;
    }

    public Optional<ExternalMealDTO> getRandomMeal() {
        Request request = new Request.Builder()
                .url(BASE_URL + "/random.php")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                log.warn("TheMealDB вернул неуспешный ответ: {}", response.code());
                return Optional.empty();
            }
            String body = response.body().string();
            return parseMeal(body);
        } catch (IOException e) {
            log.warn("Ошибка при обращении к TheMealDB: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<ExternalMealDTO> parseMeal(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode meals = root.path("meals");
            if (meals.isMissingNode() || meals.isNull() || meals.isEmpty()) {
                return Optional.empty();
            }
            JsonNode meal = meals.get(0);
            return Optional.of(new ExternalMealDTO(
                    meal.path("idMeal").asText(),
                    meal.path("strMeal").asText(),
                    meal.path("strCategory").asText(),
                    meal.path("strArea").asText(),
                    meal.path("strInstructions").asText(),
                    meal.path("strMealThumb").asText(),
                    meal.path("strYoutube").asText(null),
                    meal.path("strSource").asText(null)
            ));
        } catch (Exception e) {
            log.warn("Ошибка при разборе ответа TheMealDB: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
