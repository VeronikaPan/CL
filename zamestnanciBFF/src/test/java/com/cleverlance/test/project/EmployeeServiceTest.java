package com.cleverlance.test.project;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.openapitools.model.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmployeeServiceTest {

	@Autowired
	private KafkaConsumerForTests consumer;
	@Autowired
	private EmployeeService underTest;
	private static ClientAndServer mockServer;

	@BeforeEach
	void setup() {
		consumer.resetLatch();
	}

	@BeforeAll
	public static void startServer() {
		mockServer = startClientAndServer(1080);
	}

	@ParameterizedTest
	@MethodSource("employeeProvider")
	public void testAddEmployeeMessageRecievedByKafka(EmployeeDTO employee) throws Exception {
		underTest.addEmployee(employee);

		boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(messageConsumed);
		assertEquals(employee, consumer.getEmployee());
	}

	private static Stream<Arguments> employeeProvider() {
		return Stream.of(
				Arguments.of(createEmployee("Josef", "Novak")),
				Arguments.of(createEmployee("Anna", "Svobodova")), 
				Arguments.of(createEmployee("Petr", "Kral")));
	}

	private static EmployeeDTO createEmployee(String name, String surname) {
		EmployeeDTO employee = new EmployeeDTO();
		employee.setId(10L);
		employee.setName(name);
		employee.setSurname(surname);
		employee.setDateBirth(LocalDate.now());
		employee.setEmail(name.toLowerCase() + "@" + surname.toLowerCase() + ".com");
		return employee;
	}

	@ParameterizedTest
	@MethodSource("employeeProvider")
	public void testUpdateEmployeeMessageRecievedByKafka(EmployeeDTO employee) throws Exception {
		underTest.updateEmployee(employee.getId(), employee);

		boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(messageConsumed);
		assertEquals(employee, consumer.getEmployee());
	}

	@ParameterizedTest
	@MethodSource("employeeProvider")
	public void testDeleteEmployeeMessageRecievedByKafka(EmployeeDTO employee) throws Exception {
		underTest.deleteEmployeeByID(employee.getId());

		boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(messageConsumed);
		assertEquals(employee.getId().intValue(), consumer.getId());
	}

	@Test
	public void testGetAllEmployeesRequestToMockServer() throws JsonProcessingException {
		List<Employee> employees = new ArrayList<>();
		Employee employee1 = new Employee();
		employee1.setId(1l);
		employee1.setDateBirth(LocalDate.now());
		employee1.setName("Josef");
		employee1.setSurname("Novak");
		employee1.setEmail("josefNovak@gmail.com");
		employees.add(employee1);

		Employee employee2 = new Employee();
		employee2.setId(2l);
		employee2.setName("Karel");
		employee2.setSurname("Gott");
		employee2.setEmail("karel@gmail.com");
		employees.add(employee2);

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		String jsonEmployees = objectMapper.writeValueAsString(employees);

		MockServerClient mockServer = new MockServerClient("127.0.0.1", 1080);
		mockServer
				.when(request().withMethod("GET").withPath("/employees/")
						.withHeader("Accept", "application/json, application/*+json")
						.withHeader("User-Agent", "Java/17.0.7").withHeader("Host", "localhost:1080")
						.withHeader("Connection", "keep-alive").withHeader("Content-Length", "0"))
				.respond(response().withStatusCode(200)
						.withHeaders(new Header("Content-Type", "application/json; charset=utf-8"),
								new Header("Cache-Control", "public, max-age=86400"))
						.withBody(jsonEmployees).withDelay(TimeUnit.SECONDS, 1));

		List<Employee> resultEmployees = underTest.getAllEmployees();
		Assert.assertEquals(employees, resultEmployees);
	}

	@Test
	public void testGetEmployeeByIDRequestToMockServer() throws JsonProcessingException {
		Employee employee1 = new Employee();
		employee1.setId(1l);
		employee1.setDateBirth(LocalDate.now());
		employee1.setName("Josef");
		employee1.setSurname("Novak");
		employee1.setEmail("josefNovak@gmail.com");

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		String jsonEmployee = objectMapper.writeValueAsString(employee1);

		MockServerClient mockServer = new MockServerClient("127.0.0.1", 1080);
		mockServer
				.when(request().withMethod("GET").withPath("/employees/1")
						.withHeader("Accept", "application/json, application/*+json")
						.withHeader("User-Agent", "Java/17.0.7").withHeader("Host", "localhost:1080")
						.withHeader("Connection", "keep-alive").withHeader("Content-Length", "0"))
				.respond(response().withStatusCode(200)
						.withHeaders(new Header("Content-Type", "application/json; charset=utf-8"),
								new Header("Cache-Control", "public, max-age=86400"))
						.withBody(jsonEmployee).withDelay(TimeUnit.SECONDS, 1));

		ResponseEntity<Employee> response = underTest.getEmployeeByID(1l);
		assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(employee1, response.getBody());
	}

	@AfterAll
	public static void stopServer() {
		mockServer.stop();
	}
}
