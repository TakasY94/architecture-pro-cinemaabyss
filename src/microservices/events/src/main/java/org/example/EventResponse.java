package org.example;

import lombok.Data;

@Data
public class EventResponse {
    private String status;
    private Integer partition;
    private Long offset;
    private Event<?> event;
}
