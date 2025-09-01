package org.example;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<String> proxyGetMovieById(@RequestParam(required = false) String id) {
        String targetUrl = determineTargetUrl();
        String fullUrl = targetUrl + "/api/movies";
        if (id != null) {
            fullUrl = targetUrl + "/api/movies?id=" + id;
            log.info("Proxying /api/movies?id={} request to {}", id, fullUrl);
        } else log.info("Proxying /api/movies request to {}", fullUrl);
        try {
            return restTemplate.getForEntity(fullUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @PostMapping("/api/movies")
    public ResponseEntity<String> proxyCreateMovieRequest(@RequestBody String body) {
        String targetUrl = determineTargetUrl();
        String fullUrl = targetUrl + "/api/movies";
        log.info("Proxying POST /api/movies request to {}", fullUrl);
        try {
            return restTemplate.postForEntity(fullUrl, body, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @GetMapping("/api/users")
    public ResponseEntity<String> proxyGetUserById(@RequestParam(required = false) String id) {
        String fullUrl = monolithUrl + "/api/users";
        if (id != null){
            fullUrl = monolithUrl + "/api/users?id=" + id;
            log.info("Proxying /api/users?id={} request to {}", id, fullUrl);
        } else log.info("Proxying /api/users request to {}", fullUrl);
        try {
            return restTemplate.getForEntity(fullUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> proxyCreateUserRequest(@RequestBody String body) {
        String fullUrl = monolithUrl + "/api/users";
        log.info("Proxying POST /api/users request to {}", fullUrl);
        try {
            return restTemplate.postForEntity(fullUrl, body, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @GetMapping("/api/payments")
    public ResponseEntity<String> proxyGetPaymentById(@RequestParam(required = false) String id) {
        String fullUrl = monolithUrl + "/api/payments";
        if (id != null) {
            fullUrl = monolithUrl + "/api/payments?id=" + id;
            log.info("Proxying /api/payments?id={} request to {}", id, fullUrl);
        } else log.info("Proxying /api/payments request to {}", fullUrl);
        try {
            return restTemplate.getForEntity(fullUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @PostMapping("/api/payments")
    public ResponseEntity<String> proxyCreatePaymentRequest(@RequestBody String body) {
        String fullUrl = monolithUrl + "/api/payments";
        log.info("Proxying POST /api/payments request to {}", fullUrl);
        try {
            return restTemplate.postForEntity(fullUrl, body, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @GetMapping("/api/subscriptions")
    public ResponseEntity<String> proxyGetSubscriptionById(@RequestParam(required = false) String id) {
        String fullUrl = monolithUrl + "/api/subscriptions";
        if (id != null) {
            fullUrl = monolithUrl + "/api/subscriptions?id=" + id;
            log.info("Proxying /api/subscriptions?id={} request to {}", id, fullUrl);
        } else log.info("Proxying /api/subscriptions request to {}", fullUrl);
        try {
            return restTemplate.getForEntity(fullUrl, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error proxying request: " + e.getMessage());
        }
    }

    @PostMapping("/api/subscriptions")
    public ResponseEntity<String> proxyCreateSubscriptionRequest(@RequestBody String body) {
        String fullUrl = monolithUrl + "/api/subscriptions";
        log.info("Proxying POST /api/subscriptions request to {}", fullUrl);
        try {
            return restTemplate.postForEntity(fullUrl, body, String.class);
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