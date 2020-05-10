package com.example.reactive.mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.reactive.mongodb.model.Tweet;

import reactor.core.publisher.Flux;

@Repository
public interface TweetRepository extends ReactiveMongoRepository<Tweet, String> {
	// You can update the number for number of results
	Flux<Tweet> findFirst2ByOrderByCreatedAtDesc();

	Flux<Tweet> findFirst5ByOrderByCreatedAtDesc();

	// It will always return top last row by id
	Flux<Tweet> findTopByOrderByIdDesc();

}
