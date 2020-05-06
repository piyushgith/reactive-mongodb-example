package com.example.reactive.mongodb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.repository.EmployeeRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public Mono<Employee> findEmployeebyID(String id) {
		Mono<Employee> mono = employeeRepository.findById(id);
		return mono;
	}

	@Override
	public Flux<Employee> findAllEmployee() {
		Flux<Employee> flux = employeeRepository.findAll();
		return flux;
	}

}
