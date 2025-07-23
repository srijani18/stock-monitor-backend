
package com.stock.demo.service;

import com.stock.demo.model.Alert;

import java.util.List;

public interface AlertService {
//    List<Alert> getAlertsForUser(String username);
    Alert createAlert(Alert alert);
    Alert updateAlert(Long id, Alert updatedAlert);
    void deleteAlert(Long id);

    List<Alert> getAlertsByUser(String userId);

    List<Alert> getAll();  // Optional, for admin/debugging

    void checkAndTriggerAlerts(String symbol, double currentPrice);

    Alert resetAlert(Long id);
}
