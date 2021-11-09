package io.petstore.service;

import io.petstore.client.HttpClient;
import io.petstore.dto.Pet;
import io.petstore.util.Endpoints;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static io.petstore.util.Endpoints.PET_ID_PATH;
import static io.restassured.RestAssured.given;

@Component
public class PetService {

    @Autowired
    private HttpClient httpClient;

    public Response addPet(final Pet petToAdd) {
        return httpClient.doPost(Endpoints.PET_PATH, petToAdd);
    }

    public Response findPetsByStatus(Pet.PetStatus... statuses) {
        Map<String, Object> queryParams = new HashMap<>();
        Stream.of(statuses)
                .forEach(status -> queryParams.put("status", status.getValue()));
        return httpClient.doGet(Endpoints.FIND_PET_BY_STATUS_PATH, given().queryParams(queryParams));
    }

    public Response findPetById(long petId) {
        return httpClient.doGet(PET_ID_PATH, given().pathParam("id", petId));
    }

    public Response updatePet(final Pet pet) {
        return httpClient.doPut(Endpoints.PET_PATH, pet);
    }

    public Response deletePetById(long petId) {
        return httpClient.doDelete(PET_ID_PATH, given().pathParam("id", petId));
    }

    public Response uploadImageByPetId(long petId, String pathToImage, String additionalMetadata) {
        return given()
                .contentType("multipart/form-data")
                .multiPart(new File(pathToImage))
                .multiPart("additionalMetadata", additionalMetadata)
                .when()
                .post(Endpoints.PET_PATH + petId + "/uploadImage");
    }

    public Response updatePetNameAndStatusById(long petId, String petName, Pet.PetStatus status) {
        return given()
                .contentType(ContentType.URLENC)
                .formParam("name", petName)
                .formParam("status", status.getValue())
                .pathParam("id", petId)
                .when()
                .post(PET_ID_PATH);
    }
}
