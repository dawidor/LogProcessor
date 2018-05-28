package com.csgroup.model;

import com.csgroup.enums.State;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogEvent {

    private String id;
    private State state;
    private String type;
    private Long timestamp;
    private String host;

    public LogEvent() {}
}
