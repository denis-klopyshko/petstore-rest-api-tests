package org.owm.repository.city;

import org.owm.domain.City;

public interface CityRepository {
    City getCityByName(String cityName) throws Exception;
}
