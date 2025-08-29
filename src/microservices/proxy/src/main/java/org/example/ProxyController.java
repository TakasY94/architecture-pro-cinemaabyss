package org.example;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate;
    private final Random random = new Random();

    @Value("${GRADUAL_MIGRATION:false}")
    private boolean gradualMigrationEnabled;

    @Value("${MOVIES_MIGRATION_PERCENT:0}")
    private int moviesMigrationPercent;

    @Value("${MONOLITH_URL:http://monolith:8080}")
    private String monolithUrl;

    @Value("${MOVIES_SERVICE_URL:http://movies-service:8081}")
    private String moviesServiceUrl;

    @Value("${EVENTS_SERVICE_URL:http://events-service:8082}")
    private String eventsServiceUrl;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/api/movies")
    public ResponseEntity<String> proxyMoviesRequest() {
        String targetUrl = determineTargetUrl();
        String fullUrl = targetUrl + "/api/movies";

        try {
            return restTemplate.getForEntity(fullUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @GetMapping("/api/users")
    public ResponseEntity<String> proxyUsersRequest() {
        String fullUrl = monolithUrl + "/api/users";

        try {
            return restTemplate.getForEntity(fullUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    private String determineTargetUrl() {
        if (!gradualMigrationEnabled) {
            return monolithUrl;
        }

        // Generate a random number between 0 and 100
        int randomValue = random.nextInt(100);
        return randomValue < moviesMigrationPercent ? moviesServiceUrl : monolithUrl;
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        if (gradualMigrationEnabled) {
            return ResponseEntity.ok("Gradual migration is enabled");
        } else {
            return ResponseEntity.ok("Gradual migration is disabled");
        }
    }
}