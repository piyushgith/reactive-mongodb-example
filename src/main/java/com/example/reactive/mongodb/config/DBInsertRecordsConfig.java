package com.example.reactive.mongodb.config;

import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.model.Tweet;
import com.example.reactive.mongodb.model.User;
import com.example.reactive.mongodb.repository.EmployeeRepository;
import com.example.reactive.mongodb.repository.TweetRepository;
import com.example.reactive.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

            List<User> userList=new ArrayList<>();
            userList.addAll(Stream.of(new User("piyush", this.encode("prasad"), Arrays.asList("USER,ADMIN"))).collect(Collectors.toList()));

            //userRepository.deleteAll().thenMany(userRepository.saveAll(Stream.of(new User("piyush", this.encode("prasad"), Arrays.asList("USER,ADMIN"))).collect(Collectors.toList())));
            userRepository.deleteAll().thenMany(userRepository.saveAll(userList)).subscribe(data -> System.out.println("$$$$$$ \t" + data.toString()));
        };
    }


}
