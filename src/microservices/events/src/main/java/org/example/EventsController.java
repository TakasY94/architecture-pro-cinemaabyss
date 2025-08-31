package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class EventsController {

    private static final Logger log = LoggerFactory.getLogger(EventsController.class);
    private final EventService eventService;

    public EventsController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/movies")
    public ResponseEntity<EventResponse> createMovieEvent(@RequestBody MovieEvent movieEvent) {
        log.info("Creating movie event: {}", movieEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createMovieEvent(movieEvent));
    }

    @PostMapping("/users")
    public ResponseEntity<EventResponse> createUserEvent(@RequestBody UserEvent userEvent) {
        log.info("Creating user event: {}", userEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createUserEvent(userEvent));
    }

    @PostMapping("/payments")
    public ResponseEntity<EventResponse> createPaymentEvent(@RequestBody PaymentEvent paymentEvent) {
        log.info("Creating payment event: {}", paymentEvent);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.createPaymentEvent(paymentEvent));
    }

    @GetMapping("/health")
    public ResponseEntity<Object> getHealth() {
        return ResponseEntity.ok().body(new HealthStatus(true));
    }

    private static class HealthStatus {
        private boolean status;

        public HealthStatus(boolean status) {
            this.status = status;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
