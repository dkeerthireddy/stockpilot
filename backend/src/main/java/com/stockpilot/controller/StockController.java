package com.stockpilot.controller;

import com.stockpilot.domain.*;
import com.stockpilot.service.FinanceUtilsService;
import com.stockpilot.service.MarketDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@CrossOrigin(origins = {"http://localhost:4200", "https://*.vercel.app"})
public class StockController {

    private final MarketDataService marketDataService;
    private final FinanceUtilsService financeUtils;

    public StockController(MarketDataService marketDataService, FinanceUtilsService financeUtils) {
        this.marketDataService = marketDataService;
        this.financeUtils = financeUtils;
    }

    @GetMapping("/search")
    public ResponseEntity<List<StockSearchResult>> searchStocks(
            @RequestParam String query) {
        
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<StockSearchResult> results = marketDataService.searchSymbols(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/quote/{symbol}")
    public ResponseEntity<StockQuote> getQuote(@PathVariable String symbol) {
        StockQuote quote = marketDataService.getQuote(symbol);
        
        if (quote == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(quote);
    }

    @GetMapping("/historical/{symbol}")
    public ResponseEntity<List<HistoricalPrice>> getHistoricalData(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1M") String range) {
        
        List<HistoricalPrice> data = marketDataService.getHistoricalData(symbol, range);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/fundamentals/{symbol}")
    public ResponseEntity<StockFundamentals> getFundamentals(@PathVariable String symbol) {
        StockFundamentals fundamentals = marketDataService.getFundamentals(symbol);
        
        if (fundamentals == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(fundamentals);
    }

    @GetMapping("/news/{symbol}")
    public ResponseEntity<List<NewsArticle>> getNews(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "10") int limit) {
        
        List<NewsArticle> news = marketDataService.getNews(symbol, Math.min(limit, 50));
        return ResponseEntity.ok(news);
    }

    @GetMapping("/analysis/{symbol}")
    public ResponseEntity<Map<String, Object>> getAnalysis(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1Y") String range) {
        
        List<HistoricalPrice> prices = marketDataService.getHistoricalData(symbol, range);
        
        if (prices == null || prices.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> analysis = new HashMap<>();
        
        BigDecimal volatility = financeUtils.computeVolatility(prices);
        BigDecimal maxDrawdown = financeUtils.computeMaxDrawdown(prices);
        String riskLevel = financeUtils.classifyRisk(volatility);

        analysis.put("volatility", volatility);
        analysis.put("maxDrawdown", maxDrawdown);
        analysis.put("riskLevel", riskLevel);
        analysis.put("dataPoints", prices.size());
        
        if (!prices.isEmpty()) {
            BigDecimal startPrice = prices.get(0).getClose();
            BigDecimal endPrice = prices.get(prices.size() - 1).getClose();
            BigDecimal returns = financeUtils.computeReturns(startPrice, endPrice);
            analysis.put("returns", returns);
        }

        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "StockPilot API");
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
