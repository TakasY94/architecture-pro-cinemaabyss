package org.example;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
public class ProxyController {

    private static final Logger log = LoggerFactory.getLogger(ProxyController.class);
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

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/api/movies")
    public ResponseEntity<String> proxyMoviesRequest() {
        String targetUrl = determineTargetUrl();
        String fullUrl = targetUrl + "/api/movies";
        log.info("Proxying /api/movies request to {}", fullUrl);
        try {
            return restTemplate.getForEntity(fullUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @GetMapping("/api/users")
    public ResponseEntity<String> proxyUsersRequest() {
        String fullUrl = monolithUrl + "/api/users";
        log.info("Proxying /api/users request to {}", fullUrl);
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
        log.info("Health check");
        if (gradualMigrationEnabled) {
            return ResponseEntity.ok("Gradual migration is enabled");
        } else {
            return ResponseEntity.ok("Gradual migration is disabled");
        }
    }
}