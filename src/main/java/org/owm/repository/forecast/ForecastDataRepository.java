package org.owm.repository.forecast;

import org.owm.domain.ForecastWeatherData;
import org.owm.ex.ApiResponseException;
import org.owm.util.QueryParameters;

public interface ForecastDataRepository {
    ForecastWeatherData getForecastWeatherData(QueryParameters queryParameters) throws ApiResponseException;
}
