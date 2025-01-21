package com.wheather.repository;

import com.wheather.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WeatherInfoRepo extends JpaRepository<WeatherInfo,Integer> {


}
