# Market Data Providers

## Overview

StockPilot uses a flexible provider architecture to fetch market data from free, public APIs that require **no API keys or authentication**.

## Provider Architecture

### Interface: `MarketDataProvider`

All data providers implement this interface:

```java
public interface MarketDataProvider {
    List<StockSearchResult> searchSymbols(String query);
    StockQuote getQuote(String symbol);
    List<HistoricalPrice> getHistoricalData(String symbol, String range);
    StockFundamentals getFundamentals(String symbol);
    List<NewsArticle> getNews(String symbol, int limit);
    boolean isAvailable();
    String getProviderName();
}
```

### Orchestration: `MarketDataService`

The `MarketDataService` manages multiple providers with:

1. **Automatic Fallback**: If one provider fails, tries the next
2. **Caching**: 5-minute TTL cache to reduce API calls
3. **Error Handling**: Graceful degradation without crashing

## Current Providers

### 1. Yahoo Finance Provider (Primary)

**Implementation**: `YahooFinanceProvider.java`

**Base URL**: `https://query2.finance.yahoo.com`

**Endpoints Used**:

- **Search**: `/v1/finance/search?q={query}`
  - Returns: Symbol, name, exchange, type
  - No authentication required

- **Quote**: `/v8/finance/chart/{symbol}?interval=1d&range=1d`
  - Returns: Current price, volume, OHLC data
  - Real-time quotes (15-20 min delay)

- **Historical Data**: `/v8/finance/chart/{symbol}?interval=1d&range={range}`
  - Supports ranges: 1d, 5d, 1mo, 6mo, 1y, 5y
  - Returns: Date, OHLC, volume

- **Fundamentals**: `/v10/finance/quoteSummary/{symbol}?modules=summaryDetail,price,defaultKeyStatistics,assetProfile`
  - Returns: Market cap, P/E, EPS, beta, sector, description
  
- **News**: `/v1/finance/search?q={symbol}&newsCount={limit}`
  - Returns: Headlines, summaries, URLs, timestamps

**Why Yahoo Finance?**

- ✅ Completely free, no signup required
- ✅ No API key needed
- ✅ Comprehensive data coverage
- ✅ Global stock markets
- ✅ Reliable uptime
- ⚠️ Rate limits exist but are generous
- ⚠️ Data delayed 15-20 minutes (acceptable for education)

**Rate Limits**: 
- Unofficial limits ~2000 requests/hour per IP
- Our caching reduces actual requests significantly

### Alternative Providers (Future)

#### 2. Alpha Vantage (Free Tier)
- **Requires**: API key (free tier: 5 calls/min, 500/day)
- **Not Used**: Requires user to sign up and configure

#### 3. IEX Cloud (Free Tier)
- **Requires**: API token
- **Not Used**: Requires authentication

#### 4. Finnhub (Free Tier)
- **Requires**: API key
- **Not Used**: Requires authentication

#### 5. Twelve Data (Free Tier)
- **Requires**: API key
- **Not Used**: Requires authentication

## Why No API Keys?

StockPilot is designed to work **out-of-the-box** with zero configuration. This means:

1. ✅ Clone and run immediately
2. ✅ No signup barriers for users
3. ✅ No secret management needed
4. ✅ Simplified deployment to Vercel
5. ✅ Educational users don't need accounts

## Adding New Providers

To add a new no-auth provider:

1. Create a class implementing `MarketDataProvider`
2. Add `@Component` annotation for Spring auto-detection
3. Implement all interface methods
4. Handle errors gracefully (return null/empty on failure)
5. Spring Boot automatically registers it with `MarketDataService`

Example:

```java
@Component
public class NewProvider implements MarketDataProvider {
    
    @Override
    public List<StockSearchResult> searchSymbols(String query) {
        // Implementation
    }
    
    @Override
    public boolean isAvailable() {
        try {
            // Quick health check
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public String getProviderName() {
        return "New Provider";
    }
}
```

## Caching Strategy

### Cache Implementation

```java
private final Map<String, CachedData<?>> cache = new ConcurrentHashMap<>();
private static final int CACHE_TTL_MINUTES = 5;
```

### Cache Keys

- Search: `search:{query}`
- Quote: `quote:{SYMBOL}`
- Historical: `historical:{SYMBOL}:{RANGE}`
- Fundamentals: `fundamentals:{SYMBOL}`
- News: `news:{SYMBOL}:{LIMIT}`

### Benefits

1. **Reduced API Calls**: Avoid hitting Yahoo Finance repeatedly
2. **Faster Response**: Instant return for cached data
3. **Rate Limit Protection**: Stay within provider limits
4. **Better UX**: Faster page loads

### TTL Considerations

- 5 minutes balances freshness vs. performance
- Stock prices don't change that rapidly for educational use
- News updates every 5 minutes is acceptable

## Error Handling

### Provider Failures

When a provider fails:

1. Log error to console
2. Try next provider in list
3. Return empty results if all fail
4. Frontend handles gracefully (show "No data available")

### Network Issues

- Timeout after 30 seconds
- Retry logic handled by RestTemplate
- Frontend shows loading states

## Data Accuracy Disclaimer

⚠️ **Important**: All data is:
- Delayed 15-20 minutes (not real-time)
- Subject to provider availability
- For educational purposes only
- Not suitable for actual trading decisions

Users are notified via:
- Disclaimer page
- Footer on every page
- Tooltip hints

## Testing Data Providers

### Manual Testing

```bash
# Start backend
cd backend
mvn spring-boot:run

# Test endpoints
curl http://localhost:8080/api/stocks/search?query=AAPL
curl http://localhost:8080/api/stocks/quote/AAPL
curl http://localhost:8080/api/stocks/historical/AAPL?range=1mo
```

### Unit Testing

Mock the RestTemplate to avoid hitting actual APIs during tests.

## Monitoring Provider Health

The `/api/stocks/health` endpoint can be extended to include provider status:

```json
{
  "status": "UP",
  "service": "StockPilot API",
  "providers": {
    "Yahoo Finance": "UP"
  }
}
```

## Legal Considerations

### Yahoo Finance Terms

- Public API (not officially documented)
- No official rate limits published
- Used by many open-source projects
- For non-commercial educational use

### Disclaimer

Always include disclaimer that:
1. Data is for educational purposes
2. Not financial advice
3. Data may be delayed or inaccurate
4. Verify from official sources before trading

## Future Enhancements

1. **Multiple Provider Support**: Implement fallback to other free APIs
2. **Provider Health Monitoring**: Track which providers are working
3. **Smart Provider Selection**: Choose fastest/most reliable provider
4. **CSV Data Sources**: Use downloadable CSV files as fallback
5. **Mock Data Mode**: Generate synthetic data for demos