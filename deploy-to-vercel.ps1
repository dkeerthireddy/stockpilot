# StockPilot Vercel Deployment Script

Write-Host "=================================="
Write-Host "  StockPilot Vercel Deployment"
Write-Host "=================================="
Write-Host ""

# Check if git is initialized
if (-not (Test-Path ".git")) {
    Write-Host "Step 1: Initializing Git repository..."
    git init
    git add .
    git commit -m "Initial commit - StockPilot"
    Write-Host "✓ Git repository initialized"
    Write-Host ""
}

# Check if Vercel CLI is installed
$vercelInstalled = Get-Command vercel -ErrorAction SilentlyContinue
if (-not $vercelInstalled) {
    Write-Host "Step 2: Installing Vercel CLI..."
    npm install -g vercel
    Write-Host "✓ Vercel CLI installed"
    Write-Host ""
}

Write-Host "Step 3: Ready to deploy!"
Write-Host ""
Write-Host "Run one of these commands:"
Write-Host ""
Write-Host "  vercel           # Deploy to preview"
Write-Host "  vercel --prod    # Deploy to production"
Write-Host ""
Write-Host "Note: You'll need to:"
Write-Host "  1. Login to Vercel (vercel login)"
Write-Host "  2. Set root directory to 'frontend' when prompted"
Write-Host ""
Write-Host "=================================="