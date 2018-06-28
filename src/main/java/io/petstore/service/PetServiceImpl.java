package io.petstore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import io.petstore.api.HttpClient;
import io.petstore.dto.Pet;
import io.petstore.exception.ApiResponseException;

import java.io.File;
import java.util.*;

import static com.jayway.restassured.RestAssured.given;

public class PetServiceImpl implements PetService {
    private String PET_PATH = "pet/";
    private String FIND_PET_BY_STATUS_PATH = "pet/findByStatus";

    private HttpClient api;

    public PetServiceImpl() {
        this.api = new HttpClient();
    }

    @Override
    public ValidatableResponse addPet(final Pet petToAdd){
        return api.doPost(PET_PATH, petToAdd).then();
    }

    @Override
    public List<Pet> findPetsByStatus(Pet.PetStatus... status) throws ApiResponseException {
        Map<String, Object> queryParams = new HashMap<>();
        List<Pet> pets = null;
        for(Pet.PetStatus st : status){
            queryParams.put("status", st.getValue());
        }
        Response resp = api.doGet(FIND_PET_BY_STATUS_PATH, queryParams);
        try {
            pets = Arrays.asList(new ObjectMapper().readValue(resp.asString(), Pet[].class));
        } catch (Exception e) {
            throw new ApiResponseException(resp.getStatusCode(), resp.path("message"));
        }
        return pets;
    }

    @Override
    public Pet findPetById(long id) throws ApiResponseException{
       Pet pet = null;
       Response resp = api.doGet(PET_PATH + id);
       try{
           pet = resp.as(Pet.class);
       } catch(Exception e){
           throw new ApiResponseException(resp.getStatusCode(), resp.path("message"));
       }
        return pet;
    }

    @Override
    public ValidatableResponse updatePet(final Pet pet){
        assert pet != null;
        return api.doPut(PET_PATH, pet).then();
    }

    @Override
    public ValidatableResponse deletePetById(long id){
       return api.doDelete(PET_PATH + id).then();
    }

    @Override
    public ValidatableResponse uploadImageByPetId(long petId, String pathToImage, String additionalMetadata) {
        return given()
                .contentType("multipart/form-data")
                .multiPart(new File(pathToImage))
                .multiPart("additionalMetadata", additionalMetadata)
                .when().post(PET_PATH + petId + "/uploadImage")
                .then();
    }

    @Override
    public ValidatableResponse updatePetNameAndStatusById(long petId, String petName, Pet.PetStatus status) {
        return given()
                .contentType(ContentType.URLENC)
                .formParam("name", petName)
                .formParam("status", status.getValue())
                .when().post(PET_PATH + petId)
                .then();
    }
}
