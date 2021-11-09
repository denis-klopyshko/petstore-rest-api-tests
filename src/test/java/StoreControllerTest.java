import io.petstore.dto.Order;
import io.petstore.dto.Pet;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class StoreControllerTest extends BaseTest {
    @Test
    public void orderForPetShouldBePlaced() {
        Pet petToAdd = Pet.ofNameAndStatus("Richard", Pet.PetStatus.AVAILABLE);
        Pet addedPet = successCreatePet(petToAdd);

        Order orderToAdd = Order.builder()
                .petId(addedPet.getId())
                .quantity(1)
                .status(Order.OrderStatus.PLACED)
                .build();

        petStoreService.placePetOrder(orderToAdd)
                .then()
                .statusCode(200)
                .body("quantity", equalTo(1))
                .body("status", equalTo(Order.OrderStatus.PLACED.getValue()));
    }

    @Test
    public void shouldNotFindNonExistingOrder() {
        petStoreService.deleteOrderById(1L);
        petStoreService.findOrderById(1L).then().statusCode(404);
    }

    @Test
    public void shouldDeletePlacedOrder() {
        Pet petToAdd = Pet.ofNameAndStatus("newPet", Pet.PetStatus.AVAILABLE);
        Pet addedPet = successCreatePet(petToAdd);

        Order orderToAdd = Order.builder()
                .petId(addedPet.getId())
                .quantity(1)
                .status(Order.OrderStatus.PLACED)
                .build();

        Response response = petStoreService.placePetOrder(orderToAdd);
        long id = response.jsonPath().getLong("id");
        response.then()
                .statusCode(200)
                .body("quantity", equalTo(1))
                .body("status", equalTo(Order.OrderStatus.PLACED.getValue()));

        petStoreService.deleteOrderById(id)
                .then()
                .statusCode(200);
    }
}
