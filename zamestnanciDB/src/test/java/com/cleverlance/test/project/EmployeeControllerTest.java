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
import org.mockito.MockitoAnnotations;
import org.openapitools.model.EmployeeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {
    private EmployeeController employeeController;
    @Mock
    private EmployeeService employeeService;
    @Mock
    private EmployeeMapper employeeMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        employeeController = new EmployeeController(employeeService, employeeMapper);
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
        // Arrange
        Employee employee = new Employee(requestDTO.getId(), requestDTO.getName(), requestDTO.getSurname(),requestDTO.getDateBirth(), requestDTO.getEmail());

        when(employeeMapper.dtoToEmployee(requestDTO)).thenReturn(employee);        
        when(employeeMapper.employeeToDTO(employee)).thenReturn(requestDTO);

        // Act
        ResponseEntity<EmployeeDTO> response = employeeController.employeesPost(requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requestDTO, response.getBody());
        verify(employeeMapper, times(1)).dtoToEmployee(requestDTO);
        verify(employeeService, times(1)).addEmployee(employee);
        verify(employeeMapper, times(1)).employeeToDTO(employee);
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeDTOs")
    void testEmployeesIdPut_ExistingEmployee(EmployeeDTO requestDTO) {
        // Arrange
        int employeeId = requestDTO.getId().intValue();
        Employee existingEmployee = new Employee(requestDTO.getId(), "John", "Doe", LocalDate.now(), "blals@gmail.com");
        Employee updatedEmployee = new Employee(requestDTO.getId(), requestDTO.getName(), requestDTO.getSurname(), requestDTO.getDateBirth(), requestDTO.getEmail());

        when(employeeService.findEmployeeByID(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeMapper.dtoToEmployee(requestDTO)).thenReturn(updatedEmployee);
        when(employeeService.updateEmployee(updatedEmployee)).thenReturn(Optional.of(updatedEmployee));
        when(employeeMapper.employeeToDTO(updatedEmployee)).thenReturn(requestDTO);

        // Act
        ResponseEntity<EmployeeDTO> response = employeeController.employeesIdPut(employeeId, requestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requestDTO, response.getBody());
        verify(employeeService, times(1)).findEmployeeByID(employeeId);
        verify(employeeMapper, times(1)).dtoToEmployee(requestDTO);
        verify(employeeService, times(1)).updateEmployee(updatedEmployee);
        verify(employeeMapper, times(1)).employeeToDTO(updatedEmployee);
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeDTOs")
    void testEmployeesIdPut_NonExistingEmployee(EmployeeDTO requestDTO) {
        // Arrange
        int employeeId = requestDTO.getId().intValue();

        when(employeeService.findEmployeeByID(employeeId)).thenReturn(Optional.empty());

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                employeeController.employeesIdPut(employeeId, requestDTO));
        assertEquals("Employee with ID " + employeeId + " not found.", exception.getMessage());
        verify(employeeService, times(1)).findEmployeeByID(employeeId);
        verify(employeeMapper, never()).dtoToEmployee(any(EmployeeDTO.class));
        verify(employeeService, never()).updateEmployee(any(Employee.class));
        verify(employeeMapper, never()).employeeToDTO(any(Employee.class));
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeDTOs")
    void testEmployeesIdDelete_ExistingEmployee(EmployeeDTO requestDTO) {
        // Arrange
        int employeeId = requestDTO.getId().intValue();
        Employee existingEmployee = new Employee(requestDTO.getId(), "John", "Doe", LocalDate.now(), "boaoa@gmail.com");

        when(employeeService.deleteEmployeeByID(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeMapper.employeeToDTO(existingEmployee)).thenReturn(requestDTO);

        // Act
        ResponseEntity<EmployeeDTO> response = employeeController.employeesIdDelete(employeeId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requestDTO, response.getBody());
        verify(employeeService, times(1)).deleteEmployeeByID(employeeId);
        verify(employeeMapper, times(1)).employeeToDTO(existingEmployee);
    }

    @ParameterizedTest
    @MethodSource("provideEmployeeDTOs")
    void testEmployeesIdDelete_NonExistingEmployee(EmployeeDTO requestDTO) {
        // Arrange
        int employeeId = requestDTO.getId().intValue();

        when(employeeService.deleteEmployeeByID(employeeId)).thenReturn(Optional.empty());

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                employeeController.employeesIdDelete(employeeId));
        assertEquals("Employee with ID " + employeeId + " not found.", exception.getMessage());
        verify(employeeService, times(1)).deleteEmployeeByID(employeeId);
        verify(employeeMapper, never()).employeeToDTO(any(Employee.class));
    }
}
