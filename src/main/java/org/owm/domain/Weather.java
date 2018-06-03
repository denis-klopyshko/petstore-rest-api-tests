package org.owm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Weather {
    private long id;
    private String description;
    private String main;
    private String icon;

    public Weather() {
    }
}
