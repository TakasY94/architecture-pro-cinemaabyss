package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEvent {
    private Integer userId;
    private String username;
    private String email;
    private String action;
    private Instant timestamp;
}