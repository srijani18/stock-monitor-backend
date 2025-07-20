package com.stock.demo.controller;

import com.stock.demo.model.Stock;
import com.stock.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class StockWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;
    private final StockService stockService;

    @Autowired
    public StockWebSocketController(SimpMessagingTemplate messagingTemplate, StockService stockService) {
        this.messagingTemplate = messagingTemplate;
        this.stockService = stockService;
    }

    @Scheduled(fixedRate = 5000)
    public void broadcastLiveStockData() {
        List<Stock> stocks = stockService.getAllStocks();
        messagingTemplate.convertAndSend("/topic/live-stocks", stocks);
    }
}
