package org.owm.repository.city;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.owm.domain.City;
import org.owm.ex.CityNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class CityRepositoryImpl implements CityRepository {
    private ObjectMapper objectMapper;
    private File citiesSource;
    private List<City> cityList;

    public CityRepositoryImpl() throws IOException {
        this.objectMapper = new ObjectMapper();
        citiesSource = new File("src/main/resources/city.list.json");
        cityList = objectMapper.readValue(citiesSource, new TypeReference<List<City>>(){});
    }

    @Override
    public City getCityByName(String cityName) throws Exception {
        Optional<City> city = cityList.stream()
                .filter(s -> s.getName().equals(cityName))
                .findFirst();
        if(city.isPresent()){
            return city.get();
        } else
            throw new CityNotFoundException("City not found.");
    }
}

