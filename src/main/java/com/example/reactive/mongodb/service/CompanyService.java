package com.example.reactive.mongodb.service;

import com.example.reactive.mongodb.model.Company;

import reactor.core.publisher.Flux;

public interface CompanyService {

	public Flux<Company> findEmployeeFlux();

	public Flux<Company> insertCompanyRecords();

}
