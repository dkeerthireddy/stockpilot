# StockPilot 📈

> **Educational stock market exploration tool** - Learn about investing, track stocks, and make informed decisions.

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Angular](https://img.shields.io/badge/Angular-17-red.svg)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)

**⚠️ DISCLAIMER: This is an educational tool, not financial advice. All data is delayed and for learning purposes only.**

---

## 🌟 Features

### Core Functionality
- 🔍 **Smart Stock Search** - Find stocks by symbol or company name with autocomplete
- 📊 **Interactive Charts** - View price history across multiple time ranges (1D to 5Y)
- 📈 **Real-Time Quotes** - Get current prices, changes, and volume (15-20 min delay)
- 💼 **Fundamentals** - Explore P/E ratio, EPS, market cap, and more
- ⭐ **Personal Watchlist** - Track your favorite stocks with local storage
- 📰 **Latest News** - Stay informed with recent headlines per stock
- 📚 **Learning Center** - Glossary, beginner tips, and chart reading guide
- 🌓 **Dark Mode** - Easy on the eyes, day or night

### Technical Highlights
- ✅ **No API Keys Required** - Works out-of-the-box, zero configuration
- ✅ **Fully Responsive** - Mobile-first design, works on all devices
- ✅ **Accessible** - ARIA labels, keyboard navigation, screen reader friendly
- ✅ **Fast & Cached** - In-memory caching for optimal performance
- ✅ **Type Safe** - TypeScript + Java for robust code
- ✅ **Modern Stack** - Angular 17 + Spring Boot 3.2 + Tailwind CSS

---

## 🚀 Quick Start

### Prerequisites

- **Node.js** 18+ ([Download](https://nodejs.org/))
- **Java JDK** 21 ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven** 3.6+ (usually bundled with Java IDEs)

### Run Locally (Simple)

**1. Clone the repository**
```bash
git clone https://github.com/yourusername/stockpilot.git
cd stockpilot
```

**2. Start the backend**
```bash
cd backend
mvn spring-boot:run
```
Backend runs on `http://localhost:8080`

**3. Start the frontend** (in a new terminal)
```bash
cd frontend
npm install
npm start
```
Frontend runs on `http://localhost:4200`

**4. Open your browser**
Navigate to `http://localhost:4200` and start exploring stocks!

---

## 📁 Project Structure

```
stockpilot/
├── frontend/                  # Angular application
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/        # Route components (Home, Search, Stock Detail, etc.)
│   │   │   ├── app.component.* # Root component with navigation
│   │   │   └── app.routes.ts # Routing configuration
│   │   ├── services/         # API communication & business logic
│   │   ├── models/           # TypeScript interfaces
│   │   ├── utils/            # Helper functions (formatting, etc.)
│   │   └── styles/           # Global CSS with Tailwind
│   ├── angular.json
│   ├── package.json
│   └── tailwind.config.js
│
├── backend/                   # Spring Boot REST API
│   ├── src/main/java/com/stockpilot/
│   │   ├── StockPilotApplication.java  # Main entry point
│   │   ├── controller/       # REST endpoints
│   │   │   └── StockController.java
│   │   ├── service/          # Business logic
│   │   │   ├── MarketDataService.java     # Orchestration
│   │   │   ├── MarketDataProvider.java    # Interface
│   │   │   ├── YahooFinanceProvider.java  # Implementation
│   │   │   └── FinanceUtilsService.java   # Calculations
│   │   └── domain/           # Data models (POJOs)
│   ├── src/test/java/        # Unit tests
│   └── pom.xml
│
├── docs/                      # Documentation
│   ├── architecture.md       # System design & patterns
│   ├── data-providers.md     # API integrations explained
│   └── deployment.md         # Production deployment guide
│
├── vercel.json               # Vercel deployment config
├── .gitignore
└── README.md                 # This file
```

---

## 🏗️ Architecture

### Frontend (Angular)

**Technology Stack:**
- Angular 17 with standalone components
- TypeScript for type safety
- Tailwind CSS for styling
- RxJS for reactive programming
- Local Storage for watchlist persistence

**Key Components:**
- `HomeComponent` - Landing page with search
- `SearchComponent` - Advanced stock search with filters
- `StockDetailComponent` - Comprehensive stock view
- `WatchlistComponent` - Personal stock tracker
- `LearnComponent` - Educational content
- `DisclaimerComponent` - Legal information

**Services:**
- `StockService` - HTTP client for backend API
- `WatchlistService` - Manages user's watchlist
- `ThemeService` - Dark mode toggle

### Backend (Spring Boot)

**Technology Stack:**
- Java 21
- Spring Boot 3.2 (REST API)
- Maven for build management
- Jackson for JSON serialization

**Layers:**
1. **Controller**: REST endpoints (`StockController`)
2. **Service**: Business logic (`MarketDataService`, `FinanceUtilsService`)
3. **Provider**: External API integration (`YahooFinanceProvider`)
4. **Domain**: Data models (POJOs)

**Key Features:**
- Provider pattern with automatic fallback
- In-memory caching (5-min TTL)
- CORS configured for Vercel + localhost
- Comprehensive error handling

---

## 🔌 API Endpoints

Base URL: `http://localhost:8080/api/stocks`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/search?query={query}` | Search stocks by symbol/name |
| GET | `/quote/{symbol}` | Get real-time quote |
| GET | `/historical/{symbol}?range={range}` | Get price history |
| GET | `/fundamentals/{symbol}` | Get company fundamentals |
| GET | `/news/{symbol}?limit={limit}` | Get news articles |
| GET | `/analysis/{symbol}?range={range}` | Get risk analysis |
| GET | `/health` | Health check |

**Example:**
```bash
curl http://localhost:8080/api/stocks/quote/AAPL
```

---

## 📊 Market Data Source

### Yahoo Finance (Public API)

**Why Yahoo Finance?**
- ✅ **No API key required** - Zero configuration
- ✅ **No signup needed** - Works immediately
- ✅ **Comprehensive data** - Quotes, history, fundamentals, news
- ✅ **Global coverage** - Major exchanges worldwide
- ✅ **Reliable** - Used by countless educational projects

**Data Characteristics:**
- Delayed 15-20 minutes (not real-time)
- Sufficient for educational purposes
- Free and unlimited for learning

**Technical Implementation:**
- Abstracted behind `MarketDataProvider` interface
- Easy to add fallback providers
- Caching reduces API calls
- Graceful error handling

See [docs/data-providers.md](docs/data-providers.md) for detailed information.

---

## 🧮 Finance Utilities

The backend includes production-ready financial calculations:

### Implemented Functions

**1. Returns Calculation**
```java
computeReturns(startPrice, endPrice) 
// Returns percentage gain/loss
```

**2. Volatility (Risk Metric)**
```java
computeVolatility(historicalPrices)
// Annualized standard deviation of returns
```

**3. Max Drawdown**
```java
computeMaxDrawdown(historicalPrices)
// Largest peak-to-trough decline
```

**4. Risk Classification**
```java
classifyRisk(volatility)
// Returns: LOW, MEDIUM, or HIGH
```

**5. Formatting Utilities**
- Currency: `$1,234.56`
- Percent: `+12.34%`
- Large numbers: `$5.50B`, `$1.2T`

---

## 🎨 User Interface

### Design Principles

1. **Mobile-First**: Responsive on all screen sizes
2. **Accessibility**: ARIA labels, keyboard navigation, semantic HTML
3. **Dark Mode**: System preference detection + manual toggle
4. **Fast Loading**: Skeleton screens, optimized assets
5. **Clear Hierarchy**: Intuitive information architecture

### Color Palette

- **Primary**: Blue (`#0ea5e9`) - Trust, finance
- **Success**: Green - Positive changes
- **Danger**: Red - Negative changes
- **Neutral**: Gray scale for backgrounds

### Typography

- System fonts for performance
- Clear hierarchy (3xl → xs)
- Readable line lengths

---

## 🧪 Testing

### Backend Tests

Run unit tests:
```bash
cd backend
mvn test
```

**Coverage:**
- `FinanceUtilsServiceTest` - Financial calculations
- Additional tests in `src/test/java/`

### Frontend Tests

Run component tests:
```bash
cd frontend
npm test
```

### Manual Testing

1. Start both backend and frontend
2. Test all pages:
   - Home: Search functionality
   - Search: Filters and results
   - Stock Detail: All data loads correctly
   - Watchlist: Add/remove stocks
   - Learn: Content renders
3. Test responsive design (mobile/tablet/desktop)
4. Test dark mode toggle

---

## 🚢 Deployment

### Deploy Frontend (Vercel)

**Quick Deploy:**
```bash
npm install -g vercel
cd stockpilot
vercel
```

Or use the [Vercel Dashboard](https://vercel.com):
1. Import Git repository
2. Set root directory to `frontend`
3. Deploy!

### Deploy Backend

**Options:**
- **Heroku**: Free tier with sleep mode
- **Railway**: $5 free credit monthly
- **Render**: Free tier available
- **AWS/GCP/Azure**: More control, pay-as-you-go

**Example (Heroku):**
```bash
cd backend
heroku create stockpilot-api
git push heroku main
```

See [docs/deployment.md](docs/deployment.md) for comprehensive guide.

---

## 🔧 Configuration

### Frontend Environment

Edit `frontend/src/services/stock.service.ts`:

```typescript
// Local development
private apiUrl = 'http://localhost:8080/api/stocks';

// Production
private apiUrl = 'https://your-backend.herokuapp.com/api/stocks';
```

### Backend CORS

Edit `backend/src/main/java/com/stockpilot/StockPilotApplication.java`:

```java
.allowedOrigins("http://localhost:4200", "https://your-frontend.vercel.app")
```

### Caching

Edit `backend/src/main/java/com/stockpilot/service/MarketDataService.java`:

```java
private static final int CACHE_TTL_MINUTES = 5; // Adjust as needed
```

---

## 🎓 Educational Resources

### Built-In Learning Center

Navigate to `/learn` in the app to access:
- **Investing Glossary**: 13+ key terms explained
- **Common Mistakes**: What beginners should avoid
- **Chart Reading**: How to interpret price charts
- **Key Principles**: Long-term investing wisdom

### External Resources

- [Investopedia](https://www.investopedia.com/) - Financial education
- [Khan Academy Finance](https://www.khanacademy.org/economics-finance-domain) - Free courses
- [Bogleheads](https://www.bogleheads.org/) - Investment philosophy

---

## 🛡️ Disclaimer

**IMPORTANT - PLEASE READ:**

StockPilot is an **educational platform only**. It is NOT:
- ❌ Financial advice
- ❌ Investment recommendations
- ❌ A trading platform
- ❌ Suitable for actual investment decisions

**Before investing real money:**
1. Consult a licensed financial advisor
2. Do thorough research
3. Understand the risks
4. Consider your financial situation

**Data Accuracy:**
- Market data delayed 15-20 minutes
- Sourced from third parties (Yahoo Finance)
- May contain errors or omissions
- Always verify from official sources

All investments involve risk, including loss of principal. Past performance does not guarantee future results.

See [/disclaimer](http://localhost:4200/disclaimer) for full legal disclaimer.

---

## 🤝 Contributing

Contributions welcome! Here's how:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/amazing-feature`)
3. **Commit changes** (`git commit -m 'Add amazing feature'`)
4. **Push to branch** (`git push origin feature/amazing-feature`)
5. **Open a Pull Request**

### Development Guidelines

- Follow existing code style
- Write tests for new features
- Update documentation
- Ensure builds pass

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

**Summary**: Free to use, modify, and distribute. No warranty provided.

---

## 🙏 Acknowledgments

- **Yahoo Finance** - Market data API
- **Spring Boot** - Backend framework
- **Angular** - Frontend framework
- **Tailwind CSS** - Styling
- **Vercel** - Hosting platform

---

## 📧 Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/stockpilot/issues)
- **Documentation**: See `/docs` folder
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/stockpilot/discussions)

---

## 🗺️ Roadmap

### Planned Features

- [ ] Additional data providers (fallback sources)
- [ ] More advanced charts (candlestick, indicators)
- [ ] Portfolio tracking with hypothetical trades
- [ ] Export watchlist to CSV
- [ ] Compare multiple stocks side-by-side
- [ ] Stock screener with filters
- [ ] Email alerts for price changes
- [ ] Mobile app (React Native/Flutter)

### Ideas Welcome!

Open an issue to suggest features or improvements.

---

## 📊 Project Stats

- **Lines of Code**: ~5,000+
- **Components**: 6 pages, 3 services
- **API Endpoints**: 7
- **Test Coverage**: Unit tests for core logic
- **Supported Stocks**: Global markets (wherever Yahoo Finance has data)

---

## ⭐ Show Your Support

If this project helped you learn about stocks or web development, give it a ⭐ on GitHub!

---

<div align="center">

**Built with ❤️ for learning and education**

[View Demo](https://stockpilot.vercel.app) • [Report Bug](https://github.com/yourusername/stockpilot/issues) • [Request Feature](https://github.com/yourusername/stockpilot/issues)

</div>