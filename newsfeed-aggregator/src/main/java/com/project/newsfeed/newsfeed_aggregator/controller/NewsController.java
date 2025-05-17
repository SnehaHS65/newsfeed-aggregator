package com.project.newsfeed.newsfeed_aggregator.controller;

import com.project.newsfeed.newsfeed_aggregator.service.DeferredNewsStorage;
import com.project.newsfeed.newsfeed_aggregator.service.NewsApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsApiService newsApiService;

    @Autowired
    private DeferredNewsStorage deferredNewsStorage;

    @GetMapping
    public String getNewsByTopic(@RequestParam String topic) {
        return newsApiService.fetchNews(topic);
    }

    // ✅ Deferred News: /news/deferred
    @GetMapping("/deferred")
    public Map<String, String> getDeferredNews() {
        return deferredNewsStorage.getAll();
    }
}
