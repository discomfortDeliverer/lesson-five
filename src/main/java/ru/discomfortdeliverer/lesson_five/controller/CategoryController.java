package ru.discomfortdeliverer.lesson_five.controller;

import jakarta.annotation.PostConstruct;
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
public class CategoryController implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final KudagoApiService kudagoApiService;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository,
                              KudagoApiService kudagoApiService) {
        this.categoryRepository = categoryRepository;
        this.kudagoApiService = kudagoApiService;
    }

    @LogExecutionTime
    @Override
    public void run(String... args) throws Exception {
        log.info("Инициализация CategoryRepository");
        fillRepositoryFromExternalApi();
    }

    private void fillRepositoryFromExternalApi() {
        log.info("Начало заполнения CategoryRepository данными");
        List<Category> categories = kudagoApiService.getCategories();
        for (Category category : categories) {
            log.debug("Добавление категории - {} в репозиторий", category);
            categoryRepository.put(category.getId(), category);
        }
        Integer maxId = findMaxId(categories);
        categoryRepository.setNextId(++maxId);
        log.info("NextId в categoryRepository после добавления всех категорий - {}", maxId);
        log.info("Завершение заполнения CategoryRepository данными");
    }

    private Integer findMaxId(List<Category> categories) {
        Integer maxId = 0;
        for (Category category : categories) {
            if (category.getId() > maxId) maxId = category.getId();
        }
        return maxId;
    }

    @GetMapping
    public List<Category> getAllCategories() throws InterruptedException {
        return categoryRepository.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        Optional<Category> categoryOptional = categoryRepository.getCategoryById(id);
        if (categoryOptional.isPresent()) {
            return ResponseEntity.ok(categoryOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Категория с id - " + id + " не найдена");
        }
    }

    @PostMapping()
    public Category createCategory(@RequestBody Category newCategory) {
        return categoryRepository.createCategory(newCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryById(@PathVariable int id, @RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryRepository.updateCategoryById(id, category));
        } catch (NoValueExistsByIdException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable int id) {
        try {
            Integer deletedId = categoryRepository.deleteCategoryById(id);
            return ResponseEntity.ok("Категория с id - " + deletedId + " удалена");
        } catch (NoValueExistsByIdException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


}
