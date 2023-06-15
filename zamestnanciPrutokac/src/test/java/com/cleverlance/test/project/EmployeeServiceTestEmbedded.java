package com.cleverlance.test.project;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openapitools.model.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import com.cleverlance.test.project.service.EmployeeService;
import com.cleverlance.test.project.service.kafka.JsonKafkaProducer;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class EmployeeServiceTestEmbedded {
	
	@Autowired
	private KafkaConsumerForTests consumer;
	@Autowired
	private JsonKafkaProducer producer;	
	@Mock
	private RestTemplate restTemplate;
	private EmployeeService underTest;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		underTest = new EmployeeService(restTemplate, producer);
		consumer.resetLatch();
	}

	private EmployeeDTO getSampleEmployeeDTO() {
		EmployeeDTO employee = new EmployeeDTO();
		employee.setId(10L);
		employee.setName("Josef");
		employee.setSurname("Novak");
		employee.setDateBirth(LocalDate.now());
		employee.setEmail("novak@gmail.com");
		return employee;
	}

	@Test
	public void testAddEmployeeMessageRecievedByKafka() throws Exception {
		EmployeeDTO employee = getSampleEmployeeDTO();
		underTest.addEmployee(employee);

		boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(messageConsumed);
		assertEquals(employee, consumer.getEmployee());
	}

	@Test
	public void testUpdateEmployeeMessageRecievedByKafka() throws Exception {
		EmployeeDTO employee = getSampleEmployeeDTO();
		underTest.updateEmployee(employee.getId(), employee);

		boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(messageConsumed);
		assertEquals(employee, consumer.getEmployee());
	}

	@Test
	public void testDeleteEmployeeMessageRecievedByKafka() throws Exception {
		EmployeeDTO employee = getSampleEmployeeDTO();
		underTest.deleteEmployeeByID(employee.getId());

		boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
		assertTrue(messageConsumed);
		assertEquals(employee.getId().intValue(), consumer.getId());
	}
}
