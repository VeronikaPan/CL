package com.cleverlance.test.project.service;

import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.service.kafka.JsonKafkaProducer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.openapitools.model.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

	private String urlAnotherRestApi = "http://localhost:8080/employees/";
	private JsonKafkaProducer kafkaProducer;
	private RestTemplate restTemplate;


	public EmployeeService(RestTemplate restTemplate, JsonKafkaProducer kafkaProducer) {
		this.kafkaProducer = kafkaProducer;
	}

	public List<Employee> getAllEmployees() {
		String url = urlAnotherRestApi;
		ResponseEntity<Employee[]> response = restTemplate.exchange(url, HttpMethod.GET, null, Employee[].class);
		Employee[] employees = response.getBody();
		if (employees != null) {
			return Arrays.asList(employees);
		} else {
			return Collections.emptyList();
		}
	}

	public Optional<Employee> getEmployeeByID(long id) {
		String url = urlAnotherRestApi + id;
		ResponseEntity<Employee> response = restTemplate.exchange(url, HttpMethod.GET, null, Employee.class);
		Employee employee = response.getBody();
		return Optional.ofNullable(employee);
	}

	public void addEmployee(EmployeeDTO e) {
		kafkaProducer.sendMessageAdd(e);
	}

	public void updateEmployee(long id, EmployeeDTO updatedEmployee) {
		updatedEmployee.setId( id);
		kafkaProducer.sendMessageUpdate(updatedEmployee);
	}

	public void deleteEmployeeByID(long id) {
		kafkaProducer.sendMessageDelete(id);
	}

}
