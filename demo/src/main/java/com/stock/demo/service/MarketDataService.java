package com.stock.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class MarketDataService {

    private static final String API_KEY = "d1rq4i9r01qth6r6c5pgd1rq4i9r01qth6r6c5q0";
    private static final String BASE_URL = "https://finnhub.io/api/v1/quote";

    private final RestTemplate restTemplate = new RestTemplate();

    public Optional<Double> fetchLivePrice(String symbol) {
        String url = BASE_URL + "?symbol=" + symbol + "&token=" + API_KEY;

        try {
            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
            JsonNode body = response.getBody();
            if (body != null && body.has("c")) {
                return Optional.of(body.get("c").asDouble());
            }
        } catch (Exception e) {
            System.err.println("Error fetching price for " + symbol + ": " + e.getMessage());
        }

        return Optional.empty();
    }
}


