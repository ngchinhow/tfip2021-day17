package com.tfip2021.module2.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import jakarta.json.JsonObject;

@Component
public class City {
    private String id;
    private String name;
    private String code;
    private String timezone;
    private Float lat; //latitude
    private Float lon; // longitude 
    private List<Weather> weathers;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public Float getLat() { return lat; }
    public void setLat(Float lat) { this.lat = lat; }

    public Float getLon() { return lon; }
    public void setLon(Float lon) { this.lon = lon; }

    public List<Weather> getWeathers() { return weathers; }
    public void setWeathers(List<Weather> weathers) { this.weathers = weathers; }

    public static City fromJson(JsonObject cObject) {
        final City city = new City();
        city.setId(String.valueOf(cObject.getInt("id")));
        city.setName(cObject.getString("name"));
        city.setCode(cObject.getJsonObject("sys").getString("country"));
        city.setTimezone(String.valueOf(cObject.getInt("timezone")));
        city.setLat((float) cObject.getJsonObject("coord").
            getJsonNumber("lat").doubleValue());
        city.setLon((float) cObject.getJsonObject("coord").
            getJsonNumber("lon").doubleValue());
        city.setWeathers(
            cObject.getJsonArray("weather").stream().
                map(v -> Weather.
                    fromJson((JsonObject) v,cObject.getJsonObject("main"))).
                collect(Collectors.toList())
        );
        return city;
    }
}
