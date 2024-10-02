package ru.discomfortdeliverer.lesson_five.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.discomfortdeliverer.lesson_five.aspect.LogExecutionTime;
import ru.discomfortdeliverer.lesson_five.model.Location;
import ru.discomfortdeliverer.lesson_five.service.KudagoApiService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class LocationRepositoryInitializer implements CommandLineRunner {
    private KudagoApiService kudagoApiService;
    private LocationRepository locationRepository;
    @LogExecutionTime
    @Override
    public void run(String... args) throws Exception {
        log.info("Инициализация LocationRepository");
        fillRepositoryFromExternalApi();
    }

    private void fillRepositoryFromExternalApi() {
        log.info("Начало заполнения LocationRepository данными");
        List<Location> locations = kudagoApiService.getLocations();
        int id = 1;
        for (Location location : locations) {
            log.debug("Добавление локации - {} с id - {} в репозиторий", location, id);
            location.setId(id);
            locationRepository.put(location.getId(), location);
            id++;
        }
        locationRepository.setNextId(id);
        log.info("NextId в locationRepository после добавления всех категорий - {}", id);
        log.info("Завершение заполнения CategoryRepository данными");
    }
}
