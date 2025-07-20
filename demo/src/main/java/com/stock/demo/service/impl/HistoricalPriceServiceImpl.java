
package com.stock.demo.service.impl;

import com.stock.demo.model.HistoricalPrice;
import com.stock.demo.repository.HistoricalPriceRepository;
import com.stock.demo.service.HistoricalPriceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoricalPriceServiceImpl implements HistoricalPriceService {

    private final HistoricalPriceRepository repository;

    public HistoricalPriceServiceImpl(HistoricalPriceRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<HistoricalPrice> getPricesBySymbol(String symbol) {
        return repository.findBySymbolOrderByTimestampDesc(symbol);
    }

    @Override
    public List<HistoricalPrice> getPricesByCustomRange(String symbol, LocalDateTime start, LocalDateTime end) {
        return repository.findBySymbolAndTimestampBetweenOrderByTimestampAsc(symbol, start, end);
    }

    @Override
    public List<HistoricalPrice> getPricesByRange(String symbol, String range) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;

        switch (range.toUpperCase()) {
            case "1H":
                start = now.minusHours(1);
                break;
            case "1D":
                start = now.minusDays(1);
                break;
            case "1W":
                start = now.minusWeeks(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid range: " + range);
        }

        return repository.findBySymbolAndTimestampBetweenOrderByTimestampAsc(symbol, start, now);
    }

}

