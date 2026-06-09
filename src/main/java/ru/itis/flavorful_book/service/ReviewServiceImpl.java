package ru.itis.flavorful_book.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.dto.ReviewDTO;
import ru.itis.flavorful_book.entity.Recipe;
import ru.itis.flavorful_book.entity.Review;
import ru.itis.flavorful_book.exception.ConflictException;
import ru.itis.flavorful_book.exception.EntityNotFoundException;
import ru.itis.flavorful_book.exception.ForbiddenException;
import ru.itis.flavorful_book.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final RecipeService recipeService;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserService userService,
                             RecipeService recipeService) {
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.recipeService = recipeService;
    }

    @Override
    @Transactional
    public void save(Long userId, Long recipeId, Integer rating, String comment) {
        Recipe recipe = recipeService.getEntityById(recipeId);
        if (recipe.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Нельзя оставить отзыв на свой рецепт");
        }
        if (reviewRepository.findByUser_IdAndRecipe_Id(userId, recipeId).isPresent()) {
            throw new ConflictException("Вы уже оставляли отзыв на этот рецепт");
        }
        Review review = new Review();
        review.setUser(userService.findById(userId));
        review.setRecipe(recipe);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void update(Long id, Long userId, Integer rating, String comment) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв с id=" + id + " не найден"));
        if (!review.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Нет прав на редактирование отзыва");
        }
        review.setRating(rating);
        review.setComment(comment);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteById(Long id, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв с id=" + id + " не найден"));
        if (!review.getUser().getId().equals(userId)) {
            throw new ForbiddenException("Нет прав на удаление отзыва");
        }
        reviewRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO findById(Long id) {
        return reviewRepository.findById(id)
                .map(ReviewDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв с id=" + id + " не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO findByUserIdRecipeId(Long userId, Long recipeId) {
        return reviewRepository.findByUser_IdAndRecipe_Id(userId, recipeId)
                .map(ReviewDTO::from)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> findAllByRecipeId(Long recipeId) {
        return reviewRepository.findAllByRecipe_Id(recipeId).stream()
                .map(ReviewDTO::from)
                .toList();
    }
}
