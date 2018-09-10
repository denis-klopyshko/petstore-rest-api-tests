package io.petstore.service;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import io.petstore.util.Endpoints;
import io.petstore.util.HttpClient;
import io.petstore.dto.Order;
import io.petstore.exception.ApiResponseException;

import static com.jayway.restassured.path.json.JsonPath.from;
import java.util.Map;

public class PetStoreServiceImpl implements PetStoreService {
    private HttpClient api;

    public PetStoreServiceImpl() {
        this.api = new HttpClient();
    }

    @Override
    public Map<String, Long> getPetInventoriesByStatus() {
        Response resp = api.doGet(Endpoints.STORE_IVENTORY_PATH);
        return from(resp.asString()).getMap(".");
    }

    @Override
    public ValidatableResponse placePetOrder(Order order) {
        assert order != null;
        return api.doPost(Endpoints.PLACE_ORDER_PATH, order).then();
    }

    @Override
    public Order findOrderById(long orderId) throws ApiResponseException {
        Order order = null;
        Response resp = api.doGet(Endpoints.PLACE_ORDER_PATH + orderId);
        try{
            order = resp.as(Order.class);
        } catch(Exception e){
            throw new ApiResponseException(resp.getStatusCode(), resp.path("message"));
        }
        return order;
    }

    @Override
    public ValidatableResponse deleteOrderById(long orderId) {
        return api.doDelete(Endpoints.PLACE_ORDER_PATH + orderId).then();
    }
}
