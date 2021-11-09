import io.petstore.dto.Category;
import io.petstore.dto.Pet;
import io.petstore.dto.Tag;
import io.petstore.exception.ApiResponseException;
import io.petstore.service.PetService;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.number.OrderingComparison.greaterThan;

public class PetControllerTest extends BaseTest {
    @Autowired
    private PetService petService;

    private String PATH_TO_IMAGE = "src/main/resources/petImage.jpg";

    @Test
    public void newPetShouldBeAdded() {
        Category category = new Category(0, "home_pet");
        Tag[] tag = {new Tag(0, "tag1")};
        long petId = 1L;
        String petName = "Richard";
        Pet petToAdd = Pet.builder()
                .name(petName)
                .status(Pet.PetStatus.SOLD)
                .id(petId)
                .tags(tag)
                .category(category)
                .photoUrls(new String[]{"test"})
                .build();

        petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .body("name", equalTo(petName));

        Pet addedPet = petService.findPetById(petId).as(Pet.class);

        assertThat(petToAdd, equalTo(addedPet));
    }

    @Test
    public void newPetWithMultiplyTagsShouldBeAdded() {
        Tag[] tag = {new Tag(1, "dog"), new Tag(2, "rottweiler")};
        long petId = 2L;
        Pet petToAdd = Pet.builder()
                .name("Richard")
                .status(Pet.PetStatus.SOLD)
                .id(petId)
                .tags(tag)
                .photoUrls(new String[]{"test"})
                .build();

        petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .body("tags[0].name", equalTo("dog"))
                .body("tags[1].name", equalTo("rottweiler"));
    }

    @Test
    public void shouldUpdatePetNameAndStatusById() {
        Pet petToAdd = Pet.builder()
                .id(1L)
                .name("Max")
                .status(Pet.PetStatus.AVAILABLE)
                .build();

        petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .body("name", equalTo("Max"));

        petService.updatePetNameAndStatusById(1L, "Maxik", Pet.PetStatus.SOLD)
                .then()
                .statusCode(200);

        Pet updatedPet = petService.findPetById(1L).as(Pet.class);

        assertThat(updatedPet.getName(), equalTo("Maxik"));
        assertThat(updatedPet.getStatus(), equalTo(Pet.PetStatus.SOLD));
    }

    @Test
    public void shouldNotUpdateNonExistingPet() {
        petService.deletePetById(101L);
        petService.updatePetNameAndStatusById(101L, "Cat", Pet.PetStatus.SOLD)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    /**
     * Replaces all existing pet data with new pet data
     */
    @Test
    public void shouldUpdatePetWithReplacingExistingPetData() {
        Pet petToAdd = Pet.builder()
                .id(3L)
                .status(Pet.PetStatus.AVAILABLE)
                .name("PetDog")
                .build();
        petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .body("name", equalTo("PetDog"))
                .body("status", equalTo(Pet.PetStatus.AVAILABLE.getValue()));

        Pet petToUpdate = Pet.builder()
                .id(3L)
                .status(Pet.PetStatus.PENDING)
                .name("PetCat").build();
        petService.updatePet(petToUpdate)
                .then()
                .statusCode(200)
                .body("name", equalTo("PetCat"))
                .body("status", equalTo(Pet.PetStatus.PENDING.getValue()));
    }

    @Test
    public void shouldNotFindNonExistingPetById() {
        long petId = 1001L;
        petService.deletePetById(petId);
        petService.findPetById(petId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void petShouldBeDeleted() {
        long petId = 1L;
        Pet petToBeDeleted = Pet.builder()
                .id(petId)
                .status(Pet.PetStatus.AVAILABLE)
                .name("Masik")
                .build();
        petService.addPet(petToBeDeleted)
                .then()
                .statusCode(200)
                .body("name", equalTo("Masik"));

        petService.deletePetById(petId)
                .then()
                .statusCode(200);

        petService.findPetById(petId)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);;
    }

    @Test
    public void shouldReturnSoldPetsByStatus() throws ApiResponseException {
        Pet petToAdd = Pet.builder()
                .id(2001L)
                .status(Pet.PetStatus.SOLD)
                .name("Rich")
                .build();
        petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .body("name", equalTo("Rich"));

        List<Pet> pets = petService.findPetsByStatus(Pet.PetStatus.SOLD).jsonPath().getList(".");
        assertThat(pets, not(empty()));
        for (Pet pet : pets) {
            assertThat("Pet Status", pet.getStatus(), equalTo(Pet.PetStatus.SOLD));
        }
    }

    @Test
    public void shouldReturnSoldAndPendingPetsByStatus() throws ApiResponseException {
        List<Pet> pets = petService.findPetsByStatus(Pet.PetStatus.SOLD, Pet.PetStatus.PENDING)
                .jsonPath().getList(".");
        for (Pet pet : pets) {
            assertThat("Pet Status", pet.getStatus(),
                    anyOf(equalTo(Pet.PetStatus.SOLD), equalTo(Pet.PetStatus.PENDING)));
        }
    }

    @Test
    public void shouldUploadPetsImageByPetId() {
        String additionalData = "FavouritePicture";
        petService.uploadImageByPetId(1L, PATH_TO_IMAGE, additionalData)
                .then()
                .statusCode(200)
                .body("message", containsString(additionalData));
    }

    @Test
    public void shouldNotUploadNotImageFile() {
        Pet petToAdd = Pet.builder()
                .id(401L)
                .name("addedPet")
                .build();
        petService.addPet(petToAdd)
                .then()
                .statusCode(200).contentType(ContentType.JSON)
                .body("name", equalTo("addedPet"));

        String path_to_pdf = "src/main/resources/pdf-sample.pdf";
        petService.uploadImageByPetId(401L, path_to_pdf, "")
                .then()
                .statusCode(400);
    }

    /**
     * Expect that we can't load image for non existing pet
     * May be 404 Not Found with message Pet Not Found
     */
    @Test
    public void shouldNotUploadImageForNonExistingPet() {
        petService.deletePetById(1L);
        petService.uploadImageByPetId(1L, PATH_TO_IMAGE, "")
                .then()
                .statusCode(404);
    }

    /**
     * 1. Upload image for pet by pet id
     * 2. Get pet by id
     * 3. Сheck if pet has photoUrl
     */
    @Test
    public void petShouldContainsImageUrlAfterImageUploading() throws ApiResponseException {
        Pet petToAdd = Pet.builder()
                .id(101L)
                .name("PetWithPhoto")
                .build();
        petService.addPet(petToAdd)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("name", equalTo("PetWithPhoto"));

        petService.uploadImageByPetId(101L, PATH_TO_IMAGE, "")
                .then()
                .statusCode(200);

        Pet pet = petService.findPetById(101L).as(Pet.class);
        assertThat(pet.getPhotoUrls().length, greaterThan(0));
    }
}

