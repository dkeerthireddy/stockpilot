import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { StockService } from '../../../services/stock.service';
import { WatchlistService } from '../../../services/watchlist.service';
import {
  StockQuote,
  StockFundamentals,
  HistoricalPrice,
  NewsArticle,
  StockAnalysis,
} from '../../../models/stock.model';
import {
  formatCurrency,
  formatPercent,
  formatLargeNumber,
  formatVolume,
  getChangeColor,
  getChangeIcon,
  formatDateTime,
} from '../../../utils/format.utils';
import { METRIC_EXPLANATIONS, RISK_COLORS, CHART_RANGES } from '../../../models/constants';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-stock-detail',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './stock-detail.component.html',
  styleUrls: ['./stock-detail.component.css'],
})
export class StockDetailComponent implements OnInit, OnDestroy {
  symbol = '';
  quote: StockQuote | null = null;
  fundamentals: StockFundamentals | null = null;
  historicalData: HistoricalPrice[] = [];
  news: NewsArticle[] = [];
  analysis: StockAnalysis | null = null;
  
  selectedRange = '1y';
  chartRanges = CHART_RANGES;
  
  isLoadingQuote = false;
  isLoadingFundamentals = false;
  isLoadingChart = false;
  isLoadingNews = false;
  
  isInWatchlist = false;
  
  metricExplanations = METRIC_EXPLANATIONS;
  riskColors = RISK_COLORS;
  
  private subscriptions: Subscription[] = [];
  
  // Utility functions
  formatCurrency = formatCurrency;
  formatPercent = formatPercent;
  formatLargeNumber = formatLargeNumber;
  formatVolume = formatVolume;
  getChangeColor = getChangeColor;
  getChangeIcon = getChangeIcon;
  formatDateTime = formatDateTime;

  constructor(
    private route: ActivatedRoute,
    private stockService: StockService,
    private watchlistService: WatchlistService
  ) {}

  ngOnInit(): void {
    this.subscriptions.push(
      this.route.params.subscribe(params => {
        this.symbol = params['symbol'];
        this.loadStockData();
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  loadStockData(): void {
    this.loadQuote();
    this.loadFundamentals();
    this.loadHistoricalData();
    this.loadNews();
    this.loadAnalysis();
    this.checkWatchlist();
  }

  loadQuote(): void {
    this.isLoadingQuote = true;
    this.subscriptions.push(
      this.stockService.getQuote(this.symbol).subscribe({
        next: (quote) => {
          this.quote = quote;
          this.isLoadingQuote = false;
        },
        error: (error) => {
          console.error('Error loading quote:', error);
          this.isLoadingQuote = false;
        },
      })
    );
  }

  loadFundamentals(): void {
    this.isLoadingFundamentals = true;
    this.subscriptions.push(
      this.stockService.getFundamentals(this.symbol).subscribe({
        next: (fundamentals) => {
          this.fundamentals = fundamentals;
          this.isLoadingFundamentals = false;
        },
        error: (error) => {
          console.error('Error loading fundamentals:', error);
          this.isLoadingFundamentals = false;
        },
      })
    );
  }

  loadHistoricalData(): void {
    this.isLoadingChart = true;
    this.subscriptions.push(
      this.stockService.getHistoricalData(this.symbol, this.selectedRange).subscribe({
        next: (data) => {
          this.historicalData = data;
          this.isLoadingChart = false;
        },
        error: (error) => {
          console.error('Error loading historical data:', error);
          this.isLoadingChart = false;
        },
      })
    );
  }

  loadNews(): void {
    this.isLoadingNews = true;
    this.subscriptions.push(
      this.stockService.getNews(this.symbol).subscribe({
        next: (news) => {
          this.news = news;
          this.isLoadingNews = false;
        },
        error: (error) => {
          console.error('Error loading news:', error);
          this.isLoadingNews = false;
        },
      })
    );
  }

  loadAnalysis(): void {
    this.subscriptions.push(
      this.stockService.getAnalysis(this.symbol).subscribe({
        next: (analysis) => {
          this.analysis = analysis;
        },
        error: (error) => {
          console.error('Error loading analysis:', error);
        },
      })
    );
  }

  changeRange(range: string): void {
    this.selectedRange = range;
    this.loadHistoricalData();
    this.loadAnalysis();
  }

  checkWatchlist(): void {
    this.isInWatchlist = this.watchlistService.isInWatchlist(this.symbol);
  }

  toggleWatchlist(): void {
    if (this.isInWatchlist) {
      this.watchlistService.removeFromWatchlist(this.symbol);
    } else if (this.quote) {
      this.watchlistService.addToWatchlist({
        symbol: this.quote.symbol,
        name: this.quote.name,
        price: this.quote.price,
        change: this.quote.change,
        changePercent: this.quote.changePercent,
        addedAt: new Date().toISOString(),
      });
    }
    this.checkWatchlist();
  }

  getExplanation(key: string): string {
    return this.metricExplanations[key] || '';
  }
}