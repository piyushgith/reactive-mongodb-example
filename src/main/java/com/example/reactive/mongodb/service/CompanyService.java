package com.example.reactive.mongodb.service;

import java.util.List;

import com.example.reactive.mongodb.model.Company;
import com.example.reactive.mongodb.model.Employee;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompanyService {

	public Flux<Company> findEmployeeFlux();

	public Flux<Company> insertCompanyRecords();

	public Mono<List<Employee>> findCompanyEmployeeList(String companyId);

}
