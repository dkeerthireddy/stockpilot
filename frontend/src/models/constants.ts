export const METRIC_EXPLANATIONS: { [key: string]: string } = {
  'P/E Ratio': 'Price-to-Earnings ratio. Shows how much investors pay for each dollar of earnings. Lower may indicate undervaluation.',
  'EPS': 'Earnings Per Share. The portion of profit allocated to each share. Higher is generally better.',
  'Market Cap': 'Total market value of all outstanding shares. Indicates company size.',
  'Beta': 'Measures volatility relative to the market. Beta > 1 means more volatile than market.',
  'Dividend Yield': 'Annual dividend payment as a percentage of stock price. Higher means more income.',
  'Volatility': 'Measure of price fluctuations. Higher volatility means higher risk.',
  'Max Drawdown': 'Largest peak-to-trough decline. Shows worst-case loss scenario.',
  '52-Week High': 'Highest price in the past year. Useful for identifying trends.',
  '52-Week Low': 'Lowest price in the past year. Useful for identifying support levels.',
};

export const RISK_COLORS: { [key: string]: string } = {
  'LOW': 'text-green-600 bg-green-50',
  'MEDIUM': 'text-yellow-600 bg-yellow-50',
  'HIGH': 'text-red-600 bg-red-50',
};

export const CHART_RANGES = [
  { label: '1D', value: '1d' },
  { label: '5D', value: '5d' },
  { label: '1M', value: '1mo' },
  { label: '6M', value: '6mo' },
  { label: '1Y', value: '1y' },
  { label: '5Y', value: '5y' },
];
