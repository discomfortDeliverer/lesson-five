package ru.discomfortdeliverer.lesson_five.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.discomfortdeliverer.lesson_five.exception.LocationNotFoundException;
import ru.discomfortdeliverer.lesson_five.model.Location;
import ru.discomfortdeliverer.lesson_five.repository.LocationRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LocationController.class)
public class LocationControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationRepository locationRepository;

    @Test
    void getAllLocations_ShouldReturnAllLocations() throws Exception {
        List<Location> locations = List.of(
                new Location(1, "ekb", "Екатеринбург"),
                new Location(2, "kzn", "Казань"),
                new Location(3, "msk", "Москва")
        );
        when(locationRepository.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].slug").value("ekb"))
                .andExpect(jsonPath("$[1].slug").value("kzn"))
                .andExpect(jsonPath("$[2].slug").value("msk"));
    }

    @Test
    void getAllLocations_ShouldEmptyJson() throws Exception {
        List<Location> locations = new ArrayList<>();
        when(locationRepository.getAllLocations()).thenReturn(locations);

        mockMvc.perform(get("/api/v1/locations"))
                .andExpect(status().is(200))
                .andExpect(content().string("[]"));
    }

    @Test
    void getLocationById_ShouldReturnJsonLocation() throws Exception {
        Location location = new Location(5, "msk", "Москва");
        when(locationRepository.getLocationById(5)).thenReturn(location);

        mockMvc.perform(get("/api/v1/locations/5"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.slug").value("msk"))
                .andExpect(jsonPath("$.name").value("Москва"));
    }

    @Test
    void getLocationById_ShouldThrowLocationNotFoundException() throws Exception {
        when(locationRepository.getLocationById(5))
                .thenThrow(new LocationNotFoundException("Локация с id - 5 не найдена"));

        mockMvc.perform(get("/api/v1/locations/5"))
                .andExpect(status().is(404))
                .andExpect(content().string("Локация с id - 5 не найдена"));
    }

    @Test
    void createLocation_ShouldReturnCreatedLocationJson() throws Exception {
        Location createdLocation = new Location(5, "msk", "Москва");
        when(locationRepository.createLocation(any(Location.class))).thenReturn(createdLocation);

        mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"id\": null,\n" +
                        "    \"slug\": \"msk\",\n" +
                        "    \"name\": \"Москва\"\n" +
                        "}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("5"))
                .andExpect(jsonPath("$.slug").value("msk"))
                .andExpect(jsonPath("$.name").value("Москва"));
    }

    @Test
    void updateLocationById_ShouldReturnUpdatedLocationJson() throws Exception {
        Location updatedLocation = new Location(1, "msk", "Москва");
        when(locationRepository.updateLocationById(anyInt(), any(Location.class)))
                .thenReturn(updatedLocation);

        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"slug\": \"krd\",\n" +
                                "    \"name\": \"Краснодар\"\n" +
                                "}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.slug").value("msk"))
                .andExpect(jsonPath("$.name").value("Москва"));
    }

    @Test
    void updateLocationById_ShouldReturnMessageFromLocationNotFoundException() throws Exception {
        when(locationRepository.updateLocationById(anyInt(), any(Location.class)))
                .thenThrow(new LocationNotFoundException("Локация с id - 1 не найдена"));

        mockMvc.perform(put("/api/v1/locations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"slug\": \"krd\",\n" +
                                "    \"name\": \"Краснодар\"\n" +
                                "}"))
                .andExpect(status().is(404))
                .andExpect(content().string("Локация с id - 1 не найдена"));
    }

    @Test
    void deleteLocationById_ShouldReturnMessageThatLocationSuccessfullyDeleted() throws Exception {
        when(locationRepository.deleteLocationById(anyInt()))
                .thenReturn(1);

        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().is(200))
                .andExpect(content().string("Локация с id - 1 удалена"));
    }

    @Test
    void deleteLocationById_ShouldReturnMessageFromLocationNotFoundException() throws Exception {
        when(locationRepository.deleteLocationById(anyInt()))
                .thenThrow(new LocationNotFoundException("Локация с id - 1 не существует"));

        mockMvc.perform(delete("/api/v1/locations/1"))
                .andExpect(status().is(404))
                .andExpect(content().string("Локация с id - 1 не существует"));
    }
}
