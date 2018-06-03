package org.owm.repository.uvindex;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.response.Response;
import org.owm.api.OwmApiClient;
import org.owm.domain.UVData;
import org.owm.ex.ApiResponseException;
import org.owm.util.QueryParameters;

import java.util.List;

public class UvIndexRepositoryImpl implements UvIndexRepository {
    private OwmApiClient apiClient;
    private ObjectMapper objectMapper;

    public UvIndexRepositoryImpl() {
        this.apiClient = new OwmApiClient();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public UVData getUvData(String path, QueryParameters queryParameters) throws ApiResponseException {
        UVData uvindex = null;
        Response response = apiClient.doGet(path, queryParameters.build());
        try{
            uvindex = response.as(UVData.class);
        } catch (Exception e){
            throw new ApiResponseException(response.path("cod"), response.path("message"));
        }
        return uvindex;
    }

    @Override
    public List<UVData> getUvIndexForecastData(String path, QueryParameters queryParams) throws ApiResponseException {
        List<UVData> weatherDataList = null;
        Response response = apiClient.doGet(path, queryParams.build());
        try{
            weatherDataList = objectMapper.readValue(response.asString(), new TypeReference<List<UVData>>(){});
        } catch (Exception e){
            throw new ApiResponseException(response.path("cod"), response.path("message"));
        }
        return weatherDataList;
    }
}
