package org.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentEvent {
    private Integer paymentId;
    private Integer userId;
    private Float amount;
    private String status;
    private Instant timestamp;
    private String methodType;
}
