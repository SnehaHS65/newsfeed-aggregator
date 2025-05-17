package com.project.newsfeed.newsfeed_aggregator.filter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;

import com.project.newsfeed.newsfeed_aggregator.model.DeferredRequest;


@Component
public class RateLimitFilter implements Filter {

    @Autowired
    private CacheManager cacheManager;

    private final Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1)); // 5 tokens per minute
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private Bucket resolveBucket(String ip) {
        return bucketCache.computeIfAbsent(ip, k -> createNewBucket());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String ip = httpRequest.getRemoteAddr();
        Bucket bucket = resolveBucket(ip);

        if (bucket.tryConsume(1)) {
            chain.doFilter(request, response); // proceed
        }
        else {
            String topic = httpRequest.getParameter("topic");
            Cache cache = cacheManager.getCache("overflowRequests");
            if (topic != null && cache != null) {
                String cacheKey = ip + "_" + System.currentTimeMillis();
                System.out.println(cacheKey);
                cache.put(cacheKey, new DeferredRequest(topic));
            }

            HttpServletResponse httpResp = (HttpServletResponse) response;
            httpResp.setStatus(429);
            httpResp.setContentType("application/json");
            httpResp.setCharacterEncoding("UTF-8");

// 🛠️ Manually add CORS header to the error response
            httpResp.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");

            httpResp.getWriter().write("{\"error\":\"⚠️ Rate limit exceeded. Request cached for deferred processing.\"}");


        }

    }
}

