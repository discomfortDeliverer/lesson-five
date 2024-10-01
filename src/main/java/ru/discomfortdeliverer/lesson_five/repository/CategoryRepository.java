package ru.discomfortdeliverer.lesson_five.repository;


import org.springframework.stereotype.Repository;
import ru.discomfortdeliverer.lesson_five.exception.CategoryNotFoundException;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Category;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository extends ConcurrentHashMapWrapper<Category> {
    public void put(Integer id, Category category) {
        super.put(id, category);
    }

    public List<Category> getAllCategories() {
        return super.getAllValues();
    }

    public Category getCategoryById(Integer id) {
        try {
            return super.getValueById(id);
        } catch (NoValueExistsByIdException e) {
            throw new CategoryNotFoundException("Категория с id - " + id + " не найдена", e);
        }
    }

    public Category createCategory(Category category) {
        category.setId(super.getNextId());
        return super.createValue(category);
    }

    public Category updateCategoryById(Integer id, Category category) {
        category.setId(id);
        try {
            return super.updateValueById(id, category);
        } catch (NoValueExistsByIdException e) {
            throw new CategoryNotFoundException("Категория с id - " + id + " не найдена", e);
        }
    }

    public Integer deleteCategoryById(Integer id) {
        try {
            return super.deleteValueById(id);
        } catch (NoValueExistsByIdException e) {
            throw new CategoryNotFoundException("Категория с id - " + id + " не сущесвует", e);
        }
    }
}
