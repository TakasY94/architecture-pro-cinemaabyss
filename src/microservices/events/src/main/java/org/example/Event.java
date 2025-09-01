package org.example;

import lombok.Data;

import java.time.Instant;

@Data
public class Event<T> {
    private String id;
    private String type;
    private Instant timestamp;
    private T payload;
}
