import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { WatchlistService } from '../../../services/watchlist.service';
import { WatchlistItem } from '../../../models/stock.model';
import { formatCurrency, formatPercent, getChangeColor } from '../../../utils/format.utils';

@Component({
  selector: 'app-watchlist',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './watchlist.component.html',
  styleUrls: ['./watchlist.component.css'],
})
export class WatchlistComponent implements OnInit {
  watchlist: WatchlistItem[] = [];
  sortBy: 'symbol' | 'name' | 'change' | 'changePercent' = 'symbol';
  sortAsc = true;

  formatCurrency = formatCurrency;
  formatPercent = formatPercent;
  getChangeColor = getChangeColor;

  constructor(
    private watchlistService: WatchlistService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.watchlistService.watchlist$.subscribe(watchlist => {
      this.watchlist = watchlist;
      this.sortWatchlist();
    });
  }

  sortWatchlist(): void {
    this.watchlist.sort((a, b) => {
      let comparison = 0;
      
      switch (this.sortBy) {
        case 'symbol':
          comparison = a.symbol.localeCompare(b.symbol);
          break;
        case 'name':
          comparison = a.name.localeCompare(b.name);
          break;
        case 'change':
          comparison = a.change - b.change;
          break;
        case 'changePercent':
          comparison = a.changePercent - b.changePercent;
          break;
      }
      
      return this.sortAsc ? comparison : -comparison;
    });
  }

  changeSortBy(column: 'symbol' | 'name' | 'change' | 'changePercent'): void {
    if (this.sortBy === column) {
      this.sortAsc = !this.sortAsc;
    } else {
      this.sortBy = column;
      this.sortAsc = true;
    }
    this.sortWatchlist();
  }

  removeFromWatchlist(symbol: string): void {
    this.watchlistService.removeFromWatchlist(symbol);
  }

  viewStock(symbol: string): void {
    this.router.navigate(['/stock', symbol]);
  }

  clearWatchlist(): void {
    if (confirm('Are you sure you want to clear your entire watchlist?')) {
      this.watchlistService.clearWatchlist();
    }
  }
}