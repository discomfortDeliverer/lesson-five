package ru.discomfortdeliverer.lesson_five.repository;

import lombok.Setter;
import org.springframework.stereotype.Repository;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;
import ru.discomfortdeliverer.lesson_five.exception.ValueByIdAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RepositoryWrapper<V> {
    private final ConcurrentHashMap<Integer, V> storage;
    @Setter
    private Integer nextId;

    public RepositoryWrapper() {
        this.storage = new ConcurrentHashMap<>();
    }

    public void put(Integer id, V value) {
        storage.put(id, value);
    }

    public List<V> getAllValues() {
        return new ArrayList<>(storage.values());
    }

    public Optional<V> getValueById(Integer id) {
        if (storage.containsKey(id)) {
            return Optional.of(storage.get(id));
        }
        return Optional.empty();
    }

    public Integer createValue(V value) {
        int id = nextId;
        storage.put(nextId, value);
        nextId++;
        return id;
    }

    public V updateValueById(Integer id, V value) {
        if (storage.containsKey(id)) {
            storage.put(id, value);
            return value;
        } else {
            throw new NoValueExistsByIdException("Объект с id: " + id + " не уже существует");
        }
    }

    public Integer deleteValueById(Integer id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return id;
        } else {
            throw new NoValueExistsByIdException("Объект с id: " + id + " не уже существует");
        }
    }
}
