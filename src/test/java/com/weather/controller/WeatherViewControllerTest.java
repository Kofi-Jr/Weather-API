package com.weather.controller;

import com.weather.model.WeatherInfo;
import com.weather.service.WeatherInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherViewController.class)
class WeatherViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherInfoService weatherInfoService;

    @Test
    void index_rendersRecordsTable() throws Exception {
        when(weatherInfoService.getAllWeatherInfo())
                .thenReturn(List.of(new WeatherInfo(1, "New York", 18.5, 21.0, 19.5)));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("New York")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("21.0")));
    }

    @Test
    void index_showsEmptyMessageWhenNoRecords() throws Exception {
        when(weatherInfoService.getAllWeatherInfo()).thenReturn(List.of());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No records yet")));
    }

    @Test
    void addFromForm_savesAndRedirectsToIndex() throws Exception {
        mockMvc.perform(post("/weather/form")
                        .param("id", "2")
                        .param("cityName", "Boston")
                        .param("yesterdayTemp", "17.0")
                        .param("todayTemp", "20.0")
                        .param("tomorrowTemp", "19.0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
