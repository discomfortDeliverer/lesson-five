package ru.discomfortdeliverer.lesson_five.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.model.Location;

import java.util.List;

@Service
@Slf4j
public class KudagoApiService {
    private final String categoryUrl = "https://kudago.com/public-api/v1.4/place-categories/";
    private final String locationUrl = "https://kudago.com/public-api/v1.4/locations/";
    private final RestTemplate restTemplate;

    public KudagoApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Category> getCategories() {
        log.info("Получение данных по categoryUrl {}", categoryUrl);
        ResponseEntity<List<Category>> response = restTemplate.exchange(
                categoryUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Category>>() {
                }
        );
        List<Category> categories = response.getBody();
        log.debug("Полученные данные: {}", categories);
        return categories;
    }

    public List<Location> getLocations() {
        log.info("Получение данных по categoryUrl {}", locationUrl);
        ResponseEntity<List<Location>> response = restTemplate.exchange(
                locationUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Location>>() {
                }
        );
        List<Location> locations = response.getBody();
        log.debug("Полученные данные: {}", locations);
        return locations;
    }
}
