package com.tfip2021.module2.service;

import java.io.StringReader;

import com.tfip2021.module2.model.City;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    private static final String REDIS_CITY_LAST_UPDATED = "city_last_updated";
    private static final String REDIS_CITY_WEATHER = "city_weather";
    private static final long REFRESH_TIME = 1800;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public City getWeather(String q) {
        String cleanedQuery = q.trim().toLowerCase().
            replaceAll("\\p{M}", "").replaceAll(" ", "+");
        String lastUpdatedAt = (String) redisTemplate.opsForHash().
            get(REDIS_CITY_LAST_UPDATED, cleanedQuery);
        String weather;
        if (
            lastUpdatedAt != null &&
            Long.parseLong(lastUpdatedAt) + REFRESH_TIME > 
                System.currentTimeMillis() / 1000L
        ) {
            System.out.println("did not do a refresh");
            weather = getWeatherFromRedis(cleanedQuery);
        } else {
            System.out.println("did a refresh");
            weather = getWeatherFromAPI(cleanedQuery);
        }
        JsonReader reader = Json.createReader(new StringReader(weather));
        return City.fromJson(reader.readObject());
    }

    private String getWeatherFromRedis(String query) {
        return (String) redisTemplate.opsForHash().get(REDIS_CITY_WEATHER, query);
    }

    private String getWeatherFromAPI(String q) {
        System.out.println(q);
        final String weatherAPIKey = System.getenv(ENV_API_KEY);
        String url = UriComponentsBuilder.
            fromUriString(BASE_URI).
            pathSegment("weather").
            queryParam("q", q).
            queryParam("appid", weatherAPIKey).
            encode().
            toUriString();
        System.out.println(url);
        RequestEntity<Void> req = RequestEntity.
            get(url).
            accept(MediaType.APPLICATION_JSON).
            build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = template.exchange(req, String.class);
        String updatedAt = String.valueOf(System.currentTimeMillis() / 1000L);
        redisTemplate.opsForHash().put(
            REDIS_CITY_LAST_UPDATED,
            q,
            updatedAt
        );
        redisTemplate.opsForHash().put(
            REDIS_CITY_WEATHER,
            q,
            resp.getBody()
        );
        return resp.getBody();
    }
}