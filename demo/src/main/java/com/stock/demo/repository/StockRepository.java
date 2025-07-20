package com.stock.demo.repository;

import org.springframework.stereotype.Repository;
import com.stock.demo.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySymbol(String symbol);

    List<Stock> findBySymbolIn(List<String> symbols);

    List<Stock> findTop5ByOrderByCurrentPriceDesc();
    // You can add custom query methods here if needed
}
