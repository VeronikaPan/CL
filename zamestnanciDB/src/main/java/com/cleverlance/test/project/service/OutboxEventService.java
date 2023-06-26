package com.cleverlance.test.project.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cleverlance.test.project.repository.IOutboxEventRepository;
import com.cleverlance.test.project.repository.model.EventStatus;
import com.cleverlance.test.project.repository.model.EventType;
import com.cleverlance.test.project.repository.model.OutboxEvent;
import com.cleverlance.test.project.service.kafka.JsonKafkaProducer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Service
@Slf4j
public class OutboxEventService {

	@Autowired
	private IOutboxEventRepository outboxRepository;

	public List<OutboxEvent> getAllEvents() {
		return outboxRepository.findAll();
	}

	public List<OutboxEvent> getAllPendingEvents() {
		return outboxRepository.findAllByStatus(EventStatus.PENDING);
	}

	public OutboxEvent getEventById(Long id) {
		return outboxRepository.getById(id);
	}

	public OutboxEvent createAndSaveEvent(EventType eventType, String payload) {
		OutboxEvent event = new OutboxEvent();
		event.setCreatedAt(LocalDateTime.now());
		event.setPayload(payload);
		event.setEventType(eventType);
		event.setStatus(EventStatus.PENDING);

		return outboxRepository.save(event);
	}

	public void deleteEvent(Long id) {
		outboxRepository.deleteById(id);
		log.info("outbox event id" + id + " deleted");
	}

	public void updateStatusEvent(Long id, EventStatus status) {
		OutboxEvent event = outboxRepository.getById(id);
		event.setStatus(status);
		outboxRepository.save(event);
	}

	private void deleteAllProcessedEvents() {
		List<OutboxEvent> successfulEvents = outboxRepository.findAllByStatus(EventStatus.PROCESSED);

		for (OutboxEvent event : successfulEvents) {
			CompletableFuture.runAsync(() -> outboxRepository.deleteById(event.getId()));
		}
	}

	public void markEventAsProcessed(Long id) {
		log.info("Outbox event id "+ id + " marked as processed");
		updateStatusEvent(id, EventStatus.PROCESSED);
	}
}