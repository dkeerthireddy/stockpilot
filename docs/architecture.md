# StockPilot Architecture

## Overview

StockPilot is a full-stack web application built with Angular (frontend) and Spring Boot (backend) designed to help users learn about stock investing.

## Technology Stack

### Frontend
- **Angular 17**: Modern web framework with standalone components
- **TypeScript**: Type-safe JavaScript
- **Tailwind CSS**: Utility-first CSS framework
- **RxJS**: Reactive programming for async operations

### Backend
- **Java 21**: Latest LTS version
- **Spring Boot 3.2**: REST API framework
- **Maven**: Build and dependency management
- **Jackson**: JSON serialization

### Deployment
- **Vercel**: Serverless hosting platform
- Frontend deployed as static site
- Backend runs locally or on separate server

## Architecture Layers

### Frontend Architecture

```
frontend/
├── src/
│   ├── app/
│   │   ├── pages/              # Route components
│   │   │   ├── home/
│   │   │   ├── search/
│   │   │   ├── stock-detail/
│   │   │   ├── watchlist/
│   │   │   ├── learn/
│   │   │   └── disclaimer/
│   │   ├── app.component.*     # Root component
│   │   └── app.routes.ts       # Routing configuration
│   ├── services/               # Business logic & API calls
│   │   ├── stock.service.ts
│   │   ├── watchlist.service.ts
│   │   └── theme.service.ts
│   ├── models/                 # TypeScript interfaces
│   │   ├── stock.model.ts
│   │   └── constants.ts
│   ├── utils/                  # Helper functions
│   │   └── format.utils.ts
│   └── styles/                 # Global styles
```

### Backend Architecture

```
backend/
└── src/main/java/com/stockpilot/
    ├── StockPilotApplication.java    # Main entry point
    ├── controller/                   # REST endpoints
    │   └── StockController.java
    ├── service/                      # Business logic
    │   ├── MarketDataService.java    # Orchestration layer
    │   ├── MarketDataProvider.java   # Interface
    │   ├── YahooFinanceProvider.java # Implementation
    │   └── FinanceUtilsService.java  # Calculations
    └── domain/                       # Data models
        ├── StockQuote.java
        ├── StockFundamentals.java
        ├── HistoricalPrice.java
        ├── NewsArticle.java
        └── StockSearchResult.java
```

## Design Patterns

### Backend

1. **Service Layer Pattern**: Separation of concerns between controllers and business logic
2. **Provider Pattern**: Abstract data sources behind MarketDataProvider interface
3. **Caching**: In-memory cache with TTL for API responses
4. **Fallback Strategy**: Multiple providers with automatic failover

### Frontend

1. **Component-Based**: Modular, reusable UI components
2. **Reactive Programming**: RxJS Observables for async data
3. **Service Injection**: Dependency injection for shared logic
4. **Local Storage**: Browser storage for watchlist persistence

## Data Flow

### Stock Quote Request Flow

1. User navigates to `/stock/AAPL`
2. `StockDetailComponent` calls `StockService.getQuote('AAPL')`
3. Angular HTTP client sends GET request to `/api/stocks/quote/AAPL`
4. `StockController` receives request
5. `MarketDataService` checks cache
6. If cache miss, queries `YahooFinanceProvider`
7. Provider fetches data from Yahoo Finance API
8. Response cached and returned through layers
9. Component receives data and renders UI

## API Endpoints

### Stock Operations

- `GET /api/stocks/search?query={query}` - Search stocks
- `GET /api/stocks/quote/{symbol}` - Get real-time quote
- `GET /api/stocks/historical/{symbol}?range={range}` - Get price history
- `GET /api/stocks/fundamentals/{symbol}` - Get company fundamentals
- `GET /api/stocks/news/{symbol}?limit={limit}` - Get news articles
- `GET /api/stocks/analysis/{symbol}?range={range}` - Get risk analysis
- `GET /api/stocks/health` - Health check

## Security Considerations

1. **CORS**: Configured for localhost and Vercel domains
2. **No Authentication**: Public read-only data only
3. **Rate Limiting**: Rely on upstream provider limits
4. **Input Validation**: All user inputs validated
5. **Error Handling**: Graceful degradation on failures

## Performance Optimizations

1. **Caching**: 5-minute TTL on all market data
2. **Lazy Loading**: Angular routes lazy-loaded where beneficial
3. **Code Splitting**: Separate bundles for better load times
4. **Responsive Design**: Mobile-first approach

## Scalability

### Current Limitations
- In-memory cache (not shared across instances)
- Stateless backend (horizontally scalable)
- Client-side watchlist (no backend persistence)

### Future Improvements
- Redis for distributed caching
- Database for user data persistence
- WebSocket for real-time updates
- CDN for static assets

## Testing Strategy

### Backend
- Unit tests for service layer (JUnit)
- Integration tests for controllers
- Mock external API calls

### Frontend
- Component tests (Angular testing utilities)
- Service tests (mocked HTTP)
- E2E tests (optional)

## Monitoring & Observability

- Server logs via Spring Boot logging
- Frontend errors logged to console
- Health check endpoint for uptime monitoring