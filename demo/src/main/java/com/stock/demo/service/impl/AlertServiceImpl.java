package com.stock.demo.service.impl;

import com.stock.demo.model.Alert;
import com.stock.demo.repository.AlertRepository;
import com.stock.demo.service.AlertService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public Alert createAlert(Alert alert) {
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
            alert.setTriggered(updatedAlert.isTriggered());
            alert.setTriggeredAt(updatedAlert.getTriggeredAt());
            return alertRepository.save(alert);
        } else {
            throw new RuntimeException("Alert not found");
        }
    }

    @Override
    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }

    public List<Alert> getAlertsByUser(String userId) {
        return alertRepository.findByUserId(userId);
    }
}
