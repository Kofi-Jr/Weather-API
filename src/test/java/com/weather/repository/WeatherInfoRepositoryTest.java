package com.weather.repository;

import com.weather.model.WeatherInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WeatherInfoRepositoryTest {

    @Autowired
    private WeatherInfoRepo weatherInfoRepo;

    @Test
    void saveAndFindById_roundTrips() {
        WeatherInfo saved = weatherInfoRepo.save(new WeatherInfo(1, "New York", 18.5, 21.0, 19.5));

        Optional<WeatherInfo> found = weatherInfoRepo.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("New York", found.get().getCityName());
        assertEquals(18.5, found.get().getYesterdayTemp());
        assertEquals(21.0, found.get().getTodayTemp());
        assertEquals(19.5, found.get().getTomorrowTemp());
    }

    @Test
    void findById_returnsEmptyForUnknownId() {
        assertTrue(weatherInfoRepo.findById(999).isEmpty());
    }

    @Test
    void deleteById_removesRecord() {
        weatherInfoRepo.save(new WeatherInfo(1, "New York", 18.5, 21.0, 19.5));

        weatherInfoRepo.deleteById(1);

        assertTrue(weatherInfoRepo.findById(1).isEmpty());
        assertEquals(0, weatherInfoRepo.count());
    }
}
