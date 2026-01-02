import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { SearchComponent } from './pages/search/search.component';
import { StockDetailComponent } from './pages/stock-detail/stock-detail.component';
import { WatchlistComponent } from './pages/watchlist/watchlist.component';
import { LearnComponent } from './pages/learn/learn.component';
import { DisclaimerComponent } from './pages/disclaimer/disclaimer.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'search', component: SearchComponent },
  { path: 'stock/:symbol', component: StockDetailComponent },
  { path: 'watchlist', component: WatchlistComponent },
  { path: 'learn', component: LearnComponent },
  { path: 'disclaimer', component: DisclaimerComponent },
  { path: '**', redirectTo: '' },
];