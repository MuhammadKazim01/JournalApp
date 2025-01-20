package com.graphode.journalapp.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse {

    @Getter
    @Setter
    public class Current {
        @JsonProperty("feelslike_c")
        private int feelsLike;
    }

    private Current current;
}