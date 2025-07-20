package com.stock.demo.kafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.stock.demo.service.MarketDataService;

import java.util.List;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MarketDataService marketDataService;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, MarketDataService marketDataService) {
        this.kafkaTemplate = kafkaTemplate;
        this.marketDataService = marketDataService;
    }
//
//    public void sendMessage(String topic, String message) {
//        kafkaTemplate.send(topic, message);
//    }

    @Scheduled(fixedRate = 5000)
    public void publishStockPrices() {
        List<String> symbols = List.of("AAPL", "GOOGL", "MSFT");

        symbols.forEach(symbol -> {
            marketDataService.fetchLivePrice(symbol).ifPresent(price -> {
                String json = String.format("{\"symbol\":\"%s\", \"price\":%.2f}", symbol, price);
                kafkaTemplate.send("stock-prices", json);
            });
        });
    }
}


