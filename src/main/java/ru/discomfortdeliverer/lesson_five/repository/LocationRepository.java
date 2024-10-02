package ru.discomfortdeliverer.lesson_five.repository;

import org.springframework.stereotype.Repository;
import ru.discomfortdeliverer.lesson_five.exception.CategoryNotFoundException;
import ru.discomfortdeliverer.lesson_five.exception.LocationNotFoundException;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.model.Location;

import java.util.List;
import java.util.Optional;

@Repository
public class LocationRepository extends ConcurrentHashMapWrapper<Location> {
    public void put(Integer id, Location location) {
        super.put(id, location);
    }

    public List<Location> getAllLocations() {
        return super.getAllValues();
    }

    public Location getLocationById(Integer id) {
        try {
            return super.getValueById(id);
        } catch (NoValueExistsByIdException e) {
            throw new LocationNotFoundException("Локация с id - " + id + " не найдена", e);
        }
    }

    public Location createLocation(Location location) {
        location.setId(super.getNextId());
        return super.createValue(location);
    }

    public Location updateLocationById(Integer id, Location location) {
        location.setId(id);
        try {
            return super.updateValueById(id, location);
        } catch (NoValueExistsByIdException e) {
            throw new LocationNotFoundException("Локация с id - " + id + " не найдена", e);
        }
    }

    public Integer deleteLocationById(Integer id) {
        try {
            return super.deleteValueById(id);
        } catch (NoValueExistsByIdException e) {
            throw new LocationNotFoundException("Локация с id - " + id + " не существует", e);
        }
    }
}
