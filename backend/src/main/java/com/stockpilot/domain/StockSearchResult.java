package com.stockpilot.domain;

public class StockSearchResult {
    private String symbol;
    private String name;
    private String exchange;
    private String type;
    private String sector;

    public StockSearchResult() {}

    public StockSearchResult(String symbol, String name, String exchange) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
    }

    // Getters and Setters
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getExchange() { return exchange; }
    public void setExchange(String exchange) { this.exchange = exchange; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
}
