# Backend Deployment Guide

## Option 1: Heroku (Easiest)

### Step 1: Install Heroku CLI
Download from: https://devcenter.heroku.com/articles/heroku-cli

### Step 2: Login
```bash
heroku login
```

### Step 3: Deploy
```bash
cd stockpilot/backend

# Create Heroku app
heroku create stockpilot-api

# Initialize git (if not already)
git init
git add .
git commit -m "Initial commit"

# Add Heroku remote
heroku git:remote -a stockpilot-api

# Deploy
git push heroku main
```

### Step 4: Get Your API URL
```bash
heroku info
```

Your API will be at: `https://stockpilot-api.herokuapp.com/api/stocks`

---

## Option 2: Railway

1. Go to https://railway.app
2. Click "Start a New Project"
3. Select "Deploy from GitHub repo"
4. Choose your repository
5. Root directory: `backend`
6. Railway auto-detects Spring Boot
7. Click "Deploy"
8. Get your URL from dashboard

---

## Option 3: Render

1. Go to https://render.com
2. Click "New +"
3. Select "Web Service"
4. Connect your GitHub repository
5. Configure:
   - **Name**: stockpilot-backend
   - **Root Directory**: backend
   - **Environment**: Java
   - **Build Command**: `mvn clean install`
   - **Start Command**: `java -jar target/stockpilot-backend-1.0.0.jar`
6. Click "Create Web Service"
7. Wait for deployment
8. Get your URL

---

## Update Frontend with Backend URL

After deploying backend, update frontend:

Edit `frontend/src/services/stock.service.ts`:

```typescript
private apiUrl = 'https://your-backend-url.herokuapp.com/api/stocks';
```

Commit and push:
```bash
git add .
git commit -m "Update API URL"
git push
```

Vercel will auto-deploy!

---

## CORS Configuration

Ensure backend allows your frontend domain.

Edit `backend/src/main/java/com/stockpilot/StockPilotApplication.java`:

```java
.allowedOrigins(
    "http://localhost:4200", 
    "https://stockpilot-xyz.vercel.app",  // Your Vercel URL
    "https://*.vercel.app"
)
```

Redeploy backend after this change.