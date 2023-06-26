package com.cleverlance.test.project.repository.model;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "outbox")
@Getter
@Setter
public class OutboxEvent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "event_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private EventType eventType;

	@Column(name = "payload", nullable = false)
	private String payload;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private EventStatus status;

}


