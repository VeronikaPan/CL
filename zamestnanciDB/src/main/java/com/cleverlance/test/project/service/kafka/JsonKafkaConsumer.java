package com.cleverlance.test.project.service.kafka;

import org.openapitools.model.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cleverlance.test.project.controller.EmployeeController;
import com.cleverlance.test.project.repository.model.Employee;

@Service
public class JsonKafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonKafkaConsumer.class);
	EmployeeController employeeController;

	public JsonKafkaConsumer(EmployeeController controller) {
		this.employeeController = controller;
	}

	@KafkaListener(topics = "${spring.kafka.topic-add.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeAdd(EmployeeDTO user) {
		LOGGER.info(String.format("Json message for adding recieved -> %s", user.toString()));
		employeeController.employeesPost(user);
	}

	@KafkaListener(topics = "${spring.kafka.topic-update.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeUpdate(EmployeeDTO user) {
		LOGGER.info(String.format("Json message update received -> %s", user.toString()));
		employeeController.employeesIdPut(user.getId().intValue(), user);
	}

	@KafkaListener(topics = "${spring.kafka.topic-delete.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeDelete(int id) {
		LOGGER.info(String.format("Message for delete received -> %s", id));
		employeeController.employeesIdDelete(id);
	}
}
