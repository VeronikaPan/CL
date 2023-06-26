package com.cleverlance.test.project.service;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.cleverlance.test.project.repository.IEmployeesRepository;
import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.repository.model.EventType;
import com.cleverlance.test.project.repository.model.OutboxEvent;
import com.cleverlance.test.project.service.kafka.JsonKafkaProducer;
import com.cleverlance.test.project.service.kafka.KafkaAvailabilityChecker;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {

	private IEmployeesRepository employeesRepository;
	private OutboxEventService outboxService;
	private JsonKafkaProducer kafkaProducer;
	private OutboxEventPublisher outboxEventPublisher;

	public EmployeeService(IEmployeesRepository employeeRepository, JsonKafkaProducer kafkaProducer,
			OutboxEventService outservice, OutboxEventPublisher outboxEventPublisher) {
		this.employeesRepository = employeeRepository;
		this.kafkaProducer = kafkaProducer;
		this.outboxService = outservice;
		this.outboxEventPublisher = outboxEventPublisher;
	}

	public IEmployeesRepository getEmployeesRepository() {
		return employeesRepository;
	}

	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		employeesRepository.findAll().forEach(employees::add);
		return employees;
	}

	public Optional<Employee> findEmployeeByID(long id) {
		Optional<Employee> employeeData = employeesRepository.findById(id);
		return employeeData;
	}

	public void addEmployee(Employee employee) {
		log.info("EmployeeService>addEmployee - Add employee in EmployeeService called");
		if (findEmployeeByID(employee.getId()).isEmpty()) {
			OutboxEvent outboxEvent = null;
			try {
				outboxEvent = addEmployeeWithEvent(employee);
			} catch (JsonProcessingException e1) {
				log.info("EmployeeService>addEmployee - Error caused by serialization to json for outboxService");
				e1.printStackTrace();
			}
			sendMessageToKafka(employee, outboxEvent);
		} else {
			log.info("Employee already exists");
		}
	}

	@Transactional
	public OutboxEvent addEmployeeWithEvent(Employee employee) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String employeeJson = objectMapper.writeValueAsString(employee);
		employeesRepository.save(employee);
		return outboxService.createAndSaveEvent(EventType.ADD, employeeJson);
	}

	public void sendMessageToKafka(Employee employee, OutboxEvent outboxEvent) {
		if (outboxEvent != null)
			try {
				log.info("EmployeeService>sendMessageToKafka");
				outboxEventPublisher.publishOutboxEvents();
			} catch (JsonMappingException e1) {
				log.info("Error caused by deserialization JSON to employee");
				e1.printStackTrace();
			} catch (JsonProcessingException e1) {
				log.info("Error caused by deserialization JSON to employee");
				e1.printStackTrace();
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
	}

	public Optional<Employee> updateEmployee(Employee updatedEmployee) {
		log.info("EmployeeService>updateEmployee - update employee in EmployeeService called");
		if (!findEmployeeByID(updatedEmployee.getId()).isEmpty()) {
			OutboxEvent outboxEvent = null;
			try {
				outboxEvent = updateEmployeeWithEvent(updatedEmployee);
			} catch (JsonProcessingException e1) {
				log.info("EmployeeService>addEmployee - Error caused by serialization to json for outboxService");
				e1.printStackTrace();
			}
			sendMessageToKafka(updatedEmployee, outboxEvent);
			return Optional.ofNullable(updatedEmployee);
		} else {
			log.info("Employee does not exists");
			return null;
		}
	}

	@Transactional
	public OutboxEvent updateEmployeeWithEvent(Employee employee) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String employeeJson = objectMapper.writeValueAsString(employee);
		employeesRepository.save(employee);
		return outboxService.createAndSaveEvent(EventType.UPDATE, employeeJson);
	}

	public Optional<Employee> deleteEmployeeByID(long id) {
		log.info("EmployeeService>deleteEmployee - delete employee in EmployeeService called");
		Optional<Employee> emplToDelete = findEmployeeByID(id);
		if (!emplToDelete.isEmpty()) {
			OutboxEvent outboxEvent = null;
			try {
				outboxEvent = deleteEmployeeWithEvent(emplToDelete.get());
			} catch (JsonProcessingException e1) {
				log.info("EmployeeService>addEmployee - Error caused by serialization to json for outboxService");
				e1.printStackTrace();
			}
			sendMessageToKafka(emplToDelete.get(), outboxEvent);
			return Optional.ofNullable(emplToDelete.get());
		} else {
			log.info("Employee does not exists");
			return null;
		}
	}
	
	@Transactional
	public OutboxEvent deleteEmployeeWithEvent(Employee employee) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String employeeJson = objectMapper.writeValueAsString(employee);
		employeesRepository.delete(employee);
		return outboxService.createAndSaveEvent(EventType.DELETE, employeeJson);
	}

}
