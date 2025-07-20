package com.stock.demo.service;

import com.stock.demo.model.Stock;

import java.util.List;

public interface StockService {
    List<Stock> getAllStocks();

    Stock getStockBySymbol(String symbol);

    List<Stock> getTopMovers();

    List<Stock> getStocksBySymbols(List<String> symbols);

    void updateRandomPrices();
}
