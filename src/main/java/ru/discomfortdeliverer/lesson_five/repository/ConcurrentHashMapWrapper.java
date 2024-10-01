package ru.discomfortdeliverer.lesson_five.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import ru.discomfortdeliverer.lesson_five.exception.NoValueExistsByIdException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConcurrentHashMapWrapper<V> {
    private final ConcurrentHashMap<Integer, V> storage;
    @Setter
    @Getter
    private Integer nextId;

    protected ConcurrentHashMapWrapper() {
        this.storage = new ConcurrentHashMap<>();
    }

    protected void put(Integer id, V value) {
        storage.put(id, value);
    }

    protected List<V> getAllValues() {
        return new ArrayList<>(storage.values());
    }

    protected V getValueById(Integer id) throws NoValueExistsByIdException {
        if (storage.containsKey(id)) {
            return storage.get(id);
        }
        throw new NoValueExistsByIdException("Объект с id " + id + " не найден");
    }

    protected V createValue(V value) {
        int id = nextId;
        storage.put(nextId, value);
        nextId++;
        return value;
    }

    protected V updateValueById(Integer id, V value) throws NoValueExistsByIdException{
        if (storage.containsKey(id)) {
            storage.put(id, value);
            return value;
        } else {
            throw new NoValueExistsByIdException("Объект с id: " + id + " не найден");
        }
    }

    protected Integer deleteValueById(Integer id) {
        if (storage.containsKey(id)) {
            storage.remove(id);
            return id;
        } else {
            throw new NoValueExistsByIdException("Объект с id: " + id + " не существует");
        }
    }
}
