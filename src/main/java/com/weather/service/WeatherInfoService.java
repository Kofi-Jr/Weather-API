package com.weather.service;

import com.weather.model.WeatherInfo;
import com.weather.repository.WeatherInfoRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherInfoService {

    private final WeatherInfoRepo weatherInfoRepo;

    public WeatherInfoService(WeatherInfoRepo weatherInfoRepo) {
        this.weatherInfoRepo = weatherInfoRepo;
    }

    public WeatherInfo createWeatherInfo(WeatherInfo weatherInfo) {
        return weatherInfoRepo.save(weatherInfo);
    }

    public WeatherInfo updateWeatherInfo(int id, WeatherInfo weatherInfo) {
        Optional<WeatherInfo> existingWeatherInfo = weatherInfoRepo.findById(id);

        if (existingWeatherInfo.isEmpty()) {
            return null; // Return null if the resource isn't found
        }
        WeatherInfo updatedWeatherInfo = existingWeatherInfo.get();
        // Update the temperatures and the city name from the request body.
        updatedWeatherInfo.setTodayTemp(weatherInfo.getTodayTemp());
        updatedWeatherInfo.setYesterdayTemp(weatherInfo.getYesterdayTemp());
        updatedWeatherInfo.setTomorrowTemp(weatherInfo.getTomorrowTemp());
        updatedWeatherInfo.setCityName(weatherInfo.getCityName());

        // Save and return the updated WeatherInfo object
        return weatherInfoRepo.save(updatedWeatherInfo);
    }

    public Optional<WeatherInfo> getWeatherInfoById(int id) {
        return weatherInfoRepo.findById(id);
    }

    public List<WeatherInfo> getAllWeatherInfo() {
        return weatherInfoRepo.findAll();
    }

    public boolean deleteWeatherInfoById(int id) {
        if (weatherInfoRepo.findById(id).isPresent()) {
            weatherInfoRepo.deleteById(id); // Perform deletion
            return true; // Return true if deletion was successful
        }

        return false; // Return false if entity not found (could not be deleted)
    }
}
