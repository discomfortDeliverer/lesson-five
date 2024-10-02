package ru.discomfortdeliverer.lesson_five.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.discomfortdeliverer.lesson_five.aspect.LogExecutionTime;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.repository.CategoryRepository;
import ru.discomfortdeliverer.lesson_five.service.KudagoApiService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places/categories")
@Slf4j
@LogExecutionTime
@AllArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() throws InterruptedException {
        return categoryRepository.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryRepository.getCategoryById(id);
    }

    @PostMapping()
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.createCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategoryById(@PathVariable int id, @RequestBody Category category) {
        return categoryRepository.updateCategoryById(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategoryById(@PathVariable int id) {
        Integer deletedId = categoryRepository.deleteCategoryById(id);
        return "Удалена категория с id " + deletedId;
    }


}
