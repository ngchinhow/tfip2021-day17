package com.tfip2021.module2.service;

import java.io.StringReader;

import com.tfip2021.module2.model.City;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonReader;

import static com.tfip2021.module2.service.Constants.*;

@Service
public class WeatherService {
    public City getWeather(String q) {
        final String apiKey = System.getenv(API_KEY);
        String url = UriComponentsBuilder.
            fromUriString(BASE_URI).
            pathSegment("weather").
            queryParam("q", q).
            queryParam("appid", apiKey).
            toUriString();
        System.out.println(url);
        RequestEntity<Void> req = RequestEntity.
            get(url).
            accept(MediaType.APPLICATION_JSON).
            build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
        return City.fromJson(reader.readObject());
    }
}