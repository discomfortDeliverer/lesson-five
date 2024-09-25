package ru.discomfortdeliverer.lesson_five.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.model.ResponseMessage;
import ru.discomfortdeliverer.lesson_five.repository.RepositoryWrapper;
import ru.discomfortdeliverer.lesson_five.service.ExternalApiService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/places/categories")
public class CategoryController {
    private final RepositoryWrapper<Category> repositoryWrapper;
    private final ExternalApiService externalApiService;

    public CategoryController(RepositoryWrapper<Category> repositoryWrapper,
                              ExternalApiService externalApiService) {
        this.repositoryWrapper = repositoryWrapper;
        this.externalApiService = externalApiService;
    }

    @PostConstruct
    public void init() {
        fillRepositoryFromExternalApi();
    }

    private void fillRepositoryFromExternalApi() {
        List<Category> categories = externalApiService.getCategories();
        for (Category category : categories) {
            repositoryWrapper.put(category.getId(), category);
        }
        Integer maxId = findMaxId(categories);
        repositoryWrapper.setNextId(++maxId);
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
        return repositoryWrapper.getAllValues();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        Optional<Category> categoryOptional = repositoryWrapper.getValueById(id);

        return categoryOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public Integer createCategory(@RequestBody Category updatedCategory) {
        return repositoryWrapper.createValue(updatedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategoryById(@PathVariable int id, @RequestBody Category updatedCategory) {
        return ResponseEntity.ok(repositoryWrapper.updateValueById(id, updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteCategoryById(@PathVariable int id) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            repositoryWrapper.deleteValueById(id);
            responseMessage.setMessage("Категория с id - " + id + " удалена");
        } catch (NoValueExistsByIdException e) {
            responseMessage.setMessage(e.getMessage());
        }
        return responseMessage;
    }
}
