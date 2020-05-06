package com.example.reactive.mongodb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.example.reactive.mongodb.model.Employee;
import com.example.reactive.mongodb.repository.EmployeeRepository;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class ReactiveMongodbExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveMongodbExampleApplication.class, args);
	}

	@Bean
	CommandLineRunner init(EmployeeRepository employeeRepository) {
		return init -> {

			List<Employee> list = new ArrayList<>();
			list.add(new Employee(UUID.randomUUID().toString(), "Ram", new Date()));
			list.add(new Employee(UUID.randomUUID().toString(), "Sita", new Date()));
			list.add(new Employee(UUID.randomUUID().toString(), "Luxman", new Date()));
			list.add(new Employee(UUID.randomUUID().toString(), "Hanuman", new Date()));

			// employeeRepository.saveAll(list);
			list.forEach(x -> {
				System.out.println(x.toString());
				employeeRepository.save(x);
			});
		};

	}

}
