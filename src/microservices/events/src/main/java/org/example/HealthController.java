package org.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Object> getHealth() {
        return ResponseEntity.ok().body(new HealthController.HealthStatus(true));
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
