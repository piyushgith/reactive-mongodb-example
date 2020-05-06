package com.example.reactive.mongodb.service;

import com.example.reactive.mongodb.model.Employee;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {

	Mono<Employee> findEmployeebyID(String id);

	Flux<Employee> findAllEmployee();

}
