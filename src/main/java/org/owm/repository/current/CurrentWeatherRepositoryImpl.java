package org.owm.repository.current;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import org.owm.api.OwmApiClient;
import org.owm.domain.WeatherData;
import org.owm.ex.ApiResponseException;
import org.owm.util.QueryParameters;

import java.util.*;


public class CurrentWeatherRepositoryImpl implements CurrentWeatherRepository {
    private String CURRENT_WEATHER_PATH = "/weather";
    private OwmApiClient apiClient;
    private ObjectMapper objectMapper;

    public CurrentWeatherRepositoryImpl() {
        this.apiClient = new OwmApiClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public WeatherData getWeather(QueryParameters queryParameters) throws ApiResponseException {
        WeatherData weatherData = null;
        Response response = apiClient.doGet(CURRENT_WEATHER_PATH, queryParameters.build());
        try{
            weatherData = objectMapper.readValue(response.asString(), WeatherData.class);
        } catch (Exception e){
            throw new ApiResponseException(response.path("cod"), response.path("message"));
        }
        return weatherData;
    }

    @Override
    public List<WeatherData> getWeatherBySeveralCities(String path, QueryParameters queryParams) throws ApiResponseException {
        List<WeatherData> weatherDataList = null;
        Response response = apiClient.doGet(path, queryParams.build());
        try{
            JsonNode node = objectMapper.readTree(response.asString());
            weatherDataList = objectMapper.readValue(node.path("list").toString(), new TypeReference<List<WeatherData>>(){});
        } catch (Exception e){
            throw new ApiResponseException(response.path("cod"), response.path("message"));
        }
        return weatherDataList;
    }

}
