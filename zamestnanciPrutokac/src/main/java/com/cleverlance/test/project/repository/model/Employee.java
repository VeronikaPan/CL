package com.cleverlance.test.project.repository.model;

import java.sql.Date;

public class Employee {

	private Long id;
	private String name;
	private String surname;
	private String email;
	private Date dateBirth;

	public Employee() {

	}

	public Employee(Long id, String name, String surname, Date dateBirth, String email) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.dateBirth = dateBirth;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Date getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", surname=" + surname + ", dateBirth=" + dateBirth
				+ ", email=" + email + "]";
	}

}
