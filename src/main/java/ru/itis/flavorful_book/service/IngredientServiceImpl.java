package ru.itis.flavorful_book.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itis.flavorful_book.entity.Ingredient;
import ru.itis.flavorful_book.exception.EntityNotFoundException;
import ru.itis.flavorful_book.repository.IngredientRepository;

import java.util.Collection;
import java.util.List;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ингредиент с id=" + id + " не найден"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> findAllByIds(Collection<Long> ids) {
        return ingredientRepository.findAllById(ids);
    }

    @Override
    @Cacheable("ingredients")
    @Transactional(readOnly = true)
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll(Sort.by("name"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ingredient> findAllByRecipeId(Long recipeId) {
        return ingredientRepository.findAllByRecipeId(recipeId);
    }
}
