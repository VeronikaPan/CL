package com.example.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openapitools.client.model.EmployeeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiParam;

@RestController
public class EmployeeController {

	private final EmployeeService employeeService;
	private final EmployeeMapper employeeMapper;

	public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
		this.employeeService = employeeService;
		this.employeeMapper = employeeMapper;
	}

	// vrati vsechny zamestnance
	@GetMapping("/employees")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		List<Employee> employees = employeeService.getAllEmployees();
		if (employees.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		List<EmployeeDTO> employeeDTOs = employees.stream().map(employee -> employeeMapper.employeeToDTO(employee))
				.collect(Collectors.toList());
		return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
	}

	// vrati zamestnance podle id
	@GetMapping("/employees/{id}")
	public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("id") long id) {
		Optional<Employee> employeeData = employeeService.getEmployeeByID(id);
		if (employeeData.isPresent()) {
			EmployeeDTO employeeDTO = employeeMapper.employeeToDTO(employeeData.get());
			return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// prida zamestnance
	@PostMapping("/")
	public Employee addEmployee(@RequestBody EmployeeDTO employeeDTO) {
		Employee employee = employeeMapper.dtoToEmployee(employeeDTO);
		employeeService.addEmployee(employee);
		return employee;
	}

	// upravi zamestnance
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable("id") long id,
			@RequestBody EmployeeDTO updatedEmployeeDTO) {
		Employee updatedEmployee = employeeMapper.dtoToEmployee(updatedEmployeeDTO);
		Optional<Employee> employeeData = employeeService.updateEmployee(updatedEmployee);
		if (employeeData.isPresent()) {
			EmployeeDTO employeeDTO = employeeMapper.employeeToDTO(employeeData.get());
			return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
		} else {
			throw new IllegalArgumentException("Employee with ID " + id + " not found.");
		}
	}

	// smaze zamestnance
	@DeleteMapping("/{id}")
	public ResponseEntity<EmployeeDTO> deleteEmployee(
			@ApiParam(value = "testId", required = true) @PathVariable long id) {
		Optional<Employee> employeeData = employeeService.deleteEmployeeByID(id);
		if (employeeData.isPresent()) {
			EmployeeDTO employeeDTO = employeeMapper.employeeToDTO(employeeData.get());
			return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
		} else {
			throw new IllegalArgumentException("Employee with ID " + id + " not found.");
		}
	}

}
