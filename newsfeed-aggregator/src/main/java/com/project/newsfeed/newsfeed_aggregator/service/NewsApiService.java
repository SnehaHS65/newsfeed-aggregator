package com.project.newsfeed.newsfeed_aggregator.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NewsApiService {

    private static final String API_KEY = "d4f6304b08ea4b87beb8484bb43f362c";
    private static final String BASE_URL = "https://newsapi.org/v2/everything?q=%s&pageSize=5&apiKey=%s";


    public String fetchNews(String topic) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(BASE_URL, topic, API_KEY);
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            return "❌ Failed to fetch news for topic: " + topic + " — " + e.getMessage();
        }
    }
}
