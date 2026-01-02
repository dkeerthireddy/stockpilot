package com.stockpilot.service;

import com.stockpilot.domain.HistoricalPrice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class FinanceUtilsService {

    public BigDecimal computeReturns(BigDecimal startPrice, BigDecimal endPrice) {
        if (startPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return endPrice.subtract(startPrice)
                .divide(startPrice, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100"));
    }

    public BigDecimal computeVolatility(List<HistoricalPrice> prices) {
        if (prices == null || prices.size() < 2) {
            return BigDecimal.ZERO;
        }

        // Calculate daily returns
        double[] returns = new double[prices.size() - 1];
        for (int i = 1; i < prices.size(); i++) {
            BigDecimal prev = prices.get(i - 1).getClose();
            BigDecimal current = prices.get(i).getClose();
            returns[i - 1] = current.subtract(prev)
                    .divide(prev, 6, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        // Calculate standard deviation
        double mean = 0;
        for (double ret : returns) {
            mean += ret;
        }
        mean /= returns.length;

        double variance = 0;
        for (double ret : returns) {
            variance += Math.pow(ret - mean, 2);
        }
        variance /= returns.length;

        double stdDev = Math.sqrt(variance);
        
        // Annualize (assuming 252 trading days)
        double annualizedVolatility = stdDev * Math.sqrt(252) * 100;
        
        return BigDecimal.valueOf(annualizedVolatility).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal computeMaxDrawdown(List<HistoricalPrice> prices) {
        if (prices == null || prices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal maxPrice = prices.get(0).getClose();
        BigDecimal maxDrawdown = BigDecimal.ZERO;

        for (HistoricalPrice price : prices) {
            BigDecimal currentPrice = price.getClose();
            
            if (currentPrice.compareTo(maxPrice) > 0) {
                maxPrice = currentPrice;
            }

            BigDecimal drawdown = maxPrice.subtract(currentPrice)
                    .divide(maxPrice, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));

            if (drawdown.compareTo(maxDrawdown) > 0) {
                maxDrawdown = drawdown;
            }
        }

        return maxDrawdown;
    }

    public String classifyRisk(BigDecimal volatility) {
        if (volatility == null) {
            return "UNKNOWN";
        }
        
        double vol = volatility.doubleValue();
        
        if (vol < 15) {
            return "LOW";
        } else if (vol < 30) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    public String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return String.format("$%,.2f", amount);
    }

    public String formatPercent(BigDecimal percent) {
        if (percent == null) {
            return "0.00%";
        }
        return String.format("%+,.2f%%", percent);
    }

    public String formatLargeNumber(BigDecimal number) {
        if (number == null) {
            return "N/A";
        }

        double value = number.doubleValue();

        if (value >= 1_000_000_000_000.0) {
            return String.format("$%.2fT", value / 1_000_000_000_000.0);
        } else if (value >= 1_000_000_000.0) {
            return String.format("$%.2fB", value / 1_000_000_000.0);
        } else if (value >= 1_000_000.0) {
            return String.format("$%.2fM", value / 1_000_000.0);
        } else if (value >= 1_000.0) {
            return String.format("$%.2fK", value / 1_000.0);
        } else {
            return formatCurrency(number);
        }
    }
}
