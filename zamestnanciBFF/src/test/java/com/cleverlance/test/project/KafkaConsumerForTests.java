package com.cleverlance.test.project;

import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.openapitools.model.EmployeeDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.cleverlance.test.project.service.kafka.JsonKafkaProducer;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
@Component
public class KafkaConsumerForTests {

    private CountDownLatch latch = new CountDownLatch(1);

    private EmployeeDTO employee;
    private int id;
    
	@KafkaListener(topics = "${spring.kafka.topic-add.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeAdd(EmployeeDTO e) {
		log.info(String.format("Message for adding recieved by embedded Kafka -> %s", e.toString()));
		employee = e;
        latch.countDown();
	}

	@KafkaListener(topics = "${spring.kafka.topic-update.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeUpdate(EmployeeDTO e) {
		log.info(String.format("Message update received by embedded Kafka -> %s", e.toString()));
		employee = e;
        latch.countDown();
	}

	@KafkaListener(topics = "${spring.kafka.topic-delete.name}", groupId = "${spring.kafka.consumer.group-id}")
	public void consumeDelete(int id) {
		log.info(String.format("Message for delete received by embedded Kafka -> %s", id));
		this.id = id;
        latch.countDown();
	}

    public CountDownLatch getLatch() {
        return latch;
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

    public EmployeeDTO getEmployee() {
        return this.employee;
    }
    
    public int getId() {
    	return this.id;
    }

}

