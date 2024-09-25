package ru.discomfortdeliverer.lesson_five.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.model.ResponseMessage;
import ru.discomfortdeliverer.lesson_five.repository.CategoryRepository;
import ru.discomfortdeliverer.lesson_five.service.ExternalApiService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/places/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final ExternalApiService externalApiService;

    public CategoryController(CategoryRepository categoryRepository,
                              ExternalApiService externalApiService) {
        this.categoryRepository = categoryRepository;
        this.externalApiService = externalApiService;
    }

    @PostConstruct
    public void init() {
        fillRepositoryFromExternalApi();
    }

    private void fillRepositoryFromExternalApi() {
        List<Category> categories = externalApiService.getCategories();
        for (Category category : categories) {
            categoryRepository.put(category.getId(), category);
        }
        Integer maxId = findMaxId(categories);
        categoryRepository.setNextId(++maxId);
    }

    private Integer findMaxId(List<Category> categories) {
        Integer maxId = 0;
        for (Category category : categories) {
            if (category.getId() > maxId) maxId = category.getId();
        }
        return maxId;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> categoryOptional = categoryRepository.getCategoryById(id);

        return categoryOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public Integer createCategory(@RequestBody Category newCategory) {
        return categoryRepository.createCategory(newCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategoryById(@PathVariable int id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryRepository.updateCategoryById(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteCategoryById(@PathVariable int id) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            categoryRepository.deleteCategoryById(id);
            responseMessage.setMessage("Категория с id - " + id + " удалена");
        } catch (NoValueExistsByIdException e) {
            responseMessage.setMessage(e.getMessage());
        }
        return responseMessage;
    }
}
