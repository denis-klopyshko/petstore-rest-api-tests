package io.petstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Arrays;

@Data
@AllArgsConstructor
@Builder
public class Pet {
    private long id;
    private Category category;
    private String name;
    private Tag[] tags;
    private String[] photoUrls;
    private PetStatus status;

    public Pet() {
    }

    public enum PetStatus {
        @JsonProperty("available")
        AVAILABLE("available"),

        @JsonProperty("pending")
        PENDING("pending"),

        @JsonProperty("sold")
        SOLD("sold");

        private String value;

        public String getValue() {
            return value;
        }

        PetStatus(String value) {
            this.value = value;
        }
    }
}
