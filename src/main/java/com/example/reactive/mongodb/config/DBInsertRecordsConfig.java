package com.example.reactive.mongodb.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.reactive.mongodb.model.Authority;
import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.model.Tweet;
import com.example.reactive.mongodb.model.User;
import com.example.reactive.mongodb.repository.EmployeeRepository;
import com.example.reactive.mongodb.repository.TweetRepository;
import com.example.reactive.mongodb.repository.UserRepository;
import com.example.reactive.mongodb.security.AuthoritiesConstants;

import reactor.core.publisher.Flux;

@Configuration
public class DBInsertRecordsConfig {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    public String encode(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);
        String encodedPassword = encoder.encode(password);
        return encodedPassword;
    }

    @Bean
	CommandLineRunner init() {
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
									new Tweet(UUID.randomUUID().toString(), "Spring is awesome........"),
									new Tweet(UUID.randomUUID().toString(), "I love Anime !!"),
									new Tweet(UUID.randomUUID().toString(), "I am learning it."))
							.collect(Collectors.toList()));

			tweetRepository.deleteAll().thenMany(Flux.just(tweetList).flatMap(tweetRepository::saveAll))
					.thenMany(tweetRepository.findAll())
					.subscribe(data -> System.out.println("###### \t" + data.toString()));

			List<User> userList = new ArrayList<>();

			Set<Authority> authSet = new HashSet<>();
			Set<Authority> authSet1 = new HashSet<>();
			Set<Authority> authSet2 = new HashSet<>();

			authSet.add(new Authority(AuthoritiesConstants.ADMIN));
			authSet.add(new Authority(AuthoritiesConstants.USER));

			// authSet1.add(new Authority(AuthoritiesConstants.ANONYMOUS));
			authSet1.add(new Authority(AuthoritiesConstants.USER));
			authSet2.add(new Authority(AuthoritiesConstants.ADMIN));

			userList.add(new User(UUID.randomUUID().toString(),"piyush", this.encode("prasad"), authSet));
			userList.add(new User(UUID.randomUUID().toString(),"user", this.encode("password"), authSet1));
			userList.add(new User(UUID.randomUUID().toString(),"admin", this.encode("password"), authSet2));

			userRepository.deleteAll().thenMany(userRepository.saveAll(userList))
					.subscribe(data -> System.out.println("$$$$$$ \t" + data.toString()));
		};
	}


}
