package com.stockpilot.service;

import com.stockpilot.domain.*;
import java.util.List;

public interface MarketDataProvider {
    
    /**
     * Search for stocks by symbol or name
     */
    List<StockSearchResult> searchSymbols(String query);
    
    /**
     * Get real-time quote for a symbol
     */
    StockQuote getQuote(String symbol);
    
    /**
     * Get historical price data
     * @param range: 1D, 5D, 1M, 6M, 1Y, 5Y
     */
    List<HistoricalPrice> getHistoricalData(String symbol, String range);
    
    /**
     * Get fundamental data for a symbol
     */
    StockFundamentals getFundamentals(String symbol);
    
    /**
     * Get news articles for a symbol
     */
    List<NewsArticle> getNews(String symbol, int limit);
    
    /**
     * Check if this provider is available
     */
    boolean isAvailable();
    
    /**
     * Get provider name
     */
    String getProviderName();
}
