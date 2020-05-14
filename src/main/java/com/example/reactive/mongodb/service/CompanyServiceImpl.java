package com.example.reactive.mongodb.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.reactive.mongodb.model.Company;
import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.model.Tweet;
import com.example.reactive.mongodb.repository.CompanyRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public Flux<Company> findEmployeeFlux() {
		return null;
	}

	@Override
	public Flux<Company> insertCompanyRecords() {
		return companyRepository.deleteAll().thenMany(companyRepository.saveAll(getCompanyList()))
				.thenMany(companyRepository.findAll());
	}

	@Override
	public Mono<List<Employee>> findCompanyEmployeeList(String companyId) {
		// You can do return object from map as well
		// flatmap is tricky so stick with Mono as much possible. Below line works fine
		// return companyRepository.findById(companyId).map(comp -> { return comp.getEmpList();});
		return companyRepository.findById(companyId).map(comp -> {
			return comp.getEmpList() != null ? comp.getEmpList() : Collections.emptyList();
		});
	}

	public static List<Company> getCompanyList() {
		List<Employee> list = new ArrayList<>();
		list.add(new Employee(UUID.randomUUID().toString(), "Ram", new Date()));
		list.add(new Employee(UUID.randomUUID().toString(), "Sita", new Date()));
		list.add(new Employee(UUID.randomUUID().toString(), "Luxman", new Date()));
		list.add(new Employee(UUID.randomUUID().toString(), "Hanuman", new Date()));

		List<Tweet> tweetList = new ArrayList<>();
		tweetList
				.addAll(Stream
						.of(new Tweet(UUID.randomUUID().toString(), "Hello World !!"),
								new Tweet(UUID.randomUUID().toString(), "Spring is awesome."),
								new Tweet(UUID.randomUUID().toString(), "I love Anime !!"),
								new Tweet(UUID.randomUUID().toString(), "I am learning it."))
						.collect(Collectors.toList()));

		List<Company> companyList = new ArrayList<>();

		companyList.add(new Company(UUID.randomUUID().toString(), list, null));
		companyList.add(new Company(UUID.randomUUID().toString(), list, tweetList));
		companyList.add(new Company(UUID.randomUUID().toString(), null, tweetList));
		companyList.add(new Company(UUID.randomUUID().toString(), list, tweetList));

		return companyList;
	}

}
