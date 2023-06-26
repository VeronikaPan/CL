package com.cleverlance.test.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cleverlance.test.project.repository.model.EventStatus;
import com.cleverlance.test.project.repository.model.OutboxEvent;

public interface IOutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

	List<OutboxEvent> findAllByStatus(EventStatus status);
}
