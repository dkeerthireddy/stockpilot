# Deployment Guide

## Overview

StockPilot can be deployed to Vercel (frontend) while the backend runs separately. This guide covers both local development and production deployment.

## Prerequisites

### Required Software

1. **Node.js**: v18 or higher
2. **Java**: JDK 21
3. **Maven**: 3.6 or higher
4. **Git**: For version control
5. **Vercel CLI** (optional): `npm install -g vercel`

### Verify Installation

```bash
node --version    # Should be v18+
java --version    # Should be 21+
mvn --version     # Should be 3.6+
```

## Local Development

### Backend Setup

1. Navigate to backend directory:
```bash
cd stockpilot/backend
```

2. Install dependencies and run:
```bash
mvn clean install
mvn spring-boot:run
```

3. Backend will start on `http://localhost:8080`

4. Test health endpoint:
```bash
curl http://localhost:8080/api/stocks/health
```

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd stockpilot/frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start development server:
```bash
npm start
```

4. Frontend will start on `http://localhost:4200`

5. Open browser to `http://localhost:4200`

### Full Stack Local Development

Run both simultaneously in separate terminals:

**Terminal 1** (Backend):
```bash
cd backend && mvn spring-boot:run
```

**Terminal 2** (Frontend):
```bash
cd frontend && npm start
```

## Production Build

### Build Frontend

```bash
cd frontend
npm run build
```

This creates production-optimized files in `dist/frontend/browser/`

### Build Backend

```bash
cd backend
mvn clean package
```

This creates a JAR file in `target/stockpilot-backend-1.0.0.jar`

## Deployment to Vercel

### Option 1: Vercel Dashboard (Recommended)

