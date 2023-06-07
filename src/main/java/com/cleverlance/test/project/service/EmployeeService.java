package com.cleverlance.test.project.service;

import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.repository.EmployeesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

	@Autowired
	private EmployeesRepository employeesRepository;

	public EmployeesRepository getEmployeesRepository() {
		return employeesRepository;
	}

	public void addEmployee(Employee e) {
		employeesRepository.save(e);
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
		}else return null;	
	}

	public Optional<Employee> deleteEmployeeByID(Long id) {
		Optional<Employee> employeeData = employeesRepository.findById(id);
		if (employeeData.isPresent()) {
			employeesRepository.deleteById(id);
			return employeeData;
		}else return null;
	}

}
