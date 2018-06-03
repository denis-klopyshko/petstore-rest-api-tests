import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNull;
import org.owm.domain.Coordinates;
import org.owm.domain.WeatherData;
import org.owm.enums.Lang;
import org.owm.enums.Type;
import org.owm.enums.Units;
import org.owm.ex.ApiResponseException;
import org.owm.repository.current.CurrentWeatherRepository;
import org.owm.repository.current.CurrentWeatherRepositoryImpl;
import org.owm.util.QueryParameters;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class CurrentWeatherForSeveralCitiesTest {
    private static final String BBOX_PATH_URL = "/box/city";
    private static final String CYCLE_FIND_PATH = "/find";
    private static final String FIND_BY_IDS_PATH = "/group";
    private CurrentWeatherRepository weatherRepository;

    @BeforeSuite
    public void setUp() throws IOException {
        weatherRepository = new CurrentWeatherRepositoryImpl();
    }

    @Test
    public void getWeatherByRectangleZone() throws ApiResponseException {
        String rectangleZoneCoords = "19.37,41.76,28.25,34.81,10";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setBbox(rectangleZoneCoords);

        List<WeatherData> weatherDataList = weatherRepository
                .getWeatherBySeveralCities(BBOX_PATH_URL, queryParameters);
        assertThat(weatherDataList.size(), equalTo(137));
        for(WeatherData data : weatherDataList){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getWeatherByRectangleZoneWithServerClustering() throws ApiResponseException {
        String rectangleZoneCoords = "1.42,42.66,1.78,42.44,10";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setBbox(rectangleZoneCoords)
                .setClustering("yes");

        List<WeatherData> weatherDataList = weatherRepository
                .getWeatherBySeveralCities(BBOX_PATH_URL, queryParameters);
        assertThat(weatherDataList.size(), equalTo(1));
        for(WeatherData data : weatherDataList){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getWeatherByRectangleZoneWithAllAvailableParameters() throws ApiResponseException {
        String rectangleZoneCoords = "19.37,41.76,28.25,34.81,10";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setBbox(rectangleZoneCoords)
                .setClustering("yes")
                .setLang(Lang.CHINESE_SIMPLIFIED)
                .setUnits(Units.FAHRENHEIT)
                 .setAccuracy(Type.LIKE);

        List<WeatherData> weatherDataList = weatherRepository
                .getWeatherBySeveralCities(BBOX_PATH_URL, queryParameters);
        assertThat(weatherDataList.size(), equalTo(137));
        for(WeatherData data : weatherDataList){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "400 : incorrect rectangle coordinates")
    public void getWeatherByIncorrectRectangleZone() throws ApiResponseException {
        String rectangleZoneCoords = "99,99,99,99,10";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setBbox(rectangleZoneCoords)
                .setClustering("yes");

        weatherRepository.getWeatherBySeveralCities(BBOX_PATH_URL, queryParameters);
    }

    @Test
    public void getWeatherByCitiesInCycle() throws ApiResponseException {
        Coordinates cycleCenterPoint = Coordinates
                .builder().lat(50.30).lon(30.25).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(cycleCenterPoint);

        List<WeatherData> weatherDataList = weatherRepository
                .getWeatherBySeveralCities(CYCLE_FIND_PATH, queryParameters);
        assertThat(weatherDataList.size(), equalTo(10));
        for(WeatherData data : weatherDataList){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "400 : cnt from 1 to 50")
    public void getWeatherByCitiesInCycleWithCnt() throws ApiResponseException {
        Coordinates cycleCenterPoint = Coordinates
                .builder().lat(50.30).lon(30.25).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(cycleCenterPoint)
                .setCnt(51);

        weatherRepository.getWeatherBySeveralCities(CYCLE_FIND_PATH, queryParameters);
    }

    @Test
    public void getWeatherByCitiesInCycleWithAllAvailableParameters() throws ApiResponseException {
        Coordinates cycleCenterPoint = Coordinates
                .builder().lat(50.30).lon(30.25).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(cycleCenterPoint)
                .setCnt(15)
                .setUnits(Units.CELCIUM)
                .setLang(Lang.SWEDISH)
                .setAccuracy(Type.ACCURATE);

        List<WeatherData> weatherDataList = weatherRepository
                .getWeatherBySeveralCities(CYCLE_FIND_PATH, queryParameters);
        assertThat(weatherDataList.size(), equalTo(15));
        for(WeatherData data : weatherDataList){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getWeatherBySeveralCityIds() throws ApiResponseException {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setSeveralCityIds(1223738,1230089,1236854);

        List<WeatherData> weatherDataList = weatherRepository
                .getWeatherBySeveralCities(FIND_BY_IDS_PATH, queryParameters);
        assertThat(weatherDataList.size(), equalTo(3));
        for(WeatherData data : weatherDataList){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "400 : id list must be in range from 1 to 20")
    public void getWeatherByCityIdsMoreThanLimit() throws ApiResponseException {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setSeveralCityIds(1223738,1230089,1236854,1239047,
                1240935,1223738,1230089,1236854,1239047,
                1240935,1223738,1230089,1236854,1239047,
                1240935,1223738,1230089,1236854,1239047,
                1240935,1223738,1230089,1236854);

        weatherRepository.getWeatherBySeveralCities(FIND_BY_IDS_PATH, queryParameters);
    }

    @Test
    public void getWeatherByCityIdsWithAllAvailableParameters() throws ApiResponseException {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setSeveralCityIds(1223738,1230089,1236854)
                .setUnits(Units.CELCIUM)
                .setLang(Lang.CHINESE_TRADITIONAL)
                .setAccuracy(Type.ACCURATE);

        List<WeatherData> weatherDataList = weatherRepository
                .getWeatherBySeveralCities(FIND_BY_IDS_PATH, queryParameters);
        assertThat(weatherDataList.size(), equalTo(3));
        for(WeatherData data : weatherDataList){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : City not found: 123456")
    public void getWeatherByCityIdsWithIncorrectId() throws ApiResponseException {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setSeveralCityIds(1223738,123456);

        weatherRepository.getWeatherBySeveralCities(FIND_BY_IDS_PATH, queryParameters);
    }
}
