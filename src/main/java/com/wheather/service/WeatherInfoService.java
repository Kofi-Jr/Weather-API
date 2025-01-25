package com.wheather.service;

import com.wheather.model.WeatherInfo;
import com.wheather.repository.WeatherInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WeatherInfoService {

    @Autowired //dependency injection
    private WeatherInfoRepo weatherInfoRepo;


    public WeatherInfo createWeatherInfo(WeatherInfo weatherInfo){

        return weatherInfoRepo.save(weatherInfo);
    }

    public WeatherInfo updateWeatherInfo(int id, WeatherInfo weatherInfo) {
        Optional<WeatherInfo> existingWeatherInfo = weatherInfoRepo.findById(id);

        if (!existingWeatherInfo.isPresent()) {
            return null; // Return null if the resource isn't found
        }
        WeatherInfo updatedWeatherInfo = existingWeatherInfo.get();
        // Use the existing fields to update the temperature
        updatedWeatherInfo.setTodayTemp(weatherInfo.getTodayTemp());  // Update today's temperature
        updatedWeatherInfo.setYesterdayTemp(weatherInfo.getYesterdayTemp());  // Optionally update yesterday's temp
        updatedWeatherInfo.setTomorrowTemp(weatherInfo.getTomorrowTemp());  // Optionally update tomorrow's temp

        // Update the city name
        updatedWeatherInfo.setCityName(weatherInfo.getCityName());

        // Save and return the updated WeatherInfo object
        return weatherInfoRepo.save(updatedWeatherInfo);
    }

    public Optional<WeatherInfo> getWeatherInfoById(int id) {

        return weatherInfoRepo.findById(id);
    }

    public boolean deleteWeatherInfoById(int id) {
        Optional<WeatherInfo> weatherInfo = weatherInfoRepo.findById(id);

        if (weatherInfo.isPresent()) {
            weatherInfoRepo.deleteById(id); // Perform deletion
            return true; // Return true if deletion was successful
        }

        return false; // Return false if entity not found (could not be deleted)
    }
}
