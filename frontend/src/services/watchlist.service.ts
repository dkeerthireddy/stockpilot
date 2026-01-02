import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { WatchlistItem } from '../models/stock.model';

@Injectable({
  providedIn: 'root',
})
export class WatchlistService {
  private readonly STORAGE_KEY = 'stockpilot_watchlist';
  private watchlistSubject = new BehaviorSubject<WatchlistItem[]>([]);
  public watchlist$: Observable<WatchlistItem[]> = this.watchlistSubject.asObservable();

  constructor() {
    this.loadWatchlist();
  }

  private loadWatchlist(): void {
    const stored = localStorage.getItem(this.STORAGE_KEY);
    if (stored) {
      try {
        const watchlist = JSON.parse(stored);
        this.watchlistSubject.next(watchlist);
      } catch (e) {
        console.error('Error loading watchlist:', e);
      }
    }
  }

  private saveWatchlist(watchlist: WatchlistItem[]): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(watchlist));
    this.watchlistSubject.next(watchlist);
  }

  addToWatchlist(item: WatchlistItem): void {
    const currentWatchlist = this.watchlistSubject.value;
    
    if (currentWatchlist.some(w => w.symbol === item.symbol)) {
      return;
    }

    const newWatchlist = [...currentWatchlist, { ...item, addedAt: new Date().toISOString() }];
    this.saveWatchlist(newWatchlist);
  }

  removeFromWatchlist(symbol: string): void {
    const currentWatchlist = this.watchlistSubject.value;
    const newWatchlist = currentWatchlist.filter(item => item.symbol !== symbol);
    this.saveWatchlist(newWatchlist);
  }

  isInWatchlist(symbol: string): boolean {
    return this.watchlistSubject.value.some(item => item.symbol === symbol);
  }

  getWatchlist(): WatchlistItem[] {
    return this.watchlistSubject.value;
  }

  clearWatchlist(): void {
    this.saveWatchlist([]);
  }
}