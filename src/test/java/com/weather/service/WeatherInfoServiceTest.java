package com.weather.service;

import com.weather.model.WeatherInfo;
import com.weather.repository.WeatherInfoRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherInfoServiceTest {

    @Mock
    private WeatherInfoRepo weatherInfoRepo;

    @InjectMocks
    private WeatherInfoService weatherInfoService;

    private WeatherInfo sample(int id, String city) {
        return new WeatherInfo(id, city, 18.5, 21.0, 19.5);
    }

    @Test
    void createWeatherInfo_savesAndReturnsRecord() {
        WeatherInfo input = sample(1, "New York");
        when(weatherInfoRepo.save(input)).thenReturn(input);

        assertEquals(input, weatherInfoService.createWeatherInfo(input));
        verify(weatherInfoRepo).save(input);
    }

    @Test
    void getWeatherInfoById_returnsRecordWhenPresent() {
        WeatherInfo stored = sample(1, "New York");
        when(weatherInfoRepo.findById(1)).thenReturn(Optional.of(stored));

        assertEquals(Optional.of(stored), weatherInfoService.getWeatherInfoById(1));
    }

    @Test
    void getWeatherInfoById_returnsEmptyWhenMissing() {
        when(weatherInfoRepo.findById(99)).thenReturn(Optional.empty());

        assertTrue(weatherInfoService.getWeatherInfoById(99).isEmpty());
    }

    @Test
    void getAllWeatherInfo_returnsEveryRecord() {
        List<WeatherInfo> stored = List.of(sample(1, "New York"), sample(2, "Boston"));
        when(weatherInfoRepo.findAll()).thenReturn(stored);

        assertEquals(stored, weatherInfoService.getAllWeatherInfo());
    }

    @Test
    void updateWeatherInfo_updatesFieldsWhenRecordExists() {
        WeatherInfo stored = sample(1, "New York");
        WeatherInfo update = sample(1, "New York");
        update.setTodayTemp(25.0);
        update.setCityName("New York City");
        when(weatherInfoRepo.findById(1)).thenReturn(Optional.of(stored));
        when(weatherInfoRepo.save(any(WeatherInfo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WeatherInfo result = weatherInfoService.updateWeatherInfo(1, update);

        assertNotNull(result);
        assertEquals("New York City", result.getCityName());
        assertEquals(25.0, result.getTodayTemp());
        assertEquals(18.5, result.getYesterdayTemp());
        verify(weatherInfoRepo).save(stored);
    }

    @Test
    void updateWeatherInfo_returnsNullWhenRecordMissing() {
        when(weatherInfoRepo.findById(99)).thenReturn(Optional.empty());

        assertNull(weatherInfoService.updateWeatherInfo(99, sample(99, "Nowhere")));
        verify(weatherInfoRepo, never()).save(any());
    }

    @Test
    void deleteWeatherInfoById_deletesAndReturnsTrueWhenRecordExists() {
        when(weatherInfoRepo.findById(1)).thenReturn(Optional.of(sample(1, "New York")));

        assertTrue(weatherInfoService.deleteWeatherInfoById(1));
        verify(weatherInfoRepo).deleteById(1);
    }

    @Test
    void deleteWeatherInfoById_returnsFalseWhenRecordMissing() {
        when(weatherInfoRepo.findById(99)).thenReturn(Optional.empty());

        assertFalse(weatherInfoService.deleteWeatherInfoById(99));
        verify(weatherInfoRepo, never()).deleteById(anyInt());
    }
}
