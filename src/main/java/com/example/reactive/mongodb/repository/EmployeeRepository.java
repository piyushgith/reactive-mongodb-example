package com.example.reactive.mongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.example.reactive.mongodb.model.Employee;

@Repository
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

}