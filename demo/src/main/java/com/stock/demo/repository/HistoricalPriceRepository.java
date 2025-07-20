package com.stock.demo.repository;
import com.stock.demo.model.HistoricalPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
public interface HistoricalPriceRepository extends JpaRepository<HistoricalPrice, Long>  {
    List<HistoricalPrice> findBySymbolAndTimestampBetweenOrderByTimestampAsc(String symbol, LocalDateTime start, LocalDateTime end);
    List<HistoricalPrice> findBySymbolOrderByTimestampDesc(String symbol);


}
