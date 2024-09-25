package ru.discomfortdeliverer.lesson_five.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Location;
import ru.discomfortdeliverer.lesson_five.model.ResponseMessage;
import ru.discomfortdeliverer.lesson_five.repository.LocationRepository;
import ru.discomfortdeliverer.lesson_five.service.ExternalApiService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {
    private final LocationRepository locationRepository;
    private final ExternalApiService externalApiService;

    public LocationController(LocationRepository locationRepository,
                              ExternalApiService externalApiService) {
        this.locationRepository = locationRepository;
        this.externalApiService = externalApiService;
    }

    @PostConstruct
    public void init() {
        fillRepositoryFromExternalApi();
    }

    private void fillRepositoryFromExternalApi() {
        List<Location> locations = externalApiService.getLocations();
        int id = 1;
        for (Location location : locations) {
            location.setId(id);
            locationRepository.put(location.getId(), location);
            id++;
        }
        locationRepository.setNextId(id);
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return locationRepository.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Location> getLocationById(@PathVariable Integer id) {
        Optional<Location> categoryOptional = locationRepository.getLocationById(id);

        return categoryOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping()
    public Integer createLocation(@RequestBody Location newLocation) {
        return locationRepository.createLocation(newLocation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Location> updateLocationById(@PathVariable int id, @RequestBody Location location) {
        return ResponseEntity.ok(locationRepository.updateLocationById(id, location));
    }

    @DeleteMapping("/{id}")
    public ResponseMessage deleteLocationById(@PathVariable int id) {
        ResponseMessage responseMessage = new ResponseMessage();
        try {
            Integer deletedId = locationRepository.deleteLocationById(id);
            responseMessage.setMessage("Город с id - " + deletedId + " удален");
        } catch (NoValueExistsByIdException e) {
            responseMessage.setMessage(e.getMessage());
        }
        return responseMessage;
    }
}
