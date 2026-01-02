import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { StockService } from '../../../services/stock.service';
import { StockSearchResult } from '../../../models/stock.model';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css'],
})
export class SearchComponent {
  searchQuery = '';
  searchResults: StockSearchResult[] = [];
  filteredResults: StockSearchResult[] = [];
  isSearching = false;
  
  selectedExchange = '';
  selectedSector = '';
  
  exchanges: string[] = [];
  sectors: string[] = [];

  constructor(
    private stockService: StockService,
    private router: Router
  ) {}

  onSearch(): void {
    if (this.searchQuery.trim().length < 1) {
      this.searchResults = [];
      this.filteredResults = [];
      return;
    }

    this.isSearching = true;
    this.stockService.searchStocks(this.searchQuery).subscribe({
      next: (results) => {
        this.searchResults = results;
        this.updateFilters();
        this.applyFilters();
        this.isSearching = false;
      },
      error: (error) => {
        console.error('Search error:', error);
        this.isSearching = false;
      },
    });
  }

  updateFilters(): void {
    this.exchanges = [...new Set(this.searchResults.map(r => r.exchange).filter(e => e))] as string[];
    this.sectors = [...new Set(this.searchResults.map(r => r.sector).filter(s => s))] as string[];
  }

  applyFilters(): void {
    this.filteredResults = this.searchResults.filter(result => {
      const matchesExchange = !this.selectedExchange || result.exchange === this.selectedExchange;
      const matchesSector = !this.selectedSector || result.sector === this.selectedSector;
      return matchesExchange && matchesSector;
    });
  }

  viewStock(symbol: string): void {
    this.router.navigate(['/stock', symbol]);
  }

  clearFilters(): void {
    this.selectedExchange = '';
    this.selectedSector = '';
    this.applyFilters();
  }
}