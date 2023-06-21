package com.cleverlance.test.project.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.openapitools.model.EmployeeDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.service.kafka.JsonKafkaProducer;

@Service
public class EmployeeService {
	
	private String dbRestApiUrl;
	private JsonKafkaProducer kafkaProducer;
	private RestTemplate restTemplate;
	
	public EmployeeService(@Value("${DBrestApiUrl}") String dbRestApiUrl, RestTemplate restTemplate, JsonKafkaProducer kafkaProducer) {
	    this.kafkaProducer = kafkaProducer;
	    this.restTemplate = restTemplate;
	    this.dbRestApiUrl = dbRestApiUrl;
	}


	public List<Employee> getAllEmployees() {
		ResponseEntity<Employee[]> response = restTemplate.exchange(dbRestApiUrl, HttpMethod.GET, null,
				Employee[].class);
		Employee[] employees = response.getBody();
		if (employees != null) {
			return Arrays.asList(employees);
		} else {
			return Collections.emptyList();
		}
	}

	public ResponseEntity<Employee> getEmployeeByID(long id) {
		String url = dbRestApiUrl + id;
		ResponseEntity<Employee> response = restTemplate.exchange(url, HttpMethod.GET, null, Employee.class);
		return response;
	}

	public void addEmployee(EmployeeDTO e) {
		kafkaProducer.sendMessageAdd(e);
	}

	public void updateEmployee(long id, EmployeeDTO updatedEmployee) {
		updatedEmployee.setId(id);
		kafkaProducer.sendMessageUpdate(updatedEmployee);
	}

	public void deleteEmployeeByID(long id) {
		kafkaProducer.sendMessageDelete(id);
	}

}
