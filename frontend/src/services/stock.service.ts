import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  StockQuote,
  StockSearchResult,
  HistoricalPrice,
  StockFundamentals,
  NewsArticle,
  StockAnalysis,
} from '../models/stock.model';

@Injectable({
  providedIn: 'root',
})
export class StockService {
  private apiUrl = 'http://localhost:8080/api/stocks';

  constructor(private http: HttpClient) {}

  searchStocks(query: string): Observable<StockSearchResult[]> {
    const params = new HttpParams().set('query', query);
    return this.http.get<StockSearchResult[]>(`${this.apiUrl}/search`, { params });
  }

  getQuote(symbol: string): Observable<StockQuote> {
    return this.http.get<StockQuote>(`${this.apiUrl}/quote/${symbol}`);
  }

  getHistoricalData(symbol: string, range: string = '1M'): Observable<HistoricalPrice[]> {
    const params = new HttpParams().set('range', range);
    return this.http.get<HistoricalPrice[]>(`${this.apiUrl}/historical/${symbol}`, { params });
  }

  getFundamentals(symbol: string): Observable<StockFundamentals> {
    return this.http.get<StockFundamentals>(`${this.apiUrl}/fundamentals/${symbol}`);
  }

  getNews(symbol: string, limit: number = 10): Observable<NewsArticle[]> {
    const params = new HttpParams().set('limit', limit.toString());
    return this.http.get<NewsArticle[]>(`${this.apiUrl}/news/${symbol}`, { params });
  }

  getAnalysis(symbol: string, range: string = '1Y'): Observable<StockAnalysis> {
    const params = new HttpParams().set('range', range);
    return this.http.get<StockAnalysis>(`${this.apiUrl}/analysis/${symbol}`, { params });
  }

  healthCheck(): Observable<any> {
    return this.http.get(`${this.apiUrl}/health`);
  }
}
