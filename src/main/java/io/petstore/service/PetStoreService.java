package io.petstore.service;

import io.petstore.client.HttpClient;
import io.petstore.dto.Order;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static io.restassured.RestAssured.given;

@Slf4j
@Component
public class PetStoreService {
    private static final String ORDER_PATH = "/store/order";
    private static final String ORDER_ID_PATH = "/store/order/{id}";

    @Autowired
    private HttpClient httpClient;

    public Response placePetOrder(Order order) {
        return httpClient.doPost(ORDER_PATH, order);
    }

    public Response findOrderById(long orderId) {
        return httpClient.doGet(ORDER_ID_PATH, given().pathParam("id", orderId));
    }

    public Response deleteOrderById(long orderId) {
        return httpClient.doDelete(ORDER_ID_PATH, given().pathParam("id", orderId));
    }
}
