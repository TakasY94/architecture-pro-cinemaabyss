package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaEventListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaEventListener.class);

    @KafkaListener(topics = {"${kafka.topics.movie}", "${kafka.topics.user}", "${kafka.topics.payment}"}, groupId = "${spring.kafka.consumer.group-id}")
    public void listen(Event<?> event) {
        logger.info("Received event: id={}, type={}, timestamp={}, payload={}",
                event.getId(), event.getType(), event.getTimestamp(), event.getPayload());
    }
}