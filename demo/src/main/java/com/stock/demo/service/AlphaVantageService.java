package com.stock.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.stock.demo.model.HistoricalPrice;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlphaVantageService {

    private static final String API_KEY = "46JSNIBQ8H8WGHF6"; // Replace with your real key
    private static final String BASE_URL = "https://www.alphavantage.co/query";

    private final RestTemplate restTemplate = new RestTemplate();

    public List<HistoricalPrice> getHistorical(String symbol, String function, String interval, LocalDateTime from, LocalDateTime to) {
        String url = String.format("%s?function=%s&symbol=%s%s&apikey=%s",
                BASE_URL,
                function,
                symbol,
                interval != null ? "&interval=" + interval : "",
                API_KEY
        );
//        ZoneId alphaVantageZone = ZoneId.of("America/New_York");
//        ZonedDateTime fromZoned = from.atZone(ZoneId.systemDefault()).withZoneSameInstant(alphaVantageZone);
//        ZonedDateTime toZoned = to.atZone(ZoneId.systemDefault()).withZoneSameInstant(alphaVantageZone);

        JsonNode root;
        try {
            root = restTemplate.getForObject(url, JsonNode.class);

        } catch (Exception e) {
            System.err.println("Failed to fetch Alpha Vantage data: " + e.getMessage());
            return new ArrayList<>();
        }

        JsonNode timeSeriesNode;
        if (function.contains("INTRADAY")) {
            timeSeriesNode = root.path("Time Series (" + interval + ")");
        } else if ("TIME_SERIES_DAILY".equals(function)) {
            timeSeriesNode = root.path("Time Series (Daily)");
        } else {
            System.err.println("Unsupported function: " + function);
            return new ArrayList<>();
        }

        if (timeSeriesNode.isMissingNode() || timeSeriesNode.isEmpty()) {
            System.err.println("No time series data available from Alpha Vantage.");
            return new ArrayList<>();
        }

        List<HistoricalPrice> prices = new ArrayList<>();
        DateTimeFormatter formatter = function.contains("INTRADAY")
                ? DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                : DateTimeFormatter.ofPattern("yyyy-MM-dd");

        timeSeriesNode.fields().forEachRemaining(entry -> {
            try {
                String timestamp = entry.getKey();
                JsonNode value = entry.getValue();
                double closePrice = value.path("4. close").asDouble();

                LocalDateTime parsedTime = function.contains("INTRADAY")
                        ? LocalDateTime.parse(timestamp, formatter)
                        : LocalDateTime.of(LocalDate.parse(timestamp, formatter), LocalTime.MIDNIGHT);

////                if ((parsedTime.isEqual(from) || parsedTime.isAfter(from)) &&
////                        (parsedTime.isEqual(to) || parsedTime.isBefore(to))) {
//
                    prices.add(new HistoricalPrice(symbol, parsedTime, closePrice));
////                }
//                if ((parsedTime.isEqual(fromZoned.toLocalDateTime()) || parsedTime.isAfter(fromZoned.toLocalDateTime())) &&
//                        (parsedTime.isEqual(toZoned.toLocalDateTime()) || parsedTime.isBefore(toZoned.toLocalDateTime()))) {
//
//                    prices.add(new HistoricalPrice(symbol, parsedTime, closePrice));
//                }

            } catch (Exception e) {
                System.err.println("Error parsing entry: " + entry.getKey() + " - " + e.getMessage());
            }
        });

        return prices;
    }
}
