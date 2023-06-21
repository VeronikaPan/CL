package com.cleverlance.test.project;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.repository.EmployeesRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

@DataJpaTest
public class EmployeeRepositoryTest {

	// zadne vlastni dotazy k otestovani, testuji jen defaultni volani z
	// JpaRepository

	@Autowired
	private EmployeesRepository underTest;

	@AfterEach
	void tearDown() {
		underTest.deleteAll();
	}

	@Test
	void itShouldCheckIfEmployeeExistsID() {
		// given
		Employee employee = new Employee();
		employee.setId(null);
		employee.setDateBirth(LocalDate.now());
		employee.setName("Pepa");
		employee.setSurname("Upraveny");
		employee.setEmail("josefNovak@gmail.com");
		
		underTest.save(employee);

		// when
		Optional<Employee> expectedEmployee = underTest.findById(employee.getId());

		// then
		assertThat(expectedEmployee.isPresent()).isTrue();
	}

	@Test
	void itShouldCheckIfEmployeeDoesNotExistID() {
		// given
		long id = 11;

		// when
		Optional<Employee> expectedEmployee = underTest.findById(id);

		// then
		assertThat(expectedEmployee.isPresent()).isFalse();
	}

	@Test
	void itShouldCheckIfEmployeeWasDeleted() {
		// given
		Employee employee = new Employee();
		employee.setDateBirth(LocalDate.now());
		employee.setName("Pepa");
		employee.setSurname("Upraveny");
		employee.setEmail("josefNovak@gmail.com");
		
		underTest.save(employee);

		// when
		underTest.deleteById(employee.getId());
		Optional<Employee> expectedEmployee = underTest.findById(employee.getId());

		// then
		assertThat(expectedEmployee.isPresent()).isFalse();
	}
	
	@Test
	void itShouldCheckIfEmployeeWasUpdated() {
		Employee employee = new Employee();
		employee.setDateBirth(LocalDate.now());
		employee.setName("Josef");
		employee.setSurname("Novak");
		employee.setEmail("josefNovak@gmail.com");
		underTest.save(employee);
		
		Employee employee2 = new Employee();
		employee2.setId(employee.getId());
		employee2.setDateBirth(LocalDate.now());
		employee2.setName("Pepa");
		employee2.setSurname("Upraveny");
		employee2.setEmail("novak@gmail.com");

		// when
		Employee updatedEmployee = employee2;
		underTest.save(updatedEmployee);
		Optional<Employee> expectedEmployee = underTest.findById(employee.getId());

		// then
		assertThat(expectedEmployee.isPresent()).isTrue();
		assertThat(expectedEmployee.get().getName()).isEqualTo("Pepa");
		assertThat(expectedEmployee.get().getSurname()).isEqualTo("Upraveny");
	}
}
