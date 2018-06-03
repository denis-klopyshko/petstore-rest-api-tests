package org.owm.util;

import org.owm.domain.Coordinates;
import org.owm.enums.Lang;
import org.owm.enums.Type;
import org.owm.enums.Units;

import java.util.HashMap;
import java.util.Map;

public class QueryParameters {
    private Map<String, Object> queryParameters;

    public QueryParameters() {
        this.queryParameters = new HashMap<>();
    }

    public QueryParameters setLang(Lang lang){
        queryParameters.put("lang", lang.getLangCode());
        return this;
    }

    public QueryParameters setUnits(Units unit){
        queryParameters.put("units", unit.getUnitName());
        return this;
    }

    public QueryParameters setCallbackFunctionName(String functionName){
        queryParameters.put("callback", functionName);
        return this;
    }

    public QueryParameters setAccuracy(Type type){
        queryParameters.put("type", type.getValue());
        return this;
    }

    public QueryParameters setCityName(String cityName){
        queryParameters.put("q", cityName);
        return this;
    }

    public QueryParameters setCityId(long id){
        queryParameters.put("id", id);
        return this;
    }

    public QueryParameters setCoordinates(final Coordinates coordinates){
        queryParameters.put("lat", coordinates.getLat());
        queryParameters.put("lon", coordinates.getLon());
        return this;
    }

    public QueryParameters setCityZipCode(String zipCode){
        queryParameters.put("zip", zipCode);
        return this;
    }

    public QueryParameters setBbox(String bbox){
        queryParameters.put("bbox", bbox);
        return this;
    }

    public QueryParameters setCnt(int cnt){
        queryParameters.put("cnt", cnt);
        return this;
    }

    // yes/no possible variants
    public QueryParameters setClustering(String isClustering){
        queryParameters.put("clustering", isClustering);
        return this;
    }

    public QueryParameters setSeveralCityIds(long... ids){
        StringBuilder sb = new StringBuilder();
        sb.append(ids[0]);
        for(int i = 1; i < ids.length;i++){
            sb.append(",").append(ids[i]);
        }
        queryParameters.put("id", sb.toString());
        return this;
    }

    public QueryParameters setStart(long start){
        queryParameters.put("start", start);
        return this;
    }

    public QueryParameters setEnd(long end){
        queryParameters.put("end", end);
        return this;
    }

    public Map<String, Object> build(){
        return queryParameters;
    }
}
