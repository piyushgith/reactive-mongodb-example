package com.example.reactive.mongodb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.service.EmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/all")
	public Flux<Employee> findAllEmployeeFlux() {
		return employeeService.findAllEmployee();
	}

	@GetMapping("/{id}")
	public Mono<Employee> findEmployeebyIDMono(@PathVariable String id) {
		return employeeService.findEmployeebyID(id);
	}

}
