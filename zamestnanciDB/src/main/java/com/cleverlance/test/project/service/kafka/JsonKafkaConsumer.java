package com.cleverlance.test.project.service.kafka;

import java.util.Optional;

import org.openapitools.model.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cleverlance.test.project.EmployeeMapper;
import com.cleverlance.test.project.controller.EmployeeController;
import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JsonKafkaConsumer {

	EmployeeService employeeService;
	private final EmployeeMapper employeeMapper;

	public JsonKafkaConsumer(EmployeeService controller, EmployeeMapper employeeMapper) {
		this.employeeService = controller;
		this.employeeMapper = employeeMapper;
	}

	@KafkaListener(topics = "${spring.kafka.topic-add.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeAdd(EmployeeDTO employeeDTO) {
		log.info(String.format("Json message for adding recieved -> %s", employeeDTO.toString()));
		Employee employee = employeeMapper.dtoToEmployee(employeeDTO);
		employeeService.addEmployee(employee);
	}

	@KafkaListener(topics = "${spring.kafka.topic-update.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeUpdate(EmployeeDTO employeeDTO) {
		log.info(String.format("Json message update received -> %s", employeeDTO.toString()));
		Employee employee = employeeMapper.dtoToEmployee(employeeDTO);
		employeeService.updateEmployee(employee);
	}

	@KafkaListener(topics = "${spring.kafka.topic-delete.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeDelete(int id) {
		log.info(String.format("Message for delete received -> %s", id));
		employeeService.deleteEmployeeByID(id);
	}
}
