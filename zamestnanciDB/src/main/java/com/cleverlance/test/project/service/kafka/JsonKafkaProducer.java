package com.cleverlance.test.project.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import com.cleverlance.test.project.repository.model.Employee;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class JsonKafkaProducer {

	@Value("${spring.kafka.topic-add-response.name}")
	private String addResponsetopicName;

	@Value("${spring.kafka.topic-delete-response.name}")
	private String deleteResponsetopicName;

	@Value("${spring.kafka.topic-update-response.name}")
	private String updateResponsetopicName;

	@Autowired
	private KafkaTemplate<String, Employee> kafkaTemplate;

	public JsonKafkaProducer(KafkaTemplate<String, Employee> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public CompletableFuture<SendResult<String, Employee>> sendAddResponse(Employee employee)
			throws InterruptedException {
		log.info(String.format("Trying to send ADD message to Kafka -> %s", employee.toString()));
		Message<Employee> message = MessageBuilder.withPayload(employee)
				.setHeader(KafkaHeaders.TOPIC, addResponsetopicName).build();

		CompletableFuture<SendResult<String, Employee>> future = new CompletableFuture<>();
		kafkaTemplate.send(message).addCallback(result -> future.complete(result), ex -> {
			log.error("Failed to send ADD message to Kafka: " + ex.getMessage());
			future.completeExceptionally(ex);
		});
		return future;

	}

	public CompletableFuture<SendResult<String, Employee>> sendDeleteResponse(Employee employee)
			throws InterruptedException {
		log.info(String.format("Trying to send Delete message to Kafka -> %s", employee.toString()));
		Message<Employee> message = MessageBuilder.withPayload(employee)
				.setHeader(KafkaHeaders.TOPIC, deleteResponsetopicName).build();

		CompletableFuture<SendResult<String, Employee>> future = new CompletableFuture<>();
		kafkaTemplate.send(message).addCallback(result -> future.complete(result), ex -> {
			log.error("Failed to send delete message to Kafka: " + ex.getMessage());
			future.completeExceptionally(ex);
		});
		return future;
	}

	public CompletableFuture<SendResult<String, Employee>> sendUpdateResponse(Employee employee)
			throws InterruptedException {
		log.info(String.format("Trying to send update message to Kafka -> %s", employee.toString()));
		Message<Employee> message = MessageBuilder.withPayload(employee)
				.setHeader(KafkaHeaders.TOPIC, updateResponsetopicName).build();

		CompletableFuture<SendResult<String, Employee>> future = new CompletableFuture<>();
		kafkaTemplate.send(message).addCallback(result -> future.complete(result), ex -> {
			log.error("Failed to send update message to Kafka: " + ex.getMessage());
			future.completeExceptionally(ex);
		});
		return future;
	}
}
