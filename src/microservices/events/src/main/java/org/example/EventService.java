package org.example;


import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final KafkaTemplate<String, Event<?>> kafkaTemplate;

    @Value("${kafka.topics.movie}")
    private String movieTopic;

    @Value("${kafka.topics.user}")
    private String userTopic;

    @Value("${kafka.topics.payment}")
    private String paymentTopic;

    public EventService(KafkaTemplate<String, Event<?>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public EventResponse createMovieEvent(MovieEvent movieEvent) {
        return sendEvent(createEvent("movie", movieEvent), movieTopic);
    }

    public EventResponse createUserEvent(UserEvent userEvent) {
        return sendEvent(createEvent("user", userEvent), userTopic);
    }

    public EventResponse createPaymentEvent(PaymentEvent paymentEvent) {
        return sendEvent(createEvent("payment", paymentEvent), paymentTopic);
    }

    private <T> Event<T> createEvent(String type, T payload) {
        Event<T> event = new Event<>();
        event.setId(UUID.randomUUID().toString());
        event.setType(type);
        event.setTimestamp(Instant.now());
        event.setPayload(payload);
        return event;
    }

    private EventResponse sendEvent(Event<?> event, String topic) {
        try {
            SendResult<String, Event<?>> result = kafkaTemplate.send(topic, event.getId(), event).get();
            EventResponse response = new EventResponse();
            response.setStatus("success");
            response.setPartition(result.getRecordMetadata().partition());
            response.setOffset(result.getRecordMetadata().offset());
            response.setEvent(event);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to send event to Kafka", e);
        }
    }
}
