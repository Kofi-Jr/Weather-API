package com.weather.controller;

import com.weather.model.WeatherInfo;
import com.weather.service.WeatherInfoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Server-rendered web view (Thymeleaf) of the stored weather records.
 * The JSON REST API lives in {@link WeatherInfoController}; this controller
 * is the human-facing page at {@code /}.
 */
@Controller
public class WeatherViewController {

    private final WeatherInfoService weatherInfoService;

    public WeatherViewController(WeatherInfoService weatherInfoService) {
        this.weatherInfoService = weatherInfoService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("records", weatherInfoService.getAllWeatherInfo());
        model.addAttribute("weatherInfo", new WeatherInfo());
        return "index";
    }

    @PostMapping("/weather/form")
    public String addFromForm(@Valid @ModelAttribute WeatherInfo weatherInfo, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("records", weatherInfoService.getAllWeatherInfo());
            return "index";
        }
        weatherInfoService.createWeatherInfo(weatherInfo);
        return "redirect:/";
    }
}
