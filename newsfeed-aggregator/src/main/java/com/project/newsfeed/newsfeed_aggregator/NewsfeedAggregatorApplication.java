package com.project.newsfeed.newsfeed_aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NewsfeedAggregatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsfeedAggregatorApplication.class, args);
	}

}
