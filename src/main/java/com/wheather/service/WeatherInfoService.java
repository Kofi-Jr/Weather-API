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


    public WeatherInfo creatWeatherInfo(WeatherInfo weatherInfo){

        return weatherInfoRepo.save(weatherInfo);
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
