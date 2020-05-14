package com.example.reactive.mongodb.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
public class Company {

	@Id
	private String companyId;

	private List<Employee> empList;

	private List<Tweet> twtList;

	public Company() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Company(String companyId, List<Employee> empList, List<Tweet> twtList) {
		super();
		this.companyId = companyId;
		this.empList = empList;
		this.twtList = twtList;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public List<Employee> getEmpList() {
		return empList;
	}

	public void setEmpList(List<Employee> empList) {
		this.empList = empList;
	}

	public List<Tweet> getTwtList() {
		return twtList;
	}

	public void setTwtList(List<Tweet> twtList) {
		this.twtList = twtList;
	}

	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", empList=" + empList + ", twtList=" + twtList + ", getCompanyId()="
				+ getCompanyId() + ", getEmpList()=" + getEmpList() + ", getTwtList()=" + getTwtList() + "]";
	}

}
