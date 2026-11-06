package com.weather.controller;

import com.weather.model.WeatherInfo;
import com.weather.service.WeatherInfoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class WeatherInfoController {

    private final WeatherInfoService weatherInfoService;

    public WeatherInfoController(WeatherInfoService weatherInfoService) {
        this.weatherInfoService = weatherInfoService;
    }

    // ResponseEntity allows me to set the HTTP status code explicitly
    @PostMapping("/create")
    public ResponseEntity<WeatherInfo> createWeatherInfo(@Valid @RequestBody WeatherInfo weatherInfo) {
        WeatherInfo savedWeatherInfo = weatherInfoService.createWeatherInfo(weatherInfo);

        return new ResponseEntity<>(savedWeatherInfo, HttpStatus.CREATED);
    }

    @PutMapping("/weather/{id}")
    public ResponseEntity<WeatherInfo> updateWeatherInfo(@PathVariable int id, @Valid @RequestBody WeatherInfo weatherInfo) {
        // Pass both the id and the weatherInfo to the service layer
        WeatherInfo updatedWeatherInfo = weatherInfoService.updateWeatherInfo(id, weatherInfo);

        if (updatedWeatherInfo == null) {
            // If the weather info with that ID doesn't exist, return a not found response
            return ResponseEntity.notFound().build();
        }

        // Return the updated weather info with 200 OK status
        return ResponseEntity.ok(updatedWeatherInfo);
    }

    @GetMapping("/weather")
    public ResponseEntity<List<WeatherInfo>> getAllWeatherInfo() {
        return ResponseEntity.ok(weatherInfoService.getAllWeatherInfo());
    }

    @GetMapping("/weather/{id}")
    public ResponseEntity<WeatherInfo> getWeatherInfoById(@PathVariable("id") int id) {
        // Optional is used to avoid null pointer exceptions
        Optional<WeatherInfo> getWeatherInfo = weatherInfoService.getWeatherInfoById(id);
        if (getWeatherInfo.isPresent()) {
            return new ResponseEntity<>(getWeatherInfo.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/weather/{id}")
    public ResponseEntity<WeatherInfo> deleteWeatherInfoById(@PathVariable("id") int id) {
        boolean isDeleted = weatherInfoService.deleteWeatherInfoById(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "Test successful!";
    }
}
