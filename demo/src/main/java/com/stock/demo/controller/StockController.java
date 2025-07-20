package com.stock.demo.controller;

import com.stock.demo.model.Stock;
import com.stock.demo.service.StockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    // ✅ GET /api/stocks/{symbol} — Get stock details by symbol
    @GetMapping("/{symbol}")
    public ResponseEntity<Stock> getStockBySymbol(@PathVariable String symbol) {
        Stock stock = stockService.getStockBySymbol(symbol);
        if (stock != null) {
            return ResponseEntity.ok(stock);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ GET /api/stocks/summary — Quick summary (e.g., top 5 movers or selected)
    @GetMapping("/summary")
    public List<Stock> getStockSummary(@RequestParam(required = false) List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            return stockService.getTopMovers(); // fallback summary
        }
        return stockService.getStocksBySymbols(symbols);
    }

    // ✅ GET /api/stocks/live — This is handled by WebSocket, so REST can just confirm endpoint
    @GetMapping("/live")
    public String liveStockInfo() {
        return "WebSocket endpoint for live prices is active on /ws/stocks";
    }
}
