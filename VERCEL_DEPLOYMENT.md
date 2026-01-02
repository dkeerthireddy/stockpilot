# 🚀 StockPilot Vercel Deployment Guide

## Prerequisites

✓ Node.js 18+ installed
✓ Git installed
✓ Vercel account (free - sign up at vercel.com)

## Method 1: Deploy via Vercel Dashboard (Easiest)

### Step 1: Push to GitHub

```bash
cd stockpilot

# Initialize git if not already done
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - StockPilot"

# Create a new repository on GitHub, then:
git remote add origin https://github.com/YOUR_USERNAME/stockpilot.git
git branch -M main
git push -u origin main
```

### Step 2: Deploy on Vercel

1. Go to https://vercel.com
2. Sign in (use GitHub account for easier integration)
3. Click "New Project"
4. Click "Import Git Repository"
5. Select your `stockpilot` repository
6. Configure settings:
   - **Framework Preset**: Other
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist/frontend/browser`
   - **Install Command**: `npm install`
7. Click "Deploy"
8. Wait 2-3 minutes
9. ✅ Your site is live!

### Step 3: Get Your URL

After deployment completes, you'll see:
- **Production URL**: `https://stockpilot-xyz.vercel.app`
- **Deployment Dashboard**: Monitor builds and logs

### Step 4: Configure Backend URL

Once deployed, update the API URL:

1. Deploy your backend to Heroku/Railway/Render (see BACKEND_DEPLOYMENT.md)
2. Update `frontend/src/services/stock.service.ts`:
   ```typescript
   private apiUrl = 'https://your-backend-url.herokuapp.com/api/stocks';
   ```
3. Commit and push changes
4. Vercel auto-deploys on push!

---

## Method 2: Deploy via Vercel CLI

### Step 1: Install Vercel CLI

```bash
npm install -g vercel
```

### Step 2: Login

```bash
vercel login
```

This opens your browser. Sign in with:
- GitHub (recommended)
- GitLab
- Bitbucket
- Email

### Step 3: Deploy

```bash
cd stockpilot
vercel
```

**Prompts and Answers:**

```
? Set up and deploy "~/stockpilot"? Y
? Which scope? (Use arrow keys)
  > Your Account Name
? Link to existing project? N
? What's your project's name? stockpilot
? In which directory is your code located? ./frontend
? Want to override the settings? N
```

### Step 4: Production Deployment

After testing the preview:

```bash
vercel --prod
```

✅ Your production site is live!

---

## Method 3: One-Command Deploy Script

### Windows (PowerShell):

```powershell
cd stockpilot
.\deploy-to-vercel.ps1
```

Then run:
```bash
vercel login
vercel --prod
```

### Mac/Linux:

```bash
cd stockpilot
chmod +x deploy-to-vercel.sh
./deploy-to-vercel.sh
```

---

## Environment Variables (Optional)

If you need to set environment variables:

### Via Dashboard:
1. Go to your project on Vercel
2. Settings → Environment Variables
3. Add variables (e.g., `API_URL`)

### Via CLI:
```bash
vercel env add API_URL production
```

---

## Custom Domain (Optional)

1. Go to your project on Vercel
2. Settings → Domains
3. Add your custom domain
4. Update DNS records as instructed
5. SSL certificate auto-provisioned

---

## Troubleshooting

### Build Fails

**Error: "Cannot find module '@angular/core'"**

Solution: Ensure `package.json` is in `frontend/` directory and root directory is set to `frontend`

**Error: "Output directory not found"**

Solution: Set output directory to `dist/frontend/browser`

### Site Loads But API Fails

**Error: CORS or 404 on API calls**

Solution:
1. Deploy backend separately
2. Update API URL in `stock.service.ts`
3. Redeploy frontend

### How to Redeploy

**After making changes:**

```bash
git add .
git commit -m "Update description"
git push
```

Vercel auto-deploys on push!

Or use CLI:
```bash
vercel --prod
```

---

## Backend Deployment

StockPilot backend needs separate deployment. Options:

### Heroku (Free Tier)

```bash
cd backend
heroku create stockpilot-api
git init
git add .
git commit -m "Initial commit"
heroku git:remote -a stockpilot-api
git push heroku main
```

### Railway

1. Go to railway.app
2. New Project → Deploy from GitHub
3. Select repository
4. Root directory: `backend`
5. Railway auto-detects Spring Boot
6. Get your backend URL

### Render

1. Go to render.com
2. New → Web Service
3. Connect repository
4. Root directory: `backend`
5. Build command: `mvn clean install`
6. Start command: `java -jar target/stockpilot-backend-1.0.0.jar`

---

## Monitoring & Logs

### View Deployment Logs

**Dashboard**: Vercel project → Deployments → View logs

**CLI**:
```bash
vercel logs
```

### View Runtime Logs

```bash
vercel logs --follow
```

---

## Cost

**Vercel (Frontend):**
- ✓ Hobby Plan: FREE
- ✓ 100GB bandwidth/month
- ✓ Unlimited deployments
- ✓ Automatic HTTPS
- ✓ Global CDN

**Backend Options:**
- Heroku Hobby: FREE (sleeps after 30min inactivity)
- Railway: $5 free credit/month
- Render: FREE tier available

**Total Monthly Cost**: $0 for moderate usage!

---

## Support

**Vercel Docs**: https://vercel.com/docs
**Community**: https://github.com/vercel/vercel/discussions
**StockPilot Issues**: See TROUBLESHOOTING.md

---

## Success Checklist

✓ Frontend deployed to Vercel
✓ Custom domain configured (optional)
✓ Backend deployed separately
✓ API URL updated in frontend
✓ CORS configured in backend
✓ Site loads and works
✓ Stock search functions
✓ Charts display
✓ Watchlist works

---

## Next Steps

1. Share your deployed URL!
2. Add to your portfolio
3. Monitor analytics in Vercel dashboard
4. Set up custom domain (optional)
5. Configure alerts for downtime

**Your StockPilot is now live! 🎉**