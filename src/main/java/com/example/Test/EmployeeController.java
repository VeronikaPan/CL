package com.example.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class EmployeeController {

	@Autowired
	EmployeesRepository employeesRepository;	
	
	//vrati vsechny zamestnance
	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> getAllEmployees() {
			List<Employee> employees = new ArrayList<Employee>();
			employeesRepository.findAll().forEach(employees::add);		
			if (employees.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(employees, HttpStatus.OK);
	}
	
	//vrati zamestnance podle id
	@GetMapping("/employees/{id}")
	public ResponseEntity<Optional<Employee>> getEmployeeById(@PathVariable("id") long id) {
		Optional<Employee> employeeData = employeesRepository.findById(id);
		if (employeeData!=null) {
			return new ResponseEntity<>(employeeData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	//prida zamestnance
	@PostMapping("/")
	public Employee addEmployee(@RequestBody Employee employee) {
		employeesRepository.save(employee);
		//employees.put(zamestnanec.getId(), employee);
		return employee;
	}

	//upravi zamestnance
	@PutMapping("/{id}")
	public Employee updateEmployee(@RequestBody Employee updatedEmployee) {
		Optional<Employee> employeeData = employeesRepository.findById(updatedEmployee.getId());
	    if (employeeData.isPresent()) {
	    	employeesRepository.save(updatedEmployee);
	        return updatedEmployee;
	    } else {
	        throw new IllegalArgumentException("Employee with ID " + updatedEmployee.getId() + " not found.");
	    }
	}

	//smaze zamestnance
	@DeleteMapping("/{id}")
	public void deleteEmployee(
			@ApiParam(value = "testId", required = true) @PathVariable Long id) {
		employeesRepository.deleteById(id);		 
	}

}
