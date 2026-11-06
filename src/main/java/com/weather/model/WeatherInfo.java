package com.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

/**
 * One city's temperatures for three days: yesterday, today and tomorrow.
 * Mapped to the {@code weather_info} table. The id is supplied by the client
 * when a record is created.
 */
@Entity
public class WeatherInfo {

    @Id
    private int id;
    @NotBlank(message = "cityName must not be blank")
    private String cityName;
    private double yesterdayTemp;
    private double todayTemp;
    private double tomorrowTemp;

    public WeatherInfo() {
    }

    public WeatherInfo(int id, String cityName, double yesterdayTemp, double todayTemp, double tomorrowTemp) {
        this.id = id;
        this.cityName = cityName;
        this.yesterdayTemp = yesterdayTemp;
        this.todayTemp = todayTemp;
        this.tomorrowTemp = tomorrowTemp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getYesterdayTemp() {
        return yesterdayTemp;
    }

    public void setYesterdayTemp(double yesterdayTemp) {
        this.yesterdayTemp = yesterdayTemp;
    }

    public double getTodayTemp() {
        return todayTemp;
    }

    public void setTodayTemp(double todayTemp) {
        this.todayTemp = todayTemp;
    }

    public double getTomorrowTemp() {
        return tomorrowTemp;
    }

    public void setTomorrowTemp(double tomorrowTemp) {
        this.tomorrowTemp = tomorrowTemp;
    }
}
