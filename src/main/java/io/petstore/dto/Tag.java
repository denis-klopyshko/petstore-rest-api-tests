package io.petstore.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class Tag {
    private long id;
    private String name;

    public Tag() {
    }
}
