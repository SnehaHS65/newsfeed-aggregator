package com.project.newsfeed.newsfeed_aggregator.scheduler;

import com.project.newsfeed.newsfeed_aggregator.service.DeferredNewsStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.project.newsfeed.newsfeed_aggregator.service.NewsApiService;
import com.project.newsfeed.newsfeed_aggregator.model.DeferredRequest;

import java.util.Objects;

@Component
public class DeferredRequestProcessor {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private NewsApiService newsApiService;

    @Autowired
    private DeferredNewsStorage deferredNewsStorage;


    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void processCachedRequests() {
        Cache cache = cacheManager.getCache("overflowRequests");
        if (cache == null) return;

        System.out.println("🔄 Processing cached overflow requests...");

        // Access native Caffeine cache
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                (com.github.benmanes.caffeine.cache.Cache<Object, Object>) Objects.requireNonNull(cache).getNativeCache();

        nativeCache.asMap().forEach((key, value) -> {
            try {
                DeferredRequest request = (DeferredRequest) value;
                String topic = request.getTopic();
                System.out.println("🔍 Fetching news for topic: " + topic);

                // Call the News API
                String newsResponse = newsApiService.fetchNews(topic);

                // ✅ Save the result in memory for /news/deferred
                deferredNewsStorage.save(key.toString() + " (Topic: " + topic + ")", newsResponse);


                System.out.println("📰 Cached result saved for key: " + key);

            } catch (Exception e) {
                System.err.println("❌ Error processing cached request for key: " + key + " — " + e.getMessage());
            } finally {
                nativeCache.invalidate(key); // remove after processing
            }
        });
    }

}
