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
import ru.discomfortdeliverer.lesson_five.model.Location;
import ru.discomfortdeliverer.lesson_five.repository.LocationRepository;
import ru.discomfortdeliverer.lesson_five.service.KudagoApiService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
@Slf4j
@LogExecutionTime
@AllArgsConstructor
public class LocationController{
    private final LocationRepository locationRepository;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationRepository.getAllLocations();
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable Integer id) {
        return locationRepository.getLocationById(id);
    }

    @PostMapping()
    public Location createLocation(@RequestBody Location newLocation) {
        return locationRepository.createLocation(newLocation);
    }

    @PutMapping("/{id}")
    public Location updateLocationById(@PathVariable int id, @RequestBody Location location) {
        return locationRepository.updateLocationById(id, location);
    }

    @DeleteMapping("/{id}")
    public String deleteLocationById(@PathVariable int id) {
        Integer deletedId = locationRepository.deleteLocationById(id);
        return "Локация с id - " + deletedId + " удалена";
    }
}
