package org.owm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForecastWeatherData {
    private int cod;
    private float message;
    private int cnt;
    private List<WeatherData> list;
    private City city;

    public ForecastWeatherData() {
    }
}
