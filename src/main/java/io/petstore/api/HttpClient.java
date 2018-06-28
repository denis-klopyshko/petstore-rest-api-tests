package io.petstore.api;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.filter.log.LogDetail;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

public class HttpClient {
    private static final String BASE_URL = "http://petstore.swagger.io/v2/";

    public HttpClient() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URL)
                .log(LogDetail.ALL)
                .build();
    }

    public Response doPost(String path, final Object body) {
       return given().body(body).post(path);
    }

    public Response doGet(String path, final Map<String, Object> params) {
        return given().queryParams(params).get(path);
    }

    public Response doGet(String path) {
        return this.doGet(path, Collections.emptyMap());
    }

    public Response doDelete(String path) {
        return given().delete(path);
    }

    public Response doPut(String path, final Object body) {
        return given().body(body).post(path);
    }
}
