package ru.discomfortdeliverer.lesson_five.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.service.KudagoApiService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class CategoryRepositoryInitializer implements CommandLineRunner{
    private KudagoApiService kudagoApiService;
    private CategoryRepository categoryRepository;
    @Override
    public void run(String... args) throws Exception {
        log.info("Инициализация CategoryRepository");
        fillRepositoryFromExternalApi();
    }

    private void fillRepositoryFromExternalApi() {
        log.info("Начало заполнения CategoryRepository данными");
        List<Category> categories = kudagoApiService.getCategories();

        Integer maxId = 0;
        for (Category category : categories) {
            log.debug("Добавление категории - {} в репозиторий", category);
            categoryRepository.put(category.getId(), category);
            if (category.getId() > maxId) maxId = category.getId();
        }
        categoryRepository.setNextId(++maxId);
        log.info("NextId в categoryRepository после добавления всех категорий - {}", maxId);
        log.info("Завершение заполнения CategoryRepository данными");
    }
}
