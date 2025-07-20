
package com.stock.demo.service;

import com.stock.demo.model.HistoricalPrice;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoricalPriceService {
    List<HistoricalPrice> getPricesBySymbol(String symbol);
    List<HistoricalPrice> getPricesByCustomRange(String symbol, LocalDateTime start, LocalDateTime end);

    List<HistoricalPrice> getPricesByRange(String symbol, String range);
}
