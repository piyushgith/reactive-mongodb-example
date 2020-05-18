package com.example.reactive.mongodb.repository;

import com.example.reactive.mongodb.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User,String> {

    Mono<User> findByUsername(String username);

    //Mono<User> findById(String username);
}
