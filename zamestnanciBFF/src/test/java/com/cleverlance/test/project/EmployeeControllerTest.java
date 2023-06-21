package com.cleverlance.test.project;

import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.EmployeeMapper;
import com.cleverlance.test.project.controller.EmployeeController;
import com.cleverlance.test.project.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.EmployeeDTO;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {
	private EmployeeController underTest;
	@Mock
	private EmployeeService employeeService;
	@Mock
	private EmployeeMapper employeeMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		underTest = new EmployeeController(employeeService, employeeMapper);
	}

	private static Stream<EmployeeDTO> provideEmployeeDTOs() {
		EmployeeDTO employee1 = new EmployeeDTO();
		employee1.setId(1l);
		employee1.setName("John");
		employee1.setSurname("Doe");
		employee1.setDateBirth(LocalDate.now());
		employee1.setEmail("pokus@gmail.com");

		EmployeeDTO employee2 = new EmployeeDTO();
		employee2.setId(2l);
		employee2.setName("Jane");
		employee2.setSurname("Smith");
		employee2.setDateBirth(LocalDate.now());
		employee2.setEmail("lkjlskjglas@gmail.com");
		return Stream.of(employee1, employee2);
	}

	@ParameterizedTest
	@MethodSource("provideEmployeeDTOs")
	void testEmployeesPost(EmployeeDTO requestDTO) {
		// Act
		ResponseEntity<EmployeeDTO> response = underTest.employeesPost(requestDTO);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(requestDTO, response.getBody());
		verify(employeeService, times(1)).addEmployee(requestDTO);
	}

	@ParameterizedTest
	@MethodSource("provideEmployeeDTOs")
	void testEmployeesIdPut_ExistingEmployee(EmployeeDTO requestDTO) {
		// Arrange
		Employee existingEmployee = new Employee(10l, "John", "Doe", LocalDate.now(), "blals@gmail.com");
		ResponseEntity<Employee> responseMock = ResponseEntity.ok(existingEmployee);
		int employeeId = existingEmployee.getId().intValue();

		Mockito.when(employeeService.getEmployeeByID(employeeId)).thenReturn(responseMock);

		// Act
		ResponseEntity<EmployeeDTO> response = underTest.employeesIdPut(employeeId, requestDTO);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(requestDTO, response.getBody());
		verify(employeeService, times(1)).getEmployeeByID(employeeId);
		verify(employeeService, times(1)).updateEmployee(employeeId, requestDTO);
	}

	@ParameterizedTest
	@MethodSource("provideEmployeeDTOs")
	void testEmployeesIdPut_NonExistingEmployee(EmployeeDTO requestDTO) {
		// Arrange
		int employeeId = requestDTO.getId().intValue();
		ResponseEntity<Employee> notFound = ResponseEntity.notFound().build();
	    ResponseEntity<Employee> responseMock = notFound;
	    Mockito.when(employeeService.getEmployeeByID(employeeId)).thenReturn(responseMock);

		// Act and Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> underTest.employeesIdPut(employeeId, requestDTO));
		assertEquals("Employee with ID " + employeeId + " not found.", exception.getMessage());
		verify(employeeService, times(1)).getEmployeeByID(employeeId);
		Mockito.verify(employeeService, Mockito.never()).updateEmployee(Mockito.anyLong(),
				Mockito.any(EmployeeDTO.class));

	}

	@Test
	void testEmployeesIdDelete_ExistingEmployee() {
		// Arrange
		Employee existingEmployee = new Employee(10l, "John", "Doe", LocalDate.now(), "boaoa@gmail.com");
		EmployeeDTO existingEmployeeDTO = new EmployeeDTO();
		existingEmployeeDTO.setId(existingEmployee.getId());
		existingEmployeeDTO.setName(existingEmployee.getName());
		existingEmployeeDTO.setSurname(existingEmployee.getSurname());
		existingEmployeeDTO.setEmail(existingEmployee.getEmail());
		existingEmployeeDTO.setDateBirth(existingEmployee.getDateBirth());
		
		ResponseEntity<Employee> responseMock = ResponseEntity.ok(existingEmployee);

		Mockito.when(employeeService.getEmployeeByID(existingEmployee.getId())).thenReturn(responseMock);
		when(employeeMapper.employeeToDTO(existingEmployee)).thenReturn(existingEmployeeDTO);

		// Act
		ResponseEntity<EmployeeDTO> response = underTest.employeesIdDelete(existingEmployee.getId().intValue());

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(existingEmployeeDTO, response.getBody());
		verify(employeeService, times(1)).deleteEmployeeByID(existingEmployee.getId());
		verify(employeeMapper, times(1)).employeeToDTO(existingEmployee);
	}

	@Test
	void testEmployeesIdDelete_NonExistingEmployee() {
	    // Arrange
	    int employeeId = 10;
	    ResponseEntity<Employee> notFound = ResponseEntity.notFound().build();
	    ResponseEntity<Employee> responseMock = notFound;
	    Mockito.when(employeeService.getEmployeeByID(employeeId)).thenReturn(responseMock);

	    // Act and Assert
	    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
	            () -> underTest.employeesIdDelete(employeeId));
	    assertEquals("Employee with ID " + employeeId + " not found.", exception.getMessage());
	    verify(employeeService, never()).deleteEmployeeByID(employeeId);
	    verify(employeeMapper, never()).employeeToDTO(any(Employee.class));
	}

}
