package com.project.newsfeed.newsfeed_aggregator.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DeferredNewsStorage {

    private final Map<String, String> processedNews = Collections.synchronizedMap(new LinkedHashMap<>());

    public void save(String key, String result) {
        processedNews.put(key, result);
    }

    public Map<String, String> getAll() {
        return processedNews;
    }

    public void clear() {
        processedNews.clear();
    }
}
