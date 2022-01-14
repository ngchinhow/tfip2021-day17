package com.tfip2021.module2.model;

import org.springframework.stereotype.Component;

import jakarta.json.JsonObject;

@Component
public class Weather {
    private String id;
    private String main;
    private String description;
    private String iconUrl;
    private Float temp;
    private Float feelsLike;
    private Float humidity; // in %
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMain() { return main; }
    public void setMain(String main) { this.main = main; }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public Float getTemp() { return temp; }
    public void setTemp(Float temp) { this.temp = temp; }

    public Float getFeelsLike() { return feelsLike; }
    public void setFeelsLike(Float feelsLike) { this.feelsLike = feelsLike; }

    public Float getHumidity() { return humidity; }
    public void setHumidity(Float humidity) { this.humidity = humidity; }
    
    public static Weather fromJson(
        JsonObject weatherObject,
        JsonObject measurementsObject
    ) {
        final Weather w = new Weather();
        w.setId(String.valueOf(weatherObject.getInt("id")));
        w.setMain(weatherObject.getString("main"));
        w.setDescription(weatherObject.getString("description"));
        w.setIconUrl(Weather.buildIconUrl(weatherObject.getString("icon")));
        w.setTemp((float) measurementsObject.getJsonNumber("temp").doubleValue());
        w.setFeelsLike((float) measurementsObject.
            getJsonNumber("feels_like").doubleValue()
        );
        w.setHumidity((float) measurementsObject.
            getJsonNumber("humidity").doubleValue()
        );
        return w;
    }
    public static String buildIconUrl(String icon) {
        return String.format("http://openweathermap.org/img/wn/%s@2x.png", icon);
    }
}
