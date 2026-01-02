package com.stockpilot.service;

import com.stockpilot.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MarketDataService {

    private final List<MarketDataProvider> providers;
    private final Map<String, CachedData<?>> cache = new ConcurrentHashMap<>();
    private static final int CACHE_TTL_MINUTES = 5;

    public MarketDataService(List<MarketDataProvider> providers) {
        this.providers = providers;
    }

    public List<StockSearchResult> searchSymbols(String query) {
        String cacheKey = "search:" + query.toLowerCase();
        
        @SuppressWarnings("unchecked")
        List<StockSearchResult> cached = (List<StockSearchResult>) getCached(cacheKey);
        if (cached != null) {
            return cached;
        }

        for (MarketDataProvider provider : providers) {
            try {
                if (provider.isAvailable()) {
                    List<StockSearchResult> results = provider.searchSymbols(query);
                    if (results != null && !results.isEmpty()) {
                        putCache(cacheKey, results);
                        return results;
                    }
                }
            } catch (Exception e) {
                System.err.println("Provider " + provider.getProviderName() + " failed: " + e.getMessage());
            }
        }

        return Collections.emptyList();
    }

    public StockQuote getQuote(String symbol) {
        String cacheKey = "quote:" + symbol.toUpperCase();
        
        StockQuote cached = (StockQuote) getCached(cacheKey);
        if (cached != null) {
            return cached;
        }

        for (MarketDataProvider provider : providers) {
            try {
                if (provider.isAvailable()) {
                    StockQuote quote = provider.getQuote(symbol);
                    if (quote != null) {
                        putCache(cacheKey, quote);
                        return quote;
                    }
                }
            } catch (Exception e) {
                System.err.println("Provider " + provider.getProviderName() + " failed: " + e.getMessage());
            }
        }

        return null;
    }

    public List<HistoricalPrice> getHistoricalData(String symbol, String range) {
        String cacheKey = "historical:" + symbol.toUpperCase() + ":" + range;
        
        @SuppressWarnings("unchecked")
        List<HistoricalPrice> cached = (List<HistoricalPrice>) getCached(cacheKey);
        if (cached != null) {
            return cached;
        }

        for (MarketDataProvider provider : providers) {
            try {
                if (provider.isAvailable()) {
                    List<HistoricalPrice> data = provider.getHistoricalData(symbol, range);
                    if (data != null && !data.isEmpty()) {
                        putCache(cacheKey, data);
                        return data;
                    }
                }
            } catch (Exception e) {
                System.err.println("Provider " + provider.getProviderName() + " failed: " + e.getMessage());
            }
        }

        return Collections.emptyList();
    }

    public StockFundamentals getFundamentals(String symbol) {
        String cacheKey = "fundamentals:" + symbol.toUpperCase();
        
        StockFundamentals cached = (StockFundamentals) getCached(cacheKey);
        if (cached != null) {
            return cached;
        }

        for (MarketDataProvider provider : providers) {
            try {
                if (provider.isAvailable()) {
                    StockFundamentals fundamentals = provider.getFundamentals(symbol);
                    if (fundamentals != null) {
                        putCache(cacheKey, fundamentals);
                        return fundamentals;
                    }
                }
            } catch (Exception e) {
                System.err.println("Provider " + provider.getProviderName() + " failed: " + e.getMessage());
            }
        }

        return null;
    }

    public List<NewsArticle> getNews(String symbol, int limit) {
        String cacheKey = "news:" + symbol.toUpperCase() + ":" + limit;
        
        @SuppressWarnings("unchecked")
        List<NewsArticle> cached = (List<NewsArticle>) getCached(cacheKey);
        if (cached != null) {
            return cached;
        }

        for (MarketDataProvider provider : providers) {
            try {
                if (provider.isAvailable()) {
                    List<NewsArticle> news = provider.getNews(symbol, limit);
                    if (news != null && !news.isEmpty()) {
                        putCache(cacheKey, news);
                        return news;
                    }
                }
            } catch (Exception e) {
                System.err.println("Provider " + provider.getProviderName() + " failed: " + e.getMessage());
            }
        }

        return Collections.emptyList();
    }

    private Object getCached(String key) {
        CachedData<?> cached = cache.get(key);
        if (cached != null && !cached.isExpired()) {
            return cached.getData();
        }
        cache.remove(key);
        return null;
    }

    private void putCache(String key, Object data) {
        cache.put(key, new CachedData<>(data, CACHE_TTL_MINUTES));
    }

    private static class CachedData<T> {
        private final T data;
        private final LocalDateTime expiresAt;

        public CachedData(T data, int ttlMinutes) {
            this.data = data;
            this.expiresAt = LocalDateTime.now().plusMinutes(ttlMinutes);
        }

        public T getData() {
            return data;
        }

        public boolean isExpired() {
            return LocalDateTime.now().isAfter(expiresAt);
        }
    }
}
