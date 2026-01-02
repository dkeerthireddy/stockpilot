# StockPilot - Quick Start Guide

## 🚀 Get Running in 5 Minutes

### Step 1: Install Prerequisites

**Check if you have the required tools:**
```bash
node --version    # Need 18+
java --version    # Need 21+
mvn --version     # Need 3.6+
```

**If not installed:**
- Node.js: https://nodejs.org/
- Java JDK 21: https://www.oracle.com/java/technologies/downloads/
- Maven: https://maven.apache.org/download.cgi (or use your IDE)

### Step 2: Start Backend

```bash
cd stockpilot/backend
mvn spring-boot:run
```

Wait for: `Started StockPilotApplication in X seconds`

Backend is now running on: **http://localhost:8080**

### Step 3: Start Frontend

Open a NEW terminal window:

```bash
cd stockpilot/frontend
npm install
npm start
```

Wait for: `Compiled successfully`

Frontend is now running on: **http://localhost:4200**

### Step 4: Open Browser

Navigate to: **http://localhost:4200**

You should see the StockPilot homepage! 🎉

### Step 5: Try It Out

1. **Search for a stock** - Type "Apple" or "AAPL" in the search bar
2. **View details** - Click on any search result
3. **Add to watchlist** - Click the star icon
4. **Explore charts** - Try different time ranges (1D, 1M, 1Y, etc.)
5. **Read news** - Scroll down to see latest headlines
6. **Learn** - Visit the Learning Center from the navigation

## 🔧 Troubleshooting

### Backend won't start

**Problem:** Port 8080 already in use
**Solution:** 
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Mac/Linux
lsof -ti:8080 | xargs kill -9
```

**Problem:** Maven dependencies fail
**Solution:**
```bash
mvn clean install -U
```

### Frontend won't start

**Problem:** Node modules error
**Solution:**
```bash
rm -rf node_modules package-lock.json
npm install
```

**Problem:** Port 4200 already in use
**Solution:**
```bash
npm start -- --port 4201
```

### Can't fetch stock data

**Problem:** CORS errors in browser console
**Solution:** Ensure backend is running on port 8080

**Problem:** "No data available"
**Solution:** Check your internet connection and try a different stock symbol

## 📚 Next Steps

- Read the full README.md
- Explore the Learning Center in the app
- Check out the documentation in `/docs`
- Deploy to Vercel (see docs/deployment.md)

## 💡 Tips

- Use **Ctrl+C** in terminals to stop servers
- Backend changes require restart
- Frontend hot-reloads automatically
- Clear browser cache if issues persist

## 🆘 Still Having Issues?

1. Check backend logs in the terminal
2. Check browser console for errors (F12)
3. Verify all prerequisites are installed
4. Try restarting both servers
5. Open a GitHub issue with details

---

**Happy Learning! 📈**