1. **Create Vercel Account**
   - Go to [vercel.com](https://vercel.com)
   - Sign up with GitHub/GitLab/Bitbucket

2. **Import Project**
   - Click "New Project"
   - Import your Git repository
   - Vercel auto-detects the configuration

3. **Configure Build Settings**
   - Framework Preset: `Other`
   - Root Directory: `frontend`
   - Build Command: `npm run build`
   - Output Directory: `dist/frontend/browser`

4. **Environment Variables** (if needed)
   - Add any custom environment variables
   - For this project, none are required!

5. **Deploy**
   - Click "Deploy"
   - Vercel builds and deploys automatically
   - Get your live URL: `https://stockpilot-xyz.vercel.app`

### Option 2: Vercel CLI

```bash
# Install Vercel CLI
npm install -g vercel

# Login
vercel login

# Deploy from project root
cd stockpilot
vercel

# Follow prompts:
# - Setup and deploy? Yes
# - Which scope? Your account
# - Link to existing project? No
# - Project name? stockpilot
# - Directory? ./frontend
```

For production deployment:
```bash
vercel --prod
```

### Vercel Configuration

The `vercel.json` in the project root configures:

- Frontend served as static site
- API routes proxied (or configure backend separately)
- SPA routing handled correctly

## Backend Deployment Options

Since Vercel doesn't natively support Java Spring Boot, deploy backend separately:

### Option 1: Heroku (Free Tier Available)

1. Create Heroku account
2. Install Heroku CLI
3. Deploy:

```bash
cd backend
heroku create stockpilot-api
git init
git add .
git commit -m "Initial commit"
heroku git:remote -a stockpilot-api
git push heroku main
```

4. Update frontend API URL to Heroku URL

### Option 2: Railway.app

1. Sign up at [railway.app](https://railway.app)
2. Create new project from GitHub
3. Select backend directory
4. Railway auto-detects Spring Boot
5. Get your backend URL

### Option 3: Render.com

1. Sign up at [render.com](https://render.com)
2. New Web Service
3. Connect repository
4. Root directory: `backend`
5. Build command: `mvn clean install`
6. Start command: `java -jar target/stockpilot-backend-1.0.0.jar`

### Option 4: AWS Elastic Beanstalk

1. Package as JAR: `mvn clean package`
2. Upload to Elastic Beanstalk console
3. Configure environment
4. Deploy

### Option 5: Docker Container

```dockerfile
# Dockerfile for backend
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/stockpilot-backend-1.0.0.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Deploy to:
- Docker Hub
- Google Cloud Run
- AWS ECS
- Azure Container Instances

## Connecting Frontend to Backend

### Update API URL

Edit `frontend/src/services/stock.service.ts`:

```typescript
// Development
private apiUrl = 'http://localhost:8080/api/stocks';

// Production
private apiUrl = 'https://your-backend-url.herokuapp.com/api/stocks';
```

Or use environment files:

```typescript
// environment.prod.ts
export const environment = {
  production: true,
  apiUrl: 'https://stockpilot-api.herokuapp.com/api/stocks'
};
```

### CORS Configuration

Ensure backend allows your frontend domain:

```java
registry.addMapping("/api/**")
    .allowedOrigins("https://stockpilot-xyz.vercel.app")
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
```

## Environment Variables

### Frontend

Create `.env` files (not committed to Git):

```env
# .env.development
VITE_API_URL=http://localhost:8080

# .env.production
VITE_API_URL=https://your-backend.com
```

### Backend

Set via platform-specific configuration:

- Heroku: `heroku config:set KEY=VALUE`
- Vercel: Dashboard → Settings → Environment Variables
- Railway: Dashboard → Variables

## Custom Domain (Optional)

### Vercel

1. Go to project settings
2. Add custom domain
3. Update DNS records as instructed
4. SSL automatically provisioned

### Backend

Configure custom domain through your backend hosting provider.

## Monitoring & Logs

### Vercel

- Dashboard → Project → Logs
- Real-time deployment logs
- Function logs (if using serverless)

### Backend

- Check platform-specific logs
- Heroku: `heroku logs --tail`
- Railway: Dashboard → Deployments → Logs
- Render: Dashboard → Logs

## CI/CD (Continuous Deployment)

### Automatic Deployments

Vercel automatically deploys on:
- Push to `main` branch (production)
- Pull requests (preview deployments)

### GitHub Actions (Optional)

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy

on:
  push:
    branches: [main]

jobs:
  deploy-frontend:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-node@v2
        with:
          node-version: '18'
      - run: cd frontend && npm ci && npm run build
      - uses: amondnet/vercel-action@v20
        with:
          vercel-token: ${{ secrets.VERCEL_TOKEN }}
          vercel-org-id: ${{ secrets.ORG_ID }}
          vercel-project-id: ${{ secrets.PROJECT_ID }}
```

## Performance Optimization

### Frontend

1. **Enable Gzip/Brotli**: Vercel handles automatically
2. **CDN**: Vercel's global CDN
3. **Cache Headers**: Configured in `vercel.json`
4. **Image Optimization**: Use Vercel Image Optimization

### Backend

1. **Connection Pooling**: Spring Boot default
2. **Caching**: In-memory cache already implemented
3. **Compression**: Enable in `application.properties`:

```properties
server.compression.enabled=true
server.compression.mime-types=application/json,application/xml,text/html,text/xml,text/plain
```

## Troubleshooting

### Frontend Not Loading

- Check browser console for errors
- Verify API URL is correct
- Check CORS configuration

### Backend Not Responding

- Check logs for errors
- Verify port 8080 is not blocked
- Test health endpoint: `/api/stocks/health`

### CORS Errors

- Update `allowedOrigins` in backend
- Clear browser cache
- Check preflight OPTIONS requests

### Build Failures

- Clear node_modules and reinstall
- Clear Maven cache: `mvn clean`
- Check Java/Node versions

## Security Considerations

1. **No Secrets in Code**: Use environment variables
2. **HTTPS Only**: Enforce SSL in production
3. **CORS**: Restrict to your domain only
4. **Rate Limiting**: Consider adding to backend
5. **API Keys**: Not needed, but secure if added

## Scaling

### Frontend (Vercel)
- Automatically scales
- Global CDN distribution
- No configuration needed

### Backend
- Start with single instance
- Add load balancer if needed
- Use managed services for scaling

## Cost Estimates

### Free Tier Options

**Vercel**:
- Personal: Free
- 100GB bandwidth/month
- Unlimited deployments

**Heroku**:
- Hobby: Free (with sleep mode)
- Must verify with credit card

**Railway**:
- $5 credit/month free
- Pay-as-you-go after

**Render**:
- Free tier available
- Automatic sleep after inactivity

### Estimated Monthly Costs

For moderate traffic (~10k requests/month):
- Frontend (Vercel): $0
- Backend (Render free tier): $0
- **Total: $0/month**

For production traffic:
- Frontend (Vercel Pro): $20/month
- Backend (Render): $7/month
- **Total: ~$27/month**

## Backup & Recovery

1. **Code**: Use Git (GitHub/GitLab)
2. **Deployments**: Vercel keeps history
3. **Data**: No persistent data (stateless app)
4. **Configuration**: Document all settings

## Next Steps After Deployment

1. Test all functionality in production
2. Monitor error rates and performance
3. Set up uptime monitoring (UptimeRobot, Pingdom)
4. Configure custom domain
5. Share your app!

## Support

For deployment issues:
- Vercel: [vercel.com/support](https://vercel.com/support)
- Spring Boot: [spring.io/guides](https://spring.io/guides)
- GitHub Issues: Report bugs in your repository