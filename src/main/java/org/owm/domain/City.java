package org.owm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class City {
    private long id;
    private String name;
    private String country;
    private Coordinates coord;
    private long population;

    public City() {
    }
}
