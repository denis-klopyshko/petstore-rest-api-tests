package org.owm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UVData {
    private float lon;
    private float lat;
    private String date_iso;
    private int date;
    private float value;

    public UVData() {
    }
}
