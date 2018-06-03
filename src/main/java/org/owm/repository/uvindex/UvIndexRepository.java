package org.owm.repository.uvindex;

import org.owm.domain.UVData;
import org.owm.ex.ApiResponseException;
import org.owm.util.QueryParameters;

import java.util.List;

public interface UvIndexRepository {
    UVData getUvData(String path, QueryParameters queryParameters) throws ApiResponseException;
    List<UVData> getUvIndexForecastData(String path, QueryParameters queryParameters) throws ApiResponseException;
}
