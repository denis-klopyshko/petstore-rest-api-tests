package org.owm.repository.current;

import org.owm.domain.WeatherData;
import org.owm.ex.ApiResponseException;
import org.owm.util.QueryParameters;

import java.util.List;

public interface CurrentWeatherRepository {
    WeatherData getWeather(QueryParameters queryParams) throws ApiResponseException;
    List<WeatherData> getWeatherBySeveralCities(String path, QueryParameters queryParams) throws ApiResponseException;
}
