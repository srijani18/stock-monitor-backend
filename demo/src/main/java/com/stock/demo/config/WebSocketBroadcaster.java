package com.stock.demo.config;

import com.stock.demo.model.Stock;
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

    public WebSocketBroadcaster(SimpMessagingTemplate messagingTemplate, StockService stockService, MarketDataService marketDataService) {
        this.messagingTemplate = messagingTemplate;
        this.stockService = stockService;
        this.marketDataService = marketDataService;
    }

    @Scheduled(fixedRate = 5000) // Every 5 seconds
    public void broadcastPrices() {
        //stockService.updateRandomPrices(); // 🔁 Step 1: update data
        List<Stock> stocks = stockService.getAllStocks();
        // ✅ Step 2: fetch updated
        //List<Stock> stocks = List.of(new Stock("AAPL",192.45));
        stocks.forEach(stock ->
                marketDataService.fetchLivePrice(stock.getSymbol())
                        .ifPresent(stock::setCurrentPrice)
        );
        System.out.println("📡 Sending to frontend: " + stocks);
        messagingTemplate.convertAndSend("/topic/live-stocks", stocks);
    }
}



