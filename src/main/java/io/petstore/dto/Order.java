package io.petstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private long id;
    private long petId;
    private long quantity;
    private Date shipDate;
    private OrderStatus status;
    private boolean complete;

    public enum OrderStatus {
        @JsonProperty("placed")
        PLACED("placed"),
        @JsonProperty("approved")
        APPROVED("approved"),
        @JsonProperty("delivered")
        DELIVERED("delivered");

        private String value;

        public String getValue() {
            return value;
        }

        OrderStatus(String value) {
            this.value = value;
        }
    }
}
