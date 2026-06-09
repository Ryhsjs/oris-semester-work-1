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
import ru.itis.flavorful_book.form.RecipeForm;
import ru.itis.flavorful_book.security.CustomeUserDetails;
import ru.itis.flavorful_book.service.RecipeService;
import ru.itis.flavorful_book.util.validation.ValidationUtils;

import java.util.Map;

@Tag(name = "Рецепты", description = "Создание, обновление и удаление рецептов")
@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeRestController {

    private final RecipeService recipeService;

    @Operation(
        summary = "Создать рецепт",
        description = "Создаёт новый рецепт от имени текущего пользователя. Требует аутентификации.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Рецепт создан, возвращается его ID"),
            @ApiResponse(responseCode = "400", description = "Ошибки валидации"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован")
        }
    )
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> create(
            @Valid @RequestBody RecipeForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        Long id = recipeService.create(form, currentUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", id));
    }

    @Operation(
        summary = "Обновить рецепт",
        description = "Обновляет рецепт по ID. Только владелец может редактировать свой рецепт.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Рецепт обновлён"),
            @ApiResponse(responseCode = "400", description = "Ошибки валидации"),
            @ApiResponse(responseCode = "403", description = "Нет прав на редактирование")
        }
    )
    @PutMapping(value = "/{id}", consumes = "application/json")
    public ResponseEntity<?> update(
            @Parameter(description = "ID рецепта") @PathVariable Long id,
            @Valid @RequestBody RecipeForm form,
            BindingResult errors,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(ValidationUtils.extractFieldErrors(errors));
        }
        recipeService.update(id, form, currentUser.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Удалить рецепт",
        description = "Удаляет рецепт по ID. Только владелец может удалить свой рецепт.",
        responses = {
            @ApiResponse(responseCode = "204", description = "Рецепт удалён"),
            @ApiResponse(responseCode = "403", description = "Нет прав на удаление")
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID рецепта") @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        recipeService.deleteById(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Добавить рецепт в избранное",
        responses = {
            @ApiResponse(responseCode = "200", description = "Добавлено в избранное"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован")
        }
    )
    @PostMapping("/{id}/favorites")
    public ResponseEntity<Void> addToFavorites(
            @Parameter(description = "ID рецепта") @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        recipeService.addToFavorites(currentUser.getId(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Убрать рецепт из избранного",
        responses = {
            @ApiResponse(responseCode = "200", description = "Убрано из избранного"),
            @ApiResponse(responseCode = "401", description = "Не аутентифицирован")
        }
    )
    @DeleteMapping("/{id}/favorites")
    public ResponseEntity<Void> removeFromFavorites(
            @Parameter(description = "ID рецепта") @PathVariable Long id,
            @AuthenticationPrincipal CustomeUserDetails currentUser) {
        recipeService.deleteFromFavorites(currentUser.getId(), id);
        return ResponseEntity.ok().build();
    }
}
