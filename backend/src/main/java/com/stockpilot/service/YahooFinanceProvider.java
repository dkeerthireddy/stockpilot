package com.stockpilot.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockpilot.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceProvider implements MarketDataProvider {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public List<StockSearchResult> searchSymbols(String query) {
        List<StockSearchResult> results = new ArrayList<>();
        
        try {
            String url = String.format(
                "https://query2.finance.yahoo.com/v1/finance/search?q=%s&quotesCount=10&newsCount=0",
                query
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode quotes = root.path("quotes");
            
            for (JsonNode quote : quotes) {
                StockSearchResult result = new StockSearchResult();
                result.setSymbol(quote.path("symbol").asText());
                result.setName(quote.path("shortname").asText());
                result.setExchange(quote.path("exchange").asText());
                result.setType(quote.path("quoteType").asText());
                results.add(result);
            }
        } catch (Exception e) {
            System.err.println("Yahoo Finance search error: " + e.getMessage());
        }
        
        return results;
    }

    @Override
    public StockQuote getQuote(String symbol) {
        try {
            String url = String.format(
                "https://query2.finance.yahoo.com/v8/finance/chart/%s?interval=1d&range=1d",
                symbol
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode result = root.path("chart").path("result").get(0);
            JsonNode meta = result.path("meta");
            JsonNode quote = result.path("indicators").path("quote").get(0);
            
            StockQuote stockQuote = new StockQuote();
            stockQuote.setSymbol(symbol);
            stockQuote.setName(meta.path("longName").asText(symbol));
            stockQuote.setExchange(meta.path("exchangeName").asText());
            
            BigDecimal currentPrice = new BigDecimal(meta.path("regularMarketPrice").asText());
            BigDecimal previousClose = new BigDecimal(meta.path("previousClose").asText());
            
            stockQuote.setPrice(currentPrice);
            stockQuote.setPreviousClose(previousClose);
            stockQuote.setChange(currentPrice.subtract(previousClose));
            stockQuote.setChangePercent(
                currentPrice.subtract(previousClose)
                    .divide(previousClose, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"))
            );
            
            stockQuote.setOpen(new BigDecimal(meta.path("regularMarketOpen").asText("0")));
            stockQuote.setHigh(new BigDecimal(meta.path("regularMarketDayHigh").asText("0")));
            stockQuote.setLow(new BigDecimal(meta.path("regularMarketDayLow").asText("0")));
            stockQuote.setVolume(meta.path("regularMarketVolume").asLong(0));
            stockQuote.setTimestamp(LocalDateTime.now());
            
            return stockQuote;
        } catch (Exception e) {
            System.err.println("Yahoo Finance quote error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<HistoricalPrice> getHistoricalData(String symbol, String range) {
        List<HistoricalPrice> prices = new ArrayList<>();
        
        try {
            String url = String.format(
                "https://query2.finance.yahoo.com/v8/finance/chart/%s?interval=1d&range=%s",
                symbol, range.toLowerCase()
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode result = root.path("chart").path("result").get(0);
            JsonNode timestamps = result.path("timestamp");
            JsonNode quote = result.path("indicators").path("quote").get(0);
            
            JsonNode opens = quote.path("open");
            JsonNode highs = quote.path("high");
            JsonNode lows = quote.path("low");
            JsonNode closes = quote.path("close");
            JsonNode volumes = quote.path("volume");
            
            for (int i = 0; i < timestamps.size(); i++) {
                long timestamp = timestamps.get(i).asLong();
                LocalDate date = Instant.ofEpochSecond(timestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
                
                HistoricalPrice price = new HistoricalPrice();
                price.setDate(date);
                price.setOpen(getBigDecimal(opens, i));
                price.setHigh(getBigDecimal(highs, i));
                price.setLow(getBigDecimal(lows, i));
                price.setClose(getBigDecimal(closes, i));
                price.setVolume(volumes.get(i).asLong(0));
                
                prices.add(price);
            }
        } catch (Exception e) {
            System.err.println("Yahoo Finance historical error: " + e.getMessage());
        }
        
        return prices;
    }

    @Override
    public StockFundamentals getFundamentals(String symbol) {
        try {
            String url = String.format(
                "https://query2.finance.yahoo.com/v10/finance/quoteSummary/%s?modules=summaryDetail,price,defaultKeyStatistics,assetProfile",
                symbol
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode result = root.path("quoteSummary").path("result").get(0);
            
            StockFundamentals fundamentals = new StockFundamentals();
            fundamentals.setSymbol(symbol);
            
            JsonNode price = result.path("price");
            fundamentals.setName(price.path("longName").asText(symbol));
            
            JsonNode summaryDetail = result.path("summaryDetail");
            fundamentals.setMarketCap(getBigDecimalFromRaw(summaryDetail.path("marketCap")));
            fundamentals.setPeRatio(getBigDecimalFromRaw(summaryDetail.path("trailingPE")));
            fundamentals.setDividendYield(getBigDecimalFromRaw(summaryDetail.path("dividendYield")));
            fundamentals.setBeta(getBigDecimalFromRaw(summaryDetail.path("beta")));
            fundamentals.setFiftyTwoWeekHigh(getBigDecimalFromRaw(summaryDetail.path("fiftyTwoWeekHigh")));
            fundamentals.setFiftyTwoWeekLow(getBigDecimalFromRaw(summaryDetail.path("fiftyTwoWeekLow")));
            
            JsonNode keyStats = result.path("defaultKeyStatistics");
            fundamentals.setEps(getBigDecimalFromRaw(keyStats.path("trailingEps")));
            
            JsonNode assetProfile = result.path("assetProfile");
            fundamentals.setSector(assetProfile.path("sector").asText());
            fundamentals.setIndustry(assetProfile.path("industry").asText());
            fundamentals.setDescription(assetProfile.path("longBusinessSummary").asText());
            
            return fundamentals;
        } catch (Exception e) {
            System.err.println("Yahoo Finance fundamentals error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<NewsArticle> getNews(String symbol, int limit) {
        List<NewsArticle> articles = new ArrayList<>();
        
        try {
            String url = String.format(
                "https://query2.finance.yahoo.com/v1/finance/search?q=%s&quotesCount=0&newsCount=%d",
                symbol, limit
            );
            
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode news = root.path("news");
            
            for (JsonNode item : news) {
                NewsArticle article = new NewsArticle();
                article.setTitle(item.path("title").asText());
                article.setSummary(item.path("summary").asText());
                article.setUrl(item.path("link").asText());
                article.setSource(item.path("publisher").asText());
                
                long timestamp = item.path("providerPublishTime").asLong();
                article.setPublishedAt(
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault())
                );
                
                JsonNode thumbnail = item.path("thumbnail");
                if (!thumbnail.isMissingNode()) {
                    article.setImageUrl(thumbnail.path("resolutions").get(0).path("url").asText());
                }
                
                articles.add(article);
            }
        } catch (Exception e) {
            System.err.println("Yahoo Finance news error: " + e.getMessage());
        }
        
        return articles;
    }

    @Override
    public boolean isAvailable() {
        try {
            String url = "https://query2.finance.yahoo.com/v1/finance/search?q=AAPL&quotesCount=1";
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getProviderName() {
        return "Yahoo Finance";
    }

    private BigDecimal getBigDecimal(JsonNode array, int index) {
        JsonNode node = array.get(index);
        if (node == null || node.isNull()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(node.asText("0"));
    }

    private BigDecimal getBigDecimalFromRaw(JsonNode node) {
        if (node.isMissingNode() || node.isNull()) {
            return null;
        }
        JsonNode raw = node.path("raw");
        if (raw.isMissingNode()) {
            return null;
        }
        return new BigDecimal(raw.asText("0"));
    }
}
