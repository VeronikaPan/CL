package com.cleverlance.test.project.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cleverlance.test.project.repository.model.Employee;
import com.cleverlance.test.project.repository.model.EventType;
import com.cleverlance.test.project.repository.model.OutboxEvent;
import com.cleverlance.test.project.service.kafka.JsonKafkaProducer;
import com.cleverlance.test.project.service.kafka.KafkaAvailabilityChecker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@EnableAsync
public class OutboxEventPublisher {

	private final OutboxEventService outboxEventService;
	private final JsonKafkaProducer kafkaProducer;
	@Autowired
	private KafkaAvailabilityChecker kafkaAvailabilityChecker;

	public OutboxEventPublisher(OutboxEventService outboxEventService, JsonKafkaProducer kafkaProducer) {
		this.outboxEventService = outboxEventService;
		this.kafkaProducer = kafkaProducer;
	}

	@Async()
	@Scheduled(fixedDelay = 10000)
	public void publishOutboxEvents() throws JsonMappingException, JsonProcessingException, InterruptedException {
		if (kafkaAvailabilityChecker.isKafkaBrokerAvailable()) {
			log.info("Kafka broker available");
			List<OutboxEvent> pendingEvents = outboxEventService.getAllPendingEvents();
			log.info("cout of pendingEvents " + pendingEvents.size());
			if (pendingEvents.isEmpty()) {
				log.info("No pending events in the outbox. Stopping the scheduled task.");
				return; 
			}
			for (OutboxEvent event : pendingEvents) {
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.registerModule(new JavaTimeModule());
				Employee employee = objectMapper.readValue(event.getPayload(), Employee.class);
				sendMessageToKafka(employee, event, event.getEventType());
			}
		} else {
			log.info("Kafka broker still unavailable");
		}
	}

	public void sendMessageToKafka(Employee employee, OutboxEvent outboxEvent, EventType eventType) {
		log.info("OutboxEventPublisher>sendMessageToKafka - Sending  message to Kafka");
		CompletableFuture<SendResult<String, Employee>> future = null;
		try {
			switch (eventType) {
		    case ADD:
		    	future = kafkaProducer.sendAddResponse(employee);
		        break;
		    case UPDATE:
		    	future = kafkaProducer.sendUpdateResponse(employee);
		        break;
		    case DELETE:
		    	future = kafkaProducer.sendDeleteResponse(employee);
		        break;
		    default:
		        break;
		}
			future.thenAccept(result -> {
				log.info(
						"OutboxEventPublisher>sendMessageToKafka - message sent to Kafka successfully - Employee ID: "
								+ employee.getId());
				outboxEventService.deleteEvent(outboxEvent.getId());
			}).exceptionally(ex -> {
				log.error("Failed to send message to Kafka, keep the message in outbox and try again", ex);
				return null;
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
