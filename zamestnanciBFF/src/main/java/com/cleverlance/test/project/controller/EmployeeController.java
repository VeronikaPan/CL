package com.cleverlance.test.project.controller;

import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.EmployeeMapper;
import com.cleverlance.test.project.service.EmployeeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openapitools.api.EmployeesApi;
//import org.openapitools.client.model.EmployeeDTO;
import org.openapitools.model.EmployeeDTO;
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
public class EmployeeController implements EmployeesApi {

	private final EmployeeService employeeService;
	private final EmployeeMapper employeeMapper;

	public EmployeeController(EmployeeService employeeService, EmployeeMapper employeeMapper) {
		this.employeeService = employeeService;
		this.employeeMapper = employeeMapper;
	}

	// vrati vsechny zamestnance
	@Override
	public ResponseEntity<List<EmployeeDTO>> employeesGet() {
		List<Employee> employees = employeeService.getAllEmployees();
		if (employees.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		List<org.openapitools.model.EmployeeDTO> employeeDTOs = employees.stream()
				.map(employee -> employeeMapper.employeeToDTO(employee)).collect(Collectors.toList());
		return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);
	}

	// vrati zamestnance podle ID
	@Override
	public ResponseEntity<EmployeeDTO> employeesIdGet(Integer id) {
		ResponseEntity<Employee> existingEmployee = employeeService.getEmployeeByID(id);
		if (existingEmployee.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			throw new IllegalArgumentException("Employee with ID " + id + " not found.");
		}
		if (existingEmployee.getBody() != null) {
			EmployeeDTO employeeDTO = employeeMapper.employeeToDTO(existingEmployee.getBody());
			return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// prida zamestnance
	@Override
	public ResponseEntity<EmployeeDTO> employeesPost(EmployeeDTO employeeDTO) {
		employeeService.addEmployee(employeeDTO);
		return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
	}

	// upravi zamestnance
	@Override
	public ResponseEntity<EmployeeDTO> employeesIdPut(Integer id, EmployeeDTO updatedEmployee) {
		ResponseEntity<Employee> existingEmployee = employeeService.getEmployeeByID(id);
		if (existingEmployee.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			throw new IllegalArgumentException("Employee with ID " + id + " not found.");
		}

		updatedEmployee.setId(existingEmployee.getBody().getId()); // Set the ID of the existing employee

		employeeService.updateEmployee(id, updatedEmployee);
		return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
	}

	// smaze zamestnance
	@Override
	public ResponseEntity<EmployeeDTO> employeesIdDelete(Integer id) {
		ResponseEntity<Employee> existingEmployee = employeeService.getEmployeeByID(id);
		if (existingEmployee.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
			throw new IllegalArgumentException("Employee with ID " + id + " not found.");
		}

		employeeService.deleteEmployeeByID(id);
		EmployeeDTO deletedEmployeeDTO = employeeMapper.employeeToDTO(existingEmployee.getBody());
		return new ResponseEntity<>(deletedEmployeeDTO, HttpStatus.OK);

	}
}
