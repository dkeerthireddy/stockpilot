export function formatCurrency(value: number | null | undefined): string {
  if (value == null) return '$0.00';
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(value);
}

export function formatPercent(value: number | null | undefined): string {
  if (value == null) return '0.00%';
  const sign = value >= 0 ? '+' : '';
  return ${sign}%;
}

export function formatLargeNumber(value: number | null | undefined): string {
  if (value == null) return 'N/A';
  
  if (value >= 1_000_000_000_000) {
    return $T;
  } else if (value >= 1_000_000_000) {
    return $B;
  } else if (value >= 1_000_000) {
    return $M;
  } else if (value >= 1_000) {
    return $K;
  }
  return formatCurrency(value);
}

export function formatVolume(value: number | null | undefined): string {
  if (value == null) return 'N/A';
  
  if (value >= 1_000_000_000) {
    return ${(value / 1_000_000_000).toFixed(2)}B;
  } else if (value >= 1_000_000) {
    return ${(value / 1_000_000).toFixed(2)}M;
  } else if (value >= 1_000) {
    return ${(value / 1_000).toFixed(2)}K;
  }
  return value.toString();
}

export function getChangeColor(change: number | null | undefined): string {
  if (change == null || change === 0) return 'text-gray-600';
  return change > 0 ? 'text-green-600' : 'text-red-600';
}

export function getChangeIcon(change: number | null | undefined): string {
  if (change == null || change === 0) return '→';
  return change > 0 ? '↑' : '↓';
}

export function formatDate(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  });
}

export function formatDateTime(dateString: string): string {
  const date = new Date(dateString);
  return date.toLocaleString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}
