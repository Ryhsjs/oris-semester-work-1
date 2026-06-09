package ru.itis.flavorful_book.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.itis.flavorful_book.dto.ReviewDTO;
import ru.itis.flavorful_book.form.ReviewForm;
import ru.itis.flavorful_book.security.CustomeUserDetails;
import ru.itis.flavorful_book.service.ReviewService;
import ru.itis.flavorful_book.util.validation.ValidationUtils;

import java.util.List;

@Tag(name = "Отзывы", description = "Управление отзывами к рецептам")
@RestController
@RequestMapping("/recipes/{recipeId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
        summary = "Получить все отзывы к рецепту",
        responses = {
            @ApiResponse(responseCode = "200", description = "Список отзывов")
        }
    )
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> findAll(
            @Parameter(description = "ID рецепта") @PathVariable Long recipeId) {
        return ResponseEntity.ok(reviewService.findAllByRecipeId(recipeId));
    }

    @Operation(
        summary = "Оставить отзыв",
        description = "Создаёт новый отзыв к рецепту от текущего пользователя.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Отзыв сохранён"),
            @ApiResponse(responseCode = "400", description = "Ошибки валидации"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован")
        }
    )
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> save(
            @Parameter(description = "ID рецепта") @PathVariable Long recipeId,
            @Valid @RequestBody ReviewForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        reviewService.save(currentUser.getId(), recipeId, form.getRating(), form.getComment());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
        summary = "Обновить отзыв",
        description = "Редактирует отзыв. Только автор отзыва может его изменить.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Отзыв обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибки валидации"),
            @ApiResponse(responseCode = "403", description = "Нет прав")
        }
    )
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<?> update(
            @Parameter(description = "ID рецепта") @PathVariable Long recipeId,
            @Parameter(description = "ID отзыва") @PathVariable Long id,
            @Valid @RequestBody ReviewForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        reviewService.update(id, currentUser.getId(), form.getRating(), form.getComment());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Удалить отзыв",
        description = "Удаляет отзыв. Только автор может удалить свой отзыв.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Отзыв удалён"),
            @ApiResponse(responseCode = "403", description = "Нет прав")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID рецепта") @PathVariable Long recipeId,
            @Parameter(description = "ID отзыва") @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        reviewService.deleteById(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
