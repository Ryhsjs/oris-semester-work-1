package ru.itis.flavorful_book.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.entity.Category;
import ru.itis.flavorful_book.exception.EntityNotFoundException;
import ru.itis.flavorful_book.repository.CategoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void save(Long recipeId, Long categoryId) {
        categoryRepository.addToRecipe(recipeId, categoryId);
    }

    @Override
    @Transactional
    public void saveAll(Long recipeId, List<Long> categories) {
        Set<Long> current = categoryRepository.findAllByRecipeId(recipeId).stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        Set<Long> incoming = new HashSet<>(categories);

        incoming.stream()
                .filter(id -> !current.contains(id))
                .forEach(id -> categoryRepository.addToRecipe(recipeId, id));

        Set<Long> toRemove = current.stream()
                .filter(id -> !incoming.contains(id))
                .collect(Collectors.toSet());
        if (!toRemove.isEmpty()) {
            categoryRepository.removeAllFromRecipe(recipeId, toRemove);
        }
    }

    @Override
    @Transactional
    public void delete(Long recipeId, Long categoryId) {
        categoryRepository.removeFromRecipe(recipeId, categoryId);
    }

    @Override
    @Transactional
    public void deleteAll(Long recipeId, List<Long> categories) {
        if (!categories.isEmpty()) {
            categoryRepository.removeAllFromRecipe(recipeId, categories);
        }
    }

    @Override
    @Cacheable("categories")
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAllByRecipeId(Long recipeId) {
        return categoryRepository.findAllByRecipeId(recipeId);
    }
}
