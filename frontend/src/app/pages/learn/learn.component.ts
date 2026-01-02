import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

interface GlossaryTerm {
  term: string;
  definition: string;
  example?: string;
}

@Component({
  selector: 'app-learn',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './learn.component.html',
  styleUrls: ['./learn.component.css'],
})
export class LearnComponent {
  glossaryTerms: GlossaryTerm[] = [
    {
      term: 'Stock',
      definition: 'A share of ownership in a company. When you buy stock, you become a partial owner.',
      example: 'Buying 10 shares of Apple means you own a tiny piece of Apple Inc.',
    },
    {
      term: 'Price (Stock Price)',
      definition: 'The current market value of one share. This changes throughout the trading day based on supply and demand.',
    },
    {
      term: 'Market Cap',
      definition: 'Market Capitalization - the total value of all company shares. Calculated as share price × total shares outstanding.',
      example: 'If a company has 1 million shares at $50 each, market cap is $50 million.',
    },
    {
      term: 'P/E Ratio',
      definition: 'Price-to-Earnings ratio. Shows how much investors pay for each dollar of earnings. Lower can mean better value.',
      example: 'P/E of 20 means investors pay $20 for every $1 of annual earnings.',
    },
    {
      term: 'EPS',
      definition: 'Earnings Per Share - company profit divided by number of shares. Higher EPS generally indicates better profitability.',
    },
    {
      term: 'Dividend',
      definition: 'A portion of company profits paid to shareholders, usually quarterly. Not all companies pay dividends.',
    },
    {
      term: 'Dividend Yield',
      definition: 'Annual dividend payment as a percentage of stock price. Shows income return from dividends.',
      example: 'A $100 stock paying $3/year in dividends has a 3% yield.',
    },
    {
      term: 'Beta',
      definition: 'Measures stock volatility compared to the overall market. Beta > 1 means more volatile; < 1 means less volatile.',
      example: 'Beta of 1.5 means the stock typically moves 50% more than the market.',
    },
    {
      term: 'Volatility',
      definition: 'The degree of price fluctuation. High volatility means larger price swings and higher risk.',
    },
    {
      term: 'Bull Market',
      definition: 'A market condition where prices are rising or expected to rise. Opposite of bear market.',
    },
    {
      term: 'Bear Market',
      definition: 'A market condition where prices fall 20% or more from recent highs. Signals investor pessimism.',
    },
    {
      term: 'Portfolio',
      definition: 'A collection of investments owned by an individual or institution. Diversification reduces risk.',
    },
    {
      term: '52-Week High/Low',
      definition: 'The highest and lowest prices at which a stock has traded during the past year.',
    },
  ];

  beginnerMistakes = [
    {
      mistake: 'Investing without research',
      advice: 'Always understand what you\'re buying. Read about the company, industry, and risks.',
    },
    {
      mistake: 'Putting all money in one stock',
      advice: 'Diversify! Spread investments across different companies and sectors to reduce risk.',
    },
    {
      mistake: 'Trying to time the market',
      advice: 'Even experts struggle to predict short-term movements. Focus on long-term investing.',
    },
    {
      mistake: 'Emotional decision-making',
      advice: 'Don\'t panic sell or FOMO buy. Stick to your investment strategy and goals.',
    },
    {
      mistake: 'Ignoring fees and taxes',
      advice: 'Trading fees and capital gains taxes can eat into profits. Be mindful of costs.',
    },
    {
      mistake: 'Chasing hot tips',
      advice: 'Social media "get rich quick" tips are often misleading. Do your own research.',
    },
  ];

  chartReadingTips = [
    'Trend Lines: Connect highs or lows to identify price direction (uptrend, downtrend, sideways)',
    'Support & Resistance: Price levels where stocks historically bounce or stall',
    'Volume: Higher volume confirms price movements; low volume may signal weak trends',
    'Time Frames: Use different ranges (1D, 1M, 1Y) to see short-term vs long-term patterns',
    'Moving Averages: Smooth out price data to identify trends more easily',
  ];
}