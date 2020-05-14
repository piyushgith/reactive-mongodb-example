package com.example.reactive.mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.reactive.mongodb.model.Company;

@Repository
public interface CompanyRepository extends ReactiveMongoRepository<Company, String> {

}
