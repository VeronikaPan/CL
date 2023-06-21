package com.cleverlance.test.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cleverlance.test.project.repository.EmployeesRepository;
import com.cleverlance.test.project.repository.model.Employee;

@Service
public class EmployeeService {

	private EmployeesRepository employeesRepository;
	
	public EmployeeService(EmployeesRepository employeeRepository) {
		this.employeesRepository = employeeRepository;
	}

	public EmployeesRepository getEmployeesRepository() {
		return employeesRepository;
	}

	public void addEmployee(Employee e) {
		if(findEmployeeByID(e.getId()).isEmpty()) {
			employeesRepository.save(e);
		}
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

	public Optional<Employee> updateEmployee(Employee updatedEmployee) {
		Optional<Employee> employeeData = employeesRepository.findById(updatedEmployee.getId());
		if (employeeData.isPresent()) {
			employeesRepository.save(updatedEmployee);
			return employeeData;
		} else
			return null;
	}

	public Optional<Employee> deleteEmployeeByID(long id) {
		Optional<Employee> employeeData = employeesRepository.findById(id);
		if (employeeData.isPresent()) {
			employeesRepository.deleteById(id);
			return employeeData;
		} else
			return null;
	}

}
