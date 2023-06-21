package com.cleverlance.test.project.service.kafka;

import org.openapitools.model.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.cleverlance.test.project.repository.model.Employee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JsonKafkaProducer {
	
	@Autowired
	private KafkaTemplate<String, EmployeeDTO> kafkaTemplate;
	
	@Value("${spring.kafka.topic-add.name}")
	private String addEmployeeTopic;
	@Value("${spring.kafka.topic-update.name}")
	private String updateEmployeeTopic;
	@Value("${spring.kafka.topic-delete.name}")
	private String deleteEmployeeTopic;

	

	public void sendMessageAdd(EmployeeDTO employee) {
		log.info(String.format("Message for add sent -> %s", employee.toString()));
		Message<EmployeeDTO> message = MessageBuilder.withPayload(employee)
				.setHeader(KafkaHeaders.TOPIC, addEmployeeTopic).build();
		kafkaTemplate.send(message);
	}

	public void sendMessageUpdate(EmployeeDTO employee) {
		log.info(String.format("Message for update sent -> %s", employee.toString()));
		Message<EmployeeDTO> message = MessageBuilder.withPayload(employee)
				.setHeader(KafkaHeaders.TOPIC, updateEmployeeTopic).build();
		kafkaTemplate.send(message);
	}

	public void sendMessageDelete(long id) {
		log.info(String.format("Message sent -> %s", "delete employee id>" + id));
		Message<Long> message = MessageBuilder.withPayload(id).setHeader(KafkaHeaders.TOPIC, deleteEmployeeTopic)
				.build();
		kafkaTemplate.send(message);
	}
	
	
}
