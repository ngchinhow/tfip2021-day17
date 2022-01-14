package com.tfip2021.module2.controller;

import com.tfip2021.module2.model.City;
import com.tfip2021.module2.service.WeatherService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(path = { "/weather" })
public class WeatherResource {

    @Autowired
    private WeatherService service;

    @GetMapping(produces = { "text/html" })
    public String getCurrentWeather(
        @RequestParam MultiValueMap<String, String> params,
        Model model
    ) {
        String city = params.getFirst("city");
        City response = service.getWeather(city);
        model.addAttribute("city", response);
        return "weather";
    }
}
