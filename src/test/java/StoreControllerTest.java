import io.petstore.dto.Order;
import io.petstore.dto.Pet;
import io.petstore.service.PetService;
import io.petstore.service.PetStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class StoreControllerTest extends BaseTest {

    @Autowired
    private PetStoreService petStoreService;

    @Autowired
    private PetService petService;

    @Test
    public void orderForPetShouldBePlaced() {
        long petId = 2L;
        Pet petToAdd = Pet.builder()
                .name("Richard")
                .status(Pet.PetStatus.AVAILABLE)
                .id(petId)
                .build();
        petService.addPet(petToAdd).then().statusCode(200);

        Order orderToAdd = Order.builder()
                .petId(petToAdd.getId())
                .quantity(1)
                .id(1L).status(Order.OrderStatus.PLACED)
                .build();
        petStoreService.placePetOrder(orderToAdd)
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
        Pet petToAdd = Pet.builder()
                .name("newPet")
                .status(Pet.PetStatus.AVAILABLE)
                .id(3L)
                .build();
        petService.addPet(petToAdd).then().statusCode(200);

        Order orderToAdd = Order.builder()
                .petId(petToAdd.getId())
                .quantity(1)
                .id(3L).status(Order.OrderStatus.PLACED)
                .build();
        petStoreService.placePetOrder(orderToAdd)
                .statusCode(200)
                .body("quantity", equalTo(1))
                .body("status", equalTo(Order.OrderStatus.PLACED.getValue()));

        petStoreService.deleteOrderById(orderToAdd.getId())
                .then()
                .statusCode(200);
    }
}
