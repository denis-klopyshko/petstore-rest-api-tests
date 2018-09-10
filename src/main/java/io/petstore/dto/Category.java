package io.petstore.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class Category {
    private long id;
    private String name;

    public Category() {
    }
}
