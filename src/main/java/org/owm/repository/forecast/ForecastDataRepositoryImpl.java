package org.owm.repository.forecast;

import com.jayway.restassured.response.Response;
import org.owm.api.OwmApiClient;
import org.owm.domain.ForecastWeatherData;
import org.owm.ex.ApiResponseException;
import org.owm.util.QueryParameters;

public class ForecastDataRepositoryImpl implements ForecastDataRepository {
    private String FORECAST_WEATHER_PATH = "/forecast";
    private OwmApiClient apiClient;

    public ForecastDataRepositoryImpl() {
        this.apiClient = new OwmApiClient();
    }

    public ForecastWeatherData getForecastWeatherData(QueryParameters queryParameters) throws ApiResponseException {
        ForecastWeatherData forecastWeatherData = null;
        Response response = apiClient.doGet(FORECAST_WEATHER_PATH, queryParameters.build());
        try{
            forecastWeatherData = response.as(ForecastWeatherData.class);
        } catch (Exception e){
            throw new ApiResponseException(response.path("cod"), response.path("message"));
        }
        return forecastWeatherData;
    }
}
