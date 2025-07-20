package com.stock.demo.model;//package com.stock.demo.model;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//
//
//@Data
//@Entity
//@Table(name = "historical_price")
//public class HistoricalPrice {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long historicalPriceId;
//
//    private String symbol;
//
//    private Double price;
//
//    private LocalDateTime timestamp;
//}


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historical_price")
public class HistoricalPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historical_price_id")
    private Long id;

    private String symbol;
    private Double price;
    private LocalDateTime timestamp;
    public HistoricalPrice(String symbol, LocalDateTime timestamp, Double price) {
        this.symbol = symbol;
        this.timestamp = timestamp;
        this.price = price;
    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getPrice() {
        return price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

