package com.wheather.controller;

import com.wheather.model.WeatherInfo;
import com.wheather.service.WeatherInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class WeatherInfoController {

    @Autowired
    private WeatherInfoService weatherInfoService;

    //Response entity allows me to set the https status code explicitly
    @PostMapping("/create")
    public ResponseEntity<WeatherInfo> createWeatherInfo(@RequestBody WeatherInfo weatherInfo){

        WeatherInfo savedWeatherInfo = weatherInfoService.createWeatherInfo(weatherInfo);

        return new ResponseEntity<>(savedWeatherInfo, HttpStatus.CREATED);
    }


    @PutMapping("/weather/{id}")
    public ResponseEntity<WeatherInfo> updateWeatherInfo(@PathVariable int id, @RequestBody WeatherInfo weatherInfo) {

        // Pass both the id and the weatherInfo to the service layer
        WeatherInfo updatedWeatherInfo = weatherInfoService.updateWeatherInfo(id, weatherInfo);

        if (updatedWeatherInfo == null) {
            // If the weather info with that ID doesn't exist, return a not found response
            return ResponseEntity.notFound().build();
        }

        // Return the updated weather info with 200 OK status
        return ResponseEntity.ok(updatedWeatherInfo);
    }

    @GetMapping("/weather/{id}")
    public ResponseEntity<WeatherInfo> getWeatherInfoById(@PathVariable("id") int id) {

        // optional is to avoid null pointer exceptions
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
