package ru.discomfortdeliverer.lesson_five.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Location;
import ru.discomfortdeliverer.lesson_five.repository.LocationRepository;
import ru.discomfortdeliverer.lesson_five.service.KudagoApiService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
@Slf4j
public class LocationController {
    private final LocationRepository locationRepository;
    private final KudagoApiService kudagoApiService;

    @Autowired
    public LocationController(LocationRepository locationRepository,
                              KudagoApiService kudagoApiService) {
        this.locationRepository = locationRepository;
        this.kudagoApiService = kudagoApiService;
    }

    @PostConstruct
    public void init() {
        log.info("Инициализация LocationController");
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

    @GetMapping
    public List<Location> getAllLocations() {
        return locationRepository.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable Integer id) {
        Optional<Location> locationOptional = locationRepository.getLocationById(id);
        if (locationOptional.isPresent()) {
            return ResponseEntity.ok(locationOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Локация с id - " + id + " не найдена");
        }
    }

    @PostMapping()
    public Location createLocation(@RequestBody Location newLocation) {
        return locationRepository.createLocation(newLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocationById(@PathVariable int id, @RequestBody Location location) {
        try {
            return ResponseEntity.ok(locationRepository.updateLocationById(id, location));
        } catch (NoValueExistsByIdException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocationById(@PathVariable int id) {
        try {
            Integer deletedId = locationRepository.deleteLocationById(id);
            return ResponseEntity.ok("Город с id - " + deletedId + " удален");
        } catch (NoValueExistsByIdException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
