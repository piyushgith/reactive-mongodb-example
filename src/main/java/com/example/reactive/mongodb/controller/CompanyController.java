package com.example.reactive.mongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.reactive.mongodb.model.Company;
import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.repository.CompanyRepository;
import com.example.reactive.mongodb.service.CompanyService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	private CompanyRepository companyRepository;

	@GetMapping("/all")
	public Flux<Company> findAllCompany() {
		return companyRepository.findAll();
	}

	@GetMapping("/insert")
	public Flux<Company> insertRecords() {
		return companyService.insertCompanyRecords();
	}

	@GetMapping("/employee/{companyid}")
	public Mono<List<Employee>> findCompanyEmployee(@PathVariable String companyid) {
		return companyService.findCompanyEmployeeList(companyid);
	}

}
