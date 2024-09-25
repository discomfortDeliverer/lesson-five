package ru.discomfortdeliverer.lesson_five.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.model.Location;

import java.util.List;

@Service
public class ExternalApiService {
    private final String url = "https://kudago.com/public-api/v1.4/place-categories/";
    private final String locationUrl = "https://kudago.com/public-api/v1.4/locations/";
    private final RestTemplate restTemplate;

    public ExternalApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Category> getCategories() {
        ResponseEntity<List<Category>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Category>>() {
                }
        );
        return response.getBody();
    }

    public List<Location> getLocations() {
        ResponseEntity<List<Location>> response = restTemplate.exchange(
                locationUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Location>>() {
                }
        );
        return response.getBody();
    }
}
