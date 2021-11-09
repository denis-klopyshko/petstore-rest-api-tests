package io.petstore.service;

import io.petstore.dto.Order;
import io.petstore.exception.ApiResponseException;
import io.petstore.client.HttpClient;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.petstore.util.Endpoints.*;
import static io.restassured.RestAssured.given;

@Slf4j
@Component
public class PetStoreService {

    @Autowired
    private HttpClient httpClient;

    public Map<String, Long> getPetInventoriesByStatus() {
        Response response = httpClient.doGet(STORE_INVENTORY_PATH);
        return response.jsonPath().getMap(".");
    }

    public ValidatableResponse placePetOrder(Order order) {
        return httpClient.doPost(ORDER_PATH, order).then();
    }

    public Response findOrderById(long orderId) {
        return httpClient.doGet(ORDER_ID_PATH, given().pathParam("id", orderId));
    }

    public Response deleteOrderById(long orderId) {
        return httpClient.doDelete(ORDER_ID_PATH, given().pathParam("id", orderId));
    }
}
