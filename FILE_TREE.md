# StockPilot - Complete File Tree

```
stockpilot/
│
├── README.md                          # Main documentation
├── QUICK_START.md                     # 5-minute setup guide
├── CONTRIBUTING.md                    # Contribution guidelines
├── LICENSE                            # MIT License
├── .gitignore                         # Git exclusions
├── .env.template                      # Environment template
├── vercel.json                        # Vercel deployment config
│
├── frontend/                          # Angular Application
│   ├── package.json                   # NPM dependencies
│   ├── angular.json                   # Angular CLI config
│   ├── tsconfig.json                  # TypeScript config
│   ├── tsconfig.app.json             # App-specific TS config
│   ├── tailwind.config.js            # Tailwind CSS config
│   ├── postcss.config.js             # PostCSS config
│   │
│   └── src/
│       ├── main.ts                    # Application entry point
│       ├── index.html                 # HTML template
│       │
│       ├── app/
│       │   ├── app.component.ts       # Root component
│       │   ├── app.component.html     # Root template (nav + footer)
│       │   ├── app.component.css      # Root styles
│       │   ├── app.routes.ts          # Route configuration
│       │   │
│       │   └── pages/
│       │       ├── home/
│       │       │   ├── home.component.ts
│       │       │   ├── home.component.html
│       │       │   └── home.component.css
│       │       ├── search/
│       │       │   ├── search.component.ts
│       │       │   ├── search.component.html
│       │       │   └── search.component.css
│       │       ├── stock-detail/
│       │       │   ├── stock-detail.component.ts
│       │       │   ├── stock-detail.component.html
│       │       │   └── stock-detail.component.css
│       │       ├── watchlist/
│       │       │   ├── watchlist.component.ts
│       │       │   ├── watchlist.component.html
│       │       │   └── watchlist.component.css
│       │       ├── learn/
│       │       │   ├── learn.component.ts
│       │       │   ├── learn.component.html
│       │       │   └── learn.component.css
│       │       └── disclaimer/
│       │           ├── disclaimer.component.ts
│       │           ├── disclaimer.component.html
│       │           └── disclaimer.component.css
│       │
│       ├── services/
│       │   ├── stock.service.ts       # API client
│       │   ├── watchlist.service.ts   # Watchlist manager
│       │   └── theme.service.ts       # Dark mode
│       │
│       ├── models/
│       │   ├── stock.model.ts         # TypeScript interfaces
│       │   └── constants.ts           # App constants
│       │
│       ├── utils/
│       │   └── format.utils.ts        # Helper functions
│       │
│       └── styles/
│           └── styles.css             # Global styles + Tailwind
│
├── backend/                           # Spring Boot API
│   ├── pom.xml                        # Maven dependencies
│   │
│   └── src/
│       ├── main/
│       │   ├── java/com/stockpilot/
│       │   │   ├── StockPilotApplication.java    # Main entry
│       │   │   │
│       │   │   ├── controller/
│       │   │   │   └── StockController.java      # REST endpoints
│       │   │   │
│       │   │   ├── service/
│       │   │   │   ├── MarketDataService.java    # Orchestration
│       │   │   │   ├── MarketDataProvider.java   # Interface
│       │   │   │   ├── YahooFinanceProvider.java # Implementation
│       │   │   │   └── FinanceUtilsService.java  # Calculations
│       │   │   │
│       │   │   └── domain/
│       │   │       ├── StockQuote.java
│       │   │       ├── StockFundamentals.java
│       │   │       ├── HistoricalPrice.java
│       │   │       ├── NewsArticle.java
│       │   │       └── StockSearchResult.java
│       │   │
│       │   └── resources/
│       │       └── application.properties        # Spring config
│       │
│       └── test/
│           └── java/com/stockpilot/
│               └── service/
│                   └── FinanceUtilsServiceTest.java  # Unit tests
│
└── docs/                              # Documentation
    ├── architecture.md                # System design
    ├── data-providers.md             # API integrations
    └── deployment.md                 # Deployment guide
```

## Key Files Explained

### Configuration
- `vercel.json` - Vercel deployment settings
- `angular.json` - Angular project configuration
- `pom.xml` - Maven dependencies for backend
- `tailwind.config.js` - Styling configuration

### Entry Points
- `frontend/src/main.ts` - Angular bootstrap
- `backend/.../StockPilotApplication.java` - Spring Boot main

### Core Logic
- `StockController.java` - 7 REST endpoints
- `MarketDataService.java` - Provider orchestration + caching
- `YahooFinanceProvider.java` - Yahoo Finance API client
- `FinanceUtilsService.java` - Financial calculations

### Components (6 pages)
- Home - Landing page with search
- Search - Advanced search with filters
- Stock Detail - Comprehensive stock view
- Watchlist - Personal tracker
- Learn - Educational content
- Disclaimer - Legal information

### Services (3)
- StockService - HTTP client
- WatchlistService - Local storage manager
- ThemeService - Dark mode toggle

## Statistics

- **Total Components**: 6 pages + 1 root
- **Services**: 3 frontend + 4 backend
- **Models/Domain**: 8 classes
- **API Endpoints**: 7
- **Documentation Pages**: 3 + README
- **Test Files**: 1 (expandable)
- **Configuration Files**: 8

## Technology Stack Summary

**Frontend:**
- Angular 17 (TypeScript)
- Tailwind CSS
- RxJS

**Backend:**
- Java 21
- Spring Boot 3.2
- Maven

**Deployment:**
- Vercel (frontend)
- Heroku/Railway/Render (backend options)

**Data Source:**
- Yahoo Finance (no API key)