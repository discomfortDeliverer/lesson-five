package ru.discomfortdeliverer.lesson_five.controller;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.discomfortdeliverer.lesson_five.aspect.LogExecutionTime;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.repository.CategoryRepository;
import ru.discomfortdeliverer.lesson_five.service.KudagoApiService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/places/categories")
@Slf4j
@LogExecutionTime
@AllArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final KudagoApiService kudagoApiService;

    @GetMapping
    public List<Category> getAllCategories() throws InterruptedException {
        return categoryRepository.getAllCategories();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Integer id) {
        return categoryRepository.getCategoryById(id);
    }

    @PostMapping()
    public Category createCategory(@RequestBody Category newCategory) {
        return categoryRepository.createCategory(newCategory);
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
