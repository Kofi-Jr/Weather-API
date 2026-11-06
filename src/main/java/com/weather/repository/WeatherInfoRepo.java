package com.weather.repository;

import com.weather.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WeatherInfoRepo extends JpaRepository<WeatherInfo,Integer> {


}
