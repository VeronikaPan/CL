package com.cleverlance.test.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cleverlance.test.project.repository.EmployeesRepository;
import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.service.kafka.KafkaProducer;

@Service
public class EmployeeService {

	private EmployeesRepository employeesRepository;
	//private KafkaProducer kafkaProducer;

//	public EmployeeService(EmployeesRepository employeeRepository, KafkaProducer kafkaProducer) {
//		this.employeesRepository = employeeRepository;
//		this.kafkaProducer = kafkaProducer;
//	}
	
	public EmployeeService(EmployeesRepository employeeRepository) {
		this.employeesRepository = employeeRepository;
	}

	public EmployeesRepository getEmployeesRepository() {
		return employeesRepository;
	}

	public void addEmployee(Employee e) {
		if(getEmployeeByID(e.getId()).isEmpty()) {
			employeesRepository.save(e);
		}else {
			//kafkaProducer.sendAddResponse("Zamestnanec se zadanym ID uz existuje");
		}
	}

	public List<Employee> getAllEmployees() {
		List<Employee> employees = new ArrayList<Employee>();
		employeesRepository.findAll().forEach(employees::add);
		return employees;
	}

	public Optional<Employee> getEmployeeByID(long id) {
		Optional<Employee> employeeData = employeesRepository.findById(id);
		return employeeData;
	}

	public Optional<Employee> updateEmployee(Employee updatedEmployee) {
		Optional<Employee> employeeData = employeesRepository.findById(updatedEmployee.getId());
		if (employeeData.isPresent()) {
			employeesRepository.save(updatedEmployee);
			return employeeData;
		} else
			//kafkaProducer.sendUpdateResponse("Zamestnanec se zadanym ID nebyl nalezen v DB");
			return null;
	}

	public Optional<Employee> deleteEmployeeByID(long id) {
		Optional<Employee> employeeData = employeesRepository.findById(id);
		if (employeeData.isPresent()) {
			employeesRepository.deleteById(id);
			return employeeData;
		} else
			//kafkaProducer.sendDeleteResponse("Zamestnanec se zadanym ID nebyl nalezen v DB");
			return null;
	}

}
