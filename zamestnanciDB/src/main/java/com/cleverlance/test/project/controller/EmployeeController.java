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

	//vrati vsechny zamestnance
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
		Optional<Employee> employeeData = employeeService.getEmployeeByID(id);
		if (employeeData.isPresent()) {
			EmployeeDTO employeeDTO = employeeMapper.employeeToDTO(employeeData.get());
			return new ResponseEntity<>(employeeDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// prida zamestnance
	@Override
	public ResponseEntity<EmployeeDTO> employeesPost(EmployeeDTO employeeDTO) {
	    Employee employee = employeeMapper.dtoToEmployee(employeeDTO);
	    employeeService.addEmployee(employee);
	    EmployeeDTO createdEmployeeDTO = employeeMapper.employeeToDTO(employee);
	    return new ResponseEntity<>(createdEmployeeDTO, HttpStatus.OK);
	}

	// upravi zamestnance
	@Override
	public ResponseEntity<EmployeeDTO> employeesIdPut(Integer id, EmployeeDTO employeeDTO) {
	    Employee existingEmployee = employeeService.getEmployeeByID(id)
	            .orElseThrow(() -> new IllegalArgumentException("Employee with ID " + id + " not found."));

	    Employee updatedEmployee = employeeMapper.dtoToEmployee(employeeDTO);
	    updatedEmployee.setId(existingEmployee.getId()); // Set the ID of the existing employee

	    Optional<Employee> updatedEmployeeData = employeeService.updateEmployee(updatedEmployee);
	    if (updatedEmployeeData.isPresent()) {
	        EmployeeDTO updatedEmployeeDTO = employeeMapper.employeeToDTO(updatedEmployeeData.get());
	        return new ResponseEntity<>(updatedEmployeeDTO, HttpStatus.OK);
	    } else {
	        throw new IllegalArgumentException("Employee with ID " + id + " not found.");
	    }
	}

	// smaze zamestnance
	@Override
	public ResponseEntity<EmployeeDTO> employeesIdDelete(Integer id) {
	    Optional<Employee> employeeData = employeeService.deleteEmployeeByID(id);
	    if (employeeData.isPresent()) {
	        EmployeeDTO deletedEmployeeDTO = employeeMapper.employeeToDTO(employeeData.get());
	        return new ResponseEntity<>(deletedEmployeeDTO, HttpStatus.OK);
	    } else {
	        throw new IllegalArgumentException("Employee with ID " + id + " not found.");
	    }
	}
}
