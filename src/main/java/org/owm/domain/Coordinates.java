package org.owm.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Coordinates{
    @JsonAlias({"lat", "Lat"})
    private double lat;

    @JsonAlias({"lon", "Lon"})
    private double lon;

    public Coordinates() {
    }
}