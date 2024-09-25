package ru.discomfortdeliverer.lesson_five.repository;


import org.springframework.stereotype.Repository;
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

    public Optional<Category> getCategoryById(Integer id) {
        return super.getValueById(id);
    }

    public Category createCategory(Category category) {
        category.setId(super.getNextId());
        return super.createValue(category);
    }

    public Category updateCategoryById(Integer id, Category category) {
        category.setId(id);
        return super.updateValueById(id, category);
    }

    public Integer deleteCategoryById(Integer id) {
        return super.deleteValueById(id);
    }
}
