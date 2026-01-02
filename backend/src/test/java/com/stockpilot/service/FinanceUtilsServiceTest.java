package com.stockpilot.service;

import com.stockpilot.domain.HistoricalPrice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinanceUtilsServiceTest {

    private FinanceUtilsService financeUtils;

    @BeforeEach
    void setUp() {
        financeUtils = new FinanceUtilsService();
    }

    @Test
    void testComputeReturns() {
        BigDecimal startPrice = new BigDecimal("100");
        BigDecimal endPrice = new BigDecimal("120");
        
        BigDecimal returns = financeUtils.computeReturns(startPrice, endPrice);
        
        assertEquals(new BigDecimal("20.0000"), returns);
    }

    @Test
    void testComputeReturnsZeroStart() {
        BigDecimal startPrice = BigDecimal.ZERO;
        BigDecimal endPrice = new BigDecimal("100");
        
        BigDecimal returns = financeUtils.computeReturns(startPrice, endPrice);
        
        assertEquals(BigDecimal.ZERO, returns);
    }

    @Test
    void testComputeVolatility() {
        List<HistoricalPrice> prices = new ArrayList<>();
        prices.add(createPrice(LocalDate.now().minusDays(4), new BigDecimal("100")));
        prices.add(createPrice(LocalDate.now().minusDays(3), new BigDecimal("102")));
        prices.add(createPrice(LocalDate.now().minusDays(2), new BigDecimal("98")));
        prices.add(createPrice(LocalDate.now().minusDays(1), new BigDecimal("101")));
        prices.add(createPrice(LocalDate.now(), new BigDecimal("103")));
        
        BigDecimal volatility = financeUtils.computeVolatility(prices);
        
        assertNotNull(volatility);
        assertTrue(volatility.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void testComputeMaxDrawdown() {
        List<HistoricalPrice> prices = new ArrayList<>();
        prices.add(createPrice(LocalDate.now().minusDays(4), new BigDecimal("100")));
        prices.add(createPrice(LocalDate.now().minusDays(3), new BigDecimal("120")));
        prices.add(createPrice(LocalDate.now().minusDays(2), new BigDecimal("90")));
        prices.add(createPrice(LocalDate.now().minusDays(1), new BigDecimal("110")));
        
        BigDecimal maxDrawdown = financeUtils.computeMaxDrawdown(prices);
        
        assertNotNull(maxDrawdown);
        assertTrue(maxDrawdown.compareTo(BigDecimal.ZERO) >= 0);
    }

    @Test
    void testClassifyRisk() {
        assertEquals("LOW", financeUtils.classifyRisk(new BigDecimal("10")));
        assertEquals("MEDIUM", financeUtils.classifyRisk(new BigDecimal("20")));
        assertEquals("HIGH", financeUtils.classifyRisk(new BigDecimal("35")));
        assertEquals("UNKNOWN", financeUtils.classifyRisk(null));
    }

    @Test
    void testFormatCurrency() {
        String formatted = financeUtils.formatCurrency(new BigDecimal("1234.56"));
        assertTrue(formatted.contains("1,234.56"));
    }

    @Test
    void testFormatPercent() {
        String formatted = financeUtils.formatPercent(new BigDecimal("12.345"));
        assertTrue(formatted.contains("12.34"));
    }

    @Test
    void testFormatLargeNumber() {
        assertEquals("$1.00T", financeUtils.formatLargeNumber(new BigDecimal("1000000000000")));
        assertEquals("$5.50B", financeUtils.formatLargeNumber(new BigDecimal("5500000000")));
        assertEquals("$250.00M", financeUtils.formatLargeNumber(new BigDecimal("250000000")));
    }

    private HistoricalPrice createPrice(LocalDate date, BigDecimal close) {
        HistoricalPrice price = new HistoricalPrice();
        price.setDate(date);
        price.setClose(close);
        price.setOpen(close);
        price.setHigh(close);
        price.setLow(close);
        price.setVolume(1000000L);
        return price;
    }
}