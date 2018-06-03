import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNull;
import org.owm.domain.City;
import org.owm.domain.Coordinates;
import org.owm.domain.WeatherData;
import org.owm.enums.Lang;
import org.owm.enums.Type;
import org.owm.enums.Units;
import org.owm.ex.ApiResponseException;
import org.owm.repository.city.CityRepository;
import org.owm.repository.city.CityRepositoryImpl;
import org.owm.repository.current.CurrentWeatherRepository;
import org.owm.repository.current.CurrentWeatherRepositoryImpl;
import org.owm.util.QueryParameters;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class CurrentWeatherForOneCityTest {
    private CityRepository cityRepository;
    private CurrentWeatherRepository weatherRepository;

    @BeforeSuite
    public void setUp() throws IOException {
        cityRepository = new CityRepositoryImpl();
        weatherRepository = new CurrentWeatherRepositoryImpl();
    }

    // one city current weather tests
    @Test
    public void shouldReturnWeatherByCityName() throws ApiResponseException {
        String cityName = "Marmaris";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getId(), equalTo(304782L));
        assertThat(weatherData.getName(), equalTo(cityName));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getSys().getCountry(), equalTo("TR"));
    }

    @Test
    public void shouldReturnWeatherByCityNameAndCountryCode() throws ApiResponseException {
        String cityName = "Paris";
        String countryCode = "FR";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName + "," + countryCode);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getId(), equalTo(2988507L));
        assertThat(weatherData.getName(), equalTo(cityName));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("FR"));
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void shouldNotReturnWeatherByNonExistingCityName() throws ApiResponseException {
        String cityName = "test";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName);
        weatherRepository.getWeather(queryParameters);
    }

    @Test
    public void shouldReturnWeatherByCityNameWithAllAvailableParams() throws ApiResponseException {
        String cityName = "İstanbul";
        String countryCode = "TR";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName + "," + countryCode)
                .setUnits(Units.CELCIUM)
                .setAccuracy(Type.LIKE)
                .setLang(Lang.ROMANIAN);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getId(), equalTo(745042L));
        assertThat(weatherData.getName(), equalTo(cityName));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("TR"));
    }

    @Test
    public void shouldReturnWeatherByCityId() throws Exception {
        String cityName = "Hurzuf";
        City city = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityId(city.getId()).setLang(Lang.FRENCH);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getId(), equalTo(city.getId()));
        assertThat(weatherData.getName(), equalTo(cityName));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("UA"));
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void shouldNotReturnWeatherByNonExistingCityId() throws ApiResponseException {
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityId(Integer.MAX_VALUE);
        weatherRepository.getWeather(queryParameters);
    }

    @Test
    public void shouldReturnWeatherByCityIdWithAllAvailableParameters() throws Exception {
        String cityName = "Bucha";
        City city = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityId(city.getId())
                .setLang(Lang.CATALAN)
                .setUnits(Units.FAHRENHEIT)
                .setAccuracy(Type.LIKE);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getId(), equalTo(city.getId()));
        assertThat(weatherData.getName(), equalTo(cityName));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("UA"));
    }


    @Test
    public void shouldReturnWeatherByGeographicCoordinates() throws Exception {
        String cityName = "London";
        City city = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord());
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getId(), equalTo(city.getId()));
        assertThat(weatherData.getName(), equalTo(cityName));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("GB"));
    }

    // Should find nearest city to geographic point by coordinates
    @Test
    public void shouldReturnWeatherOfNearestCityByCoords() throws ApiResponseException {
        Coordinates coordinates = Coordinates.builder().lat(64.299662).lon(-23.816441).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(coordinates);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getId(), equalTo(3413727L));
        assertThat(weatherData.getName(), equalTo("Sandgerdi"));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));;
        assertThat(weatherData.getSys().getCountry(), equalTo("IS"));
    }

    @Test
    public void shouldReturnWeatherByZipCodeWithDefaultCountryCode()throws ApiResponseException {
        String zipCode = "94040";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getName(), equalTo("Mountain View"));
        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("US"));
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void shouldNotFoundCityByInvalidZipCodeForUsa()throws ApiResponseException {
        String zipCode = "74823";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode);
        weatherRepository.getWeather(queryParameters);
    }

    @Test
    public void shouldReturnWeatherByZipCodeAndCountryCode()throws ApiResponseException {
        String zipCode = "01032";
        String countryCode = "UA";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode + "," + countryCode);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getName(), equalTo("Kyiv"));
        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("UA"));
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void shouldNotFindCityInCountryByInvalidZipCode()throws ApiResponseException {
        String zipCode = "02140";
        String countryCode = "GB";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode + "," + countryCode);
        weatherRepository.getWeather(queryParameters);
    }

    @Test
    public void shouldReturnWeatherByZipCodeWithAllAvailableParameters()throws ApiResponseException {
        String zipCode = "01032";
        String countryCode = "UA";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode + "," + countryCode)
                .setLang(Lang.ARABIC)
                .setUnits(Units.CELCIUM)
                .setAccuracy(Type.ACCURATE);
        WeatherData weatherData = weatherRepository.getWeather(queryParameters);

        assertThat(weatherData.getName(), equalTo("Kyiv"));
        assertThat(weatherData.getCod(), equalTo(200));
        assertThat(weatherData.getWeather(), not(IsEmptyCollection.empty()));
        assertThat(weatherData.getMain(), is(IsNull.notNullValue()));
        assertThat(weatherData.getSys().getCountry(), equalTo("UA"));
    }
}
