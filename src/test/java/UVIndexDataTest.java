import org.hamcrest.core.IsNull;
import org.owm.domain.City;
import org.owm.domain.Coordinates;
import org.owm.domain.UVData;
import org.owm.ex.ApiResponseException;
import org.owm.repository.city.CityRepository;
import org.owm.repository.city.CityRepositoryImpl;
import org.owm.repository.uvindex.UvIndexRepository;
import org.owm.repository.uvindex.UvIndexRepositoryImpl;
import org.owm.util.QueryParameters;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UVIndexDataTest {
    private static final String UV_INDEX_FIND_PATH = "/uvi";
    private static final String FORECAST_UV_INDEX_FIND_PATH = "/uvi/forecast";
    private static final String UV_HISTORY_DATA = "/uvi/history";
    private CityRepository cityRepository;
    private UvIndexRepository uvIndexRepository;

    @BeforeSuite
    public void setUp() throws IOException {
        cityRepository = new CityRepositoryImpl();
        uvIndexRepository = new UvIndexRepositoryImpl();
    }

    @Test
    public void getUvIndexByCityCoordinates() throws Exception {
        String cityName = "Guraki";
        City city  = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord());

        UVData uvData = uvIndexRepository.getUvData(UV_INDEX_FIND_PATH, queryParameters);
        assertThat(uvData.getValue(), is(IsNull.notNullValue()));
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "400 : 99.99 is not a float")
    public void getUvIndexByIncorrectCoordinates() throws Exception {
        Coordinates coordinates = Coordinates.builder().lat(99.99).lon(99.99).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(coordinates);

        uvIndexRepository.getUvData(UV_INDEX_FIND_PATH, queryParameters);
    }

    @Test
    public void getForecastUvDataByCoordinates() throws Exception {
        String cityName = "Riga";
        City city  = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord());

        List<UVData> uvDataList = uvIndexRepository.getUvIndexForecastData(FORECAST_UV_INDEX_FIND_PATH, queryParameters);
        assertThat(uvDataList.size(), equalTo(8));
        for(UVData uvData : uvDataList){
            assertThat(uvData.getValue(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "400 : 99.99 is not a float")
    public void getUvIndexForecastByIncorrectCoordinates() throws Exception {
        Coordinates coordinates = Coordinates.builder().lat(99.99).lon(99.99).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(coordinates);

        uvIndexRepository.getUvIndexForecastData(FORECAST_UV_INDEX_FIND_PATH, queryParameters);
    }

    @Test
    public void getForecastUvDataByCoordinatesWithCntLimitation() throws Exception {
        String cityName = "Rozovka";
        City city  = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord()).setCnt(5);

        List<UVData> uvDataList = uvIndexRepository.getUvIndexForecastData(FORECAST_UV_INDEX_FIND_PATH, queryParameters);
        assertThat(uvDataList.size(), equalTo(5));
        for(UVData uvData : uvDataList){
            assertThat(uvData.getValue(), is(IsNull.notNullValue()));
        }
    }

    // historical data for 1-2 june 2018
    @Test
    public void getUvHistoryData() throws Exception {
        String cityName = "Zaton";
        City city  = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord())
                       .setStart(1527811200)
                       .setEnd(1527897600);

        List<UVData> uvDataList = uvIndexRepository.getUvIndexForecastData(UV_HISTORY_DATA, queryParameters);
        System.out.println(uvDataList.toString());
        assertThat(uvDataList.size(), equalTo(2));
        for(UVData uvData : uvDataList){
            assertThat(uvData.getValue(), is(IsNull.notNullValue()));
        }
    }

    // historical data for 1-2 june 2018
    @Test(expectedExceptions = ApiResponseException.class)
    public void getUvHistoryDataWithIncorrectPerion() throws Exception {
        String cityName = "Perevoz";
        City city  = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord())
                .setEnd(1527811200)
                .setStart(1527897600);

        uvIndexRepository.getUvIndexForecastData(UV_HISTORY_DATA, queryParameters);
    }
}
