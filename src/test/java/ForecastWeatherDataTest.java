import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNull;
import org.owm.domain.City;
import org.owm.domain.Coordinates;
import org.owm.domain.ForecastWeatherData;
import org.owm.domain.WeatherData;
import org.owm.enums.Lang;
import org.owm.enums.Type;
import org.owm.enums.Units;
import org.owm.ex.ApiResponseException;
import org.owm.repository.city.CityRepository;
import org.owm.repository.city.CityRepositoryImpl;
import org.owm.repository.forecast.ForecastDataRepository;
import org.owm.repository.forecast.ForecastDataRepositoryImpl;
import org.owm.util.QueryParameters;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class ForecastWeatherDataTest {
    private CityRepository cityRepository;
    private ForecastDataRepository forecastDataRepository;

    @BeforeSuite
    public void setUp() throws IOException {
        cityRepository = new CityRepositoryImpl();
        forecastDataRepository = new ForecastDataRepositoryImpl();
    }

    @Test
    public void getForecastWeatherDataByCityName() throws ApiResponseException {
        String cityName = "Nice";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName).setLang(Lang.UKRAINIAN);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo(cityName));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("FR"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void getForecastWeatherForNonExistingCity() throws ApiResponseException {
        String cityName = "test";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName);

       forecastDataRepository.getForecastWeatherData(queryParameters);
    }

    @Test
    public void getWeatherByCityNameAndCountryCode() throws ApiResponseException {
        String cityName = "Lisbon";
        String countryCode = "PT";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName + "," + countryCode).setLang(Lang.JAPANESE);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo(cityName));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("PT"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void shouldNotFindCityInCountry() throws ApiResponseException {
        String cityName = "Lisbon";
        String countryCode = "GR";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName + "," + countryCode);

        forecastDataRepository.getForecastWeatherData(queryParameters);
    }

    @Test
    public void getForecastWeatherByCityNameWithAllAvailableParameters() throws ApiResponseException {
        String cityName = "Helsinki";
        String countryCode = "FI";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityName(cityName + "," + countryCode)
                .setLang(Lang.CHINESE_SIMPLIFIED)
                .setUnits(Units.FAHRENHEIT)
                .setAccuracy(Type.LIKE);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo(cityName));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("FI"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getForecastWeatherByCityId() throws ApiResponseException {
        long cityId = 5815135;
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityId(cityId);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo("Washington"));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("US"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void getForecastWeatherByNonExistingCityId() throws ApiResponseException {
        long cityId = 123456789L;
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityId(cityId);

        forecastDataRepository.getForecastWeatherData(queryParameters);
    }

    @Test
    public void getForecastWeatherByCityIdWithAllAvailableParameters() throws ApiResponseException {
        long cityId = 6358120;
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityId(cityId)
                .setLang(Lang.VIETNAMESE)
                .setUnits(Units.CELCIUM)
                .setAccuracy(Type.ACCURATE);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo("Eibar"));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("ES"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getForecastWeatherByGeographicCoordinates() throws Exception {
        String cityName = "Kisaran";
        City city = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord()).setLang(Lang.SWEDISH);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo("Kisaran"));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("ID"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "400 : 99.99 is not a float")
    public void shouldNotFindWeatherByIncorrectCoordinates() throws Exception {
        Coordinates coordinates = Coordinates.builder().lat(99.99).lon(99.99).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(coordinates);
        forecastDataRepository.getForecastWeatherData(queryParameters);
    }

    @Test
    public void getForecastWeatherByCoordinatesWithoutCity() throws Exception {
        Coordinates coordinates = Coordinates.builder().lat(35.31).lon(17.42).build();
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(coordinates);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getForecastWeatherByGeographicCoordinatesWithAllAvailableParameters() throws Exception {
        String cityName = "Zuwarah";
        City city = cityRepository.getCityByName(cityName);
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCoordinates(city.getCoord())
                .setLang(Lang.DUTCH)
                .setUnits(Units.FAHRENHEIT)
                .setAccuracy(Type.LIKE);
        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);

        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo("Zuwarah"));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("LY"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getForecastWeatherByZipCodeAndDefaultCountryCode() throws ApiResponseException {
        String zipCode = "94040";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode);

        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);
        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo("Mountain View"));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("US"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void getForecastWeatherByNotUsZipCode() throws ApiResponseException {
        String zipCode = "74823";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode);

        forecastDataRepository.getForecastWeatherData(queryParameters);
    }

    @Test(expectedExceptions = ApiResponseException.class,
            expectedExceptionsMessageRegExp = "404 : city not found")
    public void getForecastWeatherByZipCodeAndNotRelatedCountryCode() throws ApiResponseException {
        String zipCode = "74823";
        String countryCode = "ID";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode + "," + countryCode);

        forecastDataRepository.getForecastWeatherData(queryParameters);
    }

    @Test
    public void getForecastWeatherByZipCodeAndCountryCode() throws ApiResponseException {
        String zipCode = "04073";
        String countryCode = "US";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode + "," + countryCode);

        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);
        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo("Portland"));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("US"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }

    @Test
    public void getForecastWeatherByZipCodeAndCountryCodeWithAllAvailableParameters() throws ApiResponseException {
        String zipCode = "04073";
        String countryCode = "US";
        QueryParameters queryParameters = new QueryParameters();
        queryParameters.setCityZipCode(zipCode + "," + countryCode)
                    .setUnits(Units.CELCIUM)
                    .setAccuracy(Type.ACCURATE)
                    .setLang(Lang.SPANISH);

        ForecastWeatherData forecastWeatherData = forecastDataRepository.getForecastWeatherData(queryParameters);
        assertThat(forecastWeatherData.getCod(), equalTo(200));
        assertThat(forecastWeatherData.getCity().getName(), equalTo("Portland"));
        assertThat(forecastWeatherData.getCity().getCountry(), equalTo("US"));
        for(WeatherData data : forecastWeatherData.getList()){
            assertThat(data.getWeather(), not(IsEmptyCollection.empty()));
            assertThat(data.getMain(), is(IsNull.notNullValue()));
        }
    }
}
