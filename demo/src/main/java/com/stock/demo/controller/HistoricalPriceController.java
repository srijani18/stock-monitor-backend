

package com.stock.demo.controller;

import com.stock.demo.model.HistoricalPrice;
import com.stock.demo.service.AlphaVantageService;
import org.springframework.web.bind.annotation.*;

        import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/historical")
public class HistoricalPriceController {

    private final AlphaVantageService alphaVantageService;

    public HistoricalPriceController(AlphaVantageService alphaVantageService) {
        this.alphaVantageService = alphaVantageService;
    }


    // 🔹 1H → INTRADAY 5min, 1D → INTRADAY 15min, 1W → DAILY
    @GetMapping("/{symbol}/{range}")
    public List<HistoricalPrice> getByRange(@PathVariable String symbol, @PathVariable String range) {
        String function;
        String interval = null;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from;




        switch (range.toUpperCase()) {
            case "1H":
                function = "TIME_SERIES_INTRADAY";
                interval = "5min";
                from = now.minusHours(1);
                break;
            case "1D":
                function = "TIME_SERIES_INTRADAY";
                interval = "15min";
                from = now.minusDays(1);
                break;
            case "1W":
                function = "TIME_SERIES_DAILY";
                from = now.minusWeeks(1);
                break;
            default:
                throw new IllegalArgumentException("Invalid range: " + range);
        }

        return alphaVantageService.getHistorical(symbol, function, interval, from, now);
    }

    // 🔹 Custom range — use dynamic resolution and filter in Java
    @PostMapping("/custom")
    public List<HistoricalPrice> getCustomRange(@RequestBody Map<String, String> request) {
        String symbol = request.get("symbol");
        LocalDateTime start = LocalDateTime.parse(request.get("startDate"));
        LocalDateTime end = LocalDateTime.parse(request.get("endDate"));

        String function;
        String interval = null;
        long minutes = ChronoUnit.MINUTES.between(start, end);

        if (minutes <= 60) {
            function = "TIME_SERIES_INTRADAY";
            interval = "1min";
        } else if (minutes <= 1440) {
            function = "TIME_SERIES_INTRADAY";
            interval = "5min";
        } else if (minutes <= 10080) {
            function = "TIME_SERIES_INTRADAY";
            interval = "60min";
        } else {
            function = "TIME_SERIES_DAILY";
        }

        return alphaVantageService.getHistorical(symbol, function, interval, start, end);
    }
}
