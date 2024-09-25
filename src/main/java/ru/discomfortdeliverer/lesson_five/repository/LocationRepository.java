package ru.discomfortdeliverer.lesson_five.repository;

import org.springframework.stereotype.Repository;
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

    public Optional<Location> getLocationById(Integer id) {
        return super.getValueById(id);
    }

    public Location createLocation(Location location) {
        location.setId(super.getNextId());
        return super.createValue(location);
    }

    public Location updateLocationById(Integer id, Location location) {
        location.setId(id);
        return super.updateValueById(id, location);
    }

    public Integer deleteLocationById(Integer id) {
        return super.deleteValueById(id);
    }
}
