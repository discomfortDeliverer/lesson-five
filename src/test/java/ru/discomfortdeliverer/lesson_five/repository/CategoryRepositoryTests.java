package ru.discomfortdeliverer.lesson_five.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.discomfortdeliverer.lesson_five.exception.CategoryNotFoundException;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository categoryRepository;
    private final List<Category> categories = List.of(
            new Category(135, "comedy-club", "Камеди клаб"),
            new Category(136, "rynok", "Рынок"),
            new Category(137, "prirodnyj-zapovednik", "Природный заповедник")
    );

    @BeforeEach
    void setUp() {
        categoryRepository = new CategoryRepository();

        for (Category category : categories) {
            categoryRepository.put(category.getId(), category);
        }
        categoryRepository.setNextId(138);
    }

    @Test
    void getAllCategories_ShouldReturnAllCategories() {
        List<Category> categories = List.of(
                new Category(135, "comedy-club", "Камеди клаб"),
                new Category(136, "rynok", "Рынок"),
                new Category(137, "prirodnyj-zapovednik", "Природный заповедник")
        );
        List<Category> allCategories = categoryRepository.getAllCategories();

        assertThat(allCategories).isEqualTo(categories);
    }

    @Test
    void getCategoryById_ShouldReturnCategory() throws Exception {

        Category categoryById = categoryRepository.getCategoryById(136);

        assertThat(categoryById).isEqualTo(categories.get(1));
    }

    @Test
    void getCategoryById_ShouldThrowCategoryNotFoundException() throws Exception {
        assertThatThrownBy(() -> {
            categoryRepository.getCategoryById(-1);
        })
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Категория с id - -1 не найдена");
    }

    @Test
    void createCategory_ShouldReturnCreatedCategory() throws Exception {
        Category newCategory = new Category(null, "recreation", "Активный отдых");
        Category createdCategory = categoryRepository.createCategory(newCategory);

        assertThat(createdCategory.getId()).isNotNull();
        assertThat(createdCategory.getSlug()).isEqualTo(newCategory.getSlug());
        assertThat(createdCategory.getName()).isEqualTo(newCategory.getName());
    }

    @Test
    void updateCategoryById_ShouldReturnUpdatedCategoryJson() throws Exception {
        Category newCategory = new Category(null, "anticafe", "Антикафе");

        Category updatedCategory = categoryRepository.updateCategoryById(135, newCategory);

        assertThat(updatedCategory.getId()).isNotNull();
        assertThat(updatedCategory.getSlug()).isEqualTo(newCategory.getSlug());
        assertThat(updatedCategory.getName()).isEqualTo(newCategory.getName());
        assertThat(categoryRepository.getCategoryById(135)).isEqualTo(updatedCategory);
    }

    @Test
    void updateCategoryById_ShouldThrowCategoryNotFoundException() throws Exception {
        Category newCategory = new Category(null, "anticafe", "Антикафе");

        assertThatThrownBy(() -> {
            categoryRepository.updateCategoryById(-1, newCategory);
        })
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Категория с id - -1 не найдена");
    }

    @Test
    void deleteCategoryById_ShouldReturnDeletedCategoryId() throws Exception {
        Integer deletedId = categoryRepository.deleteCategoryById(135);

        assertThat(deletedId).isEqualTo(135);
        assertThatThrownBy(() -> {
            categoryRepository.getCategoryById(135);
        })
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessageContaining("Категория с id - 135 не найдена");
    }

    @Test
    void deleteCategoryById_ShouldThrowCategoryNotFoundException() throws Exception {
        assertThatThrownBy(() -> {
            categoryRepository.deleteValueById(-1);
        })
                .isInstanceOf(NoValueExistsByIdException.class)
                .hasMessageContaining("Объект с id: -1 не существует");
    }
}
