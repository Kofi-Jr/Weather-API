package com.wheather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

// Entity Class
// Map to DB
@Entity
public class WeatherInfo {

    //5 fields
    @Id
    private int id; // Map id to database tables
    private String cityName;
    private double yesterdayTemp;
    private double todayTemp;
    private double tomorrowTemp;

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
