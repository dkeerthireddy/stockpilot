import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { StockService } from '../../../services/stock.service';
import { StockSearchResult } from '../../../models/stock.model';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  searchQuery = '';
  searchResults: StockSearchResult[] = [];
  isSearching = false;
  featuredStocks = ['AAPL', 'GOOGL', 'MSFT', 'TSLA', 'AMZN'];

  constructor(
    private stockService: StockService,
    private router: Router
  ) {}

  ngOnInit(): void {}

  onSearch(): void {
    if (this.searchQuery.trim().length < 1) {
      this.searchResults = [];
      return;
    }

    this.isSearching = true;
    this.stockService.searchStocks(this.searchQuery).subscribe({
      next: (results) => {
        this.searchResults = results.slice(0, 5);
        this.isSearching = false;
      },
      error: (error) => {
        console.error('Search error:', error);
        this.isSearching = false;
      },
    });
  }

  selectStock(symbol: string): void {
    this.router.navigate(['/stock', symbol]);
    this.searchQuery = '';
    this.searchResults = [];
  }

  viewStock(symbol: string): void {
    this.router.navigate(['/stock', symbol]);
  }
}