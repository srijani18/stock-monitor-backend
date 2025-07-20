package com.stock.demo.repository;
import com.stock.demo.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    //List<Alert> findByUsername(String username);

    List<Alert> findByUserId(String userId);
}
