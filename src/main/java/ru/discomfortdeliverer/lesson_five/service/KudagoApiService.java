package ru.discomfortdeliverer.lesson_five.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import ru.discomfortdeliverer.lesson_five.exception.ReceivingDataFromExternalApiException;
import ru.discomfortdeliverer.lesson_five.model.Category;
import ru.discomfortdeliverer.lesson_five.model.Location;

import java.util.List;

@Service
@Slf4j
public class KudagoApiService {
    @Value("${kudago.categoryUrl}")
    private String categoryUrl;
    @Value("${kudago.locationUrl}")
    private String locationUrl;
    private final RestTemplate restTemplate;

    public KudagoApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Category> getCategories() {
        log.info("Получение данных по categoryUrl {}", categoryUrl);

        try {
            ResponseEntity<List<Category>> response = restTemplate.exchange(
                    categoryUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Category>>() {}
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                List<Category> categories = response.getBody();
                log.debug("Полученные данные: {}", categories);
                return categories;
            } else {
                log.error("Ошибка получения данных: статус {}", response.getStatusCode());
                throw new ReceivingDataFromExternalApiException("Ошибка получения данных: статус " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Ошибка при выполнении запроса к Kudago API: {}", e.getMessage());
            throw new RuntimeException("Ошибка при выполнении запроса к Kudago API", e);
        }
    }

    public List<Location> getLocations() {
        log.info("Получение данных по categoryUrl {}", locationUrl);
        try {
            ResponseEntity<List<Location>> response = restTemplate.exchange(
                    locationUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Location>>() {}
            );

            // Проверка статуса ответа
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Location> locations = response.getBody();
                log.debug("Полученные данные: {}", locations);
                return locations;
            } else {
                log.error("Ошибка получения данных: статус {}", response.getStatusCode());
                throw new ReceivingDataFromExternalApiException("Ошибка получения данных: статус " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Ошибка при выполнении запроса к Kudago API: {}", e.getMessage());
            throw new RuntimeException("Ошибка при выполнении запроса к Kudago API", e);
        }
    }
}
