package org.owm.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.LogDetail;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class OwmApiClient{
    private static final String API_KEY = "a897cfc55dd33823feffa3a06a1a253c";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5";

    public OwmApiClient() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .setUrlEncodingEnabled(false)
                .addQueryParam("APPID", API_KEY)
                .log(LogDetail.METHOD)
                .log(LogDetail.PATH)
                .build();
    }

    public Response doGet(String path, Map<String, Object> params) {
        return given().queryParameters(params).get(path);
    }
}
