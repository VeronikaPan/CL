//package com.cleverlance.test.project;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.sql.Date;
//import java.util.Optional;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import com.cleverlance.test.project.repository.EmployeesRepository;
//import com.cleverlance.test.project.repository.model.Employee;
//import com.cleverlance.test.project.service.EmployeeService;
//
//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
//public class EmployeeServiceTest {
//
//	@Mock
//	private EmployeesRepository employeeRepository;
//	private EmployeeService underTest;
//	private AutoCloseable autoCloseable;
//
//	@BeforeEach
//	void setUp() {
//		autoCloseable = MockitoAnnotations.openMocks(this);
//		employeeRepository = mock(EmployeesRepository.class);
//		underTest = new EmployeeService(employeeRepository);
//	}
//
//	@AfterEach
//	void tearDown() throws Exception {
//		autoCloseable.close();
//	}
//
//	@Test
//	public void canAddEmployee() {
//		// given
//		Employee e = new Employee(null, "Josef", "Novak", Date.valueOf("2023-06-01"), "novak@gmail.com");
//
//		// when
//		underTest.addEmployee(e);
//
//		// then
//		ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);
//		verify(employeeRepository).save(employeeArgumentCaptor.capture());
//
//		Employee capturedEmployee = employeeArgumentCaptor.getValue();
//		assertThat(capturedEmployee).isEqualTo(e);
//	}
//
//	@Test
//	public void canGetAllEmployees() {
//		// when
//		underTest.getAllEmployees();
//		// then
//		verify(employeeRepository).findAll();
//	}
//
//	@Test
//	public void canGetEmployeeByID() {
//		// given
//		Long employeeId = 123L;
//
//		// when
//		underTest.getEmployeeByID(employeeId);
//
//		// then
//		ArgumentCaptor<Long> idArgumentCaptor = ArgumentCaptor.forClass(Long.class);
//		verify(employeeRepository).findById(idArgumentCaptor.capture());
//
//		Long capturedId = idArgumentCaptor.getValue();
//		assertThat(capturedId).isEqualTo(employeeId);
//	}
//
//	@Test
//	public void canUpdateEmployee() {
//		// given
//		long employeeId = 123L;
//		Employee updatedEmployee = new Employee(employeeId, "Pepa", "Upraveny", Date.valueOf("2023-06-01"),
//				"novak@gmail.com");
//		Optional<Employee> employeeData = Optional
//				.of(new Employee(employeeId, "Josef", "Novak", Date.valueOf("2023-06-01"), "novak@gmail.com"));
//
//		when(employeeRepository.findById(employeeId)).thenReturn(employeeData);
//
//		// when
//		Optional<Employee> result = underTest.updateEmployee(updatedEmployee);
//
//		// then
//		verify(employeeRepository).save(updatedEmployee);
//		assertThat(result).isEqualTo(employeeData);
//	}
//
//	@Test
//	public void canDeleteEmployeeByID() {
//		// given
//		long employeeId = 123L;
//		Optional<Employee> employeeData = Optional
//				.of(new Employee(employeeId, "Josef", "Novak", Date.valueOf("2023-06-01"), "novak@gmail.com"));
//
//		when(employeeRepository.findById(employeeId)).thenReturn(employeeData);
//
//		// when
//		Optional<Employee> result = underTest.deleteEmployeeByID(employeeId);
//
//		// then
//		verify(employeeRepository).deleteById(employeeId);
//		assertThat(result).isEqualTo(employeeData);
//	}
//}
