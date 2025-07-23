package com.stock.demo.controller;
import com.stock.demo.model.Alert;
import com.stock.demo.service.AlertService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "http://localhost:3000")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public Alert createAlert(@RequestBody Alert alert) {
        return alertService.createAlert(alert);
    }

    @PutMapping("/{id}")
    public Alert updateAlert(@PathVariable Long id, @RequestBody Alert alert) {
        return alertService.updateAlert(id, alert);
    }

    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        alertService.deleteAlert(id);
    }

    @GetMapping
    public List<Alert> getAllAlerts() {
        return alertService.getAll(); // or alertService.getAlertsByUser(userId) if using auth
    }

    @PutMapping("/{id}/reset")
    public Alert resetAlert(@PathVariable Long id) {
        return alertService.resetAlert(id);
    }


}
