package org.owm.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherData {
    private Coordinates coord;
    private List<Weather> weather;
    private String base;
    private Main main;
    private String visibility;
    private Wind wind;
    private Rain rain;
    private Clouds clouds;
    private Snow snow;
    private Sys sys;
    private long dt;
    private long id;
    private String name;
    private int cod;
    private String dt_txt;

    public WeatherData() {
    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Wind {
        private double speed;
        private double deg;
        private float gust;

        public Wind() {
        }
    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Rain {
        @JsonProperty("3h")
        private String volume;

        public Rain() {}

    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Snow {
        @JsonProperty("3h")
        private String volume;

        public Snow() {}
    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Clouds {
        private double all;
        private int today;

        public Clouds() {
        }
    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Main {
        private double temp;
        private double pressure;
        private double humidity;
        private double temp_min;
        private double temp_max;
        private double sea_level;
        private double grnd_level;
        private double temp_kf;

        public Main() {
        }
    }

    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class Sys {
        private int type;
        private long id;
        private double message;
        private String country;
        private long sunrise;
        private long sunset;
        private String pod;

        public Sys() {
        }
    }
}
