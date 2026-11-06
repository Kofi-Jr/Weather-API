package com.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.model.WeatherInfo;
import com.weather.service.WeatherInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherInfoController.class)
class WeatherInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WeatherInfoService weatherInfoService;

    private WeatherInfo sample(int id, String city) {
        return new WeatherInfo(id, city, 18.5, 21.0, 19.5);
    }

    @Test
    void createWeatherInfo_returns201WithSavedRecord() throws Exception {
        WeatherInfo input = sample(1, "New York");
        when(weatherInfoService.createWeatherInfo(any(WeatherInfo.class))).thenReturn(input);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.cityName").value("New York"))
                .andExpect(jsonPath("$.todayTemp").value(21.0));
    }

    @Test
    void createWeatherInfo_returns400WhenCityNameBlank() throws Exception {
        WeatherInfo input = sample(1, "");
        when(weatherInfoService.createWeatherInfo(any(WeatherInfo.class))).thenReturn(input);

        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void createWeatherInfo_returns400ForMalformedJson() throws Exception {
        mockMvc.perform(post("/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not valid json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void getAllWeatherInfo_returnsList() throws Exception {
        when(weatherInfoService.getAllWeatherInfo())
                .thenReturn(List.of(sample(1, "New York"), sample(2, "Boston")));

        mockMvc.perform(get("/weather"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].cityName").value("Boston"));
    }

    @Test
    void getWeatherInfoById_returns200WhenFound() throws Exception {
        when(weatherInfoService.getWeatherInfoById(1)).thenReturn(Optional.of(sample(1, "New York")));

        mockMvc.perform(get("/weather/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("New York"));
    }

    @Test
    void getWeatherInfoById_returns404WhenMissing() throws Exception {
        when(weatherInfoService.getWeatherInfoById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/weather/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getWeatherInfoById_returns400ForNonNumericId() throws Exception {
        mockMvc.perform(get("/weather/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateWeatherInfo_returns200WhenFound() throws Exception {
        WeatherInfo updated = sample(1, "New York");
        updated.setTodayTemp(25.0);
        when(weatherInfoService.updateWeatherInfo(eq(1), any(WeatherInfo.class))).thenReturn(updated);

        mockMvc.perform(put("/weather/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.todayTemp").value(25.0));
    }

    @Test
    void updateWeatherInfo_returns404WhenMissing() throws Exception {
        WeatherInfo input = sample(99, "Nowhere");
        when(weatherInfoService.updateWeatherInfo(eq(99), any(WeatherInfo.class))).thenReturn(null);

        mockMvc.perform(put("/weather/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteWeatherInfoById_returns204WhenDeleted() throws Exception {
        when(weatherInfoService.deleteWeatherInfoById(1)).thenReturn(true);

        mockMvc.perform(delete("/weather/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWeatherInfoById_returns404WhenMissing() throws Exception {
        when(weatherInfoService.deleteWeatherInfoById(99)).thenReturn(false);

        mockMvc.perform(delete("/weather/99"))
                .andExpect(status().isNotFound());
    }
}
