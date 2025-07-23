package com.stock.demo.service.impl;

import com.stock.demo.model.Alert;
import com.stock.demo.repository.AlertRepository;
import com.stock.demo.service.AlertService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final SimpMessagingTemplate messagingTemplate;
    public AlertServiceImpl(AlertRepository alertRepository, SimpMessagingTemplate messagingTemplate) {
        this.alertRepository = alertRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public Alert createAlert(Alert alert) {
        alert.setTriggered(false);
        alert.setTriggeredAt(null);
        return alertRepository.save(alert);
    }

    @Override
    public Alert updateAlert(Long id, Alert updatedAlert) {
        Optional<Alert> existing = alertRepository.findById(id);
        if (existing.isPresent()) {
            Alert alert = existing.get();
            alert.setSymbol(updatedAlert.getSymbol());
            alert.setCondition(updatedAlert.getCondition());
            alert.setTargetPrice(updatedAlert.getTargetPrice());
            // Don't update 'triggered' unless resetting
            return alertRepository.save(alert);
        } else {
            throw new RuntimeException("Alert not found with id: " + id);
        }
    }

    @Override
    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }

    @Override
    public List<Alert> getAlertsByUser(String userId) {
        return alertRepository.findByUserId(userId);
    }

    @Override
    public List<Alert> getAll() {
        return alertRepository.findAll();
    }

    @Override
    public void checkAndTriggerAlerts(String symbol, double currentPrice) {
        List<Alert> pending = alertRepository.findByTriggeredFalse();

        for (Alert alert : pending) {
            if (!alert.getSymbol().equalsIgnoreCase(symbol)) continue;

            boolean shouldTrigger =
                    (alert.getCondition().equals(">") && currentPrice > alert.getTargetPrice()) ||
                            (alert.getCondition().equals("<") && currentPrice < alert.getTargetPrice());

            if (shouldTrigger) {
                alert.setTriggered(true);
                alert.setTriggeredAt(LocalDateTime.now());
                alertRepository.save(alert);
                // (Optional) notify the user via WebSocket or email here


                messagingTemplate.convertAndSend("/topic/alerts-triggered", alert);
            }
        }
    }

    @Override
    public Alert resetAlert(Long id) {
        Optional<Alert> optional = alertRepository.findById(id);
        if (optional.isPresent()) {
            Alert alert = optional.get();
            alert.setTriggered(false);
            alert.setTriggeredAt(null);
            return alertRepository.save(alert);
        }
        throw new RuntimeException("Alert not found");
    }

}
