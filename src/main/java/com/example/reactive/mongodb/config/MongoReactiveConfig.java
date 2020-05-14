package com.example.reactive.mongodb.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.model.Tweet;
import com.example.reactive.mongodb.repository.EmployeeRepository;
import com.example.reactive.mongodb.repository.TweetRepository;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import reactor.core.publisher.Flux;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.example.reactive.mongodb.repository")
public class MongoReactiveConfig extends AbstractReactiveMongoConfiguration {

	@Autowired
	private TweetRepository tweetRepository;

	@Override
	public MongoClient reactiveMongoClient() {
		return MongoClients.create();
	}

	@Override
	protected String getDatabaseName() {
		return "test";
	}

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
	}

	@Bean
	CommandLineRunner init(EmployeeRepository employeeRepository) {
		return init -> {

			List<Employee> list = new ArrayList<>();
			list.add(new Employee(UUID.randomUUID().toString(), "Ram", new Date()));
			list.add(new Employee(UUID.randomUUID().toString(), "Sita", new Date()));
			list.add(new Employee(UUID.randomUUID().toString(), "Luxman", new Date()));
			list.add(new Employee(UUID.randomUUID().toString(), "Hanuman", new Date()));

			// employeeRepository.saveAll(list);
			list.forEach(x -> {
				// System.out.println(x.toString());
				// employeeRepository.save(x).subscribe();
			});

			employeeRepository.deleteAll().thenMany(Flux.just(list).flatMap(employeeRepository::saveAll))
					.thenMany(employeeRepository.findAll())
					.subscribe(data -> System.out.println("****** \t" + data.toString()));

			List<Tweet> tweetList = new ArrayList<>();
			tweetList
					.addAll(Stream
							.of(new Tweet(UUID.randomUUID().toString(), "Hello World !!"),
									new Tweet(UUID.randomUUID().toString(), "Spring is awesome."),
									new Tweet(UUID.randomUUID().toString(), "I love Anime !!"),
									new Tweet(UUID.randomUUID().toString(), "I am learning it."))
							.collect(Collectors.toList()));

			tweetRepository.deleteAll().thenMany(Flux.just(tweetList).flatMap(tweetRepository::saveAll))
					.thenMany(tweetRepository.findAll())
					.subscribe(data -> System.out.println("###### \t" + data.toString()));

		};

	}

}
