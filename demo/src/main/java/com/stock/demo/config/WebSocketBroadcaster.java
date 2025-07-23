package com.stock.demo.config;

import com.stock.demo.model.Stock;
import com.stock.demo.service.AlertService;
import com.stock.demo.service.MarketDataService;
import com.stock.demo.service.StockService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketBroadcaster {

    private final SimpMessagingTemplate messagingTemplate;
    private final StockService stockService;
    private final MarketDataService marketDataService;
    private final AlertService alertService;

    public WebSocketBroadcaster(SimpMessagingTemplate messagingTemplate, StockService stockService, MarketDataService marketDataService, AlertService alertService ) {
        this.messagingTemplate = messagingTemplate;
        this.stockService = stockService;
        this.marketDataService = marketDataService;
        this.alertService = alertService;
    }

    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void broadcastPrices() {
        //stockService.updateRandomPrices(); // 🔁 Step 1: update data
        List<Stock> stocks = stockService.getAllStocks();


        stocks.forEach(stock -> {

            marketDataService.fetchLivePrice(stock.getSymbol())
                    .ifPresent(currentPrice -> {
                        stock.setCurrentPrice(currentPrice);

                        // ✅ Check and trigger alerts
                        alertService.checkAndTriggerAlerts(stock.getSymbol(), currentPrice);
                    });
        });


        // for testing
//        stocks.forEach(stock -> {
//            double currentPrice = 250.0; // 🚨 Force test price
//            stock.setCurrentPrice(currentPrice);
//
//            // ✅ Check and trigger alerts
//            alertService.checkAndTriggerAlerts(stock.getSymbol(), currentPrice);
//        });

        System.out.println("📡 Sending to frontend: " + stocks);
        messagingTemplate.convertAndSend("/topic/live-stocks", stocks);
    }
}



