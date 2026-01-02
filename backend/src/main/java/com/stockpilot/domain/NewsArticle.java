package com.stockpilot.domain;

import java.time.LocalDateTime;

public class NewsArticle {
    private String title;
    private String summary;
    private String url;
    private String source;
    private LocalDateTime publishedAt;
    private String imageUrl;

    public NewsArticle() {}

    public NewsArticle(String title, String summary, String url) {
        this.title = title;
        this.summary = summary;
        this.url = url;
        this.publishedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
