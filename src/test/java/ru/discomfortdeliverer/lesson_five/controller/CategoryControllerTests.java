package ru.discomfortdeliverer.lesson_five.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.discomfortdeliverer.lesson_five.exception.CategoryNotFoundException;
import ru.discomfortdeliverer.lesson_five.exception.LocationNotFoundException;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @Test
    void getAllCategories_ShouldReturnAllCategories() throws Exception {
        List<Category> categories = List.of(
                new Category(135, "comedy-club", "Камеди клаб"),
                new Category(136, "rynok", "Рынок"),
                new Category(137, "prirodnyj-zapovednik", "Природный заповедник")
        );
        when(categoryRepository.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].slug").value("comedy-club"))
                .andExpect(jsonPath("$[1].slug").value("rynok"))
                .andExpect(jsonPath("$[2].slug").value("prirodnyj-zapovednik"));
    }

    @Test
    void getAllCategories_ShouldEmptyJson() throws Exception {
        List<Category> categories = new ArrayList<>();
        when(categoryRepository.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/places/categories"))
                .andExpect(status().is(200))
                .andExpect(content().string("[]"));
    }

    @Test
    void getCategoryById_ShouldReturnJsonCategory() throws Exception {
        Category category = new Category(5, "rynok", "Рынок");
        when(categoryRepository.getCategoryById(5)).thenReturn(category);

        mockMvc.perform(get("/api/v1/places/categories/5"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.slug").value("rynok"))
                .andExpect(jsonPath("$.name").value("Рынок"));
    }

    @Test
    void getCategoryById_ShouldThrowCategoryNotFoundException() throws Exception {
        when(categoryRepository.getCategoryById(5))
                .thenThrow(new CategoryNotFoundException("Категория с id - 5 не найдена"));

        mockMvc.perform(get("/api/v1/places/categories/5"))
                .andExpect(status().is(404))
                .andExpect(content().string("Категория с id - 5 не найдена"));
    }

    @Test
    void createCategory_ShouldReturnCreatedCategoryJson() throws Exception {
        Category category = new Category(5, "anticafe", "Антикафе");
        when(categoryRepository.createCategory(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"id\": null,\n" +
                                "    \"slug\": \"anticafe\",\n" +
                                "    \"name\": \"Антикафе\"\n" +
                                "}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.slug").value("anticafe"))
                .andExpect(jsonPath("$.name").value("Антикафе"));
    }

    @Test
    void updateCategoryById_ShouldReturnUpdatedCategoryJson() throws Exception {
        Category updatedCategory = new Category(5, "anticafe", "Антикафе");
        when(categoryRepository.updateCategoryById(anyInt(), any(Category.class)))
                .thenReturn(updatedCategory);

        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"slug\": \"anticafe\",\n" +
                                "    \"name\": \"Антикафе\"\n" +
                                "}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.slug").value("anticafe"))
                .andExpect(jsonPath("$.name").value("Антикафе"));
    }

    @Test
    void updateCategoryById_ShouldReturnMessageFromCategoryNotFoundException() throws Exception {
        when(categoryRepository.updateCategoryById(anyInt(), any(Category.class)))
                .thenThrow(new CategoryNotFoundException("Категория с id - 1 не найдена"));

        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"slug\": \"anticafe\",\n" +
                                "    \"name\": \"Антикафе\"\n" +
                                "}"))
                .andExpect(status().is(404))
                .andExpect(content().string("Категория с id - 1 не найдена"));
    }

    @Test
    void deleteCategoryById_ShouldReturnMessageThatCategorySuccessfullyDeleted() throws Exception {
        when(categoryRepository.deleteCategoryById(anyInt()))
                .thenReturn(1);

        mockMvc.perform(delete("/api/v1/places/categories/1"))
                .andExpect(status().is(200))
                .andExpect(content().string("Удалена категория с id 1"));
    }

    @Test
    void deleteCategoryById_ShouldReturnMessageFromCategoryNotFoundException() throws Exception {
        when(categoryRepository.deleteCategoryById(anyInt()))
                .thenThrow(new LocationNotFoundException("Категория с id - 1 не существует"));

        mockMvc.perform(delete("/api/v1/places/categories/1"))
                .andExpect(status().is(404))
                .andExpect(content().string("Категория с id - 1 не существует"));
    }
}
