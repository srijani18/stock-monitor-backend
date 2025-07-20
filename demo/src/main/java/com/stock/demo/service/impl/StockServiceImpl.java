
package com.stock.demo.service.impl;
import com.stock.demo.model.Stock;
import java.util.Random;
import com.stock.demo.repository.StockRepository;
import com.stock.demo.service.StockService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;

    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Stock getStockBySymbol(String symbol) {
        Optional<Stock> stock = stockRepository.findBySymbol(symbol);
        return stock.orElse(null); // or throw exception if preferred
    }

    @Override
    public List<Stock> getStocksBySymbols(List<String> symbols) {
        return stockRepository.findBySymbolIn(symbols);
    }

    @Override
    public List<Stock> getTopMovers() {
        // Dummy logic for now: return first 5 stocks sorted by price descending
        return stockRepository.findTop5ByOrderByCurrentPriceDesc();
    }

    @Override
    public void updateRandomPrices() {
        List<Stock> stocks = stockRepository.findAll();
        Random random = new Random();

        for (Stock stock : stocks) {
            double change = (random.nextDouble() - 0.5) * 10; // Random change between -5 and +5
            double newPrice = Math.max(1, stock.getCurrentPrice() + change); // Don't allow price < 1
            stock.setCurrentPrice(newPrice);
        }

        stockRepository.saveAll(stocks); // Persist changes
    }
}
