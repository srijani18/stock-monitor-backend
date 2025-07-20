package com.stock.demo.controller;

import com.stock.demo.kafka.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class KafkaController {
    @Autowired
    private KafkaProducerService producer;

    // 🔁 Trigger stock price publishing manually (optional)
    @PostMapping("/publish-live-prices")
    public String publishLivePrices() {
        producer.publishStockPrices();
        return "✅ Live stock prices pushed to Kafka";
    }
}


